package olter.loaf.users;

import olter.loaf.users.dto.RegisterRequest;
import olter.loaf.users.dto.UserResponse;
import olter.loaf.users.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse entityToResponse(UserEntity entity);

    @Mapping(target = "name", source = "username")
    @Mapping(target = "password", ignore = true)
    UserEntity registerRequestToEntity(RegisterRequest registerRequest);
}
