package unipay.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import unipay.dto.UserResponse;
import unipay.entity.User;

/**
 * Mapper interface for converting User entities into UserResponse DTOs.
 * Uses MapStruct to generate the implementation at compile time.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Maps a User entity to its corresponding UserResponse DTO.
     *
     * @param user the source User entity
     * @return the mapped UserResponse DTO
     */
    @Mapping(target = "plate", source = "plate")
    UserResponse toUserResponse(User user);
}
