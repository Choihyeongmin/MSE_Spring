package rph.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import rph.dto.*;
import rph.dto.user.*;
import rph.entity.Item;
import rph.entity.RefreshToken;
import rph.entity.User;
import rph.entity.UserItem;
import rph.exception.ErrorCode.CommonErrorCode;
import rph.exception.ErrorCode.UserErrorCode;
import rph.exception.RestApiException;
import rph.exception.UserException;
import rph.external.GoogleTokenVerifier;
import rph.jwt.JwtTokenProvider;
import rph.repository.ItemRepository;
import rph.repository.RefreshTokenRepository;
import rph.repository.UserItemRepository;
import rph.repository.UserRepository;
import rph.util.PasswordUtil;

@Service
public class UserServiceImpl implements UserService {

    private final GoogleTokenVerifier tokenVerifier; // For verifying Google ID tokens
    private final UserRepository userRepository; // Repository for user
    private final JwtTokenProvider jwtTokenProvider; // JWT token provider
    private final RefreshTokenRepository refreshTokenRepository; // Repository for refresh tokens
    private final ItemRepository itemRepository; // Repository for item
    private final UserItemRepository userItemRepository; // Repository for user-item relationship

    @Autowired
    public UserServiceImpl(
        GoogleTokenVerifier tokenVerifier,
        UserRepository userRepository,
        JwtTokenProvider jwtTokenProvider,
        RefreshTokenRepository refreshTokenRepository,
        ItemRepository itemRepository,
        UserItemRepository userItemRepository
    ) {
        this.tokenVerifier = tokenVerifier;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.itemRepository = itemRepository;
        this.userItemRepository = userItemRepository;
    }

    @Override
    public SignupResponse signup(SignupRequest request) {
        // Check for duplicated username and nickname
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserException(UserErrorCode.USERNAME_DUPLICATED);
        }
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new UserException(UserErrorCode.NICKNAME_DUPLICATED);
        }

        // Create new user with hashed password
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
        userRepository.save(user); // 저장 후 user.getId() 자동 설정됨

        // Give 5 of itemId 1, 2, 3 each to the new user
        for (long itemId = 1L; itemId <= 3L; itemId++) {
            Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.ITEM_NOT_FOUND));

            UserItem userItem = new UserItem();
            userItem.setUser(user);
            userItem.setItem(item);
            userItem.setCount(5);
            userItem.setOwnedAt(LocalDateTime.now());

            userItemRepository.save(userItem);
        }
        return new SignupResponse(true, "Signup Success!");
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        // Check user existence and verify password
        User user = userRepository.findByUsername(request.getUsername());
        if (user == null) {
            throw new UserException(UserErrorCode.USER_NOT_FOUND);
        }

        String hashedInput = PasswordUtil.hashPassword(request.getPassword(), user.getSalt());
        if (!hashedInput.equals(user.getPassword())) {
            throw new UserException(UserErrorCode.INVALID_PASSWORD);
        }

        // Generate and return tokens
        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());
        refreshTokenRepository.save(new RefreshToken(user.getUsername(), refreshToken));

        return new LoginResponse(user.getId(), true, "Login Success!", accessToken, refreshToken);
    }

    @Override
    public void updateUser(User user, UserUpdateRequest request) {
        // Update user nickname and password
        user.setNickname(request.getNickname());
        String hashedPassword = PasswordUtil.hashPassword(request.getPassword(), user.getSalt());
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) {
        // Delete current user
        if (user == null) {
            throw new UserException(UserErrorCode.USER_NOT_FOUND); 
        }
        try {
            userRepository.delete(user);
        } catch (Exception e) {
            throw new UserException(UserErrorCode.USER_DELETE_FAILED); 
        }
    }

    @Override
    public void deleteUserByAdmin(String username) {
        // Admin deletes a user by username
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserException(UserErrorCode.USER_NOT_FOUND); 
        }
        try {
            userRepository.delete(user);
        } catch (Exception e) {
            throw new UserException(UserErrorCode.USER_DELETE_FAILED);
        }
    }

    @Override
    public LoginResponse googleLogin(GoogleLoginRequest request) {
        // Verify Google token and login if user exists
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

        return new LoginResponse(user.getId(), true, "Login Success!", accessToken, refreshToken);
    }

    @Override
    public LoginResponse googleSignup(GoogleSignupRequest request) {
        // Signup using Google account info
        GoogleIdToken.Payload payload = tokenVerifier.verify(request.getIdToken());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setNickname(request.getNickname());
        user.setGoogleId(payload.getSubject());
        user.setRole("ROLE_USER"); 

        userRepository.save(user);

        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        return new LoginResponse(user.getId(), true, "Login Success!", accessToken, refreshToken);
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        // Check if username is available
        return !userRepository.existsByUsername(username);
    }
}