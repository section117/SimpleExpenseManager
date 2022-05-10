package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.config.DBHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DBTransactionDAO implements TransactionDAO {

    private final DBHandler dbHandler;

    public DBTransactionDAO() {
        dbHandler = DBHandler.getDBHandlerInstance();
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = dbHandler.getWritableDBInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String stringDate= dateFormat.format(date);

        ContentValues contentValues = new ContentValues();
        contentValues.put("date", stringDate);
        contentValues.put("expense_type", expenseType.toString());
        contentValues.put("amount", amount);
        contentValues.put("account_id", accountNo);

        long id = db.insert("transactions", null, contentValues);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        SQLiteDatabase db = dbHandler.getReadableDBInstance();
        Cursor result = db.rawQuery("SELECT * FROM transactions", null);
        return getTransactionsListFromCursor(result);
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        SQLiteDatabase db = dbHandler.getReadableDBInstance();
        Cursor result = db.rawQuery("SELECT * FROM transactions LIMIT ?", new String[] {Integer.toString(limit)});
        return getTransactionsListFromCursor(result);
    }

    private List<Transaction> getTransactionsListFromCursor(Cursor result) {

        Map<String,Integer> column_indexes = new HashMap<>();
        List<Transaction> transactions = new LinkedList<>();
        try {
            column_indexes.put("id",result.getColumnIndexOrThrow("id"));
            column_indexes.put("date",result.getColumnIndexOrThrow("date"));
            column_indexes.put("expense_type",result.getColumnIndexOrThrow("expense_type"));
            column_indexes.put("amount",result.getColumnIndexOrThrow("amount"));
            column_indexes.put("account_id",result.getColumnIndexOrThrow("account_id"));
        } catch (Exception e) {
            return transactions;
        }

        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
            try {
                transactions.add(new Transaction(
                        new SimpleDateFormat("dd/MM/yyyy").parse(result.getString(column_indexes.get("date"))),
                        result.getString(column_indexes.get("account_id")),
                        ExpenseType.valueOf(result.getString(column_indexes.get("expense_type"))),
                        result.getDouble(column_indexes.get("amount"))
                ));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return transactions;
    }
}
