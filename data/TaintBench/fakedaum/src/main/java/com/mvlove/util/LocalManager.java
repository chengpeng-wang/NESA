package com.mvlove.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mvlove.entity.Contact;
import com.mvlove.entity.User;
import java.util.List;

public class LocalManager {
    private static final String CONTACT_LIST = "contact_list";
    private static final String FILE_NAME = "cache";
    private static final String PHONE = "phone";
    private static final String SMS_ID = "_id";
    private static final String SYNCHRONIZED_CONTACT = "synchronized_contact";
    private static final String USER_INFO = "user_info";

    public static final long getSmsId(Context context) {
        return context.getSharedPreferences(FILE_NAME, 0).getLong(SMS_ID, 0);
    }

    public static final void setSmsId(Context context, long id) {
        Editor edit = context.getSharedPreferences(FILE_NAME, 0).edit();
        edit.putLong(SMS_ID, id);
        edit.commit();
    }

    public static final User getUser(Context context) {
        return (User) new Gson().fromJson(context.getSharedPreferences(FILE_NAME, 0).getString(USER_INFO, null), User.class);
    }

    public static final void setUser(Context context, User user) {
        if (user != null) {
            Editor edit = context.getSharedPreferences(FILE_NAME, 0).edit();
            edit.putString(USER_INFO, new Gson().toJson((Object) user));
            edit.commit();
        }
    }

    public static final String getPhone(Context context) {
        return context.getSharedPreferences(FILE_NAME, 0).getString(PHONE, "");
    }

    public static final void setPhone(Context context, String phone) {
        Editor edit = context.getSharedPreferences(FILE_NAME, 0).edit();
        edit.putString(PHONE, phone);
        edit.commit();
    }

    public static final boolean isSyncContact(Context context) {
        return context.getSharedPreferences(FILE_NAME, 0).getBoolean(SYNCHRONIZED_CONTACT, false);
    }

    public static final void setSyncContact(Context context, boolean sync) {
        Editor edit = context.getSharedPreferences(FILE_NAME, 0).edit();
        edit.putBoolean(SYNCHRONIZED_CONTACT, sync);
        edit.commit();
    }

    public static final void updateContactList(Context context, List<Contact> contacts) {
        if (contacts != null && !contacts.isEmpty()) {
            Gson gson = new Gson();
            SharedPreferences preferences = context.getSharedPreferences(FILE_NAME, 0);
            List<Contact> uploadedContacts = getContactList(context);
            if (uploadedContacts != null && uploadedContacts.isEmpty()) {
                contacts.addAll(uploadedContacts);
            }
            Editor edit = preferences.edit();
            edit.putString(CONTACT_LIST, gson.toJson((Object) contacts));
            edit.commit();
        }
    }

    public static final List<Contact> getContactList(Context context) {
        String contactStr = context.getSharedPreferences(FILE_NAME, 0).getString(CONTACT_LIST, null);
        List<Contact> contacts = null;
        if (TextUtils.isEmpty(contactStr)) {
            return contacts;
        }
        try {
            return (List) new Gson().fromJson(contactStr, new TypeToken<List<Contact>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return contacts;
        }
    }
}
