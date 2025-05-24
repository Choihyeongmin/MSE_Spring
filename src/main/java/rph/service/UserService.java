package rph.service;

import rph.dto.user.*;

public interface UserService {
    SignupResponse signup(SignupRequest request);
    LoginResponse login(LoginRequest request);
    LoginResponse googleLogin(GoogleLoginRequest request);
    LoginResponse googleSignup(GoogleSignupRequest request);
    boolean isUsernameAvailable(String username);
}
