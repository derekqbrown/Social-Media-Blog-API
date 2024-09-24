package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;


public class AccountService {
    public AccountDAO aDao;
    public MessageDAO mDao;

    public AccountService(){
        this.aDao = new AccountDAO();
        this.mDao = new MessageDAO();
    }

    public Account registerAccount(Account account) {
        if(account.getUsername().isEmpty() || account.getUsername().isBlank()){
            return null;
        }
        List <Account> accounts = aDao.getAllAccounts();
        
        for (Account a : accounts){
            if (account.getUsername() == a.getUsername()){
                
                return null;
            }
            
        }
        return aDao.register(account);
    }

    public Account login(Account account) {
        List <Account> accounts = aDao.getAllAccounts();
        for (Account a : accounts){
            if (account.getUsername() == a.getUsername()){
                if(account.getPassword() == a.getPassword()){
                    return account;
                }
            }
        }
        return null;
    }

    public Account getAccountById(int account_id) {
        List <Account> accounts = aDao.getAllAccounts();
        for (Account a : accounts){
            if (account_id == a.getAccount_id()){
                return null;
            }
        }
        return aDao.getAccountById(account_id);
    }

}
