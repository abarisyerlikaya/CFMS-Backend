package tr.edu.yildiz.cfms.business.abstracts;

import org.springframework.stereotype.Service;
import tr.edu.yildiz.cfms.api.models.*;
import tr.edu.yildiz.cfms.entities.concretes.Conversation;

import java.util.List;

@Service
public interface ConversationService {
    List<Conversation> getList(GetConversationsRequest request);

    ConversationDetail getConversationDetail(GetConversationDetailRequest request);

    Conversation sendMessage(Message request);
}
