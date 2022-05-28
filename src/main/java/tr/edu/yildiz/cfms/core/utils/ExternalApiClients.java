package tr.edu.yildiz.cfms.core.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramDirectShareRequest;
import org.brunocvcunha.instagram4j.requests.InstagramDirectShareRequest.ShareType.*;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUser;
import org.brunocvcunha.instagram4j.requests.payload.StatusResult;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tr.edu.yildiz.cfms.api.dtos.apis.facebook.FacebookApiUserDto;
import tr.edu.yildiz.cfms.api.dtos.webhooks.instagram.InstagramConversationDto;
import tr.edu.yildiz.cfms.business.concretes.ConversationManager;
import tr.edu.yildiz.cfms.core.enums.Platform;
import tr.edu.yildiz.cfms.entities.concretes.hibernate.Conversation;
import tr.edu.yildiz.cfms.entities.concretes.mongodb.MongoDbMessages;
import tr.edu.yildiz.cfms.entities.concretes.mongodb.MongoDbMessagesItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static tr.edu.yildiz.cfms.core.utils.Constants.*;
import static tr.edu.yildiz.cfms.core.utils.Constants.TELEGRAM_TOKEN;

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
        var result =  dto.getResult();
        if (!dto.isOk() || result == null)
            return null;

        return Long.toString(result.getMessageId());
    }

    public static String createInstagramConversation(InstagramConversationDto dto) throws IOException {

//        ConversationManager conversationManager = new ConversationManager();
//
//        Conversation conversation = new Conversation();
//        conversation.setId(dto.getId());
//        conversation.setClientName(dto.getClientName());
//        conversation.setLastMessageDate(dto.getLastMessageDate());
//        conversation.setPlatform(Platform.INSTAGRAM);
//
//
//        MongoDbMessagesItem mongoDbMessagesItem = new MongoDbMessagesItem();
//        List<MongoDbMessagesItem> messages = new ArrayList<>();
//
//        for (var message : dto.getMessages()) {
//            mongoDbMessagesItem.setText(message.getText());
//            mongoDbMessagesItem.setSentDate(message.getDate());
//            mongoDbMessagesItem.setId(message.getId());
//            mongoDbMessagesItem.setSentByClient(message.isClient());
//            messages.add(mongoDbMessagesItem);
//        }
//
//        MongoDbMessages mongoDbMessages = new MongoDbMessages();
//        mongoDbMessages.setId(dto.getId());
//        mongoDbMessages.setMessages(messages);
//
//
//        conversationManager.createConversation(conversation, mongoDbMessagesItem);




        return null;
    }


    public static String sendMessageWithInstagram(Conversation conversation, MongoDbMessagesItem mongoDbMessagesItem) throws IOException {

        Instagram4j instagram = Instagram4j.builder().username("bitirmeytu").password("Bitirme123").build();
        instagram.setup();
        instagram.login();

        ConversationManager conversationManager = new ConversationManager();



        InstagramSearchUsernameResult result = instagram.sendRequest(new InstagramSearchUsernameRequest("anilberdogan"));
        InstagramUser user = result.getUser();

//
//        List<String> receipents = new ArrayList<String>();
//        receipents.add(String.valueOf(user.getPk()));
//
//        String messageText = "Merhaba";
//
//        InstagramDirectShareRequest.ShareType type = InstagramDirectShareRequest.ShareType.MESSAGE;
//
//        instagram.sendRequest(InstagramDirectShareRequest.builder().shareType(type).message("31").recipients(receipents).build());

//        StatusResult hello = instagram.sendRequest(InstagramDirectShareRequest.builder(InstagramDirectShareRequest.ShareType.MESSAGE, receipents).message("Hello").build());
//        try {
//            instagram.sendRequest(InstagramDirectShareRequest.builder(InstagramDirectShareRequest.ShareType.MESSAGE,).message("Hello").build());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

            return null;

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


}
