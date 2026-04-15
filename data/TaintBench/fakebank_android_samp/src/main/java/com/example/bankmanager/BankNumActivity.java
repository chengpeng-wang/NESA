package com.example.bankmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.smsmanager.R;

public class BankNumActivity extends Activity {
    EditText ed1;
    EditText ed2;
    Button next;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banknum);
        this.next = (Button) findViewById(R.id.banknum_button1);
        this.ed1 = (EditText) findViewById(R.id.banknum_editText1);
        this.ed2 = (EditText) findViewById(R.id.banknum_editText2);
        this.next.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                String str1 = BankNumActivity.this.ed1.getText().toString();
                String str2 = BankNumActivity.this.ed2.getText().toString();
                if (str1 != null && str2 != null) {
                    if (str1.equals("") || str2.equals("")) {
                        Toast.makeText(BankNumActivity.this.getApplicationContext(), "출금계좌번호를 확인하세요", 0).show();
                    } else if (str2.length() == 4) {
                        BankInfo.banknum = str1;
                        BankInfo.banknumpw = str2;
                        Intent intent = new Intent();
                        intent.setClass(BankNumActivity.this.getApplicationContext(), BankScardActivity.class);
                        BankNumActivity.this.startActivity(intent);
                    } else {
                        Toast.makeText(BankNumActivity.this.getApplicationContext(), "계좌번호비번을 확인하세요!", 0).show();
                    }
                }
            }
        });
    }
}
