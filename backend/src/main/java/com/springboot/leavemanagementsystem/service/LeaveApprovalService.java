package springboot.leavemanagementsystem.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.leavemanagementsystem.dto.LeaveRequestResponse;
import springboot.leavemanagementsystem.entity.*;
import springboot.leavemanagementsystem.repository.LeaveBalanceRepository;
import springboot.leavemanagementsystem.repository.LeaveRequestRepository;
import springboot.leavemanagementsystem.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true) // default read-only
public class LeaveApprovalService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;

    public LeaveApprovalService(LeaveRequestRepository leaveRequestRepository,
                                UserRepository userRepository,
                                LeaveBalanceRepository leaveBalanceRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.userRepository = userRepository;
        this.leaveBalanceRepository = leaveBalanceRepository;
    }

    // VIEW PENDING REQUESTS
    public List<LeaveRequestResponse> getPendingRequestsForManager(String managerEmail) {

        User manager = userRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Manager not found"));

        List<LeaveRequest> requests =
                leaveRequestRepository.findByStatusAndEmployee_Manager(
                        LeaveRequestStatus.PENDING,
                        manager
                );

        return requests.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // APPROVE LEAVE
    @Transactional
    public void approveLeave(Long leaveRequestId, String managerEmail) {

        User manager = getManager(managerEmail);
        LeaveRequest request = getValidatedPendingRequest(leaveRequestId, manager);

        // BALANCE DEDUCTION
        int year = request.getStartDate().getYear();

        LeaveBalance balance = leaveBalanceRepository
                .findByUserAndLeaveTypeAndYear(
                        request.getEmployee(),
                        request.getLeaveType(),
                        year
                )
                .orElseThrow(() ->
                        new IllegalStateException("Leave balance not found")
                );

        int requestedDays = request.getNumberOfDays();

        if (balance.getRemainingDays() < requestedDays) {
            throw new IllegalStateException(
                    "Insufficient leave balance at approval time"
            );
        }

        balance.setUsedDays(balance.getUsedDays() + requestedDays);
        balance.setRemainingDays(balance.getRemainingDays() - requestedDays);

        // Update leave status
        request.setStatus(LeaveRequestStatus.APPROVED);
        request.setReviewedBy(manager);
        request.setReviewedAt(LocalDateTime.now());
    }

    // REJECT LEAVE
    @Transactional
    public void rejectLeave(Long leaveRequestId, String remarks, String managerEmail) {

        User manager = getManager(managerEmail);
        LeaveRequest request = getValidatedPendingRequest(leaveRequestId, manager);

        request.setStatus(LeaveRequestStatus.REJECTED);
        request.setReviewRemarks(remarks);
        request.setReviewedBy(manager);
        request.setReviewedAt(LocalDateTime.now());
    }

    // SHARED VALIDATION METHODS
    private User getManager(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Manager not found"));
    }

    private LeaveRequest getValidatedPendingRequest(Long leaveRequestId, User manager) {

        LeaveRequest request = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Leave request not found"));

        validateManagerOwnership(request, manager);

        if (request.getStatus() != LeaveRequestStatus.PENDING) {
            throw new IllegalStateException(
                    "Only PENDING leave can be processed"
            );
        }

        if (request.getEmployee().equals(manager)) {
            throw new IllegalStateException(
                    "Manager cannot process their own leave"
            );
        }

        return request;
    }

    private void validateManagerOwnership(LeaveRequest request, User manager) {

        if (request.getEmployee().getManager() == null ||
                !request.getEmployee().getManager().equals(manager)) {

            throw new AccessDeniedException(
                    "You can only manage leave requests of your team members"
            );
        }
    }

    // DTO MAPPING
    private LeaveRequestResponse mapToResponse(LeaveRequest leaveRequest) {

        LeaveRequestResponse response = new LeaveRequestResponse();

        User employee = leaveRequest.getEmployee();

        String fullName =
                employee.getFirstName() + " " + employee.getLastName();

        response.setId(leaveRequest.getId());
        response.setEmployeeName(fullName);
        response.setLeaveType(leaveRequest.getLeaveType().getName());
        response.setStartDate(leaveRequest.getStartDate());
        response.setEndDate(leaveRequest.getEndDate());
        response.setNumberOfDays(leaveRequest.getNumberOfDays());
        response.setStatus(leaveRequest.getStatus().name());

        if (leaveRequest.getStatus() == LeaveRequestStatus.REJECTED) {
            response.setReason(leaveRequest.getReviewRemarks());
        } else {
            response.setReason(leaveRequest.getReason());
        }

        response.setAppliedAt(leaveRequest.getAppliedAt());
        response.setReviewedAt(leaveRequest.getReviewedAt());

        return response;
    }
}