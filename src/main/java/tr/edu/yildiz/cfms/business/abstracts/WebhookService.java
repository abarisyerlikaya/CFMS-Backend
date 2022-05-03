package tr.edu.yildiz.cfms.business.abstracts;

import org.springframework.stereotype.Service;
import tr.edu.yildiz.cfms.api.dtos.webhooks.facebook.FacebookWebhookDto;

@Service
public interface WebhookService {
    void handleWebhook(FacebookWebhookDto dto);
}
