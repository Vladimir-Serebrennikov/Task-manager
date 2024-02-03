package hexlet.code.mapper;

import org.mapstruct.*;

import hexlet.code.model.TaskStatus;
import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.dto.TaskStatusCreateDTO;
import hexlet.code.dto.TaskStatusUpdateDTO;


@Mapper(
        uses = { JsonNullableMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskStatusMapper {
    public abstract TaskStatusDTO map(TaskStatus model);

    public abstract TaskStatus map(TaskStatusCreateDTO data);

    public abstract void update(TaskStatusUpdateDTO dto, @MappingTarget TaskStatus model);
}
