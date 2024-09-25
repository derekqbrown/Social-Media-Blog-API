package DAO;

import Util.ConnectionUtil;
import Model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public List<Message> getAllMessages(){
        Connection conn = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM message;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id =rs.getInt("message_id");
                int poster = rs.getInt("posted_by");
                String text = rs.getString("message_text");
                long time = rs.getLong("time_posted_epoch");

                Message message = new Message(id, poster, text, time);

                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;

    }

    public Message postMessage(Message message) {
        Connection conn = ConnectionUtil.getConnection();

        try{
            String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(?,?,?);";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3,message.getTime_posted_epoch());
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()){
                message.setMessage_id(rs.getInt(1));

                return message;
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
        return null; 
    }

    public Message getMessageById(int message_id) {
        Connection conn = ConnectionUtil.getConnection();

        try{
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, message_id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int id =rs.getInt("message_id");
                int poster = rs.getInt("posted_by");
                String text = rs.getString("message_text");
                long time = rs.getLong("time_posted_epoch");

                Message message = new Message(id, poster, text, time);
                return message;
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message deleteMessage(int message_id) {
        Connection conn = ConnectionUtil.getConnection();
        Message message = this.getMessageById(message_id);
        try{
            String sql = "DELETE FROM message WHERE message_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, message_id);
            ps.executeUpdate();
            return message;

        }catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Message updateMessage(Message message) {
        Connection conn = ConnectionUtil.getConnection();
        try{
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, message.getMessage_text());
            ps.setInt(2, message.getMessage_id());

            ps.executeUpdate();
            Message oldMessage = this.getMessageById(message.getMessage_id());
            
            message.setPosted_by(oldMessage.getPosted_by());
            message.setTime_posted_epoch(oldMessage.getTime_posted_epoch());
            return message;

        }catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Message> getMessagesByAccount(int account_id) {
        Connection conn = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM message WHERE posted_by = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                int id =rs.getInt("message_id");
                int poster = rs.getInt("posted_by");
                String text = rs.getString("message_text");
                long time = rs.getLong("time_posted_epoch");

                Message message = new Message(id, poster, text, time);

                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

}
