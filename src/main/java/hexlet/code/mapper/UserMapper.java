package hexlet.code.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import hexlet.code.model.User;
import hexlet.code.dto.UserDTO;
import hexlet.code.dto.UserCreateDTO;
import hexlet.code.dto.UserUpdateDTO;

@Mapper(
        uses = { JsonNullableMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
    public abstract UserDTO map(User model);
    public abstract User map(UserCreateDTO data);
    public abstract void update(UserUpdateDTO dto, @MappingTarget User model);
}
