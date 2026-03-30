package springboot.leavemanagementsystem.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.leavemanagementsystem.dto.ApplyLeaveRequest;
import springboot.leavemanagementsystem.dto.LeaveRequestResponse;
import springboot.leavemanagementsystem.entity.*;
import springboot.leavemanagementsystem.repository.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final HolidayRepository holidayRepository;

    public LeaveRequestService(
            LeaveRequestRepository leaveRequestRepository,
            UserRepository userRepository,
            LeaveTypeRepository leaveTypeRepository,
            LeaveBalanceRepository leaveBalanceRepository,
            HolidayRepository holidayRepository
    ) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.userRepository = userRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.holidayRepository = holidayRepository;
    }

    // APPLY FOR LEAVE
    @Transactional
    public void applyForLeave(String email, ApplyLeaveRequest request) {

        User employee = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (employee.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalStateException("Inactive employee cannot apply for leave");
        }

        LeaveType leaveType = leaveTypeRepository.findById(request.getLeaveTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Leave type not found"));

        if (leaveType.getStatus() != LeaveTypeStatus.ACTIVE) {
            throw new IllegalStateException("Leave type is not active");
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        if (request.getStartDate().getYear() != request.getEndDate().getYear()) {
            throw new IllegalArgumentException("Leave cannot span multiple years");
        }

        int numberOfDays = calculateWorkingDays(
                request.getStartDate(),
                request.getEndDate()
        );

        if (numberOfDays <= 0) {
            throw new IllegalStateException(
                    "Selected dates do not contain any working days"
            );
        }

        List<LeaveRequestStatus> activeStatuses =
                Arrays.asList(
                        LeaveRequestStatus.PENDING,
                        LeaveRequestStatus.APPROVED
                );

        List<LeaveRequest> existingRequests =
                leaveRequestRepository.findByEmployeeAndStatusIn(
                        employee,
                        activeStatuses
                );

        for (LeaveRequest existing : existingRequests) {

            boolean overlaps = !(
                    request.getEndDate().isBefore(existing.getStartDate()) ||
                            request.getStartDate().isAfter(existing.getEndDate())
            );

            if (overlaps) {
                throw new IllegalStateException(
                        "Leave dates overlap with existing leave request"
                );
            }
        }

        int year = request.getStartDate().getYear();

        LeaveBalance balance = leaveBalanceRepository
                .findByUserAndLeaveTypeAndYear(employee, leaveType, year)
                .orElseGet(() -> {

                    LeaveBalance newBalance = new LeaveBalance(
                            employee,
                            leaveType,
                            year,
                            leaveType.getMaxDaysPerYear()
                    );

                    return leaveBalanceRepository.save(newBalance);
                });

        if (balance.getRemainingDays() < numberOfDays) {
            throw new IllegalStateException("Insufficient leave balance");
        }

        LeaveRequest leaveRequest = new LeaveRequest(
                employee,
                leaveType,
                request.getStartDate(),
                request.getEndDate(),
                request.getReason()
        );

        leaveRequest.setNumberOfDays(numberOfDays);
        leaveRequest.setStatus(LeaveRequestStatus.PENDING);

        leaveRequestRepository.save(leaveRequest);
    }

    // VIEW EMPLOYEE LEAVE HISTORY
    public List<LeaveRequestResponse> getLeaveRequestsByEmployee(String email) {

        User employee = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<LeaveRequest> requests =
                leaveRequestRepository.findByEmployeeWithLeaveType(employee);

        return requests.stream()
                .map(this::mapToResponse)
                .toList();
    }

    // CANCEL LEAVE REQUEST
    @Transactional
    public void cancelLeaveRequest(String email, Long requestId) {

        User employee = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        LeaveRequest leaveRequest = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Leave request not found"));

        if (!leaveRequest.getEmployee().getId().equals(employee.getId())) {
            throw new IllegalStateException(
                    "You are not allowed to cancel this leave request"
            );
        }

        if (leaveRequest.getStatus() == LeaveRequestStatus.REJECTED ||
                leaveRequest.getStatus() == LeaveRequestStatus.CANCELLED) {

            throw new IllegalStateException(
                    "This leave request cannot be cancelled"
            );
        }

        if (leaveRequest.getStatus() == LeaveRequestStatus.APPROVED) {

            int year = leaveRequest.getStartDate().getYear();

            LeaveBalance balance = leaveBalanceRepository
                    .findByUserAndLeaveTypeAndYear(
                            employee,
                            leaveRequest.getLeaveType(),
                            year
                    )
                    .orElseThrow(() ->
                            new IllegalStateException("Leave balance not found")
                    );

            int approvedDays = leaveRequest.getNumberOfDays();

            balance.setUsedDays(balance.getUsedDays() - approvedDays);
            balance.setRemainingDays(balance.getRemainingDays() + approvedDays);
        }

        leaveRequest.setStatus(LeaveRequestStatus.CANCELLED);
    }

    // TEAM LEAVE HISTORY
    public List<LeaveRequestResponse> getTeamLeaveHistory(String managerEmail) {

        User manager = userRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Manager not found"));

        List<LeaveRequest> requests =
                leaveRequestRepository.findByManagerWithEmployeeAndLeaveType(manager);

        return requests.stream()
                .map(this::mapToResponse)
                .toList();
    }

    // WORKING DAY CALCULATION
    private int calculateWorkingDays(LocalDate start, LocalDate end) {

        List<Holiday> holidays =
                holidayRepository.findOverlappingHolidays(start, end);

        int workingDays = 0;
        LocalDate current = start;

        while (!current.isAfter(end)) {

            LocalDate date = current;

            boolean isWeekend =
                    date.getDayOfWeek().getValue() >= 6;

            boolean isHoliday = holidays.stream()
                    .anyMatch(h ->
                            !date.isBefore(h.getStartDate()) &&
                                    !date.isAfter(h.getEndDate())
                    );

            if (!isWeekend && !isHoliday) {
                workingDays++;
            }

            current = current.plusDays(1);
        }

        return workingDays;
    }

    // DTO MAPPER
    private LeaveRequestResponse mapToResponse(LeaveRequest leaveRequest) {

        LeaveRequestResponse response = new LeaveRequestResponse();

        User employee = leaveRequest.getEmployee();
        String fullName = employee.getFirstName() + " " + employee.getLastName();

        response.setId(leaveRequest.getId());
        response.setEmployeeName(fullName);
        response.setLeaveType(leaveRequest.getLeaveType().getName());
        response.setStartDate(leaveRequest.getStartDate());
        response.setEndDate(leaveRequest.getEndDate());
        response.setNumberOfDays(leaveRequest.getNumberOfDays());
        response.setStatus(leaveRequest.getStatus().name());

        // Employee reason
        response.setReason(leaveRequest.getReason());

        // Manager rejection remark
        response.setManagerRemark(leaveRequest.getReviewRemarks());

        response.setAppliedAt(leaveRequest.getAppliedAt());
        response.setReviewedAt(leaveRequest.getReviewedAt());

        return response;
    }
}