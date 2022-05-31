package tr.edu.yildiz.cfms.business.abstracts;

import org.springframework.stereotype.Service;
import tr.edu.yildiz.cfms.api.dtos.webhooks.facebook.FacebookWebhookDto;
import tr.edu.yildiz.cfms.api.dtos.webhooks.instagram.InstagramConversationDto;
import tr.edu.yildiz.cfms.api.dtos.webhooks.telegram.TelegramWebhookDto;

@Service
public interface WebhookService {
    void handleFacebookWebhook(FacebookWebhookDto dto);
    void handleTelegramWebhook(TelegramWebhookDto dto);
    void handleInstagramConversation(InstagramConversationDto dto, Boolean isNew);
}
