package tr.edu.yildiz.cfms.api.dtos.webhooks.twitter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class TwitterWebhookDtoAttachment {
    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    @JsonProperty("media")
    private TwitterWebhookDtoMedia media;
}
