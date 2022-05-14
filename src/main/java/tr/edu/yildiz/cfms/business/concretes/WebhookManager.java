package tr.edu.yildiz.cfms.business.concretes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tr.edu.yildiz.cfms.api.controllers.ChatController;
import tr.edu.yildiz.cfms.api.dtos.apis.facebook.FacebookApiUserDto;
import tr.edu.yildiz.cfms.api.dtos.apis.telegram.TelegramApiFilePathDto;
import tr.edu.yildiz.cfms.api.dtos.webhooks.facebook.FacebookWebhookDto;
import tr.edu.yildiz.cfms.api.dtos.webhooks.facebook.FacebookWebhookDtoEntry;
import tr.edu.yildiz.cfms.api.dtos.webhooks.facebook.FacebookWebhookDtoMessage;
import tr.edu.yildiz.cfms.api.dtos.webhooks.telegram.TelegramWebhookDto;
import tr.edu.yildiz.cfms.api.dtos.webhooks.telegram.TelegramWebhookDtoMessage;
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
import java.util.ArrayList;
import java.util.List;

import static java.time.Instant.ofEpochMilli;
import static java.util.TimeZone.getDefault;
import static tr.edu.yildiz.cfms.core.utils.Constants.*;

@Service
public class WebhookManager implements WebhookService {
    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatController chatController;


    @Override
    public void handleFacebookWebhook(FacebookWebhookDto dto) {
        if (!dto.getObject().equals("page")) return;

        var entries = dto.getEntry();

        for (var entry : entries)
            processFacebookEntry(entry);

    }

    @Override
    public void handleTelegramWebhook(TelegramWebhookDto dto) {
        var message = dto.getMessage();
        if (message == null) return;

        processTelegramMessage(message);
    }


    private void processFacebookEntry(FacebookWebhookDtoEntry entry) {
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

        if (dtoMessage.isTextMessage()) text = dtoMessage.getText();
        if (dtoMessage.hasAttachment()) {
            attachments = new ArrayList<>();
            var dtoAttachments = dtoMessage.getAttachments();
            for (var item : dtoAttachments) {
                String url = null;
                String type = item.getType();
                var payload = item.getPayload();
                if (payload != null) url = payload.getUrl();
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

        if (dtoMessage.isTextMessage()) text = dtoMessage.getText();
        if (dtoMessage.hasAttachment()) {
            var dtoAttachments = dtoMessage.getAttachments();
            for (var item : dtoAttachments) {
                String url = null;
                String type = item.getType();
                var payload = item.getPayload();
                if (payload != null) url = payload.getUrl();
                attachments.add(new MongoDbMessagesAttachment(type, url));
            }
        }

        LocalDateTime sentDate = conversation.getLastMessageDate();
        var message = new MongoDbMessagesItem(mId, sentDate, sentByClient, text, attachments);
        var webSocketClientConversation = new WebSocketClientConversation(conversation, message);
        chatController.createConversation(webSocketClientConversation);
    }

    private void processTelegramMessage(TelegramWebhookDtoMessage message) {
        var from = message.getFrom();
        String clientName = from.getFirstName() + " " + from.getLastName();
        LocalDateTime lastMessageDate = LocalDateTime.ofInstant(ofEpochMilli(message.getDate()), getDefault().toZoneId());
        String conversationId = Long.toString(message.getChat().getId());

        boolean doesExist = conversationRepository.existsById(conversationId);

        if (doesExist) {
            saveTelegramMessage(conversationId, lastMessageDate, message);
        } else {
            Platform platform = Platform.TELEGRAM;
            var conversation = new Conversation(conversationId, platform, clientName, lastMessageDate);
            createTelegramConversation(conversation, message);
        }
    }

    private void saveTelegramMessage(String conversationId, LocalDateTime lastMessageDate, TelegramWebhookDtoMessage dtoMessage) {
        String mId = Long.toString(dtoMessage.getMessageId());
        boolean sentByClient = true;
        String text = null;
        List<MongoDbMessagesAttachment> attachments = null;

        if (dtoMessage.isTextMessage()) text = dtoMessage.getText();
        if (dtoMessage.hasAttachment()) {
            var attachment = getAttachmentFromTelegram(dtoMessage);
            if (attachment != null) {
                attachments = new ArrayList<>();
                attachments.add(attachment);
            }
        }

        var message = new MongoDbMessagesItem(mId, lastMessageDate, sentByClient, text, attachments);
        var webSocketClientMessage = new WebSocketClientMessage(conversationId, message);
        chatController.sendMessage(webSocketClientMessage);
    }

    private void createTelegramConversation(Conversation conversation, TelegramWebhookDtoMessage dtoMessage) {
        String mId = Long.toString(dtoMessage.getMessageId());
        boolean sentByClient = true;
        String text = null;
        List<MongoDbMessagesAttachment> attachments = null;

        if (dtoMessage.isTextMessage())
            text = dtoMessage.getText();
        if (dtoMessage.hasAttachment()) {
            var attachment = getAttachmentFromTelegram(dtoMessage);
            if (attachment != null) {
                attachments = new ArrayList<>();
                attachments.add(attachment);
            }
        }

        LocalDateTime sentDate = conversation.getLastMessageDate();
        var message = new MongoDbMessagesItem(mId, sentDate, sentByClient, text, attachments);
        var webSocketClientConversation = new WebSocketClientConversation(conversation, message);
        chatController.createConversation(webSocketClientConversation);
    }

    private MongoDbMessagesAttachment getAttachmentFromTelegram(TelegramWebhookDtoMessage dtoMessage) {
        try {
            var photos = dtoMessage.getPhoto();
            var video = dtoMessage.getVideo();
            var voice = dtoMessage.getVoice();
            var document = dtoMessage.getDocument();
            String fileId = null;
            String fileType = null;

            if (photos != null && photos.size() > 0) {
                fileId = photos.get(photos.size() - 1).getFileId();
                fileType = "image";
            } else if (video != null) {
                fileId = video.getFileId();
                fileType = "audio";
            } else if (voice != null) {
                fileId = voice.getFileId();
                fileType = "video";
            } else if (document != null) {
                fileId = document.getFileId();
                fileType = "file";
            }

            if (fileId == null)
                return null;

            String url = TELEGRAM_BASE_URL + "/bot" + TELEGRAM_TOKEN + "/getFile?file_id=" + fileId;
            WebClient client = WebClient.create(url);
            Mono<TelegramApiFilePathDto> mono = client.get().retrieve().bodyToMono(TelegramApiFilePathDto.class);
            var dto = mono.block();
            var result = dto.getResult();
            if (!dto.isOk() || result == null)
                return null;
            String filePath = result.getFilePath();
            String fileUrl = TELEGRAM_BASE_URL + "/bot" + TELEGRAM_TOKEN + "/" + filePath;
            return new MongoDbMessagesAttachment(fileType, fileUrl);

        } catch (Exception e) {
            return null;
        }
    }
}
