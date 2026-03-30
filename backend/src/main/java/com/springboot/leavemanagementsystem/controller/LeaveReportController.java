package springboot.leavemanagementsystem.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springboot.leavemanagementsystem.dto.LeaveRequestResponse;
import springboot.leavemanagementsystem.entity.LeaveRequestStatus;
import springboot.leavemanagementsystem.service.LeaveReportService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
@PreAuthorize("hasRole('ADMIN')")
public class LeaveReportController {

    private final LeaveReportService leaveReportService;

    public LeaveReportController(LeaveReportService leaveReportService) {
        this.leaveReportService = leaveReportService;
    }

    @GetMapping("/leaves")
    public Page<LeaveRequestResponse> getCompanyLeaveReport(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,

            @RequestParam(required = false)
            Long leaveTypeId,

            @RequestParam(required = false)
            LeaveRequestStatus status,

            Pageable pageable) {

        return leaveReportService.getCompanyLeaveReport(
                startDate,
                endDate,
                leaveTypeId,
                status,
                pageable
        );
    }
}