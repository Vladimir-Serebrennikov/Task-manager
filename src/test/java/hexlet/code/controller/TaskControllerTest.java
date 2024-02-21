package hexlet.code.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.Label;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.UserRepository;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    }

    @Test
    public void testTaskUpdate() throws Exception {
        taskRepository.save(testTask);

        var data = new TaskUpdateDTO();
        data.setTitle(JsonNullable.of("New title"));

        var request = put("/api/tasks/" + testTask.getId())
                .with(jwt().jwt(builder -> builder.subject(testTask.getName())))
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        testTask = taskRepository.findById(testTask.getId()).get();
        assertThat(testTask.getName()).isEqualTo(data.getTitle().get());
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
