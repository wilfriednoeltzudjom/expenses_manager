package com.thetechshrine.expensemanager.fragments.main;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thetechshrine.expensemanager.R;
import com.thetechshrine.expensemanager.adapters.ExpenseAdapter;
import com.thetechshrine.expensemanager.components.Overview;
import com.thetechshrine.expensemanager.models.Category;
import com.thetechshrine.expensemanager.models.Expense;
import com.thetechshrine.expensemanager.payloads.DatabaseResponse;
import com.thetechshrine.expensemanager.utils.EntitiesUtils;
import com.thetechshrine.expensemanager.utils.Event;
import com.thetechshrine.expensemanager.utils.EventBus;
import com.thetechshrine.expensemanager.utils.TypefaceUtils;
import com.thetechshrine.expensemanager.utils.modals.ConfirmationDeleteModal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DiscoverFragment extends Fragment {

    private Context context;
    private DiscoverViewModel viewModel;

    private Overview overview;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayout contentContainer;
    private LinearLayout noDataFoundContainer;

    private ExpenseAdapter adapter;

    public static DiscoverFragment newInstance() {
        return new DiscoverFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.discover_fragment, container, false);

        bindViews(rootView);
        setupViews();
        subscribeToBus();

        return rootView;
    }

    private void bindViews(View rootView) {
        overview = rootView.findViewById(R.id.discover_overview);
        recyclerView = rootView.findViewById(R.id.discover_recycler_view);
        refreshLayout = rootView.findViewById(R.id.refresh_layout);
        contentContainer = rootView.findViewById(R.id.content_container);
        noDataFoundContainer = rootView.findViewById(R.id.no_data_found_container);
    }

    private void setupViews() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        adapter = new ExpenseAdapter(context,  new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        overview.setExpenses(new ArrayList<>());
    }

    private void subscribeToBus() {
        EventBus.subscribe(EventBus.SUBJECT_DISCOVER_FRAGMENT, this, o -> {
            if (o instanceof Event) {
                Event event = (Event) o;
                switch (event.getSubject()) {
                    case Event.DELETE_EXPENSE:
                        deleteExpense((String) event.getData());
                        break;
                }
            }
        });
    }

    private void deleteExpense(String expenseId) {
        ConfirmationDeleteModal confirmationDeleteModal = new ConfirmationDeleteModal(context, (ConfirmationDeleteModal.OnConfirmationDeleteModalChangeListener) () -> viewModel.deleteExpense(expenseId));
        confirmationDeleteModal.setMessage(getResources().getString(R.string.confirm_delete_expense));
        confirmationDeleteModal.show();
    }

    private void showOrHideContent(boolean dataAvailable) {
        noDataFoundContainer.setVisibility(dataAvailable ? View.GONE : View.VISIBLE);
        contentContainer.setVisibility(dataAvailable ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DiscoverViewModel.class);

        viewModel.getLoading().observe(this, loading -> {
            refreshLayout.setEnabled(loading);
            refreshLayout.setRefreshing(loading);
        });

        viewModel.getLastDaysExpenses().observe(this, expenses -> {
            showOrHideContent(expenses.size() > 0);
            overview.setExpenses(expenses);
            adapter.updateExpenses(expenses);
        });

        viewModel.getTotalExpensesOfTheCurrentMonth().observe(this, totalExpensesOfTheCurrentMonth -> {
            overview.setOutcome(totalExpensesOfTheCurrentMonth);
        });

        viewModel.getDatabaseResponse().observe(this, this::handleDatabaseResponse);
    }

    @Override
    public void onResume() {
        super.onResume();

        viewModel.loadLastDaysExpenses();
        viewModel.loadTotalExpensesOfTheCurrentMonth();
    }

    private void handleDatabaseResponse(DatabaseResponse databaseResponse) {
        if (databaseResponse == null) return;

        if (databaseResponse.getResponseCode() == DatabaseResponse.EXPENSE_DELETED) {
            viewModel.loadLastDaysExpenses();
            viewModel.loadTotalExpensesOfTheCurrentMonth();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBus.unregister(this);
    }
}
