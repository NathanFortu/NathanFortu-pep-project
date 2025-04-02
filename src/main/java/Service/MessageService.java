package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;
    
    public MessageService() {
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }
    
    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }
    
    public Message createMessage(Message message) {
        // Validate message requirements: text not blank, not over 255 chars, posted_by exists
        if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty() ||
            message.getMessage_text().length() > 255 ||
            accountDAO.getAccountById(message.getPosted_by()) == null) {
            return null;
        }
        
        // If time_posted_epoch is not set, use current time
        if (message.getTime_posted_epoch() == 0) {
            message.setTime_posted_epoch(System.currentTimeMillis());
        }
        
        return messageDAO.insertMessage(message);
    }
    
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }
    
    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }
    
    public Message deleteMessage(int messageId) {
        return messageDAO.deleteMessage(messageId);
    }
    
    public Message updateMessageText(int messageId, String newMessageText) {
        // Validate the update requirements
        if (newMessageText == null || newMessageText.trim().isEmpty() ||
            newMessageText.length() > 255) {
            return null;
        }
        
        // Check if the message exists
        Message existingMessage = messageDAO.getMessageById(messageId);
        if (existingMessage == null) {
            return null;
        }
        
        return messageDAO.updateMessageText(messageId, newMessageText);
    }
    
    public List<Message> getMessagesByUser(int accountId) {
        return messageDAO.getMessagesByUser(accountId);
    }
}