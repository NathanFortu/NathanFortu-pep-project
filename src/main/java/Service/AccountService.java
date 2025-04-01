package Service;
import java.util.List;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;
    public AccountService(){
        accountDAO = new AccountDAO();
    }
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }
    public List<Account> getAllAccounts() {
        return accountDAO.RetrieveAllAccounts();
    }

     public boolean registerAccount(String username, String password) {
        // Check if username already exists
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            return false; // Invalid input
        }
        
        // Check if username already exists before trying to register
        if (accountDAO.usernameExists(username)) {
            return false; // Username already taken
        }
        
        // Create new account object
        Account newAccount = new Account(username, password);
        
        // Try to register the account
        return accountDAO.registerAccount(newAccount);
    }
    
    /**
     * Authenticate a user login attempt
     * @param username The username to check
     * @param password The password to verify
     * @return The Account object if login successful, null otherwise
     */
    public Account login(String username, String password) {
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            return null; // Invalid input
        }
        
        // Try to find and authenticate the account
        return accountDAO.login(username, password);
    }
    
    /**
     * Check if a username is available
     * @param username The username to check
     * @return true if the username is available, false if it's already taken
     */
    public boolean isUsernameAvailable(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false; // Invalid input
        }
        
        return !accountDAO.usernameExists(username);
    }
}
