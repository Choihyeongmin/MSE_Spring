package rph.dto.user;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequest {
    @NotBlank(message = "Username is a required field.")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters.")
    private String username;

    @NotBlank(message = "Password is a required field.")
    @Size(min = 4, max = 20, message = "Password must be between 4 and 20 characters.")
    private String password;

    @NotBlank(message = "Nickname is a required field.")
    @Size(min = 2, max = 20, message = "Nickname must be between 2 and 20 characters.")
    private String nickname;
}
