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
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @Getter
    @Setter
    @Column(name="username")
    private String username;

    @Getter
    @Setter
    @Column(name="first_name")
    private String firstName;

    @Getter
    @Setter
    @Column(name="last_name")
    private String lastName;

    @Getter
    @Setter
    @Column(name="password")
    private String password;
}
