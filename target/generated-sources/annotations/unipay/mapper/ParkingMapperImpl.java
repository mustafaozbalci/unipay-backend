package unipay.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import unipay.dto.ParkingSessionDto;
import unipay.entity.ParkingArea;
import unipay.entity.ParkingSession;
import unipay.entity.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-30T00:50:44+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class ParkingMapperImpl implements ParkingMapper {

    @Override
    public ParkingSessionDto toDto(ParkingSession session) {
        if ( session == null ) {
            return null;
        }

        ParkingSessionDto parkingSessionDto = new ParkingSessionDto();

        parkingSessionDto.setParkingAreaId( sessionParkingAreaId( session ) );
        parkingSessionDto.setUserId( sessionUserId( session ) );
        parkingSessionDto.setId( session.getId() );
        parkingSessionDto.setPlate( session.getPlate() );
        parkingSessionDto.setEnterTime( session.getEnterTime() );
        parkingSessionDto.setExitTime( session.getExitTime() );
        parkingSessionDto.setFee( session.getFee() );

        return parkingSessionDto;
    }

    private Long sessionParkingAreaId(ParkingSession parkingSession) {
        if ( parkingSession == null ) {
            return null;
        }
        ParkingArea parkingArea = parkingSession.getParkingArea();
        if ( parkingArea == null ) {
            return null;
        }
        Long id = parkingArea.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long sessionUserId(ParkingSession parkingSession) {
        if ( parkingSession == null ) {
            return null;
        }
        User user = parkingSession.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
