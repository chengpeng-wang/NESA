package com.xmpp.client.util;

import android.content.Context;
import com.android.service.R;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class XmppTool {
    private static XMPPConnection con = null;
    private String hostname = null;

    public XmppTool(Context context) {
        this.hostname = context.getString(R.string.hostname);
    }

    private void openConnection() {
        try {
            con = new XMPPConnection(new ConnectionConfiguration(this.hostname, 5222));
            con.connect();
        } catch (XMPPException xe) {
            xe.printStackTrace();
        }
    }

    public void connectionClosedOnError(Exception arg0) {
        if (con == null) {
            openConnection();
        }
    }

    public XMPPConnection getConnection() {
        if (con == null) {
            openConnection();
        }
        return con;
    }

    public void closeConnection() {
        if (con != null) {
            con.disconnect();
            con = null;
        }
    }
}
