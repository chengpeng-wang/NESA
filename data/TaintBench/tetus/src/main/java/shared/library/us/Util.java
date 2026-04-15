package shared.library.us;

import android.content.Context;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Util {
    private static final String fileName = "temp.dat";

    public static void WriteFile(String data, Context context) {
        Exception e;
        Throwable th;
        FileOutputStream fOut = null;
        OutputStreamWriter osw = null;
        try {
            fOut = context.openFileOutput(fileName, 0);
            OutputStreamWriter osw2 = new OutputStreamWriter(fOut);
            try {
                osw2.write(data);
                osw2.flush();
                try {
                    osw2.close();
                    fOut.close();
                    osw = osw2;
                } catch (IOException e2) {
                    e2.printStackTrace();
                    osw = osw2;
                }
            } catch (Exception e3) {
                e = e3;
                osw = osw2;
                try {
                    e.printStackTrace();
                    try {
                        osw.close();
                        fOut.close();
                    } catch (IOException e22) {
                        e22.printStackTrace();
                    }
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        osw.close();
                        fOut.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                osw = osw2;
                osw.close();
                fOut.close();
                throw th;
            }
        } catch (Exception e32) {
            e = e32;
            e.printStackTrace();
            osw.close();
            fOut.close();
        }
    }

    public static String ReadSettings(Context context) {
        Exception e;
        Throwable th;
        FileInputStream fIn = null;
        InputStreamReader isr = null;
        char[] inputBuffer = new char[255];
        try {
            fIn = context.openFileInput(fileName);
            InputStreamReader isr2 = new InputStreamReader(fIn);
            try {
                isr2.read(inputBuffer);
                String data = new String(inputBuffer);
                try {
                    isr2.close();
                    fIn.close();
                    isr = isr2;
                    return data;
                } catch (IOException e2) {
                    e2.printStackTrace();
                    isr = isr2;
                    return data;
                }
            } catch (Exception e3) {
                e = e3;
                isr = isr2;
                try {
                    e.printStackTrace();
                    try {
                        isr.close();
                        fIn.close();
                        return null;
                    } catch (IOException e22) {
                        e22.printStackTrace();
                        return null;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        isr.close();
                        fIn.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                isr = isr2;
                isr.close();
                fIn.close();
                throw th;
            }
        } catch (Exception e32) {
            e = e32;
            e.printStackTrace();
            isr.close();
            fIn.close();
            return null;
        }
    }
}
