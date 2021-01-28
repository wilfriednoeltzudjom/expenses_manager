package com.thetechshrine.expensemanager.fragments.main;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.thetechshrine.expensemanager.R;
import com.thetechshrine.expensemanager.database.SessionManager;
import com.thetechshrine.expensemanager.fragments.SelectCurrencyFragment;
import com.thetechshrine.expensemanager.payloads.CurrencyItem;
import com.thetechshrine.expensemanager.utils.Event;
import com.thetechshrine.expensemanager.utils.EventBus;

public class ProfileFragment extends Fragment {

    private Context context;
    private SessionManager sessionManager;

    private TextView currency;
    private ImageButton editCurrency;

    private ProfileViewModel viewModel;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();
        sessionManager = SessionManager.from(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);
        bindViews(rootView);
        setupViews();
        subscribeToBus();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();

        initCurrency();
    }

    private void bindViews(View rootView) {
        currency = rootView.findViewById(R.id.profile_currency);
        editCurrency = rootView.findViewById(R.id.profile_edit_currency);
    }

    private void setupViews() {
        setupEditCurrency();
    }

    private void subscribeToBus() {
        EventBus.subscribe(EventBus.SUBJECT_PROFILE_FRAGMENT, this, o -> {
            if (o instanceof Event) {
                Event event = (Event) o;
                switch (event.getSubject()) {
                    case Event.CURRENCY_SELECTED:
                        updateCurrency((CurrencyItem) event.getData());
                        break;
                }
            }
        });
    }

    private void setupEditCurrency() {
        editCurrency.setOnClickListener(v -> {
            SelectCurrencyFragment selectCurrencyFragment = new SelectCurrencyFragment();
            if (getActivity() != null) {
                selectCurrencyFragment.show(getActivity().getSupportFragmentManager(), selectCurrencyFragment.getTag());
            }
        });
    }

    private void initCurrency() {
        String currencyValue = sessionManager.getCurrencyName() + " (" +sessionManager.getCurrencyCode()+ ")";
        currency.setText(currencyValue);
    }

    private void updateCurrency(CurrencyItem currencyItem) {
        sessionManager.setCurrencyCode(currencyItem.getCode());
        sessionManager.setCurrencyName(currencyItem.getName());
        initCurrency();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBus.unregister(this);
    }
}
