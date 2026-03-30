package springboot.leavemanagementsystem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.leavemanagementsystem.dto.LeaveRequestResponse;
import springboot.leavemanagementsystem.entity.LeaveRequest;
import springboot.leavemanagementsystem.entity.LeaveRequestStatus;
import springboot.leavemanagementsystem.repository.LeaveRequestRepository;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
public class LeaveReportService {

    private final LeaveRequestRepository leaveRequestRepository;

    public LeaveReportService(LeaveRequestRepository leaveRequestRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
    }

    public Page<LeaveRequestResponse> getCompanyLeaveReport(
            LocalDate startDate,
            LocalDate endDate,
            Long leaveTypeId,
            LeaveRequestStatus status,
            Pageable pageable) {

        Page<LeaveRequest> page =
                leaveRequestRepository.findLeaveReports(
                        startDate,
                        endDate,
                        leaveTypeId,
                        status,
                        pageable
                );

        return page.map(this::mapToResponse);
    }

    private LeaveRequestResponse mapToResponse(LeaveRequest request) {

        LeaveRequestResponse response = new LeaveRequestResponse();

        response.setId(request.getId());
        response.setLeaveType(request.getLeaveType().getName());
        response.setStartDate(request.getStartDate());
        response.setEndDate(request.getEndDate());
        response.setNumberOfDays(request.getNumberOfDays());
        response.setStatus(request.getStatus().name());
        response.setReason(request.getReason());
        response.setAppliedAt(request.getAppliedAt());
        response.setReviewedAt(request.getReviewedAt());

        return response;
    }
}