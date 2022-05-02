package tr.edu.yildiz.cfms.api.dtos.webhooks.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class _Payload {
    @Getter
    @Setter
    private String url;

    @Getter
    @Setter
    @JsonProperty("product")
    private _Product product;

    @Getter
    @Setter
    private String title;
}
