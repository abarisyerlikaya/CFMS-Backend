package tr.edu.yildiz.cfms.api.controllers;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tr.edu.yildiz.cfms.api.dtos.webhooks.facebook.FacebookWebhookDto;
import tr.edu.yildiz.cfms.api.dtos.webhooks.instagram.InstagramConversationDto;
import tr.edu.yildiz.cfms.api.dtos.webhooks.telegram.TelegramWebhookDto;
import tr.edu.yildiz.cfms.api.dtos.webhooks.twitter.TwitterWebhookDto;
import tr.edu.yildiz.cfms.business.abstracts.WebhookService;
import tr.edu.yildiz.cfms.core.response_types.Response;
import tr.edu.yildiz.cfms.core.response_types.SuccessResponse;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/webhooks")
public class WebhooksController {
    @Autowired
    private WebhookService webhookService;


    private static final String VERIFY_TOKEN = "EAAE0ZBjxLFEQBACbTM5ZAzLWYECXWuu4rlSo8QQRzJQv551FQIQtNjxWAEBvShjZCOCd4SIOQGdyDhKjSnGfZArOC1z6rDf4B7OaOG9Ubsg6VGOZAnr8XsODooZCVZCA2I7LvIpPnfApgknn3Rod3RoqJHF7nX30F3ubbAXzA7nur2RuZCVT2KeZBKThBguxAOTYZD";

    @PostMapping("/facebook")
    public Response handleFacebookWebhook(@RequestBody FacebookWebhookDto dto) {
        webhookService.handleFacebookWebhook(dto);
        return new SuccessResponse();
    }

    @GetMapping("/facebook")
    public String verifyFacebookWebhook(@RequestParam("hub.verify_token") String token, @RequestParam("hub.challenge") String challenge) {
        if (token != null && token.equals(VERIFY_TOKEN)) return challenge;
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
        return verifyTwitterWebhook(crcToken);
    }

    @PostMapping("/twitter")
    public Response handleTwitterWebhook(@RequestBody TwitterWebhookDto dto) {
        webhookService.handleTwitterWebhook(dto);
        return new SuccessResponse();
    }
}
