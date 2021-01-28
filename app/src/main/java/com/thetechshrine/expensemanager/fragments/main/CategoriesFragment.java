package com.thetechshrine.expensemanager.fragments.main;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.thetechshrine.expensemanager.R;
import com.thetechshrine.expensemanager.adapters.CategoryAdapter;
import com.thetechshrine.expensemanager.fragments.CreateCategoryFragment;
import com.thetechshrine.expensemanager.models.Category;
import com.thetechshrine.expensemanager.payloads.DatabaseResponse;
import com.thetechshrine.expensemanager.utils.EntitiesUtils;
import com.thetechshrine.expensemanager.utils.Event;
import com.thetechshrine.expensemanager.utils.EventBus;
import com.thetechshrine.expensemanager.utils.modals.ConfirmationDeleteModal;
import com.thetechshrine.expensemanager.utils.modals.SuccessModal;

import java.util.ArrayList;

public class CategoriesFragment extends Fragment {

    private Context context;
    private CategoriesViewModel viewModel;

    private Button create;
    private RecyclerView recyclerView;

    private CategoryAdapter adapter;

    public static CategoriesFragment newInstance() {
        return new CategoriesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.categories_fragment, container, false);
        bindViews(rootView);
        setupViews();
        subscribeToBus();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);

        viewModel.getCategories().observe(this, categories -> adapter.updateCategories(categories));

        viewModel.getDatabaseResponse().observe(this, this::handleDatabaseResponse);
    }

    @Override
    public void onResume() {
        super.onResume();

        viewModel.loadCategories();
    }

    private void bindViews(View rootView) {
        create = rootView.findViewById(R.id.categories_create);
        recyclerView = rootView.findViewById(R.id.categories_recycler_view);
    }

    private void setupViews() {
        setupCreate();
        setupRecyclerView();
    }

    private void subscribeToBus() {
        EventBus.subscribe(EventBus.SUBJECT_CATEGORIES_FRAGMENT, this, o -> {
            if (o instanceof Event) {
                Event event = (Event) o;
                switch (event.getSubject()) {
                    case Event.CREATE_CATEGORY:
                        saveCategory((String) event.getData());
                        break;
                    case Event.DELETE_CATEGORY:
                        deleteCategory((String) event.getData());
                        break;
                }
            }
        });
    }

    private void saveCategory(String name) {
        Category category = new Category(EntitiesUtils.from(context).capitalize(name), R.drawable.ic_category_expense, "ic_category_expense", true);
        viewModel.saveCategory(category);
    }

    private void deleteCategory(String categoryId) {
        ConfirmationDeleteModal confirmationDeleteModal = new ConfirmationDeleteModal(context, (ConfirmationDeleteModal.OnConfirmationDeleteModalChangeListener) () -> viewModel.deleteCategory(categoryId));
        confirmationDeleteModal.setMessage(getResources().getString(R.string.confirm_delete_category));
        confirmationDeleteModal.show();
    }

    private void setupCreate() {
         create.setOnClickListener(v -> {
             CreateCategoryFragment fragment = new CreateCategoryFragment();
             if (getActivity() != null) {
                 fragment.show(getActivity().getSupportFragmentManager(), fragment.getTag());
             }
         });
    }

    private void setupRecyclerView() {
        adapter = new CategoryAdapter(context, new ArrayList<>());
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setAdapter(adapter);
    }

    private void handleDatabaseResponse(DatabaseResponse databaseResponse) {
        if (databaseResponse == null) return;

        if (databaseResponse.getResponseCode() == DatabaseResponse.CATEGORY_CREATED) {
            viewModel.loadCategories();

            SuccessModal successModal = new SuccessModal(context);
            successModal.setMessage(getResources().getString(R.string.category_saved));
            successModal.show();
        } else if (databaseResponse.getResponseCode() == DatabaseResponse.CATEGORY_DELETED) viewModel.loadCategories();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBus.unregister(this);
    }
}
