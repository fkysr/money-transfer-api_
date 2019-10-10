package logic;

import dao.BankAccountDAO;
import db.DBUtils;
import db.DataSource;
import model.BankAccount;
import model.Transfer;

import java.sql.Connection;

public class TransferLogic {
    private static TransferLogic instance=new TransferLogic();
    public static TransferLogic getInstance(){
        return instance;
    }
    public int transferMoney(Transfer transfer) {
        long fromAccountId= transfer.getFromAccountId();
        long toAccountId=transfer.getToAccountId();

        Connection con=null;
        try{
            con = DataSource.getConnection();


            //avoid deadlock here
            BankAccount fromBankAccount = null;
            BankAccount toBankAccount=null;
            if(fromAccountId<toAccountId){
                fromBankAccount= BankAccountDAO.getInstance().getBankAccountById_ForUpdate(con,fromAccountId);
                toBankAccount= BankAccountDAO.getInstance().getBankAccountById_ForUpdate(con,toAccountId);
            }else{
                toBankAccount= BankAccountDAO.getInstance().getBankAccountById_ForUpdate(con,toAccountId);
                fromBankAccount= BankAccountDAO.getInstance().getBankAccountById_ForUpdate(con,fromAccountId);
            }

            if(fromBankAccount.getBalance().compareTo(transfer.getAmount())<0 )
                return 100;//not sufficient amount
            fromBankAccount.setBalance(fromBankAccount.getBalance().subtract(transfer.getAmount()));
            toBankAccount.setBalance(toBankAccount.getBalance().add(transfer.getAmount()));

            BankAccountDAO.getInstance().updateBankAccount(con,fromBankAccount);
            BankAccountDAO.getInstance().updateBankAccount(con,toBankAccount);
            con.commit();


        } catch (Exception ex) {
            DBUtils.rollbackTransaction(con);
            return -1;
        } finally {
            DBUtils.closeConnection(con);
        }
        return 1;
    }
}
