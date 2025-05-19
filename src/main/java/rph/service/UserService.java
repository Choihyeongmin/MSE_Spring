package rph.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import rph.dto.*;
import rph.dto.user.GoogleLoginFalseResponse;
import rph.dto.user.GoogleLoginRequest;
import rph.dto.user.GoogleSignupRequest;
import rph.dto.user.LoginRequest;
import rph.dto.user.LoginResponse;
import rph.dto.user.SignupRequest;
import rph.dto.user.SignupResponse;
import rph.entity.User;
import rph.exception.CommonErrorCode;
import rph.exception.ErrorResponse;
import rph.exception.RestApiException;
import rph.external.GoogleTokenVerifier;
import rph.jwt.JwtTokenProvider;
import rph.repository.UserRepository;

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

    public SignupResponse signup(SignupRequest request) {

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
    
        String token = jwtTokenProvider.generateToken(user.getUsername());
    
        return new LoginResponse(true, "로그인 성공!", token);
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

        String token = jwtTokenProvider.generateToken(user.getUsername());
        return new LoginResponse(true, "로그인 성공!", token);
    }

}
