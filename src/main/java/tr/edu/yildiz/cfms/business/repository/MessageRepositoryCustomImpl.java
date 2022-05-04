package tr.edu.yildiz.cfms.business.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import tr.edu.yildiz.cfms.entities.concretes.mongodb.MongoDbMessagesItem;
import tr.edu.yildiz.cfms.entities.concretes.mongodb.MongoDbMessages;

public class MessageRepositoryCustomImpl implements MessageRepositoryCustom {
    @Autowired
    protected MongoTemplate mongoTemplate;

    @Override
    public void pushMessage(String id, MongoDbMessagesItem message) {
        mongoTemplate.updateFirst(
                Query.query(Criteria.where("id").is(id)),
                new Update().push("messages", message),
                MongoDbMessages.class
        );
    }
}

