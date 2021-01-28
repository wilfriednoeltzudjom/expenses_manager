package com.thetechshrine.expensemanager.database;

import android.content.Context;
import android.util.Log;

import com.thetechshrine.expensemanager.R;
import com.thetechshrine.expensemanager.models.Category;
import com.thetechshrine.expensemanager.models.Market;

import java.util.Arrays;
import java.util.List;

import io.realm.Realm;

public class DatabaseManager {

    Context context;

    public DatabaseManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public static DatabaseManager from(Context context) {
        return new DatabaseManager(context);
    }

    public void initCategories() {
        try (Realm realm = Realm.getDefaultInstance()) {
            List<Category> categories = Arrays.asList(
                    new Category(context.getResources().getString(R.string.clothing), R.drawable.ic_category_clothing, "ic_category_clothing", false),
                    new Category(context.getResources().getString(R.string.drug), R.drawable.ic_category_drug, "ic_category_drug", false),
                    new Category(context.getResources().getString(R.string.expense), R.drawable.ic_category_expense, "ic_category_expense", false),
                    new Category(context.getResources().getString(R.string.family), R.drawable.ic_category_family, "ic_category_family", false),
                    new Category(context.getResources().getString(R.string.food_drink), R.drawable.ic_category_food_drink, "ic_category_food_drink", false),
                    new Category(context.getResources().getString(R.string.fuel), R.drawable.ic_category_fuel, "ic_category_fuel", false),
                    new Category(context.getResources().getString(R.string.gift), R.drawable.ic_category_gift, "ic_category_gift", false),
                    new Category(context.getResources().getString(R.string.groceries), R.drawable.ic_category_groceries, "ic_category_groceries", false),
                    new Category(context.getResources().getString(R.string.hospital), R.drawable.ic_category_hospital, "ic_category_hospital", false),
                    new Category(context.getResources().getString(R.string.insurance), R.drawable.ic_category_insurance, "ic_category_insurance", false),
                    new Category(context.getResources().getString(R.string.internet), R.drawable.ic_category_internet, "ic_category_internet", false),
                    new Category(context.getResources().getString(R.string.invoice), R.drawable.ic_category_invoice, "ic_category_invoice", false),
                    new Category(context.getResources().getString(R.string.laundry), R.drawable.ic_category_laundry, "ic_category_laundry", false),
                    new Category(context.getResources().getString(R.string.loan), R.drawable.ic_category_loan, "ic_category_loan", false),
                    new Category(context.getResources().getString(R.string.merchandise), R.drawable.ic_category_merchandise, "ic_category_merchandise", false),
                    new Category(context.getResources().getString(R.string.movie), R.drawable.ic_category_movie, "ic_category_movie", false),
                    new Category(context.getResources().getString(R.string.pets), R.drawable.ic_category_pets, "ic_category_pets", false),
                    new Category(context.getResources().getString(R.string.rent), R.drawable.ic_category_rent, "ic_category_rent", false),
                    new Category(context.getResources().getString(R.string.restaurant), R.drawable.ic_category_restaurant, "ic_category_restaurant", false),
                    new Category(context.getResources().getString(R.string.smartphone), R.drawable.ic_category_smartphone, "ic_category_smartphone", false),
                    new Category(context.getResources().getString(R.string.tax), R.drawable.ic_category_tax, "ic_category_tax", false),
                    new Category(context.getResources().getString(R.string.transport), R.drawable.ic_category_transport, "ic_category_transport", false)
            );

            realm.beginTransaction();
            realm.insert(categories);
            realm.commitTransaction();
        }
    }

    public void initMarkets() {
        try (Realm realm = Realm.getDefaultInstance()) {
            List<Market> markets = Arrays.asList(
                    new Market("Carrefour"),
                    new Market("Auchan"),
                    new Market("Amazon"),
                    new Market("Cora"),
                    new Market("Netflix"),
                    new Market("Prime video"),
                    new Market("Deliveroo"),
                    new Market("UberEats"),
                    new Market("JustEat"),
                    new Market("Cdiscount"),
                    new Market("Backmarket"),
                    new Market("Uber"),
                    new Market("Colruyt"),
                    new Market("Delhaize"),
                    new Market("Super U"),
                    new Market("Grand Frais"),
                    new Market("Intermarché"),
                    new Market("Casino")
            );

            realm.beginTransaction();
            realm.insert(markets);
            realm.commitTransaction();
        }
    }
}
