package Service;

import java.util.List;

import DAO.AccountDAO;
import Model.Account;


public class AccountService {
    public AccountDAO aDao;

    public AccountService(){
        this.aDao = new AccountDAO();
    }

    public Account registerAccount(Account account) {
        if(account.getUsername().isEmpty() || account.getUsername().isBlank() || account.getPassword().length() < 4){
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
            
            if (account.getUsername().equals(a.getUsername())){
                if(account.getPassword().equals(a.getPassword())){
                    return a;
                }
            }
        }
        return null;
    }

    public Account getAccountById(int account_id) {
        
        return aDao.getAccountById(account_id);
    }

}
