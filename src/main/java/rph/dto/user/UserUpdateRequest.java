package rph.dto.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


import lombok.Getter;

@Getter
public class UserUpdateRequest {

    @NotBlank
    @Size(min = 2, max = 20, message = "Nickname must be between 2 and 20 characters.")
    private String nickname;

    @NotBlank
    @Size(min = 4, max = 20, message = "Password must be between 4 and 20 characters.")
    private String password;
}
