package br.com.brolam.projectm.data.models;

import android.text.TextUtils;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

/**
 * Created by brenomar on 02/08/17.
 */

public class UserAccount {

    public static final String REFERENCE_NAME = "userAccount";
    public static final String TYPE = "type";



    public enum AccountTypes {
        UNDEFINED,
        FREE,
        PREMIUM
    }

    public static HashMap<String, Object> getNewUserAccount(AccountTypes accountType) {
        HashMap<String, Object> result = new HashMap<>();
        result.put(TYPE, accountType.name());
        return result;
    }

    public static boolean isReferenceUserAccount(DatabaseReference ref) {
        return ref.getRef().toString().contains(REFERENCE_NAME);
    }

    public static boolean isValid(HashMap<String, Object> userAccount) {
        if (userAccount == null) return false;
        if (userAccount.containsKey(TYPE) == false ) return false;
        if (userAccount.get(TYPE).equals(AccountTypes.UNDEFINED)) return false;
        return true;
    }
}
