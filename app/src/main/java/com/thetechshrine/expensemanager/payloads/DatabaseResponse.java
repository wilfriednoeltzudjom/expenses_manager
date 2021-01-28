package com.thetechshrine.expensemanager.payloads;

public class DatabaseResponse {

    public static final int EXPENSE_CREATED = 1;
    public static final int CATEGORY_CREATED = 2;
    public static final int CATEGORY_DELETED = 3;
    public static final int EXPENSE_DELETED = 3;

    private int responseCode;

    public DatabaseResponse() {
    }

    public DatabaseResponse(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
