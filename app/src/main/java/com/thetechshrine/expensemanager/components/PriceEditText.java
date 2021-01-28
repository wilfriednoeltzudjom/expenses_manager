package com.thetechshrine.expensemanager.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thetechshrine.expensemanager.R;

import java.util.Arrays;
import java.util.List;

public class PriceEditText extends LinearLayout {

    private Context globalContext;

    private RecyclerView recyclerView;
    private EditText price;

    public PriceEditText(Context context) {
        super(context);
    }

    public PriceEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        globalContext = context;

        bindViews();
        setupViews();
    }

    private void bindViews() {
        LayoutInflater.from(globalContext).inflate(R.layout.price_edit_text, this, true);

        recyclerView = findViewById(R.id.price_edit_text_recycler_view);
        price = findViewById(R.id.price_edit_text_price);
    }

    private void setupViews() {
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        List<KeyBoardItem> keyBoardItems = Arrays.asList(
                new KeyBoardItem("7", false),
                new KeyBoardItem("8", false),
                new KeyBoardItem("9", false),
                new KeyBoardItem("4", false),
                new KeyBoardItem("5", false),
                new KeyBoardItem("6", false),
                new KeyBoardItem("1", false),
                new KeyBoardItem("2", false),
                new KeyBoardItem("3", false),
                new KeyBoardItem(".", false),
                new KeyBoardItem("0", false),
                new KeyBoardItem("", true)
        );
        KeyBoardAdapter adapter = new KeyBoardAdapter(globalContext, keyBoardItems);
        recyclerView.setLayoutManager(new GridLayoutManager(globalContext, 3));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
    }

    private class KeyBoardItem {
        String value;
        boolean action;

        public KeyBoardItem(String value, boolean action) {
            this.value = value;
            this.action = action;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public boolean isAction() {
            return action;
        }
    }

    private class KeyBoardAdapter extends RecyclerView.Adapter<KeyBoardAdapter.ViewHolder> {

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView value;
            ImageButton clear;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                value = itemView.findViewById(R.id.price_edit_text_recycler_view_item_value);
                clear = itemView.findViewById(R.id.price_edit_text_recycler_view_item_clear);
            }
        }

        private Context context;
        private List<KeyBoardItem> keyBoardItems;

        public KeyBoardAdapter(Context context, List<KeyBoardItem> keyBoardItems) {
            this.context = context;
            this.keyBoardItems = keyBoardItems;
        }

        @NonNull
        @Override
        public KeyBoardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View keyBoardItemView = inflater.inflate(R.layout.price_edit_text_recycler_view_item, parent, false);
            return new ViewHolder(keyBoardItemView);
        }

        @Override
        public void onBindViewHolder(@NonNull KeyBoardAdapter.ViewHolder holder, int position) {
            KeyBoardItem keyBoardItem = keyBoardItems.get(position);

            if (keyBoardItem.isAction()) holder.value.setVisibility(View.GONE);
            else {
                holder.clear.setVisibility(View.GONE);
                holder.value.setText(keyBoardItem.getValue());
            }

            holder.clear.setOnClickListener(v -> {
                String priceValue = price.getText().toString();
                if (priceValue.length() > 0) price.getText().delete(priceValue.length() - 1, priceValue.length());
            });
            holder.value.setOnClickListener(v -> {
                String priceValue = price.getText().toString();
                if (!(priceValue.equals("") && (keyBoardItem.value.equals("0") || keyBoardItem.value.equals(".")))) {
                    price.append(keyBoardItem.getValue());
                }
            });

        }

        @Override
        public int getItemCount() {
            return keyBoardItems.size();
        }
    }

    public double getValue() {
        String priceValue = price.getText().toString();
        if (priceValue.equals("")) priceValue = "0";

        return Double.parseDouble(priceValue);
    }
}
