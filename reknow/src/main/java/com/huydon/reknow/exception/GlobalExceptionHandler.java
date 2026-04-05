package com.huydon.reknow.exception;

import com.huydon.reknow.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice

public class GlobalExceptionHandler {
    //bắt lỗi custom
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(ResourceNotFoundException ex){
        ApiResponse<Void> res = ApiResponse.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);

    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Void>> handleAppException(AppException ex){
        ApiResponse<Void> res = ApiResponse.error(ex.getMessage());
        return ResponseEntity.status(ex.getStatusCode()).body(res);

    }

    @ExceptionHandler (Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneral (Exception ex){
        ApiResponse<Void> res = ApiResponse.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);

    }
}
