package rph.controller;
import rph.entity.User;
import rph.security.CustomUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;

import rph.dto.*;
import rph.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public SignupResponse signup(@RequestBody SignupRequest request) {
        return userService.signup(request);
    }

    @PostMapping("/login")
public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
    LoginResponse response = userService.login(request);

    if (response.isSuccess()) {
        return ResponseEntity.ok(response);  // HTTP 200 OK
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);  // HTTP 401 Unauthorized
    }
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal rph.security.CustomUserDetails userDetails) {
    User user = userDetails.getUser();
    return new ResponseEntity<>(user, HttpStatus.OK);
    }   

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
    return ResponseEntity.ok("로그아웃 성공 (클라이언트에서 토큰 삭제하세요)"); //클라에서 토큰을 삭제 해야함, jwt 자체 삭제해서 헤더에 인증안오게
    }

}
