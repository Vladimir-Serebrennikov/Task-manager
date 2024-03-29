package hexlet.code.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.TaskRepository;
import org.springframework.http.MediaType;

import hexlet.code.model.TaskStatus;
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

import java.util.HashMap;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
public final class TaskStatusControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskStatusMapper taskStatusMapper;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskRepository taskRepository;


    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    private TaskStatus testTaskStatus;

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel())
                .create();
    }

    @Test
    public void testTaskStatusIndex() throws Exception {
        taskStatusRepository.save(testTaskStatus);
        var result = mockMvc.perform(get("/api/task_statuses").with(token))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testTaskStatusShow() throws Exception {
        taskStatusRepository.save(testTaskStatus);
        var result = mockMvc.perform(get("/api/task_statuses/" + testTaskStatus.getId()).with(token))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testTaskStatus.getName()),
                v -> v.node("slug").isEqualTo(testTaskStatus.getSlug())
        );
    }

    @Test
    public void testTaskStatusesCreate() throws Exception {
        var data = Instancio.of(modelGenerator.getTaskStatusModel())
                .create();
        var request = post("/api/task_statuses")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var taskStatus = taskStatusRepository.findBySlug(data.getSlug()).get();

        assertNotNull(taskStatus);
        assertThat(taskStatus.getSlug()).isEqualTo(data.getSlug());
        assertThat(taskStatus.getName()).isEqualTo(data.getName());
    }

    @Test
    public void testTaskStatusUpdate() throws Exception {
        taskStatusRepository.save(testTaskStatus);
        var data = new HashMap<>();
        data.put("name", "newStatus");
        var originalSlug = testTaskStatus.getSlug();

        var request = put("/api/task_statuses/" + testTaskStatus.getId())
                .with(jwt().jwt(builder -> builder.subject(testTaskStatus.getName())))
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var taskStatus = taskStatusRepository.findById(testTaskStatus.getId()).get();
        assertThat(taskStatus.getName()).isEqualTo(("newStatus"));
        assertThat(taskStatus.getSlug()).isEqualTo(originalSlug);
    }

    @Test
    public void testTaskStatusDestroy() throws Exception {
        taskStatusRepository.save(testTaskStatus);
        var result = delete("/api/task_statuses/" + testTaskStatus.getId()).with(token);
        mockMvc.perform(result)
                .andExpect(status().isNoContent());
        assertThat(taskRepository.existsById(testTaskStatus.getId())).isEqualTo(false);
    }
}
