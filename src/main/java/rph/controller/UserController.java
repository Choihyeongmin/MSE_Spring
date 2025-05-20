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
import rph.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserService userService;

   @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignupRequest request) {
    userService.signup(request);  // 예외 발생 시 GlobalExceptionHandler가 처리
    return ResponseEntity.ok("회원가입 성공!");
    }


    
    @GetMapping("/signup/{username}")
    public ResponseEntity<String> checkUsername(@PathVariable String username) {
    if (!userService.isUsernameAvailable(username)) {
        throw new UserException(UserErrorCode.USERNAME_DUPLICATED);
    }
    return ResponseEntity.ok("사용 가능한 아이디입니다.");
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
    LoginResponse response = userService.login(request);  // 실패 시 내부에서 예외 발생
    return ResponseEntity.ok(response);  // 성공만 처리
    }

    @PostMapping("/refresh")
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

    @GetMapping("/me")
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

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
    return ResponseEntity.ok("로그아웃 성공 (클라이언트에서 토큰 삭제하세요)"); //클라에서 토큰을 삭제 해야함, jwt 자체 삭제해서 헤더에 인증안오게
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
