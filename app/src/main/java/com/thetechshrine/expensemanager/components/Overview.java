package com.thetechshrine.expensemanager.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thetechshrine.expensemanager.R;
import com.thetechshrine.expensemanager.database.SessionManager;
import com.thetechshrine.expensemanager.models.Expense;
import com.thetechshrine.expensemanager.utils.DateUtils;
import com.thetechshrine.expensemanager.utils.EntitiesUtils;
import com.thetechshrine.expensemanager.utils.TypefaceUtils;
import com.thetechshrine.expensemanager.utils.modals.ExpenseModal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Overview extends LinearLayout {

    private Context globalContext;

    private TextView date;
    private TextView outcome;
    private TextView currency;
    private RecyclerView recyclerView;

    private OverviewAdapter adapter;
    private List<OverviewItem> overviewItems;

    public Overview(Context context) {
        super(context);
    }

    public Overview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        globalContext = context;


        bindViews();
        setupViews();
    }

    private void bindViews() {
        LayoutInflater.from(globalContext).inflate(R.layout.overview, this, true);

        date = findViewById(R.id.overview_date);
        outcome = findViewById(R.id.overview_outcome);
        currency = findViewById(R.id.overview_currency);
        recyclerView = findViewById(R.id.overview_recycler_view);
    }

    private void setupViews() {
        date.setText(EntitiesUtils.from(globalContext).capitalize(DateUtils.formatMonthYear(new Date())));
        outcome.setTypeface(TypefaceUtils.from(globalContext).getSemiBoldTypeface());
        currency.setTypeface(TypefaceUtils.from(globalContext).getSemiBoldTypeface());
        currency.setText(SessionManager.from(globalContext).getCurrencyCode());

        overviewItems = new ArrayList<>();

        adapter = new OverviewAdapter(globalContext, overviewItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(globalContext, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(overviewItems.size() - 1);
    }

    private class OverviewItem {
        private Date date;
        private double amount;

        OverviewItem(Date date, double amount) {
            this.date = date;
            this.amount = amount;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public double getAmount() {
            return amount;
        }
    }

    private class OverviewAdapter extends RecyclerView.Adapter<OverviewAdapter.ViewHolder> {

        class ViewHolder extends RecyclerView.ViewHolder {

            LinearLayout container;
            LinearLayout amountContainer;
            LinearLayout amount;
            TextView date;

            ViewHolder(@NonNull View itemView) {
                super(itemView);

                container = itemView.findViewById(R.id.overview_item_container);
                amountContainer = itemView.findViewById(R.id.overview_item_amount_container);
                amount = itemView.findViewById(R.id.overview_item_amount);
                date = itemView.findViewById(R.id.overview_item_date);
            }
        }

        private Context context;
        private List<OverviewItem> overviewItems;

        private OverviewAdapter(Context context, List<OverviewItem> overviewItems) {
            this.context = context;
            this.overviewItems = overviewItems;
        }

        @NonNull
        @Override
        public OverviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View overviewItemView = inflater.inflate(R.layout.overview_item, parent, false);
            return new ViewHolder(overviewItemView);
        }

        @Override
        public void onBindViewHolder(@NonNull OverviewAdapter.ViewHolder holder, int position) {
            OverviewItem overviewItem = overviewItems.get(position);

            double maxAmount = getMaxAmount();
            if (maxAmount == overviewItem.amount) {
                holder.amount.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 100));
            } else {
                int weight = (int) Math.round(100 * overviewItem.amount / maxAmount);
                holder.amount.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, weight));
            }
            holder.date.setText(DateUtils.formatDayMonth(overviewItem.getDate(), false));

            holder.container.setOnClickListener(v -> {
                ExpenseModal expenseModal = new ExpenseModal(context);
                expenseModal.setValues(overviewItem.getDate(), overviewItem.getAmount());
                expenseModal.show();
            });

            if (isLastItem(position)) {
                holder.date.setTextColor(getResources().getColor(R.color.colorWhite));
                holder.amount.setBackgroundResource(R.drawable.background_overview_item_green);
            } else {
                holder.date.setTextColor(getResources().getColor(R.color.colorDarkGray));
                holder.amount.setBackgroundResource(R.drawable.background_overview_item_shadow_gray);
            }
        }

        @Override
        public int getItemCount() {
            return overviewItems.size();
        }

        private boolean isLastItem(int position) {
            return overviewItems.size() - 1 == position;
        }

        private double getMaxAmount() {
            double max = overviewItems.get(0).amount;
            for (int i=0; i<overviewItems.size(); i++) {
                if (overviewItems.get(i).amount > max) max = overviewItems.get(i).amount;
            }

            return max;
        }

        private void setOverviewItems(List<OverviewItem> overviewItems) {
            this.overviewItems = overviewItems;
            notifyDataSetChanged();
        }
    }

    private boolean isDateAlreadyIncludedInList(Date date, List<OverviewItem> items) {
        boolean included = false;

        for (OverviewItem overviewItem : items) {
            if (DateUtils.isSameDate(date, overviewItem.getDate())) included = true;
        }

        return included;
    }

    private  List<OverviewItem> parseExpenses(List<Expense> expenses) {
        List<OverviewItem> items = new ArrayList<>();

        for (int i=0; i<expenses.size(); i++) {
            double amount = expenses.get(i).getAmount();
            Date current = expenses.get(i).getCreatedAt();
            if (isDateAlreadyIncludedInList(current, items)) continue;

            for (int j=i+1; j<expenses.size(); j++) {
                if (DateUtils.isSameDate(current, expenses.get(j).getCreatedAt())) {
                    amount += expenses.get(j).getAmount();
                }
            }

            items.add(new OverviewItem(current, amount));
        }

        Collections.reverse(items);

        return items;
    }

    public void setExpenses(List<Expense> expenses) {
        adapter.setOverviewItems(parseExpenses(expenses));
    }

    public void setOutcome(double outcomeValue) {
        outcome.setText(EntitiesUtils.from(globalContext).formatAmount(outcomeValue, false, false));
    }
}
