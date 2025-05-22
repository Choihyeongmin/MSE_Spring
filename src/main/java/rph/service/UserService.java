package rph.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;


import rph.exception.ErrorCode.UserErrorCode;
import rph.exception.UserException;
import rph.dto.*;
import rph.dto.user.GoogleLoginFalseResponse;
import rph.dto.user.GoogleLoginRequest;
import rph.dto.user.GoogleSignupRequest;
import rph.dto.user.LoginRequest;
import rph.dto.user.LoginResponse;
import rph.dto.user.SignupRequest;
import rph.dto.user.SignupResponse;
import rph.exception.ErrorCode.UserErrorCode;
import rph.exception.UserException;
import rph.dto.*;
import rph.entity.RefreshToken;
import rph.entity.User;
import rph.exception.ErrorCode.CommonErrorCode;
import rph.exception.ErrorResponse;
import rph.exception.RestApiException;
import rph.external.GoogleTokenVerifier;
import rph.jwt.JwtTokenProvider;
import rph.repository.RefreshTokenRepository;
import rph.repository.UserRepository;
import rph.util.PasswordUtil;

@Service
public class UserService {
    private final GoogleTokenVerifier tokenVerifier;

    public UserService(GoogleTokenVerifier tokenVerifier){
        this.tokenVerifier = tokenVerifier;
    }
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;


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
        user.setRole("ROLE_USER");

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

        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        refreshTokenRepository.save(new RefreshToken(user.getUsername(), refreshToken)); // 리프레시 토큰 저장

    return new LoginResponse(true, "로그인 성공!", accessToken, refreshToken);
    }

  
    
    public LoginResponse googleLogin(GoogleLoginRequest request) {
        GoogleIdToken.Payload payload = tokenVerifier.verify(request.getIdToken());
        
        String email = payload.getEmail();
        String googleId = payload.getSubject(); // "sub" 필드

        User user = userRepository.findByGoogleId(googleId);
        if (user == null) {
            Map<String, Object> data = Map.of(
                "username", email,
                "idToken", request.getIdToken()
            );

            throw new RestApiException(CommonErrorCode.NEED_SIGNUP_GOOGLE, data);
        }
    
        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

    
        return new LoginResponse(true, "로그인 성공!", accessToken, refreshToken);
    }

    public LoginResponse googleSignup(GoogleSignupRequest request){
        GoogleIdToken.Payload payload = tokenVerifier.verify(request.getIdToken());
        if(request.getUsername() == (String) payload.getEmail()){
            throw new RestApiException(CommonErrorCode.INVALID_PARAMETER);
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setNickname(request.getNickname());
        user.setGoogleId(payload.getSubject());
        userRepository.save(user);

        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        return new LoginResponse(true, "로그인 성공!", accessToken,refreshToken);
    }
     public boolean isUsernameAvailable(String username) {
    return !userRepository.existsByUsername(username);
    }

}
