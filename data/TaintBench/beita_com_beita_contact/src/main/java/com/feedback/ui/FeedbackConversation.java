package com.feedback.ui;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.feedback.a.b;
import com.feedback.a.d;
import com.feedback.b.a;
import com.feedback.b.c;
import com.mobclick.android.UmengConstants;
import com.mobclick.android.l;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FeedbackConversation extends ListActivity {
    static Context a = null;
    static final String c = FeedbackConversation.class.getSimpleName();
    static boolean d = true;
    public static ExecutorService executorService = Executors.newFixedThreadPool(3);
    boolean b = false;
    /* access modifiers changed from: private */
    public d e;
    /* access modifiers changed from: private */
    public c f;
    private TextView g;
    /* access modifiers changed from: private */
    public EditText h;
    private Button i;
    private b j;

    private void a() {
        this.g.setText(getString(l.a(this, "string", "UMFeedbackConversationTitle")));
        this.i.setText(getString(l.a(this, "string", "UMFeedbackSummit")));
    }

    public static void setUserContext(Context context) {
        a = context;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(l.a(this, "layout", "umeng_analyse_feedback_conversation"));
        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra(UmengConstants.AtomKey_FeedbackID);
        String stringExtra2 = intent.getStringExtra(UmengConstants.AtomKey_SequenceNum);
        if (stringExtra != null) {
            this.e = c.b((Context) this, stringExtra);
        } else if (stringExtra2 != null) {
            this.e = c.b(this, stringExtra2, UmengConstants.TempPreName);
        }
        try {
            this.f = new c(this, this.e);
        } catch (Exception e) {
            Log.e(c, "In Feedback class,fail to initialize feedback adapter.");
            finish();
        }
        setListAdapter(this.f);
        setSelection(this.f.getCount() - 1);
        this.g = (TextView) findViewById(l.a(this, "id", "umeng_analyse_feedback_conversation_title"));
        this.h = (EditText) findViewById(l.a(this, "id", "umeng_analyse_editTxtFb"));
        this.i = (Button) findViewById(l.a(this, "id", "umeng_analyse_btnSendFb"));
        this.i.setOnClickListener(new a(this));
        this.h.requestFocus();
        registerForContextMenu(getListView());
        a();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i != 4) {
            return super.onKeyDown(i, keyEvent);
        }
        if (a != null) {
            a.b(a);
        } else {
            a.b(this);
        }
        finish();
        return true;
    }

    /* access modifiers changed from: protected */
    public void onListItemClick(ListView listView, View view, int i, long j) {
        super.onListItemClick(listView, view, i, j);
        com.feedback.a.a a = this.e.a(i);
        if (a.g != b.Fail) {
            return;
        }
        if (a.f == com.feedback.a.c.Starting) {
            a.a(this, this.e);
            finish();
            return;
        }
        this.h.setText(a.a());
        c.a((Context) this, this.e, i);
        this.f.a(this.e);
        this.f.notifyDataSetChanged();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        this.j = new b(this, null);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UmengConstants.PostFeedbackBroadcastAction);
        intentFilter.addAction(UmengConstants.RetrieveReplyBroadcastAction);
        registerReceiver(this.j, intentFilter);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        unregisterReceiver(this.j);
    }
}
