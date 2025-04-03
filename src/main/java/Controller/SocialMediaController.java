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
        
        app.post("/register", this::registerUserHandler);
        
        app.post("/login", this::loginUserHandler);
        
        app.post("/messages", this::createMessageHandler);
        
        app.get("/messages", this::getAllMessagesHandler);

        app.get("/messages/{message_id}", this::getMessageByIdHandler);

        app.delete("/messages/{message_id}", this::deleteMessageHandler);

        app.patch("/messages/{message_id}", this::updateMessageHandler);

        app.get("/accounts/{account_id}/messages", this::getMessagesByUserHandler);
        
        return app;
    }
    
    private void registerUserHandler(Context ctx) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Account account = mapper.readValue(ctx.body(), Account.class);
            
            boolean successful = accountService.registerAccount(account.getUsername(), account.getPassword());
            
            if (successful) {
                // Get the newly registered account to return (including account_id)
                Account registeredAccount = accountService.login(account.getUsername(), account.getPassword());
                ctx.status(200);
                ctx.json(registeredAccount);
            } else {
                ctx.status(400);
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
                ctx.status(200);
                ctx.json(loggedInAccount);
            } else {
                ctx.status(401); // Unauthorized
            }
        } catch (Exception e) {
            ctx.status(400);
        }
    }
    
    private void createMessageHandler(Context ctx) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Message message = mapper.readValue(ctx.body(), Message.class);
            
            Message createdMessage = messageService.createMessage(message);
            
            if (createdMessage != null) {
                ctx.status(200);
                ctx.json(createdMessage);
            } else {
                ctx.status(400);
            }
        } catch (Exception e) {
            ctx.status(400);
        }
    }
    
    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }
    
    private void getMessageByIdHandler(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = messageService.getMessageById(messageId);
            
            if (message != null) {
                ctx.json(message);
            } else {
                // When message doesn't exist, return empty body with 200 status
                ctx.json("");
            }
        } catch (NumberFormatException e) {
            ctx.status(400);
        }
    }
    
    private void deleteMessageHandler(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message deletedMessage = messageService.deleteMessage(messageId);
            
            if (deletedMessage != null) {
                ctx.json(deletedMessage);
            } else {
                // For DELETE, when resource doesn't exist, still return 200 with empty body
                ctx.json("");
            }
        } catch (NumberFormatException e) {
            ctx.status(400);
        }
    }
    
    private void updateMessageHandler(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            
            // For PATCH, we only need the message_text from the request body
            ObjectMapper mapper = new ObjectMapper();
            Message updates = mapper.readValue(ctx.body(), Message.class);
            String newMessageText = updates.getMessage_text();
            
            Message updatedMessage = messageService.updateMessageText(messageId, newMessageText);
            
            if (updatedMessage != null) {
                ctx.status(200);
                ctx.json(updatedMessage);
            } else {
                ctx.status(400);
            }
        } catch (Exception e) {
            ctx.status(400);
        }
    }
    
    private void getMessagesByUserHandler(Context ctx) {
        try {
            int accountId = Integer.parseInt(ctx.pathParam("account_id"));
            List<Message> messages = messageService.getMessagesByUser(accountId);
            ctx.json(messages);
        } catch (NumberFormatException e) {
            ctx.status(400);
        }
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }


}