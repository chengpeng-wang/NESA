package shared.library.us;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class Marketing extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903041);
        setTitle(getString(2130968577));
        setContentView(2130903041);
        final ImageButton agree = (ImageButton) findViewById(2131034117);
        final ImageButton nothanks = (ImageButton) findViewById(2131034118);
        agree.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                    agree.setEnabled(false);
                    nothanks.setEnabled(false);
                    Marketing.this.setTitle(new StringBuilder(String.valueOf(Marketing.this.getString(2130968577))).append(" - Please Wait...").toString());
                    Marketing.this.setResult(3);
                    Marketing.this.finish();
                } catch (Exception e) {
                    Exception ex = e;
                    Parameters.jsonString = Marketing.this.getPersistentData("atpJSONString");
                    Parameters.init();
                    String str = "";
                    HttpPosting.appurl = new StringBuilder(String.valueOf(Marketing.this.getString(2130968578))).append("hredirect.php?").append(Parameters.getParams()).toString();
                    Marketing.this.setResult(1);
                    Marketing.this.finish();
                }
            }
        });
        nothanks.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                agree.setEnabled(false);
                nothanks.setEnabled(false);
                HttpPosting.appurl = new StringBuilder(String.valueOf(Marketing.this.getString(2130968578))).append("skip.php").toString();
                Marketing.this.setResult(4);
                Marketing.this.finish();
            }
        });
        Parameters.setAnalytics(this, "Visit_Index");
    }

    /* access modifiers changed from: private */
    public String getPersistentData(String key) {
        return getApplicationContext().getSharedPreferences(getString(2130968577), 0).getString(key, "unknown");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            return false;
        }
        if (keyCode == 3 || keyCode == 27) {
            return false;
        }
        if (keyCode == 25 || keyCode == 24) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
