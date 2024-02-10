package hexlet.code.mapper;

import hexlet.code.dto.TaskStatusCreateDTO;
import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.dto.TaskStatusUpdateDTO;
import hexlet.code.model.TaskStatus;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-08T21:00:06+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.4.jar, environment: Java 20 (Oracle Corporation)"
)
@Component
public class TaskStatusMapperImpl extends TaskStatusMapper {

    @Override
    public TaskStatusDTO map(TaskStatus model) {
        if ( model == null ) {
            return null;
        }

        TaskStatusDTO taskStatusDTO = new TaskStatusDTO();

        taskStatusDTO.setId( model.getId() );
        taskStatusDTO.setName( model.getName() );
        taskStatusDTO.setSlug( model.getSlug() );
        taskStatusDTO.setCreatedAt( model.getCreatedAt() );

        return taskStatusDTO;
    }

    @Override
    public TaskStatus map(TaskStatusCreateDTO data) {
        if ( data == null ) {
            return null;
        }

        TaskStatus taskStatus = new TaskStatus();

        taskStatus.setName( data.getName() );
        taskStatus.setSlug( data.getSlug() );

        return taskStatus;
    }

    @Override
    public void update(TaskStatusUpdateDTO dto, TaskStatus model) {
        if ( dto == null ) {
            return;
        }

        if ( JsonNullableMapper.isPresent( dto.getName() ) ) {
            model.setName( JsonNullableMapper.unwrap( dto.getName() ) );
        }
        if ( JsonNullableMapper.isPresent( dto.getSlug() ) ) {
            model.setSlug( JsonNullableMapper.unwrap( dto.getSlug() ) );
        }
    }
}
