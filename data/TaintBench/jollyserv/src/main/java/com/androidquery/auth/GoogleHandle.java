package com.androidquery.auth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.androidquery.AQuery;
import com.androidquery.callback.AbstractAjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.AQUtility;
import com.androidquery.util.Constants;
import java.io.IOException;
import org.apache.http.HttpRequest;

public class GoogleHandle extends AccountHandle implements OnClickListener, OnCancelListener {
    /* access modifiers changed from: private */
    public Account acc;
    private Account[] accs;
    /* access modifiers changed from: private */
    public Activity act;
    /* access modifiers changed from: private */
    public AccountManager am;
    private String email;
    /* access modifiers changed from: private */
    public String token;
    /* access modifiers changed from: private */
    public String type;

    private class Task extends AsyncTask<String, String, Bundle> {
        private Task() {
        }

        /* synthetic */ Task(GoogleHandle googleHandle, Task task) {
            this();
        }

        /* access modifiers changed from: protected|varargs */
        public Bundle doInBackground(String... params) {
            Bundle bundle = null;
            try {
                return (Bundle) GoogleHandle.this.am.getAuthToken(GoogleHandle.this.acc, GoogleHandle.this.type, null, GoogleHandle.this.act, null, null).getResult();
            } catch (OperationCanceledException e) {
                return bundle;
            } catch (AuthenticatorException e2) {
                AQUtility.debug(e2);
                return bundle;
            } catch (IOException e22) {
                AQUtility.debug(e22);
                return bundle;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Bundle bundle) {
            if (bundle == null || !bundle.containsKey("authtoken")) {
                GoogleHandle.this.failure(GoogleHandle.this.act, AjaxStatus.AUTH_ERROR, "rejected");
                return;
            }
            GoogleHandle.this.token = bundle.getString("authtoken");
            GoogleHandle.this.success(GoogleHandle.this.act);
        }
    }

    public GoogleHandle(Activity act, String type, String email) {
        if (Constants.ACTIVE_ACCOUNT.equals(email)) {
            email = getActiveAccount(act);
        }
        this.act = act;
        this.type = type.substring(2);
        this.email = email;
        this.am = AccountManager.get(act);
    }

    /* access modifiers changed from: protected */
    public void auth() {
        if (this.email == null) {
            accountDialog();
            return;
        }
        Account[] accounts = this.am.getAccountsByType("com.google");
        for (Account account : accounts) {
            if (this.email.equals(account.name)) {
                auth(account);
                return;
            }
        }
    }

    public boolean reauth(AbstractAjaxCallback<?, ?> abstractAjaxCallback) {
        this.am.invalidateAuthToken(this.acc.type, this.token);
        try {
            this.token = this.am.blockingGetAuthToken(this.acc, this.type, true);
            AQUtility.debug("re token", this.token);
        } catch (Exception e) {
            AQUtility.debug(e);
            this.token = null;
        }
        if (this.token != null) {
            return true;
        }
        return false;
    }

    public String getType() {
        return this.type;
    }

    private void accountDialog() {
        Builder builder = new Builder(this.act);
        this.accs = this.am.getAccountsByType("com.google");
        int size = this.accs.length;
        if (size == 1) {
            auth(this.accs[0]);
            return;
        }
        String[] names = new String[size];
        for (int i = 0; i < size; i++) {
            names[i] = this.accs[i].name;
        }
        builder.setItems(names, this);
        builder.setOnCancelListener(this);
        new AQuery(this.act).show(builder.create());
    }

    public void onClick(DialogInterface dialog, int which) {
        Account acc = this.accs[which];
        AQUtility.debug("acc", acc.name);
        setActiveAccount(this.act, acc.name);
        auth(acc);
    }

    public static void setActiveAccount(Context context, String account) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(Constants.ACTIVE_ACCOUNT, account).commit();
    }

    public static String getActiveAccount(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.ACTIVE_ACCOUNT, null);
    }

    private void auth(Account account) {
        this.acc = account;
        new Task(this, null).execute(new String[0]);
    }

    public void onCancel(DialogInterface dialog) {
        failure(this.act, AjaxStatus.AUTH_ERROR, "cancel");
    }

    public boolean expired(AbstractAjaxCallback<?, ?> abstractAjaxCallback, AjaxStatus status) {
        int code = status.getCode();
        return code == 401 || code == 403;
    }

    public void applyToken(AbstractAjaxCallback<?, ?> abstractAjaxCallback, HttpRequest request) {
        request.addHeader("Authorization", "GoogleLogin auth=" + this.token);
    }

    public String getCacheUrl(String url) {
        return new StringBuilder(String.valueOf(url)).append("#").append(this.token).toString();
    }

    public boolean authenticated() {
        return this.token != null;
    }
}
