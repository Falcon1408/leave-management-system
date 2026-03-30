package springboot.leavemanagementsystem.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springboot.leavemanagementsystem.dto.CreateLeaveTypeRequest;
import springboot.leavemanagementsystem.dto.LeaveTypeResponse;
import springboot.leavemanagementsystem.dto.UpdateLeaveTypeRequest;
import springboot.leavemanagementsystem.service.LeaveTypeService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LeaveTypeController {

    private final LeaveTypeService leaveTypeService;

    public LeaveTypeController(LeaveTypeService leaveTypeService) {
        this.leaveTypeService = leaveTypeService;
    }

    // ====================================================
    // ADMIN ENDPOINTS
    // ====================================================

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/leave-types")
    public ResponseEntity<LeaveTypeResponse> createLeaveType(@Valid @RequestBody CreateLeaveTypeRequest request) {

        LeaveTypeResponse response = leaveTypeService.createLeaveType(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/leave-types/{id}")
    public ResponseEntity<LeaveTypeResponse> updateLeaveType(
            @PathVariable Long id,
            @Valid @RequestBody UpdateLeaveTypeRequest request) {

        LeaveTypeResponse response = leaveTypeService.updateLeaveType(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/leave-types/{id}/enable")
    public ResponseEntity<Void> enableLeaveType(@PathVariable Long id) {

        leaveTypeService.enableLeaveType(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/leave-types/{id}/disable")
    public ResponseEntity<Void> disableLeaveType(@PathVariable Long id) {

        leaveTypeService.disableLeaveType(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/leave-types")
    public ResponseEntity<List<LeaveTypeResponse>> getAllLeaveTypes() {

        List<LeaveTypeResponse> leaveTypes = leaveTypeService.getAllLeaveTypes();
        return ResponseEntity.ok(leaveTypes);
    }

    // EMPLOYEE ENDPOINT
    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER','ADMIN')")
    @GetMapping("/leave-types")
    public ResponseEntity<List<LeaveTypeResponse>> getActiveLeaveTypes() {

        List<LeaveTypeResponse> leaveTypes = leaveTypeService.getActiveLeaveTypes();
        return ResponseEntity.ok(leaveTypes);
    }
}