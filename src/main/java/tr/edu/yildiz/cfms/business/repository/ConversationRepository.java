package tr.edu.yildiz.cfms.business.repository;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import tr.edu.yildiz.cfms.entities.concretes.Conversation;


import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface Phone repository.
 *
 * @author Robley Gori - ro6ley.github.io
 */
public interface ConversationRepository extends JpaRepository<Conversation, String> {
}
