package unipay.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import unipay.dto.UserResponse;
import unipay.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "plate", source = "plate")
    UserResponse toUserResponse(User user);
}
