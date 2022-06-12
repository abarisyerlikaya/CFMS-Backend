package tr.edu.yildiz.cfms.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tr.edu.yildiz.cfms.api.dtos.webhooks.facebook.FacebookWebhookDto;
import tr.edu.yildiz.cfms.api.dtos.webhooks.instagram.InstagramConversationDto;
import tr.edu.yildiz.cfms.api.dtos.webhooks.telegram.TelegramWebhookDto;
import tr.edu.yildiz.cfms.api.dtos.webhooks.twitter.TwitterWebhookDto;
import tr.edu.yildiz.cfms.business.abstracts.WebhookService;
import tr.edu.yildiz.cfms.core.response_types.Response;
import tr.edu.yildiz.cfms.core.response_types.SuccessResponse;

import java.io.IOException;
import java.util.Map;

import static tr.edu.yildiz.cfms.core.utils.Constants.FB_VERIFY_TOKEN;


@RestController
@RequestMapping("/api/webhooks")
public class WebhooksController {
    @Autowired
    private WebhookService webhookService;

    @PostMapping("/facebook")
    public Response handleFacebookWebhook(@RequestBody FacebookWebhookDto dto) {
        webhookService.handleFacebookWebhook(dto);
        return new SuccessResponse();
    }

    @GetMapping("/facebook")
    public String verifyFacebookWebhook(@RequestParam("hub.verify_token") String token, @RequestParam("hub.challenge") String challenge) {
        if (token != null && token.equals(FB_VERIFY_TOKEN)) return challenge;
        return "Not verified!";
    }

    @PostMapping("/telegram")
    public Response handleTelegramWebhook(@RequestBody TelegramWebhookDto dto) {
        webhookService.handleTelegramWebhook(dto);
        return new SuccessResponse();
    }

    @GetMapping("/telegram")
    public Response verifyTelegramWebhook() {
        return new SuccessResponse();
    }

    @PostMapping("/newinstagram")
    public Response newInstagramConversation(@RequestBody InstagramConversationDto dto) throws IOException {
        webhookService.handleInstagramConversation(dto, Boolean.TRUE);
        return new SuccessResponse();
    }

    @PostMapping("/updateinstagram")
    public Response updateInstagramConversation(@RequestBody InstagramConversationDto dto) throws IOException {
        webhookService.handleInstagramConversation(dto, Boolean.FALSE);
        return new SuccessResponse();
    }

    @GetMapping("/twitter")
    public Map verifyTwitterWebhook(@RequestParam(name = "crc_token") String crcToken) throws Exception {
        return webhookService.verifyTwitterWebhook(crcToken);
    }

    @PostMapping("/twitter")
    public Response handleTwitterWebhook(@RequestBody TwitterWebhookDto dto) {
        webhookService.handleTwitterWebhook(dto);
        return new SuccessResponse();
    }
}
