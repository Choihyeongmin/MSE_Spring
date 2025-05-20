package rph.exception;

import rph.exception.ErrorCode.ErrorCode;
import rph.exception.ErrorCode.UserErrorCode;

public class UserException extends RestApiException {
    public UserException(UserErrorCode errorCode) {
        super(errorCode);
    }
}
