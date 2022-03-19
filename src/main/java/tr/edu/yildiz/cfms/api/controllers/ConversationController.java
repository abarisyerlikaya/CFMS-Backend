package tr.edu.yildiz.cfms.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.edu.yildiz.cfms.api.models.ConversationDetail;
import tr.edu.yildiz.cfms.api.models.GetConversationsRequest;
import tr.edu.yildiz.cfms.api.models.GetMessagesRequest;
import tr.edu.yildiz.cfms.api.models.Message;
import tr.edu.yildiz.cfms.business.abstracts.ConversationService;
import tr.edu.yildiz.cfms.entities.concretes.Conversation;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {
    private ConversationService conversationService;

    @Autowired
    public ConversationController(ConversationService conversationService) {
        super();
        this.conversationService = conversationService;
    }

    @GetMapping("/")
    public List<Conversation> getList(GetConversationsRequest request) {
        conversationService.getList(request);
        return null;
    }

    @GetMapping("/{conversationId}")
    public ConversationDetail getDetail(@PathVariable String conversationId) {
        conversationService.getDetail(conversationId);
        return null;
    }

    @GetMapping("/{conversationId}/messages")
    public List<Message> getMessages(@PathVariable String conversationId, GetMessagesRequest request) {
        request.conversationId = conversationId;
        conversationService.getMessages(request);
        return null;
    }
}
