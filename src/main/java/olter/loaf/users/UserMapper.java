package olter.loaf.users;

import olter.loaf.users.dto.UserResponse;
import olter.loaf.users.model.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse entityToResponse(UserEntity entity);
}
