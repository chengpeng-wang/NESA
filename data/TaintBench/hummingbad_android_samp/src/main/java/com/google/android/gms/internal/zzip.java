package com.google.android.gms.internal;

import android.app.Activity;
import android.content.Context;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.overlay.zzd;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import java.util.Map;
import org.json.JSONObject;

@zzgk
public interface zzip extends zzaw {
    void destroy();

    Context getContext();

    LayoutParams getLayoutParams();

    void getLocationOnScreen(int[] iArr);

    int getMeasuredHeight();

    int getMeasuredWidth();

    ViewParent getParent();

    String getRequestId();

    int getRequestedOrientation();

    WebView getWebView();

    boolean isDestroyed();

    void loadData(String str, String str2, String str3);

    void loadDataWithBaseURL(String str, String str2, String str3, String str4, String str5);

    void loadUrl(String str);

    void measure(int i, int i2);

    void setBackgroundColor(int i);

    void setContext(Context context);

    void setOnClickListener(OnClickListener onClickListener);

    void setOnTouchListener(OnTouchListener onTouchListener);

    void setRequestedOrientation(int i);

    void setWebViewClient(WebViewClient webViewClient);

    void setWillNotDraw(boolean z);

    void stopLoading();

    boolean willNotDraw();

    void zzC(boolean z);

    void zzD(boolean z);

    void zzE(boolean z);

    void zza(Context context, AdSizeParcel adSizeParcel);

    void zza(AdSizeParcel adSizeParcel);

    void zza(zzd zzd);

    void zza(String str, String str2);

    void zza(String str, JSONObject jSONObject);

    void zzaF(String str);

    void zzaG(String str);

    void zzaH(String str);

    AdSizeParcel zzaN();

    void zzb(zzd zzd);

    void zzb(String str, JSONObject jSONObject);

    void zzc(String str, Map<String, ?> map);

    void zzeD();

    void zzgM();

    Activity zzgN();

    Context zzgO();

    com.google.android.gms.ads.internal.zzd zzgP();

    zzd zzgQ();

    zzd zzgR();

    zziq zzgS();

    boolean zzgT();

    zzan zzgU();

    VersionInfoParcel zzgV();

    boolean zzgW();

    void zzgX();

    boolean zzgY();

    void zzgZ();

    String zzha();

    void zzv(int i);
}
