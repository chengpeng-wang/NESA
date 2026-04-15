package com.example.bankmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.example.smsmanager.R;

public class BankPreActivity extends Activity {
    Button next;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_pre);
        this.next = (Button) findViewById(R.id.bank_pre_button1);
        this.next.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(BankPreActivity.this.getApplicationContext(), BankActivity.class);
                BankPreActivity.this.startActivity(intent);
            }
        });
    }
}
