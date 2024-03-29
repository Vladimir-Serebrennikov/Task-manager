package hexlet.code.component;

import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public final class DataInitializer implements ApplicationRunner {
    private final UserRepository userRepository;
    private final CustomUserDetailsService userService;
    private final TaskStatusRepository taskStatusRepository;
    private final LabelRepository labelRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var email = "hexlet@example.com";
        var userData = new User();
        userData.setEmail(email);
        userData.setPasswordDigest("qwerty");
        userService.createUser(userData);

        var user = userRepository.findByEmail(email).get();

        var draft = new TaskStatus();
        draft.setName("Draft");
        draft.setSlug("draft");

        var toReview = new TaskStatus();
        toReview.setName("ToReview");
        toReview.setSlug("to_review");

        var toBeFixed = new TaskStatus();
        toBeFixed.setName("ToBeFixed");
        toBeFixed.setSlug("to_be_fixed");

        var toPublish = new TaskStatus();
        toPublish.setName("ToPublish");
        toPublish.setSlug("to_publish");

        var published = new TaskStatus();
        published.setName("Published");
        published.setSlug("published");

        taskStatusRepository.saveAll(List.of(
                draft,
                toReview,
                toBeFixed,
                toPublish,
                published
        ));

        var bug = new Label();
        bug.setName("bug");

        var feature = new Label();
        feature.setName("feature");

        labelRepository.saveAll(List.of(
                bug,
                feature
        ));

    }
}
