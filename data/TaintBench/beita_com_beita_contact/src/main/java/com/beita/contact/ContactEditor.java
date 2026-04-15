package com.beita.contact;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ContactEditor extends Activity {
    private static final int DELETE_ID = 3;
    private static final int DISCARD_ID = 2;
    private static final int REVERT_ID = 1;
    private static final int STATE_EDIT = 0;
    private static final int STATE_INSERT = 1;
    private static final String TAG = "ContactEditor";
    private EditText addressText;
    private EditText blogText;
    private Button cancelButton;
    private EditText emailText;
    private EditText homeText;
    private Cursor mCursor;
    /* access modifiers changed from: private */
    public int mState;
    private Uri mUri;
    private EditText mobileText;
    /* access modifiers changed from: private */
    public EditText nameText;
    private Button okButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String action = intent.getAction();
        Log.e("ContactEditor:onCreate", action);
        if ("android.intent.action.EDIT".equals(action)) {
            this.mState = 0;
            this.mUri = intent.getData();
        } else if ("android.intent.action.INSERT".equals(action)) {
            this.mState = 1;
            this.mUri = getContentResolver().insert(intent.getData(), null);
            if (this.mUri == null) {
                Log.e("ContactEditor:onCreate", "Failed to insert new Contact into " + getIntent().getData());
                finish();
                return;
            }
            setResult(-1, new Intent().setAction(this.mUri.toString()));
        } else {
            Log.e("ContactEditor:onCreate", " unknown action");
            finish();
            return;
        }
        setContentView(R.layout.editorcontacts);
        this.nameText = (EditText) findViewById(R.id.EditText01);
        this.mobileText = (EditText) findViewById(R.id.EditText02);
        this.homeText = (EditText) findViewById(R.id.EditText03);
        this.addressText = (EditText) findViewById(R.id.EditText04);
        this.emailText = (EditText) findViewById(R.id.EditText05);
        this.blogText = (EditText) findViewById(R.id.EditText06);
        this.okButton = (Button) findViewById(R.id.Button01);
        this.cancelButton = (Button) findViewById(R.id.Button02);
        this.okButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ContactEditor.this.nameText.getText().toString().length() == 0) {
                    ContactEditor.this.setResult(0);
                    ContactEditor.this.deleteContact();
                    ContactEditor.this.finish();
                    return;
                }
                ContactEditor.this.updateContact();
            }
        });
        this.cancelButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ContactEditor.this.mState == 1) {
                    ContactEditor.this.setResult(0);
                    ContactEditor.this.deleteContact();
                    ContactEditor.this.finish();
                    return;
                }
                ContactEditor.this.backupContact();
            }
        });
        Log.e("ContactEditor:onCreate", this.mUri.toString());
        this.mCursor = managedQuery(this.mUri, ContactColumn.PROJECTION, null, null, null);
        this.mCursor.moveToFirst();
        Log.e(TAG, "end of onCreate()");
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.mCursor != null) {
            Log.e("ContactEditor:onResume", "count:" + this.mCursor.getColumnCount());
            this.mCursor.moveToFirst();
            if (this.mState == 0) {
                setTitle(getText(R.string.editor_user));
            } else if (this.mState == 1) {
                setTitle(getText(R.string.add_user));
            }
            String name = this.mCursor.getString(1);
            String moblie = this.mCursor.getString(2);
            String home = this.mCursor.getString(3);
            String address = this.mCursor.getString(4);
            String email = this.mCursor.getString(5);
            String blog = this.mCursor.getString(6);
            this.nameText.setText(name);
            this.mobileText.setText(moblie);
            this.homeText.setText(home);
            this.addressText.setText(address);
            this.emailText.setText(email);
            this.blogText.setText(blog);
            return;
        }
        setTitle("错误信息");
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        if (this.mCursor == null) {
            return;
        }
        if (this.nameText.getText().toString().length() == 0) {
            Log.e("ContactEditor:onPause", "nameText is null ");
            setResult(0);
            deleteContact();
            return;
        }
        ContentValues values = new ContentValues();
        values.put(ContactColumn.NAME, this.nameText.getText().toString());
        values.put(ContactColumn.MOBILENUM, this.mobileText.getText().toString());
        values.put(ContactColumn.HOMENUM, this.homeText.getText().toString());
        values.put(ContactColumn.ADDRESS, this.addressText.getText().toString());
        values.put("email", this.emailText.getText().toString());
        values.put(ContactColumn.BLOG, this.blogText.getText().toString());
        Log.e("ContactEditor:onPause", this.mUri.toString());
        Log.e("ContactEditor:onPause", values.toString());
        getContentResolver().update(this.mUri, values, null, null);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (this.mState == 0) {
            menu.add(0, 1, 0, R.string.revert).setShortcut('0', 'r').setIcon(R.drawable.listuser);
            menu.add(0, 3, 0, R.string.delete_user).setShortcut('0', 'f').setIcon(R.drawable.remove);
        } else {
            menu.add(0, 2, 0, R.string.revert).setShortcut('0', 'd').setIcon(R.drawable.listuser);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                backupContact();
                break;
            case 2:
                cancelContact();
                break;
            case 3:
                deleteContact();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /* access modifiers changed from: private */
    public void deleteContact() {
        if (this.mCursor != null) {
            this.mCursor.close();
            this.mCursor = null;
            getContentResolver().delete(this.mUri, null, null);
            this.nameText.setText("");
        }
    }

    private void cancelContact() {
        if (this.mCursor != null) {
            deleteContact();
        }
        setResult(0);
        finish();
    }

    /* access modifiers changed from: private */
    public void updateContact() {
        if (this.mCursor != null) {
            this.mCursor.close();
            this.mCursor = null;
            ContentValues values = new ContentValues();
            values.put(ContactColumn.NAME, this.nameText.getText().toString());
            values.put(ContactColumn.MOBILENUM, this.mobileText.getText().toString());
            values.put(ContactColumn.HOMENUM, this.homeText.getText().toString());
            values.put(ContactColumn.ADDRESS, this.addressText.getText().toString());
            values.put("email", this.emailText.getText().toString());
            values.put(ContactColumn.BLOG, this.blogText.getText().toString());
            Log.e("ContactEditor:onPause", this.mUri.toString());
            Log.e("ContactEditor:onPause", values.toString());
            getContentResolver().update(this.mUri, values, null, null);
        }
        setResult(0);
        finish();
    }

    /* access modifiers changed from: private */
    public void backupContact() {
        if (this.mCursor != null) {
            this.mCursor.close();
            this.mCursor = null;
            ContentValues values = new ContentValues();
            values.put(ContactColumn.NAME, this.nameText.getText().toString());
            values.put(ContactColumn.MOBILENUM, this.mobileText.getText().toString());
            values.put(ContactColumn.HOMENUM, this.homeText.getText().toString());
            values.put(ContactColumn.ADDRESS, this.addressText.getText().toString());
            values.put("email", this.emailText.getText().toString());
            values.put(ContactColumn.BLOG, this.blogText.getText().toString());
            Log.e("ContactEditor:onPause", this.mUri.toString());
            Log.e("ContactEditor:onPause", values.toString());
            getContentResolver().update(this.mUri, values, null, null);
        }
        setResult(0);
        finish();
    }
}
