//package Bilgi.TDM.Controller;
//
//import Bilgi.TDM.Entity.User;
//import Bilgi.TDM.Service.UserService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    // Güvenlik için gerekli bean’leri test context’ine dahil ediyoruz.
//    @MockBean
//    private Bilgi.TDM.Security.JWTUtil jwtUtil;
//
//    @MockBean
//    private Bilgi.TDM.Security.CustomUserDetailsService customUserDetailsService;
//
//    @MockBean
//    private UserService userService;
//
//    @Test
//    public void testRegisterUser_Success() throws Exception {
//        User newUser = new User();
//        newUser.setId(1L);
//        newUser.setUsername("testuser");
//        newUser.setPassword("password"); // Gerçek uygulamada bu şifre encode edilmiş olur
//
//        when(userService.registerUser(any(User.class))).thenReturn(newUser);
//
//        String userJson = objectMapper.writeValueAsString(newUser);
//
//        mockMvc.perform(post("/users/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(userJson))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.username").value("testuser"));
//    }
//
//    @Test
//    public void testLoginUser_Success() throws Exception {
//        User loginUser = new User();
//        loginUser.setUsername("testuser");
//        loginUser.setPassword("password");
//
//        // Örneğin login işlemi başarılı olduğunda bir token string’i dönsün.
//        String token = "dummy-jwt-token";
//        when(userService.loginUser(any(User.class))).thenReturn(token);
//
//        String loginJson = objectMapper.writeValueAsString(loginUser);
//
//        mockMvc.perform(post("/users/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(loginJson))
//                .andExpect(status().isOk())
//                .andExpect(content().string(token));
//    }
//
//    // Diğer UserController test metotlarını ekleyebilirsiniz.
//}
