package messenger.exception.handler;

import messenger.exception.dto.ViolationListResponseDto;
import messenger.exception.dto.ViolationResponseDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ViolationListResponseDto> handle(MethodArgumentNotValidException e) {

        BindingResult br = e.getBindingResult();

        List<FieldError> errors = br.getFieldErrors();

        List<ViolationResponseDto> violations = errors.stream()
                .map(error -> ViolationResponseDto.builder()
                        .rejectedValue(error.getRejectedValue().toString())
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
