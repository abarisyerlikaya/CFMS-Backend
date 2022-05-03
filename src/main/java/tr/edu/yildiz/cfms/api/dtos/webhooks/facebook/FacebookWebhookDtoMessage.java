package tr.edu.yildiz.cfms.api.dtos.webhooks.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class FacebookWebhookDtoMessage {
    @Getter
    @Setter
    private String mid;

    @Getter
    @Setter
    private String text;

    @Getter
    @Setter
    @JsonProperty("reply_to")
    private FacebookWebhookDtoReplyTo replyTo;

    @Getter
    @Setter
    @JsonProperty("attachments")
    private List<FacebookWebhookDtoAttachment> attachments;

    @Getter
    @Setter
    @JsonProperty("referral")
    private FacebookWebhookDtoObjectWithId referral; // TODO bu neydi

    public boolean isTextMessage() {
        return this.text != null && this.text.length() > 0;
    }

    public boolean isReplyMessage() {
        if (this.replyTo == null)
            return false;

        String mid = this.replyTo.getMid();
        return mid != null && mid.length() > 0;
    }

    public boolean hasAttachment() {
        return this.attachments != null && this.attachments.size() > 0;
    }
}
