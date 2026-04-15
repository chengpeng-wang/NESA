package gr.georkouk.kastorakiacounter_new;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;

public class ActHelp extends AppCompatActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.act_help);
        setTitle(R.string.str_act_help);
        ((AdView) findViewById(R.id.adView)).loadAd(new Builder().build());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.act_game_menu, menu);
        menu.findItem(R.id.menuHelp).setIcon(17301569);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuHelp /*2131558652*/:
                showDetails();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDetails() {
        String app_ver = "";
        try {
            app_ver = app_ver + getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        new AlertDialog.Builder(this).setNegativeButton(R.string.str_close, null).setMessage(getResources().getString(R.string.str_details) + "\n\n\n" + getResources().getString(R.string.str_version_number) + app_ver + " \n\n" + getResources().getString(R.string.str_contact) + "\nfamoussofthouse@gmail.com\n").show();
    }
}
