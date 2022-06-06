package tr.edu.yildiz.cfms.business.concretes;

import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tr.edu.yildiz.cfms.business.abstracts.OptimizationService;
import tr.edu.yildiz.cfms.business.repository.ConversationRepository;
import tr.edu.yildiz.cfms.business.repository.UserRepository;
import tr.edu.yildiz.cfms.entities.concretes.hibernate.Conversation;
import tr.edu.yildiz.cfms.entities.concretes.hibernate.User;
import tr.edu.yildiz.cfms.entities.concretes.optaplanner.Csr;
import tr.edu.yildiz.cfms.entities.concretes.optaplanner.Task;
import tr.edu.yildiz.cfms.entities.concretes.optaplanner.TaskAssignment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class OptimizationManager implements OptimizationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SolverManager<TaskAssignment, UUID> solverManager;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Override
    public void assignConversation(Conversation conversation) {
        var newTask = conversationToTask(conversation);
        var csrs = getCsrs();
        var problem = new TaskAssignment(newTask, csrs);
        var solution = solve(problem);
        String selectedCsrId = solution.getTask().getCsr().getId();
        updateConversation(conversation.getId(), selectedCsrId);
    }

    private Task conversationToTask(Conversation conversation) {
        String id = conversation.getId();
        return new Task(id);
    }

    private List<Csr> getCsrs() {
        var csrs = new ArrayList<Csr>();
        var onlinePrincipals = sessionRegistry.getAllPrincipals();

        for (var principal : onlinePrincipals)
            if (principal instanceof User && !((User) principal).getUsername().equals("admin"))
                csrs.add(createCsr((User) principal));

        return csrs;
    }

    private Csr createCsr(User user) {
        String username = user.getUsername();
        int numberOfConversations = conversationRepository.countByUsername(username);
        int avgConversationLength = (int) ((float) user.getTotalMessageLength() / (float) user.getConversationCount());
        return new Csr(username, numberOfConversations, avgConversationLength);
    }

    private TaskAssignment solve(TaskAssignment problem) {
        try {
            var problemId = UUID.randomUUID();
            var solverJob = solverManager.solve(problemId, problem);
            return solverJob.getFinalBestSolution();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    private void updateConversation(String conversationId, String csrId) {
        var conversation = conversationRepository.getById(conversationId);
        conversation.setAssignedTo(csrId);
        conversationRepository.save(conversation);
    }
}
