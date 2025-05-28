package rph.dto.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {

        @NotBlank(message = "Username is a required field.")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters.")
    public String username;

    @NotBlank(message = "Password is a required field.")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
    public String password;
}
