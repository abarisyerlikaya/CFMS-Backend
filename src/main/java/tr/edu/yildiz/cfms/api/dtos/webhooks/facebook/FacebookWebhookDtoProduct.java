package tr.edu.yildiz.cfms.api.dtos.webhooks.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class FacebookWebhookDtoProduct {
    @Getter
    @Setter
    @JsonProperty("elements")
    private List<FacebookWebhookDtoElement> elements;
}
