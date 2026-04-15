package org.apache.log4j;

/* compiled from: PropertyConfigurator */
class NameValue {
    String key;
    String value;

    public NameValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String toString() {
        return new StringBuffer().append(this.key).append("=").append(this.value).toString();
    }
}
