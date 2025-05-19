package main.java.rph.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter

public enum UserErrorCode implements ErrorCode{
    USERNAME_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 ID입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    UserErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
