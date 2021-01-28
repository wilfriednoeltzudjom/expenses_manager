package com.thetechshrine.expensemanager.utils;

public class Event {

    public static final int DATE_SELECTED = 1;
    public static final int CREATE_CATEGORY = 2;
    public static final int DELETE_CATEGORY = 3;
    public static final int DELETE_EXPENSE = 4;
    public static final int CURRENCY_SELECTED = 5;

    private int subject;
    private Object data;

    public Event() {
    }

    public Event(int subject) {
        this.subject = subject;
    }

    public Event(int subject, Object data) {
        this.subject = subject;
        this.data = data;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
