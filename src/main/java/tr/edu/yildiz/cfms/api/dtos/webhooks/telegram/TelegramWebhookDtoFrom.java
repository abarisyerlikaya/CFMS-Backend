package tr.edu.yildiz.cfms.api.dtos.webhooks.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class TelegramWebhookDtoFrom {
    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    @JsonProperty("first_name")
    private String firstName;

    @Getter
    @Setter
    @JsonProperty("last_name")
    private String lastName;
}
