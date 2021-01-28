package com.thetechshrine.expensemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thetechshrine.expensemanager.R;
import com.thetechshrine.expensemanager.models.Expense;
import com.thetechshrine.expensemanager.utils.DateUtils;
import com.thetechshrine.expensemanager.utils.EntitiesUtils;
import com.thetechshrine.expensemanager.utils.Event;
import com.thetechshrine.expensemanager.utils.EventBus;
import com.thetechshrine.expensemanager.utils.ResourcesUtils;
import com.thetechshrine.expensemanager.utils.TypefaceUtils;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        ImageView icon;
        TextView title;
        TextView time;
        TextView amount;
        TextView marketName;
        LinearLayout deleteContainer;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.expense_item_date);
            icon = itemView.findViewById(R.id.expense_item_icon);
            title = itemView.findViewById(R.id.expense_item_title);
            time = itemView.findViewById(R.id.expense_item_time);
            amount = itemView.findViewById(R.id.expense_item_amount);
            marketName = itemView.findViewById(R.id.expense_item_market_name);
            deleteContainer = itemView.findViewById(R.id.expense_item_delete_container);
        }
    }

    private Context context;
    private List<Expense> expenses;

    public ExpenseAdapter(Context context, List<Expense> expenses) {
        this.context = context;
        this.expenses = expenses;
    }

    @NonNull
    @Override
    public ExpenseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View expenseView = inflater.inflate(R.layout.expense_item, parent, false);
        return new ViewHolder(expenseView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapter.ViewHolder holder, int position) {
        Expense expense = expenses.get(position);

        if (position > 0 && DateUtils.isSameDate(expense.getCreatedAt(), expenses.get(position - 1).getCreatedAt())) {
            holder.date.setVisibility(View.GONE);
        } else {
            String dateText;
            if (DateUtils.isToday(expense.getCreatedAt())) dateText = context.getResources().getString(R.string.today);
            else if (DateUtils.isYesterday(expense.getCreatedAt())) dateText = context.getResources().getString(R.string.yesterday);
            else dateText = DateUtils.formatDayMonth(expense.getCreatedAt(), true);

            holder.date.setVisibility(View.VISIBLE);
            holder.date.setText(dateText);
        }

        holder.title.setText(EntitiesUtils.from(context).isValidString(expense.getName()) ? expense.getName() : expense.getCategory().getName());

        if (expense.getMarketName() != null) {
            holder.marketName.setVisibility(View.VISIBLE);
            holder.marketName.setText(expense.getMarketName());
        }
        else holder.marketName.setVisibility(View.GONE);

        holder.amount.setTypeface(TypefaceUtils.from(context).getMediumTypeface());
        holder.amount.setText(EntitiesUtils.from(context).formatAmount(expense.getAmount(), true, true));

        holder.time.setText(DateUtils.formatHourMinute(expense.getCreatedAt()));

        holder.icon.setImageResource(ResourcesUtils.from(context).getDrawableResourceId(expense.getCategory().getIconName()));

        holder.deleteContainer.setOnClickListener(v -> {
            EventBus.publish(EventBus.SUBJECT_DISCOVER_FRAGMENT, new Event(Event.DELETE_EXPENSE, expense.getId()));
        });
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public void updateExpenses(List<Expense> list) {
        expenses.clear();
        expenses.addAll(list);
        notifyDataSetChanged();
    }
}
