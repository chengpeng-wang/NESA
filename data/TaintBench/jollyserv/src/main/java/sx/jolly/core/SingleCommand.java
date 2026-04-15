package sx.jolly.core;

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class SingleCommand {
    private ArrayList<NameValuePair> params = new ArrayList();

    public SingleCommand(String resp) {
        String[] params = resp.split("&");
        String[] values = null;
        for (String split : params) {
            values = split.split("=");
            addPair(values[0], values[1]);
        }
    }

    private ArrayList<NameValuePair> getParams() {
        return this.params;
    }

    public boolean addPair(String key, String value) {
        return this.params.add(new BasicNameValuePair(key, value));
    }

    public BasicNameValuePair findProperty(String key) {
        try {
            Iterator it = getParams().iterator();
            while (it.hasNext()) {
                NameValuePair pair2 = (NameValuePair) it.next();
                if (pair2.getName().equals(key)) {
                    return new BasicNameValuePair(pair2.getName(), pair2.getValue());
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
