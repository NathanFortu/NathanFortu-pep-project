package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    private AccountService accountService;
    private MessageService messageService;
    
    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        
        // User registration and login endpoints
        app.post("/register", this::registerUserHandler);
        app.post("/login", this::loginUserHandler);
        
        // Message endpoints
        app.get("/messages", this::getAllMessagesHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        
        // Account-specific message endpoints
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserHandler);
        
        return app;
    }
    
    private void registerUserHandler(Context ctx) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Account account = mapper.readValue(ctx.body(), Account.class);
            
            Boolean registered = accountService.registerAccount(account.getUsername(), account.getPassword());
            
            if (registered != false) {
                ctx.status(201); // Created
                ctx.json(account);
            } else {
                ctx.status(400); // Bad Request
            }
        } catch (Exception e) {
            ctx.status(400);
        }
    }
    
    private void loginUserHandler(Context ctx) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Account credentials = mapper.readValue(ctx.body(), Account.class);
            
            Account loggedInAccount = accountService.login(credentials.getUsername(), credentials.getPassword());
            
            if (loggedInAccount != null) {
                ctx.status(200); // OK
                ctx.json(loggedInAccount);
            } else {
                ctx.status(401); // Unauthorized
            }
        } catch (Exception e) {
            ctx.status(400);
        }
    }
    
    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }
    
    private void createMessageHandler(Context ctx) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Message message = mapper.readValue(ctx.body(), Message.class);
            
            Message createdMessage = messageService.createMessage(message);
            
            if (createdMessage != null) {
                ctx.status(201); // Created
                ctx.json(createdMessage);
            } else {
                ctx.status(400); // Bad Request
            }
        } catch (Exception e) {
            ctx.status(400);
        }
    }
    
    private void getMessageByIdHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        
        if (message != null) {
            ctx.json(message);
        } else {
            ctx.status(404); // Not Found
        }
    }
    
    private void deleteMessageHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessage(messageId);
        
        if (deletedMessage != null) {
            ctx.json(deletedMessage);
        } else {
            ctx.status(404); // Not Found
        }
    }
    
    private void updateMessageHandler(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            ObjectMapper mapper = new ObjectMapper();
            Message updates = mapper.readValue(ctx.body(), Message.class);
            
            Message updatedMessage = messageService.updateMessage(messageId, updates.getMessage_text());
            
            if (updatedMessage != null) {
                ctx.json(updatedMessage);
            } else {
                ctx.status(400); // Bad Request
            }
        } catch (Exception e) {
            ctx.status(400);
        }
    }
    
    private void getMessagesByUserHandler(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByUser(accountId);
        ctx.json(messages);
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }


}