package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    MessageDAO mDao;

    public MessageService(){
        this.mDao = new MessageDAO();
    }

    public List<Message> getAllMessages() {
        return mDao.getAllMessages();
    }

    public Message postMessage(Message message) {
        if(message.getMessage_text().isEmpty() || message.getMessage_text().length() > 255){
            return null;
        }
        return mDao.postMessage(message);
    }

    public Message getMessageById(int id) {
        return mDao.getMessageById(id);
    }

    public Message deleteMessage(int id) {
        return mDao.deleteMessage(id);
    }

    public Message updateMessage(Message message) {
        if(message.getMessage_text().isEmpty() || message.getMessage_text().length() > 255 || message.getMessage_text().length() < 1){
            return null;
        }
        if(mDao.getMessageById(message.getMessage_id()) != null){
            return mDao.updateMessage(message);
        }
        return null;
    }

    public List<Message> getMessagesByAccount(int account_id) {
        return mDao.getMessagesByAccount(account_id);
    }

}
