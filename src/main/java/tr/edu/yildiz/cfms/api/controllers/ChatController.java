package tr.edu.yildiz.cfms.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import tr.edu.yildiz.cfms.api.models.WebSocketClientConversation;
import tr.edu.yildiz.cfms.api.models.WebSocketClientMessage;
import tr.edu.yildiz.cfms.api.models.WebSocketClientMessages;
import tr.edu.yildiz.cfms.core.response_types.WebSocketError;
import tr.edu.yildiz.cfms.core.response_types.WebSocketServerMessage;
import tr.edu.yildiz.cfms.business.abstracts.ConversationService;
import tr.edu.yildiz.cfms.core.enums.WebSocketEvent;
import tr.edu.yildiz.cfms.entities.concretes.hibernate.Conversation;
import tr.edu.yildiz.cfms.entities.concretes.mongodb.MongoDbMessagesItem;

@Controller
@CrossOrigin(origins = "*")
public class ChatController {


    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    private ConversationService conversationService;

    // CSR buradan mesaj gonderecek
    @MessageMapping("/send-message")
    @SendTo("/topic")
    public void sendMessage(@Payload WebSocketClientMessage webSocketClientMessage) {
        String conversationId = webSocketClientMessage.getConversationId();
        var message = webSocketClientMessage.getMessage();

        try {
            conversationService.sendMessage(conversationId, message);
            var serverMessage = new WebSocketServerMessage<>(WebSocketEvent.SENT_MESSAGE, message);
            simpMessagingTemplate.convertAndSend("/topic", serverMessage);
        } catch (Exception e) {
            var error = new WebSocketError("SEND_MESSAGE_ERROR", "Cannot send message!");
            var serverMessage = new WebSocketServerMessage<>(WebSocketEvent.ERROR, error, false);
            simpMessagingTemplate.convertAndSend("/topic", serverMessage);
        }
    }

    @MessageMapping("/send-messages")
    @SendTo("/topic")
    public void sendMessages(@Payload WebSocketClientMessages webSocketClientMessages) {
        String conversationId = webSocketClientMessages.getConversationId();
        var messages = webSocketClientMessages.getMessages();

        try {
            conversationService.sendMessages(conversationId, messages);
            var serverMessage = new WebSocketServerMessage<>(WebSocketEvent.SENT_MESSAGES, messages);
            simpMessagingTemplate.convertAndSend("/topic", serverMessage);
        } catch (Exception e) {
            var error = new WebSocketError("SEND_MESSAGES_ERROR", "Cannot send messages!");
            var serverMessage = new WebSocketServerMessage<>(WebSocketEvent.ERROR, error, false);
            simpMessagingTemplate.convertAndSend("/topic", serverMessage);
        }

    }

    // Musteriden ilk mesaj geldiginde webhook burayi cagiracak
    @MessageMapping("/create-conversation")
    @SendTo("/topic")
    public void createConversation(@Payload WebSocketClientConversation webSocketClientConversation) {
        sessionRegistry.getAllPrincipals();
        Conversation conversation = webSocketClientConversation.getConversation();
        MongoDbMessagesItem message = webSocketClientConversation.getMessage();

        try {
            conversationService.createConversation(conversation, message);


            var serverMessage = new WebSocketServerMessage<>(WebSocketEvent.NEW_CONVERSATION, conversation);


            simpMessagingTemplate.convertAndSend("/topic", serverMessage);
        } catch (Exception e) {
            var error = new WebSocketError("CREATE_CONVERSATION_ERROR", "Cannot create conversation!");
            var serverMessage = new WebSocketServerMessage<>(WebSocketEvent.NEW_CONVERSATION, error, false);
            simpMessagingTemplate.convertAndSend("/topic", serverMessage);
        }
    }
}
