package hexlet.code.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.*;

@Setter
@Getter
public class UserDTO {
    private long id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate createdAt;
}
