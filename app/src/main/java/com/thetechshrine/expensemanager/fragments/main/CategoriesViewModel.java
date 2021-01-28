package com.thetechshrine.expensemanager.fragments.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetechshrine.expensemanager.models.Category;
import com.thetechshrine.expensemanager.payloads.DatabaseResponse;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class CategoriesViewModel extends ViewModel {

    private MutableLiveData<List<Category>> categories;
    private MutableLiveData<DatabaseResponse> databaseResponse;

    LiveData<List<Category>> getCategories() {
        if (categories == null) categories = new MutableLiveData<>();
        return categories;
    }

    LiveData<DatabaseResponse> getDatabaseResponse() {
        if (databaseResponse == null) databaseResponse = new MutableLiveData<>();
        return databaseResponse;
    }

    void loadCategories() {
        try (Realm realm = Realm.getDefaultInstance()) {
            RealmResults<Category> categoryRealmResults = realm.where(Category.class).sort(Category.PROPERTY_NAME, Sort.ASCENDING).findAll();
            categories.setValue(new ArrayList<>(categoryRealmResults));
        }
    }

    void saveCategory(Category category) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            realm.copyToRealm(category);
            realm.commitTransaction();
        }

        databaseResponse.setValue(new DatabaseResponse(DatabaseResponse.CATEGORY_CREATED));
    }

    void deleteCategory(String categoryId) {
        try (Realm realm = Realm.getDefaultInstance()) {
            Category category = realm.where(Category.class).equalTo(Category.PROPERTY_ID, categoryId).findFirst();
            if (category != null) {
                realm.beginTransaction();
                category.deleteFromRealm();
                realm.commitTransaction();
            }
        }

        databaseResponse.setValue(new DatabaseResponse(DatabaseResponse.CATEGORY_DELETED));
    }
}
