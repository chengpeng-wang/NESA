package javax.activation;

import com.sun.activation.registries.LogSupport;
import com.sun.activation.registries.MailcapFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MailcapCommandMap extends CommandMap {
    private static final int PROG = 0;
    private static MailcapFile defDB = null;
    private MailcapFile[] DB;

    public MailcapCommandMap() {
        MailcapFile mf;
        List dbv = new ArrayList(5);
        dbv.add(null);
        LogSupport.log("MailcapCommandMap: load HOME");
        try {
            String user_home = System.getProperty("user.home");
            if (user_home != null) {
                mf = loadFile(new StringBuilder(String.valueOf(user_home)).append(File.separator).append(".mailcap").toString());
                if (mf != null) {
                    dbv.add(mf);
                }
            }
        } catch (SecurityException e) {
        }
        LogSupport.log("MailcapCommandMap: load SYS");
        try {
            mf = loadFile(new StringBuilder(String.valueOf(System.getProperty("java.home"))).append(File.separator).append("lib").append(File.separator).append("mailcap").toString());
            if (mf != null) {
                dbv.add(mf);
            }
        } catch (SecurityException e2) {
        }
        LogSupport.log("MailcapCommandMap: load JAR");
        loadAllResources(dbv, "mailcap");
        LogSupport.log("MailcapCommandMap: load DEF");
        synchronized (MailcapCommandMap.class) {
            if (defDB == null) {
                defDB = loadResource("mailcap.default");
            }
        }
        if (defDB != null) {
            dbv.add(defDB);
        }
        this.DB = new MailcapFile[dbv.size()];
        this.DB = (MailcapFile[]) dbv.toArray(this.DB);
    }

    private MailcapFile loadResource(String name) {
        InputStream clis = null;
        try {
            clis = SecuritySupport.getResourceAsStream(getClass(), name);
            if (clis != null) {
                MailcapFile mf = new MailcapFile(clis);
                if (LogSupport.isLoggable()) {
                    LogSupport.log("MailcapCommandMap: successfully loaded mailcap file: " + name);
                }
                if (clis == null) {
                    return mf;
                }
                try {
                    clis.close();
                    return mf;
                } catch (IOException e) {
                    return mf;
                }
            }
            if (LogSupport.isLoggable()) {
                LogSupport.log("MailcapCommandMap: not loading mailcap file: " + name);
            }
            if (clis != null) {
                try {
                    clis.close();
                } catch (IOException e2) {
                }
            }
            return null;
        } catch (IOException e3) {
            if (LogSupport.isLoggable()) {
                LogSupport.log("MailcapCommandMap: can't load " + name, e3);
            }
            if (clis != null) {
                try {
                    clis.close();
                } catch (IOException e4) {
                }
            }
        } catch (SecurityException sex) {
            if (LogSupport.isLoggable()) {
                LogSupport.log("MailcapCommandMap: can't load " + name, sex);
            }
            if (clis != null) {
                try {
                    clis.close();
                } catch (IOException e5) {
                }
            }
        } catch (Throwable th) {
            if (clis != null) {
                try {
                    clis.close();
                } catch (IOException e6) {
                }
            }
        }
    }

    private void loadAllResources(List v, String name) {
        boolean anyLoaded = false;
        try {
            URL[] urls;
            ClassLoader cld = SecuritySupport.getContextClassLoader();
            if (cld == null) {
                cld = getClass().getClassLoader();
            }
            if (cld != null) {
                urls = SecuritySupport.getResources(cld, name);
            } else {
                urls = SecuritySupport.getSystemResources(name);
            }
            if (urls != null) {
                if (LogSupport.isLoggable()) {
                    LogSupport.log("MailcapCommandMap: getResources");
                }
                for (URL url : urls) {
                    InputStream clis = null;
                    if (LogSupport.isLoggable()) {
                        LogSupport.log("MailcapCommandMap: URL " + url);
                    }
                    try {
                        clis = SecuritySupport.openStream(url);
                        if (clis != null) {
                            v.add(new MailcapFile(clis));
                            anyLoaded = true;
                            if (LogSupport.isLoggable()) {
                                LogSupport.log("MailcapCommandMap: successfully loaded mailcap file from URL: " + url);
                            }
                        } else if (LogSupport.isLoggable()) {
                            LogSupport.log("MailcapCommandMap: not loading mailcap file from URL: " + url);
                        }
                        if (clis != null) {
                            try {
                                clis.close();
                            } catch (IOException e) {
                            }
                        }
                    } catch (IOException ioex) {
                        if (LogSupport.isLoggable()) {
                            LogSupport.log("MailcapCommandMap: can't load " + url, ioex);
                        }
                        if (clis != null) {
                            try {
                                clis.close();
                            } catch (IOException e2) {
                            }
                        }
                    } catch (SecurityException sex) {
                        if (LogSupport.isLoggable()) {
                            LogSupport.log("MailcapCommandMap: can't load " + url, sex);
                        }
                        if (clis != null) {
                            try {
                                clis.close();
                            } catch (IOException e3) {
                            }
                        }
                    } catch (Throwable th) {
                        if (clis != null) {
                            try {
                                clis.close();
                            } catch (IOException e4) {
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            if (LogSupport.isLoggable()) {
                LogSupport.log("MailcapCommandMap: can't load " + name, ex);
            }
        }
        if (!anyLoaded) {
            if (LogSupport.isLoggable()) {
                LogSupport.log("MailcapCommandMap: !anyLoaded");
            }
            MailcapFile mf = loadResource("/" + name);
            if (mf != null) {
                v.add(mf);
            }
        }
    }

    private MailcapFile loadFile(String name) {
        try {
            return new MailcapFile(name);
        } catch (IOException e) {
            return null;
        }
    }

    public MailcapCommandMap(String fileName) throws IOException {
        this();
        if (LogSupport.isLoggable()) {
            LogSupport.log("MailcapCommandMap: load PROG from " + fileName);
        }
        if (this.DB[0] == null) {
            this.DB[0] = new MailcapFile(fileName);
        }
    }

    public MailcapCommandMap(InputStream is) {
        this();
        LogSupport.log("MailcapCommandMap: load PROG");
        if (this.DB[0] == null) {
            try {
                this.DB[0] = new MailcapFile(is);
            } catch (IOException e) {
            }
        }
    }

    public synchronized CommandInfo[] getPreferredCommands(String mimeType) {
        List cmdList;
        int i;
        Map cmdMap;
        cmdList = new ArrayList();
        if (mimeType != null) {
            mimeType = mimeType.toLowerCase(Locale.ENGLISH);
        }
        for (i = 0; i < this.DB.length; i++) {
            if (this.DB[i] != null) {
                cmdMap = this.DB[i].getMailcapList(mimeType);
                if (cmdMap != null) {
                    appendPrefCmdsToList(cmdMap, cmdList);
                }
            }
        }
        for (i = 0; i < this.DB.length; i++) {
            if (this.DB[i] != null) {
                cmdMap = this.DB[i].getMailcapFallbackList(mimeType);
                if (cmdMap != null) {
                    appendPrefCmdsToList(cmdMap, cmdList);
                }
            }
        }
        return (CommandInfo[]) cmdList.toArray(new CommandInfo[cmdList.size()]);
    }

    private void appendPrefCmdsToList(Map cmdHash, List cmdList) {
        for (String verb : cmdHash.keySet()) {
            if (!checkForVerb(cmdList, verb)) {
                cmdList.add(new CommandInfo(verb, (String) ((List) cmdHash.get(verb)).get(0)));
            }
        }
    }

    private boolean checkForVerb(List cmdList, String verb) {
        for (CommandInfo commandName : cmdList) {
            if (commandName.getCommandName().equals(verb)) {
                return true;
            }
        }
        return false;
    }

    public synchronized CommandInfo[] getAllCommands(String mimeType) {
        List cmdList;
        int i;
        Map cmdMap;
        cmdList = new ArrayList();
        if (mimeType != null) {
            mimeType = mimeType.toLowerCase(Locale.ENGLISH);
        }
        for (i = 0; i < this.DB.length; i++) {
            if (this.DB[i] != null) {
                cmdMap = this.DB[i].getMailcapList(mimeType);
                if (cmdMap != null) {
                    appendCmdsToList(cmdMap, cmdList);
                }
            }
        }
        for (i = 0; i < this.DB.length; i++) {
            if (this.DB[i] != null) {
                cmdMap = this.DB[i].getMailcapFallbackList(mimeType);
                if (cmdMap != null) {
                    appendCmdsToList(cmdMap, cmdList);
                }
            }
        }
        return (CommandInfo[]) cmdList.toArray(new CommandInfo[cmdList.size()]);
    }

    private void appendCmdsToList(Map typeHash, List cmdList) {
        for (String verb : typeHash.keySet()) {
            for (String cmd : (List) typeHash.get(verb)) {
                cmdList.add(new CommandInfo(verb, cmd));
            }
        }
    }

    public synchronized CommandInfo getCommand(String mimeType, String cmdName) {
        CommandInfo commandInfo;
        int i;
        Map cmdMap;
        List v;
        String cmdClassName;
        if (mimeType != null) {
            mimeType = mimeType.toLowerCase(Locale.ENGLISH);
        }
        for (i = 0; i < this.DB.length; i++) {
            if (this.DB[i] != null) {
                cmdMap = this.DB[i].getMailcapList(mimeType);
                if (cmdMap != null) {
                    v = (List) cmdMap.get(cmdName);
                    if (v != null) {
                        cmdClassName = (String) v.get(0);
                        if (cmdClassName != null) {
                            commandInfo = new CommandInfo(cmdName, cmdClassName);
                            break;
                        }
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            }
        }
        for (i = 0; i < this.DB.length; i++) {
            if (this.DB[i] != null) {
                cmdMap = this.DB[i].getMailcapFallbackList(mimeType);
                if (cmdMap != null) {
                    v = (List) cmdMap.get(cmdName);
                    if (v != null) {
                        cmdClassName = (String) v.get(0);
                        if (cmdClassName != null) {
                            commandInfo = new CommandInfo(cmdName, cmdClassName);
                            break;
                        }
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            }
        }
        commandInfo = null;
        return commandInfo;
    }

    public synchronized void addMailcap(String mail_cap) {
        LogSupport.log("MailcapCommandMap: add to PROG");
        if (this.DB[0] == null) {
            this.DB[0] = new MailcapFile();
        }
        this.DB[0].appendToMailcap(mail_cap);
    }

    public synchronized DataContentHandler createDataContentHandler(String mimeType) {
        DataContentHandler dch;
        int i;
        Map cmdMap;
        List v;
        if (LogSupport.isLoggable()) {
            LogSupport.log("MailcapCommandMap: createDataContentHandler for " + mimeType);
        }
        if (mimeType != null) {
            mimeType = mimeType.toLowerCase(Locale.ENGLISH);
        }
        for (i = 0; i < this.DB.length; i++) {
            if (this.DB[i] != null) {
                if (LogSupport.isLoggable()) {
                    LogSupport.log("  search DB #" + i);
                }
                cmdMap = this.DB[i].getMailcapList(mimeType);
                if (cmdMap != null) {
                    v = (List) cmdMap.get("content-handler");
                    if (v != null) {
                        dch = getDataContentHandler((String) v.get(0));
                        if (dch != null) {
                            break;
                        }
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            }
        }
        for (i = 0; i < this.DB.length; i++) {
            if (this.DB[i] != null) {
                if (LogSupport.isLoggable()) {
                    LogSupport.log("  search fallback DB #" + i);
                }
                cmdMap = this.DB[i].getMailcapFallbackList(mimeType);
                if (cmdMap != null) {
                    v = (List) cmdMap.get("content-handler");
                    if (v != null) {
                        dch = getDataContentHandler((String) v.get(0));
                        if (dch != null) {
                            break;
                        }
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            }
        }
        dch = null;
        return dch;
    }

    private DataContentHandler getDataContentHandler(String name) {
        if (LogSupport.isLoggable()) {
            LogSupport.log("    got content-handler");
        }
        if (LogSupport.isLoggable()) {
            LogSupport.log("      class " + name);
        }
        try {
            Class cl;
            ClassLoader cld = SecuritySupport.getContextClassLoader();
            if (cld == null) {
                cld = getClass().getClassLoader();
            }
            try {
                cl = cld.loadClass(name);
            } catch (Exception e) {
                cl = Class.forName(name);
            }
            if (cl != null) {
                return (DataContentHandler) cl.newInstance();
            }
        } catch (IllegalAccessException e2) {
            if (LogSupport.isLoggable()) {
                LogSupport.log("Can't load DCH " + name, e2);
            }
        } catch (ClassNotFoundException e3) {
            if (LogSupport.isLoggable()) {
                LogSupport.log("Can't load DCH " + name, e3);
            }
        } catch (InstantiationException e4) {
            if (LogSupport.isLoggable()) {
                LogSupport.log("Can't load DCH " + name, e4);
            }
        }
        return null;
    }

    public synchronized String[] getMimeTypes() {
        List mtList;
        mtList = new ArrayList();
        for (int i = 0; i < this.DB.length; i++) {
            if (this.DB[i] != null) {
                String[] ts = this.DB[i].getMimeTypes();
                if (ts != null) {
                    for (int j = 0; j < ts.length; j++) {
                        if (!mtList.contains(ts[j])) {
                            mtList.add(ts[j]);
                        }
                    }
                }
            }
        }
        return (String[]) mtList.toArray(new String[mtList.size()]);
    }

    public synchronized String[] getNativeCommands(String mimeType) {
        List cmdList;
        cmdList = new ArrayList();
        if (mimeType != null) {
            mimeType = mimeType.toLowerCase(Locale.ENGLISH);
        }
        for (int i = 0; i < this.DB.length; i++) {
            if (this.DB[i] != null) {
                String[] cmds = this.DB[i].getNativeCommands(mimeType);
                if (cmds != null) {
                    for (int j = 0; j < cmds.length; j++) {
                        if (!cmdList.contains(cmds[j])) {
                            cmdList.add(cmds[j]);
                        }
                    }
                }
            }
        }
        return (String[]) cmdList.toArray(new String[cmdList.size()]);
    }
}
