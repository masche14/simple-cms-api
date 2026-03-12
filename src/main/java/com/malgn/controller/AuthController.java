package com.malgn.controller;

import com.malgn.dto.user.LoginRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "2. 인증 (Auth)", description = "사용자 로그인 및 로그아웃 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    @Operation(summary = "로그인", description = "아이디와 비밀번호로 로그인하여 세션을 발급받습니다.")
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDTO requestDTO, HttpServletRequest request) {

        log.info("{}.login Start", this.getClass().getSimpleName());

        log.info("requestDTO: {}", requestDTO);

        // 아이디와 비밀번호 검증 (실패 시 AuthenticationException 발생)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDTO.userId(), requestDTO.password())
        );

        // 인증 성공 시 SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 세션을 생성하고 SecurityContext를 세션에 저장
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        return ResponseEntity.ok("로그인에 성공했습니다.");
    }

    @Operation(summary = "로그아웃", description = "현재 발급된 세션을 만료시킵니다.")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok("로그아웃에 성공했습니다.");
    }

}
