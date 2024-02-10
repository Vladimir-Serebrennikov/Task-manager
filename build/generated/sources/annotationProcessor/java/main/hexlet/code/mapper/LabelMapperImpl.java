package hexlet.code.mapper;

import hexlet.code.dto.LabelCreateDTO;
import hexlet.code.dto.LabelDTO;
import hexlet.code.dto.LabelUpdateDTO;
import hexlet.code.model.Label;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-08T21:00:06+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.4.jar, environment: Java 20 (Oracle Corporation)"
)
@Component
public class LabelMapperImpl extends LabelMapper {

    @Override
    public LabelDTO map(Label model) {
        if ( model == null ) {
            return null;
        }

        LabelDTO labelDTO = new LabelDTO();

        labelDTO.setId( model.getId() );
        labelDTO.setName( model.getName() );
        labelDTO.setCreatedAt( model.getCreatedAt() );

        return labelDTO;
    }

    @Override
    public Label map(LabelCreateDTO data) {
        if ( data == null ) {
            return null;
        }

        Label label = new Label();

        label.setName( data.getName() );

        return label;
    }

    @Override
    public void update(LabelUpdateDTO dto, Label model) {
        if ( dto == null ) {
            return;
        }

        if ( JsonNullableMapper.isPresent( dto.getName() ) ) {
            model.setName( JsonNullableMapper.unwrap( dto.getName() ) );
        }
    }
}
