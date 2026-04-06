package com.huydon.reknow.service;

import com.huydon.reknow.dto.request.LoginRequest;
import com.huydon.reknow.dto.request.RegisterRequest;
import com.huydon.reknow.dto.response.AuthResponse;


public interface AuthService {
    public  void register(RegisterRequest req);
    public AuthResponse login (LoginRequest req);
}
