package com.feedback;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.feedback.b.a;
import com.feedback.c.c;
import com.feedback.ui.FeedbackConversations;
import com.mobclick.android.l;

public class UMFeedbackService {
    private static NotificationType a;
    /* access modifiers changed from: private|static */
    public static Context b;
    private static boolean c = false;

    /* access modifiers changed from: private|static */
    public static void b(String str) {
        if (a == NotificationType.NotificationBar) {
            Object applicationLabel;
            NotificationManager notificationManager = (NotificationManager) b.getSystemService("notification");
            Notification notification = new Notification(17301598, b.getString(l.a(b, "string", "UMNewReplyFlick")), System.currentTimeMillis());
            Intent intent = new Intent(b, FeedbackConversations.class);
            intent.setFlags(131072);
            PendingIntent activity = PendingIntent.getActivity(b, 0, intent, 0);
            PackageManager packageManager = b.getPackageManager();
            try {
                applicationLabel = packageManager.getApplicationLabel(packageManager.getApplicationInfo(b.getPackageName(), 128));
            } catch (NameNotFoundException e) {
                e.printStackTrace();
                applicationLabel = null;
            }
            if (applicationLabel != null) {
                applicationLabel = new StringBuilder(String.valueOf(applicationLabel)).append(" : ").toString();
            }
            notification.setLatestEventInfo(b, applicationLabel + b.getString(l.a(b, "string", "UMNewReplyTitle")), b.getString(l.a(b, "string", "UMNewReplyHint")), activity);
            notification.flags = 16;
            notificationManager.notify(0, notification);
            return;
        }
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(b).inflate(l.a(b, "layout", "umeng_analyse_new_reply_alert_dialog"), null);
        TextView textView = (TextView) linearLayout.findViewById(l.a(b, "id", "umeng_analyse_new_reply_alert_title"));
        TextView textView2 = (TextView) linearLayout.findViewById(l.a(b, "id", "umeng_analyse_new_dev_reply_box"));
        textView2.setText(str);
        AlertDialog create = new Builder(b).create();
        create.show();
        create.setContentView(linearLayout);
        textView.setText(b.getString(l.a(b, "string", "UMNewReplyAlertTitle")));
        ((Button) linearLayout.findViewById(l.a(b, "id", "umeng_analyse_exitBtn"))).setOnClickListener(new a(create));
        Button button = (Button) linearLayout.findViewById(l.a(b, "id", "umeng_analyse_see_detail_btn"));
        b bVar = new b(create);
        button.setOnClickListener(bVar);
        textView2.setOnClickListener(bVar);
    }

    public static void enableNewReplyNotification(Context context, NotificationType notificationType) {
        Handler cVar = new c();
        b = context;
        a = notificationType;
        new c(context, cVar).start();
        c = true;
    }

    public static boolean getHasCheckedReply() {
        return c;
    }

    public static void openUmengFeedbackSDK(Context context) {
        a.a(context);
    }
}
