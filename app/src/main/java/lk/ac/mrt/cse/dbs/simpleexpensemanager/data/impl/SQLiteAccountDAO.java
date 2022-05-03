package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.config.SQLiteHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.schema.AccountSchema;

public class SQLiteAccountDAO implements AccountDAO {

    private SQLiteHelper db_helper;

    public SQLiteAccountDAO(){
        db_helper = SQLiteHelper.getDbHelperInstance();
    }

    @Override
    public List<String> getAccountNumbersList() {

        List<String> account_number_list = new LinkedList<>();

        SQLiteDatabase db = db_helper.getReadableDatabase();
        Cursor results = db.rawQuery("SELECT "+ AccountSchema.COLUMN_NAME_ACCOUNT_NO +" FROM "+ AccountSchema.TABLE_NAME , null);

        for (results.moveToFirst(); !results.isAfterLast(); results.moveToNext()) {
            account_number_list.add(results.getString(results.getColumnIndex(AccountSchema.COLUMN_NAME_ACCOUNT_NO)));
        }

        return account_number_list;
    }

    @Override
    public List<Account> getAccountsList() {
        SQLiteDatabase db = db_helper.getReadableDatabase();
        Cursor results = db.rawQuery("SELECT * FROM " + AccountSchema.TABLE_NAME, null);


        List<Account> accounts = new LinkedList<>();
        for (results.moveToFirst(); !results.isAfterLast(); results.moveToNext()) {
            accounts.add(new Account(
                    results.getString( results.getColumnIndex(AccountSchema.COLUMN_NAME_ACCOUNT_NO) ),
                    results.getString(results.getColumnIndex(AccountSchema.COLUMN_NAME_BANK_NAME)),
                    results.getString(results.getColumnIndex(AccountSchema.COLUMN_NAME_ACCOUNT_HOLDER_NAME)),
                    results.getDouble(results.getColumnIndex(AccountSchema.COLUMN_NAME_BALANCE))
            ));
        }
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = db_helper.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM "+ AccountSchema.TABLE_NAME +" WHERE "+ AccountSchema.COLUMN_NAME_ACCOUNT_NO +" = '" + accountNo + "'", null);


        if(result.moveToFirst()) {
            return new Account(
                    result.getString(result.getColumnIndex(AccountSchema.COLUMN_NAME_ACCOUNT_NO)),
                    result.getString(result.getColumnIndex(AccountSchema.COLUMN_NAME_BANK_NAME)),
                    result.getString(result.getColumnIndex(AccountSchema.COLUMN_NAME_ACCOUNT_HOLDER_NAME)),
                    result.getDouble(result.getColumnIndex(AccountSchema.COLUMN_NAME_BALANCE))
            );
        }else {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = db_helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(AccountSchema.COLUMN_NAME_ACCOUNT_NO , account.getAccountNo());
        contentValues.put(AccountSchema.COLUMN_NAME_BANK_NAME , account.getBankName());
        contentValues.put(AccountSchema.COLUMN_NAME_ACCOUNT_HOLDER_NAME , account.getAccountHolderName());
        contentValues.put(AccountSchema.COLUMN_NAME_BALANCE , account.getBalance());
        db.insert(AccountSchema.TABLE_NAME, null, contentValues);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = db_helper.getWritableDatabase();

        int result = db.delete(AccountSchema.TABLE_NAME , AccountSchema.COLUMN_NAME_ACCOUNT_NO +" =? ", new String[] {accountNo});

        if(result == 0){
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db = db_helper.getWritableDatabase();

        Account account = getAccount(accountNo);
        double new_balance;

        if(expenseType == ExpenseType.EXPENSE) {
            new_balance = account.getBalance() - amount;
        }else {
            new_balance = account.getBalance() + amount;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put( AccountSchema.COLUMN_NAME_BALANCE, new_balance);

        int result = db.update(AccountSchema.TABLE_NAME , contentValues, AccountSchema.COLUMN_NAME_ACCOUNT_NO +" =? ", new String[] {accountNo});

        if(result == 0){
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }
}
