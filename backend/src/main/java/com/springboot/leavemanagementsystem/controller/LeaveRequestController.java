package springboot.leavemanagementsystem.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import springboot.leavemanagementsystem.dto.ApplyLeaveRequest;
import springboot.leavemanagementsystem.dto.LeaveRequestResponse;
import springboot.leavemanagementsystem.service.LeaveRequestService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/leave-requests")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    public LeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    // APPLY FOR LEAVE
    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public void applyLeave(
            @AuthenticationPrincipal UserDetails user,
            @Valid @RequestBody ApplyLeaveRequest request
    ) {
        leaveRequestService.applyForLeave(user.getUsername(), request);
    }

    // GET MY LEAVE HISTORY
    @GetMapping("/my")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public List<LeaveRequestResponse> getMyLeaves(
            @AuthenticationPrincipal UserDetails user
    ) {
        return leaveRequestService.getLeaveRequestsByEmployee(user.getUsername());
    }

    // CANCEL LEAVE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public void cancelLeave(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long id
    ) {
        leaveRequestService.cancelLeaveRequest(user.getUsername(), id);
    }

    @GetMapping("/team-history")
    @PreAuthorize("hasRole('MANAGER')")
    public List<LeaveRequestResponse> getTeamHistory(Authentication authentication) {

        String email = authentication.getName();

        return leaveRequestService.getTeamLeaveHistory(email);
    }
}