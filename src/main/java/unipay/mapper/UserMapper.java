package unipay.mapper;

import unipay.dto.UserResponse;
import unipay.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);
}
