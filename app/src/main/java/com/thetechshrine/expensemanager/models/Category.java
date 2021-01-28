package com.thetechshrine.expensemanager.models;

import com.thetechshrine.expensemanager.utils.IdGenerator;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Category extends RealmObject {

    public static String PROPERTY_ID = "id";
    public static String PROPERTY_NAME = "name";

    @Required
    @PrimaryKey
    private String id;

    private Date createdAt;
    private Date updatedAt;
    private String name;
    private String description;
    private int icon;
    private String iconName;
    private double budgetAmount;
    private boolean editable;

    private Category parent;

    public Category() {}

    private void initFields() {
        id = IdGenerator.generateId();
        createdAt = new Date();
        updatedAt = new Date();
    }

    public Category(int icon) {
        this.icon = icon;
    }

    public Category(int icon, String iconName) {
        this.icon = icon;
        this.iconName = iconName;
    }

    public Category(String name, int icon) {
        initFields();
        this.name = name;
        this.icon = icon;
    }

    public Category(String name, int icon, boolean editable) {
        initFields();
        this.name = name;
        this.icon = icon;
        this.editable = editable;
    }

    public Category(String name, int icon, String iconName, boolean editable) {
        initFields();
        this.name = name;
        this.icon = icon;
        this.iconName = iconName;
        this.editable = editable;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public double getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(double budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }
}
