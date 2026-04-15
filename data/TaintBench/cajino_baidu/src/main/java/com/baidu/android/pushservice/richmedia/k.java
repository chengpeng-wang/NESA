package com.baidu.android.pushservice.richmedia;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

class k extends WebViewClient {
    final /* synthetic */ MediaViewActivity a;

    k(MediaViewActivity mediaViewActivity) {
        this.a = mediaViewActivity;
    }

    public void onPageFinished(WebView webView, String str) {
        super.onPageFinished(webView, str);
    }

    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        super.onPageStarted(webView, str, bitmap);
    }

    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        Intent intent;
        if (str.startsWith("tel:")) {
            try {
                intent = new Intent("android.intent.action.DIAL");
                intent.setData(Uri.parse(str));
                this.a.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Log.e("MediaViewActivity", "Error dialing " + str + ": " + e.toString());
            }
        } else if (str.startsWith("geo:")) {
            try {
                intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse(str));
                this.a.startActivity(intent);
            } catch (ActivityNotFoundException e2) {
                Log.e("MediaViewActivity", "Error showing map " + str + ": " + e2.toString());
            }
        } else if (str.startsWith("mailto:")) {
            try {
                intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse(str));
                this.a.startActivity(intent);
            } catch (ActivityNotFoundException e22) {
                Log.e("MediaViewActivity", "Error sending email " + str + ": " + e22.toString());
            }
        } else if (str.startsWith("sms:")) {
            try {
                String substring;
                Intent intent2 = new Intent("android.intent.action.VIEW");
                int indexOf = str.indexOf(63);
                if (indexOf == -1) {
                    substring = str.substring(4);
                } else {
                    substring = str.substring(4, indexOf);
                    String query = Uri.parse(str).getQuery();
                    if (query != null && query.startsWith("body=")) {
                        intent2.putExtra("sms_body", query.substring(5));
                    }
                }
                intent2.setData(Uri.parse("sms:" + substring));
                intent2.putExtra("address", substring);
                intent2.setType("vnd.android-dir/mms-sms");
                this.a.startActivity(intent2);
            } catch (ActivityNotFoundException e222) {
                Log.e("MediaViewActivity", "Error sending sms " + str + ":" + e222.toString());
            }
        }
        try {
            intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(str));
            this.a.startActivity(intent);
        } catch (ActivityNotFoundException e2222) {
            Log.e("MediaViewActivity", "Error loading url " + str, e2222);
        }
        return true;
    }
}
