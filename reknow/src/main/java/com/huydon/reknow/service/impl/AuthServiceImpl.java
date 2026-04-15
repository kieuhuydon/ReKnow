package com.huydon.reknow.service.impl;

import com.huydon.reknow.dto.auth.LoginRequest;
import com.huydon.reknow.dto.auth.RegisterRequest;
import com.huydon.reknow.dto.auth.AuthResponse;
import com.huydon.reknow.entity.User;
import com.huydon.reknow.entity.enums.Provider;
import com.huydon.reknow.entity.enums.Role;
import com.huydon.reknow.entity.enums.Status;
import com.huydon.reknow.exception.AppException;
import com.huydon.reknow.exception.ResourceNotFoundException;
import com.huydon.reknow.repository.UserRepository;
import com.huydon.reknow.service.AuthService;
import com.huydon.reknow.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
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

        String token = jwtService.generateToken(user);


        return AuthResponse.builder().token(token).build();
    }
}
