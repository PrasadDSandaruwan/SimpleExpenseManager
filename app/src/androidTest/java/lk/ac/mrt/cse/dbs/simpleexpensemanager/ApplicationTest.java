/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.config.SQLiteHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest  {
    private static ExpenseManager expenseManager;
    private static Context context;

    private final String ACCOUNT_NO = "test_ad_001";
    private final String BANK_NAME = "test bank";
    private final String BANK_HOLDER = "T.H.P.D.Sandaruwan";
    private final Double BALANCE = 1000d;

    private final String TRANSACTION_ACCOUNT_NO = "test_tl_001";
    private final String TRANSACTION_BANK_NAME = "test bank";
    private final String TRANSACTION_BANK_HOLDER = "T.H.P.D.Sandaruwan";
    private final Double TRANSACTION_BALANCE = 1000d;
    private final Integer DAY = 11;
    private final Integer MONTH =5;
    private final Integer YEAR = 2022;
    private final String AMOUNT= "100";
    private final ExpenseType TYPE = ExpenseType.EXPENSE;


    @BeforeClass
    public static void  setUPTestEnvironment(){
        context = ApplicationProvider.getApplicationContext();
        SQLiteHelper.createSQLiteHelper(context);
        expenseManager = new PersistentExpenseManager();
    }

    @Test
    public void checkAccountSuccessfullyAddOrNot(){

        if (expenseManager.getAccountNumbersList().contains(ACCOUNT_NO))
            fail("Account Already exists.");
        try {
            expenseManager.addAccount(ACCOUNT_NO,BANK_NAME,BANK_HOLDER,BALANCE);
        }catch (Exception e){
            fail("Account Adding failed.");
        }

        assertTrue("Account Successfully Added.",expenseManager.getAccountNumbersList().contains(ACCOUNT_NO));

    }

    @Test
    public void checkTransactionSuccessfullyPerformOrNot()  {
        int count = expenseManager.getTransactionLogs().size();

        if (!expenseManager.getAccountNumbersList().contains(TRANSACTION_ACCOUNT_NO)) {
            try {
                expenseManager.addAccount(TRANSACTION_ACCOUNT_NO, TRANSACTION_BANK_NAME, TRANSACTION_BANK_HOLDER, TRANSACTION_BALANCE);
            } catch (Exception e) {
                fail("Account Adding failed.");
            }
        }

        try {
            expenseManager.updateAccountBalance(TRANSACTION_ACCOUNT_NO,DAY,MONTH,YEAR, TYPE,AMOUNT);
        } catch (InvalidAccountException e) {
            fail("Account balance update failed.");
        }

        int new_count = expenseManager.getTransactionLogs().size();

        if(count <=9)
            assertTrue("Transaction Log update successfully.", count+1 == new_count ) ;
        else
            assertTrue("Transaction Log update successfully.", count == new_count );


    }




}