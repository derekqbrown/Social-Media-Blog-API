package Controller;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    MessageService ms;
    AccountService as;

    public SocialMediaController(){
        this.ms = new MessageService();
        this.as = new AccountService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("localhost:8080/register", this::registerHandler);
        app.post("localhost:8080/login", this::loginHandler);
        app.post("localhost:8080/messages", this::postMessageHandler);
        app.get("localhost:8080/messages", this::getMessagesHandler);
        app.get("localhost:8080/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("localhost:8080/delete/{message_id}", this::deleteHandler);
        app.patch("localhost:8080/messages/{message_id}", this::patchMessageHandler);
        app.get("localhost:8080/accounts/{account_id}/messages", this::getMessagesByAccountHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    // private void exampleHandler(Context context) {
    //     ObjectMapper om = new ObjectMapper();

    //     context.json();
    // }

    private void registerHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(ctx.body(), Account.class);
        Account registeredAccount = as.registerAccount(account);
        if(registeredAccount!=null){
            ctx.json(om.writeValueAsString(registeredAccount));
        }else{
            ctx.status(400);
        }
    }

    private void loginHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(ctx.body(), Account.class);
        Account loginAccount = as.login(account);
        if(loginAccount!=null){
            ctx.json(om.writeValueAsString(loginAccount));
        }else{
            ctx.status(401);
        }
    }

    private void postMessageHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Message message = om.readValue(ctx.body(), Message.class);
        Message postedMessage = ms.postMessage(message);
        if(postedMessage!=null){
            ctx.json(om.writeValueAsString(postedMessage));
        }else{
            ctx.status(400);
        }
    }

    private void getMessagesHandler(Context ctx){
        List<Message> messages = ms.getAllMessages();
        ctx.json(messages);
        
    }

    private void getMessageByIdHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Message message = om.readValue(ctx.body(), Message.class);
        Message retrievedMessage = ms.getMessageById(message);
        if(retrievedMessage!=null){
            ctx.json(om.writeValueAsString(retrievedMessage));
        }else{
            ctx.status(200);
        }
    }

    private void deleteHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Message message = om.readValue(ctx.body(), Message.class);
        Message deletedMessage = ms.deleteMessage(message);
        if(deletedMessage!=null){
            ctx.json(om.writeValueAsString(deletedMessage));
        }else{
            ctx.status(200);
        }
    }

    private void patchMessageHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Message message = om.readValue(ctx.body(), Message.class);

        if(message.message_text.length() > 255 || message.message_text.length() < 1){
            ctx.status(400);
            return;
        }

        Message updatedMessage = ms.updateMessage(message);
        if(updatedMessage!=null){
            ctx.json(om.writeValueAsString(updatedMessage));
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }

    private void getMessagesByAccountHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(ctx.body(), Account.class);
        Account userAccount = as.getAccountById(account.account_id);
        List<Message> messagesByAccount = new ArrayList<>();
        if(userAccount!=null){
            messagesByAccount = ms.getMessagesByAccount(account.account_id);
            ctx.json(om.writeValueAsString(messagesByAccount));
        }
        
        ctx.status(200);
        
    }
}