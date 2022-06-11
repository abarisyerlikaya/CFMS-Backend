package tr.edu.yildiz.cfms.core.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramDirectShareRequest;
import org.brunocvcunha.instagram4j.requests.InstagramGetInboxRequest;
import org.brunocvcunha.instagram4j.requests.InstagramGetInboxThreadRequest;
import org.brunocvcunha.instagram4j.requests.payload.*;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tr.edu.yildiz.cfms.entities.concretes.hibernate.Conversation;
import tr.edu.yildiz.cfms.entities.concretes.mongodb.MongoDbMessagesItem;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static tr.edu.yildiz.cfms.core.utils.Constants.*;
import static tr.edu.yildiz.cfms.core.utils.Constants.TELEGRAM_TOKEN;

public class ExternalApiClients {

    static Instagram4j instagram;

    static {
        try {
            instagram = getInstagramClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public static String sendMessageWithTelegram(Conversation conversation, MongoDbMessagesItem mongoDbMessagesItem) {
        String url = TELEGRAM_BASE_URL + "/bot" + TELEGRAM_TOKEN + "/sendMessage";
        String conversationId = conversation.getId();
        String messageText = mongoDbMessagesItem.getText();

        var requestBody = new TelegramSendMessageRequestBody(conversationId, messageText);

        WebClient client = WebClient.create(url);
        Mono<TelegramSendMessageResponseDto> mono = client
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(TelegramSendMessageResponseDto.class);

        TelegramSendMessageResponseDto dto = mono.block();
        var result = dto.getResult();
        if (!dto.isOk() || result == null)
            return null;

        return Long.toString(result.getMessageId());
    }


    public static String sendMessageWithInstagram(Conversation conversation, MongoDbMessagesItem mongoDbMessagesItem) throws IOException {

        InstagramInboxResult inbox;;
        AtomicReference<InstagramInboxThread> inboxThread = null;
        String newConversationId = null;

        try {
            String messageText = mongoDbMessagesItem.getText();
            String conversationId = conversation.getId().replace("IG_", "");


            InstagramDirectShareRequest.ShareType type = InstagramDirectShareRequest.ShareType.MESSAGE;
            instagram.sendRequest(InstagramDirectShareRequest.builder().shareType(type).message(messageText).threadId(conversationId).build());

            inbox = instagram.sendRequest(new InstagramGetInboxRequest());
            for (InstagramInboxThread thread : inbox.getInbox().getThreads()) {
                if (thread.getThread_id().equals(conversationId)) {
                    inboxThread = new AtomicReference<>(thread);
                    break;
                }
            }

            newConversationId = inboxThread.get().getOldest_cursor();

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }


        return newConversationId;
    }

    private static Instagram4j getInstagramClient() throws IOException {

        Instagram4j instagram = Instagram4j.builder().username("bitirmeytu").password("Bitirme123").build();
        instagram.setup();
        instagram.login();

        return instagram;
    }

    public static String sendMessageWithTwitter(Conversation conversation, MongoDbMessagesItem mongoDbMessagesItem) {
        int splitIndex = "TW_".length();
        String recipientId = conversation.getId().substring(splitIndex);
        String text = mongoDbMessagesItem.getText();
        var requestBody = new TwitterSendMessageRequestBody(recipientId, text);
        WebClient client = WebClient.create(TWITTER_MESSAGE_URL);
        Mono<TwitterSendMessageResponseDto> mono = client
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .header("Authorization", TWITTER_AUTH_HEADER)
                .retrieve()
                .bodyToMono(TwitterSendMessageResponseDto.class);
        var responseDto = mono.block();
        String mId = responseDto.getMessageId();
        if (mId == null || mId.isEmpty())
            return null;
        return mId;
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

    @AllArgsConstructor
    private static class TelegramSendMessageRequestBody {
        @Getter
        @Setter
        @JsonProperty("chat_id")
        private String chatId;

        @Getter
        @Setter
        @JsonProperty("text")
        private String text;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    private static class TelegramSendMessageResponseDto {
        @Getter
        @Setter
        private boolean ok;

        @Getter
        @Setter
        @JsonProperty("result")
        private _Result result;

        @NoArgsConstructor
        @AllArgsConstructor
        class _Result {
            @Getter
            @Setter
            @JsonProperty("message_id")
            private long messageId;
        }
    }

    private static class TwitterSendMessageRequestBody {
        @Getter
        @Setter
        @JsonProperty("event")
        private _Event event;

        public TwitterSendMessageRequestBody(String recipientId, String text) {
            this.event = new _Event(recipientId, text);
        }

        class _Event {
            @Getter
            @Setter
            private String type = "message_create";

            @Getter
            @Setter
            @JsonProperty("message_create")
            private _MessageCreate messageCreate;

            public _Event(String recipientId, String text) {
                this.messageCreate = new _MessageCreate(recipientId, text);
            }

            class _MessageCreate {
                @Getter
                @Setter
                @JsonProperty("target")
                private _Target target;

                @Getter
                @Setter
                @JsonProperty("message_data")
                private _MessageData messageData;

                public _MessageCreate(String recipientId, String text) {
                    this.target = new _Target(recipientId);
                    this.messageData = new _MessageData(text);
                }

                @AllArgsConstructor
                class _Target {
                    @Getter
                    @Setter
                    @JsonProperty("recipient_id")
                    private String recipientId;
                }

                @AllArgsConstructor
                class _MessageData {
                    @Getter
                    @Setter
                    private String text;
                }
            }
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    private static class TwitterSendMessageResponseDto {
        @Getter
        @Setter
        @JsonProperty("event")
        private _Event event;

        @NoArgsConstructor
        @AllArgsConstructor
        class _Event {
            @Getter
            @Setter
            private String id;
        }

        public String getMessageId() {
            if (event == null)
                return null;
            return event.getId();
        }
    }
}
