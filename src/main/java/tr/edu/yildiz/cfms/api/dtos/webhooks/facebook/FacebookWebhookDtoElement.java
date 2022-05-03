package tr.edu.yildiz.cfms.api.dtos.webhooks.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class FacebookWebhookDtoElement {
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    @JsonProperty("retailer_id")
    private String retailerId;

    @Getter
    @Setter
    @JsonProperty("image_url")
    private String imageUrl;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private String subtitle;
}
