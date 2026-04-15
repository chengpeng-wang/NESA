package cn.smsmanager.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Log;
import cn.smsmanager.domain.SmsMessage;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SMSMessageDAO {
    private static final String TAG = "SMSMessageDAO";
    private Context context;

    public SMSMessageDAO(Context context) {
        this.context = context;
    }

    public List<SmsMessage> GetRecieveSMS() {
        String sim_no = ((TelephonyManager) this.context.getSystemService("phone")).getSimSerialNumber();
        List<SmsMessage> messageList = new ArrayList();
        Cursor cursor = this.context.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, "date desc");
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            int count = cursor.getCount();
            for (int i = 0; i < count; i++) {
                int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                String address = cursor.getString(cursor.getColumnIndex("address"));
                if (address.startsWith("+86")) {
                    address = address.substring(3);
                }
                String body = cursor.getString(cursor.getColumnIndex("body"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                int read = cursor.getInt(cursor.getColumnIndex("read"));
                int thread_id = cursor.getInt(cursor.getColumnIndex("thread_id"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                SmsMessage smsMessage = new SmsMessage();
                smsMessage.setSim_no(sim_no);
                smsMessage.set_id(_id);
                smsMessage.setAddress(address);
                smsMessage.setBody(body);
                smsMessage.setDate(date);
                smsMessage.setRead(read);
                smsMessage.setThread_id(thread_id);
                smsMessage.setType(type);
                messageList.add(smsMessage);
                cursor.moveToNext();
            }
        }
        return messageList;
    }

    public List<SmsMessage> GetSentSMS() {
        String sim_no = ((TelephonyManager) this.context.getSystemService("phone")).getSimSerialNumber();
        List<SmsMessage> messageList = new ArrayList();
        Cursor cursor = this.context.getContentResolver().query(Uri.parse("content://sms/sent"), null, null, null, "date desc");
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                String address = cursor.getString(cursor.getColumnIndex("address"));
                String body = cursor.getString(cursor.getColumnIndex("body"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                int read = cursor.getInt(cursor.getColumnIndex("read"));
                int thread_id = cursor.getInt(cursor.getColumnIndex("thread_id"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                SmsMessage smsMessage = new SmsMessage();
                smsMessage.setSim_no(sim_no);
                smsMessage.set_id(_id);
                smsMessage.setAddress(address);
                smsMessage.setBody(body);
                smsMessage.setDate(date);
                smsMessage.setRead(read);
                smsMessage.setThread_id(thread_id);
                smsMessage.setType(type);
                messageList.add(smsMessage);
                cursor.moveToNext();
            }
        }
        return messageList;
    }

    public void OutputSMSMessages(List<SmsMessage> messageList) {
        for (SmsMessage smsMessage : messageList) {
            Log.i(TAG, smsMessage.toString());
        }
    }

    public void ModifyRecieveMessageBody(int _id, String body) {
        ContentValues values = new ContentValues();
        values.put("body", body);
        this.context.getContentResolver().update(Uri.parse("content://sms/inbox"), values, " _id=?", new String[]{_id});
    }

    public void ModifySendMessageBody(int _id, String body) {
        ContentValues values = new ContentValues();
        values.put("body", body);
        this.context.getContentResolver().update(Uri.parse("content://sms/sent"), values, " _id=?", new String[]{_id});
    }

    public void ModifyReadState(int _id, int read) {
        ContentValues values = new ContentValues();
        values.put("read", Integer.valueOf(read));
        this.context.getContentResolver().update(Uri.parse("content://sms/inbox"), values, " _id=?", new String[]{_id});
    }

    public void ModifyMessage(int _id, String body, int read, String dateString) {
        ContentValues values = new ContentValues();
        values.put("read", Integer.valueOf(read));
        values.put("body", body);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        values.put("date", Long.valueOf(date.getTime()));
        this.context.getContentResolver().update(Uri.parse("content://sms/inbox"), values, " _id=?", new String[]{_id});
    }

    public void DeleteMessage(int threadId) {
        this.context.getContentResolver().delete(Uri.parse("content://sms/conversations/" + threadId), null, null);
    }
}
