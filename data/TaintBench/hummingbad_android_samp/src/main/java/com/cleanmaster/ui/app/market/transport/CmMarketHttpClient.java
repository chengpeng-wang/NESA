package com.cleanmaster.ui.app.market.transport;

import android.content.Context;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;
import com.cleanmaster.common.a;
import com.cleanmaster.gaid.AdvertisingIdHelper;
import com.cleanmaster.ui.app.market.b;
import com.cleanmaster.ui.app.market.data.MarketResponse;
import com.cleanmaster.util.g;
import com.cleanmaster.util.j;
import com.cleanmaster.util.o;
import com.moceanmobile.mast.Defaults;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.common.Constants;
import com.mopub.mobileads.VastIconXmlManager;
import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.picksinit.PicksConfig;
import com.picksinit.c;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class CmMarketHttpClient {
    private static CmMarketHttpClient a = new CmMarketHttpClient();

    public class MarketRequestBuilder extends a {
        public static final String REQUEST_APPS_CATEGORY = "14";
        public static final String REQUEST_APPS_POP = "29";
        public static final String REQUEST_APPS_TOP = "30";
        public static final String REQUEST_BANNER = "10";
        public static final String REQUEST_CM_FAMILY = "42";
        public static final String REQUEST_GAMES = "12";
        public static final String REQUEST_GAMES_TOP = "28";
        public static final String REQUEST_GAME_BOX = "15";
        public static final String REQUEST_GAME_CATEGORY = "13";
        public static final String REQUEST_GAME_TIP = "15001";
        public static final String REQUEST_GUESSYOU_LIKE_APP = "33";
        public static final String REQUEST_HOT_APPS = "11";
        public static final String REQUEST_HOT_APPS_FROM_GIFTBOX = "6";
        public static final String REQUEST_MAIN_TOP = "26";
        public static final String REQUEST_MARKET_MY_GAME_BATCH_RECOMMEND = "39";
        public static final String REQUEST_MARKET_MY_GAME_BUSINESS_RECOMMEND = "36";
        public static final String REQUEST_MARKET_MY_GAME_WEB_DETAILS = "38";
        public static final String REQUEST_MARKET_UNINSTALL_BATCH_RECOMMEND = "3004";
        public static final String REQUEST_RECOMMEND_AD_FOR_PROCESS = "21";
        public static final String REQUEST_RECOMMEND_AD_FOR_UNINSTALL = "22";
        public static final String REQUEST_RECOMMEND_APP = "41";
        public static final String REQUEST_RECOMMEND_FEWER_APP = "3003";
        public static final String REQUEST_RECOMMEND_FOR_FEWER_APP = "24";
        public static final String REQUEST_RECOMMEND_FOR_IN_MARKET_FEWER_APP = "31";
        public static final String REQUEST_RECOMMEND_HISTORY = "20";
        public static final String REQUEST_RECOMMEND_MUST_APP = "3002";
        public static final String REQUEST_RECOMMEND_MUST_HAVE_GAME = "505";
        public static final String REQUEST_RECOMMEND_SIMILAR_APP = "32";
        public static final String REQUEST_RECOMMOND_GAEME = "40";
        public static final String REQUEST_RESULTPAGE_BANNER_AD = "4004";
        public static final String REQUEST_SEARCH_HOTKEY = "16002";
        public static final String REQUEST_SEARCH_HOTKEY_GAME = "16012";
        public static final String REQUEST_SEARCH_RECOM = "16003";
        public static final String REQUEST_SEARCH_RECOM_GAME = "16014";
        public static final String REQUEST_SEARCH_RESULT = "16";
        public static final String REQUEST_SEARCH_RESULT_GAME = "16013";
        public static final String REQUEST_SEARCH_SUGGEST = "16001";
        public static final String REQUEST_SEARCH_SUGGEST_GAME = "16011";
        public static final String REQUEST_UNINSTALL_RECOMMEND_FROM_5_4 = "3001";
        private static final int TEST_PARAM_KEY = 0;
        private static final int TEST_PARAM_VALUE = 1;
        public String mPosId = "";

        public static boolean isAppRequest(String requestPosId) {
            if (REQUEST_GAME_CATEGORY.equals(requestPosId) || REQUEST_APPS_CATEGORY.equals(requestPosId)) {
                return false;
            }
            return true;
        }

        public static MarketRequestBuilder CREATOR() {
            return new MarketRequestBuilder();
        }

        public MarketRequestBuilder() {
            int i;
            Context context = c.getInstance().getContext();
            String j = a.j(context);
            String k = a.k(context);
            mid(c.getInstance().getMid());
            sdkt(1);
            lan(String.format("%s_%s", new Object[]{j, k}));
            brand(com.cleanmaster.functionevent.report.a.a("ro.product.brand", "unknow"));
            model(com.cleanmaster.functionevent.report.a.a("ro.product.model", "unknow"));
            androidid(a.a());
            cver(c.getInstance().getmAdResourceRp() == 1 ? a.a(context, context.getPackageName()) : c.getInstance().getmCver());
            mcc(a.g(context));
            mnc(a.h(context));
            ov(VERSION.SDK_INT);
            if (o.a(context)) {
                i = 1;
            } else {
                i = 2;
            }
            nt(i);
            ch(a.b());
            resolution(a.e(context));
            dpi(a.f(context));
            mem_size();
            setGooglePlayAdvertisingId();
            pl();
            sspId(c.getInstance().getSSPId());
            if (c.getInstance().isCnVersion()) {
                setDeviceParms(context);
            }
            setCapacityParms();
            if (c.getInstance().isDebug()) {
                setOfferidParams();
            }
        }

        private MarketRequestBuilder setOfferidParams() {
            String offeridParams = getOfferidParams();
            if (!TextUtils.isEmpty(offeridParams)) {
                this.qparams.add(new BasicNameValuePair("test_appid", offeridParams));
            }
            return this;
        }

        private String getOfferidParams() {
            return b.h();
        }

        private MarketRequestBuilder setCapacityParms() {
            String capacityParms = getCapacityParms();
            if (!TextUtils.isEmpty(capacityParms)) {
                this.qparams.add(new BasicNameValuePair("capacity", capacityParms));
            }
            return this;
        }

        private MarketRequestBuilder setDeviceParms(Context context) {
            try {
                String a = j.a(g.a("7069636b733230313531313034".getBytes(), ("attach=" + getDeviceParms(context)).getBytes(Defaults.ENCODING_UTF_8)));
                if (!TextUtils.isEmpty(a)) {
                    this.qparams.add(new BasicNameValuePair("append", a));
                }
            } catch (Exception e) {
            }
            return this;
        }

        private String getDeviceParms(Context context) {
            return a.i(context);
        }

        private String getCapacityParms() {
            return a.d();
        }

        public MarketRequestBuilder mem_size() {
            this.qparams.add(new BasicNameValuePair("mem_size", String.valueOf(com.cleanmaster.functionevent.report.a.a(com.cleanmaster.func.process.b.a()))));
            return this;
        }

        public MarketRequestBuilder setGooglePlayAdvertisingId() {
            String gAId = AdvertisingIdHelper.getInstance().getGAId();
            if (!TextUtils.isEmpty(gAId)) {
                this.qparams.add(new BasicNameValuePair("gaid", gAId));
            }
            return this;
        }

        public MarketRequestBuilder mid(String mid) {
            this.qparams.add(new BasicNameValuePair("mid", String.valueOf(mid)));
            return this;
        }

        public MarketRequestBuilder posid(String posid) {
            if (this.mPosId == null || !this.mPosId.equals(posid)) {
                this.mPosId = posid;
                this.qparams.add(new BasicNameValuePair("posid", String.valueOf(posid)));
            }
            return this;
        }

        public MarketRequestBuilder androidid(String androidid) {
            this.qparams.add(new BasicNameValuePair("androidid", String.valueOf(androidid)));
            return this;
        }

        public MarketRequestBuilder cver(int cver) {
            this.qparams.add(new BasicNameValuePair("cver", String.valueOf(cver)));
            return this;
        }

        public MarketRequestBuilder adn(int adn) {
            this.qparams.add(new BasicNameValuePair("adn", String.valueOf(adn)));
            return this;
        }

        public MarketRequestBuilder g_pg(int g_pg) {
            this.qparams.add(new BasicNameValuePair("g_pg", String.valueOf(g_pg)));
            return this;
        }

        public MarketRequestBuilder pg(int pg) {
            this.qparams.add(new BasicNameValuePair("pg", String.valueOf(pg)));
            return this;
        }

        public MarketRequestBuilder w(int w) {
            this.qparams.add(new BasicNameValuePair(MASTNativeAdConstants.NATIVE_IMAGE_W, String.valueOf(w)));
            return this;
        }

        public MarketRequestBuilder h(int h) {
            this.qparams.add(new BasicNameValuePair(MASTNativeAdConstants.NATIVE_IMAGE_H, String.valueOf(h)));
            return this;
        }

        public MarketRequestBuilder ov(int ov) {
            this.qparams.add(new BasicNameValuePair("ov", String.valueOf(ov)));
            return this;
        }

        public MarketRequestBuilder nt(int nt) {
            this.qparams.add(new BasicNameValuePair("nt", String.valueOf(nt)));
            return this;
        }

        public MarketRequestBuilder mcc(String mcc) {
            this.qparams.add(new BasicNameValuePair(MASTNativeAdConstants.TELEPHONY_MCC, mcc));
            return this;
        }

        public MarketRequestBuilder mnc(String mnc) {
            this.qparams.add(new BasicNameValuePair(MASTNativeAdConstants.TELEPHONY_MNC, mnc));
            return this;
        }

        public MarketRequestBuilder pl() {
            this.qparams.add(new BasicNameValuePair("pl", "2"));
            return this;
        }

        public MarketRequestBuilder query(String packageName) {
            this.qparams.add(new BasicNameValuePair("query", packageName));
            return this;
        }

        public MarketRequestBuilder brand(String brand) {
            this.qparams.add(new BasicNameValuePair("brand", String.valueOf(brand)));
            return this;
        }

        public MarketRequestBuilder model(String model) {
            this.qparams.add(new BasicNameValuePair("model", String.valueOf(model)));
            return this;
        }

        public MarketRequestBuilder ch(String ch) {
            this.qparams.add(new BasicNameValuePair("ch", String.valueOf(ch)));
            return this;
        }

        public MarketRequestBuilder lan(String lan) {
            this.qparams.add(new BasicNameValuePair("lan", String.valueOf(lan)));
            return this;
        }

        public MarketRequestBuilder dpi(float dpi) {
            this.qparams.add(new BasicNameValuePair("dpi", String.valueOf(dpi)));
            return this;
        }

        public MarketRequestBuilder resolution(String resolution) {
            this.qparams.add(new BasicNameValuePair("resolution", String.valueOf(resolution)));
            return this;
        }

        public MarketRequestBuilder sdkt(int sdkt) {
            this.qparams.add(new BasicNameValuePair("sdkt", String.valueOf(sdkt)));
            return this;
        }

        public MarketRequestBuilder offset(int offset) {
            this.qparams.add(new BasicNameValuePair(VastIconXmlManager.OFFSET, String.valueOf(offset)));
            return this;
        }

        public MarketRequestBuilder sessionID(int sessionID) {
            this.qparams.add(new BasicNameValuePair("sessionid", String.valueOf(sessionID)));
            return this;
        }

        public MarketRequestBuilder sspId(int sspId) {
            if (sspId != -1) {
                this.qparams.add(new BasicNameValuePair("sspid", String.valueOf(sspId)));
            }
            return this;
        }

        public MarketRequestBuilder addParams(String key, Object value) {
            this.qparams.add(new BasicNameValuePair(key, String.valueOf(value)));
            return this;
        }

        public URI toURI() {
            try {
                String str = Constants.HTTP;
                String str2 = this.mHost;
                String str3 = "/b/";
                if (!com.cleanmaster.ui.app.market.c.a()) {
                    str = b.c();
                    if (!(b.f.equalsIgnoreCase(str) || b.e.equalsIgnoreCase(str))) {
                        str = Constants.HTTPS;
                    }
                }
                PicksConfig config = c.getInstance().getConfig();
                if (!(config == null || TextUtils.isEmpty(config.server_request))) {
                    str = Constants.HTTP;
                    str2 = config.server_request;
                }
                return URIUtils.createURI(str, str2, this.mPort, str3, URLEncodedUtils.format(this.qparams, Defaults.ENCODING_UTF_8), null);
            } catch (Exception e) {
                Log.e(c.TAG, "fail to generate uri");
                return null;
            }
        }

        private void addDebugParams() {
            String debugParams = getDebugParams();
            if (!TextUtils.isEmpty(debugParams)) {
                String[] split = debugParams.split(MASTNativeAdConstants.AMPERSAND);
                if (split.length > 0) {
                    for (String split2 : split) {
                        String[] split3 = split2.split(MASTNativeAdConstants.EQUAL);
                        if (split3.length == 2) {
                            this.qparams.add(new BasicNameValuePair(split3[0], split3[1]));
                        }
                    }
                }
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:31:0x0055 A:{SYNTHETIC, Splitter:B:31:0x0055} */
        /* JADX WARNING: Removed duplicated region for block: B:34:0x005a A:{Catch:{ FileNotFoundException -> 0x004b, IOException -> 0x005e }} */
        /* JADX WARNING: Removed duplicated region for block: B:22:0x0042 A:{SYNTHETIC, Splitter:B:22:0x0042} */
        /* JADX WARNING: Removed duplicated region for block: B:25:0x0047 A:{Catch:{ FileNotFoundException -> 0x004b, IOException -> 0x005e }} */
        /* JADX WARNING: Removed duplicated region for block: B:31:0x0055 A:{SYNTHETIC, Splitter:B:31:0x0055} */
        /* JADX WARNING: Removed duplicated region for block: B:34:0x005a A:{Catch:{ FileNotFoundException -> 0x004b, IOException -> 0x005e }} */
        private java.lang.String getDebugParams() {
            /*
            r4 = this;
            r0 = 0;
            r1 = android.os.Environment.getExternalStorageDirectory();
            if (r1 == 0) goto L_0x0039;
        L_0x0007:
            r2 = r1.exists();
            if (r2 == 0) goto L_0x0039;
        L_0x000d:
            r2 = new java.io.File;
            r3 = "ksmobile.txt";
            r2.<init>(r1, r3);
            r1 = r2.exists();
            if (r1 == 0) goto L_0x0039;
        L_0x001a:
            r1 = r2.isFile();
            if (r1 == 0) goto L_0x0039;
        L_0x0020:
            r3 = new java.io.FileInputStream;	 Catch:{ Exception -> 0x003a, all -> 0x0050 }
            r3.<init>(r2);	 Catch:{ Exception -> 0x003a, all -> 0x0050 }
            r2 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x0068, all -> 0x0063 }
            r1 = new java.io.InputStreamReader;	 Catch:{ Exception -> 0x0068, all -> 0x0063 }
            r1.<init>(r3);	 Catch:{ Exception -> 0x0068, all -> 0x0063 }
            r2.<init>(r1);	 Catch:{ Exception -> 0x0068, all -> 0x0063 }
            r0 = r2.readLine();	 Catch:{ Exception -> 0x006b }
            r2.close();	 Catch:{ FileNotFoundException -> 0x004b, IOException -> 0x005e }
            r3.close();	 Catch:{ FileNotFoundException -> 0x004b, IOException -> 0x005e }
        L_0x0039:
            return r0;
        L_0x003a:
            r1 = move-exception;
            r2 = r0;
            r3 = r0;
        L_0x003d:
            r1.printStackTrace();	 Catch:{ all -> 0x0066 }
            if (r2 == 0) goto L_0x0045;
        L_0x0042:
            r2.close();	 Catch:{ FileNotFoundException -> 0x004b, IOException -> 0x005e }
        L_0x0045:
            if (r3 == 0) goto L_0x0039;
        L_0x0047:
            r3.close();	 Catch:{ FileNotFoundException -> 0x004b, IOException -> 0x005e }
            goto L_0x0039;
        L_0x004b:
            r1 = move-exception;
            r1.printStackTrace();
            goto L_0x0039;
        L_0x0050:
            r1 = move-exception;
            r2 = r0;
            r3 = r0;
        L_0x0053:
            if (r2 == 0) goto L_0x0058;
        L_0x0055:
            r2.close();	 Catch:{ FileNotFoundException -> 0x004b, IOException -> 0x005e }
        L_0x0058:
            if (r3 == 0) goto L_0x005d;
        L_0x005a:
            r3.close();	 Catch:{ FileNotFoundException -> 0x004b, IOException -> 0x005e }
        L_0x005d:
            throw r1;	 Catch:{ FileNotFoundException -> 0x004b, IOException -> 0x005e }
        L_0x005e:
            r1 = move-exception;
            r1.printStackTrace();
            goto L_0x0039;
        L_0x0063:
            r1 = move-exception;
            r2 = r0;
            goto L_0x0053;
        L_0x0066:
            r1 = move-exception;
            goto L_0x0053;
        L_0x0068:
            r1 = move-exception;
            r2 = r0;
            goto L_0x003d;
        L_0x006b:
            r1 = move-exception;
            goto L_0x003d;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.cleanmaster.ui.app.market.transport.CmMarketHttpClient$MarketRequestBuilder.getDebugParams():java.lang.String");
        }

        public String toString() {
            return String.valueOf(toURI());
        }
    }

    private CmMarketHttpClient() {
    }

    public static CmMarketHttpClient a() {
        return a;
    }

    public boolean a(String str) {
        String a = a(null, b.j + str, true);
        if (a == null) {
            return false;
        }
        return b.a(a);
    }

    public MarketResponse a(String str, URI uri) {
        if (uri == null) {
            return null;
        }
        String a = a(null, uri.toASCIIString(), true);
        if (a != null) {
            return MarketResponse.fromJson(str, a);
        }
        return null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0032 A:{SYNTHETIC, Splitter:B:18:0x0032} */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x003c A:{Catch:{ Exception -> 0x0036 }} */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0044 A:{Catch:{ Exception -> 0x0036 }} */
    public static java.lang.String a(java.io.InputStream r5) {
        /*
        r0 = 2048; // 0x800 float:2.87E-42 double:1.0118E-320;
        r3 = new char[r0];
        r2 = 0;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r1 = new java.io.InputStreamReader;	 Catch:{ Exception -> 0x002b, OutOfMemoryError -> 0x0038, all -> 0x0040 }
        r0 = "utf-8";
        r1.<init>(r5, r0);	 Catch:{ Exception -> 0x002b, OutOfMemoryError -> 0x0038, all -> 0x0040 }
        r0 = 0;
        r2 = 2048; // 0x800 float:2.87E-42 double:1.0118E-320;
        r0 = r1.read(r3, r0, r2);	 Catch:{ Exception -> 0x004c, OutOfMemoryError -> 0x004a }
    L_0x0018:
        if (r0 <= 0) goto L_0x0023;
    L_0x001a:
        r2 = 0;
        r4.append(r3, r2, r0);	 Catch:{ Exception -> 0x004c, OutOfMemoryError -> 0x004a }
        r0 = r1.read(r3);	 Catch:{ Exception -> 0x004c, OutOfMemoryError -> 0x004a }
        goto L_0x0018;
    L_0x0023:
        r1.close();	 Catch:{ Exception -> 0x0036 }
    L_0x0026:
        r0 = r4.toString();
        return r0;
    L_0x002b:
        r0 = move-exception;
        r1 = r2;
    L_0x002d:
        r0.printStackTrace();	 Catch:{ all -> 0x0048 }
        if (r1 == 0) goto L_0x0026;
    L_0x0032:
        r1.close();	 Catch:{ Exception -> 0x0036 }
        goto L_0x0026;
    L_0x0036:
        r0 = move-exception;
        goto L_0x0026;
    L_0x0038:
        r0 = move-exception;
        r1 = r2;
    L_0x003a:
        if (r1 == 0) goto L_0x0026;
    L_0x003c:
        r1.close();	 Catch:{ Exception -> 0x0036 }
        goto L_0x0026;
    L_0x0040:
        r0 = move-exception;
        r1 = r2;
    L_0x0042:
        if (r1 == 0) goto L_0x0047;
    L_0x0044:
        r1.close();	 Catch:{ Exception -> 0x0036 }
    L_0x0047:
        throw r0;	 Catch:{ Exception -> 0x0036 }
    L_0x0048:
        r0 = move-exception;
        goto L_0x0042;
    L_0x004a:
        r0 = move-exception;
        goto L_0x003a;
    L_0x004c:
        r0 = move-exception;
        goto L_0x002d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cleanmaster.ui.app.market.transport.CmMarketHttpClient.a(java.io.InputStream):java.lang.String");
    }

    public static String a(HttpClient httpClient, String str, boolean z) {
        String str2 = null;
        if (httpClient == null) {
            httpClient = b();
        }
        System.currentTimeMillis();
        com.cleanmaster.net.a aVar = new com.cleanmaster.net.a();
        try {
            HttpGet httpGet = new HttpGet(str);
            if (z) {
                httpGet.setHeader("Accept-Encoding", "gzip");
            }
            HttpResponse execute = httpClient.execute(httpGet);
            int statusCode = execute.getStatusLine().getStatusCode();
            aVar.a(execute);
            if (statusCode == CtaButton.WIDTH_DIPS) {
                InputStream inputStream;
                InputStream content = execute.getEntity().getContent();
                Header firstHeader = execute.getFirstHeader("Content-Encoding");
                if (firstHeader == null || !firstHeader.getValue().equalsIgnoreCase("gzip")) {
                    inputStream = content;
                } else {
                    inputStream = new GZIPInputStream(new BufferedInputStream(content));
                }
                str2 = a(inputStream);
                if (httpClient != null) {
                    httpClient.getConnectionManager().shutdown();
                }
                System.currentTimeMillis();
            }
        } catch (Exception e) {
        } finally {
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
            System.currentTimeMillis();
        }
        return str2;
    }

    protected static HttpClient b() {
        HttpParams basicHttpParams = new BasicHttpParams();
        ConnManagerParams.setMaxTotalConnections(basicHttpParams, 1);
        ConnManagerParams.setTimeout(basicHttpParams, 20000);
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT);
        HttpConnectionParams.setSoTimeout(basicHttpParams, BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT);
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient(b.a(basicHttpParams), basicHttpParams);
        defaultHttpClient.getParams().setParameter("http.useragent", b.b());
        return defaultHttpClient;
    }
}
