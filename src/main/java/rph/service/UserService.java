package rph.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rph.dto.*;
import rph.entity.User;
<<<<<<< HEAD
import rph.jwt.JwtTokenProvider;
=======
>>>>>>> Dev
import rph.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

<<<<<<< HEAD
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return new SignupResponse(false, "이미 존재하는 ID입니다.");
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
        if (user == null || !user.getPassword().equals(request.getPassword())) {
            return new LoginResponse(false, "아이디 또는 비밀번호 오류",null);
        }
    
        String token = jwtTokenProvider.generateToken(user.getUsername());
    
        return new LoginResponse(true, "로그인 성공!", token);
=======
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
>>>>>>> Dev
    }
}
