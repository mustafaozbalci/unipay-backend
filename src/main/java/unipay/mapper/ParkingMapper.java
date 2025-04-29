// src/main/java/unipay/mapper/ParkingMapper.java
package unipay.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import unipay.dto.ParkingSessionDto;
import unipay.entity.ParkingSession;

@Mapper(componentModel = "spring")
public interface ParkingMapper {

    @Mapping(target = "parkingAreaId", source = "parkingArea.id")
    @Mapping(target = "userId",      source = "user.id")
    ParkingSessionDto toDto(ParkingSession session);
}
