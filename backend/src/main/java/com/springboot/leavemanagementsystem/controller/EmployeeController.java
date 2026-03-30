package springboot.leavemanagementsystem.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    /**
     * Endpoint: View logged-in employee leave history
     * Accessible only to EMPLOYEE role
     */
    @GetMapping("/leave-history")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String viewLeaveHistory() {
        return "Employee leave history";
    }
}