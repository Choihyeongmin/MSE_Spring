package rph.exception;

import javax.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import rph.exception.ErrorCode.CommonErrorCode;
import rph.exception.ErrorCode.ErrorCode;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //Custom Exception
    @ExceptionHandler(RestApiException.class)
    protected ResponseEntity<ErrorResponse> handleRestApiException(RestApiException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse response = ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .data(e.getData())  
                .build();
        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    // JPA Entity Not Found
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException e) {
        ErrorResponse response = ErrorResponse.builder()
                .code(CommonErrorCode.RESOURCE_NOT_FOUND.name())
                .message("Entity not found")
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // DB 제약조건 위반
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        ErrorResponse response = ErrorResponse.builder()
                .code("DATA_INTEGRITY_VIOLATION")
                .message("Database integrity violation")
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // Security - 인증 실패
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e) {
        ErrorResponse response = ErrorResponse.builder()
                .code("AUTHENTICATION_FAILED")
                .message("Authentication failed")
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // Security - 인가 실패 (권한 없음)
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        ErrorResponse response = ErrorResponse.builder()
                .code("ACCESS_DENIED")
                .message("Access is denied")
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    //Type Mismatch
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request) {
        ErrorResponse response = ErrorResponse.builder()
                .code("TYPE_MISMATCH")
                .message("The provided value has an invalid type.")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Validation 실패
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()   
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");

        ErrorResponse response = ErrorResponse.builder()
                .code("VALIDATION_FAILED")
                .message(errorMessage)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 모든 예외 처리 (fallback)
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse response = ErrorResponse.builder()
                .code(CommonErrorCode.INTERNAL_SERVER_ERROR.name())
                .message(CommonErrorCode.INTERNAL_SERVER_ERROR.getMessage())
                .build();
        return ResponseEntity.status(CommonErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus()).body(response);
    }
}
