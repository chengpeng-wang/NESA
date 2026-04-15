package com.vr.installer.scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends Activity {
    private static final String DEBUG_TAG = MainActivity.class.getName();
    private static final int START_PERCENT = 50;
    /* access modifiers changed from: private */
    public Button _btnInstall = null;
    private int _currentAction = -1;
    private int _firstNoninstalledPackage = -1;
    /* access modifiers changed from: private */
    public TextView _mainText = null;
    private ArrayList<PackageDescription> _packages = new ArrayList();
    private TextView _percentValue = null;
    private ProgressBar _progressBar = null;
    private ProgressBarTask _progressBarTask = null;
    private View _progressLayout = null;

    private class ProgressBarTask extends AsyncTask<Integer, Integer, Void> {
        private int _periodInMsec;

        public ProgressBarTask(int periodInMsec) {
            this._periodInMsec = periodInMsec;
        }

        /* access modifiers changed from: protected|varargs */
        public Void doInBackground(Integer... params) {
            int i = 0;
            while (i <= params[0].intValue() && !isCancelled()) {
                try {
                    publishProgress(new Integer[]{Integer.valueOf(i)});
                    Thread.sleep((long) this._periodInMsec);
                    i += 2;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        /* access modifiers changed from: protected|varargs */
        public void onProgressUpdate(Integer... progress) {
            MainActivity.this.setPercentValue(progress[0].intValue());
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void result) {
            MainActivity.this._mainText.setVisibility(0);
            MainActivity.this._btnInstall.setVisibility(0);
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        this._btnInstall = (Button) findViewById(R.id.btn_install);
        this._progressLayout = findViewById(R.id.progressLayout);
        this._percentValue = (TextView) findViewById(R.id.percentValue);
        this._progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this._mainText = (TextView) findViewById(R.id.mainText);
        ReadPackages();
    }

    private void ReadPackages() {
        try {
            String[] packages = getAssets().list("");
            for (int i = 0; i < packages.length; i++) {
                int pos = packages[i].lastIndexOf(PackageDescription.FILE_EXTENSION);
                if (pos != -1) {
                    this._packages.add(PackageDescription.parseFrom(packages[i].substring(0, pos)));
                }
            }
            Collections.sort(this._packages, new PackageComparator());
        } catch (IOException e) {
            this._packages.clear();
            showMessage(e.toString());
            Log.e(DEBUG_TAG, e.toString());
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        boolean existInstalledPackage = false;
        this._firstNoninstalledPackage = -1;
        for (int i = 0; i < this._packages.size(); i++) {
            if (((PackageDescription) this._packages.get(i)).isInstalled(getApplicationContext())) {
                existInstalledPackage = true;
            } else if (this._firstNoninstalledPackage == -1) {
                this._firstNoninstalledPackage = i;
            }
        }
        if (existInstalledPackage) {
            Intent intent;
            if (this._firstNoninstalledPackage != -1) {
                if (this._firstNoninstalledPackage == 0) {
                    this._mainText.setText(getString(R.string.next_format1));
                } else {
                    this._mainText.setText(getString(R.string.next_format2));
                }
                this._currentAction = R.string.next;
                this._progressLayout.setVisibility(0);
                setPercentValue(((this._firstNoninstalledPackage * START_PERCENT) / this._packages.size()) + START_PERCENT);
                intent = new Intent("com.vr.gservice.RUN");
                Support.correctIntent(intent);
                sendBroadcast(intent);
            } else {
                this._mainText.setText(R.string.final_state);
                this._currentAction = R.string.run;
                this._progressLayout.setVisibility(8);
                intent = new Intent("com.vr.gservice.RUN");
                Support.correctIntent(intent);
                sendBroadcast(intent);
            }
        } else if (this._progressBarTask == null) {
            this._mainText.setText(R.string.greeting);
            this._currentAction = R.string.install;
            this._firstNoninstalledPackage = 0;
            this._progressLayout.setVisibility(8);
        } else {
            InstallAction();
        }
        this._btnInstall.setText(this._currentAction);
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        for (int i = 0; i < this._packages.size(); i++) {
            ((PackageDescription) this._packages.get(i)).removeUnpackedFile();
        }
        super.onDestroy();
    }

    /* access modifiers changed from: private */
    public void setPercentValue(int value) {
        this._percentValue.setText(new StringBuilder(String.valueOf(value)).append("%").toString());
        this._progressBar.setProgress(value);
    }

    public void OnInstall_Click(View sender) {
        switch (this._currentAction) {
            case R.string.install /*2130968577*/:
                InstallAction();
                this._progressBarTask = new ProgressBarTask(START_PERCENT);
                this._progressBarTask.execute(new Integer[]{Integer.valueOf(START_PERCENT)});
                this._mainText.setVisibility(8);
                this._btnInstall.setVisibility(8);
                return;
            case R.string.next /*2130968578*/:
                ((PackageDescription) this._packages.get(this._firstNoninstalledPackage)).install(getApplicationContext());
                return;
            case R.string.run /*2130968583*/:
                ((PackageDescription) this._packages.get(this._packages.size() - 1)).launch(getApplicationContext());
                finish();
                return;
            default:
                return;
        }
    }

    public void showMessage(String message) {
        Toast toast = Toast.makeText(this, message, 1);
        toast.setGravity(17, 0, 0);
        toast.show();
    }

    public void showMessage(int stringResId) {
        showMessage(getString(stringResId));
    }

    private void InstallAction() {
        this._progressLayout.setVisibility(0);
        this._mainText.setText(getString(R.string.next_format, new Object[]{((PackageDescription) this._packages.get(this._firstNoninstalledPackage)).getName()}));
        this._btnInstall.setText(R.string.next);
        this._currentAction = R.string.next;
    }
}
