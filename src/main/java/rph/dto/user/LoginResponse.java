package rph.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private Long userId;
    private boolean success;
    private String message;
    private String accessToken;
    private String refreshToken;


}
