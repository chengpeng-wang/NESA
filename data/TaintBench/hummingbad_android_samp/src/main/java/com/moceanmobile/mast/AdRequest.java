package com.moceanmobile.mast;

import android.text.TextUtils;
import android.util.Log;
import com.moceanmobile.mast.bean.AssetRequest;
import com.moceanmobile.mast.bean.DataAssetRequest;
import com.moceanmobile.mast.bean.ImageAssetRequest;
import com.moceanmobile.mast.bean.TitleAssetRequest;
import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdRequest {
    /* access modifiers changed from: private */
    public Handler handler = null;
    /* access modifiers changed from: private */
    public boolean isNative = false;
    /* access modifiers changed from: private|final */
    public final String requestUrl;
    private List<AssetRequest> requestedAssetsList = null;
    /* access modifiers changed from: private|final */
    public final int timeout;
    /* access modifiers changed from: private|final */
    public final String userAgent;

    public interface Handler {
        @Deprecated
        void adRequestCompleted(AdRequest adRequest, AdDescriptor adDescriptor);

        @Deprecated
        void adRequestError(AdRequest adRequest, String str, String str2);

        @Deprecated
        void adRequestFailed(AdRequest adRequest, Exception exception);
    }

    private class RequestProcessor implements Runnable {
        private RequestProcessor() {
        }

        /* synthetic */ RequestProcessor(AdRequest adRequest, RequestProcessor requestProcessor) {
            this();
        }

        public void run() {
            InputStream inputStream = null;
            try {
                String access$4;
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(AdRequest.this.requestUrl).openConnection();
                httpURLConnection.setRequestProperty("Connection", "close");
                httpURLConnection.setRequestProperty("User-Agent", AdRequest.this.userAgent);
                httpURLConnection.setConnectTimeout(AdRequest.this.timeout * 1000);
                if (AdRequest.this.isNative) {
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setRequestProperty(MASTNativeAdConstants.REQUEST_HEADER_CONTENT_TYPE, MASTNativeAdConstants.REQUEST_HEADER_CONTENT_TYPE_VALUE);
                    access$4 = AdRequest.this.generateNativeAssetRequestJson();
                    httpURLConnection.setFixedLengthStreamingMode(access$4.getBytes().length);
                    DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    dataOutputStream.writeBytes(access$4);
                    dataOutputStream.flush();
                    dataOutputStream.close();
                } else {
                    httpURLConnection.setRequestMethod("GET");
                }
                if (httpURLConnection.getResponseCode() == CtaButton.WIDTH_DIPS) {
                    access$4 = httpURLConnection.getHeaderField(MASTNativeAdConstants.REQUEST_HEADER_CONTENT_TYPE);
                    inputStream = httpURLConnection.getInputStream();
                    Log.d("AdRequest", "Response Content-Type = " + access$4);
                    if (access$4 != null && access$4.contains(MASTNativeAdConstants.RESPONSE_HEADER_CONTENT_TYPE_JSON)) {
                        AdDescriptor parseNativeResponse = AdDescriptor.parseNativeResponse(inputStream);
                        if (AdRequest.this.handler != null) {
                            if (parseNativeResponse != null) {
                                access$4 = parseNativeResponse.getErrroMessage();
                                if (access$4 != null) {
                                    AdRequest.this.handler.adRequestError(AdRequest.this, null, access$4);
                                } else {
                                    AdRequest.this.handler.adRequestCompleted(AdRequest.this, parseNativeResponse);
                                }
                            } else {
                                AdRequest.this.handler.adRequestFailed(AdRequest.this, new Exception("Invalid Response Received.."));
                            }
                        }
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                } else if (AdRequest.this.handler != null) {
                    AdRequest.this.handler.adRequestFailed(AdRequest.this, null);
                }
            } catch (Exception e3) {
                if (AdRequest.this.handler != null) {
                    AdRequest.this.handler.adRequestFailed(AdRequest.this, e3);
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e22) {
                        e22.printStackTrace();
                    }
                }
            } catch (Throwable th) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                }
            }
        }
    }

    public static AdRequest create(int timeout, String adServerUrl, String userAgent, Map<String, String> parameters, List<AssetRequest> requestedAssets, Handler handler, boolean isNative) throws UnsupportedEncodingException {
        AdRequest adRequest = new AdRequest(timeout, adServerUrl, userAgent, parameters, requestedAssets, handler, isNative);
        adRequest.start();
        return adRequest;
    }

    private AdRequest(int timeout, String adServerUrl, String userAgent, Map<String, String> parameters, List<AssetRequest> requestedAssets, Handler handler, boolean isNative) throws UnsupportedEncodingException {
        this.isNative = isNative;
        this.timeout = timeout;
        this.userAgent = userAgent;
        this.handler = handler;
        this.requestedAssetsList = requestedAssets;
        StringBuilder stringBuilder = new StringBuilder(128);
        stringBuilder.append(adServerUrl);
        if (stringBuilder.indexOf(MASTNativeAdConstants.QUESTIONMARK) > 0) {
            stringBuilder.append(MASTNativeAdConstants.AMPERSAND);
        } else {
            stringBuilder.append(MASTNativeAdConstants.QUESTIONMARK);
        }
        for (Entry entry : parameters.entrySet()) {
            if (!(entry == null || TextUtils.isEmpty((CharSequence) entry.getKey()))) {
                stringBuilder.append(URLEncoder.encode((String) entry.getKey(), Defaults.ENCODING_UTF_8));
                stringBuilder.append(MASTNativeAdConstants.EQUAL);
                if (!TextUtils.isEmpty((CharSequence) entry.getValue())) {
                    stringBuilder.append(URLEncoder.encode((String) entry.getValue(), Defaults.ENCODING_UTF_8));
                }
                stringBuilder.append(MASTNativeAdConstants.AMPERSAND);
            }
        }
        stringBuilder.setLength(stringBuilder.length() - 1);
        this.requestUrl = stringBuilder.toString();
    }

    public String getRequestUrl() {
        return this.requestUrl;
    }

    public void cancel() {
        this.handler = null;
    }

    private void start() {
        Background.getExecutor().execute(new RequestProcessor(this, null));
    }

    /* access modifiers changed from: private */
    public String generateNativeAssetRequestJson() throws JSONException {
        StringBuffer stringBuffer = new StringBuffer(MASTNativeAdConstants.REQUEST_NATIVE_EQ_WRAPPER);
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("ver", "1");
        JSONArray jSONArray = new JSONArray();
        for (AssetRequest assetRequest : this.requestedAssetsList) {
            if (assetRequest != null) {
                Object obj;
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put(MASTNativeAdConstants.ID_STRING, assetRequest.assetId);
                jSONObject2.put(MASTNativeAdConstants.REQUEST_REQUIRED, assetRequest.isRequired ? 1 : 0);
                JSONObject jSONObject3;
                JSONObject obj2;
                if (assetRequest instanceof TitleAssetRequest) {
                    if (((TitleAssetRequest) assetRequest).length > 0) {
                        JSONObject jSONObject4 = new JSONObject();
                        jSONObject4.put(MASTNativeAdConstants.REQUEST_LEN, ((TitleAssetRequest) assetRequest).length);
                        jSONObject2.putOpt("title", jSONObject4);
                        obj2 = jSONObject2;
                    } else {
                        Log.w("AdRequest", "'length' parameter is mandatory for title asset");
                        obj2 = null;
                    }
                } else if (assetRequest instanceof ImageAssetRequest) {
                    jSONObject3 = new JSONObject();
                    if (((ImageAssetRequest) assetRequest).imageType != null) {
                        jSONObject3.put("type", ((ImageAssetRequest) assetRequest).imageType.getTypeId());
                    }
                    if (((ImageAssetRequest) assetRequest).width > 0) {
                        jSONObject3.put(MASTNativeAdConstants.NATIVE_IMAGE_W, ((ImageAssetRequest) assetRequest).width);
                    }
                    if (((ImageAssetRequest) assetRequest).height > 0) {
                        jSONObject3.put(MASTNativeAdConstants.NATIVE_IMAGE_H, ((ImageAssetRequest) assetRequest).height);
                    }
                    jSONObject2.putOpt("img", jSONObject3);
                    obj2 = jSONObject2;
                } else if (assetRequest instanceof DataAssetRequest) {
                    jSONObject3 = new JSONObject();
                    if (((DataAssetRequest) assetRequest).dataAssetType != null) {
                        jSONObject3.put("type", ((DataAssetRequest) assetRequest).dataAssetType.getTypeId());
                        if (((DataAssetRequest) assetRequest).length > 0) {
                            jSONObject3.put(MASTNativeAdConstants.REQUEST_LEN, ((DataAssetRequest) assetRequest).length);
                        }
                        jSONObject2.putOpt("data", jSONObject3);
                        obj2 = jSONObject2;
                    } else {
                        Log.w("AdRequest", "'type' parameter is mandatory for data asset");
                        obj2 = null;
                    }
                } else {
                    obj2 = jSONObject2;
                }
                if (obj2 != null) {
                    jSONArray.put(obj2);
                }
            }
        }
        jSONObject.putOpt(MASTNativeAdConstants.NATIVE_ASSETS_STRING, jSONArray);
        stringBuffer.append(jSONObject.toString());
        return stringBuffer.toString();
    }
}
