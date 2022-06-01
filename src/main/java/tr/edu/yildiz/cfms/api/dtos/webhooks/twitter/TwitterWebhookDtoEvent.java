package tr.edu.yildiz.cfms.api.dtos.webhooks.twitter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class TwitterWebhookDtoEvent {
    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    @JsonProperty("created_timestamp")
    private String createdTimestamp;

    @Getter
    @Setter
    @JsonProperty("message_create")
    private TwitterWebhookDtoMessageCreate messageCreate;
}
