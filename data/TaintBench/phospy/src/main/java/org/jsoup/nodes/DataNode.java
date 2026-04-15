package org.jsoup.nodes;

import org.jsoup.nodes.Document.OutputSettings;

public class DataNode extends Node {
    private static final String DATA_KEY = "data";

    public DataNode(String data, String baseUri) {
        super(baseUri);
        this.attributes.put(DATA_KEY, data);
    }

    public String nodeName() {
        return "#data";
    }

    public String getWholeData() {
        return this.attributes.get(DATA_KEY);
    }

    public DataNode setWholeData(String data) {
        this.attributes.put(DATA_KEY, data);
        return this;
    }

    /* access modifiers changed from: 0000 */
    public void outerHtmlHead(StringBuilder accum, int depth, OutputSettings out) {
        accum.append(getWholeData());
    }

    /* access modifiers changed from: 0000 */
    public void outerHtmlTail(StringBuilder accum, int depth, OutputSettings out) {
    }

    public String toString() {
        return outerHtml();
    }

    public static DataNode createFromEncoded(String encodedData, String baseUri) {
        return new DataNode(Entities.unescape(encodedData), baseUri);
    }
}
