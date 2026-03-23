package com.malgn.dto.user;

import com.malgn.auth.Role;
import com.malgn.entity.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserInfoDTO(
        String userId,
        String password,
        Role role,
        LocalDateTime registerDate,
        String registerBy
) {
    public static UserInfoDTO from (User user) {
        return UserInfoDTO.builder()
                .userId(user.getUserId())
                .password(user.getPassword())
                .role(user.getRole())
                .registerDate(user.getRegisterDate())
                .registerBy(user.getRegisterBy())
                .build();
    }
}
