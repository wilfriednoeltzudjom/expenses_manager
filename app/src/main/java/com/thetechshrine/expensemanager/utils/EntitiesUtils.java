package com.thetechshrine.expensemanager.utils;

import android.content.Context;

import com.thetechshrine.expensemanager.database.SessionManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntitiesUtils {

    private Context context;

    public EntitiesUtils(Context context) {
        this.context = context;
    }

    public static EntitiesUtils from(Context context) {
        return new EntitiesUtils(context);
    }

    private String reverseString(String value) {
        StringBuilder builder = new StringBuilder();
        builder.append(value);

        return builder.reverse().toString();
    }

    private List<String> extractThreeDigitsGroups(String value) {
        List<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d{3}");
        Matcher matcher = pattern.matcher(value);
        while (matcher.find()) result.add(matcher.group());

        return result;
    }

    private String joinListItem(List<String> items) {
        StringBuilder builder = new StringBuilder();
        for (String item : items) builder.append(item);

        return builder.toString();
    }

    public String formatAmount(double amount, boolean withCurrency, boolean withMinus) {
        String formattedAmount;
        String currency = (withMinus ? "-" : "") + (withCurrency ? SessionManager.from(context).getCurrencyCode() : "");

        String amountString = new DecimalFormat("#.##").format(amount);
        String[] amountStringParts = amountString.contains(".") ? amountString.split("\\.") : amountString.split(",");
        formattedAmount = amountStringParts[0];
        if (formattedAmount.length() > 3) {
            String reversedFormattedAmount = reverseString(formattedAmount);
            List<String> threeDigitsGroups = extractThreeDigitsGroups(reversedFormattedAmount);
            String restWithoutThreeDigits = reversedFormattedAmount.substring(joinListItem(threeDigitsGroups).length());

            StringBuilder builder = new StringBuilder();
            builder.append(reverseString(restWithoutThreeDigits));
            for (String threeDigitsGroup : threeDigitsGroups) {
                builder.append(",");
                builder.append(reverseString(threeDigitsGroup));
            }
            formattedAmount = builder.toString();
        }
        String decimal = amountStringParts.length > 1 ? amountStringParts[1] : "00";

        return currency+formattedAmount+"."+decimal;
    }

    public String capitalize(String value) {
        if (value == null) return "";

        StringBuilder capitalized = new StringBuilder();
        capitalized.append((""+value.charAt(0)).toUpperCase());
        if (value.length() > 1) capitalized.append(value.substring(1).toLowerCase());

        return capitalized.toString();
    }

    public boolean isValidString(String value) {
        return value != null && !value.isEmpty();
    }
}
