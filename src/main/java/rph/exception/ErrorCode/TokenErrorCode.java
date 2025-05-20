package rph.exception.ErrorCode;

import lombok.Getter;

import org.springframework.http.HttpStatus;
@Getter
public enum TokenErrorCode implements ErrorCode{
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다."),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "저장된 리프레시 토큰이 없습니다."),
    TOKEN_MISMATCH(HttpStatus.UNAUTHORIZED, "토큰 정보가 일치하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    TokenErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}


