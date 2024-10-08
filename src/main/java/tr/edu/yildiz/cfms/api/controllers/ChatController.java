package tr.edu.yildiz.cfms.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import tr.edu.yildiz.cfms.api.models.*;
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
            var serverMessage = new WebSocketServerMessage<>(WebSocketEvent.SENT_MESSAGE, message, conversationId);
            simpMessagingTemplate.convertAndSend("/topic", serverMessage);
        } catch (Exception e) {
            var error = new WebSocketError("SEND_MESSAGE_ERROR", "Cannot send message!");
            var serverMessage = new WebSocketServerMessage<>(WebSocketEvent.ERROR, error, false, conversationId);
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
            var serverMessage = new WebSocketServerMessage<>(WebSocketEvent.SENT_MESSAGES, messages, conversationId);
            simpMessagingTemplate.convertAndSend("/topic", serverMessage);
        } catch (Exception e) {
            var error = new WebSocketError("SEND_MESSAGES_ERROR", "Cannot send messages!");
            var serverMessage = new WebSocketServerMessage<>(WebSocketEvent.ERROR, error, false, conversationId);
            simpMessagingTemplate.convertAndSend("/topic", serverMessage);
        }
    }

    // Musteriden ilk mesaj geldiginde webhook burayi cagiracak
    @MessageMapping("/create-conversation")
    @SendTo("/topic")
    public void createConversation(@Payload WebSocketClientConversation webSocketClientConversation) {
        Conversation conversation = webSocketClientConversation.getConversation();
        MongoDbMessagesItem message = webSocketClientConversation.getMessage();

        try {
            String assignedCsr = conversationService.createConversation(conversation, message);
            conversation.setAssignedTo(assignedCsr);
            ConversationDetail conversationDetail = new ConversationDetail(conversation, null);
            var serverMessage = new WebSocketServerMessage<>(WebSocketEvent.NEW_CONVERSATION, conversationDetail, conversation.getId());
            simpMessagingTemplate.convertAndSend("/topic", serverMessage);
        } catch (Exception e) {
            var error = new WebSocketError("CREATE_CONVERSATION_ERROR", "Cannot create conversation!");
            var serverMessage = new WebSocketServerMessage<>(WebSocketEvent.NEW_CONVERSATION, error, false, conversation.getId());
            simpMessagingTemplate.convertAndSend("/topic", serverMessage);
        }
    }

    @MessageMapping("/end-conversation")
    @SendTo("/topic")
    public void endConversation(@Payload WebSocketConversation webSocketConversation){
        String conversationId = webSocketConversation.getConversationId();

        try {
            conversationService.endConversation(conversationId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
