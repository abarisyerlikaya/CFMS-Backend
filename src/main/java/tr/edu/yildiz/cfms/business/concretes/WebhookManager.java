package tr.edu.yildiz.cfms.business.concretes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tr.edu.yildiz.cfms.api.dtos.apis.facebook.FacebookApiUserDto;
import tr.edu.yildiz.cfms.api.dtos.webhooks.facebook.FacebookWebhookDto;
import tr.edu.yildiz.cfms.api.dtos.webhooks.facebook.FacebookWebhookDtoAttachment;
import tr.edu.yildiz.cfms.api.dtos.webhooks.facebook.FacebookWebhookDtoEntry;
import tr.edu.yildiz.cfms.api.dtos.webhooks.facebook.FacebookWebhookDtoMessage;
import tr.edu.yildiz.cfms.business.abstracts.WebhookService;
import tr.edu.yildiz.cfms.business.repository.ConversationRepository;
import tr.edu.yildiz.cfms.core.enums.Platform;
import tr.edu.yildiz.cfms.entities.concretes.Conversation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.time.Instant.ofEpochMilli;
import static java.util.TimeZone.getDefault;
import static tr.edu.yildiz.cfms.core.utils.Constants.FB_BASE_URL;
import static tr.edu.yildiz.cfms.core.utils.Constants.FB_PAGE_ACCESS_TOKEN;

@Service
public class WebhookManager implements WebhookService {
    @Autowired
    private ConversationRepository conversationRepository;

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
            updateConversationWithMessage(conversationId, lastMessageDate, message);
        } else {
            Platform platform = Platform.FACEBOOK;
            String clientName = getUserByIdFromFacebook(clientId);
            var conversation = new Conversation(conversationId, platform, clientName, clientId, lastMessageDate);
            createConversationWithMessage(conversation, message);
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

    private void updateConversationWithMessage(String id, LocalDateTime lastMessageDate, FacebookWebhookDtoMessage message) {
        boolean sentByClient = true;
        String text = null;
        String repliedTo = null;
        List<FacebookWebhookDtoAttachment> attachments = null;

        if (message.isTextMessage())
            text = message.getText();
        if (message.isReplyMessage())
            repliedTo = message.getReplyTo().getMid();
        if (message.hasAttachment())
            attachments = message.getAttachments();

        Optional<Conversation> optional = conversationRepository.findById(id);
        if (optional.isEmpty())
            return;

        Conversation conversation = optional.get();
        conversation.setLastMessageDate(lastMessageDate);
        conversationRepository.save(conversation);
    }

    private void createConversationWithMessage(Conversation conversation, FacebookWebhookDtoMessage message) {
        boolean sentByClient = true;
        String text = null;
        String repliedTo = null;
        List<FacebookWebhookDtoAttachment> attachments = null;

        if (message.isTextMessage())
            text = message.getText();
        if (message.isReplyMessage())
            repliedTo = message.getReplyTo().getMid();
        if (message.hasAttachment())
            attachments = message.getAttachments();

        conversationRepository.save(conversation);
    }

}
