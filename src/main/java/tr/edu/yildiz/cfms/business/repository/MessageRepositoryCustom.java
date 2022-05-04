package tr.edu.yildiz.cfms.business.repository;

import tr.edu.yildiz.cfms.entities.concretes.mongodb.MongoDbMessagesItem;

public interface MessageRepositoryCustom {
    void pushMessage(String id, MongoDbMessagesItem message);
}
