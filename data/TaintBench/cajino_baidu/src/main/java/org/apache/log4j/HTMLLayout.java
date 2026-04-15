package org.apache.log4j;

import com.baidu.inf.iis.bcs.utils.Mimetypes;
import java.util.Date;
import org.apache.log4j.helpers.Transform;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

public class HTMLLayout extends Layout {
    public static final String LOCATION_INFO_OPTION = "LocationInfo";
    public static final String TITLE_OPTION = "Title";
    static String TRACE_PREFIX = "<br>&nbsp;&nbsp;&nbsp;&nbsp;";
    protected final int BUF_SIZE = 256;
    protected final int MAX_CAPACITY = 1024;
    boolean locationInfo = false;
    private StringBuffer sbuf = new StringBuffer(256);
    String title = "Log4J Log Messages";

    public void setLocationInfo(boolean flag) {
        this.locationInfo = flag;
    }

    public boolean getLocationInfo() {
        return this.locationInfo;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContentType() {
        return Mimetypes.MIMETYPE_HTML;
    }

    public void activateOptions() {
    }

    public String format(LoggingEvent event) {
        if (this.sbuf.capacity() > 1024) {
            this.sbuf = new StringBuffer(256);
        } else {
            this.sbuf.setLength(0);
        }
        this.sbuf.append(new StringBuffer().append(Layout.LINE_SEP).append("<tr>").append(Layout.LINE_SEP).toString());
        this.sbuf.append("<td>");
        this.sbuf.append(event.timeStamp - LoggingEvent.getStartTime());
        this.sbuf.append(new StringBuffer().append("</td>").append(Layout.LINE_SEP).toString());
        String escapedThread = Transform.escapeTags(event.getThreadName());
        this.sbuf.append(new StringBuffer().append("<td title=\"").append(escapedThread).append(" thread\">").toString());
        this.sbuf.append(escapedThread);
        this.sbuf.append(new StringBuffer().append("</td>").append(Layout.LINE_SEP).toString());
        this.sbuf.append("<td title=\"Level\">");
        if (event.getLevel().equals(Level.DEBUG)) {
            this.sbuf.append("<font color=\"#339933\">");
            this.sbuf.append(Transform.escapeTags(String.valueOf(event.getLevel())));
            this.sbuf.append("</font>");
        } else if (event.getLevel().isGreaterOrEqual(Level.WARN)) {
            this.sbuf.append("<font color=\"#993300\"><strong>");
            this.sbuf.append(Transform.escapeTags(String.valueOf(event.getLevel())));
            this.sbuf.append("</strong></font>");
        } else {
            this.sbuf.append(Transform.escapeTags(String.valueOf(event.getLevel())));
        }
        this.sbuf.append(new StringBuffer().append("</td>").append(Layout.LINE_SEP).toString());
        String escapedLogger = Transform.escapeTags(event.getLoggerName());
        this.sbuf.append(new StringBuffer().append("<td title=\"").append(escapedLogger).append(" category\">").toString());
        this.sbuf.append(escapedLogger);
        this.sbuf.append(new StringBuffer().append("</td>").append(Layout.LINE_SEP).toString());
        if (this.locationInfo) {
            LocationInfo locInfo = event.getLocationInformation();
            this.sbuf.append("<td>");
            this.sbuf.append(Transform.escapeTags(locInfo.getFileName()));
            this.sbuf.append(':');
            this.sbuf.append(locInfo.getLineNumber());
            this.sbuf.append(new StringBuffer().append("</td>").append(Layout.LINE_SEP).toString());
        }
        this.sbuf.append("<td title=\"Message\">");
        this.sbuf.append(Transform.escapeTags(event.getRenderedMessage()));
        this.sbuf.append(new StringBuffer().append("</td>").append(Layout.LINE_SEP).toString());
        this.sbuf.append(new StringBuffer().append("</tr>").append(Layout.LINE_SEP).toString());
        if (event.getNDC() != null) {
            this.sbuf.append("<tr><td bgcolor=\"#EEEEEE\" style=\"font-size : xx-small;\" colspan=\"6\" title=\"Nested Diagnostic Context\">");
            this.sbuf.append(new StringBuffer().append("NDC: ").append(Transform.escapeTags(event.getNDC())).toString());
            this.sbuf.append(new StringBuffer().append("</td></tr>").append(Layout.LINE_SEP).toString());
        }
        String[] s = event.getThrowableStrRep();
        if (s != null) {
            this.sbuf.append("<tr><td bgcolor=\"#993300\" style=\"color:White; font-size : xx-small;\" colspan=\"6\">");
            appendThrowableAsHTML(s, this.sbuf);
            this.sbuf.append(new StringBuffer().append("</td></tr>").append(Layout.LINE_SEP).toString());
        }
        return this.sbuf.toString();
    }

    /* access modifiers changed from: 0000 */
    public void appendThrowableAsHTML(String[] s, StringBuffer sbuf) {
        if (s != null) {
            int len = s.length;
            if (len != 0) {
                sbuf.append(Transform.escapeTags(s[0]));
                sbuf.append(Layout.LINE_SEP);
                for (int i = 1; i < len; i++) {
                    sbuf.append(TRACE_PREFIX);
                    sbuf.append(Transform.escapeTags(s[i]));
                    sbuf.append(Layout.LINE_SEP);
                }
            }
        }
    }

    public String getHeader() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append(new StringBuffer().append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">").append(Layout.LINE_SEP).toString());
        sbuf.append(new StringBuffer().append("<html>").append(Layout.LINE_SEP).toString());
        sbuf.append(new StringBuffer().append("<head>").append(Layout.LINE_SEP).toString());
        sbuf.append(new StringBuffer().append("<title>").append(this.title).append("</title>").append(Layout.LINE_SEP).toString());
        sbuf.append(new StringBuffer().append("<style type=\"text/css\">").append(Layout.LINE_SEP).toString());
        sbuf.append(new StringBuffer().append("<!--").append(Layout.LINE_SEP).toString());
        sbuf.append(new StringBuffer().append("body, table {font-family: arial,sans-serif; font-size: x-small;}").append(Layout.LINE_SEP).toString());
        sbuf.append(new StringBuffer().append("th {background: #336699; color: #FFFFFF; text-align: left;}").append(Layout.LINE_SEP).toString());
        sbuf.append(new StringBuffer().append("-->").append(Layout.LINE_SEP).toString());
        sbuf.append(new StringBuffer().append("</style>").append(Layout.LINE_SEP).toString());
        sbuf.append(new StringBuffer().append("</head>").append(Layout.LINE_SEP).toString());
        sbuf.append(new StringBuffer().append("<body bgcolor=\"#FFFFFF\" topmargin=\"6\" leftmargin=\"6\">").append(Layout.LINE_SEP).toString());
        sbuf.append(new StringBuffer().append("<hr size=\"1\" noshade>").append(Layout.LINE_SEP).toString());
        sbuf.append(new StringBuffer().append("Log session start time ").append(new Date()).append("<br>").append(Layout.LINE_SEP).toString());
        sbuf.append(new StringBuffer().append("<br>").append(Layout.LINE_SEP).toString());
        sbuf.append(new StringBuffer().append("<table cellspacing=\"0\" cellpadding=\"4\" border=\"1\" bordercolor=\"#224466\" width=\"100%\">").append(Layout.LINE_SEP).toString());
        sbuf.append(new StringBuffer().append("<tr>").append(Layout.LINE_SEP).toString());
        sbuf.append(new StringBuffer().append("<th>Time</th>").append(Layout.LINE_SEP).toString());
        sbuf.append(new StringBuffer().append("<th>Thread</th>").append(Layout.LINE_SEP).toString());
        sbuf.append(new StringBuffer().append("<th>Level</th>").append(Layout.LINE_SEP).toString());
        sbuf.append(new StringBuffer().append("<th>Category</th>").append(Layout.LINE_SEP).toString());
        if (this.locationInfo) {
            sbuf.append(new StringBuffer().append("<th>File:Line</th>").append(Layout.LINE_SEP).toString());
        }
        sbuf.append(new StringBuffer().append("<th>Message</th>").append(Layout.LINE_SEP).toString());
        sbuf.append(new StringBuffer().append("</tr>").append(Layout.LINE_SEP).toString());
        return sbuf.toString();
    }

    public String getFooter() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append(new StringBuffer().append("</table>").append(Layout.LINE_SEP).toString());
        sbuf.append(new StringBuffer().append("<br>").append(Layout.LINE_SEP).toString());
        sbuf.append("</body></html>");
        return sbuf.toString();
    }

    public boolean ignoresThrowable() {
        return false;
    }
}
