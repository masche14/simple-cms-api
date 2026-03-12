package com.malgn.configure;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@RequiredArgsConstructor
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AppConfiguration {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> {
            // 현재 로그인한 사용자 정보를 가져옴
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
                return Optional.empty();
            }

            return Optional.of(authentication.getName());
        };
    }
}
