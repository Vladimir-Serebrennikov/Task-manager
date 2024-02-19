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
        if (task.getAssignee() != null && task.getAssignee().getId() == null) {
            User user = userRepository.save(task.getAssignee());
            task.setAssignee(user);
        }

        taskRepository.save(task);
        return taskMapper.map(task);
    }

    public TaskDTO findById(Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found."));
        return taskMapper.map(task);
    }

    public void slugToTaskStatus(TaskCreateDTO dto, Task model) {
        var taskStatus = new TaskStatus();
        if (dto.getStatus() != null) {
            taskStatus = taskStatusRepository.findBySlug(dto.getStatus()).orElseThrow();
        }
        var user = new User();
        if (dto.getAssigneeId() != null) {
            user = userRepository.findById(dto.getAssigneeId()).orElseThrow();
        }
        List<Label> labels = null;
        if (dto.getTaskLabels() != null) {
            labels = labelRepository.findAllById(dto.getTaskLabels());
        }
        model.setTaskStatus(taskStatus);
        model.setAssignee(user);
        model.setLabels(labels != null ? new HashSet<>(labels) : new HashSet<>());

    }
}
