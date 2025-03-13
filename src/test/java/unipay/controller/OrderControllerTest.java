//package Bilgi.TDM.Controller;
//
//import Bilgi.TDM.Entity.Order;
//import Bilgi.TDM.Service.OrderService;
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
//public class OrderControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    // Güvenlik ve diğer bağımlılıkların test context’ine dahil edilmesi için
//    @MockBean
//    private Bilgi.TDM.Security.JWTUtil jwtUtil;
//
//    @MockBean
//    private OrderService orderService;
//
//    @Test
//    public void testPlaceOrder() throws Exception {
//        // Örnek: OrderService.placeOrder çağrısından dönen order
//        Order mockOrder = new Order();
//        mockOrder.setId(100L);
//        mockOrder.setDescription("Test Order");
//
//        when(orderService.placeOrder(any())).thenReturn(mockOrder);
//
//        // Controller endpoint’ine gönderilecek JSON verisi
//        String orderJson = objectMapper.writeValueAsString(mockOrder);
//
//        mockMvc.perform(post("/orders")  // Endpoint URL’inizi uygulamanıza göre ayarlayın
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(orderJson))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(100))
//                .andExpect(jsonPath("$.description").value("Test Order"));
//    }
//
//    // Diğer OrderController test metotlarını da ekleyebilirsiniz.
//}
