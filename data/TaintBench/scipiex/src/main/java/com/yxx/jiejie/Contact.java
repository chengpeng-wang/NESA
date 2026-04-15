package com.yxx.jiejie;

import android.graphics.Bitmap;
import java.io.Serializable;

public class Contact implements Serializable {
    private String C_id;
    private Bitmap bm;
    private String email;
    private boolean flag;
    private boolean isCheck;
    private boolean isDel;
    private boolean isName;
    private boolean isPhone;
    private String name;
    private String[] phone;
    private String rawContactsId;
    private boolean visible;

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Bitmap getBm() {
        return this.bm;
    }

    public void setBm(Bitmap bm) {
        this.bm = bm;
    }

    public String getRawContactsId() {
        return this.rawContactsId;
    }

    public void setRawContactsId(String rawContactsId) {
        this.rawContactsId = rawContactsId;
    }

    public boolean isName() {
        return this.isName;
    }

    public void setName(boolean isName) {
        this.isName = isName;
    }

    public boolean isPhone() {
        return this.isPhone;
    }

    public void setPhone(boolean isPhone) {
        this.isPhone = isPhone;
    }

    public boolean isDel() {
        return this.isDel;
    }

    public void setDel(boolean isDel) {
        this.isDel = isDel;
    }

    public boolean isCheck() {
        return this.isCheck;
    }

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public boolean isFlag() {
        return this.flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getC_id() {
        return this.C_id;
    }

    public void setC_id(String cId) {
        this.C_id = cId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String[] getPhone() {
        return this.phone;
    }

    public void setPhone(String[] phone) {
        this.phone = phone;
    }

    public String getPhoneString() {
        String str = "";
        for (int i = 0; i < this.phone.length; i++) {
            if (this.phone[i] != null) {
                str = new StringBuilder(String.valueOf(str)).append(this.phone[i]).append("|").toString();
            }
        }
        return str;
    }
}
