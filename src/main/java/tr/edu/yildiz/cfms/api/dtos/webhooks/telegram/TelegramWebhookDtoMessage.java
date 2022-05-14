package tr.edu.yildiz.cfms.api.dtos.webhooks.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class TelegramWebhookDtoMessage {
    @Getter
    @Setter
    @JsonProperty("message_id")
    private long messageId;

    @Getter
    @Setter
    private long date;

    @Getter
    @Setter
    private String text;

    @Getter
    @Setter
    @JsonProperty("from")
    private TelegramWebhookDtoFrom from;

    @Getter
    @Setter
    @JsonProperty("chat")
    private TelegramWebhookDtoChat chat;

    @Getter
    @Setter
    @JsonProperty("photo")
    private List<TelegramWebhookDtoPhoto> photo;

    @Getter
    @Setter
    @JsonProperty("video")
    private TelegramWebhookDtoVideo video;

    @Getter
    @Setter
    @JsonProperty("voice")
    private TelegramWebhookDtoVoice voice;

    @Getter
    @Setter
    @JsonProperty("document")
    private TelegramWebhookDtoDocument document;

    public boolean isTextMessage() {
        return this.text != null && this.text.length() > 0;
    }

    public boolean hasAttachment() {
        return this.photo != null || this.video != null || this.voice != null || this.document != null;
    }
}
