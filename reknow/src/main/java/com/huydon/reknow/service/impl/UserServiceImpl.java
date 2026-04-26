package com.huydon.reknow.service.impl;

import com.huydon.reknow.dto.user.*;
import com.huydon.reknow.entity.PasswordResetToken;
import com.huydon.reknow.entity.User;
import com.huydon.reknow.exception.AppException;
import com.huydon.reknow.exception.ResourceNotFoundException;
import com.huydon.reknow.mapper.UserMapper;
import com.huydon.reknow.repository.PasswordResetTokenRepository;
import com.huydon.reknow.repository.UserRepository;
import com.huydon.reknow.service.EmailService;
import com.huydon.reknow.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository pwResetTokenRepository;
    private final EmailService emailService;


    @Override
    public UserResponse getMyProfile() {
        User user = findCurrentUser();

        return userMapper.toResponse(user);


    }

    @Override
    public UserResponse updateProfile(UpdateUserRequest req) {
        User user = findCurrentUser();

        userMapper.updateProfile(req, user);

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);

    }

    @Override
    public void changePassword(ChangePasswordRequest req) {
        User user = findCurrentUser();

        //dùng paswordEncoder để so sánh pw đã hash
        if(!passwordEncoder.matches(req.getOldPassword(), user.getPassword())){
            throw new AppException(400, "Wrong password");
        }

        if(!req.getNewPassword().equals(req.getConfirmPassword())){
            throw new AppException(400, "Password not match");
        }
        String hashPassword = passwordEncoder.encode(req.getNewPassword());
        user.setPassword(hashPassword);

        userRepository.save(user);


    }

    @Override
    @Transactional
    public void forgetPassword(ForgetPasswordRequest req) {
        //tìm user theo email, chưa đăng nhập chưa có trong context
        User user = userRepository.findByEmail(req.getEmail()).
                orElseThrow(()->new ResourceNotFoundException("User not found"));

        //xóa cái cũ
        pwResetTokenRepository.deleteByUser(user);
        pwResetTokenRepository.flush();

        //tạo reset token
        String token = UUID.randomUUID().toString();
        //lưu token vào db
        PasswordResetToken pwResetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .used(false)
                .build();

        pwResetTokenRepository.save(pwResetToken);

        //gửi email chưa link reset, link này dẫn tới giao diện frontend, frontend lấy token từ url xuống
        String resetLink = "http://localhost:3000/reset-password?token=" + token;
        emailService.sendResetPasswordEmail(user.getEmail(), resetLink);

    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest req) {
        //tìm token trong db
        PasswordResetToken pwResetToken = pwResetTokenRepository.findByToken(req.getToken())
                .orElseThrow(() -> new AppException(400, "Invalid token"));
        LocalDateTime expiredTime = pwResetToken.getExpiresAt();

        if(LocalDateTime.now().isAfter(expiredTime)){
            throw new AppException(400, "Token expired ");
        }

        //check đã dùng chưa
        if(pwResetToken.getUsed()){
            throw new AppException(400, "Token already used");
        }

        if(!req.getPassword().equals(req.getConfirmPassword())){
            throw new AppException(400, "Confirm password does not match");
        }

        User user = pwResetToken.getUser();

        //udpate pw mới
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        userRepository.save(user);

        //update token đã dùng
        pwResetToken.setUsed(true);

        pwResetTokenRepository.save(pwResetToken);


    }

    private User findCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(email).
                orElseThrow(()->new ResourceNotFoundException("User not found"));


    }
}
