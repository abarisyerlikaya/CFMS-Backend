package tr.edu.yildiz.cfms.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tr.edu.yildiz.cfms.api.dtos.webhooks.facebook.FacebookWebhookDto;
import tr.edu.yildiz.cfms.business.abstracts.WebhookService;
import tr.edu.yildiz.cfms.core.response_types.Response;
import tr.edu.yildiz.cfms.core.response_types.SuccessResponse;


@RestController
@RequestMapping("/api/webhooks")
public class WebhooksController {
    @Autowired
    private WebhookService webhookService;
    private static final String VERIFY_TOKEN = "EAAE0ZBjxLFEQBACbTM5ZAzLWYECXWuu4rlSo8QQRzJQv551FQIQtNjxWAEBvShjZCOCd4SIOQGdyDhKjSnGfZArOC1z6rDf4B7OaOG9Ubsg6VGOZAnr8XsODooZCVZCA2I7LvIpPnfApgknn3Rod3RoqJHF7nX30F3ubbAXzA7nur2RuZCVT2KeZBKThBguxAOTYZD";

    @PostMapping("/facebook")
    public Response handleFacebookWebhook(@RequestBody FacebookWebhookDto dto) {
        webhookService.handleWebhook(dto);
        return new SuccessResponse();
    }

    @GetMapping("/facebook")
    public String verifyFacebookWebhook(@RequestParam("hub.verify_token") String token, @RequestParam("hub.challenge") String challenge) {
        if (token != null && token.equals(VERIFY_TOKEN)) return challenge;
        return "Not verified!";
    }
}
