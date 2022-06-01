package tr.edu.yildiz.cfms.api.dtos.webhooks.twitter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

public class TwitterWebhookDto {
    @Getter
    @Setter
    @JsonProperty("direct_message_events")
    private List<TwitterWebhookDtoEvent> events;

    @Getter
    @Setter
    @JsonProperty("users")
    private Map<String, TwitterWebhookDtoUser> users;
}
