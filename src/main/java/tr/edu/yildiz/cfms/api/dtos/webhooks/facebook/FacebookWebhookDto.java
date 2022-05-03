package tr.edu.yildiz.cfms.api.dtos.webhooks.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class FacebookWebhookDto {
    @Getter
    @Setter
    private String object;

    @Getter
    @Setter
    @JsonProperty("entry")
    private List<FacebookWebhookDtoEntry> entry;
}
