package sx.jolly.core;

import android.content.Context;
import sx.jolly.utils.SMS;
import sx.jolly.utils.ServerManager;
import sx.jolly.utils.Utils;

public class Command {
    private SingleCommand command = null;
    private Context context = null;

    public Command(Context context, SingleCommand command) {
        this.context = context;
        setCommand(command);
    }

    private SingleCommand getCommand() {
        return this.command;
    }

    private void setCommand(SingleCommand command) {
        this.command = command;
    }

    public void execute() {
        SingleCommand command = getCommand();
        String value = command.findProperty("command").getValue();
        Utils.slog(Command.class, "command: " + value);
        if (value.equals("sms")) {
            new SMS(this.context).sendSMS(getCommand());
        }
        if (value.equals("savebackups")) {
            new ServerManager(this.context).save(command);
        }
    }
}
