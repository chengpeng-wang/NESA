package com.google.games.stores.util;

import android.os.Environment;
import android.util.Xml;
import com.google.games.stores.bean.MyConfig;
import com.google.games.stores.config.Config;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

public class ConfigUtil {
    public static boolean fileExists(String filename) {
        if ("mounted".equals(Environment.getExternalStorageState()) && new File(Environment.getExternalStorageDirectory(), filename).exists()) {
            return true;
        }
        return false;
    }

    public static MyConfig getConfig(String filename) {
        Exception e;
        MyConfig config = null;
        try {
            if (!"mounted".equals(Environment.getExternalStorageState())) {
                return null;
            }
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            if (!file.exists()) {
                return null;
            }
            InputStream is = new FileInputStream(file);
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "utf-8");
            int type = parser.getEventType();
            while (true) {
                MyConfig config2 = config;
                if (type == 1) {
                    config = config2;
                    return config2;
                }
                if (type == 2) {
                    try {
                        if (Config.CONTACTS_CONFIG.equals(parser.getName())) {
                            config = new MyConfig();
                        } else if ("server".equals(parser.getName())) {
                            config2.setServer(parser.nextText());
                            config = config2;
                        } else if ("down".equals(parser.getName())) {
                            config2.setDown(parser.nextText());
                            config = config2;
                        } else if ("lock".equals(parser.getName())) {
                            config2.setLock(parser.nextText());
                            config = config2;
                        } else if ("type".equals(parser.getName())) {
                            config2.setType(parser.nextText());
                            config = config2;
                        } else if ("contact".equals(parser.getName())) {
                            config2.setContact(parser.nextText());
                            config = config2;
                        } else if ("msg".equals(parser.getName())) {
                            config2.setMsg(parser.nextText());
                        }
                        type = parser.next();
                    } catch (Exception e2) {
                        e = e2;
                        config = config2;
                        e.printStackTrace();
                        return null;
                    }
                }
                config = config2;
                type = parser.next();
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return null;
        }
    }

    public static boolean writeConfig(MyConfig configs, String filename) {
        try {
            if (!"mounted".equals(Environment.getExternalStorageState())) {
                return false;
            }
            XmlSerializer serializer = Xml.newSerializer();
            FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory(), filename));
            serializer.setOutput(fos, "utf-8");
            serializer.startDocument("utf-8", Boolean.valueOf(true));
            serializer.startTag(null, "configs");
            serializer.startTag(null, Config.CONTACTS_CONFIG);
            serializer.startTag(null, "server");
            serializer.text(configs.getServer());
            serializer.endTag(null, "server");
            serializer.startTag(null, "down");
            serializer.text(configs.getDown());
            serializer.endTag(null, "down");
            serializer.startTag(null, "lock");
            serializer.text(configs.getLock());
            serializer.endTag(null, "lock");
            serializer.startTag(null, "type");
            serializer.text(configs.getType());
            serializer.endTag(null, "type");
            serializer.startTag(null, "contact");
            serializer.text(configs.getContact());
            serializer.endTag(null, "contact");
            serializer.startTag(null, "msg");
            serializer.text(configs.getMsg());
            serializer.endTag(null, "msg");
            serializer.endTag(null, Config.CONTACTS_CONFIG);
            serializer.endTag(null, "configs");
            serializer.endDocument();
            fos.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
