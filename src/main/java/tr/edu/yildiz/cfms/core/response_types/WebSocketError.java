package tr.edu.yildiz.cfms.core.response_types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class WebSocketError {
    @Getter
    @Setter
    private String code = "AN_ERROR_OCCURRED";

    @Getter
    @Setter
    private String message = "An error occurred!";
}
