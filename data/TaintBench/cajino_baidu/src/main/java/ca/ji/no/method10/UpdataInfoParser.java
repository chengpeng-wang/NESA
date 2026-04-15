package ca.ji.no.method10;

import android.util.Xml;
import java.io.InputStream;
import org.apache.http.cookie.ClientCookie;
import org.xmlpull.v1.XmlPullParser;

public class UpdataInfoParser {
    public static UpdataInfo getUpdataInfo(InputStream is) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        UpdataInfo info = new UpdataInfo();
        parser.setInput(is, "utf-8");
        for (int type = parser.getEventType(); type != 1; type = parser.next()) {
            switch (type) {
                case 2:
                    if (!ClientCookie.VERSION_ATTR.equals(parser.getName())) {
                        if (!"description".equals(parser.getName())) {
                            if (!"apkurl".equals(parser.getName())) {
                                break;
                            }
                            info.setApkurl(parser.nextText());
                            break;
                        }
                        info.setDescription(parser.nextText());
                        break;
                    }
                    info.setVersion(parser.nextText());
                    break;
                default:
                    break;
            }
        }
        return info;
    }
}
