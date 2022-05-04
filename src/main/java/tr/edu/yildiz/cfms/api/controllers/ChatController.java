package tr.edu.yildiz.cfms.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import tr.edu.yildiz.cfms.entities.concretes.mongodb.MongoDbMessagesItem;

@Controller
@CrossOrigin(origins = "*")
public class ChatController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat")
    @SendTo("/topic")
    public void chatEndpoint(@Payload MongoDbMessagesItem mongoDbMessagesItem) {
        simpMessagingTemplate.convertAndSend("/topic", mongoDbMessagesItem);
    }
}
