package messenger.common.component.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class LoggerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public void exceptionLogger(Exception e) throws Exception {
        log.error("[EXCEPTION][CLASS: {}][MESSAGE: {}]", e.getClass().getSimpleName(), e.getMessage());
        throw e;
    }
}
