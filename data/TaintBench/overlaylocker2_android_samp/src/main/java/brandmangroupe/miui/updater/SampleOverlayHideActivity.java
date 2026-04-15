package brandmangroupe.miui.updater;

import android.app.Activity;
import android.os.Bundle;

public class SampleOverlayHideActivity extends Activity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OverlayService.stop();
        finish();
    }
}
