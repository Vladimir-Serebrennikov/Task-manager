package hexlet.code.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.*;

@Getter
@Setter
public class LabelDTO {
    private long id;
    private String name;
    private LocalDate createdAt;
}
