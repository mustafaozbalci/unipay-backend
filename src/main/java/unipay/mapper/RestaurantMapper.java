package unipay.mapper;

import org.mapstruct.Mapper;
import unipay.dto.RestaurantResponse;
import unipay.entity.Restaurant;

/**
 * Mapper interface for converting Restaurant entities into DTOs.
 * Uses MapStruct to generate implementation code.
 */
@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    /**
     * Converts a Restaurant entity into a RestaurantResponse DTO.
     *
     * @param restaurant the source Restaurant entity
     * @return the corresponding RestaurantResponse DTO
     */
    RestaurantResponse toRestaurantResponse(Restaurant restaurant);
}
