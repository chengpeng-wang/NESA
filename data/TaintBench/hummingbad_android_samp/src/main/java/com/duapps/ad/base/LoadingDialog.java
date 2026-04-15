package com.duapps.ad.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoadingDialog extends Dialog {
    private TextView a;
    private String b;
    private Context c;
    private ProgressBar d = new ProgressBar(this.c);

    public LoadingDialog(Context context) {
        super(context);
        this.c = context;
        requestWindowFeature(1);
        getWindow().setBackgroundDrawableResource(17170445);
        LinearLayout linearLayout = new LinearLayout(this.c);
        linearLayout.setOrientation(1);
        linearLayout.setGravity(1);
        linearLayout.addView(this.d);
        this.a = new TextView(this.c);
        this.a.setTextAppearance(this.c, 16973894);
        this.a.setTextColor(Color.parseColor("#b3b3b3"));
        this.a.setText("Switching to Google Play...");
        linearLayout.addView(this.a);
        setContentView(linearLayout);
    }

    public void setMessage(int i) {
        this.b = this.c.getString(i);
        if (isShowing()) {
            a();
        }
    }

    private void a() {
        this.a.setText(this.b);
    }

    public void setMessage(String str) {
        this.b = str;
        if (isShowing()) {
            a();
        }
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 84) {
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }
}
