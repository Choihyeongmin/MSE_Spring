package rph.dto.user;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Username is a required field.")
    public String username;

    @NotBlank(message = "Password is a required field.")
    public String password;
}
