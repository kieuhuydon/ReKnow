package com.huydon.reknow.dto.admin;

import com.huydon.reknow.entity.enums.Provider;
import com.huydon.reknow.entity.enums.Role;
import com.huydon.reknow.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private Role role;
    private Status status;
    private Provider provider;
    private LocalDateTime createdAt;

}
