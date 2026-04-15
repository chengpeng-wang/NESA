package edu.cs4541.a8;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

/**
 * This sample activity was created to research the following: 
 * https://www.securecoding.cert.org/confluence/pages/viewpage.action?pageId=134636833
 */
public class MainActivity extends Activity {
    private static final String TAG = "SampleAppActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String dataString = intent.getStringExtra(Intent.EXTRA_TEXT);
        if(dataString.startsWith("content://")){
            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
        }
        if(dataString.length() > 128){
            // truncate the log!
            dataString = dataString.substring(0, 128);
        }
        
        // log the text
        Log.i(TAG, dataString);
    }
} 