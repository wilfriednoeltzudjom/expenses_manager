package com.thetechshrine.expensemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thetechshrine.expensemanager.R;
import com.thetechshrine.expensemanager.payloads.CurrencyItem;

import java.util.List;

public class CurrencyItemAdapter extends RecyclerView.Adapter<CurrencyItemAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout container;
        TextView code;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.currency_item_container);
            code = itemView.findViewById(R.id.currency_item_currency_code);
            name = itemView.findViewById(R.id.currency_item_currency_name);
        }
    }

    private Context context;
    private List<CurrencyItem> currencyItems;

    public CurrencyItemAdapter(Context context, List<CurrencyItem> currencyItems) {
        this.context = context;
        this.currencyItems = currencyItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View currencyItemView = inflater.inflate(R.layout.currency_item, parent, false);
        return new ViewHolder(currencyItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CurrencyItem currencyItem = currencyItems.get(position);

        holder.code.setTextColor(currencyItem.isSelected() ? context.getResources().getColor(R.color.colorWhite) : context.getResources().getColor(R.color.colorBlack));
        holder.code.setText(currencyItem.getCode());
        holder.name.setTextColor(currencyItem.isSelected() ? context.getResources().getColor(R.color.colorLightDark) : context.getResources().getColor(R.color.colorGray));
        holder.name.setText(currencyItem.getName());
        holder.container.setBackgroundResource(currencyItem.isSelected() ? R.drawable.background_selectable_category_selected : R.drawable.background_selectable_category_default);
        holder.container.setOnClickListener(v -> handleCurrencyItemSelected(position));
    }

    @Override
    public int getItemCount() {
        return currencyItems.size();
    }

    private void handleCurrencyItemSelected(int position) {
        for (int i=0; i<currencyItems.size(); i++) {
            if (i != position) currencyItems.get(i).setSelected(false);
            else currencyItems.get(i).setSelected(true);
        }
        notifyDataSetChanged();
    }

    public CurrencyItem getSelectedCurrencyItem() {
        CurrencyItem selectedCurrencyItem = currencyItems.get(0);
        for (CurrencyItem currencyItem : currencyItems) {
            if (currencyItem.isSelected()) {
                selectedCurrencyItem = currencyItem;
                break;
            }
        }

        return selectedCurrencyItem;
    }
}
