package unipay.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import unipay.dto.ParkingSessionDto;
import unipay.entity.ParkingSession;

/**
 * Mapper interface for converting ParkingSession entities to DTOs.
 * Uses MapStruct to generate implementation code at compile time.
 */
@Mapper(componentModel = "spring")
public interface ParkingMapper {

    /**
     * Converts a ParkingSession entity into a ParkingSessionDto.
     *
     * @param session the ParkingSession entity to map
     * @return the corresponding ParkingSessionDto
     */
    @Mapping(target = "parkingAreaId", source = "parkingArea.id")
    @Mapping(target = "userId", source = "user.id")
    ParkingSessionDto toDto(ParkingSession session);
}
