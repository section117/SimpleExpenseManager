package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DBAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DBTransactionDAO;

public class PersistentExpenseManager extends ExpenseManager{
    @Override
    public void setup() throws ExpenseManagerException {

        setAccountsDAO(new DBAccountDAO());
        setTransactionsDAO(new DBTransactionDAO());
        
    }
}
