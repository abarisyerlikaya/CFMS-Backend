package tr.edu.yildiz.cfms.api.models;

import lombok.Getter;
import lombok.Setter;

public class GetConversationDetailRequest {
    @Getter
    @Setter
    private String conversationId;

    @Getter
    @Setter
    private boolean includeMessages;
}
