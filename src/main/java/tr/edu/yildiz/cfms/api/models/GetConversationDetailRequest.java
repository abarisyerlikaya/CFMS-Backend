package tr.edu.yildiz.cfms.api.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class GetConversationDetailRequest extends PaginationRequest {
    @Getter
    @Setter
    private String conversationId;

    @Getter
    @Setter
    private boolean withMessages = true;
}
