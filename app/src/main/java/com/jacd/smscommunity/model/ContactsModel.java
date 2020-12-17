package com.jacd.smscommunity.model;

import com.jacd.smscommunity.common.Items;

public class ContactsModel implements Items {
    private int id;
    private String name;
    private String number;
    private String topic;
    private String message;
    private String date;

    public ContactsModel() {
    }

    public ContactsModel(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public ContactsModel(int id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    public ContactsModel(int id, String name, String number, String topic) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.topic = topic;
    }

    public ContactsModel(int id, String name, String number, String topic, String date) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.topic = topic;
        this.date = date;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int getViewType() {
        return 1;
    }
}
