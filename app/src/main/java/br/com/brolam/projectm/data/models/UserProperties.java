package br.com.brolam.projectm.data.models;

import android.text.TextUtils;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

/**
 * Created by brenomar on 02/08/17.
 */

public class UserProperties {

    public static final String REFERENCE_NAME = "userProperties";
    public static final String NAME = "name";
    public static final String SURNAME = "surname";

    public static HashMap<String, Object> getNewUserProperties(String name, String surname) {
        HashMap<String, Object> result = new HashMap<>();
        result.put(NAME, name);
        result.put(SURNAME, surname);
        return result;
    }

    public static boolean isNameValid(String name) {
        if (TextUtils.isEmpty(name)) return false;
        if (name.length() <= 2) return false;
        return true;
    }

    public static boolean isSurnameValid(String surname) {
        if (TextUtils.isEmpty(surname)) return false;
        if (surname.length() <= 2) return false;
        return true;
    }

    public static boolean isEmailValid(String email) {
        if (TextUtils.isEmpty(email)) return false;
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return true;
        else
            return false;
    }

    public static boolean isPasswordValid(String password) {
        if (TextUtils.isEmpty(password)) return false;
        if (password.length() <= 4 ) return false;
        return true;
    }

    public static boolean isReferenceUserAccount(DatabaseReference ref) {
        return ref.getRef().toString().contains(REFERENCE_NAME);
    }

    public static String getFullName(HashMap<String, Object> userProperties) {
        return String.format("%s %s", userProperties.get(UserProperties.NAME), userProperties.get(UserProperties.SURNAME));
    }
}
