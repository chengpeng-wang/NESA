package javax.activation;

import com.sun.activation.registries.LogSupport;
import com.sun.activation.registries.MimeTypeFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Vector;

public class MimetypesFileTypeMap extends FileTypeMap {
    private static final int PROG = 0;
    private static MimeTypeFile defDB = null;
    private static String defaultType = "application/octet-stream";
    private MimeTypeFile[] DB;

    public MimetypesFileTypeMap() {
        MimeTypeFile mf;
        Vector dbv = new Vector(5);
        dbv.addElement(null);
        LogSupport.log("MimetypesFileTypeMap: load HOME");
        try {
            String user_home = System.getProperty("user.home");
            if (user_home != null) {
                mf = loadFile(new StringBuilder(String.valueOf(user_home)).append(File.separator).append(".mime.types").toString());
                if (mf != null) {
                    dbv.addElement(mf);
                }
            }
        } catch (SecurityException e) {
        }
        LogSupport.log("MimetypesFileTypeMap: load SYS");
        try {
            mf = loadFile(new StringBuilder(String.valueOf(System.getProperty("java.home"))).append(File.separator).append("lib").append(File.separator).append("mime.types").toString());
            if (mf != null) {
                dbv.addElement(mf);
            }
        } catch (SecurityException e2) {
        }
        LogSupport.log("MimetypesFileTypeMap: load JAR");
        loadAllResources(dbv, "mime.types");
        LogSupport.log("MimetypesFileTypeMap: load DEF");
        synchronized (MimetypesFileTypeMap.class) {
            if (defDB == null) {
                defDB = loadResource("/mimetypes.default");
            }
        }
        if (defDB != null) {
            dbv.addElement(defDB);
        }
        this.DB = new MimeTypeFile[dbv.size()];
        dbv.copyInto(this.DB);
    }

    private MimeTypeFile loadResource(String name) {
        InputStream clis = null;
        try {
            clis = SecuritySupport.getResourceAsStream(getClass(), name);
            if (clis != null) {
                MimeTypeFile mf = new MimeTypeFile(clis);
                if (LogSupport.isLoggable()) {
                    LogSupport.log("MimetypesFileTypeMap: successfully loaded mime types file: " + name);
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
                LogSupport.log("MimetypesFileTypeMap: not loading mime types file: " + name);
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
                LogSupport.log("MimetypesFileTypeMap: can't load " + name, e3);
            }
            if (clis != null) {
                try {
                    clis.close();
                } catch (IOException e4) {
                }
            }
        } catch (SecurityException sex) {
            if (LogSupport.isLoggable()) {
                LogSupport.log("MimetypesFileTypeMap: can't load " + name, sex);
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

    private void loadAllResources(Vector v, String name) {
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
                    LogSupport.log("MimetypesFileTypeMap: getResources");
                }
                for (URL url : urls) {
                    InputStream clis = null;
                    if (LogSupport.isLoggable()) {
                        LogSupport.log("MimetypesFileTypeMap: URL " + url);
                    }
                    try {
                        clis = SecuritySupport.openStream(url);
                        if (clis != null) {
                            v.addElement(new MimeTypeFile(clis));
                            anyLoaded = true;
                            if (LogSupport.isLoggable()) {
                                LogSupport.log("MimetypesFileTypeMap: successfully loaded mime types from URL: " + url);
                            }
                        } else if (LogSupport.isLoggable()) {
                            LogSupport.log("MimetypesFileTypeMap: not loading mime types from URL: " + url);
                        }
                        if (clis != null) {
                            try {
                                clis.close();
                            } catch (IOException e) {
                            }
                        }
                    } catch (IOException ioex) {
                        if (LogSupport.isLoggable()) {
                            LogSupport.log("MimetypesFileTypeMap: can't load " + url, ioex);
                        }
                        if (clis != null) {
                            try {
                                clis.close();
                            } catch (IOException e2) {
                            }
                        }
                    } catch (SecurityException sex) {
                        if (LogSupport.isLoggable()) {
                            LogSupport.log("MimetypesFileTypeMap: can't load " + url, sex);
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
                LogSupport.log("MimetypesFileTypeMap: can't load " + name, ex);
            }
        }
        if (!anyLoaded) {
            LogSupport.log("MimetypesFileTypeMap: !anyLoaded");
            MimeTypeFile mf = loadResource("/" + name);
            if (mf != null) {
                v.addElement(mf);
            }
        }
    }

    private MimeTypeFile loadFile(String name) {
        try {
            return new MimeTypeFile(name);
        } catch (IOException e) {
            return null;
        }
    }

    public MimetypesFileTypeMap(String mimeTypeFileName) throws IOException {
        this();
        this.DB[0] = new MimeTypeFile(mimeTypeFileName);
    }

    public MimetypesFileTypeMap(InputStream is) {
        this();
        try {
            this.DB[0] = new MimeTypeFile(is);
        } catch (IOException e) {
        }
    }

    public synchronized void addMimeTypes(String mime_types) {
        if (this.DB[0] == null) {
            this.DB[0] = new MimeTypeFile();
        }
        this.DB[0].appendToRegistry(mime_types);
    }

    public String getContentType(File f) {
        return getContentType(f.getName());
    }

    public synchronized String getContentType(String filename) {
        String result;
        int dot_pos = filename.lastIndexOf(".");
        if (dot_pos < 0) {
            result = defaultType;
        } else {
            String file_ext = filename.substring(dot_pos + 1);
            if (file_ext.length() == 0) {
                result = defaultType;
            } else {
                for (int i = 0; i < this.DB.length; i++) {
                    if (this.DB[i] != null) {
                        result = this.DB[i].getMIMETypeString(file_ext);
                        if (result != null) {
                            break;
                        }
                    }
                }
                result = defaultType;
            }
        }
        return result;
    }
}
