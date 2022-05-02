package tr.edu.yildiz.cfms.api.dtos.webhooks.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class _Attachment {
    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    @JsonProperty("payload")
    private _Payload payload;
}
