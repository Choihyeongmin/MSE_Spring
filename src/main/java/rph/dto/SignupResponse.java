package rph.dto;

<<<<<<< HEAD
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupResponse {
    private boolean success;
    private String message;
=======
public class SignupResponse {
    public boolean success;
    public String message;

    public SignupResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    public SignupResponse(){}
>>>>>>> Dev
}
