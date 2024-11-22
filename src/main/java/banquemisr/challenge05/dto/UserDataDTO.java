package banquemisr.challenge05.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDataDTO {
    private Long userId;
    @NotBlank(message = "name cannot be blank")
    private String name;
    @NotBlank(message = "mobile number cannot be blank")
    @Pattern(
            regexp = "^(010|011|012)\\d{8}$",
            message = "Mobile number must start with 010, 011, or 012 and contain exactly 8 digits after that."
    )
    private String mobileNumber;
}
