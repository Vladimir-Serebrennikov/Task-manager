package hexlet.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hexlet.code.model.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.*;
import java.util.Set;

@Getter
@Setter
public class TaskDTO {
    private long id;
    private String title;
    private int index;
    private String content;
    private String status;
    private LocalDate createdAt;
    @JsonProperty("assignee_id")
    private long assigneeId;
    private Set<Long> taskLabelIds;

}
