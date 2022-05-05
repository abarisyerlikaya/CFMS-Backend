package tr.edu.yildiz.cfms.core.response_types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.edu.yildiz.cfms.core.enums.WebSocketEvent;

@AllArgsConstructor
public class WebSocketServerMessage<T> {
    @Getter
    @Setter
    private WebSocketEvent event;

    @Getter
    @Setter
    private T payload;

    @Getter
    @Setter
    private boolean success = true;

    public WebSocketServerMessage(WebSocketEvent event, T payload) {
        this.event = event;
        this.payload = payload;
    }
}
