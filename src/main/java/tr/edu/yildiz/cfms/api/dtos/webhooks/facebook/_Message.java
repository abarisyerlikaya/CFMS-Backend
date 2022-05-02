package tr.edu.yildiz.cfms.api.dtos.webhooks.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class _Message {
    @Getter
    @Setter
    private String mid;

    @Getter
    @Setter
    private String text;

    @Getter
    @Setter
    @JsonProperty("quick_reply")
    private _QuickReply quickReply;

    @Getter
    @Setter
    @JsonProperty("reply_to")
    private _ReplyTo replyTo;

    @Getter
    @Setter
    @JsonProperty("attachments")
    private List<_Attachment> attachments;

    @Getter
    @Setter
    @JsonProperty("referral")
    private _ObjectWithId referral;
}
