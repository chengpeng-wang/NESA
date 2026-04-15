package org.apache.log4j.xml;

import org.apache.log4j.helpers.LogLog;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public class SAXErrorHandler implements ErrorHandler {
    public void error(SAXParseException ex) {
        emitMessage("Continuable parsing error ", ex);
    }

    public void fatalError(SAXParseException ex) {
        emitMessage("Fatal parsing error ", ex);
    }

    public void warning(SAXParseException ex) {
        emitMessage("Parsing warning ", ex);
    }

    private static void emitMessage(String msg, SAXParseException ex) {
        LogLog.warn(new StringBuffer().append(msg).append(ex.getLineNumber()).append(" and column ").append(ex.getColumnNumber()).toString());
        LogLog.warn(ex.getMessage(), ex.getException());
    }
}
