package sx.jolly.utils;

import android.content.Context;
import java.util.Iterator;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;
import sx.jolly.exceptions.NoConnectionException;

public class ACRALogSender implements ReportSender {
    private Context context;

    public ACRALogSender(Context c) {
        this.context = c;
    }

    public void send(CrashReportData arg0) throws ReportSenderException {
        Iterator it = arg0.values().iterator();
        String body = "";
        while (it.hasNext()) {
            body = new StringBuilder(String.valueOf(body)).append(it.toString()).toString();
            it.next();
        }
        try {
            new Post(new Url(Utils.CMD_SAVELOG, true, false, this.context), body).post();
            Utils.slog(ACRALogSender.class, "logs posted");
        } catch (NoConnectionException e) {
        }
    }
}
