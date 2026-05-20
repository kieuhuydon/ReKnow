package com.huydon.reknow.service;

import com.huydon.reknow.common.response.PageResponse;
import com.huydon.reknow.dto.admin.AdminUserResponse;

public interface AdminService {
    PageResponse<AdminUserResponse> getUsers(int page, int size);

    AdminUserResponse getUserById(Long id);

    AdminUserResponse toggleStatus(Long id);

    void deleteUser(Long id);



}
