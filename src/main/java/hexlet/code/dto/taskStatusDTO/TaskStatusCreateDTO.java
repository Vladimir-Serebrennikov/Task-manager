package hexlet.code.dto.taskStatusDTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskStatusCreateDTO {
    @Column(unique = true)
    @NotBlank
    @Size(min = 3)
    private String name;

    @Column(unique = true)
    @NotBlank
    @Size(min = 3)
    private String slug;
}
