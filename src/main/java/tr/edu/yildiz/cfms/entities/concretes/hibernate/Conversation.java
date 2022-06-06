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
    @Column(name="last_message_date")
    private LocalDateTime lastMessageDate;

    @Getter
    @Setter
    @Column(name="last_message_preview")
    private String lastMessagePreview;

    @Getter
    @Setter
    @Column(name="is_active")
    private boolean isActive = true;

    @Getter
    @Setter
    @Column(name="assigned_to")
    private String assignedTo = null;

    public Conversation(String id, Platform platform, String clientName, LocalDateTime lastMessageDate, String lastMessagePreview) {
        this.id = id;
        this.platform = platform;
        this.clientName = clientName;
        this.lastMessageDate = lastMessageDate;
        this.lastMessagePreview = lastMessagePreview;
    }
}
