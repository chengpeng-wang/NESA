package com.xmpp.client.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RelativeLayout;
import com.android.service.view.GPSTracker;
import java.net.URL;

public class GoogleLocationAsyncTask extends AsyncTask<URL, Integer, String> {
    public ProgressDialog dialog;
    public Context mContext;
    public GPSTracker mGooglegpsObj = null;
    public String mStrGoogleflag = null;
    public RelativeLayout mlayout_HelpMeeFreeWellcome;

    public GoogleLocationAsyncTask(Context context, GPSTracker GooglegpsObj, String StrGoogleflag) {
        this.mContext = context;
        this.mStrGoogleflag = StrGoogleflag;
        this.mGooglegpsObj = GooglegpsObj;
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
        this.dialog = ProgressDialog.show(this.mContext, "", "wait", true);
        GrabGPS(this.mContext, this.mGooglegpsObj);
        Log.e("onPreExecute", "//===========onPreExecute>>前景=============//");
    }

    /* access modifiers changed from: protected|varargs */
    public String doInBackground(URL... urls) {
        Log.e("doInBackground", "//===========doInBackground背景抓=============//");
        return "ok";
    }

    /* access modifiers changed from: protected|varargs */
    public void onProgressUpdate(Integer... progress) {
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(String result) {
        if (result.equalsIgnoreCase("ok")) {
            this.dialog.dismiss();
        }
    }

    public static String GrabGPS(Context mContext, GPSTracker mGpsObj) {
        if (mGpsObj == null || !mGpsObj.canGetLocation()) {
            return "";
        }
        double longitude = mGpsObj.getLongitude();
        double latitude = mGpsObj.getLatitude();
        String strLonObj = Double.toString(longitude);
        String strLatObj = Double.toString(latitude);
        mGpsObj.setToArea(mContext, latitude, longitude);
        if (strLatObj == null || strLatObj.length() <= 0 || strLonObj == null || strLonObj.length() <= 0) {
            return "";
        }
        if (strLonObj.equalsIgnoreCase("") || strLatObj.equalsIgnoreCase("")) {
            StaticVariable.strLocation = "";
        } else if (strLatObj.length() <= 9 || strLonObj.length() <= 9) {
            StaticVariable.strLocation = new StringBuilder(String.valueOf(strLatObj)).append(",").append(strLonObj).toString();
        } else {
            StaticVariable.strLocation = strLatObj.substring(0, 9) + "," + strLonObj.substring(0, 9);
        }
        Log.e("-----strLonLat---GrabGPS()-----: ", StaticVariable.strLocation);
        return StaticVariable.strLocation;
    }
}
