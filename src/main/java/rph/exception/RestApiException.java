package rph.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import rph.exception.ErrorCode.ErrorCode;
import rph.exception.ErrorCode.UserErrorCode;

//RestApiException (custom exception)
@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException {

    private final ErrorCode errorCode;
}
