package install.app;

import android.content.Context;

public class WebApi {
    private MainActivity activity;
    private Context context;

    public WebApi(Context context, MainActivity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void cancelUse() {
        try {
            Settings.getSettings().save(this.context);
            this.activity.uninstall();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public WebApiResult textToCommand(String command, String params) {
        try {
            if (command.equals("webapi.cancelUse")) {
                cancelUse();
                return new WebApiResult(true, "");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new WebApiResult(false, "");
    }
}
