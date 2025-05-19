package rph.exception;

import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

//RestApiException (custom exception)
@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Map<String, Object> data;

    public RestApiException(CommonErrorCode errorCode) {
        this(errorCode, null);
    }

    public RestApiException(CommonErrorCode errorCode, Map<String, Object> data) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.data = data;
    }
}

