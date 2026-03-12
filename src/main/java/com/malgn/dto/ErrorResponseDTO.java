package com.malgn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponseDTO {
    private int status;// HTTP 상태 코드 (예: 400, 403)
    private String message;   // 에러 메시지
}