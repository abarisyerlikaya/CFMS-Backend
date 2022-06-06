package tr.edu.yildiz.cfms.entities.concretes.optaplanner;

import org.optaplanner.core.api.domain.solution.PlanningEntityProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.List;

@PlanningSolution
public class TaskAssignment {
    @PlanningEntityProperty
    private Task task;

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "csr")
    private List<Csr> csrs;

    @PlanningScore
    private HardSoftScore score;

    public TaskAssignment() {
    }

    public TaskAssignment(Task task, List<Csr> csrs) {
        this.task = task;
        this.csrs = csrs;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public List<Csr> getCsrs() {
        return csrs;
    }

    public void setCsrs(List<Csr> csrs) {
        this.csrs = csrs;
    }

    public HardSoftScore getScore() {
        return score;
    }

    public void setScore(HardSoftScore score) {
        this.score = score;
    }
}
