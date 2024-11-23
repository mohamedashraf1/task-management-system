package banquemisr.challenge05.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private Long taskId;
    @Size(max = 225, message = "Title must not exceed 225 characters")
    @NotBlank(message = "Title cannot be blank")
    private String title;
    @Size(max = 500, message = "Description must not exceed 500 characters")
    @NotBlank(message = "Description cannot be blank")
    private String description;
    @Min(value = 1, message = "Priority must be at least 1")
    @Max(value = 5, message = "Priority must be no more than 5")
    @NotNull(message = "priority cannot be blank")
    private Integer priority;
    @NotNull(message = "Due Date cannot be blank")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone="GMT+2")
    private Date dueDate;
    @Min(value = 1, message = "This is not a valid status")
    @Max(value = 3, message = "This is not a valid status")
    @NotNull(message = "Status cannot be blank")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer statusId;
    private String status;
}
