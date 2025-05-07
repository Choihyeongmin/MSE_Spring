package rph.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequest {
    public String username;
    public String password;
    private String nickname; 
}
