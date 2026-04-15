package javax.activation;

import java.io.File;

public abstract class FileTypeMap {
    private static FileTypeMap defaultMap = null;

    public abstract String getContentType(File file);

    public abstract String getContentType(String str);

    public static void setDefaultFileTypeMap(FileTypeMap map) {
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            try {
                security.checkSetFactory();
            } catch (SecurityException ex) {
                if (FileTypeMap.class.getClassLoader() != map.getClass().getClassLoader()) {
                    throw ex;
                }
            }
        }
        defaultMap = map;
    }

    public static FileTypeMap getDefaultFileTypeMap() {
        if (defaultMap == null) {
            defaultMap = new MimetypesFileTypeMap();
        }
        return defaultMap;
    }
}
