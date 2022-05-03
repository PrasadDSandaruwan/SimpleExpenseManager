package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLiteAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLiteTransactionDAO;

public class PersistentExpenseManager extends ExpenseManager {
    @Override
    public void setup() throws ExpenseManagerException {
        setAccountsDAO(new SQLiteAccountDAO());
        setTransactionsDAO(new SQLiteTransactionDAO());
    }
}
