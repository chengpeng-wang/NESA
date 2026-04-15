package com.cleanmaster.gaid;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public class AdvertisingIdHelper {
    private static AdvertisingIdHelper instance = null;
    /* access modifiers changed from: private */
    public String mGAId = "";
    /* access modifiers changed from: private */
    public boolean mTrackFlag = false;

    class a implements AdvertisingIdInterface {
        private IBinder kq;

        a(IBinder paramIBinder) {
            this.kq = paramIBinder;
        }

        public IBinder asBinder() {
            return this.kq;
        }

        public String getId() {
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                this.kq.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                String readString = obtain2.readString();
                return readString;
            } finally {
                obtain2.recycle();
                obtain.recycle();
            }
        }

        public boolean isLimitAdTrackingEnabled(boolean paramBoolean) {
            boolean z = true;
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                obtain.writeInt(paramBoolean ? 1 : 0);
                this.kq.transact(2, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() == 0) {
                    z = false;
                }
                obtain2.recycle();
                obtain.recycle();
                return z;
            } catch (SecurityException e) {
                obtain2.recycle();
                obtain.recycle();
                return false;
            } catch (Throwable th) {
                obtain2.recycle();
                obtain.recycle();
                throw th;
            }
        }
    }

    static GooglePlayServiceConnection connection(Context paramContext) {
        try {
            paramContext.getPackageManager().getPackageInfo("com.android.vending", 0);
            try {
                GooglePlayServiceConnection googlePlayServiceConnection = new GooglePlayServiceConnection();
                Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
                intent.setPackage("com.google.android.gms");
                if (paramContext.bindService(intent, googlePlayServiceConnection, 1)) {
                    return googlePlayServiceConnection;
                }
                return null;
            } catch (SecurityException e) {
                return null;
            }
        } catch (Exception e2) {
            return null;
        }
    }

    public static IInterface getIdInterface(IBinder paramIBinder) {
        if (paramIBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = paramIBinder.queryLocalInterface("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
        return (queryLocalInterface == null || !(queryLocalInterface instanceof AdvertisingIdInterface)) ? new a(paramIBinder) : queryLocalInterface;
    }

    public String getGAId() {
        return this.mGAId;
    }

    public boolean getTrackFlag() {
        return this.mTrackFlag;
    }

    public void asyncGetGAId() {
        new Thread(new Runnable() {
            /* JADX WARNING: Removed duplicated region for block: B:52:? A:{SYNTHETIC, RETURN} */
            /* JADX WARNING: Removed duplicated region for block: B:14:0x0030  */
            /* JADX WARNING: Removed duplicated region for block: B:36:0x005d A:{SYNTHETIC, Splitter:B:36:0x005d} */
            /* JADX WARNING: Removed duplicated region for block: B:14:0x0030  */
            /* JADX WARNING: Removed duplicated region for block: B:52:? A:{SYNTHETIC, RETURN} */
            /* JADX WARNING: Removed duplicated region for block: B:41:0x0069 A:{ExcHandler: all (th java.lang.Throwable), Splitter:B:3:0x0011} */
            /* JADX WARNING: Removed duplicated region for block: B:52:? A:{SYNTHETIC, RETURN} */
            /* JADX WARNING: Removed duplicated region for block: B:14:0x0030  */
            /* JADX WARNING: Failed to process nested try/catch */
            /* JADX WARNING: Missing block: B:37:?, code skipped:
            r3.unbindService(r4);
     */
            /* JADX WARNING: Missing block: B:40:0x0065, code skipped:
            r1 = r0;
            r0 = false;
     */
            /* JADX WARNING: Missing block: B:42:0x006a, code skipped:
            if (r4 != null) goto L_0x006c;
     */
            /* JADX WARNING: Missing block: B:44:?, code skipped:
            r3.unbindService(r4);
     */
            /* JADX WARNING: Missing block: B:48:0x0073, code skipped:
            r0 = r2;
     */
            /* JADX WARNING: Missing block: B:50:0x0076, code skipped:
            r0 = r2;
     */
            public void run() {
                /*
                r7 = this;
                r1 = 0;
                r0 = com.picksinit.c.getInstance();
                r3 = r0.getContext();
                r4 = com.cleanmaster.gaid.AdvertisingIdHelper.connection(r3);
                if (r4 != 0) goto L_0x0010;
            L_0x000f:
                return;
            L_0x0010:
                r2 = 0;
                r0 = r4.getConnectedBinder();	 Catch:{ RemoteException -> 0x0049, InterruptedException -> 0x0059, all -> 0x0069 }
                r0 = com.cleanmaster.gaid.AdvertisingIdHelper.getIdInterface(r0);	 Catch:{ RemoteException -> 0x0049, InterruptedException -> 0x0059, all -> 0x0069 }
                r0 = (com.cleanmaster.gaid.AdvertisingIdInterface) r0;	 Catch:{ RemoteException -> 0x0049, InterruptedException -> 0x0059, all -> 0x0069 }
                r2 = r0.getId();	 Catch:{ RemoteException -> 0x0049, InterruptedException -> 0x0059, all -> 0x0069 }
                r5 = 0;
                r0 = r0.isLimitAdTrackingEnabled(r5);	 Catch:{ RemoteException -> 0x0075, InterruptedException -> 0x0072, all -> 0x0069 }
                if (r4 == 0) goto L_0x0029;
            L_0x0026:
                r3.unbindService(r4);	 Catch:{ IllegalArgumentException -> 0x0046 }
            L_0x0029:
                r1 = r2;
            L_0x002a:
                r2 = android.text.TextUtils.isEmpty(r1);
                if (r2 != 0) goto L_0x000f;
            L_0x0030:
                r2 = com.cleanmaster.gaid.AdvertisingIdHelper.this;
                r2 = r2.mGAId;
                monitor-enter(r2);
                r3 = com.cleanmaster.gaid.AdvertisingIdHelper.this;	 Catch:{ all -> 0x0043 }
                r3.mGAId = r1;	 Catch:{ all -> 0x0043 }
                r1 = com.cleanmaster.gaid.AdvertisingIdHelper.this;	 Catch:{ all -> 0x0043 }
                r1.mTrackFlag = r0;	 Catch:{ all -> 0x0043 }
                monitor-exit(r2);	 Catch:{ all -> 0x0043 }
                goto L_0x000f;
            L_0x0043:
                r0 = move-exception;
                monitor-exit(r2);	 Catch:{ all -> 0x0043 }
                throw r0;
            L_0x0046:
                r1 = move-exception;
                r1 = r2;
                goto L_0x002a;
            L_0x0049:
                r0 = move-exception;
                r0 = r2;
            L_0x004b:
                if (r4 == 0) goto L_0x0050;
            L_0x004d:
                r3.unbindService(r4);	 Catch:{ IllegalArgumentException -> 0x0054 }
            L_0x0050:
                r6 = r1;
                r1 = r0;
                r0 = r6;
                goto L_0x002a;
            L_0x0054:
                r2 = move-exception;
                r6 = r1;
                r1 = r0;
                r0 = r6;
                goto L_0x002a;
            L_0x0059:
                r0 = move-exception;
                r0 = r2;
            L_0x005b:
                if (r4 == 0) goto L_0x0060;
            L_0x005d:
                r3.unbindService(r4);	 Catch:{ IllegalArgumentException -> 0x0064 }
            L_0x0060:
                r6 = r1;
                r1 = r0;
                r0 = r6;
                goto L_0x002a;
            L_0x0064:
                r2 = move-exception;
                r6 = r1;
                r1 = r0;
                r0 = r6;
                goto L_0x002a;
            L_0x0069:
                r0 = move-exception;
                if (r4 == 0) goto L_0x006f;
            L_0x006c:
                r3.unbindService(r4);	 Catch:{ IllegalArgumentException -> 0x0070 }
            L_0x006f:
                throw r0;
            L_0x0070:
                r1 = move-exception;
                goto L_0x006f;
            L_0x0072:
                r0 = move-exception;
                r0 = r2;
                goto L_0x005b;
            L_0x0075:
                r0 = move-exception;
                r0 = r2;
                goto L_0x004b;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.cleanmaster.gaid.AdvertisingIdHelper$AnonymousClass1.run():void");
            }
        }).start();
    }

    public static AdvertisingIdHelper getInstance() {
        if (instance == null) {
            instance = new AdvertisingIdHelper();
        }
        return instance;
    }

    private AdvertisingIdHelper() {
    }
}
