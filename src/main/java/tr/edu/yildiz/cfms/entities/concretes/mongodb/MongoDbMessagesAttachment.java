package tr.edu.yildiz.cfms.entities.concretes.mongodb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class MongoDbMessagesAttachment {
    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    private String url;
}
