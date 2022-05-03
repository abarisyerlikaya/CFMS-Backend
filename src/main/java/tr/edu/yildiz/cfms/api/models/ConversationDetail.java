package tr.edu.yildiz.cfms.api.models;

import lombok.AllArgsConstructor;
import tr.edu.yildiz.cfms.core.enums.Platform;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class ConversationDetail {
    private String id;
    private Platform platform;
    private String clientName;
    private String clientId;
    private LocalDateTime lastMessageDate;
    private List<Message> messages;
}
