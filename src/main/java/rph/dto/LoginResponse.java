package rph.dto;

<<<<<<< HEAD
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private boolean success;
    private String message;
    private String token;


}
=======
public class LoginResponse {
    public boolean success;
    public String message;

    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    public LoginResponse(){}
}
>>>>>>> Dev
