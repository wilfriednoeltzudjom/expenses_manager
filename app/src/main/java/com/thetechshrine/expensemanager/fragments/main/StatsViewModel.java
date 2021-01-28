package com.thetechshrine.expensemanager.fragments.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetechshrine.expensemanager.models.Expense;
import com.thetechshrine.expensemanager.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class StatsViewModel extends ViewModel {

    private MutableLiveData<Boolean> loading;
    private MutableLiveData<Double> totalExpenses;
    private MutableLiveData<List<Expense>> expenses;

    public LiveData<Boolean> getLoading() {
        if (loading == null) {
            loading = new MutableLiveData<>();
            loading.setValue(false);
        }

        return loading;
    }

    public LiveData<Double> getTotalExpenses() {
        if (totalExpenses == null) {
            totalExpenses = new MutableLiveData<>();
            totalExpenses.setValue(0.00);
        }

        return totalExpenses;
    }

    public LiveData<List<Expense>> getExpenses() {
        if (expenses == null) expenses = new MutableLiveData<>();

        return expenses;
    }

    public void search(Date startDate, Date endDate) {
        loading.setValue(true);

        try (Realm realm = Realm.getDefaultInstance()) {
            DateUtils dateUtils = new DateUtils();
            RealmResults<Expense> expenseRealmResults = realm.where(Expense.class).between(Expense.PROPERTY_CREATED_AT, dateUtils.setTimeToStartOfDay(startDate), dateUtils.setTimeToEndOfDay(endDate)).sort(Expense.PROPERTY_CREATED_AT, Sort.DESCENDING).findAll();
            if (expenseRealmResults.size() > 0) totalExpenses.setValue(expenseRealmResults.sum(Expense.PROPERTY_AMOUNT).doubleValue());
            expenses.setValue(new ArrayList<>(expenseRealmResults));
        }

        loading.setValue(false);
    }
}
