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

/**
 * Service implementation for user-related operations:
 * signup, login, profile update, deletion, and Google login.
 */
@Service
public class UserServiceImpl implements UserService {

    private final GoogleTokenVerifier tokenVerifier;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ItemRepository itemRepository;
    private final UserItemRepository userItemRepository;

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

    /**
     * Register a new user (local account).
     * - Hashes password
     * - Checks for username/nickname duplication
     * - Gives default items (IDs 1,2,3) × 5
     */
    @Override
    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserException(UserErrorCode.USERNAME_DUPLICATED);
        }
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new UserException(UserErrorCode.NICKNAME_DUPLICATED);
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

        // Give default items: itemId 1, 2, 3 × 5 each
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

    /**
     * Local login.
     * - Verifies username and password
     * - Issues JWT access and refresh tokens
     */
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

        return new LoginResponse(user.getId(), true, "Login Success!", accessToken, refreshToken);
    }

    /**
     * Update user nickname and password.
     */
    @Override
    public void updateUser(User user, UserUpdateRequest request) {
        user.setNickname(request.getNickname());
        String hashedPassword = PasswordUtil.hashPassword(request.getPassword(), user.getSalt());
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }

    /**
     * Delete user account (self-deletion).
     */
    @Override
    public void deleteUser(User user) {
        if (user == null) {
            throw new UserException(UserErrorCode.USER_NOT_FOUND); 
        }
        try {
            userRepository.delete(user);
        } catch (Exception e) {
            throw new UserException(UserErrorCode.USER_DELETE_FAILED); 
        }
    }

    /**
     * Admin deletes a user by username.
     */
    @Override
    public void deleteUserByAdmin(String username) {
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

    /**
     * Google login.
     * - Verifies ID token
     * - If user not registered, throws NEED_SIGNUP_GOOGLE
     */
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

        return new LoginResponse(user.getId(), true, "Login Success!", accessToken, refreshToken);
    }

    /**
     * Signup using Google account.
     */
    @Override
    public LoginResponse googleSignup(GoogleSignupRequest request) {
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

    /**
     * Check if a username is available for signup.
     */
    @Override
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }
}
