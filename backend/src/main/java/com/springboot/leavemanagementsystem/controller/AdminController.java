package springboot.leavemanagementsystem.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import springboot.leavemanagementsystem.dto.CreateUserRequest;
import springboot.leavemanagementsystem.dto.UpdateUserRequest;
import springboot.leavemanagementsystem.dto.UserResponse;
import springboot.leavemanagementsystem.service.AdminUserService;

@RestController
@RequestMapping("/api/admin/employees")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminUserService adminUserService;

    public AdminController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    // CREATE USER
    @PostMapping
    public UserResponse createEmployee(@RequestBody CreateUserRequest request) {
        return adminUserService.createUser(request);
    }

    // GET USER BY ID
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return adminUserService.getUserById(id);
    }

    // GET ALL USERS (PAGINATION)
    @GetMapping
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return adminUserService.getAllUsers(pageable);
    }

    // UPDATE USER
    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable Long id,
                                   @RequestBody UpdateUserRequest request) {
        return adminUserService.updateUser(id, request);
    }

    // ACTIVATE / DEACTIVATE
    @PatchMapping("/{id}/status")
    public UserResponse toggleUserStatus(@PathVariable Long id) {
        return adminUserService.toggleUserStatus(id);
    }
}