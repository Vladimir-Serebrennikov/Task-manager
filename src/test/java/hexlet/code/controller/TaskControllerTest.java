package hexlet.code.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import hexlet.code.dto.TaskDTO.TaskCreateDTO;
import hexlet.code.dto.TaskDTO.TaskUpdateDTO;
import hexlet.code.dto.TaskStatusDTO.TaskStatusCreateDTO;
import hexlet.code.dto.UserDTO.UserCreateDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.Label;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.UserRepository;
import jakarta.transaction.Transactional;
import net.datafaker.Faker;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.http.MediaType;

import hexlet.code.util.ModelGenerator;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Set;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public final class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusMapper taskStatusMapper;

    @Autowired
    private Faker faker;

    @Autowired
    private UserMapper userMapper;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    private Task testTask;
    private TaskStatus testTaskStatus;

    private Label testLabel;

    private User testUser;

    @BeforeEach
    public void setUp() {
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        testTask = Instancio.of(modelGenerator.getTaskModel())
                .create();
        testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel())
                .create();
        testLabel = Instancio.of(modelGenerator.getLabelModel())
                .create();
        testUser = Instancio.of(modelGenerator.getUserModel())
                        .create();
        taskStatusRepository.save(testTaskStatus);
        testTask.setTaskStatus(testTaskStatus);
        testTask.setLabels(Set.of());
    }
    @Test
    public void testTaskIndex() throws Exception {
        taskRepository.save(testTask);
        var result = mockMvc.perform(get("/api/tasks").with(token))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }
    @Test
    public void testTaskIndexWithParams() throws Exception {
        taskRepository.save(testTask);
        var request = get("/api/tasks?"
                + "titleCont=" + "testTitle"
                + "&assigneeId=" + 1
                + "&status=" + "testStatus"
                + "&labelId=" + 1)
                .with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray().hasSize(0);
    }

    @Test
    public void testTaskShow() throws Exception {
        taskRepository.save(testTask);
        var result = mockMvc.perform(get("/api/tasks/" + testTask.getId()).with(token))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("title").isEqualTo(testTask.getName()),
                v -> v.node("content").isEqualTo(testTask.getDescription())
        );
    }


    @Test
    @Transactional
    public void testTaskCreate() throws Exception {
        userRepository.save(testUser);
        var taskStatus = testTaskStatus;
        var label = testLabel;
        var data = new TaskCreateDTO();
        data.setTitle(testTask.getName());
        data.setStatus(taskStatus.getSlug());
        data.setTaskLabelIds(Set.of(label.getId()));
        data.setAssigneeId(testUser.getId());

        mockMvc.perform(post("/api/tasks").with(token)
                        .content(om.writeValueAsString(data))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isCreated());

        var task = taskRepository.findByName(data.getTitle());
        assertNotNull(task.get());
        assertEquals(data.getTitle(), task.get().getName());
        assertEquals(data.getStatus(), task.get().getTaskStatus().getSlug());
        assertThat(data.getTaskLabelIds().containsAll(task.get().getLabels()));
    }

    @Test
    public void testTaskUpdate() throws Exception {
        taskRepository.save(testTask);
        var data = new TaskUpdateDTO();
        data.setTitle(JsonNullable.of("New title"));
        data.setIndex(JsonNullable.of(2023));
        data.setContent(JsonNullable.of("New content"));
        var request = put("/api/tasks/" + testTask.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isOk());
        var task = taskRepository.findById(testTask.getId()).get();
        assertThat(task.getName()).isEqualTo("New title");
        assertThat(task.getIndex()).isEqualTo(2023);
        assertThat(task.getDescription()).isEqualTo("New content");
    }

    @Test
    public void testTaskDestroy() throws Exception {
        taskRepository.save(testTask);
        var result = delete("/api/tasks/" + testTask.getId()).with(token);
        mockMvc.perform(result)
                .andExpect(status().isNoContent());
        assertThat(taskRepository.existsById(testTask.getId())).isEqualTo(false);
    }
}
