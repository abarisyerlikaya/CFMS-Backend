package tr.edu.yildiz.cfms.entities.concretes.mongodb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class MongoDbMessagesItem {
    @Getter
    @Setter
    private String id = UUID.randomUUID().toString();

    @Getter
    @Setter
    private LocalDateTime sentDate = LocalDateTime.now();

    @Getter
    @Setter
    private boolean sentByClient;

    @Getter
    @Setter
    private String text;

    @Getter
    @Setter
    @Field("attachments")
    private List<MongoDbMessagesAttachment> attachments;
}
