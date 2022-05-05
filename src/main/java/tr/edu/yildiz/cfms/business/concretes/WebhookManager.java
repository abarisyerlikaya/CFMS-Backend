package tr.edu.yildiz.cfms.business.concretes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tr.edu.yildiz.cfms.api.controllers.ChatController;
import tr.edu.yildiz.cfms.api.dtos.apis.facebook.FacebookApiUserDto;
import tr.edu.yildiz.cfms.api.dtos.webhooks.facebook.FacebookWebhookDto;
import tr.edu.yildiz.cfms.api.dtos.webhooks.facebook.FacebookWebhookDtoEntry;
import tr.edu.yildiz.cfms.api.dtos.webhooks.facebook.FacebookWebhookDtoMessage;
import tr.edu.yildiz.cfms.api.models.WebSocketClientConversation;
import tr.edu.yildiz.cfms.api.models.WebSocketClientMessage;
import tr.edu.yildiz.cfms.business.abstracts.WebhookService;
import tr.edu.yildiz.cfms.business.repository.ConversationRepository;
import tr.edu.yildiz.cfms.business.repository.MessageRepository;
import tr.edu.yildiz.cfms.core.enums.Platform;
import tr.edu.yildiz.cfms.entities.concretes.hibernate.Conversation;
import tr.edu.yildiz.cfms.entities.concretes.mongodb.MongoDbMessagesItem;
import tr.edu.yildiz.cfms.entities.concretes.mongodb.MongoDbMessagesAttachment;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.Instant.ofEpochMilli;
import static java.util.TimeZone.getDefault;
import static tr.edu.yildiz.cfms.core.utils.Constants.FB_BASE_URL;
import static tr.edu.yildiz.cfms.core.utils.Constants.FB_PAGE_ACCESS_TOKEN;

@Service
public class WebhookManager implements WebhookService {
    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatController chatController;


    @Override
    public void handleWebhook(FacebookWebhookDto dto) {
        if (!dto.getObject().equals("page"))
            return;

        var entries = dto.getEntry();

        for (var entry : entries)
            processEntry(entry);

    }


    private void processEntry(FacebookWebhookDtoEntry entry) {
        var messaging = entry.getMessagingObject();
        var message = messaging.getMessage();
        String clientId = messaging.getSenderId();
        long timestamp = messaging.getTimestamp();
        LocalDateTime lastMessageDate = LocalDateTime.ofInstant(ofEpochMilli(timestamp), getDefault().toZoneId());
        String conversationId = "FB_" + clientId;

        boolean doesExist = conversationRepository.existsById(conversationId);

        if (doesExist) {
            saveFacebookMessage(conversationId, lastMessageDate, message);
        } else {
            Platform platform = Platform.FACEBOOK;
            String clientName = getUserByIdFromFacebook(clientId);
            var conversation = new Conversation(conversationId, platform, clientName, lastMessageDate);
            createFacebookConversation(conversation, message);
        }

    }

    private String getUserByIdFromFacebook(String id) {
        try {
            String url = FB_BASE_URL + "/" + id + "?fields=name&access_token=" + FB_PAGE_ACCESS_TOKEN;
            WebClient client = WebClient.create(url);
            Mono<FacebookApiUserDto> mono = client.get().retrieve().bodyToMono(FacebookApiUserDto.class);
            FacebookApiUserDto dto = mono.block();
            return dto.getName();
        } catch (Exception e) {
            return null;
        }
    }

    private void saveFacebookMessage(String id, LocalDateTime lastMessageDate, FacebookWebhookDtoMessage dtoMessage) {
        String mId = dtoMessage.getMid();
        boolean sentByClient = true;
        String text = null;
        List<MongoDbMessagesAttachment> attachments = null;

        if (dtoMessage.isTextMessage())
            text = dtoMessage.getText();
        if (dtoMessage.hasAttachment()) {
            var dtoAttachments = dtoMessage.getAttachments();
            for (var item : dtoAttachments) {
                String url = null;
                String type = item.getType();
                var payload = item.getPayload();
                if (payload != null)
                    url = payload.getUrl();
                attachments.add(new MongoDbMessagesAttachment(type, url));
            }
        }

        var message = new MongoDbMessagesItem(mId, lastMessageDate, sentByClient, text, attachments);
        var webSocketClientMessage = new WebSocketClientMessage(id, message);
        chatController.sendMessage(webSocketClientMessage);
    }

    private void createFacebookConversation(Conversation conversation, FacebookWebhookDtoMessage dtoMessage) {
        String mId = dtoMessage.getMid();
        boolean sentByClient = true;
        String text = null;
        List<MongoDbMessagesAttachment> attachments = null;

        if (dtoMessage.isTextMessage())
            text = dtoMessage.getText();
        if (dtoMessage.hasAttachment()) {
            var dtoAttachments = dtoMessage.getAttachments();
            for (var item : dtoAttachments) {
                String url = null;
                String type = item.getType();
                var payload = item.getPayload();
                if (payload != null)
                    url = payload.getUrl();
                attachments.add(new MongoDbMessagesAttachment(type, url));
            }
        }

        LocalDateTime sentDate = conversation.getLastMessageDate();
        var message = new MongoDbMessagesItem(mId, sentDate, sentByClient, text, attachments);
        var webSocketClientConversation = new WebSocketClientConversation(conversation, message);
        chatController.createConversation(webSocketClientConversation);
    }
}
