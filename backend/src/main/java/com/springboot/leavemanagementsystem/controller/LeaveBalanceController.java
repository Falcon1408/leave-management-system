package springboot.leavemanagementsystem.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springboot.leavemanagementsystem.dto.AdjustBalanceRequest;
import springboot.leavemanagementsystem.dto.LeaveBalanceDto;
import springboot.leavemanagementsystem.service.LeaveBalanceService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/leave-balances")

@PreAuthorize("hasRole('ADMIN')")
public class LeaveBalanceController {

    private final LeaveBalanceService leaveBalanceService;

    public LeaveBalanceController(LeaveBalanceService leaveBalanceService) {
        this.leaveBalanceService = leaveBalanceService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<LeaveBalanceDto> getBalances(
            @RequestParam(required = false) Long userId,
            @RequestParam Integer year,
            Pageable pageable
    ) {
        return leaveBalanceService.getBalances(userId, year, pageable);
    }

    @PostMapping("/initialize")
    public ResponseEntity<String> initializeYear(
            @RequestParam Integer year
    ) {
        leaveBalanceService.initializeYear(year);
        return ResponseEntity.ok("Leave balances initialized for year " + year);
    }

    @PutMapping("/{id}/adjust")
    public ResponseEntity<String> adjustBalance(
            @PathVariable Long id,
            @RequestBody AdjustBalanceRequest request
    ) {
        leaveBalanceService.adjustBalance(id, request.getNewTotalAllocated());
        return ResponseEntity.ok("Leave balance adjusted successfully");
    }
}