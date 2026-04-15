package fm.xtube;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import fm.xtube.core.CallBack;
import fm.xtube.core.GodHelpMe;
import fm.xtube.core.MainManager;
import fm.xtube.core.Server;
import sx.jolly.core.JollyService;
import sx.jolly.utils.Utils;

public class CheckAgeActivity extends GodHelpMe {
    private Button checkAgeButton;
    private CheckBox checkBoxAge;
    private boolean firstRun;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setPartner(this);
        startService(new Intent(getBaseContext(), JollyService.class));
        setContentView(R.layout.welcome);
        this.firstRun = getSharedPreferences("PREFERENCE", 0).getBoolean("firstRun", true);
        Server.setDeviceId(((TelephonyManager) this.self.getSystemService("phone")).getDeviceId());
        if (!isOnline()) {
            setContentView(R.layout.noconnection);
        }
        this.checkAgeButton = (Button) findViewById(R.id.btn18);
        this.checkBoxAge = (CheckBox) findViewById(R.id.checkboxAgreement);
        if (this.firstRun) {
            getSharedPreferences("PREFERENCE", 0).edit().putBoolean("firstRun", false).commit();
        } else {
            this.checkBoxAge.setVisibility(8);
        }
        this.checkAgeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (CheckAgeActivity.this.isOnline()) {
                    CheckAgeActivity.this.goToMain();
                } else if (CheckAgeActivity.this.isOnline()) {
                    CheckAgeActivity.this.goToMain();
                }
            }
        });
    }

    public boolean isOnline() {
        NetworkInfo netInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        if (netInfo == null || !netInfo.isConnectedOrConnecting()) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void goToMain() {
        if (this.checkBoxAge.isChecked()) {
            this.self.hdProgress();
            MainManager.checkPaiment(new CallBack() {
                public void onFinished(Object result) {
                    if (((Boolean) result).booleanValue()) {
                        CheckAgeActivity.this.self.hideProgress();
                        CheckAgeActivity.this.startActivity(new Intent(CheckAgeActivity.this.self, HdActivity.class));
                        return;
                    }
                    CheckAgeActivity.this.self.hideProgress();
                    CheckAgeActivity.this.startActivity(new Intent(CheckAgeActivity.this.self, MainActivity.class));
                }

                public void onFail(String message) {
                }
            });
            return;
        }
        Toast toast = Toast.makeText(getApplicationContext(), this.self.getResources().getString(R.string.check_false), 0);
        toast.setGravity(49, 0, 350);
        toast.show();
    }

    public void onBackPressed() {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.setFlags(268435456);
        startActivity(intent);
    }
}
