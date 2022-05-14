package tr.edu.yildiz.cfms.business.concretes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.edu.yildiz.cfms.api.models.ConversationDetail;
import tr.edu.yildiz.cfms.api.models.GetConversationDetailRequest;
import tr.edu.yildiz.cfms.api.models.GetConversationsRequest;
import tr.edu.yildiz.cfms.business.abstracts.ConversationService;
import tr.edu.yildiz.cfms.business.repository.ConversationRepository;
import tr.edu.yildiz.cfms.business.repository.MessageRepository;
import tr.edu.yildiz.cfms.core.enums.Platform;
import tr.edu.yildiz.cfms.core.utils.ExternalApiClients;
import tr.edu.yildiz.cfms.entities.concretes.hibernate.Conversation;
import tr.edu.yildiz.cfms.entities.concretes.mongodb.MongoDbMessages;
import tr.edu.yildiz.cfms.entities.concretes.mongodb.MongoDbMessagesItem;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConversationManager implements ConversationService {
    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public List<Conversation> getList(GetConversationsRequest request) {
        return conversationRepository.findAll();
    }

    @Override
    public List<ConversationDetail> getListWithMessages(GetConversationsRequest request) {
        var conversations = conversationRepository.findAll();
        var results = new ArrayList<ConversationDetail>();

        for (var conversation : conversations) {
            var mongoDbResult = messageRepository.findById(conversation.getId());
            if (mongoDbResult.isEmpty())
                continue;
            var messages = mongoDbResult.get().getMessages();
            var conversationDetail = new ConversationDetail(conversation, messages);
            results.add(conversationDetail);
        }

        return results;
    }

    @Override
    public ConversationDetail getConversationDetail(GetConversationDetailRequest request) {
        return null;
    }

    @Override
    public void sendMessage(String conversationId, MongoDbMessagesItem mongoDbMessagesItem) throws Exception {
        var optional = conversationRepository.findById(conversationId);

        if (optional.isEmpty())
            throw new Exception("Conversation not found!");

        var conversation = optional.get();

        try {
            if (!mongoDbMessagesItem.isSentByClient()) { // CSR gonderdiyse mesaj Facebook API'ye de gonderilir
                String messageId = sendMessageWithExternalApi(conversation, mongoDbMessagesItem);
                mongoDbMessagesItem.setId(messageId);
            }
            conversation.setLastMessageDate(mongoDbMessagesItem.getSentDate());
            conversationRepository.save(conversation);
            messageRepository.pushMessage(conversationId, mongoDbMessagesItem);
        } catch (Exception e) {
            e.printStackTrace(); // TODO Handle error
        }
    }

    public void createConversation(Conversation conversation, MongoDbMessagesItem mongoDbMessagesItem) {
        String id = conversation.getId();
        var messages = new ArrayList<MongoDbMessagesItem>();
        messages.add(mongoDbMessagesItem);
        var mongoDbMessages = new MongoDbMessages(id, messages);

        conversationRepository.save(conversation);
        messageRepository.save(mongoDbMessages);
    }

    private String sendMessageWithExternalApi(Conversation conversation, MongoDbMessagesItem mongoDbMessagesItem) throws Exception {
        Platform platform = conversation.getPlatform();
        String messageId = null;
        switch (platform) {
            case FACEBOOK:
                messageId = ExternalApiClients.sendMessageWithFacebook(conversation, mongoDbMessagesItem);
                break;
            case INSTAGRAM:
                break;
            case TELEGRAM:
                messageId = ExternalApiClients.sendMessageWithTelegram(conversation, mongoDbMessagesItem);
                break;
            case TWITTER:
                break;
            default:
                return null;
        }

        if (messageId == null || messageId.length() <= 0)
            throw new Exception("Could not send message with " + platform.name() + " API!");

        return messageId;
    }
}
