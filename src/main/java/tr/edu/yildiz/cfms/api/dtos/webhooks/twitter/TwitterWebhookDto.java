package tr.edu.yildiz.cfms.api.dtos.webhooks.twitter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class TwitterWebhookDto {
    @Getter
    @Setter
    @JsonProperty("direct_message_events")
    private List<TwitterWebhookDtoEvent> events;
}
