package com.huydon.reknow.service;

import com.huydon.reknow.dto.auth.LoginRequest;
import com.huydon.reknow.dto.auth.RegisterRequest;
import com.huydon.reknow.dto.auth.AuthResponse;


public interface AuthService {
    public  void register(RegisterRequest req);
    public AuthResponse login (LoginRequest req);
}
