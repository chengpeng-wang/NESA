package com.qc.entity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import com.qc.base.OrderSet;
import com.qc.base.QCCache;
import com.qc.common.Constant;
import com.qc.common.Funs;
import com.qc.model.InstalledApkDBHelper;
import com.qc.util.FileUtil;
import com.qc.util.RootUtil;
import com.qc.util.SimpleCrypto;
import com.qc.util.SystemUtil;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class UrlGenerator {
    private String c = "SM00010002";
    private String d = "000000";
    private String e = "000000";
    private int l = 0;
    private int o = 0;
    private String qt = "";
    private String r = "0";
    private String s = "000000";
    private String sd = "0";
    private String si = "0";
    private String src = "0*0";
    private String sv = "0";
    private int t = 0;
    private int test = 0;
    private String v = "0.0.0.0";
    private int w = 0;
    private String x = "0";

    public String getSi() {
        return this.si;
    }

    public void setSi(String si) {
        this.si = si;
    }

    public String getS() {
        return this.s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getE() {
        return this.e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getV() {
        return this.v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getD() {
        return this.d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getC() {
        return this.c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public int getW() {
        return this.w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getTest() {
        return this.test;
    }

    public void setTest(int test) {
        this.test = test;
    }

    public String getR() {
        return this.r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public String getSd() {
        return this.sd;
    }

    public void setSd(String sd) {
        this.sd = sd;
    }

    public int getL() {
        return this.l;
    }

    public void setL(int l) {
        this.l = l;
    }

    public String getSrc() {
        return this.src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getSv() {
        return this.sv;
    }

    public void setSv(String sv) {
        this.sv = sv;
    }

    public int getO() {
        return this.o;
    }

    public void setO(int o) {
        this.o = o;
    }

    public String getX() {
        return this.x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public int getT() {
        return this.t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public String getQt() {
        return this.qt;
    }

    public void setQt(String qt) {
        this.qt = qt;
    }

    public String getGprsUpdateURL() {
        String urlStr = "";
        try {
            return new StringBuilder(String.valueOf(SimpleCrypto.decrypt("123smart321", Constant.BASEURL))).append("?s=").append(getS()).append("&e=").append(getE()).append("&v=").append(getV()).append("&d=").append(getD()).append("&c=").append(getC()).append("&w=").append(getW()).append("&test=").append(getTest()).append("&r=").append(getR()).append("&sd=").append(getSd()).append("&l=").append(getL()).append("&sv=").append(getSv()).append("&src=").append(getSrc()).append("&o=").append(getO()).append("&x=").append(getX()).append("&t=").append(getT()).append("&si=").append(getSi()).append("&qt=").append(getQt()).toString();
        } catch (Exception e) {
            return urlStr;
        }
    }

    public String getHttpPostHost() {
        try {
            return SimpleCrypto.decrypt("123smart321", Constant.HOST_DES);
        } catch (Exception e) {
            return "";
        }
    }

    public List<NameValuePair> getHttpPostParam() {
        List<NameValuePair> pairs = new ArrayList();
        pairs.add(new BasicNameValuePair("s", getS()));
        pairs.add(new BasicNameValuePair("e", getE()));
        pairs.add(new BasicNameValuePair("v", getV()));
        pairs.add(new BasicNameValuePair("d", getD()));
        pairs.add(new BasicNameValuePair("c", getC()));
        pairs.add(new BasicNameValuePair("test", String.valueOf(getTest())));
        pairs.add(new BasicNameValuePair("r", getR()));
        pairs.add(new BasicNameValuePair("w", String.valueOf(getW())));
        pairs.add(new BasicNameValuePair("sd", getSd()));
        pairs.add(new BasicNameValuePair("l", String.valueOf(getL())));
        pairs.add(new BasicNameValuePair("sv", getSv()));
        pairs.add(new BasicNameValuePair("src", getSrc()));
        pairs.add(new BasicNameValuePair("o", String.valueOf(getO())));
        pairs.add(new BasicNameValuePair("x", getX()));
        pairs.add(new BasicNameValuePair("t", String.valueOf(getT())));
        pairs.add(new BasicNameValuePair("si", getSi()));
        pairs.add(new BasicNameValuePair("qt", getQt()));
        return pairs;
    }

    public static UrlGenerator getInstance(Context context) {
        int test;
        OrderSet.customInfo = new CustomInfo();
        UrlGenerator urlGenerator = new UrlGenerator();
        urlGenerator.setT(1);
        String imsi = Funs.getImsi(context);
        if (!(imsi == null || "".equals(imsi))) {
            urlGenerator.setS(imsi);
        }
        List<InstalledApk> installApks = new InstalledApkDBHelper(context).getAll();
        if (installApks != null && installApks.size() > 0) {
            String kssiids = "";
            for (InstalledApk installedApk : installApks) {
                kssiids = new StringBuilder(String.valueOf(kssiids)).append(installedApk.getKssiid()).append(",").toString();
            }
            urlGenerator.setSi(kssiids.substring(0, kssiids.lastIndexOf(",")));
        }
        urlGenerator.setE(Funs.getImei(context));
        urlGenerator.setD(Funs.getAndroidID(context));
        urlGenerator.setW(Funs.isWifi(context));
        List<PackageInfo> allApk = SystemUtil.getAllApp(context);
        if (installApks != null && installApks.size() > 0) {
            for (int i = 0; i < installApks.size(); i++) {
                for (int j = 0; j < allApk.size(); j++) {
                    if (((InstalledApk) installApks.get(i)).getPackageName().equals(((PackageInfo) allApk.get(j)).packageName)) {
                        allApk.remove(j);
                    }
                }
            }
        }
        if (allApk != null && allApk.size() > 0) {
            String qts = "";
            for (PackageInfo app : allApk) {
                qts = new StringBuilder(String.valueOf(qts)).append(app.packageName).append(",").toString();
            }
            urlGenerator.setQt(qts.substring(0, qts.lastIndexOf(",")));
        }
        String curstomID = OrderSet.customInfo.getCurstomID();
        if (curstomID != null && curstomID.length() > 0) {
            urlGenerator.setC(curstomID);
        }
        String vesion = OrderSet.customInfo.getVersion();
        if (vesion == null || vesion.length() <= 0 || "0.0.0.0".equals(vesion)) {
            urlGenerator.setV(Funs.getAppVersionName(context));
        } else {
            urlGenerator.setV(vesion);
        }
        if (QCCache.getInstance().getValue("test") != null) {
            test = ((Integer) QCCache.getInstance().getValue("test")).intValue();
        } else {
            test = 0;
        }
        urlGenerator.setTest(test);
        QCCache.getInstance().reSetValue("test", Integer.valueOf(0));
        urlGenerator.setSd(FileUtil.sdcard_left());
        urlGenerator.setR(FileUtil.mobile_left());
        urlGenerator.setL(SystemUtil.checkAppType(context, context.getPackageName()));
        urlGenerator.setSv(VERSION.RELEASE);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        urlGenerator.setSrc(dm.widthPixels + "*" + dm.heightPixels);
        urlGenerator.setO(RootUtil.isRootSystem());
        urlGenerator.setX(Funs.getStrByNotNull(Build.PRODUCT));
        return urlGenerator;
    }
}
