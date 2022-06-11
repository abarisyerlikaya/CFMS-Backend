package tr.edu.yildiz.cfms.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tr.edu.yildiz.cfms.entities.concretes.hibernate.Conversation;

import java.util.List;


public interface ConversationRepository extends JpaRepository<Conversation, String> {
    @Query("SELECT COUNT(c) FROM Conversation c WHERE c.assignedTo=:username")
    int countByUsername(@Param("username") String username);

    @Query("SELECT c FROM Conversation c WHERE c.assignedTo=:username AND c.isActive = true")
    List<Conversation> findByUsername(@Param("username") String username);
}
