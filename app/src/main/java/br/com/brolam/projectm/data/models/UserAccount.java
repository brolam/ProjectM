package br.com.brolam.projectm.data.models;

import android.text.TextUtils;

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
}
