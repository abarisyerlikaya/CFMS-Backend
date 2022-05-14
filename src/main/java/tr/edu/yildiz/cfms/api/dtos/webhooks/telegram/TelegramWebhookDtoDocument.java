package tr.edu.yildiz.cfms.api.dtos.webhooks.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class TelegramWebhookDtoDocument {
    @Getter
    @Setter
    @JsonProperty("file_name")
    private String fileName;

    @Getter
    @Setter
    @JsonProperty("mime_type")
    private String mimeType;

    @Getter
    @Setter
    @JsonProperty("file_id")
    private String fileId;

    @Getter
    @Setter
    @JsonProperty("file_unique_id")
    private String fileUniqueId;

    @Getter
    @Setter
    @JsonProperty("file_size")
    private int fileSize;
}
