package logic;

import dao.BankAccountDAO;
import model.BankAccount;
import model.Currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class BankAccountLogic {

    private AtomicLong incrementalCount;

    private  static BankAccountLogic instance= new BankAccountLogic();
    public static BankAccountLogic getInstance(){
        return instance;
    }
    private BankAccountLogic(){
        incrementalCount = new AtomicLong(0);
    }

    public BankAccount createBankAccount(String accountOwnerName, BigDecimal initialBalance, Currency currency){
        long generatedAccountId= incrementalCount.addAndGet(1);
        BankAccount newBankBankAccount = new BankAccount(generatedAccountId,accountOwnerName,initialBalance, currency);
        return BankAccountDAO.getInstance().createBankAccount(newBankBankAccount);
    }

    public BankAccount getBankAccountById(long id){
        return BankAccountDAO.getInstance().getBankAccountById(id);
    }

    public List<BankAccount> getAllBankAccounts(){
        return BankAccountDAO.getInstance().getAllBankAccounts();
    }

}
