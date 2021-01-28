package com.thetechshrine.expensemanager.payloads;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.thetechshrine.expensemanager.utils.CurrencyUtils;

import java.util.ArrayList;
import java.util.List;

public class CurrencyItem implements Parcelable {

    private String code;
    private String name;
    private boolean selected;

    public CurrencyItem(String code, String name) {
        this.code = code;
        this.name = name;
    }

    protected CurrencyItem(Parcel in) {
        code = in.readString();
        name = in.readString();
        selected = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(name);
        dest.writeByte((byte) (selected ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CurrencyItem> CREATOR = new Creator<CurrencyItem>() {
        @Override
        public CurrencyItem createFromParcel(Parcel in) {
            return new CurrencyItem(in);
        }

        @Override
        public CurrencyItem[] newArray(int size) {
            return new CurrencyItem[size];
        }
    };

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public static List<CurrencyItem> getCurrencyItems(Context context) {
        List<CurrencyItem> currencyItems = new ArrayList<>();
        CurrencyUtils currencyUtils = CurrencyUtils.from(context);
        List<String> currenciesCodes = currencyUtils.getCurrenciesCodes();
        for (String currencyCode : currenciesCodes) {
            currencyItems.add(new CurrencyItem(currencyCode, currencyUtils.getCurrencyName(currencyCode)));
        }
        if (currencyItems.size() > 0) currencyItems.get(0).setSelected(true);

        return currencyItems;
    }
}
