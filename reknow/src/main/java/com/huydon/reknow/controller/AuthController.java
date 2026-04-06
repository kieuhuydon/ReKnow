package com.huydon.reknow.controller;

import com.huydon.reknow.common.ApiResponse;
import com.huydon.reknow.dto.request.LoginRequest;
import com.huydon.reknow.dto.request.RegisterRequest;
import com.huydon.reknow.dto.response.AuthResponse;
import com.huydon.reknow.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @RequestBody @Valid RegisterRequest req){
        authService.register(req);
        ApiResponse<AuthResponse> res = ApiResponse.success("Register successfully ", null);

        return ResponseEntity.ok(res);

    }

    @PostMapping("/login") // không lộ trên url
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @RequestBody @Valid LoginRequest req
    ){
        ApiResponse<AuthResponse> res = ApiResponse.success("Login successfully ", authService.login(req));
        return ResponseEntity.ok(res);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(){
        return ResponseEntity.ok(ApiResponse.success("Logout successfully",null));

    }
}
