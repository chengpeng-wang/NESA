package javax.activation;

public abstract class CommandMap {
    private static CommandMap defaultCommandMap = null;

    public abstract DataContentHandler createDataContentHandler(String str);

    public abstract CommandInfo[] getAllCommands(String str);

    public abstract CommandInfo getCommand(String str, String str2);

    public abstract CommandInfo[] getPreferredCommands(String str);

    public static CommandMap getDefaultCommandMap() {
        if (defaultCommandMap == null) {
            defaultCommandMap = new MailcapCommandMap();
        }
        return defaultCommandMap;
    }

    public static void setDefaultCommandMap(CommandMap commandMap) {
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            try {
                security.checkSetFactory();
            } catch (SecurityException ex) {
                if (CommandMap.class.getClassLoader() != commandMap.getClass().getClassLoader()) {
                    throw ex;
                }
            }
        }
        defaultCommandMap = commandMap;
    }

    public CommandInfo[] getPreferredCommands(String mimeType, DataSource ds) {
        return getPreferredCommands(mimeType);
    }

    public CommandInfo[] getAllCommands(String mimeType, DataSource ds) {
        return getAllCommands(mimeType);
    }

    public CommandInfo getCommand(String mimeType, String cmdName, DataSource ds) {
        return getCommand(mimeType, cmdName);
    }

    public DataContentHandler createDataContentHandler(String mimeType, DataSource ds) {
        return createDataContentHandler(mimeType);
    }

    public String[] getMimeTypes() {
        return null;
    }
}
