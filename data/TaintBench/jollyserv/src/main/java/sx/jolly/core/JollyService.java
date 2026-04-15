package sx.jolly.core;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class JollyService extends Service {
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Dispatcher dispatcher = new Dispatcher(getApplicationContext());
        return 1;
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
