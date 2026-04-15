package com.example.contactmanager;

public class Contact {
    private String contactname;
    private String contactnumber;
    private String phonenumber;

    public String getContactname() {
        return this.contactname;
    }

    public void setContactname(String contactname) {
        this.contactname = contactname;
    }

    public String getContactnumber() {
        return this.contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }

    public String getPhonenumber() {
        return this.phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String toString() {
        return "contactname=" + this.contactname + ",contactnumber=" + this.contactnumber + ",phonenumber=" + this.phonenumber;
    }
}
