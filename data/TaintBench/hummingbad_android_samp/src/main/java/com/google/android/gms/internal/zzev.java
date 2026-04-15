package com.google.android.gms.internal;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.provider.CalendarContract.Events;
import android.text.TextUtils;
import com.google.android.gms.R;
import com.google.android.gms.ads.internal.zzp;
import java.util.Map;

@zzgk
public class zzev extends zzfb {
    /* access modifiers changed from: private|final */
    public final Context mContext;
    private final Map<String, String> zzvs;
    private String zzzi;
    private long zzzj;
    private long zzzk;
    private String zzzl;
    private String zzzm;

    public zzev(zzip zzip, Map<String, String> map) {
        super(zzip, "createCalendarEvent");
        this.zzvs = map;
        this.mContext = zzip.zzgN();
        zzdV();
    }

    private String zzae(String str) {
        return TextUtils.isEmpty((CharSequence) this.zzvs.get(str)) ? "" : (String) this.zzvs.get(str);
    }

    private long zzaf(String str) {
        String str2 = (String) this.zzvs.get(str);
        if (str2 == null) {
            return -1;
        }
        try {
            return Long.parseLong(str2);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void zzdV() {
        this.zzzi = zzae("description");
        this.zzzl = zzae("summary");
        this.zzzj = zzaf("start_ticks");
        this.zzzk = zzaf("end_ticks");
        this.zzzm = zzae("location");
    }

    /* access modifiers changed from: 0000 */
    public Intent createIntent() {
        Intent data = new Intent("android.intent.action.EDIT").setData(Events.CONTENT_URI);
        data.putExtra("title", this.zzzi);
        data.putExtra("eventLocation", this.zzzm);
        data.putExtra("description", this.zzzl);
        if (this.zzzj > -1) {
            data.putExtra("beginTime", this.zzzj);
        }
        if (this.zzzk > -1) {
            data.putExtra("endTime", this.zzzk);
        }
        data.setFlags(268435456);
        return data;
    }

    public void execute() {
        if (this.mContext == null) {
            zzah("Activity context is not available.");
        } else if (zzp.zzbx().zzM(this.mContext).zzda()) {
            Builder zzL = zzp.zzbx().zzL(this.mContext);
            zzL.setTitle(zzp.zzbA().zzc(R.string.create_calendar_title, "Create calendar event"));
            zzL.setMessage(zzp.zzbA().zzc(R.string.create_calendar_message, "Allow Ad to create a calendar event?"));
            zzL.setPositiveButton(zzp.zzbA().zzc(R.string.accept, "Accept"), new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    zzev.this.mContext.startActivity(zzev.this.createIntent());
                }
            });
            zzL.setNegativeButton(zzp.zzbA().zzc(R.string.decline, "Decline"), new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    zzev.this.zzah("Operation denied by user.");
                }
            });
            zzL.create().show();
        } else {
            zzah("This feature is not available on the device.");
        }
    }
}
