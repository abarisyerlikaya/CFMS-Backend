package tr.edu.yildiz.cfms.business.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tr.edu.yildiz.cfms.entities.concretes.mongodb.MongoDbMessages;

public interface MessageRepositoryDefault extends MongoRepository<MongoDbMessages, String> {
}
