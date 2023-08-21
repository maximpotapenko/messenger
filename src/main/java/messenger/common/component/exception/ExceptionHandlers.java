package messenger.common.component.exception;

import lombok.extern.slf4j.Slf4j;
import messenger.common.dto.ViolationListResponseDto;
import messenger.common.dto.ViolationResponseDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
@Slf4j
public class ExceptionHandlers {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ViolationListResponseDto> handle(MethodArgumentNotValidException e) {

        BindingResult br = e.getBindingResult();

        log.info("[VALIDATION FAILED] FOR [{}]", br.getObjectName());

        List<FieldError> errors = br.getFieldErrors();

        List<ViolationResponseDto> violations = errors.stream()
                .map(error -> ViolationResponseDto.builder()
                        .rejectedValue((String) error.getRejectedValue())
                        .message(error.getDefaultMessage())
                        .fieldName(error.getField())
                        .build())
                .toList();

        ViolationListResponseDto response = ViolationListResponseDto.builder()
                .violations(violations)
                .status(400)
                .message("Validation failed")
                .build();

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(400));
    }
}
