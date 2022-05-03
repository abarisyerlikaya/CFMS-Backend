package tr.edu.yildiz.cfms.business.concretes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.edu.yildiz.cfms.api.models.*;
import tr.edu.yildiz.cfms.business.repository.ConversationRepository;
import tr.edu.yildiz.cfms.business.abstracts.ConversationService;
import tr.edu.yildiz.cfms.core.enums.Platform;
import tr.edu.yildiz.cfms.entities.concretes.Conversation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConversationManager implements ConversationService {
    @Autowired
    private ConversationRepository conversationRepository;

    @Override
    public List<Conversation> getList(GetConversationsRequest request) {
        List<Conversation> results = new ArrayList<>();
        results.addAll(getListFromFacebook(request));
        results.addAll(getListFromWhatsapp(request));
        results.addAll(getListFromInstagram(request));
        results.addAll(getListFromTelegram(request));
        results.addAll(getListFromTwitter(request));
        results.addAll(getListFromLinkedIn(request));
        results.add(new Conversation("MyId", Platform.FACEBOOK, "Ahmet", "", LocalDateTime.now()));

        // Bütün conversationları getiren sorgu
        results = conversationRepository.findAll();

        // Tabloya yeni bir kayıt ekleme
        //Message message = Message.builder().message("Barış").conversationId("2").platform(Platform.FACEBOOK).date(LocalDateTime.now()).build();
        //sendMessage(message);
        return results;
    }

    @Override
    public ConversationDetail getConversationDetail(GetConversationDetailRequest request) {
        return null;
    }

    @Override
    public Conversation sendMessage(Message request) {
        final String message = request.getMessage();
        final String conversationId = request.getConversationId();
        final Platform platform = request.getPlatform();
        final LocalDateTime dateTime = request.getDate();

        conversationRepository.save(new Conversation(conversationId, platform, message, "", dateTime));
        return null;
    }

    private List<Conversation> getListFromFacebook(GetConversationsRequest request) {
        return new ArrayList<>();
    }

    private List<Conversation> getListFromWhatsapp(GetConversationsRequest request) {
        return new ArrayList<>();
    }

    private List<Conversation> getListFromInstagram(GetConversationsRequest request) {
        return new ArrayList<>();
    }

    private List<Conversation> getListFromTelegram(GetConversationsRequest request) {
        return new ArrayList<>();
    }

    private List<Conversation> getListFromTwitter(GetConversationsRequest request) {
        return new ArrayList<>();
    }

    private List<Conversation> getListFromLinkedIn(GetConversationsRequest request) {
        return new ArrayList<>();
    }
}
