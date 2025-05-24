package rph.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import rph.dto.*;
import rph.dto.user.*;
import rph.entity.RefreshToken;
import rph.entity.User;
import rph.exception.ErrorCode.CommonErrorCode;
import rph.exception.ErrorCode.UserErrorCode;
import rph.exception.RestApiException;
import rph.exception.UserException;
import rph.external.GoogleTokenVerifier;
import rph.jwt.JwtTokenProvider;
import rph.repository.RefreshTokenRepository;
import rph.repository.UserRepository;
import rph.util.PasswordUtil;

@Service
public class UserServiceImpl implements UserService {

    private final GoogleTokenVerifier tokenVerifier;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public UserServiceImpl(
        GoogleTokenVerifier tokenVerifier,
        UserRepository userRepository,
        JwtTokenProvider jwtTokenProvider,
        RefreshTokenRepository refreshTokenRepository
    ) {
        this.tokenVerifier = tokenVerifier;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserException(UserErrorCode.USERNAME_DUPLICATED);
        }

        String salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword(request.getPassword(), salt);

        User user = new User();
        user.setUsername(request.getUsername());
        user.setNickname(request.getNickname());
        user.setSalt(salt);
        user.setPassword(hashedPassword);
        user.setExp(0);
        user.setLevel(1);
        user.setCoins(0);
        user.setRole("ROLE_USER");

        userRepository.save(user);

        return new SignupResponse(true, "회원가입 성공!");
    }

    @Override
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
        refreshTokenRepository.save(new RefreshToken(user.getUsername(), refreshToken));

        return new LoginResponse(true, "로그인 성공!", accessToken, refreshToken);
    }

    @Override
    public LoginResponse googleLogin(GoogleLoginRequest request) {
        GoogleIdToken.Payload payload = tokenVerifier.verify(request.getIdToken());

        String email = payload.getEmail();
        String googleId = payload.getSubject();

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

    @Override
    public LoginResponse googleSignup(GoogleSignupRequest request) {
        GoogleIdToken.Payload payload = tokenVerifier.verify(request.getIdToken());

        if (request.getUsername().equals(payload.getEmail())) {
            throw new RestApiException(CommonErrorCode.INVALID_PARAMETER);
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setNickname(request.getNickname());
        user.setGoogleId(payload.getSubject());
        user.setRole("ROLE_USER"); // 꼭 추가

        userRepository.save(user);

        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        return new LoginResponse(true, "로그인 성공!", accessToken, refreshToken);
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }
}
