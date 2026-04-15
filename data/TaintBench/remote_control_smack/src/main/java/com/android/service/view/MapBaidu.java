package com.android.service.view;

import android.app.Activity;
import android.os.Bundle;
import com.android.service.R;
import com.xmpp.client.util.AppPreferences;
import com.xmpp.client.util.PushDialogModle;
import com.xmpp.client.util.ValueUtiles;

public class MapBaidu extends Activity {
    private AppPreferences _appPrefs;
    String hostname = "";
    private double latitude = 0.0d;
    private double longitude = 0.0d;
    private MapDataBean mapdata;
    private PushDialogModle pdm;
    String serVersion;
    String strSelect_Name;
    String strSelect_Pairnum;
    String urlAPI = null;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        this.hostname = getString(R.string.hostname);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.strSelect_Name = extras.getString("select_Name");
        }
        this.hostname = getString(R.string.hostname);
        this._appPrefs = new AppPreferences(this);
        this.pdm = new PushDialogModle(this);
        this.strSelect_Pairnum = this._appPrefs.getPairnum();
        String serVersion_Got = this._appPrefs.getUserVersion();
        if (serVersion_Got == null || serVersion_Got.length() <= 0) {
            this.serVersion = ValueUtiles.UserVersion(this.hostname, this._appPrefs.getMainnum(), this._appPrefs.getpsw());
            this._appPrefs.saveUserVersion(this.serVersion);
        } else {
            this.serVersion = serVersion_Got;
        }
        this.mapdata = new MapDataBean(this, this.serVersion, this.strSelect_Pairnum);
        this.mapdata.UpdataJason(this.hostname, "baidu");
    }
}
