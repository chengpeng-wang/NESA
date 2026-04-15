package com.google.android.gms.internal;

import android.app.AlertDialog.Builder;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.webkit.URLUtil;
import com.google.android.gms.R;
import com.google.android.gms.ads.internal.zzp;
import java.util.Map;

@zzgk
public class zzey extends zzfb {
    /* access modifiers changed from: private|final */
    public final Context mContext;
    private final Map<String, String> zzvs;

    public zzey(zzip zzip, Map<String, String> map) {
        super(zzip, "storePicture");
        this.zzvs = map;
        this.mContext = zzip.zzgN();
    }

    public void execute() {
        if (this.mContext == null) {
            zzah("Activity context is not available");
        } else if (zzp.zzbx().zzM(this.mContext).zzcX()) {
            final String str = (String) this.zzvs.get("iurl");
            if (TextUtils.isEmpty(str)) {
                zzah("Image url cannot be empty.");
            } else if (URLUtil.isValidUrl(str)) {
                final String zzag = zzag(str);
                if (zzp.zzbx().zzay(zzag)) {
                    Builder zzL = zzp.zzbx().zzL(this.mContext);
                    zzL.setTitle(zzp.zzbA().zzc(R.string.store_picture_title, "Save image"));
                    zzL.setMessage(zzp.zzbA().zzc(R.string.store_picture_message, "Allow Ad to store image in Picture gallery?"));
                    zzL.setPositiveButton(zzp.zzbA().zzc(R.string.accept, "Accept"), new OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                ((DownloadManager) zzey.this.mContext.getSystemService("download")).enqueue(zzey.this.zzg(str, zzag));
                            } catch (IllegalStateException e) {
                                zzey.this.zzah("Could not store picture.");
                            }
                        }
                    });
                    zzL.setNegativeButton(zzp.zzbA().zzc(R.string.decline, "Decline"), new OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            zzey.this.zzah("User canceled the download.");
                        }
                    });
                    zzL.create().show();
                    return;
                }
                zzah("Image type not recognized: " + zzag);
            } else {
                zzah("Invalid image url: " + str);
            }
        } else {
            zzah("Feature is not supported by the device.");
        }
    }

    /* access modifiers changed from: 0000 */
    public String zzag(String str) {
        return Uri.parse(str).getLastPathSegment();
    }

    /* access modifiers changed from: 0000 */
    public Request zzg(String str, String str2) {
        Request request = new Request(Uri.parse(str));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, str2);
        zzp.zzbz().zza(request);
        return request;
    }
}
