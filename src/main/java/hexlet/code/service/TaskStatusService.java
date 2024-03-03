package hexlet.code.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import hexlet.code.dto.TaskStatusDTO.TaskStatusDTO;
import hexlet.code.dto.TaskStatusDTO.TaskStatusCreateDTO;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.repository.TaskStatusRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class TaskStatusService {
    private final TaskStatusRepository taskStatusRepository;
    private final TaskStatusMapper taskStatusMapper;

    public List<TaskStatusDTO> getAll() {
        var taskStatuses = taskStatusRepository.findAll();
        var result = taskStatuses.stream()
                .map(taskStatusMapper::map)
                .toList();
        return result;
    }

    public TaskStatusDTO create(TaskStatusCreateDTO data) {
        var taskStatus = taskStatusMapper.map(data);
        taskStatusRepository.save(taskStatus);
        return taskStatusMapper.map(taskStatus);
    }
}
