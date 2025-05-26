package unipay.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import unipay.dto.*;
import unipay.security.JWTUtil;
import unipay.service.UserService;

/**
 * Controller for user authentication and profile operations.
 * Offers endpoints to register, login, fetch user details,
 * and update password or vehicle plate.
 */
@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    /**
     * Initializes UserController with required services.
     *
     * @param userService service handling user registration, login, and updates
     * @param jwtUtil     utility for generating and validating JWT tokens
     */
    public UserController(UserService userService, JWTUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Registers a new user with the provided details.
     *
     * @param registerRequest validated DTO containing username, email, password, etc.
     * @return the created user's public response data
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegisterRequest registerRequest) {
        UserResponse userResponse = userService.registerUser(registerRequest);
        return ResponseEntity.ok(userResponse);
    }

    /**
     * Authenticates a user and returns a JWT token on success.
     *
     * @param loginRequest validated DTO containing login credentials
     * @return 200 OK with token and user info, or 401 Unauthorized with error message
     */
    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@Valid @RequestBody UserLoginRequest loginRequest) {
        try {
            UserResponse userResponse = userService.loginUser(loginRequest);
            String token = jwtUtil.generateToken(userResponse.getUsername());
            return ResponseEntity.ok(new AuthenticationResponse(token, userResponse));
        } catch (RuntimeException e) {
            // Return 401 if authentication fails
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    /**
     * Retrieves the authenticated user's details.
     *
     * @param auth injected authentication principal
     * @return 200 OK with user info, or 401 Unauthorized if not authenticated
     */
    @GetMapping("/details")
    public ResponseEntity<UserResponse> getUserDetails(@AuthenticationPrincipal UserDetails auth) {
        if (auth == null) {
            return ResponseEntity.status(401).build();
        }
        UserResponse userResponse = userService.getUserDetails(auth.getUsername());
        return ResponseEntity.ok(userResponse);
    }

    /**
     * Updates the authenticated user's password.
     *
     * @param auth injected authentication principal
     * @param req  validated DTO containing current and new passwords
     * @return 200 OK with updated user info
     */
    @PutMapping("/updatePassword")
    public ResponseEntity<UserResponse> updateUserPassword(@AuthenticationPrincipal UserDetails auth, @Valid @RequestBody UserPasswordUpdateRequest req) {
        UserResponse updated = userService.updateUserPassword(auth.getUsername(), req);
        return ResponseEntity.ok(updated);
    }

    /**
     * Updates the authenticated user's vehicle plate.
     *
     * @param auth injected authentication principal
     * @param req  validated DTO containing the new plate number
     * @return 200 OK with updated user info
     */
    @PutMapping("/updatePlate")
    public ResponseEntity<UserResponse> updateUserPlate(@AuthenticationPrincipal UserDetails auth, @Valid @RequestBody UserPlateUpdateRequest req) {
        UserResponse updated = userService.updateUserPlate(auth.getUsername(), req);
        return ResponseEntity.ok(updated);
    }

}
