package unipay.mapper;

import unipay.dto.RestaurantResponse;
import unipay.entity.Restaurant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    RestaurantResponse toRestaurantResponse(Restaurant restaurant);
}
