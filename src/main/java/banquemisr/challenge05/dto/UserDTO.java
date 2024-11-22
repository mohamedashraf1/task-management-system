package banquemisr.challenge05.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long userId;
    @NotBlank(message = "email cannot be blank")
    @Email
    private String email;
    @NotBlank(message = "password cannot be blank")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @NotBlank(message = "name cannot be blank")
    private String name;
    @NotBlank(message = "mobile number cannot be blank")
    @Pattern(
            regexp = "^(010|011|012)\\d{8}$",
            message = "Mobile number must start with 010, 011, or 012 and contain exactly 8 digits after that."
    )
    private String mobileNumber;
    private List<TaskDTO> tasks;
}
