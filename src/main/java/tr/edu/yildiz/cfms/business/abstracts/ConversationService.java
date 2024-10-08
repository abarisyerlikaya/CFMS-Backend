package tr.edu.yildiz.cfms.business.abstracts;

import org.springframework.stereotype.Service;
import tr.edu.yildiz.cfms.api.models.*;
import tr.edu.yildiz.cfms.entities.concretes.hibernate.Conversation;
import tr.edu.yildiz.cfms.entities.concretes.mongodb.MongoDbMessagesItem;

import java.util.List;

@Service
public interface ConversationService {
    List<Conversation> getList(GetConversationsRequest request);

    List<ConversationDetail> getListWithMessages(GetConversationsRequest request, String accessToken);

    ConversationDetail getConversationDetail(GetConversationDetailRequest request);

    void sendMessage(String conversationId, MongoDbMessagesItem mongoDbMessagesItem) throws Exception;

    void sendMessages(String conversationId, List<MongoDbMessagesItem> mongoDbMessagesItem) throws Exception;

    String createConversation(Conversation conversation, MongoDbMessagesItem mongoDbMessagesItem);

    void endConversation(String conversationId) throws Exception;
}
