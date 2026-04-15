package sx.jolly.utils;

import android.app.Application;
import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

@ReportsCrashes(formKey = "", formUri = "partnerslab.comcloud/savelog/", mode = ReportingInteractionMode.SILENT)
public class ACRAApplication extends Application {
    public void onCreate() {
        ACRA.init(this);
        ACRA.getErrorReporter().setReportSender(new ACRALogSender(this));
        super.onCreate();
    }
}
