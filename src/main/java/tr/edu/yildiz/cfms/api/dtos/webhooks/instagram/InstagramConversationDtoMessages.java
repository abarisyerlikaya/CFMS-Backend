package tr.edu.yildiz.cfms.api.dtos.webhooks.instagram;

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
    private Boolean isClient;

    @Getter
    @Setter
    private String date;
}
