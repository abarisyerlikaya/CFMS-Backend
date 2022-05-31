package tr.edu.yildiz.cfms.api.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.edu.yildiz.cfms.entities.concretes.mongodb.MongoDbMessagesItem;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class WebSocketClientMessages {

    @Getter
    @Setter
    private String conversationId;

    @Getter
    @Setter
    private List<MongoDbMessagesItem> messages;
}
