package com.huydon.reknow.controller;

import com.huydon.reknow.common.response.ApiResponse;
import com.huydon.reknow.dto.user.*;
import com.huydon.reknow.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name="User")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyProfile(){
        ApiResponse<UserResponse> res = ApiResponse.success("Get profile successfully", userService.getMyProfile());

        return ResponseEntity.ok(res);
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @RequestBody @Valid UpdateUserRequest request
    ){
        ApiResponse<UserResponse> res = ApiResponse.success("Update profile successfully", userService.updateProfile(request));

        return ResponseEntity.ok(res);
    }

    @PatchMapping("/me/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @RequestBody @Valid ChangePasswordRequest request
    ){
        userService.changePassword(request);
        ApiResponse<Void> res = ApiResponse.success("Change password successfully");

        return ResponseEntity.ok(res);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @RequestBody @Valid ForgetPasswordRequest request
    ){
     userService.forgetPassword(request);
     ApiResponse<Void> res = ApiResponse.success("Reset password email is sent successfully");

     return ResponseEntity.ok(res);

    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @RequestBody @Valid ResetPasswordRequest request
    ){
        userService.resetPassword(request);
        ApiResponse<Void> res = ApiResponse.success("Reset password successfully");

        return ResponseEntity.ok(res);

    }

}
