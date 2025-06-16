package rph.controller;

import rph.entity.RefreshToken;
import rph.entity.User;
import rph.security.CustomUserDetails;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rph.exception.ErrorCode.TokenErrorCode;
import rph.exception.ErrorCode.UserErrorCode;
import rph.jwt.JwtTokenProvider;
import rph.repository.RefreshTokenRepository;
import rph.exception.TokenException;
import rph.exception.UserException;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import rph.dto.*;
import rph.dto.user.GoogleLoginRequest;
import rph.dto.user.GoogleSignupRequest;
import rph.dto.user.LoginRequest;
import rph.dto.user.LoginResponse;
import rph.dto.user.SignupRequest;
import rph.dto.user.SignupResponse;
import rph.dto.user.TokenRefreshRequest;
import rph.dto.user.TokenRefreshResponse;
import rph.dto.user.UserUpdateRequest;
import rph.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider; // For token generation/validation

    @Autowired
    private RefreshTokenRepository refreshTokenRepository; // For storing refresh tokens

    @Autowired
    private UserService userService; // User business logic

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignupRequest request) {
        // User signup
        userService.signup(request);  
        return ResponseEntity.ok("Signup Success!");
    }

    @GetMapping("/signup/{username}")
    public ResponseEntity<String> checkUsername(@PathVariable String username) {
        // Check if username is available
        if (!userService.isUsernameAvailable(username)) {
            throw new UserException(UserErrorCode.USERNAME_DUPLICATED);
        }
        return ResponseEntity.ok("The username is available.");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        // Normal login
        LoginResponse response = userService.login(request);  
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refreshAccessToken(@RequestBody TokenRefreshRequest request) {
        // Refresh access token
        String refreshToken = request.getRefreshToken();

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new TokenException(TokenErrorCode.TOKEN_INVALID);
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);

        RefreshToken stored = refreshTokenRepository.findById(username)
            .orElseThrow(() -> new TokenException(TokenErrorCode.TOKEN_NOT_FOUND));

        if (!stored.getRefreshToken().equals(refreshToken)) {
            throw new TokenException(TokenErrorCode.TOKEN_MISMATCH);
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(username);
        return ResponseEntity.ok(new TokenRefreshResponse(newAccessToken));
    }

    public record UserInfoResponse(Long userId, String username, String nickname, int exp, int level, int coins) {}

    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        // Get info of the logged-in user
        User user = userDetails.getUser();
        UserInfoResponse response = new UserInfoResponse(
            user.getId(),
            user.getUsername(),
            user.getNickname(),
            user.getExp(),
            user.getLevel(),
            user.getCoins()
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<String> updateUser(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody @Valid UserUpdateRequest request
    ) {
        // Update nickname/password
        userService.updateUser(userDetails.getUser(), request);
        return ResponseEntity.ok("User info updated.");
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> deleteCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        // Delete own account
        User user = userDetails.getUser();
        userService.deleteUser(user);
        return ResponseEntity.ok("Your account has been successfully deleted.");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Logout instruction (client deletes token)
        return ResponseEntity.ok("Logout successful (please delete the token on the client side)");
    }

    @PostMapping("/google/login")
    public ResponseEntity<LoginResponse> googleLogin(@RequestBody GoogleLoginRequest request) {
        // Login via Google ID token
        LoginResponse response = userService.googleLogin(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/google/signup")
    public ResponseEntity<LoginResponse> googleLogin(@RequestBody GoogleSignupRequest request) {
        // Signup via Google ID token
        LoginResponse response = userService.googleSignup(request);
        return ResponseEntity.ok(response);
    }
}