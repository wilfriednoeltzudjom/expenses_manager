package com.thetechshrine.expensemanager.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thetechshrine.expensemanager.R;
import com.thetechshrine.expensemanager.components.PriceEditText;
import com.thetechshrine.expensemanager.fragments.CreateCategoryFragment;
import com.thetechshrine.expensemanager.fragments.PickDateFragment;
import com.thetechshrine.expensemanager.models.Category;
import com.thetechshrine.expensemanager.models.Expense;
import com.thetechshrine.expensemanager.models.Market;
import com.thetechshrine.expensemanager.payloads.DatabaseResponse;
import com.thetechshrine.expensemanager.utils.DateUtils;
import com.thetechshrine.expensemanager.utils.Event;
import com.thetechshrine.expensemanager.utils.EventBus;
import com.thetechshrine.expensemanager.utils.ModalFactory;
import com.thetechshrine.expensemanager.utils.ResourcesUtils;
import com.thetechshrine.expensemanager.utils.modals.ErrorModal;
import com.thetechshrine.expensemanager.utils.modals.SuccessModal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import io.realm.Realm;

public class CreateExpenseActivity extends AppCompatActivity {

    private Realm realm = Realm.getDefaultInstance();
    private CreateExpenseViewModel viewModel;

    private SwipeRefreshLayout refreshLayout;
    private ImageButton back;
    private Button save;
    private RecyclerView recyclerView;
    private TextView date;
    private EditText title;
    private AutoCompleteTextView marketName;
    private PriceEditText amount;

