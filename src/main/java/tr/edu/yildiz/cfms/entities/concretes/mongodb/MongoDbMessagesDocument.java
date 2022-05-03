package tr.edu.yildiz.cfms.entities.concretes.mongodb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "messages")
public class MongoDbMessagesDocument {
    @Id
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    @Field("messages")
    List<MongoDbMessage> messages;
}
