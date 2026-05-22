package com.huydon.reknow.dto.auth;

import lombok.Data;

@Data
public class LogoutRequest {
    private String refreshToken;
}
