package com.example.bankmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import com.example.smsmanager.R;
import java.util.Timer;
import java.util.TimerTask;

public class BankSplashActivity extends Activity {
    protected int _splashTime = 3000;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Intent i = new Intent();
                    i.setClass(BankSplashActivity.this, BankPreActivity.class);
                    BankSplashActivity.this.startActivity(i);
                    BankSplashActivity.this.finish();
                    BankSplashActivity.this.overridePendingTransition(R.anim.fade, R.anim.hold);
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private Thread splashTread;
    TimerTask task;
    Timer timer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.splash);
        this.task = new TimerTask() {
            public void run() {
                Message message = new Message();
                message.what = 1;
                BankSplashActivity.this.myHandler.sendMessage(message);
            }
        };
        this.timer = new Timer(true);
        this.timer.schedule(this.task, 4000);
    }

    public boolean onTouchEvent(MotionEvent event) {
        event.getAction();
        return true;
    }
}
