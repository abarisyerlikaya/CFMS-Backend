package tr.edu.yildiz.cfms.api.dtos.webhooks.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class FacebookWebhookDtoMessaging {
    @Getter
    @Setter
    @JsonProperty("sender")
    private FacebookWebhookDtoObjectWithId sender;

    @Getter
    @Setter
    @JsonProperty("recipient")
    private FacebookWebhookDtoObjectWithId recipient;

    @Getter
    @Setter
    private long timestamp;

    @Getter
    @Setter
    @JsonProperty("message")
    private FacebookWebhookDtoMessage message;

    public String getSenderId() {
        var sender = this.getSender();
        if (sender == null)
            return null;

        return sender.getId();
    }
}
