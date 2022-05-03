package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.config;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.Nullable;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.schema.AccountSchema;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.schema.TransactionSchema;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME= "190558B.db";
    private static final Integer  DB_VERSION = 1;

    private static SQLiteHelper db_helper;

    public SQLiteHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static SQLiteHelper getDbHelperInstance(){
        if (db_helper ==null)
            db_helper = new SQLiteHelper(null);
        return db_helper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE  IF NOT EXISTS " +
                 AccountSchema.TABLE_NAME +" ( " +
                 AccountSchema.COLUMN_NAME_ACCOUNT_NO +" VARCHAR(255) PRIMARY KEY NOT NULL , " +
                 AccountSchema.COLUMN_NAME_ACCOUNT_HOLDER_NAME + " VARCHAR(255) NOT NULL , " +
                 AccountSchema.COLUMN_NAME_BALANCE + " DOUBLE NOT NULL, " +
                 AccountSchema.COLUMN_NAME_BANK_NAME+ "  VARCHAR(255) ) ;";
        sqLiteDatabase.execSQL(query);

        query = "CREATE TABLE IF NOT EXISTS "+ TransactionSchema.TABLE_NAME +" ( " +
                TransactionSchema.COLUMN_NAME_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                TransactionSchema.COLUMN_NAME_DATE + " DATE NOT NULL , " +
                TransactionSchema.COLUMN_NAME_EXPENSE_TYPE + "  VARCHAR(25) NOT NULL , " +
                TransactionSchema.COLUMN_NAME_AMOUNT + "  DOUBLE NOT NULL , " +
                TransactionSchema.COLUMN_ACCOUNT_NO + "  INTEGER NOT NULL, " +
                " CONSTRAINT fk_account FOREIGN KEY ( " +
                 TransactionSchema.COLUMN_ACCOUNT_NO +
                " ) REFERENCES   accounts( " +
                AccountSchema.COLUMN_NAME_ACCOUNT_NO +
                " ));";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TransactionSchema.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +AccountSchema.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
