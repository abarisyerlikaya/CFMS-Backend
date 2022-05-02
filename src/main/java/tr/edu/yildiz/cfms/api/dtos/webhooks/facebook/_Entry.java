package tr.edu.yildiz.cfms.api.dtos.webhooks.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class _Entry {
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private long time;

    @Getter
    @Setter
    @JsonProperty("messaging")
    private List<_Messaging> messaging;
}
