package rph.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoogleLoginFalseResponse {
    private String googleId;
    private String googleEmail;
    private String name;
}
