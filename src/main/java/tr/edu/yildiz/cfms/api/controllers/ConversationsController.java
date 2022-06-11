package tr.edu.yildiz.cfms.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.*;
import tr.edu.yildiz.cfms.api.models.ConversationDetail;
import tr.edu.yildiz.cfms.api.models.GetConversationsRequest;
import tr.edu.yildiz.cfms.api.models.GetConversationDetailRequest;
import tr.edu.yildiz.cfms.api.models.Message;
import tr.edu.yildiz.cfms.business.abstracts.ConversationService;
import tr.edu.yildiz.cfms.core.response_types.Response;
import tr.edu.yildiz.cfms.core.response_types.SuccessDataResponse;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationsController {
    @Autowired
    private ConversationService conversationService;


    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    public ConversationsController(ConversationService conversationService) {
        super();
        this.conversationService = conversationService;
    }

    @GetMapping("")
    public Response getList(GetConversationsRequest request, @RequestHeader("Authorization") String accessToken) {
        var principals = sessionRegistry.getAllPrincipals();
        var conversations = conversationService.getListWithMessages(request, accessToken);
        return new SuccessDataResponse<>(conversations);
    }

    @GetMapping("/{conversationId}")
    public Response getConversationDetail(@PathVariable String conversationId, GetConversationDetailRequest request) {
        request.setConversationId(conversationId);
        var conversation = conversationService.getConversationDetail(request);
        if (conversation == null)
            return new Response(false, HttpStatus.NOT_FOUND, "Conversation not found!");
        return new SuccessDataResponse<>(conversation);
    }


}
