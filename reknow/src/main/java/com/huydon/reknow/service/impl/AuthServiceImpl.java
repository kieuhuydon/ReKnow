package com.huydon.reknow.service.impl;

import com.huydon.reknow.dto.auth.AuthResponse;
import com.huydon.reknow.dto.auth.LoginRequest;
import com.huydon.reknow.dto.auth.RegisterRequest;
import com.huydon.reknow.entity.RefreshToken;
import com.huydon.reknow.entity.User;
import com.huydon.reknow.entity.enums.Provider;
import com.huydon.reknow.entity.enums.Role;
import com.huydon.reknow.entity.enums.Status;
import com.huydon.reknow.exception.AppException;
import com.huydon.reknow.exception.ResourceNotFoundException;
import com.huydon.reknow.repository.RefreshTokenRepository;
import com.huydon.reknow.repository.UserRepository;
import com.huydon.reknow.service.AuthService;
import com.huydon.reknow.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;


    @Override
    public void  register(RegisterRequest req){
        //kiểm tra email đã tồn tại chưa
        if(userRepository.existsByEmail(req.getEmail())){
            throw new AppException(400, "Email was exist");
        }
            String password = passwordEncoder.encode(req.getPassword());


            //tạo user mới lưu vào DB
            User user = User.builder()
                    .email(req.getEmail())
                    .name(req.getName())
                    .password(password)
                    .status(Status.ACTIVE)
                    .role(Role.USER)
                    .provider(Provider.LOCAL)
                    .build();
            userRepository.save(user);


}

    @Override
    public AuthResponse login(LoginRequest req) {
        //Tìm user theo email
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(()->  new ResourceNotFoundException("User not found"));


        //So sánh password
        boolean isValidPass = passwordEncoder.matches(req.getPassword(), user.getPassword());
        if(!isValidPass){
            throw new AppException(400, "Invalid email or password");
        }

        if(user.getStatus() != Status.ACTIVE){
            throw new AppException(403, "Account is locked");
        }

        String accessToken = jwtService.generateToken(user);

        String refreshToken = UUID.randomUUID().toString();

        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshTokenEntity);


        return AuthResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    public AuthResponse refreshToken(String refreshToken){

        //1. Tìm refresh token
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(()-> new AppException(401, "Invalid refresh token"));

        //2. check hết hạn  quá khứ-hiện tại-tương lai
        if(token.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new AppException(401, "Refresh token expired ");
        }

        //3. check đã thu hồi chưa
        if(token.isRevoked()){
            throw new AppException(401, "Refresh token revoked");
        }

        //4. tạo access token mới
        String newAccessToken = jwtService.generateToken(token.getUser());

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new AppException(404, "Token not found"));

        token.setRevoked(true);

        refreshTokenRepository.save(token);
    }
}
