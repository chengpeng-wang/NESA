package com.feedback.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import com.feedback.a.a;
import com.feedback.b.c;
import com.feedback.b.d;
import com.mobclick.android.UmengConstants;
import com.mobclick.android.l;
import org.json.JSONObject;

public class SendFeedback extends Activity {
    static boolean a = true;
    /* access modifiers changed from: private */
    public Spinner b;
    /* access modifiers changed from: private */
    public Spinner c;
    /* access modifiers changed from: private */
    public EditText d;
    private TextView e;
    private TextView f;
    private ImageButton g;
    /* access modifiers changed from: private */
    public JSONObject h;

    private void a() {
        ArrayAdapter arrayAdapter;
        this.b = (Spinner) findViewById(l.a(this, "id", "umeng_analyse_feedback_age_spinner"));
        this.c = (Spinner) findViewById(l.a(this, "id", "umeng_analyse_feedback_gender_spinner"));
        this.e = (TextView) findViewById(l.a(this, "id", "umeng_analyse_feedback_submit"));
        this.d = (EditText) findViewById(l.a(this, "id", "umeng_analyse_feedback_content"));
        this.f = (TextView) findViewById(l.a(this, "id", "umeng_analyse_feedback_umeng_title"));
        this.g = (ImageButton) findViewById(l.a(this, "id", "umeng_analyse_feedback_see_list_btn"));
        if (this.b != null) {
            arrayAdapter = new ArrayAdapter(this, 17367048, getResources().getStringArray(l.a(this, "array", "UMageList")));
            arrayAdapter.setDropDownViewResource(17367049);
            this.b.setAdapter(arrayAdapter);
        }
        if (this.c != null) {
            arrayAdapter = new ArrayAdapter(this, 17367048, getResources().getStringArray(l.a(this, "array", "UMgenderList")));
            arrayAdapter.setDropDownViewResource(17367049);
            this.c.setAdapter(arrayAdapter);
        }
        if (this.g != null) {
            this.g.setOnClickListener(new i(this));
        }
        b();
        c();
    }

    private void b() {
        if (this.f != null) {
            this.f.setText(getString(l.a(this, "string", "UMFeedbackUmengTitle")));
        }
        if (this.d != null) {
            this.d.setHint(getString(l.a(this, "string", "UMFeedbackContent")));
        }
        if (this.e != null) {
            this.e.setText(getString(l.a(this, "string", "UMFeedbackSummit")));
        }
    }

    private void c() {
        int d;
        String stringExtra = getIntent().getStringExtra(UmengConstants.AtomKey_SequenceNum);
        if (!(stringExtra == null || this.d == null)) {
            String string = getSharedPreferences("fail", 0).getString(stringExtra, null);
            if (!d.a(string)) {
                try {
                    this.d.setText(new a(new JSONObject(string)).a());
                    c.a((Context) this, "fail", stringExtra);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (this.b != null) {
            d = d();
            if (d != -1) {
                this.b.setSelection(d);
            }
        }
        if (this.c != null) {
            d = e();
            if (d != -1) {
                this.c.setSelection(d);
            }
        }
    }

    private int d() {
        return getSharedPreferences(UmengConstants.PreName_Trivial, 0).getInt(UmengConstants.TrivialPreKey_AgeGroup, -1);
    }

    private int e() {
        return getSharedPreferences(UmengConstants.PreName_Trivial, 0).getInt(UmengConstants.TrivialPreKey_Sex, -1);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(l.a(this, "layout", "umeng_analyse_send_feedback"));
        a();
        if (this.e != null) {
            this.e.setOnClickListener(new j(this, null));
            if (this.d != null) {
                ((InputMethodManager) getSystemService("input_method")).toggleSoftInput(2, 0);
            }
        }
    }
}
