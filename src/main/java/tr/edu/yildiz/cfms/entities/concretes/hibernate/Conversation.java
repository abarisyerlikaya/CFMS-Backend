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
@Table(name="conversations")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Conversation {
    @Id
    @Getter
    @Setter
    @Column(name="id")
    private String id;

    @Getter
    @Setter
    @Column(name="platform")
    private Platform platform;

    @Getter
    @Setter
    @Column(name="client_name")
    private String clientName;

    @Getter
    @Setter
    @Column(name="client_id")
    private String clientId;

    @Getter
    @Setter
    @Column(name="last_message_date")
    private LocalDateTime lastMessageDate;

}
