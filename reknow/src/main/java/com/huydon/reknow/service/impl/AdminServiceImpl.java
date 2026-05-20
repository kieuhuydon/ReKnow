package com.huydon.reknow.service.impl;

import com.huydon.reknow.common.response.PageResponse;
import com.huydon.reknow.dto.admin.AdminUserResponse;
import com.huydon.reknow.entity.User;
import com.huydon.reknow.entity.enums.Role;
import com.huydon.reknow.entity.enums.Status;
import com.huydon.reknow.exception.AppException;
import com.huydon.reknow.exception.ResourceNotFoundException;
import com.huydon.reknow.mapper.UserMapper;
import com.huydon.reknow.repository.UserRepository;
import com.huydon.reknow.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public PageResponse<AdminUserResponse> getUsers(int page, int size) {
        int pageIndex = page<1 ? 0 : page-1;
        Pageable pageable = PageRequest.of(pageIndex, size);

        Page<User> userPage = userRepository.findAll(pageable);

        Page<AdminUserResponse> userResponsePage = userPage.map(user -> userMapper.toAdminUserResponse(user));

        List<AdminUserResponse> users = userResponsePage.getContent();


        return PageResponse.<AdminUserResponse>builder()
                .data(users)
                .currentPage(userResponsePage.getNumber())
                .pageSize(userResponsePage.getSize())
                .totalElements(userResponsePage.getTotalElements())
                .totalPages(userResponsePage.getTotalPages())
                .build()
                ;

    }

    @Override
    public AdminUserResponse getUserById(Long id) {
        User currentUser = userRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("User not found"));



        return userMapper.toAdminUserResponse(currentUser);
    }

    @Override
    public AdminUserResponse toggleStatus(Long id) {
        User currentUser = userRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("User not found"));

        Status userStatus = currentUser.getStatus();
        Status newStatus = (userStatus == Status.ACTIVE)? Status.INACTIVE : Status.ACTIVE;
        currentUser.setStatus(newStatus);

        User updateUser = this.userRepository.save(currentUser);
        return userMapper.toAdminUserResponse(updateUser);
    }

    @Override
    public void deleteUser(Long id) {
        User currentUser = userRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("User not found"));

        if(currentUser.getRole().equals(Role.ADMIN)){
            throw new AppException(403, "cannot delete an Admin");
        }

        userRepository.delete(currentUser);


    }
}
