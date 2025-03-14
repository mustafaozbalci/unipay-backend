package unipay.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unipay.dto.*;
import unipay.security.JWTUtil;
import unipay.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    public UserController(UserService userService, JWTUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // Kullanıcı Kaydı
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegisterRequest registerRequest) {
        UserResponse userResponse = userService.registerUser(registerRequest);
        return ResponseEntity.ok(userResponse);
    }

    // Kullanıcı Girişi (Başarılı ise JWT Token ile birlikte)
    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@Valid @RequestBody UserLoginRequest loginRequest) {
        try {
            UserResponse userResponse = userService.loginUser(loginRequest);
            String token = jwtUtil.generateToken(userResponse.getUsername());
            return ResponseEntity.ok(new AuthenticationResponse(token, userResponse));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    // Kullanıcı Detayları Getirme
    @PostMapping("/details")
    public ResponseEntity<UserResponse> getUserDetails(@RequestHeader("username") String username) {
        UserResponse userResponse = userService.getUserDetails(username);
        return ResponseEntity.ok(userResponse);
    }

    //Kullanıcının sadece ŞİFRESİNİ güncelleme
    @PutMapping("/updatePassword")
    public ResponseEntity<UserResponse> updateUserPassword(@RequestHeader("username") String username, @Valid @RequestBody UserPasswordUpdateRequest passwordUpdateRequest) {

        UserResponse updatedUser = userService.updateUserPassword(username, passwordUpdateRequest);
        return ResponseEntity.ok(updatedUser);
    }

}
