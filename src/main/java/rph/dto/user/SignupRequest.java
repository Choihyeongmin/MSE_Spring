package rph.dto.user;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequest {
    @NotBlank(message = "Username is a required field.")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters.")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Username can only contain English letters and numbers.")
    private String username;

    @NotBlank(message = "Password is a required field.")
    @Size(min = 4, max = 20, message = "Password must be between 4 and 20 characters.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).{4,20}$",
             message = "Password must include letters, numbers, and special characters, and be between 4 to 20 characters long.")
    private String password;

    @NotBlank(message = "Nickname is a required field.")
    @Size(min = 2, max = 20, message = "Nickname must be between 2 and 20 characters.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]*$", message = "Nickname can only contain Korean characters, letters, and numbers.")
    private String nickname;
    
}
