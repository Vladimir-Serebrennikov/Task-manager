package hexlet.code.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class TaskStatusUpdateDTO {
    @Column(unique = true)
    @NotBlank
    @Size(min = 3)
    private JsonNullable<String> name;

    @Column(unique = true)
    @NotBlank
    @Size(min = 3)
    private JsonNullable<String> slug;
}
