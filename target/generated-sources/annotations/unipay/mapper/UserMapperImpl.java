package unipay.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import unipay.dto.UserResponse;
import unipay.entity.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-30T00:50:44+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponse toUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse userResponse = new UserResponse();

        userResponse.setPlate( user.getPlate() );
        userResponse.setId( user.getId() );
        userResponse.setUsername( user.getUsername() );
        userResponse.setEmail( user.getEmail() );
        userResponse.setBalance( user.getBalance() );

        return userResponse;
    }
}
