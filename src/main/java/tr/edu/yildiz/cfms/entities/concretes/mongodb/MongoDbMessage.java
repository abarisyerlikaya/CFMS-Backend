package tr.edu.yildiz.cfms.entities.concretes.mongodb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class MongoDbMessage {
    @Getter
    @Setter
    private LocalDateTime sentDate;

    @Getter
    @Setter
    private boolean sentByClient;

    @Getter
    @Setter
    private String text;

    @Getter
    @Setter
    private String repliedTo;

    @Getter
    @Setter
    @Field("attachments")
    private List<MongoDbMessageAttachment> attachments;
}
