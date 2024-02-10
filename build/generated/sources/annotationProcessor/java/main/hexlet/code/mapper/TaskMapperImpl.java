package hexlet.code.mapper;

import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-08T21:00:06+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.4.jar, environment: Java 20 (Oracle Corporation)"
)
@Component
public class TaskMapperImpl extends TaskMapper {

    @Override
    public TaskDTO map(Task model) {
        if ( model == null ) {
            return null;
        }

        TaskDTO taskDTO = new TaskDTO();

        taskDTO.setTitle( model.getName() );
        taskDTO.setContent( model.getDescription() );
        taskDTO.setStatus( model.getTaskStatus() );
        taskDTO.setAssigneeId( modelAssigneeId( model ) );
        taskDTO.setId( model.getId() );
        taskDTO.setIndex( model.getIndex() );
        taskDTO.setCreatedAt( model.getCreatedAt() );

        return taskDTO;
    }

    @Override
    public Task map(TaskCreateDTO data) {
        if ( data == null ) {
            return null;
        }

        Task task = new Task();

        task.setName( data.getTitle() );
        task.setDescription( data.getContent() );
        task.setTaskStatus( data.getStatus() );
        task.setIndex( data.getIndex() );
        List<Label> list = data.getLabels();
        if ( list != null ) {
            task.setLabels( new ArrayList<Label>( list ) );
        }

        return task;
    }

    @Override
    public void update(TaskUpdateDTO dto, Task model) {
        if ( dto == null ) {
            return;
        }

        if ( JsonNullableMapper.isPresent( dto.getTitle() ) ) {
            model.setName( JsonNullableMapper.unwrap( dto.getTitle() ) );
        }
        if ( JsonNullableMapper.isPresent( dto.getContent() ) ) {
            model.setDescription( JsonNullableMapper.unwrap( dto.getContent() ) );
        }
        if ( JsonNullableMapper.isPresent( dto.getStatus() ) ) {
            model.setTaskStatus( JsonNullableMapper.unwrap( dto.getStatus() ) );
        }
        if ( JsonNullableMapper.isPresent( dto.getAssigneeId() ) ) {
            if ( model.getAssignee() == null ) {
                model.setAssignee( new User() );
            }
            longJsonNullableToUser( dto.getAssigneeId(), model.getAssignee() );
        }
        if ( JsonNullableMapper.isPresent( dto.getIndex() ) ) {
            model.setIndex( JsonNullableMapper.unwrap( dto.getIndex() ) );
        }
        if ( model.getLabels() != null ) {
            if ( JsonNullableMapper.isPresent( dto.getLabels() ) ) {
                model.getLabels().clear();
                model.getLabels().addAll( JsonNullableMapper.unwrap( dto.getLabels() ) );
            }
        }
        else {
            if ( JsonNullableMapper.isPresent( dto.getLabels() ) ) {
                model.setLabels( JsonNullableMapper.unwrap( dto.getLabels() ) );
            }
        }
    }

    private long modelAssigneeId(Task task) {
        if ( task == null ) {
            return 0L;
        }
        User assignee = task.getAssignee();
        if ( assignee == null ) {
            return 0L;
        }
        long id = assignee.getId();
        return id;
    }

    protected void longJsonNullableToUser(JsonNullable<Long> jsonNullable, User mappingTarget) {
        if ( jsonNullable == null ) {
            return;
        }
    }
}
