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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.config.DBHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest{

    private ExpenseManager expenseManager;
    private String test_account_no;

    @Before
    public void construct() {
        DBHandler.createDBHandlerInstance(ApplicationProvider.getApplicationContext());
        expenseManager = new PersistentExpenseManager();
        test_account_no = "12345";
    }

    @Test
    public void testAddAccount() {

        if(expenseManager.getAccountNumbersList().contains(test_account_no)) {
            fail("Account with number exists.");
        }

        try {
            expenseManager.addAccount(test_account_no, "BOC", "Holder A", 1000);
        }catch (Exception e) {
            fail("Account with number exists.");
        }
        assertTrue("Added account to the database", expenseManager.getAccountNumbersList().contains(test_account_no));
    }

    @Test
    public void testRetrieveAccounts() {
        assertNotNull("Retrieve a List<String> of account numbers", expenseManager.getAccountNumbersList());
    }

    @Test
    public void testUpdateBalance() {

        int count = expenseManager.getTransactionLogs().size();

        try {
            expenseManager.updateAccountBalance(test_account_no, 5, 6, 2020, ExpenseType.INCOME, "150.34");
        } catch (Exception e){
            fail();
        }

        int new_count = expenseManager.getTransactionLogs().size();
        assertTrue(new_count == count + 1);


    }
}