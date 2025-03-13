//package Bilgi.TDM.Controller;
//
//import Bilgi.TDM.Entity.Restaurant;
//import Bilgi.TDM.Service.RestaurantService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.util.List;
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
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class RestaurantControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    // Güvenlik bean’ini test context’ine dahil ediyoruz.
//    @MockBean
//    private Bilgi.TDM.Security.JWTUtil jwtUtil;
//
//    @MockBean
//    private RestaurantService restaurantService;
//
//    @Test
//    public void testAddRestaurant() throws Exception {
//        Restaurant newRestaurant = new Restaurant();
//        newRestaurant.setId(1L);
//        newRestaurant.setName("Test Restaurant");
//
//        when(restaurantService.addRestaurant(any())).thenReturn(newRestaurant);
//
//        String restaurantJson = objectMapper.writeValueAsString(newRestaurant);
//
//        mockMvc.perform(post("/restaurants")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(restaurantJson))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.name").value("Test Restaurant"));
//    }
//
//    @Test
//    public void testDeleteRestaurant() throws Exception {
//        // Varsayalım restaurantService.deleteRestaurant metodu void döndürüyor.
//        // Ek mock davranışı gerekmez.
//        mockMvc.perform(delete("/restaurants/1"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void testGetAllRestaurants() throws Exception {
//        Restaurant r1 = new Restaurant();
//        r1.setId(1L);
//        r1.setName("Test Restaurant 1");
//
//        Restaurant r2 = new Restaurant();
//        r2.setId(2L);
//        r2.setName("Test Restaurant 2");
//
//        when(restaurantService.getAllRestaurants()).thenReturn(List.of(r1, r2));
//
//        mockMvc.perform(get("/restaurants"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(2))
//                .andExpect(jsonPath("$[0].name").value("Test Restaurant 1"));
//    }
//
//    // Diğer RestaurantController test metotlarını ekleyebilirsiniz.
//}
