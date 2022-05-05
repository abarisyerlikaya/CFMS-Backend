package tr.edu.yildiz.cfms.api.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.edu.yildiz.cfms.entities.concretes.hibernate.Conversation;
import tr.edu.yildiz.cfms.entities.concretes.mongodb.MongoDbMessagesItem;

@NoArgsConstructor
@AllArgsConstructor
public class WebSocketClientConversation {
    @Getter
    @Setter
    private Conversation conversation;

    @Getter
    @Setter
    private MongoDbMessagesItem message;
}
