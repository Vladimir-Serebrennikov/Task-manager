package hexlet.code.mapper;

import hexlet.code.dto.UserCreateDTO;
import hexlet.code.dto.UserDTO;
import hexlet.code.dto.UserUpdateDTO;
import hexlet.code.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-08T21:00:06+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.4.jar, environment: Java 20 (Oracle Corporation)"
)
@Component
public class UserMapperImpl extends UserMapper {

    @Override
    public UserDTO map(User model) {
        if ( model == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( model.getId() );
        userDTO.setEmail( model.getEmail() );
        userDTO.setFirstName( model.getFirstName() );
        userDTO.setLastName( model.getLastName() );
        userDTO.setCreatedAt( model.getCreatedAt() );

        return userDTO;
    }

    @Override
    public User map(UserCreateDTO data) {
        encryptPassword( data );

        if ( data == null ) {
            return null;
        }

        User user = new User();

        user.setPasswordDigest( data.getPassword() );
        user.setFirstName( data.getFirstName() );
        user.setLastName( data.getLastName() );
        user.setEmail( data.getEmail() );

        return user;
    }

    @Override
    public void update(UserUpdateDTO dto, User model) {
        if ( dto == null ) {
            return;
        }

        if ( JsonNullableMapper.isPresent( dto.getFirstName() ) ) {
            model.setFirstName( JsonNullableMapper.unwrap( dto.getFirstName() ) );
        }
        if ( JsonNullableMapper.isPresent( dto.getLastName() ) ) {
            model.setLastName( JsonNullableMapper.unwrap( dto.getLastName() ) );
        }
        if ( JsonNullableMapper.isPresent( dto.getEmail() ) ) {
            model.setEmail( JsonNullableMapper.unwrap( dto.getEmail() ) );
        }
    }
}
