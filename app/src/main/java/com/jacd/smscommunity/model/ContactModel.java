package com.jacd.smscommunity.model;

import com.jacd.smscommunity.common.Items;

public class ContactModel implements Items {
    private int id;
    private String name;

    public ContactModel() {
    }

    public ContactModel(String name) {
        this.name = name;
    }

    public ContactModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getViewType() {
        return 3;
    }
}
