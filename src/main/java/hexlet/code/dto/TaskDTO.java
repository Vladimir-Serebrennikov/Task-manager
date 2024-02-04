package hexlet.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hexlet.code.model.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskDTO {
    private long id;
    private String title;
    private int index;
    private String content;
    private TaskStatus status;
    private LocalDate createdAt;
    @JsonProperty("assignee_id")
    private long assigneeId;

}
