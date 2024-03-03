package hexlet.code.service;

import hexlet.code.dto.LabelDTO.LabelCreateDTO;
import hexlet.code.dto.LabelDTO.LabelDTO;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.repository.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class LabelService {
    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;

    public List<LabelDTO> getAll() {
        var labels = labelRepository.findAll();
        var result = labels.stream()
                .map(labelMapper::map)
                .toList();
        return result;
    }

    public LabelDTO create(LabelCreateDTO data) {
        var label = labelMapper.map(data);
        labelRepository.save(label);
        return labelMapper.map(label);
    }
}
