package util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MilestoneNotFoundException extends Exception {
    public MilestoneNotFoundException() { }

    public MilestoneNotFoundException(String msg) { super(msg);}
}
