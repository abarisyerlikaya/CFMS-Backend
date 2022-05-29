package tr.edu.yildiz.cfms.entities.concretes.hibernate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import tr.edu.yildiz.cfms.core.enums.Platform;

import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name="conversation_assignments")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ConversationAssignment {
    @Id
    @Getter
    @Setter
    @Column(name="id")
    private int id;

    @Getter
    @Setter
    @Column(name="username")
    private String username;

    @Getter
    @Setter
    @Column(name="conversation_id")
    private String conversationId;
}
