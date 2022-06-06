package tr.edu.yildiz.cfms.entities.concretes.optaplanner;

public class Csr {
    private String id;
    private int numberOfConversations;
    private int avgConversationLength;

    public Csr() {
    }

    public Csr(String id, int numberOfConversations, int avgConversationLength) {
        this.id = id;
        this.numberOfConversations = numberOfConversations;
        this.avgConversationLength = avgConversationLength;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumberOfConversations() {
        return numberOfConversations;
    }

    public void setNumberOfConversations(int numberOfConversations) {
        this.numberOfConversations = numberOfConversations;
    }

    public int getAvgConversationLength() {
        return avgConversationLength;
    }

    public void setAvgConversationLength(int avgConversationLength) {
        this.avgConversationLength = avgConversationLength;
    }
}
