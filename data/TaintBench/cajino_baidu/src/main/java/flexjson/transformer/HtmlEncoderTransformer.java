package flexjson.transformer;

import android.support.v4.view.MotionEventCompat;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.apache.log4j.net.SyslogAppender;

public class HtmlEncoderTransformer extends AbstractTransformer {
    private static final Map<Integer, String> htmlEntities = new HashMap();

    public HtmlEncoderTransformer() {
        if (htmlEntities.isEmpty()) {
            htmlEntities.put(Integer.valueOf(34), "&quot;");
            htmlEntities.put(Integer.valueOf(38), "&amp;");
            htmlEntities.put(Integer.valueOf(60), "&lt;");
            htmlEntities.put(Integer.valueOf(62), "&gt;");
            htmlEntities.put(Integer.valueOf(SyslogAppender.LOG_LOCAL4), "&nbsp;");
            htmlEntities.put(Integer.valueOf(169), "&copy;");
            htmlEntities.put(Integer.valueOf(174), "&reg;");
            htmlEntities.put(Integer.valueOf(192), "&Agrave;");
            htmlEntities.put(Integer.valueOf(193), "&Aacute;");
            htmlEntities.put(Integer.valueOf(194), "&Acirc;");
            htmlEntities.put(Integer.valueOf(195), "&Atilde;");
            htmlEntities.put(Integer.valueOf(196), "&Auml;");
            htmlEntities.put(Integer.valueOf(197), "&Aring;");
            htmlEntities.put(Integer.valueOf(198), "&AElig;");
            htmlEntities.put(Integer.valueOf(199), "&Ccedil;");
            htmlEntities.put(Integer.valueOf(HttpStatus.SC_OK), "&Egrave;");
            htmlEntities.put(Integer.valueOf(HttpStatus.SC_CREATED), "&Eacute;");
            htmlEntities.put(Integer.valueOf(HttpStatus.SC_ACCEPTED), "&Ecirc;");
            htmlEntities.put(Integer.valueOf(HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION), "&Euml;");
            htmlEntities.put(Integer.valueOf(HttpStatus.SC_NO_CONTENT), "&Igrave;");
            htmlEntities.put(Integer.valueOf(HttpStatus.SC_RESET_CONTENT), "&Iacute;");
            htmlEntities.put(Integer.valueOf(HttpStatus.SC_PARTIAL_CONTENT), "&Icirc;");
            htmlEntities.put(Integer.valueOf(HttpStatus.SC_MULTI_STATUS), "&Iuml;");
            htmlEntities.put(Integer.valueOf(208), "&ETH;");
            htmlEntities.put(Integer.valueOf(209), "&Ntilde;");
            htmlEntities.put(Integer.valueOf(210), "&Ograve;");
            htmlEntities.put(Integer.valueOf(211), "&Oacute;");
            htmlEntities.put(Integer.valueOf(212), "&Ocirc;");
            htmlEntities.put(Integer.valueOf(213), "&Otilde;");
            htmlEntities.put(Integer.valueOf(214), "&Ouml;");
            htmlEntities.put(Integer.valueOf(216), "&Oslash;");
            htmlEntities.put(Integer.valueOf(217), "&Ugrave;");
            htmlEntities.put(Integer.valueOf(218), "&Uacute;");
            htmlEntities.put(Integer.valueOf(219), "&Ucirc;");
            htmlEntities.put(Integer.valueOf(220), "&Uuml;");
            htmlEntities.put(Integer.valueOf(221), "&Yacute;");
            htmlEntities.put(Integer.valueOf(222), "&THORN;");
            htmlEntities.put(Integer.valueOf(223), "&szlig;");
            htmlEntities.put(Integer.valueOf(224), "&agrave;");
            htmlEntities.put(Integer.valueOf(225), "&aacute;");
            htmlEntities.put(Integer.valueOf(226), "&acirc;");
            htmlEntities.put(Integer.valueOf(227), "&atilde;");
            htmlEntities.put(Integer.valueOf(228), "&auml;");
            htmlEntities.put(Integer.valueOf(229), "&aring;");
            htmlEntities.put(Integer.valueOf(230), "&aelig;");
            htmlEntities.put(Integer.valueOf(231), "&ccedil;");
            htmlEntities.put(Integer.valueOf(232), "&egrave;");
            htmlEntities.put(Integer.valueOf(233), "&eacute;");
            htmlEntities.put(Integer.valueOf(234), "&ecirc;");
            htmlEntities.put(Integer.valueOf(235), "&euml;");
            htmlEntities.put(Integer.valueOf(236), "&igrave;");
            htmlEntities.put(Integer.valueOf(237), "&iacute;");
            htmlEntities.put(Integer.valueOf(238), "&icirc;");
            htmlEntities.put(Integer.valueOf(239), "&iuml;");
            htmlEntities.put(Integer.valueOf(240), "&eth;");
            htmlEntities.put(Integer.valueOf(241), "&ntilde;");
            htmlEntities.put(Integer.valueOf(242), "&ograve;");
            htmlEntities.put(Integer.valueOf(243), "&oacute;");
            htmlEntities.put(Integer.valueOf(244), "&ocirc;");
            htmlEntities.put(Integer.valueOf(245), "&otilde;");
            htmlEntities.put(Integer.valueOf(246), "&ouml;");
            htmlEntities.put(Integer.valueOf(248), "&oslash;");
            htmlEntities.put(Integer.valueOf(249), "&ugrave;");
            htmlEntities.put(Integer.valueOf(250), "&uacute;");
            htmlEntities.put(Integer.valueOf(251), "&ucirc;");
            htmlEntities.put(Integer.valueOf(252), "&uuml;");
            htmlEntities.put(Integer.valueOf(253), "&yacute;");
            htmlEntities.put(Integer.valueOf(254), "&thorn;");
            htmlEntities.put(Integer.valueOf(MotionEventCompat.ACTION_MASK), "&yuml;");
            htmlEntities.put(Integer.valueOf(8364), "&euro;");
        }
    }

    public void transform(Object value) {
        String val = value.toString();
        getContext().write("\"");
        for (int i = 0; i < val.length(); i++) {
            int intVal = val.charAt(i);
            if (htmlEntities.containsKey(Integer.valueOf(intVal))) {
                getContext().write((String) htmlEntities.get(Integer.valueOf(intVal)));
            } else if (intVal > 128) {
                getContext().write("&#");
                getContext().write(String.valueOf(intVal));
                getContext().write(";");
            } else {
                getContext().write(String.valueOf(val.charAt(i)));
            }
        }
        getContext().write("\"");
    }
}
