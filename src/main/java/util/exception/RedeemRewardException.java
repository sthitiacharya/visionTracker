package util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RedeemRewardException extends Exception {
    public RedeemRewardException() {}

    public RedeemRewardException(String msg) { super(msg); }
}
