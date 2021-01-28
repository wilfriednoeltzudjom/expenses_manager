package com.thetechshrine.expensemanager.models;

import com.thetechshrine.expensemanager.utils.IdGenerator;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Market extends RealmObject {

    @PrimaryKey
    @Required
    private String id;

    private Date createdAt;
    private String marketName;

    public Market() {}

    private void initFields() {
        id = IdGenerator.generateId();
        createdAt = new Date();
    }

    public Market(String marketName) {
        initFields();
        this.marketName = marketName;
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

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }
}
