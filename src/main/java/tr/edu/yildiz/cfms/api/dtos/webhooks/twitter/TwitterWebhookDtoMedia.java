package tr.edu.yildiz.cfms.api.dtos.webhooks.twitter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class TwitterWebhookDtoMedia {
    @Getter
    @Setter
    @JsonProperty("id_str")
    private String id;

    @Getter
    @Setter
    @JsonProperty("media_url")
    private String mediaUrl;

    @Getter
    @Setter
    private String url;

    @Getter
    @Setter
    @JsonProperty("display_url")
    private String displayUrl;

    @Getter
    @Setter
    @JsonProperty("expanded_url")
    private String expandedUrl;

    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    @JsonProperty("video_info")
    private TwitterWebhookDtoVideoInfo videoInfo;
}
