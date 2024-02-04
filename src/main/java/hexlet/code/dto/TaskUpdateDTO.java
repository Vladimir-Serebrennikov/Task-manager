package hexlet.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hexlet.code.model.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class TaskUpdateDTO {
    @NotNull
    private JsonNullable<String> title;
    private JsonNullable<String> content;
    private JsonNullable<Integer> index;
    private JsonNullable<TaskStatus> status;
    @JsonProperty("assignee_id")
    private JsonNullable<Long> assigneeId;
}
