package com.thetechshrine.expensemanager.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.thetechshrine.expensemanager.R;

public class SessionManager {

    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private static final String KEY_FIRST_LAUNCH = "FIRST_LAUNCH";
    private static final String KEY_LOGGED_IN = "LOGGED_IN";
    private static final String KEY_CURRENCY_CODE = "CURRENCY_CODE";
    private static final String KEY_CURRENCY_NAME = "CURRENCY_NAME";

    public SessionManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(context.getResources().getString(R.string.preference_file), Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.apply();
    }

    public static SessionManager from(Context context) {
        return new SessionManager(context);
    }

    public boolean isFirstLaunch() {
        return preferences.getBoolean(KEY_FIRST_LAUNCH, false);
    }

    public void setFirstLaunch() {
        editor.putBoolean(KEY_FIRST_LAUNCH, true);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_LOGGED_IN, false);
    }

    public void setLoggedIn() {
        editor.putBoolean(KEY_LOGGED_IN, true);
        editor.commit();
    }

    public String getCurrencyCode() {
        return preferences.getString(KEY_CURRENCY_CODE, "");
    }

    public void setCurrencyCode(String currencyCode) {
        editor.putString(KEY_CURRENCY_CODE, currencyCode);
        editor.commit();
    }

    public String getCurrencyName() {
        return preferences.getString(KEY_CURRENCY_NAME, "");
    }

    public void setCurrencyName(String currencyName) {
        editor.putString(KEY_CURRENCY_NAME, currencyName);
        editor.commit();
    }
}
