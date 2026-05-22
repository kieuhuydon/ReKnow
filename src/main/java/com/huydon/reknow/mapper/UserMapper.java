package com.huydon.reknow.mapper;

import com.huydon.reknow.dto.admin.AdminUserResponse;
import com.huydon.reknow.dto.user.UpdateUserRequest;
import com.huydon.reknow.dto.user.UserResponse;
import com.huydon.reknow.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel= "spring")
public interface UserMapper {

    UserResponse toResponse (User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProfile (UpdateUserRequest req, @MappingTarget User user );

    AdminUserResponse toAdminUserResponse(User user);
}
