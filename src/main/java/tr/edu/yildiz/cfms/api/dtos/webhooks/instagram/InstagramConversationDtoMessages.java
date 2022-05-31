package tr.edu.yildiz.cfms.api.dtos.webhooks.instagram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class InstagramConversationDtoMessages {

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String text;

    @Getter
    @Setter
    @JsonProperty("sentByClient")
    private Boolean isClient;

    @Getter
    @Setter
    private String date;
}
