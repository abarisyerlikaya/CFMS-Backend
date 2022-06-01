package tr.edu.yildiz.cfms.api.dtos.webhooks.twitter;

import lombok.Getter;
import lombok.Setter;

public class TwitterWebhookDtoMessageData {
    @Getter
    @Setter
    private String text;
}
