package tr.edu.yildiz.cfms.api.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import tr.edu.yildiz.cfms.core.enums.Platform;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
public class Message {
    private String message;
    private Platform platform;
    private String conversationId;
    private LocalDateTime date;
    // TODO: change variables in this class and think about what we can add to this class

}
