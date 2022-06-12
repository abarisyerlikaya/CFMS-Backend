package tr.edu.yildiz.cfms.api.controllers;

import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.edu.yildiz.cfms.core.response_types.Response;
import tr.edu.yildiz.cfms.core.response_types.SuccessDataResponse;
import tr.edu.yildiz.cfms.entities.concretes.optaplanner.TaskAssignment;

import java.util.UUID;

@RestController
@RequestMapping("/api/optimization")
public class OptimizationController {
    @Autowired
    private SolverManager<TaskAssignment, UUID> solverManager;

    @PostMapping("/solve")
    public Response solve(@RequestBody TaskAssignment problem) {
        try {
            var problemId = UUID.randomUUID();
            var solverJob = solverManager.solve(problemId, problem);
            var solution = solverJob.getFinalBestSolution();
            var result = solution.getTask().getCsr();
            return new SuccessDataResponse<>("Optimization completed successfully!", result);
        } catch (Exception e) {
            return new Response(false, HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred!");
        }
    }
}
