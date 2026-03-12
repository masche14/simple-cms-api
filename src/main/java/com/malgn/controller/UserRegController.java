package com.malgn.controller;

import com.malgn.dto.user.UserRegisterDTO;
import com.malgn.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "1. 등록 (Reg)", description = "사용자 등록 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reg")
public class UserRegController {

    private final IUserService userService;

    // 사용자 추가
    @Operation(summary = "사용자 등록 API", description = "새로운 사용자를 등록합니다.")
    @PostMapping("/insertUser")
    public ResponseEntity<String> insertUser(@Valid @RequestBody UserRegisterDTO pDTO) {
        log.info("{}.insertUser Start", this.getClass().getSimpleName());

        log.info("pDTO: {}", pDTO);

        int res = userService.insertUser(pDTO);

        if (res != 1) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버에 오류가 발생하였습니다.");
        }

        log.info("{}.insertUser End", this.getClass().getSimpleName());

        return ResponseEntity.status(HttpStatus.CREATED).body("사용자 등록에 성공하였습니다.");
    }

}
