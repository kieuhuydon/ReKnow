package com.huydon.reknow.service;


import com.huydon.reknow.dto.user.*;

public interface UserService {
    UserResponse getMyProfile();

    UserResponse updateProfile(UpdateUserRequest req);

    void changePassword(ChangePasswordRequest req);

    void forgetPassword(ForgetPasswordRequest req);

    void resetPassword(ResetPasswordRequest req);


}
