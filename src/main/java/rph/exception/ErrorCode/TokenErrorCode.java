package rph.exception.ErrorCode;

import lombok.Getter;

import org.springframework.http.HttpStatus;
@Getter
public enum TokenErrorCode implements ErrorCode{
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "The token has expired."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "The token is invalid."),
    TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "The token is missing."),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "No stored refresh token found."),
    TOKEN_MISMATCH(HttpStatus.UNAUTHORIZED, "Token information does not match.");

    private final HttpStatus httpStatus;
    private final String message;

    TokenErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}


