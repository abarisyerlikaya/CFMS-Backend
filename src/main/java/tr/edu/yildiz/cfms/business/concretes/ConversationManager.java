package tr.edu.yildiz.cfms.business.concretes;

import tr.edu.yildiz.cfms.api.models.*;
import tr.edu.yildiz.cfms.business.abstracts.ConversationService;
import tr.edu.yildiz.cfms.entities.concretes.Conversation;

import java.util.List;

public class ConversationManager  implements ConversationService {
    @Override
    public List<Conversation> getList(GetConversationsRequest request) {
        return null;
    }

    @Override
    public ConversationDetail getDetail(String id) {
        return null;
    }

    @Override
    public List<Message> getMessages(GetMessagesRequest request) {
        return null;
    }
}
