package tr.edu.yildiz.cfms.api.dtos.webhooks.twitter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class TwitterWebhookDtoVariants {
    @Getter
    @Setter
    private int bitrate = -1;

    @Getter
    @Setter
    @JsonProperty("content_type")
    private String contentType;

    @Getter
    @Setter
    private String url;
}
