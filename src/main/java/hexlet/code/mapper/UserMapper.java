package hexlet.code.mapper;

import org.mapstruct.*;

import hexlet.code.model.User;
import hexlet.code.dto.UserDTO;
import hexlet.code.dto.UserCreateDTO;
import hexlet.code.dto.UserUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Mapper(
        uses = { JsonNullableMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {

    @Autowired
    private BCryptPasswordEncoder encoder;

    public abstract UserDTO map(User model);
    @Mapping(target = "passwordDigest", source = "password")
    public abstract User map(UserCreateDTO data);
    @Mapping(target = "password", ignore = true)
    public abstract void update(UserUpdateDTO dto, @MappingTarget User model);

    @BeforeMapping
    public void encryptPassword(UserCreateDTO data) {
        var password = data.getPassword();
        data.setPassword(encoder.encode(password));
    }

}
