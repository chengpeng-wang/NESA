package com.address.core.utilities;

import android.net.NetworkInfo;
import com.address.core.RunService;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkState {
    public static Boolean isOnline() {
        NetworkInfo activeNetwork = RunService.getService().getConnectivityManager().getActiveNetworkInfo();
        if (activeNetwork == null || !activeNetwork.isConnected()) {
            return Boolean.valueOf(false);
        }
        return Boolean.valueOf(true);
    }

    public static Boolean ping(String address, int timeout) {
        try {
            return Boolean.valueOf(InetAddress.getByName(address).isReachable(timeout));
        } catch (UnknownHostException ex) {
            Logger.getLogger(NetworkState.class.getName()).log(Level.SEVERE, null, ex);
            return Boolean.valueOf(false);
        } catch (IOException ex2) {
            Logger.getLogger(NetworkState.class.getName()).log(Level.SEVERE, null, ex2);
            return Boolean.valueOf(false);
        }
    }
}
