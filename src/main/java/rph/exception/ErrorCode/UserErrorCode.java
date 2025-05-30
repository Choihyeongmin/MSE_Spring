package rph.exception.ErrorCode;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter

public enum UserErrorCode implements ErrorCode{
    USERNAME_DUPLICATED(HttpStatus.CONFLICT, "This ID already exists."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Incorrect password.");

    private final HttpStatus httpStatus;
    private final String message;

    UserErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
