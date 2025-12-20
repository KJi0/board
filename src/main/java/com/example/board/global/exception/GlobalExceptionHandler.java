package com.example.board.global.exception;

import com.example.board.global.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.board.global.response.ApiResponse.fail;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustom(CustomException e) {
        ErrorCode ec = e.getErrorCode();
        return ResponseEntity.status(ec.getStatus())
                .body(fail(ec.getCode(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgNotValid(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().isEmpty()
                ? ErrorCode.VALIDATION_ERROR.getMessage()
                : e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

        return ResponseEntity.status(ErrorCode.VALIDATION_ERROR.getStatus())
                .body(fail(ErrorCode.VALIDATION_ERROR.getCode(), msg));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException e) {
        return ResponseEntity.status(ErrorCode.VALIDATION_ERROR.getStatus())
                .body(fail(ErrorCode.VALIDATION_ERROR.getCode(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception e) {
        // 🔥 개선: 예상 못 한 예외는 반드시 로그
        log.error("Unexpected server error", e);

        return ResponseEntity.status(ErrorCode.INTERNAL_ERROR.getStatus())
                .body(fail(
                        ErrorCode.INTERNAL_ERROR.getCode(),
                        ErrorCode.INTERNAL_ERROR.getMessage()
                ));
    }
}