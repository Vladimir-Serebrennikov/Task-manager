package hexlet.code.controller;

import hexlet.code.dto.LabelDTO.LabelCreateDTO;
import hexlet.code.dto.LabelDTO.LabelDTO;
import hexlet.code.dto.LabelDTO.LabelUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.exception.EntityAssociationException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.LabelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
@RequiredArgsConstructor
public final class LabelController {
    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;
    private final LabelService labelService;
    private final TaskRepository taskRepository;

    @GetMapping(path = "")
    public ResponseEntity<List<LabelDTO>> index() {
        var labels = labelService.getAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(labels.size()))
                .body(labels);
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LabelDTO show(@PathVariable Long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));
        return labelMapper.map(label);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public LabelDTO create(@Valid @RequestBody LabelCreateDTO data) {
        return labelService.create(data);
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LabelDTO update(@Valid @RequestBody LabelUpdateDTO data, @PathVariable Long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));
        labelMapper.update(data, label);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        var label = labelRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));
        if (label != null && taskRepository.findByLabelsName(label.getName()).isPresent()) {
            throw new EntityAssociationException("You cannot delete a label with an assigned task");
        }
        labelRepository.deleteById(id);
    }

}
