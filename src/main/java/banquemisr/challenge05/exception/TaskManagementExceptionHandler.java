package banquemisr.challenge05.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class TaskManagementExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String, String>> handleValidationError(MethodArgumentNotValidException exception){
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    Object handleDueDateFormatError(HttpMessageNotReadableException exception){
        Map<String, String> errors = new HashMap<>();

        Throwable cause = exception.getCause();
        if (cause instanceof com.fasterxml.jackson.databind.JsonMappingException jsonMappingException) {

            if (!jsonMappingException.getPath().isEmpty()) {
                String fieldName = jsonMappingException.getPath().get(0).getFieldName();
                if(fieldName.equals("dueDate")){
                    errors.put("dueDate", "Due Date must be in dd-MM-yyyy format");
                }
            }
        } else {

            errors.put("error", "Invalid request format");
        }

        return errors;
    }
}
