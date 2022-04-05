package tr.edu.yildiz.cfms.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tr.edu.yildiz.cfms.api.models.ConversationDetail;
import tr.edu.yildiz.cfms.api.models.GetConversationsRequest;
import tr.edu.yildiz.cfms.api.models.GetMessagesRequest;
import tr.edu.yildiz.cfms.api.models.Message;
import tr.edu.yildiz.cfms.business.abstracts.ConversationService;
import tr.edu.yildiz.cfms.core.response_types.DataResponse;
import tr.edu.yildiz.cfms.core.response_types.Response;
import tr.edu.yildiz.cfms.core.response_types.SuccessDataResponse;
import tr.edu.yildiz.cfms.entities.concretes.Conversation;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationsController {
    private ConversationService conversationService;

    @Autowired
    public ConversationsController(ConversationService conversationService) {
        super();
        this.conversationService = conversationService;
    }

    @GetMapping("")
    public Response getList(GetConversationsRequest request) {
        var conversations = conversationService.getList(request);
        return new SuccessDataResponse<>(conversations);
    }

    @GetMapping("/{conversationId}")
    public List<Message> getMessages(@PathVariable String conversationId, GetMessagesRequest request) {
        request.conversationId = conversationId;
        conversationService.getMessages(request);
        return null;
    }
}
