package tr.edu.yildiz.cfms.entities.concretes;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import tr.edu.yildiz.cfms.core.enums.Platform;

import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name="conversations")
@EntityListeners(AuditingEntityListener.class)
public class Conversation {
    @Id
    @Column(name="id")
    private String id;

    @Column(name="platform")
    private Platform platform;

    @Column(name="client_name")
    private String clientName;

    @Column(name="last_message_date")
    private LocalDateTime lastMessageDate;


    public Conversation(String id, Platform platform, String clientName, LocalDateTime lastMessageDate) {
        this.id = id;
        this.platform = platform;
        this.clientName = clientName;
        this.lastMessageDate = lastMessageDate;
    }

    public Conversation() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public LocalDateTime getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(LocalDateTime lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }

}
