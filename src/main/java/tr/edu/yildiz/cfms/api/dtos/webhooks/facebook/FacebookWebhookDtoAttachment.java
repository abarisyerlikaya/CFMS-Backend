package tr.edu.yildiz.cfms.api.dtos.webhooks.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class FacebookWebhookDtoAttachment {
    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    @JsonProperty("payload")
    private FacebookWebhookDtoPayload payload;

    public boolean isImage() {
        return type.equals("image");
    }

    public boolean isVideo() {
        return type.equals("video");
    }

    public boolean isAudio() { return type.equals("audio"); }

    public boolean isFile() {
        return type.equals("file");
    }

    public boolean isProductTemplate() {
        return type.equals("template");
    }

    public boolean isFallback() {
        return type.equals("fallback");
    }
}
