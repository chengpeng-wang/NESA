package com.beita.contact;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ContactView extends Activity {
    private static final int DELETE_ID = 2;
    private static final int EDITOR_ID = 3;
    private static final int REVERT_ID = 1;
    private Cursor mCursor;
    private TextView mTextViewAddress;
    private TextView mTextViewBlog;
    private TextView mTextViewEmail;
    private TextView mTextViewHome;
    private TextView mTextViewMobile;
    private TextView mTextViewName;
    private Uri mUri;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mUri = getIntent().getData();
        setContentView(R.layout.viewuser);
        this.mTextViewName = (TextView) findViewById(R.id.TextView_Name);
        this.mTextViewMobile = (TextView) findViewById(R.id.TextView_Mobile);
        this.mTextViewHome = (TextView) findViewById(R.id.TextView_Home);
        this.mTextViewAddress = (TextView) findViewById(R.id.TextView_Address);
        this.mTextViewEmail = (TextView) findViewById(R.id.TextView_Email);
        this.mTextViewBlog = (TextView) findViewById(R.id.TextView_Blog);
        this.mCursor = managedQuery(this.mUri, ContactColumn.PROJECTION, null, null, null);
        this.mCursor.moveToFirst();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.mCursor != null) {
            this.mCursor.moveToFirst();
            this.mTextViewName.setText(this.mCursor.getString(1));
            this.mTextViewMobile.setText(this.mCursor.getString(2));
            this.mTextViewHome.setText(this.mCursor.getString(3));
            this.mTextViewAddress.setText(this.mCursor.getString(4));
            this.mTextViewEmail.setText(this.mCursor.getString(5));
            this.mTextViewBlog.setText(this.mCursor.getString(6));
            return;
        }
        setTitle("错误信息");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 0, R.string.revert).setShortcut('0', 'r').setIcon(R.drawable.listuser);
        menu.add(0, 2, 0, R.string.delete_user).setShortcut('0', 'd').setIcon(R.drawable.remove);
        menu.add(0, 3, 0, R.string.editor_user).setShortcut('0', 'd').setIcon(R.drawable.edituser);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                setResult(0);
                finish();
                break;
            case 2:
                deleteContact();
                finish();
                break;
            case 3:
                startActivity(new Intent("android.intent.action.EDIT", this.mUri));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteContact() {
        if (this.mCursor != null) {
            this.mCursor.close();
            this.mCursor = null;
            getContentResolver().delete(this.mUri, null, null);
            setResult(0);
        }
    }
}
