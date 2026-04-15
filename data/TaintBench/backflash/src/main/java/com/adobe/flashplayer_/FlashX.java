package com.adobe.flashplayer_;

import android.content.Context;
import android.os.Environment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FlashX {
    public void onPreExecute(String param, String param2, Context context) {
    }

    private String getFilename(Context context, String ab) {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), ".andro_secure");
        if (!file.exists()) {
            file.mkdirs();
        }
        long rnd = System.currentTimeMillis();
        writeConfig("Av", file.getAbsolutePath() + "/" + ab + "_" + rnd + ".amr", context);
        return file.getAbsolutePath() + "/" + ab + "_" + rnd + ".amr";
    }

    private String readConfig(String config, Context context) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(config);
            if (inputStream == null) {
                return ret;
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                receiveString = bufferedReader.readLine();
                if (receiveString == null) {
                    inputStream.close();
                    return stringBuilder.toString();
                }
                stringBuilder.append(receiveString);
            }
        } catch (FileNotFoundException | IOException e) {
            return ret;
        }
    }

    private void writeConfig(String config, String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(config, 0));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
        }
    }
}
