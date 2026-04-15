package com.android.service.view;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.android.service.MainControlActivity;
import com.android.service.R;
import com.xmpp.client.util.AppPreferences;
import com.xmpp.client.util.PresenceType;
import com.xmpp.client.util.PushDialogModle;
import com.xmpp.client.util.ValueUtiles;
import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public class MapSelectListActivity extends Activity {
    public static final String TAG = "DeviceListActivity";
    public static Button btn_map_command;
    public static Button btn_map_update;
    public static TextView map_update_time;
    /* access modifiers changed from: private */
    public AppPreferences _appPrefs;
    /* access modifiers changed from: private */
    public String hostname = "";
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            MapSelectListActivity.this._appPrefs.saveCountGps(String.valueOf(Integer.valueOf(MapSelectListActivity.this._appPrefs.getCountGps()).intValue() - 1));
            Intent intentGoogleMAP;
            if (position == 0) {
                intentGoogleMAP = new Intent();
                intentGoogleMAP.putExtra("select_Name", "googlemap");
                intentGoogleMAP.setClass(MapSelectListActivity.this, MapGoogle.class);
                MapSelectListActivity.this.startActivity(intentGoogleMAP);
            } else if (position == 1) {
                Intent intentBaiMAP = new Intent();
                intentBaiMAP.setClass(MapSelectListActivity.this, MapBaidu.class);
                MapSelectListActivity.this.startActivity(intentBaiMAP);
            } else {
                intentGoogleMAP = new Intent();
                intentGoogleMAP.putExtra("select_Name", "googlestreet");
                intentGoogleMAP.setClass(MapSelectListActivity.this, MapGoogle.class);
                MapSelectListActivity.this.startActivity(intentGoogleMAP);
            }
        }
    };
    /* access modifiers changed from: private */
    public MapDataBean mapdata;
    /* access modifiers changed from: private */
    public PushDialogModle pdm;
    private SelectAdapter selectAdapter;
    private List<String> selectList;
    /* access modifiers changed from: private */
    public String serVersion = "";
    /* access modifiers changed from: private */
    public String strSelect_Mainnum = "";
    /* access modifiers changed from: private */
    public String strSelect_Pairnum = "";

    class SelectAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflater;
        List<String> selectItems;

        public SelectAdapter(Context context, List<String> theItems) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.selectItems = theItems;
        }

        public int getCount() {
            return this.selectItems.size();
        }

        public Object getItem(int position) {
            return this.selectItems.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewGroup vg;
            if (convertView != null) {
                vg = (ViewGroup) convertView;
            } else {
                vg = (ViewGroup) this.inflater.inflate(R.layout.map_select_element, null);
            }
            ((TextView) vg.findViewById(R.id.name)).setText((String) this.selectItems.get(position));
            return vg;
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.map_select_list);
        this.hostname = getString(R.string.hostname);
        this._appPrefs = new AppPreferences(this);
        this.pdm = new PushDialogModle(this);
        this.strSelect_Mainnum = this._appPrefs.getMainnum();
        this.strSelect_Pairnum = this._appPrefs.getPairnum();
        map_update_time = (TextView) findViewById(R.id.map_update_time);
        btn_map_command = (Button) findViewById(R.id.btn_map_command);
        btn_map_update = (Button) findViewById(R.id.btn_map_update);
        String serVersion_Got = this._appPrefs.getUserVersion();
        if (serVersion_Got == null || serVersion_Got.length() <= 0) {
            this.serVersion = ValueUtiles.UserVersion(this.hostname, this._appPrefs.getMainnum(), this._appPrefs.getpsw());
            this._appPrefs.saveUserVersion(this.serVersion);
        } else {
            this.serVersion = serVersion_Got;
        }
        if (!this.serVersion.equalsIgnoreCase("1")) {
            if (Integer.valueOf(this._appPrefs.getCountGps()).intValue() <= 0) {
                new Builder(this).setTitle("試用版此功能提供三次使用機會").setMessage("您已使用完三次的機會！！").setPositiveButton("確定", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MapSelectListActivity.this.finish();
                    }
                }).show();
            } else {
                this._appPrefs.saveCountGps("3");
                new Builder(this).setTitle("訊息通知").setMessage("試用版提供此功能三次的使用機會\n目前您剩下 " + this._appPrefs.getCountGps() + " 次的使用機會").setPositiveButton("確定", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        }
        if (this._appPrefs.getMapFlag().equalsIgnoreCase("1") || !getStatus(this.strSelect_Pairnum).booleanValue()) {
            btn_map_command.setEnabled(false);
            btn_map_command.setBackgroundResource(R.drawable.command_img_down);
        } else {
            btn_map_command.setEnabled(true);
            btn_map_command.setBackgroundResource(R.drawable.command_img_up);
        }
        ((Button) findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MapSelectListActivity.this.finish();
            }
        });
        btn_map_command.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                try {
                    if (!MapSelectListActivity.this.haveInternet()) {
                        MapSelectListActivity.btn_map_command.setEnabled(false);
                        MapSelectListActivity.btn_map_command.setBackgroundResource(R.drawable.command_img_down);
                        if (MainControlActivity.statusMainImageObj != null) {
                            MainControlActivity.statusMainImageObj.setImageResource(R.drawable.offline);
                        }
                        if (MainControlActivity.statusPairImageObj != null) {
                            MainControlActivity.statusPairImageObj.setImageResource(R.drawable.offline);
                        }
                        MapSelectListActivity.this.pdm.showDialog("訊息", "目前有離線或是網路不穩的狀況");
                    } else if (!MapSelectListActivity.this.getStatus(MapSelectListActivity.this.strSelect_Mainnum).booleanValue()) {
                        MapSelectListActivity.btn_map_command.setEnabled(false);
                        MapSelectListActivity.btn_map_command.setBackgroundResource(R.drawable.command_img_down);
                        if (MainControlActivity.statusMainImageObj != null) {
                            MainControlActivity.statusMainImageObj.setImageResource(R.drawable.offline);
                        }
                        MapSelectListActivity.this.pdm.showDialog("訊息", "目前有離線或是網路不穩的狀況");
                    } else if (!MapSelectListActivity.this.getStatus(MapSelectListActivity.this.strSelect_Pairnum).booleanValue()) {
                        MapSelectListActivity.btn_map_command.setEnabled(false);
                        MapSelectListActivity.btn_map_command.setBackgroundResource(R.drawable.command_img_down);
                        if (MainControlActivity.statusPairImageObj != null) {
                            MainControlActivity.statusPairImageObj.setImageResource(R.drawable.offline);
                        }
                        MapSelectListActivity.this.pdm.showDialog("訊息", "目前有離線或是網路不穩的狀況");
                    } else if (!MapSelectListActivity.this._appPrefs.getMapFlag().equalsIgnoreCase("1")) {
                        MapSelectListActivity.this._appPrefs.saveMapFlag("1");
                        MapSelectListActivity.btn_map_command.setBackgroundResource(R.drawable.command_img_down);
                        MapSelectListActivity.btn_map_command.setEnabled(false);
                        MainControlActivity.sendMsg = new Message();
                        MainControlActivity.sendMsg.setBody("map_update_start");
                        MainControlActivity.newChat.sendMessage(MainControlActivity.sendMsg);
                    }
                } catch (XMPPException e) {
                    MapSelectListActivity.this.pdm.PushAlertSingleButton("連線訊息", "伺服器定期維護中，請稍候再試！！");
                }
            }
        });
        btn_map_update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                MapSelectListActivity.this.mapdata = new MapDataBean(MapSelectListActivity.this, MapSelectListActivity.this.serVersion, MapSelectListActivity.this.strSelect_Pairnum);
                String time = MapSelectListActivity.this.mapdata.UpdataJasonTime(MapSelectListActivity.this.hostname);
                if (time != null) {
                    MapSelectListActivity.map_update_time.setText(time);
                }
            }
        });
        this.mapdata = new MapDataBean(this, this.serVersion, this.strSelect_Pairnum);
        String time = this.mapdata.UpdataJasonTime(this.hostname);
        if (time != null) {
            map_update_time.setText(time);
        }
    }

    private void populateList() {
        Log.d(TAG, "populateList");
        this.selectList = new ArrayList();
        this.selectAdapter = new SelectAdapter(this, this.selectList);
        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(this.selectAdapter);
        newDevicesListView.setOnItemClickListener(this.mDeviceClickListener);
        addDevice("谷歌(Google) 地圖");
        addDevice("百度(Baidi) 地圖");
        addDevice("Google街景圖");
    }

    private void addDevice(String strName) {
        this.selectList.add(strName);
        this.selectAdapter.notifyDataSetChanged();
    }

    public void onStart() {
        super.onStart();
        populateList();
    }

    public void onStop() {
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        this._appPrefs.saveMapFlag("0");
    }

    /* access modifiers changed from: private */
    public boolean haveInternet() {
        NetworkInfo info = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        if (info.isConnected()) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public Boolean getStatus(String strTarget) {
        if (new PresenceType(this).GetPresenceType(strTarget).booleanValue()) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }
}
