package rph.dto.user;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class UserUpdateRequest {
    @NotBlank
    private String nickname;

    @NotBlank
    private String password;
}
