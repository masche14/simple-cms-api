package com.malgn.exception;

import com.malgn.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 존재하지 않는 콘텐츠를 조회/수정/삭제하려고 했을 때 처리
    // 예: 없는 contentId로 상세 조회 요청
    // 응답: 404 Not Found
    @ExceptionHandler(ContentNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleContentNotFoundException(ContentNotFoundException e) {
        ErrorResponseDTO response = new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // 잘못된 요청 값이나 비즈니스 규칙 위반 처리
    // 예: 중복 사용자 아이디, 잘못된 파라미터 값
    // 응답: 400 Bad Request
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorResponseDTO response = new ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 인증은 되었지만 해당 리소스에 대한 권한이 없을 때 처리
    // 예: 본인이 작성하지 않은 콘텐츠를 수정/삭제 시도
    // 응답: 403 Forbidden
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDeniedException(AccessDeniedException e) {
        ErrorResponseDTO response = new ErrorResponseDTO(HttpStatus.FORBIDDEN.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    // @Valid 검증 실패 시 처리
    // 예: 필수값 누락, 길이 제한 초과, 공백 입력
    // 응답: 400 Bad Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        ErrorResponseDTO response = new ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 위에서 처리하지 못한 모든 예외를 마지막으로 처리
    // 예: 예상하지 못한 서버 내부 오류
    // 응답: 500 Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception e) {
        ErrorResponseDTO response = new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "서버 내부에서 오류가 발생했습니다."
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}