package rph.service;

import rph.entity.User;
import rph.dto.user.*;

public interface UserService {
    SignupResponse signup(SignupRequest request);
    LoginResponse login(LoginRequest request);
    LoginResponse googleLogin(GoogleLoginRequest request);
    LoginResponse googleSignup(GoogleSignupRequest request);
    boolean isUsernameAvailable(String username);
    void updateUser(User user, UserUpdateRequest request);
    void deleteUser(User user);
}
