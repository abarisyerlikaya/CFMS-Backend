package tr.edu.yildiz.cfms.business.abstracts;

import org.springframework.stereotype.Service;
import tr.edu.yildiz.cfms.entities.concretes.hibernate.Conversation;

@Service
public interface OptimizationService {
    String  assignConversation(Conversation conversation);
}
