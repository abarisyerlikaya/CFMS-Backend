package tr.edu.yildiz.cfms.api.dtos.webhooks.instagram;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class InstagramConversationDto {
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String clientName;

    @Getter
    @Setter
    private String lastMessageDate;

    @Getter
    @Setter
    private String lastMessageText;

    @Getter
    @Setter
    private String lastMessageId;

    // Messages list
    @Getter
    @Setter
    private ArrayList<InstagramConversationDtoMessages> messages;
}
