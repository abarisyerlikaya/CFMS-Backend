package tr.edu.yildiz.cfms.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.edu.yildiz.cfms.entities.concretes.hibernate.Conversation;

/**
 * The interface Phone repository.
 *
 * @author Robley Gori - ro6ley.github.io
 */
public interface ConversationRepository extends JpaRepository<Conversation, String> {
}
