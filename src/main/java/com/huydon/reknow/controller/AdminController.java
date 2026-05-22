package com.huydon.reknow.controller;

import com.huydon.reknow.common.response.ApiResponse;
import com.huydon.reknow.common.response.PageResponse;
import com.huydon.reknow.dto.admin.AdminUserResponse;
import com.huydon.reknow.service.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<PageResponse<AdminUserResponse>>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        ApiResponse<PageResponse<AdminUserResponse>> res = ApiResponse.success("Get users successfully", this.adminService.getUsers(page, size));
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("users/{id}")
    public ResponseEntity<ApiResponse<AdminUserResponse>> getUserById(
            @PathVariable Long id
    ){
        ApiResponse<AdminUserResponse> res = ApiResponse.success("Get user successfully", this.adminService.getUserById(id));
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PatchMapping("/users/{id}/toggle-status")
    public ResponseEntity<ApiResponse<AdminUserResponse>> toggleStatus(
            @PathVariable Long id
    ){
        ApiResponse<AdminUserResponse> res = ApiResponse.success("Change status  successfully", this.adminService.toggleStatus(id));
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable Long id
    ){
        this.adminService.deleteUser(id);
        ApiResponse<Void> res = ApiResponse.success("Delete user successfully");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(res);
    }
}
