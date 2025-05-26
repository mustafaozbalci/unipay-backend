package unipay.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import unipay.dto.OrderItemResponse;
import unipay.dto.OrderResponse;
import unipay.entity.Order;
import unipay.entity.OrderItem;

import java.util.List;

/**
 * Mapper interface for converting between Order-related entities and DTOs.
 * Uses MapStruct to automatically generate implementation code.
 */
@Mapper(componentModel = "spring")
public interface OrderMapper {

    /**
     * Converts an Order entity into an OrderResponse DTO.
     * <p>
     * Maps the following properties:
     * - orderItems   -> items
     * - id           -> orderId
     * - restaurant.name -> restaurantName
     * - user.username   -> customerUsername
     *
     * @param order the source Order entity
     * @return the populated OrderResponse DTO
     */
    @Mapping(target = "items", source = "orderItems")
    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "restaurantName", source = "restaurant.name")
    @Mapping(source = "user.username", target = "customerUsername")
    OrderResponse toOrderResponse(Order order);

    /**
     * Converts a single OrderItem entity into an OrderItemResponse DTO.
     *
     * @param orderItem the source OrderItem entity
     * @return the corresponding OrderItemResponse DTO
     */
    OrderItemResponse map(OrderItem orderItem);

    /**
     * Converts a list of OrderItem entities into a list of OrderItemResponse DTOs.
     * Delegates to the map(OrderItem) method for each item.
     *
     * @param orderItems the list of OrderItem entities
     * @return the list of mapped OrderItemResponse DTOs
     */
    List<OrderItemResponse> toOrderItemResponses(List<OrderItem> orderItems);
}
