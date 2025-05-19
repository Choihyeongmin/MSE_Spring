package rph.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.java.rph.exception.UserErrorCode;
import main.java.rph.exception.UserException;
import rph.dto.*;
import rph.entity.User;
import rph.jwt.JwtTokenProvider;
import rph.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
        throw new UserException(UserErrorCode.USERNAME_DUPLICATED);
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setNickname(request.getNickname());
        user.setExp(0);
        user.setLevel(1);
        user.setCoins(0);

        userRepository.save(user);

        return new SignupResponse(true, "회원가입 성공!");
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername());
            if (user == null) {
        throw new UserException(UserErrorCode.USER_NOT_FOUND);
        }

            if (!user.getPassword().equals(request.getPassword())) {
        throw new UserException(UserErrorCode.INVALID_PASSWORD);
        }

        String token = jwtTokenProvider.generateToken(user.getUsername());
        return new LoginResponse(true, "로그인 성공!", token);
    }

    public boolean isUsernameAvailable(String username) {
    return !userRepository.existsByUsername(username);
    }
    
}
