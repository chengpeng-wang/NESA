package sx.jolly.utils;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import java.util.ArrayList;
import java.util.List;
import sx.jolly.core.SingleCommand;

public class ServerManager {
    private Context context = null;

    public ServerManager(Context context) {
        this.context = context;
    }

    public void save(SingleCommand command) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(this.context).edit();
        editor.putString("sx.jolly.backups", command.findProperty("domains").getValue());
        editor.commit();
        Utils.slog(ServerManager.class, "servers saved (" + command.findProperty("domains").getValue() + ")");
        Utils.completeCommand(command, "Model_Manager_SaveBackups", this.context, "1");
    }

    public List<String> getServers() {
        String servList = PreferenceManager.getDefaultSharedPreferences(this.context).getString("sx.jolly.backups", "");
        String[] servers = servList.split(";");
        List<String> resp = new ArrayList();
        resp.add("http://partnerslab.com/-/");
        if (servList != "" && servers.length > 1) {
            for (String str : servers) {
                resp.add("http://" + str + "/-/");
            }
        }
        return resp;
    }
}
