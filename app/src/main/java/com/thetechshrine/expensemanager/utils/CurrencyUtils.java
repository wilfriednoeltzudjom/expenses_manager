package com.thetechshrine.expensemanager.utils;

import android.content.Context;

import com.thetechshrine.expensemanager.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrencyUtils {

    private Context context;
    private Map<String, String> currencies;

    public CurrencyUtils(Context context) {
        this.context = context;
        initCurrencies();
    }

    public static CurrencyUtils from(Context context) {
        return new CurrencyUtils(context);
    }

    private void initCurrencies() {
        currencies = new HashMap<>();
        currencies.put("€", context.getResources().getString(R.string.euro));
        currencies.put("$", context.getResources().getString(R.string.dollar));
        currencies.put("XAF", context.getResources().getString(R.string.cfa_central));
        currencies.put("XOF", context.getResources().getString(R.string.cfa_west));
        currencies.put("¥", context.getResources().getString(R.string.yen));
        currencies.put("元", context.getResources().getString(R.string.yuan));
        currencies.put("₦", context.getResources().getString(R.string.naira));
        currencies.put("₩", context.getResources().getString(R.string.won));
    }

    public String getCurrencyName(String currencyCode) {
        return currencies.get(currencyCode);
    }

    public List<String> getCurrenciesCodes() {
        return Arrays.asList("€", "$", "¥", "XAF", "XOF", "₦", "元", "₩");
    }
}
