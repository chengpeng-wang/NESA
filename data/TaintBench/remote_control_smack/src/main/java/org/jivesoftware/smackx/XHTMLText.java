package org.jivesoftware.smackx;

import org.jivesoftware.smack.util.StringUtils;

public class XHTMLText {
    private StringBuilder text = new StringBuilder(30);

    public XHTMLText(String style, String lang) {
        appendOpenBodyTag(style, lang);
    }

    public void appendOpenAnchorTag(String href, String style) {
        StringBuilder sb = new StringBuilder("<a");
        if (href != null) {
            sb.append(" href=\"");
            sb.append(href);
            sb.append("\"");
        }
        if (style != null) {
            sb.append(" style=\"");
            sb.append(style);
            sb.append("\"");
        }
        sb.append(">");
        this.text.append(sb.toString());
    }

    public void appendCloseAnchorTag() {
        this.text.append("</a>");
    }

    public void appendOpenBlockQuoteTag(String style) {
        StringBuilder sb = new StringBuilder("<blockquote");
        if (style != null) {
            sb.append(" style=\"");
            sb.append(style);
            sb.append("\"");
        }
        sb.append(">");
        this.text.append(sb.toString());
    }

    public void appendCloseBlockQuoteTag() {
        this.text.append("</blockquote>");
    }

    private void appendOpenBodyTag(String style, String lang) {
        StringBuilder sb = new StringBuilder("<body");
        if (style != null) {
            sb.append(" style=\"");
            sb.append(style);
            sb.append("\"");
        }
        if (lang != null) {
            sb.append(" xml:lang=\"");
            sb.append(lang);
            sb.append("\"");
        }
        sb.append(">");
        this.text.append(sb.toString());
    }

    private String closeBodyTag() {
        return "</body>";
    }

    public void appendBrTag() {
        this.text.append("<br>");
    }

    public void appendOpenCiteTag() {
        this.text.append("<cite>");
    }

    public void appendOpenCodeTag() {
        this.text.append("<code>");
    }

    public void appendCloseCodeTag() {
        this.text.append("</code>");
    }

    public void appendOpenEmTag() {
        this.text.append("<em>");
    }

    public void appendCloseEmTag() {
        this.text.append("</em>");
    }

    public void appendOpenHeaderTag(int level, String style) {
        if (level <= 3 && level >= 1) {
            StringBuilder sb = new StringBuilder("<h");
            sb.append(level);
            if (style != null) {
                sb.append(" style=\"");
                sb.append(style);
                sb.append("\"");
            }
            sb.append(">");
            this.text.append(sb.toString());
        }
    }

    public void appendCloseHeaderTag(int level) {
        if (level <= 3 && level >= 1) {
            StringBuilder sb = new StringBuilder("</h");
            sb.append(level);
            sb.append(">");
            this.text.append(sb.toString());
        }
    }

    public void appendImageTag(String align, String alt, String height, String src, String width) {
        StringBuilder sb = new StringBuilder("<img");
        if (align != null) {
            sb.append(" align=\"");
            sb.append(align);
            sb.append("\"");
        }
        if (alt != null) {
            sb.append(" alt=\"");
            sb.append(alt);
            sb.append("\"");
        }
        if (height != null) {
            sb.append(" height=\"");
            sb.append(height);
            sb.append("\"");
        }
        if (src != null) {
            sb.append(" src=\"");
            sb.append(src);
            sb.append("\"");
        }
        if (width != null) {
            sb.append(" width=\"");
            sb.append(width);
            sb.append("\"");
        }
        sb.append(">");
        this.text.append(sb.toString());
    }

    public void appendLineItemTag(String style) {
        StringBuilder sb = new StringBuilder("<li");
        if (style != null) {
            sb.append(" style=\"");
            sb.append(style);
            sb.append("\"");
        }
        sb.append(">");
        this.text.append(sb.toString());
    }

    public void appendOpenOrderedListTag(String style) {
        StringBuilder sb = new StringBuilder("<ol");
        if (style != null) {
            sb.append(" style=\"");
            sb.append(style);
            sb.append("\"");
        }
        sb.append(">");
        this.text.append(sb.toString());
    }

    public void appendCloseOrderedListTag() {
        this.text.append("</ol>");
    }

    public void appendOpenUnorderedListTag(String style) {
        StringBuilder sb = new StringBuilder("<ul");
        if (style != null) {
            sb.append(" style=\"");
            sb.append(style);
            sb.append("\"");
        }
        sb.append(">");
        this.text.append(sb.toString());
    }

    public void appendCloseUnorderedListTag() {
        this.text.append("</ul>");
    }

    public void appendOpenParagraphTag(String style) {
        StringBuilder sb = new StringBuilder("<p");
        if (style != null) {
            sb.append(" style=\"");
            sb.append(style);
            sb.append("\"");
        }
        sb.append(">");
        this.text.append(sb.toString());
    }

    public void appendCloseParagraphTag() {
        this.text.append("</p>");
    }

    public void appendOpenInlinedQuoteTag(String style) {
        StringBuilder sb = new StringBuilder("<q");
        if (style != null) {
            sb.append(" style=\"");
            sb.append(style);
            sb.append("\"");
        }
        sb.append(">");
        this.text.append(sb.toString());
    }

    public void appendCloseInlinedQuoteTag() {
        this.text.append("</q>");
    }

    public void appendOpenSpanTag(String style) {
        StringBuilder sb = new StringBuilder("<span");
        if (style != null) {
            sb.append(" style=\"");
            sb.append(style);
            sb.append("\"");
        }
        sb.append(">");
        this.text.append(sb.toString());
    }

    public void appendCloseSpanTag() {
        this.text.append("</span>");
    }

    public void appendOpenStrongTag() {
        this.text.append("<strong>");
    }

    public void appendCloseStrongTag() {
        this.text.append("</strong>");
    }

    public void append(String textToAppend) {
        this.text.append(StringUtils.escapeForXML(textToAppend));
    }

    public String toString() {
        return this.text.toString().concat(closeBodyTag());
    }
}
