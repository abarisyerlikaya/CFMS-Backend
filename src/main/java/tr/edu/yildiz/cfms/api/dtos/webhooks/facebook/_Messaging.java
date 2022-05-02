package tr.edu.yildiz.cfms.api.dtos.webhooks.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class _Messaging {
    @Getter
    @Setter
    @JsonProperty("sender")
    private _ObjectWithId sender;

    @Getter
    @Setter
    @JsonProperty("recipient")
    private _ObjectWithId recipient;

    @Getter
    @Setter
    private long timestamp;

    @Getter
    @Setter
    @JsonProperty("message")
    private _Message message;
}
