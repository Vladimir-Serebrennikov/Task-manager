package hexlet.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
    private TaskStatus status;
    private List<Label> labels;
}
