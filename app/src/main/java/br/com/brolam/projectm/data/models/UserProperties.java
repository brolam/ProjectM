package br.com.brolam.projectm.data.models;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by brenomar on 02/08/17.
 */

public class UserProperties {

    public static final String PATH_USER_PROPERTIES = "userProperties";
    public static final String SURNAME = "surname";
    public static final String DATE_OF_BIRTH = "dateOfBirth";

    public static HashMap<String, Object> getNewUserProperties(String surName, Date dateOfBirth) {
        HashMap<String, Object> result = new HashMap<>();
        result.put(SURNAME, surName);
        result.put(DATE_OF_BIRTH, dateOfBirth.getTime());
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

    public static Date parseDateOfBirthValid(String strDay, String strMonth, String strYear) throws ParseException {
        String strDateOfBirth = String.format("%04d%02d%02d", Integer.parseInt(strYear), Integer.parseInt(strMonth), Integer.parseInt(strDay));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        return simpleDateFormat.parse(strDateOfBirth);
    }

    public static boolean isPasswordValid(String password) {
        if (TextUtils.isEmpty(password)) return false;
        if (password.length() <= 4 ) return false;
        return true;
    }
}
