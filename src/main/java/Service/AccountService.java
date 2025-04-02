package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;
    
    public AccountService() {
        accountDAO = new AccountDAO();
    }
    
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }
    
    public boolean registerAccount(String username, String password) {
        // Validate requirements: username not blank, password at least 4 chars,
        // and username doesn't already exist
        if (username == null || username.trim().isEmpty() || 
            password == null || password.length() < 4 ||
            accountDAO.usernameExists(username)) {
            return false;
        }
        
        Account newAccount = new Account(0, username, password); // ID will be set by the database
        Account insertedAccount = accountDAO.insertAccount(newAccount);
        return insertedAccount != null;
    }
    
    public Account login(String username, String password) {
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            return null;
        }
        
        return accountDAO.getAccountByUsernameAndPassword(username, password);
    }
}