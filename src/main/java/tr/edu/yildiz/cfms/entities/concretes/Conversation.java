package tr.edu.yildiz.cfms.entities.concretes;

import tr.edu.yildiz.cfms.core.enums.Platform;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="conversations")
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

    @Column(name="messages_url")
    private String messagesUrl;

    public Conversation(String id, Platform platform, String clientName, LocalDateTime lastMessageDate, String messagesUrl) {
        this.id = id;
        this.platform = platform;
        this.clientName = clientName;
        this.lastMessageDate = lastMessageDate;
        this.messagesUrl = messagesUrl;
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

    public String getMessagesUrl() {
        return messagesUrl;
    }

    public void setMessagesUrl(String messagesUrl) {
        this.messagesUrl = messagesUrl;
    }
}
