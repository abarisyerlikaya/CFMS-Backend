package tr.edu.yildiz.cfms.api.dtos.apis.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class TelegramApiFilePathDtoResult {
    @Getter
    @Setter
    @JsonProperty("file_path")
    private String filePath;
}
