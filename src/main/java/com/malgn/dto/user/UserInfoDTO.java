package com.malgn.dto.user;

import com.malgn.auth.Role;
import com.malgn.entity.User;

import java.time.LocalDateTime;

public record UserInfoDTO(
        String userId,
        String password,
        Role role,
        LocalDateTime registerDate,
        String registerBy
) {
    public static UserInfoDTO from (User user) {
        return new UserInfoDTO(
                user.getUserId(),
                user.getPassword(),
                user.getRole(),
                user.getRegisterDate(),
                user.getRegisterBy()
        );
    }
}
