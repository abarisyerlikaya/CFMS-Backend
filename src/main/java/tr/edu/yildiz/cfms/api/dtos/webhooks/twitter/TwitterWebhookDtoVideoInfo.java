package tr.edu.yildiz.cfms.api.dtos.webhooks.twitter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class TwitterWebhookDtoVideoInfo {
    @Getter
    @Setter
    @JsonProperty("aspect_ratio")
    private int[] aspectRatio;

    @Getter
    @Setter
    @JsonProperty("duration_millis")
    private int durationMillis;

    @Getter
    @Setter
    @JsonProperty("variants")
    private List<TwitterWebhookDtoVariants> variants;
}
