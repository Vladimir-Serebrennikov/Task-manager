package hexlet.code.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
public class UserCreateDTO{
    @Column(unique = true)
    @Email
    @NotBlank
    private String email;
    private String firstName;
    private String lastName;
    @NotBlank
    @Size(min = 3)
    private String password;
}
