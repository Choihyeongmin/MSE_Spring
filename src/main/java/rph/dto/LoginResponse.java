package rph.dto;

public class LoginResponse {
    public boolean success;
    public String message;

    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    public LoginResponse(){}
}