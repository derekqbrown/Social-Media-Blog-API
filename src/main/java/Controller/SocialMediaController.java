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

        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteHandler);
        app.patch("/messages/{message_id}", this::patchMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountHandler);
        return app;
    }

    private void registerHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        String acctStr = ctx.body();
        Account account = om.readValue(acctStr, Account.class);
        Account registeredAccount = as.registerAccount(account);
        if(registeredAccount!=null){
            ctx.json(registeredAccount);
        }else{
            ctx.status(400);
        }
        
    }

    private void loginHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        String acctStr = ctx.body();
        Account account = om.readValue(acctStr, Account.class);
        Account loginAccount = as.login(account);
        if(loginAccount!=null){
            ctx.json(loginAccount);
        }else{
            ctx.status(401);
        }
    }

    private void postMessageHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        String msgStr = ctx.body();
        Message message = om.readValue(msgStr, Message.class);
        Account account = as.getAccountById(message.getPosted_by());
        if(account == null){
            ctx.status(400);
        }

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
        int id = Integer.parseInt(ctx.pathParam("message_id"));

        Message retrievedMessage = ms.getMessageById(id);
        if(retrievedMessage!=null){
            ctx.json(retrievedMessage);
        }else{
            ctx.status(200);
        }
    }

    private void deleteHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = new Message();
        message.setMessage_id(id);
        message = ms.deleteMessage(id);
       
        if(message != null){
            ctx.json(message);
        }else{
            ctx.status(200);
        }
    }

    private void patchMessageHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        String msgStr = ctx.body();
        Message message = om.readValue(msgStr, Message.class);
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        message.setMessage_id(id);

        Message updatedMessage = ms.updateMessage(message);
        if(updatedMessage!=null){
            ctx.json(om.writeValueAsString(updatedMessage));
        }else{
            ctx.status(400);
        }
    }

    private void getMessagesByAccountHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
        int id = Integer.parseInt(ctx.pathParam("account_id"));
        
        Account userAccount = as.getAccountById(id);
        List<Message> messagesByAccount = new ArrayList<>();
        if(userAccount!=null){
            messagesByAccount = ms.getMessagesByAccount(id);
            
        }
        ctx.json(messagesByAccount);
        
    }
}