package banquemisr.challenge05.errorhandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
        errorResponse.setTimestamp(sdf.format(date));

        return new ResponseEntity<>(errorResponse, e.getHttpStatus() != null ? e.getHttpStatus() : HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class, Throwable.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleGeneralException(BusinessException e) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
        errorResponse.setTimestamp(sdf.format(date));

        return new ResponseEntity<>(errorResponse, e.getHttpStatus() != null ? e.getHttpStatus() : HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
