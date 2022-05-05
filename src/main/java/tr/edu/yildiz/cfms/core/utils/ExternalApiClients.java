package tr.edu.yildiz.cfms.core.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tr.edu.yildiz.cfms.api.dtos.apis.facebook.FacebookApiUserDto;
import tr.edu.yildiz.cfms.entities.concretes.hibernate.Conversation;
import tr.edu.yildiz.cfms.entities.concretes.mongodb.MongoDbMessagesItem;

import static tr.edu.yildiz.cfms.core.utils.Constants.FB_BASE_URL;
import static tr.edu.yildiz.cfms.core.utils.Constants.FB_PAGE_ACCESS_TOKEN;

public class ExternalApiClients {
    public static String sendMessageWithFacebook(Conversation conversation, MongoDbMessagesItem mongoDbMessagesItem) {
        String url = FB_BASE_URL + "/v13.0/me/messages?access_token=" + FB_PAGE_ACCESS_TOKEN;
        String conversationId = conversation.getId();
        String clientId = conversationId.substring(conversationId.indexOf('_') + 1);
        String messageText = mongoDbMessagesItem.getText();

        var requestBody = new FacebookSendMessageRequestBody(clientId, messageText);

        WebClient client = WebClient.create(url);
        Mono<FacebookSendMessageResponseDto> mono = client
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(FacebookSendMessageResponseDto.class);

        FacebookSendMessageResponseDto dto = mono.block();
        String messageId = dto.getMessageId();


        return messageId;
    }

    private static class FacebookSendMessageRequestBody {
        @Getter
        @Setter
        @JsonProperty("recipient")
        private _Recipient recipient;

        @Getter
        @Setter
        @JsonProperty("message")
        private _Message message;

        public FacebookSendMessageRequestBody(String recipientId, String messageText) {
            this.recipient = new _Recipient(recipientId);
            this.message = new _Message(messageText);
        }

        @AllArgsConstructor
        class _Recipient {
            @Getter
            @Setter
            private String id;
        }

        @AllArgsConstructor
        class _Message {
            @Getter
            @Setter
            private String text;
        }
    }

    private static class FacebookSendMessageResponseDto {
        @Getter
        @Setter
        @JsonProperty("recipient_id")
        private String recipientId;

        @Getter
        @Setter
        @JsonProperty("message_id")
        private String messageId;
    }
}
