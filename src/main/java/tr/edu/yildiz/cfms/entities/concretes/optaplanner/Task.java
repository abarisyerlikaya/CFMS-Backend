package tr.edu.yildiz.cfms.entities.concretes.optaplanner;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class Task {
    private String id;

    @PlanningVariable(valueRangeProviderRefs = "csr")
    private Csr csr;

    public Task() {
    }

    public Task(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Csr getCsr() {
        return csr;
    }

    public void setCsr(Csr csr) {
        this.csr = csr;
    }
}
