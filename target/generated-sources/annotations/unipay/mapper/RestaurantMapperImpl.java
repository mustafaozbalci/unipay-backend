package unipay.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import unipay.dto.RestaurantResponse;
import unipay.entity.Restaurant;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-15T02:25:58+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class RestaurantMapperImpl implements RestaurantMapper {

    @Override
    public RestaurantResponse toRestaurantResponse(Restaurant restaurant) {
        if ( restaurant == null ) {
            return null;
        }

        RestaurantResponse restaurantResponse = new RestaurantResponse();

        restaurantResponse.setId( restaurant.getId() );
        restaurantResponse.setName( restaurant.getName() );

        return restaurantResponse;
    }
}
