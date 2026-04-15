package org.jivesoftware.smackx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.util.StringUtils;

public class FormField {
    public static final String TYPE_BOOLEAN = "boolean";
    public static final String TYPE_FIXED = "fixed";
    public static final String TYPE_HIDDEN = "hidden";
    public static final String TYPE_JID_MULTI = "jid-multi";
    public static final String TYPE_JID_SINGLE = "jid-single";
    public static final String TYPE_LIST_MULTI = "list-multi";
    public static final String TYPE_LIST_SINGLE = "list-single";
    public static final String TYPE_TEXT_MULTI = "text-multi";
    public static final String TYPE_TEXT_PRIVATE = "text-private";
    public static final String TYPE_TEXT_SINGLE = "text-single";
    private String description;
    private String label;
    private final List<Option> options;
    private boolean required;
    private String type;
    private final List<String> values;
    private String variable;

    public static class Option {
        private String label;
        private String value;

        public Option(String value) {
            this.value = value;
        }

        public Option(String label, String value) {
            this.label = label;
            this.value = value;
        }

        public String getLabel() {
            return this.label;
        }

        public String getValue() {
            return this.value;
        }

        public String toString() {
            return getLabel();
        }

        public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<option");
            if (getLabel() != null) {
                buf.append(" label=\"").append(getLabel()).append("\"");
            }
            buf.append(">");
            buf.append("<value>").append(StringUtils.escapeForXML(getValue())).append("</value>");
            buf.append("</option>");
            return buf.toString();
        }
    }

    public FormField(String variable) {
        this.required = false;
        this.options = new ArrayList();
        this.values = new ArrayList();
        this.variable = variable;
    }

    public FormField() {
        this.required = false;
        this.options = new ArrayList();
        this.values = new ArrayList();
        this.type = TYPE_FIXED;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLabel() {
        return this.label;
    }

    public Iterator<Option> getOptions() {
        Iterator it;
        synchronized (this.options) {
            it = Collections.unmodifiableList(new ArrayList(this.options)).iterator();
        }
        return it;
    }

    public boolean isRequired() {
        return this.required;
    }

    public String getType() {
        return this.type;
    }

    public Iterator<String> getValues() {
        Iterator it;
        synchronized (this.values) {
            it = Collections.unmodifiableList(new ArrayList(this.values)).iterator();
        }
        return it;
    }

    public String getVariable() {
        return this.variable;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void addValue(String value) {
        synchronized (this.values) {
            this.values.add(value);
        }
    }

    public void addValues(List<String> newValues) {
        synchronized (this.values) {
            this.values.addAll(newValues);
        }
    }

    /* access modifiers changed from: protected */
    public void resetValues() {
        synchronized (this.values) {
            this.values.removeAll(new ArrayList(this.values));
        }
    }

    public void addOption(Option option) {
        synchronized (this.options) {
            this.options.add(option);
        }
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<field");
        if (getLabel() != null) {
            buf.append(" label=\"").append(getLabel()).append("\"");
        }
        if (getVariable() != null) {
            buf.append(" var=\"").append(getVariable()).append("\"");
        }
        if (getType() != null) {
            buf.append(" type=\"").append(getType()).append("\"");
        }
        buf.append(">");
        if (getDescription() != null) {
            buf.append("<desc>").append(getDescription()).append("</desc>");
        }
        if (isRequired()) {
            buf.append("<required/>");
        }
        Iterator<String> i = getValues();
        while (i.hasNext()) {
            buf.append("<value>").append((String) i.next()).append("</value>");
        }
        Iterator i2 = getOptions();
        while (i2.hasNext()) {
            buf.append(((Option) i2.next()).toXML());
        }
        buf.append("</field>");
        return buf.toString();
    }
}
