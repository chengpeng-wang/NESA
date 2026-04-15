package ca.ji.no.method10;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;
import com.baidu.android.pushservice.PushConstants;

public class CustomActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources resource = getResources();
        String pkgName = getPackageName();
        setContentView(resource.getIdentifier("custom_activity", "layout", pkgName));
        TextView titleView = (TextView) findViewById(resource.getIdentifier("title", "id", pkgName));
        TextView contentView = (TextView) findViewById(resource.getIdentifier("content", "id", pkgName));
        Intent intent = getIntent();
        String title = intent.getStringExtra(PushConstants.EXTRA_NOTIFICATION_TITLE);
        String content = intent.getStringExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT);
        titleView.setText(title);
        contentView.setText(content);
    }
}
