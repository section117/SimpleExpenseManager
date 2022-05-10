package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.config.DBHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class DBAccountDAO implements AccountDAO {

    private final DBHandler dbHandler;

    public DBAccountDAO() {
        dbHandler = DBHandler.getDBHandlerInstance();
    }

    @Override
    public List<String> getAccountNumbersList() {
        SQLiteDatabase db = dbHandler.getReadableDBInstance();
        Cursor result = db.rawQuery("SELECT account_no FROM accounts", null);

        List<String> account_numbers = new LinkedList<>();
        try {
            for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
                account_numbers.add(result.getString(result.getColumnIndexOrThrow("account_no")));
            }
        }catch (Exception e) {
            return account_numbers;
        }
        return account_numbers;
    }

    @Override
    public List<Account> getAccountsList() {
        SQLiteDatabase db = dbHandler.getReadableDBInstance();
        Cursor result = db.rawQuery("SELECT * FROM accounts", null);

        Map<String,Integer> column_indexes = new HashMap<>();
        List<Account> accounts = new LinkedList<>();
        try {
            column_indexes.put("account_no",result.getColumnIndexOrThrow("account_no"));
            column_indexes.put("bank_name",result.getColumnIndexOrThrow("bank_name"));
            column_indexes.put("holder_name",result.getColumnIndexOrThrow("holder_name"));
            column_indexes.put("balance",result.getColumnIndexOrThrow("balance"));
        } catch (Exception e) {
            return accounts;
        }

        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
            accounts.add(new Account(
                    result.getString(column_indexes.get("account_no")),
                    result.getString(column_indexes.get("bank_name")),
                    result.getString(column_indexes.get("holder_name")),
                    result.getDouble(column_indexes.get("balance"))
            ));
        }
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = dbHandler.getReadableDBInstance();
        Cursor result = db.rawQuery("SELECT * FROM accounts WHERE account_no=?", new String[] {accountNo});

        if(result.moveToFirst()) {
            try {
                return new Account(
                        result.getString(result.getColumnIndexOrThrow("account_no")),
                        result.getString(result.getColumnIndexOrThrow("bank_name")),
                        result.getString(result.getColumnIndexOrThrow("holder_name")),
                        result.getDouble(result.getColumnIndexOrThrow("balance"))
                );
            } catch (Exception e){
                throw new InvalidAccountException("No such account with account number "+ accountNo);
            }
        }else {
            throw new InvalidAccountException("No such account with account number "+ accountNo);
        }
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = dbHandler.getWritableDBInstance();

        ContentValues contentValues = new ContentValues();
        contentValues.put("account_no", account.getAccountNo());
        contentValues.put("bank_name", account.getBankName());
        contentValues.put("holder_name", account.getAccountHolderName());
        contentValues.put("balance", account.getBalance());

        db.insert("accounts", null, contentValues);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = dbHandler.getWritableDBInstance();

        int result = db.delete("accounts", "account_no=?", new String[] {accountNo});

        if(result == 0)
            throw new InvalidAccountException("No such account with account number "+ accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db = dbHandler.getWritableDBInstance();

        Account account = getAccount(accountNo);
        double newBalance;
        if(expenseType == ExpenseType.EXPENSE) {
            newBalance = account.getBalance() - amount;
        }else {
            newBalance = account.getBalance() + amount;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put("balance", newBalance);

        int result = db.update("accounts", contentValues, "account_no=?", new String[] {accountNo});

        if(result == 0)
            throw new InvalidAccountException("No such account with account number "+ accountNo);
    }
}
