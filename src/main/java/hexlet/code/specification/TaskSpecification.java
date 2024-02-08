package hexlet.code.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import hexlet.code.model.Task;
import hexlet.code.dto.TaskParamsDTO;

@Component
public class TaskSpecification {

    public Specification<Task> build(TaskParamsDTO params) {
        return withAssigneeId(params.getAssigneeId())
                .and(withTitleCount(params.getTitleCont()))
                .and(withStatus(params.getStatus()))
                .and(withLabelId(params.getLabelId()));
    }

    private Specification<Task> withAssigneeId(Long assigneeId) {
        return (root, query, cb) -> assigneeId == null ? cb.conjunction()
                : cb.equal(root.get("assignee").get("id"), assigneeId);
    }

    private Specification<Task> withTitleCount(String titleCount) {
        return (root, query, cb) -> titleCount == null ? cb.conjunction()
                : cb.like(cb.lower(root.get("name")), "%" + titleCount.toLowerCase() + "%");

    }

    private Specification<Task> withStatus(String status) {
        return (root, query, cb) -> status == null ? cb.conjunction()
                : cb.equal(root.get("taskStatus").get("slug"), status);
    }

    private Specification<Task> withLabelId(Long labelId) {
        return (root, query, cb) -> labelId == null ? cb.conjunction()
                : cb.equal(root.join("labels").get("id"), labelId);
    }
}
