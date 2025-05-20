package rph.exception;

import rph.exception.ErrorCode.TokenErrorCode;

public class TokenException extends RestApiException {
    public TokenException(TokenErrorCode errorCode) {
        super(errorCode);
    }
}
