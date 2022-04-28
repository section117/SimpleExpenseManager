package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.config;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "190416N";
    private static final int DB_VERSION = 1;

    private static DBHandler dbHandlerInstance = null;

    public static void createDBHandlerInstance (Context context) {
        dbHandlerInstance = new DBHandler(context);
    }

    public static DBHandler getDBHandlerInstance() {
        return dbHandlerInstance;
    }


    private DBHandler(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE `accounts` ( `account_no` VARCHAR(255) NOT NULL PRIMARY KEY, `bank_name` VARCHAR(255) NOT NULL , `holder_name` VARCHAR(255) NOT NULL , `balance` DOUBLE NOT NULL);";
        sqLiteDatabase.execSQL(query);
        query = "CREATE TABLE IF NOT EXISTS `transactions` ( `id` INTEGER PRIMARY KEY AUTOINCREMENT , `date` DATE NOT NULL , `expense_type` VARCHAR(25) NOT NULL , `amount` DOUBLE NOT NULL , `account_id` VARCHAR(255) NOT NULL, CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES accounts(account_no));";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS transactions");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS accounts");
        onCreate(sqLiteDatabase);
    }

    public SQLiteDatabase getReadableDBInstance() {
        return this.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDBInstance() {
        return this.getWritableDatabase();
    }
}
