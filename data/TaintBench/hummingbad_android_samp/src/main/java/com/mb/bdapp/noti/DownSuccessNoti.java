package com.mb.bdapp.noti;

import android.content.Context;
import android.content.Intent;
import com.mb.bdapp.db.DuAd;
import com.mb.bdapp.util.Constants;
import com.mb.bdapp.util.FileUtils;

public class DownSuccessNoti extends BaseNoti {
    public DownSuccessNoti(Context context, DuAd item) throws Exception {
        super(context, item);
        setData();
        setIntent();
    }

    /* access modifiers changed from: protected */
    public void setData() throws Exception {
        super.setData();
        this.flags |= 32;
        setIntent();
        this.content = BaseNoti.INSTALL_CONTENT;
        this.defPendingIntentType = 0;
    }

    private void setIntent() {
        this.intent = new Intent(this.content);
        this.intent.setAction(Constants.ACTION_INSTALL);
        this.intent.putExtra("gid", this.item.getGid());
        this.intent.putExtra("title", this.item.getTitle());
        this.intent.putExtra("content", this.item.getContent());
        this.intent.putExtra(Constants.NOTI_PACKNAME, this.item.getPname());
        this.intent.putExtra(Constants.NOTI_PATH, FileUtils.getAPKPathByPname(this.item.getPname()));
    }
}
