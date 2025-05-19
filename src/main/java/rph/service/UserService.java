package rph.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rph.exception.ErrorCode.UserErrorCode;
import rph.exception.UserException;
import rph.dto.*;
import rph.entity.RefreshToken;
import rph.entity.User;
import rph.jwt.JwtTokenProvider;
import rph.repository.RefreshTokenRepository;
import rph.repository.UserRepository;
import rph.util.PasswordUtil;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private final RefreshTokenRepository refreshTokenRepository;


    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
        throw new UserException(UserErrorCode.USERNAME_DUPLICATED);
        }

        String salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword(request.getPassword(), salt);

        User user = new User();
        user.setUsername(request.getUsername());
        user.setNickname(request.getNickname());
        user.setSalt(salt);  // 저장
        user.setPassword(hashedPassword);  // 해시 저장
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

        String hashedInput = PasswordUtil.hashPassword(request.getPassword(), user.getSalt());
        
        if (!hashedInput.equals(user.getPassword())) {
        throw new UserException(UserErrorCode.INVALID_PASSWORD);
        }

        String token = jwtTokenProvider.generateAccessToken(user.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        refreshTokenRepository.save(new RefreshToken(user.getUsername(), refreshToken)); // 리프레시 토큰 저장

    return new LoginResponse(true, "로그인 성공!", token);
    }

    public boolean isUsernameAvailable(String username) {
    return !userRepository.existsByUsername(username);
    }
    
}
