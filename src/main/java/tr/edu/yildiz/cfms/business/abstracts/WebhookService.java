package tr.edu.yildiz.cfms.business.abstracts;

import org.springframework.stereotype.Service;
import tr.edu.yildiz.cfms.api.dtos.webhooks.facebook.FacebookWebhookDto;
import tr.edu.yildiz.cfms.api.dtos.webhooks.instagram.InstagramConversationDto;
import tr.edu.yildiz.cfms.api.dtos.webhooks.telegram.TelegramWebhookDto;
import tr.edu.yildiz.cfms.api.dtos.webhooks.twitter.TwitterWebhookDto;

import java.util.Map;

@Service
public interface WebhookService {
    void handleFacebookWebhook(FacebookWebhookDto dto);
    void handleTelegramWebhook(TelegramWebhookDto dto);
    void handleInstagramConversation(InstagramConversationDto dto, Boolean isNew);
    Map<String, String> verifyTwitterWebhook(String crcToken);
    void handleTwitterWebhook(TwitterWebhookDto dto);
}
