package tr.edu.yildiz.cfms.business.concretes;

import org.springframework.stereotype.Service;
import tr.edu.yildiz.cfms.api.models.*;
import tr.edu.yildiz.cfms.business.abstracts.ConversationService;
import tr.edu.yildiz.cfms.core.enums.Platform;
import tr.edu.yildiz.cfms.entities.concretes.Conversation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConversationManager implements ConversationService {
    @Override
    public List<Conversation> getList(GetConversationsRequest request) {
        List<Conversation> results = new ArrayList<>();
        results.addAll(getListFromFacebook(request));
        results.addAll(getListFromWhatsapp(request));
        results.addAll(getListFromInstagram(request));
        results.addAll(getListFromTelegram(request));
        results.addAll(getListFromTwitter(request));
        results.addAll(getListFromLinkedIn(request));
        results.add(new Conversation("MyId", Platform.FACEBOOK, "Ahmet", LocalDateTime.now(), ""));
        return results;
    }

    @Override
    public List<Message> getMessages(GetMessagesRequest request) {
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
