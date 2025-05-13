package rph.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

//RestApiException (custom exception)
@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException {
    private final ErrorCode errorCode;
}
