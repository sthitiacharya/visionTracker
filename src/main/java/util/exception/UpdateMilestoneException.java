package util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UpdateMilestoneException extends Exception {
    public UpdateMilestoneException() {}

    public UpdateMilestoneException(String msg) { super(msg);}
}
