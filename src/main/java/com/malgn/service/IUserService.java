package com.malgn.service;

import com.malgn.dto.user.UserRegisterDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {
    int insertUser(UserRegisterDTO pDTO);
}
