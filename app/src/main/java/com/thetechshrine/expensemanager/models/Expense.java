package com.thetechshrine.expensemanager.models;

import com.thetechshrine.expensemanager.utils.IdGenerator;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Expense extends RealmObject {

    public static String PROPERTY_ID = "id";
    public static String PROPERTY_CREATED_AT = "createdAt";
    public static String PROPERTY_AMOUNT = "amount";

    @Required
    @PrimaryKey
    private String id;

    private Date createdAt;
    private Date updatedAt;
    private String name;
    private double amount;
    private String description;
    private int quantity;
    private String marketName;
    private RealmList<String> pictures;

    private Category category;

    public Expense() {
        id = IdGenerator.generateId();
        createdAt = new Date();
        updatedAt = new Date();
    }

    public Expense(Date createdAt, double amount) {
        this.createdAt = createdAt;
        this.amount = amount;
    }

    public Expense(Date createdAt, double amount, Category category) {
        this.createdAt = createdAt;
        this.amount = amount;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public RealmList<String> getPictures() {
        return pictures;
    }

    public void setPictures(RealmList<String> pictures) {
        this.pictures = pictures;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
