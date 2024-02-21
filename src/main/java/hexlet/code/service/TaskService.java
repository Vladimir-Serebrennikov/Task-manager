package hexlet.code.service;

import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskParamsDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.model.Label;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.repository.LabelRepository;
import hexlet.code.specification.TaskSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public final class  TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskSpecification taskSpecification;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    public List<TaskDTO> getAll() {
        var tasks = taskRepository.findAll();
        var result = tasks.stream()
                .map(taskMapper::map)
                .toList();
        return result;
    }

    public List<TaskDTO> getAllWithParams(TaskParamsDTO dto) {
        var spec = taskSpecification.build(dto);
        var tasks = taskRepository.findAll(spec);
        return tasks.stream()
                .map(taskMapper::map)
                .toList();
    }

    public TaskDTO create(TaskCreateDTO data) {
        var task = taskMapper.map(data);
        slugToTaskStatus(data, task);
        Task saved = taskRepository.save(task);
        return taskMapper.map(saved);
    }

    public TaskDTO findById(Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found."));
        return taskMapper.map(task);
    }

    public void slugToTaskStatus(TaskCreateDTO dto, Task model) {
        TaskStatus taskStatus = null;
        if (dto.getStatus() != null) {
            taskStatus = taskStatusRepository.findBySlug(dto.getStatus()).orElseThrow();
        }
        User user = null;
        if (dto.getAssigneeId() != null) {
            user = userRepository.findById(dto.getAssigneeId()).orElseThrow();
        }
        List<Label> labels = null;
        if (dto.getTaskLabelIds() != null) {
            labels = labelRepository.findAllById(dto.getTaskLabelIds());
        }
        model.setTaskStatus(taskStatus);
        model.setAssignee(user);
        model.setLabels(labels != null ? new HashSet<>(labels) : new HashSet<>());

    }
}
