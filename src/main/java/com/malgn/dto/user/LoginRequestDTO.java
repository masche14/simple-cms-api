package com.malgn.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record LoginRequestDTO(
        @NotBlank(message = "유저 아이디는 필수입니다.")
        @Size(max = 50, message = "유저 아이디는 50자 이하여야 합니다.")
        String userId,

        @NotBlank(message = "비밀번호는 필수입니다.")
        String password
) {

}
