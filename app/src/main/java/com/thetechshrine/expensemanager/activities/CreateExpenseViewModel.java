package com.thetechshrine.expensemanager.activities;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetechshrine.expensemanager.R;
import com.thetechshrine.expensemanager.models.Category;
import com.thetechshrine.expensemanager.models.Expense;
import com.thetechshrine.expensemanager.models.Market;
import com.thetechshrine.expensemanager.payloads.DatabaseResponse;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class CreateExpenseViewModel extends ViewModel {

    private MutableLiveData<List<Category>> categories;
    private MutableLiveData<List<Market>> markets;
    private MutableLiveData<DatabaseResponse> databaseResponse;
    private MutableLiveData<Boolean> loading;

    LiveData<List<Category>> getCategories() {
        if (categories == null) categories = new MutableLiveData<>();
        return categories;
    }

    LiveData<DatabaseResponse> getDatabaseResponse() {
        if (databaseResponse == null) databaseResponse = new MutableLiveData<>();
        return databaseResponse;
    }

    public LiveData<Boolean> getLoading() {
        if (loading == null) {
            loading = new MutableLiveData<>();
            loading.setValue(false);
        }

        return loading;
    }

    void loadCategories() {
        try (Realm realm = Realm.getDefaultInstance()) {
            RealmResults<Category> categoryRealmResults = realm.where(Category.class).sort(Category.PROPERTY_NAME, Sort.ASCENDING).findAll();
            categories.setValue(new ArrayList<>(categoryRealmResults));
        }
    }

    LiveData<List<Market>> getMarkets() {
        if (markets == null) markets = new MutableLiveData<>();
        return markets;
    }

    void loadMarkets() {
        try (Realm realm = Realm.getDefaultInstance()) {
            RealmResults<Market> marketRealmResults = realm.where(Market.class).findAll();
            markets.setValue(new ArrayList<>(marketRealmResults));
        }
    }

    private boolean isMarketNameAlreadyIncluded(String marketName) {
        boolean included = false;
        List<Market> marketNames = markets.getValue();

        if (marketNames != null) {
            for (Market market : marketNames) {
                if (market.getMarketName().toLowerCase().equals(marketName.toLowerCase())) {
                    included = true;
                    break;
                }
            }
        }

        return included;
    }

    void saveExpense(Expense expense) {
        loading.setValue(true);

        try (Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            realm.copyToRealm(expense);

            String marketName = expense.getMarketName();
            if (marketName != null && !isMarketNameAlreadyIncluded(marketName)) {
                Market market = new Market(marketName);
                realm.copyToRealm(market);
            }
            realm.commitTransaction();
        }

        databaseResponse.setValue(new DatabaseResponse(DatabaseResponse.EXPENSE_CREATED));
        loading.setValue(false);
    }
}
