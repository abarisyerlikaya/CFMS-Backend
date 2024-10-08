package tr.edu.yildiz.cfms.api.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class GetConversationsRequest extends PaginationRequest {
    @Getter
    @Setter
    boolean withMessages = false;
}