    private SelectableCategoryAdapter selectableCategoryAdapter;
    private Date selectedDateValue = new Date();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_expense);

        viewModel = new ViewModelProvider(this).get(CreateExpenseViewModel.class);

        bindViews();
        setupViews();
        setupViewModel();
        subscribeToBus();
    }

    @Override
    protected void onResume() {
        super.onResume();

        viewModel.loadCategories();
        viewModel.loadMarkets();
    }

    private void setupViewModel() {
        viewModel.getCategories().observe(this, categories -> selectableCategoryAdapter.updateSelectableCategories(parseCategories(categories)));

        viewModel.getMarkets().observe(this, markets -> setupMarketName(parseMarkets(markets)));

        viewModel.getDatabaseResponse().observe(this, this::handleDatabaseResponse);

        viewModel.getLoading().observe(this, loading -> {
            refreshLayout.setEnabled(loading);
            refreshLayout.setRefreshing(loading);
        });
    }

    private void subscribeToBus() {
        EventBus.subscribe(EventBus.SUBJECT_SELECT_DATE, this, o -> {
            if (o instanceof Event) {
                Event event = (Event) o;
                switch (event.getSubject()) {
                    case Event.DATE_SELECTED:
                        updateDateValue((Date) event.getData());
                        break;
                }
             }
        });
    }

    private void bindViews() {
        refreshLayout = findViewById(R.id.refresh_layout);
        back = findViewById(R.id.back);
        save = findViewById(R.id.create_expense_save);
        recyclerView = findViewById(R.id.create_expense_recycler_view);
        title = findViewById(R.id.create_expense_title);
        date = findViewById(R.id.create_expense_date);
        marketName = findViewById(R.id.create_expense_market_name);
        amount = findViewById(R.id.create_expense_amount);
    }

    private void setupViews() {
        setupRecyclerView();
        setupDate();
        setupSave();
        setupBack();
    }

    private void setupRecyclerView() {
        selectableCategoryAdapter = new SelectableCategoryAdapter(this, new ArrayList<>());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(selectableCategoryAdapter);
    }

    private void setupDate() {
        date.setOnClickListener(v -> {
            Fragment fragment = new PickDateFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.add(android.R.id.content, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void setupBack() {
        back.setOnClickListener(v -> onBackPressed());
    }

    private void saveExpense() {
        String titleValue = title.getText() != null ? title.getText().toString().trim() : "";
        String marketNameValue = marketName.getText() != null ? marketName.getText().toString().trim(): "";
        double amountValue = amount.getValue();
        Category category = selectableCategoryAdapter.getSelectedCategory();

        if (amountValue == 0) {
            ErrorModal errorModal = new ErrorModal(CreateExpenseActivity.this);
            errorModal.setMessage(getResources().getString(R.string.provide_amount));
            errorModal.show();
            return;
        }

        Expense expense = new Expense();
        expense.setAmount(amountValue);
        expense.setCategory(category);
        expense.setCreatedAt(selectedDateValue);
        if (!titleValue.isEmpty()) expense.setName(titleValue);
        if (!marketNameValue.isEmpty()) expense.setMarketName(marketNameValue);

        viewModel.saveExpense(expense);
    }

    private void handleDatabaseResponse(DatabaseResponse databaseResponse) {
        if (databaseResponse == null) return;

        if (databaseResponse.getResponseCode() == DatabaseResponse.EXPENSE_CREATED) {
            SuccessModal successModal = new SuccessModal(CreateExpenseActivity.this, (SuccessModal.OnSuccessModalChangeListener) this::finish);
            successModal.setMessage(getResources().getString(R.string.expense_saved));
            successModal.show();
        }
    }

    private void setupSave() {
        save.setOnClickListener(v -> saveExpense());
    }

    private void updateDateValue(Date dateValue) {
        if (dateValue != null) {
            selectedDateValue = dateValue;
            date.setText(DateUtils.formatDate(dateValue));
        }
    }

    private void setupMarketName(String[] marketNames) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, marketNames);
        marketName.setAdapter(adapter);
    }

    private String[] parseMarkets(List<Market> markets) {
        String[] marketNames = new String[markets.size()];
        for (int i=0; i<markets.size(); i++) {
            marketNames[i] = markets.get(i).getMarketName();
        }

        return marketNames;
    }

    private List<SelectableCategory> parseCategories(List<Category> categories) {
        List<SelectableCategory> selectableCategories = new ArrayList<>();

        for (Category category : categories) {
            SelectableCategory selectableCategory = new SelectableCategory(category, false);
            selectableCategories.add(selectableCategory);
            if (selectableCategories.size() > 0) selectableCategories.get(0).setSelected(true);
        }

        return selectableCategories;
    }

    private class SelectableCategory {
        Category category;
        boolean selected;

        SelectableCategory(Category category, boolean selected) {
            this.category = category;
            this.selected = selected;
        }

        boolean isSelected() {
            return selected;
        }

        void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

    private class SelectableCategoryAdapter extends RecyclerView.Adapter<SelectableCategoryAdapter.ViewHolder> {

        class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout container;
            ImageView icon;
            TextView title;

            ViewHolder(@NonNull View itemView) {
                super(itemView);

                container = itemView.findViewById(R.id.selectable_category_item_container);
                icon = itemView.findViewById(R.id.selectable_category_item_icon);
                title = itemView.findViewById(R.id.selectable_category_item_title);
            }
        }

        private Context context;
        private List<SelectableCategory> selectableCategories;

        SelectableCategoryAdapter(Context context, List<SelectableCategory> selectableCategories) {
            this.context = context;
            this.selectableCategories = selectableCategories;
        }

        @NonNull
        @Override
        public SelectableCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View selectableCategoryView = inflater.inflate(R.layout.selectable_category_item, parent, false);
            return new ViewHolder(selectableCategoryView);
        }

        @Override
        public void onBindViewHolder(@NonNull SelectableCategoryAdapter.ViewHolder holder, int position) {
            SelectableCategory selectableCategory = selectableCategories.get(position);

            holder.icon.setImageResource(ResourcesUtils.from(context).getDrawableResourceId(selectableCategory.category.getIconName()));
            holder.title.setText(selectableCategory.category.getName());
            holder.container.setBackgroundResource(selectableCategory.isSelected() ? R.drawable.background_selectable_category_selected : R.drawable.background_selectable_category_default);
            holder.container.setOnClickListener(v -> handleSelectCategory(position));
        }

        @Override
        public int getItemCount() {
            return selectableCategories.size();
        }

        void updateSelectableCategories(List<SelectableCategory> list) {
            selectableCategories.clear();
            selectableCategories.addAll(list);
            notifyDataSetChanged();
        }

        private void handleSelectCategory(int position) {
            for (int i=0; i<selectableCategories.size(); i++) {
                if (i != position) selectableCategories.get(i).setSelected(false);
                else selectableCategories.get(i).setSelected(true);
            }
            notifyDataSetChanged();
        }

        Category getSelectedCategory() {
            Category category = selectableCategories.get(0).category;
            for (SelectableCategory selectableCategory : selectableCategories) {
                if (selectableCategory.isSelected()) {
                    category = selectableCategory.category;
                    break;
                }
            }

            return category;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        realm.close();
        EventBus.unregister(this);
    }
}
