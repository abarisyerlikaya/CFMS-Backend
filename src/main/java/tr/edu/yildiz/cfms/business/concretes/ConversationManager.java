package tr.edu.yildiz.cfms.business.concretes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.edu.yildiz.cfms.api.models.ConversationDetail;
import tr.edu.yildiz.cfms.api.models.GetConversationDetailRequest;
import tr.edu.yildiz.cfms.api.models.GetConversationsRequest;
import tr.edu.yildiz.cfms.api.util.JwtUtil;
import tr.edu.yildiz.cfms.business.abstracts.ConversationService;
import tr.edu.yildiz.cfms.business.abstracts.OptimizationService;
import tr.edu.yildiz.cfms.business.repository.ConversationRepository;
import tr.edu.yildiz.cfms.business.repository.MessageRepository;
import tr.edu.yildiz.cfms.business.repository.UserRepository;
import tr.edu.yildiz.cfms.core.enums.Platform;
import tr.edu.yildiz.cfms.core.utils.ExternalApiClients;
import tr.edu.yildiz.cfms.entities.concretes.hibernate.Conversation;
import tr.edu.yildiz.cfms.entities.concretes.mongodb.MongoDbMessages;
import tr.edu.yildiz.cfms.entities.concretes.mongodb.MongoDbMessagesItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static tr.edu.yildiz.cfms.core.utils.Constants.BEARER;
import static tr.edu.yildiz.cfms.core.utils.Constants.BEARER_LENGTH;
import static tr.edu.yildiz.cfms.core.utils.Utils.truncate;

@Service
public class ConversationManager implements ConversationService {
    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OptimizationService optimizationService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public List<Conversation> getList(GetConversationsRequest request) {
        return conversationRepository.findAll();
    }

    @Override
    public List<ConversationDetail> getListWithMessages(GetConversationsRequest request, String accessToken) {
        if (accessToken.startsWith(BEARER)) accessToken = accessToken.substring(BEARER_LENGTH);
        String username = jwtUtil.extractUsername(accessToken);

        var conversations = conversationRepository.findByUsername(username);
        var results = new ArrayList<ConversationDetail>();

        boolean withMessages = request.isWithMessages();

        for (var conversation : conversations) {
            List<MongoDbMessagesItem> messages = null;
            if (withMessages) {
                var mongoDbResult = messageRepository.findById(conversation.getId());
                if (mongoDbResult.isPresent())
                    messages = mongoDbResult.get().getMessages();
            }
            var conversationDetail = new ConversationDetail(conversation, messages);
            results.add(conversationDetail);
        }

        return results.stream().skip(request.getOffset()).limit(request.getLimit()).collect(Collectors.toList());
    }

    @Override
    public ConversationDetail getConversationDetail(GetConversationDetailRequest request) {
        List<MongoDbMessagesItem> messages = null;
        String id = request.getConversationId();
        boolean withMessages = request.isWithMessages();

        var optionalConversation = conversationRepository.findById(id);
        if (optionalConversation.isEmpty())
            return null;
        var conversation = optionalConversation.get();

        if (withMessages) {
            var mongoDbResult = messageRepository.findById(request.getConversationId());
            if (mongoDbResult.isPresent()) {
                messages = mongoDbResult.get().getMessages();
                if (messages.size() > request.getLimit()) {
                    int endIndex = messages.size() - request.getOffset();
                    int startIndex = endIndex - request.getLimit();
                    if (startIndex < 0) startIndex = 0;
                    messages = messages.subList(startIndex, endIndex);
                }
            }
        }

        return new ConversationDetail(conversation, messages);
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
            conversation.setLastMessagePreview(truncate(mongoDbMessagesItem.getText()));

            String userId = conversation.getAssignedTo();
            var optionalUser = userRepository.findById(userId);
            if (optionalUser.isEmpty()) throw new Exception("User not found!");
            var user = optionalUser.get();
            user.setTotalMessageLength(user.getTotalMessageLength() + 1);

            userRepository.save(user);
            conversationRepository.save(conversation);
            messageRepository.pushMessage(conversationId, mongoDbMessagesItem);
        } catch (Exception e) {
            e.printStackTrace(); // TODO Handle error
        }
    }

    @Override
    public void sendMessages(String conversationId, List<MongoDbMessagesItem> mongoDbMessagesItems) throws Exception {
        var optional = conversationRepository.findById(conversationId);

        if (optional.isEmpty())
            throw new Exception("Conversation not found!");

        var conversation = optional.get();

        try {
            for (var mongoDbMessagesItem : mongoDbMessagesItems) {
                messageRepository.pushMessage(conversationId, mongoDbMessagesItem);
            }
            conversation.setLastMessageDate(mongoDbMessagesItems.get(mongoDbMessagesItems.size() - 1).getSentDate());
            conversation.setLastMessagePreview(mongoDbMessagesItems.get(mongoDbMessagesItems.size() - 1).getText());

            String userId = conversation.getAssignedTo();
            var optionalUser = userRepository.findById(userId);
            if (optionalUser.isEmpty()) throw new Exception("User not found!");
            var user = optionalUser.get();
            user.setTotalMessageLength(user.getTotalMessageLength() + mongoDbMessagesItems.size());

            userRepository.save(user);
            conversationRepository.save(conversation);
        } catch (Exception e) {
            e.printStackTrace(); // TODO Handle error
        }
    }

    public void createConversation(Conversation conversation, MongoDbMessagesItem mongoDbMessagesItem) {
        String id = conversation.getId();
        var messages = new ArrayList<MongoDbMessagesItem>();
        messages.add(mongoDbMessagesItem);
        var mongoDbMessages = new MongoDbMessages(id, messages);

        String userId = conversation.getAssignedTo();
        var optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) return;
        var user = optionalUser.get();
        user.setConversationCount(user.getConversationCount());

        userRepository.save(user);
        conversationRepository.save(conversation);
        messageRepository.save(mongoDbMessages);
        optimizationService.assignConversation(conversation);
    }

    private String sendMessageWithExternalApi(Conversation conversation, MongoDbMessagesItem mongoDbMessagesItem) throws Exception {
        Platform platform = conversation.getPlatform();
        String messageId = null;
        switch (platform) {
            case FACEBOOK:
                messageId = ExternalApiClients.sendMessageWithFacebook(conversation, mongoDbMessagesItem);
                break;
            case INSTAGRAM:
                messageId = ExternalApiClients.sendMessageWithInstagram(conversation, mongoDbMessagesItem);
                break;
            case TELEGRAM:
                messageId = ExternalApiClients.sendMessageWithTelegram(conversation, mongoDbMessagesItem);
                break;
            case TWITTER:
                messageId = ExternalApiClients.sendMessageWithTwitter(conversation, mongoDbMessagesItem);
                break;
            default:
                return null;
        }

        if (messageId == null || messageId.length() <= 0)
            throw new Exception("Could not send message with " + platform.name() + " API!");

        String userId = conversation.getAssignedTo();
        var optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) throw new Exception("User not found!");
        var user = optionalUser.get();
        user.setTotalMessageLength(user.getTotalMessageLength() + 1);

        userRepository.save(user);

        return messageId;
    }


}
