package unipay.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    // Kullanıcı Detayları Getirme (now with auth principal)
    @GetMapping("/details")
    public ResponseEntity<UserResponse> getUserDetails(@AuthenticationPrincipal UserDetails auth) {
        if (auth == null) {
            return ResponseEntity.status(401).build();
        }
        UserResponse userResponse = userService.getUserDetails(auth.getUsername());
        return ResponseEntity.ok(userResponse);
    }

    // Şifre güncelleme
    @PutMapping("/updatePassword")
    public ResponseEntity<UserResponse> updateUserPassword(@AuthenticationPrincipal UserDetails auth, @Valid @RequestBody UserPasswordUpdateRequest req) {
        UserResponse updated = userService.updateUserPassword(auth.getUsername(), req);
        return ResponseEntity.ok(updated);
    }

    // Plaka güncelleme
    @PutMapping("/updatePlate")
    public ResponseEntity<UserResponse> updateUserPlate(@AuthenticationPrincipal UserDetails auth, @Valid @RequestBody UserPlateUpdateRequest req) {
        UserResponse updated = userService.updateUserPlate(auth.getUsername(), req);
        return ResponseEntity.ok(updated);
    }

}
