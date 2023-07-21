package messenger.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotYourResourceException extends RuntimeException {
    public NotYourResourceException() {}

    public NotYourResourceException(String message) {
        super(message);
    }
}
