package unipay.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import unipay.dto.OrderItemResponse;
import unipay.dto.OrderResponse;
import unipay.entity.Order;
import unipay.entity.OrderItem;
import unipay.entity.Restaurant;
import unipay.entity.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-18T17:41:11+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderResponse toOrderResponse(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderResponse orderResponse = new OrderResponse();

        orderResponse.setItems( toOrderItemResponses( order.getOrderItems() ) );
        orderResponse.setOrderId( order.getId() );
        orderResponse.setRestaurantName( orderRestaurantName( order ) );
        orderResponse.setCustomerUsername( orderUserUsername( order ) );
        orderResponse.setOrderTime( order.getOrderTime() );
        if ( order.getStatus() != null ) {
            orderResponse.setStatus( order.getStatus().name() );
        }
        orderResponse.setTotalAmount( order.getTotalAmount() );
        orderResponse.setEstimatedPreparationTime( order.getEstimatedPreparationTime() );

        return orderResponse;
    }

    @Override
    public OrderItemResponse map(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }

        OrderItemResponse orderItemResponse = new OrderItemResponse();

        orderItemResponse.setItemName( orderItem.getItemName() );
        orderItemResponse.setQuantity( orderItem.getQuantity() );
        orderItemResponse.setPrice( orderItem.getPrice() );

        return orderItemResponse;
    }

    @Override
    public List<OrderItemResponse> toOrderItemResponses(List<OrderItem> orderItems) {
        if ( orderItems == null ) {
            return null;
        }

        List<OrderItemResponse> list = new ArrayList<OrderItemResponse>( orderItems.size() );
        for ( OrderItem orderItem : orderItems ) {
            list.add( map( orderItem ) );
        }

        return list;
    }

    private String orderRestaurantName(Order order) {
        if ( order == null ) {
            return null;
        }
        Restaurant restaurant = order.getRestaurant();
        if ( restaurant == null ) {
            return null;
        }
        String name = restaurant.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String orderUserUsername(Order order) {
        if ( order == null ) {
            return null;
        }
        User user = order.getUser();
        if ( user == null ) {
            return null;
        }
        String username = user.getUsername();
        if ( username == null ) {
            return null;
        }
        return username;
    }
}
