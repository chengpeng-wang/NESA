package com.example.smsmanager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MessageActivity extends Activity {
    Button btnExit;
    TextView messageView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);
        this.messageView = (TextView) findViewById(R.id.messageView);
        this.messageView.setText(getIntent().getExtras().getString("msg"));
        ((Button) findViewById(R.id.btnExit)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MessageActivity.this.finish();
            }
        });
    }
}
