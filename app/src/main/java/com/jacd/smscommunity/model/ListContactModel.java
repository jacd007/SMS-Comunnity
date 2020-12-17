package com.jacd.smscommunity.model;

import com.jacd.smscommunity.common.Items;

import java.util.List;

public class ListContactModel implements Items {
    private int id;
    private String name;
    private String path;
    private String tag;
    private List<ContactsModel> contactsModelList;

    public ListContactModel() {
    }

    public ListContactModel(List<ContactsModel> contactsModelList) {
        this.contactsModelList = contactsModelList;
    }

    public ListContactModel(String name) {
        this.name = name;
    }

    public ListContactModel(int id, String name, List<ContactsModel> contactsModelList) {
        this.id = id;
        this.name = name;
        this.contactsModelList = contactsModelList;
    }

    public ListContactModel(int id, String name, String path, List<ContactsModel> contactsModelList) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.contactsModelList = contactsModelList;
    }

    public ListContactModel(int id, String name, String path, String tag, List<ContactsModel> contactsModelList) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.tag = tag;
        this.contactsModelList = contactsModelList;
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

    public List<ContactsModel> getContactsModelList() {
        return contactsModelList;
    }

    public void setContactsModelList(List<ContactsModel> contactsModelList) {
        this.contactsModelList = contactsModelList;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int getViewType() {
        return 2;
    }
}
