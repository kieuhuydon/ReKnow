package com.huydon.reknow.controller;

import com.huydon.reknow.common.response.ApiResponse;
import com.huydon.reknow.dto.auth.AuthResponse;
import com.huydon.reknow.dto.auth.LoginRequest;
import com.huydon.reknow.dto.auth.RegisterRequest;
import com.huydon.reknow.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag (name = "Authentication")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @RequestBody @Valid RegisterRequest req){
        authService.register(req);
        ApiResponse<AuthResponse> res = ApiResponse.success("Register successfully ", null);

        return ResponseEntity.status(HttpStatus.CREATED).body(res);

    }

    @PostMapping("/login") // không lộ trên url
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @RequestBody @Valid LoginRequest req
    ){
        ApiResponse<AuthResponse> res = ApiResponse.success("Login successfully ", authService.login(req));
        return ResponseEntity.ok(res);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            @RequestBody Map<String, String> req
    ){
        String refreshToken = req.get("refreshToken");
        AuthResponse response = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(ApiResponse.success("token refreshed", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestBody Map<String, String> req
    ){
        String refreshToken = req.get("refreshToken");
        authService.logout(refreshToken);
        return ResponseEntity.ok(ApiResponse.success("Logout successfully",null));

    }
}
