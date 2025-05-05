package rph.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rph.dto.*;
import rph.entity.User;
import rph.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByUsername(request.username)) {
            return new SignupResponse(false, "Username already taken");
        }

        User newUser = new User();
        newUser.setUsername(request.username);
        newUser.setPassword(request.password); // 실무에선 암호화 필요
        userRepository.save(newUser);

        return new SignupResponse(true, "Signup successful");
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username);
        if (user == null || !user.getPassword().equals(request.password)) {
            return new LoginResponse(false, "Invalid username or password");
        }
        return new LoginResponse(true, "Login successful");
    }
}
