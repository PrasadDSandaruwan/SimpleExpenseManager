package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.config.SQLiteHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.schema.AccountSchema;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.schema.TransactionSchema;

public class SQLiteTransactionDAO implements TransactionDAO {

    private SQLiteHelper db_helper;

    public SQLiteTransactionDAO(){
        db_helper = SQLiteHelper.getDbHelperInstance();
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = db_helper.getWritableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String string_date= dateFormat.format(date);

        ContentValues contentValues = new ContentValues();
        contentValues.put( TransactionSchema.COLUMN_NAME_DATE, string_date );
        contentValues.put( TransactionSchema.COLUMN_NAME_EXPENSE_TYPE , expenseType.toString());
        contentValues.put( TransactionSchema.COLUMN_NAME_AMOUNT , amount);
        contentValues.put( TransactionSchema.COLUMN_ACCOUNT_NO , accountNo);

        db.insert(TransactionSchema.TABLE_NAME, null, contentValues);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        SQLiteDatabase db = db_helper.getReadableDatabase();
        Cursor results = db.rawQuery("SELECT * FROM " + TransactionSchema.TABLE_NAME , null);
        return getTransactionList(results);
    }

    private List<Transaction> getTransactionList(Cursor results) {


        List<Transaction> transactions = new LinkedList<>();


        for (results.moveToFirst(); !results.isAfterLast(); results.moveToNext()) {

            try {
                transactions.add(new Transaction(
                        new SimpleDateFormat("dd/MM/yyyy").parse(results.getString(results.getColumnIndex(TransactionSchema.COLUMN_NAME_DATE))),
                        results.getString(results.getColumnIndex(TransactionSchema.COLUMN_ACCOUNT_NO)),
                        ExpenseType.valueOf(results.getString(results.getColumnIndex(TransactionSchema.COLUMN_NAME_EXPENSE_TYPE))),
                        results.getDouble(results.getColumnIndex(TransactionSchema.COLUMN_NAME_AMOUNT))
                ));
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        SQLiteDatabase db = db_helper.getReadableDatabase();
        Cursor results = db.rawQuery("SELECT * FROM " + TransactionSchema.TABLE_NAME+" LIMIT "+ limit , null);
        return getTransactionList(results);
    }
}
