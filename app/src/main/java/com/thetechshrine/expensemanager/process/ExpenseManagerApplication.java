package com.thetechshrine.expensemanager.process;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.thetechshrine.expensemanager.R;
import com.thetechshrine.expensemanager.database.DatabaseManager;
import com.thetechshrine.expensemanager.database.SessionManager;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ExpenseManagerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initRealmDatabase();
        initCalligraphy();
    }

    private void initRealmDatabase() {
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().name("expense_manager_db.realm").build();
        Realm.setDefaultConfiguration((configuration));
    }

    private void initCalligraphy() {
        ViewPump.init(ViewPump.builder().addInterceptor(new CalligraphyInterceptor(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/poppins/Poppins-Regular.ttf").setFontAttrId(R.attr.fontPath).build())).build());
    }
}