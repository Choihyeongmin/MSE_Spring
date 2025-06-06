package rph.exception;

import rph.exception.ErrorCode.ErrorCode;
import rph.exception.ErrorCode.UserItemErrorCode;

public class UserItemException extends RestApiException {
    public UserItemException(UserItemErrorCode errorCode) {
        super(errorCode);
    }
}
