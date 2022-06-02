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

    public boolean isTextMessage() {
        if (messageData == null)
            return false;
        String text = messageData.getText();
        return text != null && !text.isEmpty();
    }

    public boolean hasAttachment() {
        if (attachment == null)
            return false;
        var type = attachment.getType();
        var media = attachment.getMedia();
        if (type == null || type.isEmpty() || media == null)
            return false;
        var id = media.getId();
        var url = media.getUrl();
        return id != null && !id.isEmpty() && url != null && !url.isEmpty();
    }
}
