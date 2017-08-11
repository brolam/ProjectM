package br.com.brolam.projectm;


import org.junit.Test;

import java.util.HashMap;

import br.com.brolam.projectm.data.models.Job;
import br.com.brolam.projectm.data.models.UserAccount;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserAccountUnitTest {

    @Test
    public void getNewUserAccountUndefined(){
        UserAccount.AccountTypes accountType = UserAccount.AccountTypes.UNDEFINED;
        HashMap<String, Object> newUserAccount = UserAccount.getNewUserAccount(accountType);
        assertEquals(accountType.name(), newUserAccount.get(UserAccount.TYPE));
        assertEquals(false, UserAccount.isValid(newUserAccount));
    }

    @Test
    public void getNewUserAccountFree(){
        UserAccount.AccountTypes accountType = UserAccount.AccountTypes.FREE;
        HashMap<String, Object> newUserAccount = UserAccount.getNewUserAccount(accountType);
        assertEquals(accountType.name(), newUserAccount.get(UserAccount.TYPE));
        assertEquals(true, UserAccount.isValid(newUserAccount));
    }

    @Test
    public void getNewUserAccountPremium(){
        UserAccount.AccountTypes accountType = UserAccount.AccountTypes.PREMIUM;
        HashMap<String, Object> newUserAccount = UserAccount.getNewUserAccount(accountType);
        assertEquals(accountType.name(), newUserAccount.get(UserAccount.TYPE));
        assertEquals(true, UserAccount.isValid(newUserAccount));
    }

    @Test
    public void isUserAccountReference(){
        String fullPathReference = "https://projectm-e2a21.firebaseio.com/job/5PQBNu6346Tz3eqsOzSUZOuDEiv2";
        assertEquals(true, Job.isJobReference(fullPathReference));

    }

    @Test
    public void isValidParseException() {
        HashMap<String, Object> userAccount = new HashMap<>();
        userAccount.put(UserAccount.TYPE, "INVALID_TYPE");
        assertFalse(UserAccount.isValid(userAccount));

    }
}