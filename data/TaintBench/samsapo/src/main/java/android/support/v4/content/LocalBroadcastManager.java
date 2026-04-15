package android.support.v4.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class LocalBroadcastManager {
    private static final boolean DEBUG = false;
    static final int MSG_EXEC_PENDING_BROADCASTS = 1;
    private static final String TAG = "LocalBroadcastManager";
    private static LocalBroadcastManager mInstance;
    private static final Object mLock;
    private final HashMap<String, ArrayList<ReceiverRecord>> mActions;
    private final Context mAppContext;
    private final Handler mHandler;
    private final ArrayList<BroadcastRecord> mPendingBroadcasts;
    private final HashMap<BroadcastReceiver, ArrayList<IntentFilter>> mReceivers;

    private static class BroadcastRecord {
        final Intent intent;
        final ArrayList<ReceiverRecord> receivers;

        BroadcastRecord(Intent intent, ArrayList<ReceiverRecord> arrayList) {
            ArrayList<ReceiverRecord> arrayList2 = arrayList;
            this.intent = intent;
            this.receivers = arrayList2;
        }
    }

    private static class ReceiverRecord {
        boolean broadcasting;
        final IntentFilter filter;
        final BroadcastReceiver receiver;

        ReceiverRecord(IntentFilter intentFilter, BroadcastReceiver broadcastReceiver) {
            BroadcastReceiver broadcastReceiver2 = broadcastReceiver;
            this.filter = intentFilter;
            this.receiver = broadcastReceiver2;
        }

        public String toString() {
            StringBuilder stringBuilder = r5;
            StringBuilder stringBuilder2 = new StringBuilder(128);
            StringBuilder stringBuilder3 = stringBuilder;
            stringBuilder = stringBuilder3.append("Receiver{");
            stringBuilder = stringBuilder3.append(this.receiver);
            stringBuilder = stringBuilder3.append(" filter=");
            stringBuilder = stringBuilder3.append(this.filter);
            stringBuilder = stringBuilder3.append("}");
            return stringBuilder3.toString();
        }
    }

    static {
        Object obj = r2;
        Object obj2 = new Object();
        mLock = obj;
    }

    public static LocalBroadcastManager getInstance(Context context) {
        Context context2 = context;
        Object obj = mLock;
        Object obj2 = obj;
        synchronized (obj) {
            try {
                if (mInstance == null) {
                }
                LocalBroadcastManager localBroadcastManager = mInstance;
                return localBroadcastManager;
            } finally {
                Object obj3 = r3;
                Object obj4 = obj2;
                obj4 = obj3;
            }
        }
    }

    private LocalBroadcastManager(Context context) {
        Context context2 = context;
        HashMap hashMap = r7;
        HashMap hashMap2 = new HashMap();
        this.mReceivers = hashMap;
        hashMap = r7;
        hashMap2 = new HashMap();
        this.mActions = hashMap;
        ArrayList arrayList = r7;
        ArrayList arrayList2 = new ArrayList();
        this.mPendingBroadcasts = arrayList;
        this.mAppContext = context2;
        Handler handler = r7;
        Handler anonymousClass1 = new Handler(this, context2.getMainLooper()) {
            final /* synthetic */ LocalBroadcastManager this$0;

            public void handleMessage(Message message) {
                Message message2 = message;
                switch (message2.what) {
                    case 1:
                        this.this$0.executePendingBroadcasts();
                        return;
                    default:
                        super.handleMessage(message2);
                        return;
                }
            }
        };
        this.mHandler = handler;
    }

    public void registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        Object put;
        BroadcastReceiver broadcastReceiver2 = broadcastReceiver;
        IntentFilter intentFilter2 = intentFilter;
        HashMap hashMap = this.mReceivers;
        HashMap hashMap2 = hashMap;
        synchronized (hashMap) {
            try {
                ReceiverRecord receiverRecord = r14;
                ReceiverRecord receiverRecord2 = new ReceiverRecord(intentFilter2, broadcastReceiver2);
                ReceiverRecord receiverRecord3 = receiverRecord;
                ArrayList arrayList = (ArrayList) this.mReceivers.get(broadcastReceiver2);
                if (arrayList == null) {
                }
                boolean add = arrayList.add(intentFilter2);
                for (int i = 0; i < intentFilter2.countActions(); i++) {
                    String action = intentFilter2.getAction(i);
                    ArrayList arrayList2 = (ArrayList) this.mActions.get(action);
                    if (arrayList2 == null) {
                        ArrayList arrayList3 = r14;
                        ArrayList arrayList4 = new ArrayList(1);
                        arrayList2 = arrayList3;
                        put = this.mActions.put(action, arrayList2);
                    }
                    add = arrayList2.add(receiverRecord3);
                }
            } finally {
                Object obj = put;
                HashMap hashMap3 = hashMap2;
                put = obj;
            }
        }
    }

    public void unregisterReceiver(BroadcastReceiver broadcastReceiver) {
        BroadcastReceiver broadcastReceiver2 = broadcastReceiver;
        HashMap hashMap = this.mReceivers;
        HashMap hashMap2 = hashMap;
        synchronized (hashMap) {
            try {
                ArrayList arrayList = (ArrayList) this.mReceivers.remove(broadcastReceiver2);
                if (arrayList == null) {
                    return;
                }
                for (int i = 0; i < arrayList.size(); i++) {
                    IntentFilter intentFilter = (IntentFilter) arrayList.get(i);
                    for (int i2 = 0; i2 < intentFilter.countActions(); i2++) {
                        String action = intentFilter.getAction(i2);
                        ArrayList arrayList2 = (ArrayList) this.mActions.get(action);
                        if (arrayList2 != null) {
                            Object remove;
                            int i3 = 0;
                            while (i3 < arrayList2.size()) {
                                if (((ReceiverRecord) arrayList2.get(i3)).receiver == broadcastReceiver2) {
                                    remove = arrayList2.remove(i3);
                                    i3--;
                                }
                                i3++;
                            }
                            if (arrayList2.size() <= 0) {
                                remove = this.mActions.remove(action);
                            }
                        }
                    }
                }
            } catch (Throwable th) {
                Throwable th2 = th;
                HashMap hashMap3 = hashMap2;
                Throwable th3 = th2;
            }
        }
    }

    public boolean sendBroadcast(Intent intent) {
        Intent intent2 = intent;
        HashMap hashMap = this.mReceivers;
        HashMap hashMap2 = hashMap;
        synchronized (hashMap) {
            try {
                String str;
                StringBuilder stringBuilder;
                StringBuilder stringBuilder2;
                int v;
                String action = intent2.getAction();
                String resolveTypeIfNeeded = intent2.resolveTypeIfNeeded(this.mAppContext.getContentResolver());
                Uri data = intent2.getData();
                String scheme = intent2.getScheme();
                Set categories = intent2.getCategories();
                Object obj = (intent2.getFlags() & 8) != 0 ? 1 : null;
                if (obj != null) {
                    str = TAG;
                    stringBuilder = r25;
                    stringBuilder2 = new StringBuilder();
                    v = Log.v(str, stringBuilder.append("Resolving type ").append(resolveTypeIfNeeded).append(" scheme ").append(scheme).append(" of intent ").append(intent2).toString());
                }
                ArrayList arrayList = (ArrayList) this.mActions.get(intent2.getAction());
                if (arrayList != null) {
                    int i;
                    ArrayList arrayList2;
                    boolean add;
                    if (obj != null) {
                        str = TAG;
                        stringBuilder = r25;
                        stringBuilder2 = new StringBuilder();
                        v = Log.v(str, stringBuilder.append("Action list: ").append(arrayList).toString());
                    }
                    ArrayList arrayList3 = null;
                    for (i = 0; i < arrayList.size(); i++) {
                        ReceiverRecord receiverRecord = (ReceiverRecord) arrayList.get(i);
                        if (obj != null) {
                            str = TAG;
                            stringBuilder = r25;
                            stringBuilder2 = new StringBuilder();
                            v = Log.v(str, stringBuilder.append("Matching against filter ").append(receiverRecord.filter).toString());
                        }
                        if (!receiverRecord.broadcasting) {
                            int match = receiverRecord.filter.match(action, resolveTypeIfNeeded, scheme, data, categories, TAG);
                            if (match >= 0) {
                                if (obj != null) {
                                    str = TAG;
                                    stringBuilder = r25;
                                    stringBuilder2 = new StringBuilder();
                                    v = Log.v(str, stringBuilder.append("  Filter matched!  match=0x").append(Integer.toHexString(match)).toString());
                                }
                                if (arrayList3 == null) {
                                    arrayList2 = r25;
                                    ArrayList arrayList4 = new ArrayList();
                                    arrayList3 = arrayList2;
                                }
                                add = arrayList3.add(receiverRecord);
                                receiverRecord.broadcasting = true;
                            } else if (obj != null) {
                                String str2;
                                switch (match) {
                                    case -4:
                                        str2 = "category";
                                        break;
                                    case -3:
                                        str2 = "action";
                                        break;
                                    case -2:
                                        str2 = "data";
                                        break;
                                    case -1:
                                        str2 = "type";
                                        break;
                                    default:
                                        str2 = "unknown reason";
                                        break;
                                }
                                str = TAG;
                                stringBuilder = r25;
                                stringBuilder2 = new StringBuilder();
                                v = Log.v(str, stringBuilder.append("  Filter did not match: ").append(str2).toString());
                            }
                        } else if (obj != null) {
                            v = Log.v(TAG, "  Filter's target already added");
                        }
                    }
                    if (arrayList3 != null) {
                        for (i = 0; i < arrayList3.size(); i++) {
                            ((ReceiverRecord) arrayList3.get(i)).broadcasting = DEBUG;
                        }
                        arrayList2 = this.mPendingBroadcasts;
                        BroadcastRecord broadcastRecord = r25;
                        BroadcastRecord broadcastRecord2 = new BroadcastRecord(intent2, arrayList3);
                        add = arrayList2.add(broadcastRecord);
                        if (!this.mHandler.hasMessages(1)) {
                            add = this.mHandler.sendEmptyMessage(1);
                        }
                        return true;
                    }
                }
                return DEBUG;
            } catch (Throwable th) {
                Throwable th2 = th;
                HashMap hashMap3 = hashMap2;
                Throwable th3 = th2;
            }
        }
    }

    public void sendBroadcastSync(Intent intent) {
        if (sendBroadcast(intent)) {
            executePendingBroadcasts();
        }
    }

    /* access modifiers changed from: private */
    public void executePendingBroadcasts() {
        HashMap hashMap;
        while (true) {
            BroadcastRecord[] broadcastRecordArr = null;
            HashMap hashMap2 = this.mReceivers;
            HashMap hashMap3 = hashMap2;
            synchronized (hashMap2) {
                try {
                    int size = this.mPendingBroadcasts.size();
                    if (size <= 0) {
                        return;
                    }
                    hashMap = new BroadcastRecord[size];
                    for (BroadcastRecord broadcastRecord : broadcastRecordArr) {
                        for (int i = 0; i < broadcastRecord.receivers.size(); i++) {
                            ((ReceiverRecord) broadcastRecord.receivers.get(i)).receiver.onReceive(this.mAppContext, broadcastRecord.intent);
                        }
                    }
                } finally {
                    hashMap3 = 
/*
Method generation error in method: android.support.v4.content.LocalBroadcastManager.executePendingBroadcasts():void, dex: classes.dex
jadx.core.utils.exceptions.CodegenException: Error generate insn: ?: MERGE  (r2_2 'hashMap3' java.util.HashMap) = (r2_1 'hashMap3' java.util.HashMap), (r0_0 'this' java.util.HashMap A:{THIS}) in method: android.support.v4.content.LocalBroadcastManager.executePendingBroadcasts():void, dex: classes.dex
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:229)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:206)
	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:102)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:52)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:89)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:95)
	at jadx.core.codegen.RegionGen.makeTryCatch(RegionGen.java:300)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:89)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:89)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:95)
	at jadx.core.codegen.RegionGen.makeSynchronizedRegion(RegionGen.java:230)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:67)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:89)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:95)
	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:175)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:89)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:187)
	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:321)
	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:259)
	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:221)
	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:111)
	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:77)
	at jadx.core.codegen.CodeGen.visit(CodeGen.java:10)
	at jadx.core.ProcessClass.process(ProcessClass.java:38)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
	at jadx.api.JavaClass.decompile(JavaClass.java:61)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
Caused by: jadx.core.utils.exceptions.CodegenException: MERGE can be used only in fallback mode
	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:540)
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:512)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:223)
	... 33 more

*/
}
