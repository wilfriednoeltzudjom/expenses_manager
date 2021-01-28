package com.thetechshrine.expensemanager.fragments.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetechshrine.expensemanager.models.Expense;
import com.thetechshrine.expensemanager.payloads.DatabaseResponse;
import com.thetechshrine.expensemanager.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class DiscoverViewModel extends ViewModel {

    private MutableLiveData<Boolean> loading;
    private MutableLiveData<Double> totalExpensesOfTheCurrentMonth;
    private MutableLiveData<List<Expense>> lastDaysExpenses;
    private MutableLiveData<DatabaseResponse> databaseResponse;

    LiveData<Boolean> getLoading() {
        if (loading == null) {
            loading = new MutableLiveData<>();
            loading.setValue(true);
        }

        return loading;
    }

    LiveData<Double> getTotalExpensesOfTheCurrentMonth() {
        if (totalExpensesOfTheCurrentMonth == null) {
            totalExpensesOfTheCurrentMonth = new MutableLiveData<>();
            totalExpensesOfTheCurrentMonth.setValue(0.00);
        }

        return totalExpensesOfTheCurrentMonth;
    }

    LiveData<List<Expense>> getLastDaysExpenses() {
        if (lastDaysExpenses == null) lastDaysExpenses = new MutableLiveData<>();
        return lastDaysExpenses;
    }

    LiveData<DatabaseResponse> getDatabaseResponse() {
        if (databaseResponse == null) databaseResponse = new MutableLiveData<>();
        return databaseResponse;
    }

    void loadTotalExpensesOfTheCurrentMonth() {
        loading.setValue(true);
        totalExpensesOfTheCurrentMonth.setValue(0.0);

        try (Realm realm = Realm.getDefaultInstance()) {
            DateUtils dateUtils = new DateUtils();
            RealmResults<Expense> expenseRealmResults = realm.where(Expense.class).between(Expense.PROPERTY_CREATED_AT, dateUtils.getFirstDateOfTheCurrentMonth(), dateUtils.getLastDateOfTheCurrentMonth()).findAll();
            if (expenseRealmResults.size() > 0) totalExpensesOfTheCurrentMonth.setValue(expenseRealmResults.sum(Expense.PROPERTY_AMOUNT).doubleValue());
        }

        loading.setValue(false);
    }

    void loadLastDaysExpenses() {
        loading.setValue(true);
        lastDaysExpenses.setValue(new ArrayList<>());

        try (Realm realm = Realm.getDefaultInstance()) {
            DateUtils dateUtils = new DateUtils();
            RealmResults<Expense> expenseRealmResults = realm.where(Expense.class).greaterThanOrEqualTo(Expense.PROPERTY_CREATED_AT, dateUtils.getDate30DaysAgo()).sort(Expense.PROPERTY_CREATED_AT, Sort.DESCENDING).findAll();
            lastDaysExpenses.setValue(new ArrayList<>(expenseRealmResults));
        }

        loading.setValue(false);
    }

    void deleteExpense(String expenseId) {
        try (Realm realm = Realm.getDefaultInstance()) {
            Expense expense = realm.where(Expense.class).equalTo(Expense.PROPERTY_ID, expenseId).findFirst();
            if (expense == null) return;

            realm.beginTransaction();
            expense.deleteFromRealm();
            realm.commitTransaction();
        }

        databaseResponse.setValue(new DatabaseResponse(DatabaseResponse.EXPENSE_DELETED));
    }
}
