package messenger.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException() {}

    public ResourceNotFoundException(String name) {
        super(String.format("Couldn't find resource with name %s", name));
    }

    public ResourceNotFoundException(Long id) {
        super(String.format("Couldn't find resource with id %d", id));
    }
}
