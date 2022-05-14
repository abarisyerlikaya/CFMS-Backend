package tr.edu.yildiz.cfms.api.dtos.apis.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class TelegramApiFilePathDto {
    @Getter
    @Setter
    private boolean ok;

    @Getter
    @Setter
    @JsonProperty("result")
    private TelegramApiFilePathDtoResult result;
}
