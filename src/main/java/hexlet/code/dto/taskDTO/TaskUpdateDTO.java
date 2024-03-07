package hexlet.code.dto.taskDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Getter
@Setter
public class TaskUpdateDTO {
    @NotNull
    private JsonNullable<String> title;
    private JsonNullable<String> content;
    private JsonNullable<Integer> index;
    @NotNull
    private JsonNullable<String> status;
    @JsonProperty("assignee_id")
    private JsonNullable<Long> assigneeId;
    @NotNull
    private JsonNullable<Set<Long>> taskLabelIds;
}
