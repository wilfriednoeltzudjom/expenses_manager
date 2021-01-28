package com.thetechshrine.expensemanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.thetechshrine.expensemanager.R;
import com.thetechshrine.expensemanager.adapters.CurrencyItemAdapter;
import com.thetechshrine.expensemanager.database.DatabaseManager;
import com.thetechshrine.expensemanager.database.SessionManager;
import com.thetechshrine.expensemanager.payloads.CurrencyItem;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class SplashActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button save;

    private SessionManager sessionManager;
    private DatabaseManager databaseManager;
    private CurrencyItemAdapter currencyItemAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = SessionManager.from(this);
        databaseManager = new DatabaseManager(this);

        bindViews();
        setupViews();
    }

    private void bindViews() {
        setContentView(R.layout.activity_splash);

        recyclerView = findViewById(R.id.splash_recycler_view);
        save = findViewById(R.id.splash_save);
    }

    private void setupViews() {
        setupRecyclerView();
        setupSave();
    }

    private void setupRecyclerView() {
        currencyItemAdapter = new CurrencyItemAdapter(this, CurrencyItem.getCurrencyItems(this));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(currencyItemAdapter);
    }

    private void setupSave() {
        save.setOnClickListener(v -> {
            CurrencyItem currencyItem = currencyItemAdapter.getSelectedCurrencyItem();
            sessionManager.setCurrencyCode(currencyItem.getCode());
            sessionManager.setCurrencyName(currencyItem.getName());
            sessionManager.setLoggedIn();

            if (!sessionManager.isFirstLaunch()) {
                databaseManager.initCategories();
                databaseManager.initMarkets();
                sessionManager.setFirstLaunch();
            }

            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
