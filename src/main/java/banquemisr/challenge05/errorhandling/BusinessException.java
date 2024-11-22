package banquemisr.challenge05.errorhandling;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {
    private String message;
    private String description;
    private HttpStatus httpStatus;

    public BusinessException(String message) {
        super();
        this.message = message;
    }

}
