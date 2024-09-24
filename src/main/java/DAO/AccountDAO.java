package DAO;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {

    public List<Account> getAllAccounts() {
        Connection conn = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();

        try{
            String sql = "SELECT * FROM account;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt("account_id");
                String username = rs.getString("username");
                String password = rs.getString("password");

                Account account = new Account(id, username, password);
                accounts.add(account);
            }


        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    public Account register(Account account) {
        Connection conn = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO account(username, password) VALUES(?,?);";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            
            if (rs.next()){
                account.setAccount_id(rs.getInt(1));
                
                return account;
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
        return null;  

    }

    public Account getAccountById(int account_id) {
        Connection conn = ConnectionUtil.getConnection();

        try{
            String sql = "SELECT * FROM account WHERE account_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int id = rs.getInt("account_id");
                String username = rs.getString("username");
                String password = rs.getString("password");

                Account account = new Account(id, username, password);
                return account;
            }


        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

}
