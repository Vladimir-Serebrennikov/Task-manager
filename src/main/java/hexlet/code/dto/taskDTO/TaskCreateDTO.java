package hexlet.code.dto.taskDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class TaskCreateDTO {
    private int index;
    @JsonProperty("assignee_id")
    private Long assigneeId;
    @NotBlank
    @Size(min = 1)
    private String title;
    private String content;
    @NotNull
    private String status;
    private Set<Long> taskLabelIds;
}
