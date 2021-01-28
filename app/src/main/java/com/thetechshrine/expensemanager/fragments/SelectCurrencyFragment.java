package com.thetechshrine.expensemanager.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.thetechshrine.expensemanager.R;
import com.thetechshrine.expensemanager.adapters.CurrencyItemAdapter;
import com.thetechshrine.expensemanager.payloads.CurrencyItem;
import com.thetechshrine.expensemanager.utils.Event;
import com.thetechshrine.expensemanager.utils.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectCurrencyFragment extends BottomSheetDialogFragment {

    private Context context;

    private RecyclerView recyclerView;
    private Button save;

    private CurrencyItemAdapter currencyItemAdapter;

    public SelectCurrencyFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_select_currency, container, false);
        bindViews(rootView);
        setupViews();

        return rootView;
    }

    private void bindViews(View rootView) {
        recyclerView = rootView.findViewById(R.id.select_currency_recycler_view);
        save = rootView.findViewById(R.id.select_currency_save);
    }

    private void setupViews() {
        setupRecyclerView();
        setupSave();
    }

    private void setupRecyclerView() {
        currencyItemAdapter = new CurrencyItemAdapter(context, CurrencyItem.getCurrencyItems(context));
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setAdapter(currencyItemAdapter);
    }

    private void setupSave() {
        save.setOnClickListener(v -> {
            CurrencyItem currencyItem = currencyItemAdapter.getSelectedCurrencyItem();
            EventBus.publish(EventBus.SUBJECT_PROFILE_FRAGMENT, new Event(Event.CURRENCY_SELECTED, currencyItem));
            dismiss();
        });
    }
}
