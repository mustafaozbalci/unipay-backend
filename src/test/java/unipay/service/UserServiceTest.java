//package Bilgi.TDM.Service;
//
//import Bilgi.TDM.DTO.UserLoginRequest;
//import Bilgi.TDM.DTO.UserRegisterRequest;
//import Bilgi.TDM.DTO.UserResponse;
//import Bilgi.TDM.Entity.User;
//import Bilgi.TDM.Exception.UserNotFoundException;
//import Bilgi.TDM.Repository.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @InjectMocks
//    private UserService userService;
//
//    @Test
//    public void testRegisterUser_Success() {
//        // Arrange
//        UserRegisterRequest request = new UserRegisterRequest();
//        request.setUsername("testuser");
//        request.setPassword("password");
//        request.setEmail("test@example.com");
//
//        User user = new User();
//        user.setId(1L);
//        user.setUsername(request.getUsername());
//        user.setEmail(request.getEmail());
//        user.setPassword("encodedPassword");
//        user.setBalance(0.0);
//
//        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
//        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
//        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
//        when(userRepository.save(any(User.class))).thenReturn(user);
//
//        // Act
//        UserResponse response = userService.registerUser(request);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals("testuser", response.getUsername());
//        verify(userRepository, times(1)).save(any(User.class));
//    }
//
//    @Test
//    public void testLoginUser_Success() {
//        // Arrange
//        UserLoginRequest loginRequest = new UserLoginRequest();
//        loginRequest.setUsername("testuser");
//        loginRequest.setPassword("password");
//
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("testuser");
//        user.setPassword("encodedPassword");
//        user.setEmail("test@example.com");
//        user.setBalance(0.0);
//
//        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
//        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
//
//        // Act
//        UserResponse response = userService.loginUser(loginRequest);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals("testuser", response.getUsername());
//    }
//
//    @Test
//    public void testLoginUser_InvalidPassword() {
//        // Arrange
//        UserLoginRequest loginRequest = new UserLoginRequest();
//        loginRequest.setUsername("testuser");
//        loginRequest.setPassword("wrongPassword");
//
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("testuser");
//        user.setPassword("encodedPassword");
//        user.setEmail("test@example.com");
//        user.setBalance(0.0);
//
//        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
//        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);
//
//        // Act & Assert
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            userService.loginUser(loginRequest);
//        });
//        assertEquals("Invalid credentials", exception.getMessage());
//    }
//
//    @Test
//    public void testGetUserById_NotFound() {
//        // Arrange
//        when(userRepository.findById(1L)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        Exception exception = assertThrows(UserNotFoundException.class, () -> {
//            userService.getUserById(1L);
//        });
//        assertEquals("User not found", exception.getMessage());
//    }
//}
