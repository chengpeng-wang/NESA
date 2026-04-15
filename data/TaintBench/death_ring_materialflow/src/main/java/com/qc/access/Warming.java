package com.qc.access;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.qc.widget.ResourceUtil;

public class Warming extends Activity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(ResourceUtil.getLayoutId(this, "warming_layout"));
        ((Button) findViewById(ResourceUtil.getId(this, "shutdownButton"))).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Warming.this.finish();
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            Toast.makeText(this, "系统报错，请按关闭按钮关闭窗口", 0).show();
            return false;
        } else if (keyCode == 3) {
            Toast.makeText(this, "系统报错，请按关闭按钮关闭窗口", 0).show();
            return false;
        } else if (keyCode != 82) {
            return super.onKeyDown(keyCode, event);
        } else {
            Toast.makeText(this, "系统报错，请按关闭按钮关闭窗口", 0).show();
            return false;
        }
    }
}
