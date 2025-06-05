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
import org.springframework.http.ResponseEntity;

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
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignupRequest request) {
    userService.signup(request);  
    return ResponseEntity.ok("Signup Success!");
    }


    
    @GetMapping("/signup/{username}")
    public ResponseEntity<String> checkUsername(@PathVariable String username) {
    if (!userService.isUsernameAvailable(username)) {
        throw new UserException(UserErrorCode.USERNAME_DUPLICATED);
    }
    return ResponseEntity.ok("The username is available.");
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
    LoginResponse response = userService.login(request);  
    return ResponseEntity.ok(response);  // only response succuess
    }

    @PostMapping("/refresh")  // Get new AccessToken
    public ResponseEntity<TokenRefreshResponse> refreshAccessToken(@RequestBody TokenRefreshRequest request) {
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

    public record UserInfoResponse(String username, String nickname, int exp, int level, int coins) {}

    @GetMapping("/me") //Get my information
    public ResponseEntity<UserInfoResponse> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
    User user = userDetails.getUser();
    UserInfoResponse response = new UserInfoResponse(
        user.getUsername(),
        user.getNickname(),
        user.getExp(),
        user.getLevel(),
        user.getCoins()
    );
    return ResponseEntity.ok(response);
    }
    
    @PutMapping("/me")    // User data update, can change Password + Nickname
    public ResponseEntity<String> updateUser(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody @Valid UserUpdateRequest request
    ) {
        userService.updateUser(userDetails.getUser(), request);
        return ResponseEntity.ok("User info updated.");
    }

    @DeleteMapping("/me") // Delete myself
    public ResponseEntity<String> deleteCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
    User user = userDetails.getUser();  // 현재 로그인한 유저 객체
    userService.deleteUser(user);
    return ResponseEntity.ok("Your account has been successfully deleted.");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
    return ResponseEntity.ok("Logout successful (please delete the token on the client side)"); 
    // The client should delete the token; ensure the JWT is removed so it won't be sent in the Authorization header. 
   }

    @PostMapping("/google/login")
    public ResponseEntity<LoginResponse> googleLogin(@RequestBody GoogleLoginRequest request) {
                System.out.println("googleloginCont");

        LoginResponse response = userService.googleLogin(request);
        return ResponseEntity.ok(response);  // HTTP 200 OK 
    }

    @PostMapping("/google/signup")
    public ResponseEntity<LoginResponse> googleLogin(@RequestBody GoogleSignupRequest request) {
        LoginResponse response = userService.googleSignup(request);
        return ResponseEntity.ok(response);  // HTTP 200 OK
    }
}
