package main.java.rph.exception;

public class UserException extends RestApiException {
    public UserException(UserErrorCode errorCode) {
        super(errorCode);
    }
}
