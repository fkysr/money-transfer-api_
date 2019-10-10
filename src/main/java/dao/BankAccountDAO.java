package dao;

import model.BankAccount;
import db.*;
import model.Currency;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankAccountDAO {
    private static final String BANK_ACCOUNT_TABLE_NAME = "bank_account";
    private static final String BANK_ACCOUNT_ID_ROW = "account_id";
    private static final String BANK_ACCOUNT_OWNER_NAME_ROW = "account_owner_name";
    private static final String BANK_ACCOUNT_BALANCE_ROW = "balance";
    private static final String BANK_ACCOUNT_CURRENCY_ID_ROW = "currency_id";

    private static BankAccountDAO instance= new BankAccountDAO();
    public static BankAccountDAO getInstance(){
        return instance;
    }
    public List<BankAccount> getAllBankAccounts() {
        List<BankAccount> bankAccounts = new ArrayList<BankAccount>();
        String sqlString = "select * from " + BANK_ACCOUNT_TABLE_NAME;
        Connection con = null;
        try {
            con = DataSource.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sqlString);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                BankAccount bankAccount = new BankAccount(resultSet.getLong(BANK_ACCOUNT_ID_ROW)
                        , resultSet.getString(BANK_ACCOUNT_OWNER_NAME_ROW)
                        , resultSet.getBigDecimal(BANK_ACCOUNT_BALANCE_ROW)
                        , Currency.fromInteger(resultSet.getInt(BANK_ACCOUNT_CURRENCY_ID_ROW)));
                bankAccounts.add(bankAccount);
            }
            con.commit();
        } catch (Exception ex) {
            DBUtils.rollbackTransaction(con);
        } finally {
            DBUtils.closeConnection(con);
        }
        return bankAccounts;
    }

    public BankAccount getBankAccountById(long id) {
        BankAccount bankAccount= null;
        String sqlString =
                "select * from " + BANK_ACCOUNT_TABLE_NAME + " ba " +
                        "where ba." + BANK_ACCOUNT_ID_ROW + " = ?";
        Connection con=null;
        try {
            con = DataSource.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sqlString);
            preparedStatement.setLong(1, id);
            ResultSet resultSet= preparedStatement.executeQuery();
            int count=0;
            while (resultSet.next()) {
                count++;
                if(count>1)
                    throw new Exception("multiple records found");
                long account_id= resultSet.getInt(BANK_ACCOUNT_ID_ROW);
                bankAccount = new BankAccount(account_id
                        , resultSet.getString(BANK_ACCOUNT_OWNER_NAME_ROW)
                        , resultSet.getBigDecimal(BANK_ACCOUNT_BALANCE_ROW)
                        , Currency.fromInteger(resultSet.getInt(BANK_ACCOUNT_CURRENCY_ID_ROW)));
            }
            con.commit();
        } catch (Exception ex) {
            DBUtils.rollbackTransaction(con);
        } finally {
            DBUtils.closeConnection(con);
        }
        return bankAccount;
    }

    //row level "write lock"
    public BankAccount getBankAccountById_ForUpdate(Connection con, Long id) throws SQLException {
        BankAccount bankAccount= null;
        String sqlString =
                "select * from " + BANK_ACCOUNT_TABLE_NAME + " ba " +
                        "where ba." + BANK_ACCOUNT_ID_ROW + " = ? " +
                        "for update";
        PreparedStatement preparedStatement = con.prepareStatement(sqlString);
        preparedStatement.setLong(1,id);
        ResultSet resultSet= preparedStatement.executeQuery();
        int count=0;
        while (resultSet.next()) {
            count++;
            if(count>1)
                throw new SQLException("multiple records found");
            bankAccount = new BankAccount(resultSet.getLong(BANK_ACCOUNT_ID_ROW)
                    , resultSet.getString(BANK_ACCOUNT_OWNER_NAME_ROW)
                    , resultSet.getBigDecimal(BANK_ACCOUNT_BALANCE_ROW)
                    , Currency.fromInteger(resultSet.getInt(BANK_ACCOUNT_CURRENCY_ID_ROW)));
        }
        return bankAccount;
    }

    public int updateBankAccount(Connection con, BankAccount bankAccount) throws SQLException {
        String sqlString =
                "update " + BANK_ACCOUNT_TABLE_NAME +
                        " set " +
                        BANK_ACCOUNT_BALANCE_ROW + " = ? " +
                        "where " + BANK_ACCOUNT_ID_ROW + " = ?";

        PreparedStatement preparedStatement = con.prepareStatement(sqlString);
        preparedStatement.setBigDecimal(1, bankAccount.getBalance());
        preparedStatement.setLong(2, bankAccount.getAccountId());
        return preparedStatement.executeUpdate();
    }

    public BankAccount createBankAccount(BankAccount bankAccount) {
        String sqlString =
                "insert into " + BANK_ACCOUNT_TABLE_NAME +
                        " (" +
                        BANK_ACCOUNT_ID_ROW +", " +
                        BANK_ACCOUNT_OWNER_NAME_ROW +", " +
                        BANK_ACCOUNT_BALANCE_ROW + ", " +
                        BANK_ACCOUNT_CURRENCY_ID_ROW +
                        ") values (?, ?, ?,?)";

        Connection con=null;
        try {
            con = DataSource.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sqlString);
            preparedStatement.setLong(1, bankAccount.getAccountId());
            preparedStatement.setString(2, bankAccount.getAccountOwnerName());
            preparedStatement.setBigDecimal(3, bankAccount.getBalance());
            preparedStatement.setInt(4, bankAccount.getCurrency().ordinal());
            preparedStatement.executeUpdate();
            con.commit();
        } catch (Exception ex) {
            DBUtils.rollbackTransaction(con);
            return null;
        } finally {
            DBUtils.closeConnection(con);
        }
        return bankAccount;
    }
}
