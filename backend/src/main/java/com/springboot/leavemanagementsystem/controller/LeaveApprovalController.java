package springboot.leavemanagementsystem.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import springboot.leavemanagementsystem.dto.LeaveRequestResponse;
import springboot.leavemanagementsystem.dto.RejectLeaveRequest;
import springboot.leavemanagementsystem.service.LeaveApprovalService;

import java.util.List;

@RestController
@RequestMapping("/api/leave-approvals")
@PreAuthorize("hasRole('MANAGER')")
public class LeaveApprovalController {

    private final LeaveApprovalService leaveApprovalService;

    public LeaveApprovalController(LeaveApprovalService leaveApprovalService) {
        this.leaveApprovalService = leaveApprovalService;
    }

    @GetMapping("/pending")
    public ResponseEntity<List<LeaveRequestResponse>> getPendingRequests(
            Authentication authentication) {

        String email = authentication.getName();

        List<LeaveRequestResponse> responses =
                leaveApprovalService.getPendingRequestsForManager(email);

        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<String> approveLeave(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        leaveApprovalService.approveLeave(id, email);

        return ResponseEntity.ok("Leave approved successfully");
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<String> rejectLeave(
            @PathVariable Long id,
            @Valid @RequestBody RejectLeaveRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        leaveApprovalService.rejectLeave(id, request.getRemarks(), email);

        return ResponseEntity.ok("Leave rejected successfully");
    }

}