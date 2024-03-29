package hexlet.code.service;

import hexlet.code.dto.taskDTO.TaskDTO;
import hexlet.code.dto.taskDTO.TaskParamsDTO;
import hexlet.code.dto.taskDTO.TaskCreateDTO;
import hexlet.code.dto.taskDTO.TaskUpdateDTO;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public final class  TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskSpecification taskSpecification;
    private final TaskStatusRepository taskStatusRepository;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;

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
        var savedTask = taskRepository.save(task);
        return taskMapper.map(savedTask);
    }

    public TaskDTO update(Long taskId, TaskUpdateDTO data) {
        var task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id: " + taskId + " not found."));

        taskMapper.update(data, task);

        User assignee = null;
        if (data.getAssigneeId() != null) {
            assignee = userRepository.findById(data.getAssigneeId().get()).orElse(null);
        }
        task.setAssignee(assignee);

        TaskStatus taskStatus = null;
        if (data.getStatus() != null) {
            taskStatus = taskStatusRepository.findBySlug(data.getStatus().get()).orElse(null);
            task.setTaskStatus(taskStatus);
        }


        Optional<Set<Label>> labels = null;
        if (data.getTaskLabelIds() != null && data.getTaskLabelIds().isPresent() && data.getTaskLabelIds().get() != null) {
            labels = labelRepository.findByIdIn(data.getTaskLabelIds().get());
        } else {
            labels = Optional.of(task.getLabels());
        }

        Set<Label> labelSet = labels.orElse(Collections.emptySet());
        task.setLabels(new HashSet<>(labelSet));

        taskRepository.save(task);
        return taskMapper.map(task);
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
