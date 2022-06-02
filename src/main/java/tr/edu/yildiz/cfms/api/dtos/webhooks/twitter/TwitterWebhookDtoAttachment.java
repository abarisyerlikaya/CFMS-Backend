package tr.edu.yildiz.cfms.api.dtos.webhooks.twitter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class TwitterWebhookDtoAttachment {
    @Setter
    private String type;

    @Getter
    @Setter
    @JsonProperty("media")
    private TwitterWebhookDtoMedia media;

    public String getType() {
        if (type != "media")
            return null;
        if (media.getVideoInfo() != null)
            return "video";
        return "image";
    }
}
