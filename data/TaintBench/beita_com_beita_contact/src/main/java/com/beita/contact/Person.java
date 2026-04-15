package com.beita.contact;

import java.util.ArrayList;
import java.util.List;

public class Person {
    private String address;
    private List<String> emails = new ArrayList();
    private String id;
    private String name;
    private List<String> phones = new ArrayList();

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPhones() {
        return this.phones;
    }

    public List<String> getEmails() {
        return this.emails;
    }

    public void addEmails(String email) {
        this.emails.add(email);
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void addPhones(String phone) {
        this.phones.add(phone);
    }

    public String getID() {
        return this.id;
    }

    public void setID(String _id) {
        this.id = _id;
    }
}
