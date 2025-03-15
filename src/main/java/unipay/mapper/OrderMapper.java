package unipay.mapper;

import unipay.dto.OrderItemResponse;
import unipay.dto.OrderResponse;
import unipay.entity.Order;
import unipay.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {


    @Mapping(target = "items", source = "orderItems")
    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "restaurantName", source = "restaurant.name")
    @Mapping(source = "user.username", target = "customerUsername")
    OrderResponse toOrderResponse(Order order);


    OrderItemResponse map(OrderItem orderItem);

    List<OrderItemResponse> toOrderItemResponses(List<OrderItem> orderItems);
}
