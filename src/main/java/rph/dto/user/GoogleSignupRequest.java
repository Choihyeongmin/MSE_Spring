package rph.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoogleSignupRequest {
    private String idToken;
    private String username;
    private String nickname;
}
