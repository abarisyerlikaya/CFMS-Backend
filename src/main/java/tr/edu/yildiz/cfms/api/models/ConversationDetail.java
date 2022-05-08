package tr.edu.yildiz.cfms.api.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.edu.yildiz.cfms.core.enums.Platform;
import tr.edu.yildiz.cfms.entities.concretes.hibernate.Conversation;
import tr.edu.yildiz.cfms.entities.concretes.mongodb.MongoDbMessagesItem;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class ConversationDetail {
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private int platform;

    @Getter
    @Setter
    private String clientName;

    @Getter
    @Setter
    private LocalDateTime lastMessageDate;

    @Getter
    @Setter
    private List<MongoDbMessagesItem> messages;

    public ConversationDetail(Conversation conversation, List<MongoDbMessagesItem> messages) {
        this.id = conversation.getId();
        this.platform = conversation.getPlatform().ordinal();
        this.clientName = conversation.getClientName();
        this.lastMessageDate = conversation.getLastMessageDate();
        this.messages = messages;
    }
}
