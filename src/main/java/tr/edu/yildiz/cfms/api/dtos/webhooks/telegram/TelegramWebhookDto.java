package tr.edu.yildiz.cfms.api.dtos.webhooks.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class TelegramWebhookDto {
    @Getter
    @Setter
    @JsonProperty("message")
    private TelegramWebhookDtoMessage message;

}
