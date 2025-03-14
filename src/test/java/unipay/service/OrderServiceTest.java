//package Bilgi.TDM.Service;
//
//import Bilgi.TDM.DTO.OrderRequest;
//import Bilgi.TDM.DTO.OrderResponse;
//import Bilgi.TDM.DTO.OrderUpdateRequest;
//import Bilgi.TDM.Entity.Order;
//import Bilgi.TDM.Entity.OrderItem;
//import Bilgi.TDM.Entity.OrderStatus;
//import Bilgi.TDM.Entity.Restaurant;
//import Bilgi.TDM.Entity.User;
//import Bilgi.TDM.Exception.OrderNotFoundException;
//import Bilgi.TDM.Mapper.OrderMapper;
//import Bilgi.TDM.Repository.OrderRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class OrderServiceTest {
//
//    @Mock
//    private OrderRepository orderRepository;
//
//    @Mock
//    private RestaurantService restaurantService;
//
//    @Mock
//    private UserService userService;
//
//    @Mock
//    private OrderMapper orderMapper;
//
//    @InjectMocks
//    private OrderService orderService;
//
//    @Test
//    public void testPlaceOrder_Success() {
//        // Arrange
//        Long userId = 1L;
//        OrderRequest orderRequest = new OrderRequest();
//        orderRequest.setRestaurantId(10L);
//        orderRequest.setTotalAmount(100.0);
//        // Örnek OrderItemRequest nesnesi oluşturuyoruz.
//        Bilgi.TDM.DTO.OrderItemRequest itemRequest = new Bilgi.TDM.DTO.OrderItemRequest();
//        itemRequest.setItemName("Item 1");
//        itemRequest.setQuantity(2);
//        itemRequest.setPrice(50.0);
//        orderRequest.setItems(Arrays.asList(itemRequest));
//
//        User user = new User();
//        user.setId(userId);
//        user.setUsername("testuser");
//
//        Restaurant restaurant = new Restaurant();
//        restaurant.setId(10L);
//        restaurant.setName("Test Restaurant");
//
//        Order order = new Order();
//        order.setId(100L);
//        order.setUser(user);
//        order.setRestaurant(restaurant);
//        order.setOrderTime(LocalDateTime.now());
//        order.setStatus(OrderStatus.PENDING);
//        order.setTotalAmount(100.0);
//        order.setOrderItems(Arrays.asList(new OrderItem()));
//
//        OrderResponse orderResponse = new OrderResponse();
//        orderResponse.setOrderId(100L);
//        orderResponse.setRestaurantName("Test Restaurant");
//
//        when(userService.getUserById(userId)).thenReturn(user);
//        when(restaurantService.getRestaurantById(10L)).thenReturn(restaurant);
//        when(orderRepository.save(any(Order.class))).thenReturn(order);
//        when(orderMapper.toOrderResponse(order)).thenReturn(orderResponse);
//
//        // Act
//        OrderResponse response = orderService.placeOrder(userId, orderRequest);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(100L, response.getOrderId());
//        verify(orderRepository, times(1)).save(any(Order.class));
//    }
//
//    @Test
//    public void testUpdateOrderStatus_Success() {
//        // Arrange
//        OrderUpdateRequest updateRequest = new OrderUpdateRequest();
//        updateRequest.setOrderId(100L);
//        updateRequest.setStatus(OrderStatus.COMPLETED);
//        updateRequest.setEstimatedPreparationTime(30);
//
//        Order order = new Order();
//        order.setId(100L);
//        order.setStatus(OrderStatus.PENDING);
//
//        OrderResponse orderResponse = new OrderResponse();
//        orderResponse.setOrderId(100L);
//        orderResponse.setRestaurantName("Test Restaurant");
//        // İsteğe bağlı: orderResponse nesnesine status bilgisi eklenebilir.
//
//        when(orderRepository.findById(100L)).thenReturn(Optional.of(order));
//        when(orderRepository.save(order)).thenReturn(order);
//        when(orderMapper.toOrderResponse(order)).thenReturn(orderResponse);
//
//        // Act
//        OrderResponse response = orderService.updateOrderStatus(updateRequest);
//
//        // Assert
//        assertNotNull(response);
//        // Eğer OrderResponse DTO’nuzda status alanı varsa, kontrol edilebilir.
//        verify(orderRepository, times(1)).save(order);
//    }
//
//    @Test
//    public void testUpdateOrderStatus_OrderNotFound() {
//        // Arrange
//        OrderUpdateRequest updateRequest = new OrderUpdateRequest();
//        updateRequest.setOrderId(100L);
//        updateRequest.setStatus(OrderStatus.COMPLETED);
//
//        when(orderRepository.findById(100L)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        Exception exception = assertThrows(OrderNotFoundException.class, () -> {
//            orderService.updateOrderStatus(updateRequest);
//        });
//        assertEquals("Order not found", exception.getMessage());
//    }
//}
