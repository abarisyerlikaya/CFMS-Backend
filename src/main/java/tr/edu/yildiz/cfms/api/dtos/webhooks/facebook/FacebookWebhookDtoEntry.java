package tr.edu.yildiz.cfms.api.dtos.webhooks.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class FacebookWebhookDtoEntry {
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private long time;

    @Getter
    @Setter
    @JsonProperty("messaging")
    private List<FacebookWebhookDtoMessaging> messaging;

    public FacebookWebhookDtoMessaging getMessagingObject() {
        var messagingList = this.getMessaging();
        if (messagingList == null || messagingList.size() <= 0)
            return null;
        return messagingList.get(0);
    }
}
