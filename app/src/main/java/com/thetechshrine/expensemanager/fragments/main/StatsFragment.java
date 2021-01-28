package com.thetechshrine.expensemanager.fragments.main;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thetechshrine.expensemanager.R;
import com.thetechshrine.expensemanager.adapters.ExpenseAdapter;
import com.thetechshrine.expensemanager.database.SessionManager;
import com.thetechshrine.expensemanager.fragments.PickDateFragment;
import com.thetechshrine.expensemanager.models.Category;
import com.thetechshrine.expensemanager.models.Expense;
import com.thetechshrine.expensemanager.utils.DateUtils;
import com.thetechshrine.expensemanager.utils.EntitiesUtils;
import com.thetechshrine.expensemanager.utils.Event;
import com.thetechshrine.expensemanager.utils.EventBus;
import com.thetechshrine.expensemanager.utils.modals.ErrorModal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class StatsFragment extends Fragment {

    private Context context;
    private StatsViewModel viewModel;

    private SwipeRefreshLayout refreshLayout;
    private LinearLayout contentContainer;
    private LinearLayout noDataFoundContainer;
    private TextView startDate;
    private TextView endDate;
    private ImageButton search;
    private TextView currency;
    private TextView totalExpenses;
    private RecyclerView categoriesRecyclerView;
    private RecyclerView expensesRecyclerView;

    private Date startDateValue;
    private Date endDateValue;
    private int dateRequester;
    private StatsCategoryAdapter statsCategoryAdapter;
    private ExpenseAdapter expenseAdapter;

    public static StatsFragment newInstance() {
        return new StatsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stats_fragment, container, false);
        bindViews(rootView);
        setupViews();
        subscribeToBus();

        return rootView;
    }

    private void bindViews(View rootView) {
        refreshLayout = rootView.findViewById(R.id.refresh_layout);
        contentContainer = rootView.findViewById(R.id.content_container);
        noDataFoundContainer = rootView.findViewById(R.id.no_data_found_container);
        startDate = rootView.findViewById(R.id.stats_start_date);
        endDate = rootView.findViewById(R.id.stats_end_date);
        search = rootView.findViewById(R.id.stats_search);
        currency = rootView.findViewById(R.id.stats_currency);
        totalExpenses = rootView.findViewById(R.id.stats_total_expenses);
        categoriesRecyclerView = rootView.findViewById(R.id.stats_categories_recycler_view);
        expensesRecyclerView = rootView.findViewById(R.id.stats_expenses_recycler_view);
    }

    private void setupViews() {
        setupStartDate();
        setupEndDate();
        setupCategoriesRecyclerView();
        setupExpensesRecyclerView();
        setupSearch();
        setupCurrency();
    }

    private void subscribeToBus() {
        EventBus.subscribe(EventBus.SUBJECT_SELECT_DATE, this, o -> {
            if (o instanceof Event) {
                Event event = (Event) o;
                switch (event.getSubject()) {
                    case Event.DATE_SELECTED:
                        updateDate((Date) event.getData());
                        break;
                }
            }
        });
    }

    private void displayDatePicker() {
        if (getActivity() == null) return;

        Fragment fragment = new PickDateFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.add(android.R.id.content, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void setupStartDate() {
        startDate.setOnClickListener(v -> {
            dateRequester = DateRequester.START_DATE;
            displayDatePicker();
        });
    }

    private void setupEndDate() {
        endDate.setOnClickListener(v -> {
            dateRequester = DateRequester.END_DATE;
            displayDatePicker();
        });
    }

    private void setupSearch() {
        search.setOnClickListener(v -> processSearch());
    }

    private void processSearch() {
        if (startDateValue == null) {
            ErrorModal errorModal = new ErrorModal(context);
            errorModal.setMessage(getResources().getString(R.string.provide_start_date));
            errorModal.show();
            return;
        }

        noDataFoundContainer.setVisibility(View.GONE);
        contentContainer.setVisibility(View.GONE);
        viewModel.search(startDateValue, endDateValue != null ? endDateValue : new Date());
    }

    private void updateDate(Date date) {
        switch (dateRequester) {
            case DateRequester.START_DATE:
                startDate.setText(DateUtils.formatDate(date));
                startDateValue = date;
                break;
            case DateRequester.END_DATE:
                endDate.setText(DateUtils.formatDate(date));
                endDateValue = date;
                break;
        }
    }

    private void setupCategoriesRecyclerView() {
        statsCategoryAdapter = new StatsCategoryAdapter(context, new ArrayList<>());
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        categoriesRecyclerView.setAdapter(statsCategoryAdapter);
    }

    private void setupCurrency() {
        currency.setText(SessionManager.from(context).getCurrencyCode());
    }

    private void setupExpensesRecyclerView() {
        List<Expense> expenses = new ArrayList<>();
        expenseAdapter = new ExpenseAdapter(context, expenses);
        expensesRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        expensesRecyclerView.setAdapter(expenseAdapter);
    }

    private void showOrHideContent(boolean dataAvailable) {
        noDataFoundContainer.setVisibility(dataAvailable ? View.GONE : View.VISIBLE);
        contentContainer.setVisibility(dataAvailable ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(StatsViewModel.class);

        viewModel.getLoading().observe(this, loading -> {
            refreshLayout.setEnabled(loading);
            refreshLayout.setRefreshing(loading);
        });

        viewModel.getExpenses().observe(this, expenses -> {
            showOrHideContent(expenses.size() > 0);
            expenseAdapter.updateExpenses(expenses);
            statsCategoryAdapter.updateStatsCategories(parseExpenses(expenses));
        });

        viewModel.getTotalExpenses().observe(this, totalExpensesValue -> {
            totalExpenses.setText(EntitiesUtils.from(context).formatAmount(totalExpensesValue, false, false));
        });
    }

    private class DateRequester {
        static final int START_DATE = 1;
        static final int END_DATE = 2;
    }

    private class StatsCategory {
        private double amount;
        private String name;

        public StatsCategory(double amount, String name) {
            this.amount = amount;
            this.name = name;
        }

        public double getAmount() {
            return amount;
        }

        public String getName() {
            return name;
        }
    }

    private boolean isCategoryAlreadyIncludedInList(Category category, List<StatsCategory> statsCategories) {
        boolean included = false;
        for (StatsCategory statsCategory : statsCategories) {
            if (statsCategory.getName().equals(category.getName())) {
                included = true;
                break;
            }
        }

        return included;
    }

    private List<StatsCategory> parseExpenses(List<Expense> expenses) {
        List<StatsCategory> statsCategories = new ArrayList<>();

        for (int i=0; i<expenses.size(); i++) {
            double amount = expenses.get(i).getAmount();
            Category category = expenses.get(i).getCategory();
            if (isCategoryAlreadyIncludedInList(category, statsCategories)) continue;

            for (int j=i+1; j<expenses.size(); j++) {
                if (expenses.get(j).getCategory().getName().equals(category.getName())) {
                    amount += expenses.get(j).getAmount();
                }
            }

            statsCategories.add(new StatsCategory(amount, category.getName()));
        }

        return statsCategories;
    }

    private class StatsCategoryAdapter extends RecyclerView.Adapter<StatsCategoryAdapter.ViewHolder> {

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView amount;
            TextView name;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                amount = itemView.findViewById(R.id.stats_category_item_amount);
                name = itemView.findViewById(R.id.stats_category_item_name);
            }
        }

        private Context context;
        private List<StatsCategory> statsCategories;

        public StatsCategoryAdapter(Context context, List<StatsCategory> statsCategories) {
            this.context = context;
            this.statsCategories = statsCategories;
        }

        @NonNull
        @Override
        public StatsCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View categoryView = inflater.inflate(R.layout.stats_category_item, parent, false);
            return new ViewHolder(categoryView);
        }

        @Override
        public void onBindViewHolder(@NonNull StatsCategoryAdapter.ViewHolder holder, int position) {
            StatsCategory statsCategory = statsCategories.get(position);

            holder.name.setText(statsCategory.getName());
            holder.amount.setText(EntitiesUtils.from(context).formatAmount(statsCategory.getAmount(), true, false));
        }

        @Override
        public int getItemCount() {
            return statsCategories.size();
        }

        public void updateStatsCategories(List<StatsCategory> list) {
            if (list.size() == 0) return;

            statsCategories.clear();
            statsCategories.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBus.unregister(this);
    }
}
