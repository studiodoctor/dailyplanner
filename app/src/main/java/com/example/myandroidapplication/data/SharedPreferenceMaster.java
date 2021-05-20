package com.example.myandroidapplication.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

public class SharedPreferenceMaster {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    // shared pref mode
    int PRIVATE_MODE = 0;
    // Shared preferences file name
    private static final String PREF_NAME = "MyAndroidApplication";
    private static final String USER_NAME = "UserName";
    private static final String USER_SURNAME = "UserSurname";
    private static final String USER_CELL = "UserCell";
    private static final String USER_ADDRESS = "UserAddress";

    private static final String USER_ID = "UserId";
    private static final String IS_FIRST_TIME_USER="IsFirstTimeUser";


    public SharedPreferenceMaster(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setUserId(String result) {
        editor.putString(USER_ID, String.valueOf(result));
        editor.commit();
    }

    public String getUserId() {
        return pref.getString(USER_ID, "0");
    }


    public void setIsFirstTimeUser(boolean result) {
        editor.putBoolean(IS_FIRST_TIME_USER, result);
        editor.commit();
    }

    public boolean getIsFirstTimeUser() {
        return pref.getBoolean(IS_FIRST_TIME_USER, true);
    }

    public void setUserName(String result) {
        editor.putString(USER_NAME, String.valueOf(result));
        editor.commit();
    }

    public String getUserName() {
        return pref.getString(USER_NAME, "NA");
    }

    public void setUserSurname(String result) {
        editor.putString(USER_SURNAME, String.valueOf(result));
        editor.commit();
    }

    public String getUserSurname()
    {
        return pref.getString(USER_SURNAME,"NA");
    }

    public void setUserCell(String result) {
        editor.putString(USER_CELL, String.valueOf(result));
        editor.commit();
    }

    public String getUserCell()
    {
        return pref.getString(USER_CELL, "NA");
    }

    public void setUserAddress(String result) {
        editor.putString(USER_ADDRESS, String.valueOf(result));
        editor.commit();
    }

    public String getUserAddress()
    {
        return pref.getString(USER_ADDRESS, "NA");
    }
}
