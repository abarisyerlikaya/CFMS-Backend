package tr.edu.yildiz.cfms.api.controllers;

import org.optaplanner.core.api.solver.SolverJob;
import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.edu.yildiz.cfms.entities.concretes.optaplanner.CloudBalance;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/cloud-balancing")
public class OptaplannerController {
    @Autowired
    private SolverManager<CloudBalance, UUID> solverManager;

    @PostMapping("/solve")
    public CloudBalance solve(@RequestBody CloudBalance problem) {
        UUID problemId = UUID.randomUUID();
        // Submit the problem to start solving
        SolverJob<CloudBalance, UUID> solverJob = solverManager.solve(problemId, problem);
        CloudBalance solution;
        try {
            // Wait until the solving ends
            solution = solverJob.getFinalBestSolution();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Solving failed.", e);
        }
        return solution;
    }


}
