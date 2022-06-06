package tr.edu.yildiz.cfms.entities.concretes.optaplanner;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;

import java.util.function.Function;

import static org.optaplanner.core.api.score.stream.Joiners.equal;

public class TaskAssignmentConstraintProvider implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                numberOfConversations(constraintFactory),
                avgConversationLength(constraintFactory)
        };
    }

    private Constraint numberOfConversations(ConstraintFactory constraintFactory) {
        return constraintFactory
                .from(Csr.class)
                .ifExists(Task.class, equal(Function.identity(), Task::getCsr))
                .penalize("numberOfConversations", HardSoftScore.ONE_SOFT, Csr::getNumberOfConversations);
    }

    private Constraint avgConversationLength(ConstraintFactory constraintFactory) {
        return constraintFactory
                .from(Csr.class)
                .ifExists(Task.class, equal(Function.identity(), Task::getCsr))
                .penalize("avgConversationLength", HardSoftScore.ONE_SOFT, Csr::getAvgConversationLength);
    }
}
