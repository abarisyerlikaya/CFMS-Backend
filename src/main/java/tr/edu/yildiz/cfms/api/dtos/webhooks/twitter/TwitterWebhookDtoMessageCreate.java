package tr.edu.yildiz.cfms.api.dtos.webhooks.twitter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class TwitterWebhookDtoMessageCreate {
    @Getter
    @Setter
    @JsonProperty("sender_id")
    private String senderId;

    @Getter
    @Setter
    @JsonProperty("message_data")
    private TwitterWebhookDtoMessageData messageData;

    @Getter
    @Setter
    @JsonProperty("attachment")
    private TwitterWebhookDtoAttachment attachment;
}
