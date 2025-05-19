package rph.exception;

import org.springframework.http.HttpStatus;

//Error interface
public interface ErrorCode {
    String name();
    HttpStatus getHttpStatus();
    String getMessage();
}
