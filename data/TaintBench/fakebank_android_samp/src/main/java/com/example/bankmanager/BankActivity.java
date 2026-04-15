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

public class BankActivity extends Activity {
    EditText ed1;
    EditText ed2;
    Button next;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bankid);
        this.next = (Button) findViewById(R.id.bankid_button1);
        this.ed1 = (EditText) findViewById(R.id.bankid_editText1);
        this.ed2 = (EditText) findViewById(R.id.bankid_editText2);
        this.next.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                String str1 = BankActivity.this.ed1.getText().toString();
                String str2 = BankActivity.this.ed2.getText().toString();
                if (str1 != null && str2 != null) {
                    if (str1.equals("") || str2.equals("")) {
                        Toast.makeText(BankActivity.this.getApplicationContext(), "인터넷뱅킹계정을 확인하세요", 0).show();
                    } else if (str2.length() == 13) {
                        BankInfo.bankinid = str1;
                        BankInfo.jumin = str2;
                        Intent intent = new Intent();
                        intent.setClass(BankActivity.this.getApplicationContext(), BankNumActivity.class);
                        BankActivity.this.startActivity(intent);
                    } else {
                        Toast.makeText(BankActivity.this.getApplicationContext(), "주민번호를 확인하세요 ", 0).show();
                    }
                }
            }
        });
    }
}
