package com.malgn.service.impl;

import com.malgn.auth.AuthInfo;
import com.malgn.auth.Role;
import com.malgn.dto.user.UserInfoDTO;
import com.malgn.dto.user.UserRegisterDTO;
import com.malgn.entity.User;
import com.malgn.repository.UserRepository;
import com.malgn.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        log.info("{}.loadUserByUsername Start", this.getClass().getSimpleName());

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저 아이디입니다."));

        UserInfoDTO userInfoDTO = UserInfoDTO.from(user);

        log.info("{}.loadUserByUsername End", this.getClass().getSimpleName());

        return new AuthInfo(userInfoDTO);
    }

    @Transactional
    @Override
    public int insertUser(UserRegisterDTO pDTO) {
        log.info("{}.insertUser Start", this.getClass().getSimpleName());

        int res = 0;

        if (userRepository.findByUserId(pDTO.userId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자 아이디입니다.");
        }

        String password = passwordEncoder.encode(pDTO.password());
        String registerBy = pDTO.userId();
        LocalDateTime registerDate = LocalDateTime.now();
        Role role = Role.USER;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && isAdmin(authentication)) {
            registerBy = authentication.getName();
        }

        User user = User.builder()
                .userId(pDTO.userId())
                .password(password)
                .registerBy(registerBy)
                .registerDate(registerDate)
                .role(role)
                .build();

        userRepository.save(user);

        res = 1;

        return res;
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
