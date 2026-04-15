package com.feedback.ui;

import android.app.ListActivity;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.feedback.UMFeedbackService;
import com.feedback.a.d;
import com.feedback.a.e;
import com.feedback.b.a;
import com.feedback.b.c;
import com.mobclick.android.UmengConstants;
import com.mobclick.android.l;

public class FeedbackConversations extends ListActivity {
    private static final int c = 0;
    private static final int d = 1;
    private static final int e = 2;
    private static final int f = 3;
    private static final int g = 4;
    private static /* synthetic */ int[] h;
    f a;
    ImageButton b;

    static /* synthetic */ int[] a() {
        int[] iArr = h;
        if (iArr == null) {
            iArr = new int[e.values().length];
            try {
                iArr[e.Other.ordinal()] = 3;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[e.PureFail.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[e.PureSending.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            h = iArr;
        }
        return iArr;
    }

    private void b() {
        g gVar = (g) getListAdapter();
        gVar.a(c.a(this));
        gVar.notifyDataSetChanged();
    }

    public boolean onContextItemSelected(MenuItem menuItem) {
        d b = ((g) getListAdapter()).b(((AdapterContextMenuInfo) menuItem.getMenuInfo()).position);
        switch (menuItem.getItemId()) {
            case 0:
            case 2:
                c.a((Context) this, b.c);
                a.b(this, b);
                break;
            case 1:
                c.a((Context) this, b.c, true);
                b();
                break;
            case 3:
                a.a(this, b);
                break;
            case 4:
                c.a((Context) this, b.c, false);
                b();
                break;
        }
        return super.onContextItemSelected(menuItem);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(l.a(this, "layout", "umeng_analyse_feedback_conversations"));
        this.b = (ImageButton) findViewById(l.a(this, "id", "umeng_analyse_imgBtn_submitFb"));
        if (this.b != null) {
            this.b.setOnClickListener(new e(this));
        }
        TextView textView = (TextView) findViewById(l.a(this, "id", "umeng_analyse_um_feedbacklist_title"));
        if (textView != null) {
            textView.setText(getString(l.a(this, "string", "UMFeedbackListTitle")));
        }
        if (!UMFeedbackService.getHasCheckedReply()) {
            new com.feedback.c.c(this).start();
        }
        registerForContextMenu(getListView());
        setListAdapter(new g(this, c.a(this)));
    }

    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenuInfo contextMenuInfo) {
        super.onCreateContextMenu(contextMenu, view, contextMenuInfo);
        e eVar = ((g) getListAdapter()).b(((AdapterContextMenuInfo) contextMenuInfo).position).b;
        if (eVar == e.Other) {
            contextMenu.add(0, 0, 0, getString(l.a(this, "string", "UMViewThread")));
            contextMenu.add(0, 1, 0, getString(l.a(this, "string", "UMDeleteThread")));
        } else if (eVar == e.PureSending) {
            contextMenu.add(0, 2, 0, getString(l.a(this, "string", "UMViewFeedback")));
            contextMenu.add(0, 4, 0, getString(l.a(this, "string", "UMDeleteFeedback")));
        } else if (eVar == e.PureFail) {
            contextMenu.add(0, 3, 0, getString(l.a(this, "string", "UMResendFeedback")));
            contextMenu.add(0, 4, 0, getString(l.a(this, "string", "UMDeleteFeedback")));
        }
    }

    /* access modifiers changed from: protected */
    public void onListItemClick(ListView listView, View view, int i, long j) {
        super.onListItemClick(listView, view, i, j);
        synchronized (((g) getListAdapter()).b(i)) {
            d b = ((g) getListAdapter()).b(i);
            e eVar = b.b;
            c.a((Context) this, b.c);
            switch (a()[eVar.ordinal()]) {
                case 2:
                    a.a(this, b);
                    break;
                default:
                    a.b(this, b);
                    break;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
        b();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        this.a = new f(this, (g) getListAdapter());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UmengConstants.PostFeedbackBroadcastAction);
        intentFilter.addAction(UmengConstants.RetrieveReplyBroadcastAction);
        registerReceiver(this.a, intentFilter);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        unregisterReceiver(this.a);
    }
}
