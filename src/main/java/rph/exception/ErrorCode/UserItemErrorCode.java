package rph.exception.ErrorCode;

import lombok.Getter;

import org.springframework.http.HttpStatus;
@Getter
public enum UserItemErrorCode implements ErrorCode{
    ITEM_OWNED(HttpStatus.BAD_REQUEST, "Item already owned"),
    NO_COIN(HttpStatus.BAD_REQUEST, "Not enough coins");
    private final HttpStatus httpStatus;
    private final String message;

    UserItemErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
