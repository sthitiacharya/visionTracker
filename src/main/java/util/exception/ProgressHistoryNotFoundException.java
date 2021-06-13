package util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProgressHistoryNotFoundException extends Exception {
    public ProgressHistoryNotFoundException() {}

    public ProgressHistoryNotFoundException(String msg) { super(msg); }
}
