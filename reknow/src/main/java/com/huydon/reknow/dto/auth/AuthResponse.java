package com.huydon.reknow.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
// trả về khi register/ login thành công
public class AuthResponse {
    private String token;// chỉ login mới có

}
