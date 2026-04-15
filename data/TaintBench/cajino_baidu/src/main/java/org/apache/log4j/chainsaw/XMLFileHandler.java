package org.apache.log4j.chainsaw;

import java.util.StringTokenizer;
import org.apache.log4j.Level;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class XMLFileHandler extends DefaultHandler {
    private static final String TAG_EVENT = "log4j:event";
    private static final String TAG_LOCATION_INFO = "log4j:locationInfo";
    private static final String TAG_MESSAGE = "log4j:message";
    private static final String TAG_NDC = "log4j:NDC";
    private static final String TAG_THROWABLE = "log4j:throwable";
    private final StringBuffer mBuf = new StringBuffer();
    private String mCategoryName;
    private Level mLevel;
    private String mLocationDetails;
    private String mMessage;
    private final MyTableModel mModel;
    private String mNDC;
    private int mNumEvents;
    private String mThreadName;
    private String[] mThrowableStrRep;
    private long mTimeStamp;

    XMLFileHandler(MyTableModel aModel) {
        this.mModel = aModel;
    }

    public void startDocument() throws SAXException {
        this.mNumEvents = 0;
    }

    public void characters(char[] aChars, int aStart, int aLength) {
        this.mBuf.append(String.valueOf(aChars, aStart, aLength));
    }

    public void endElement(String aNamespaceURI, String aLocalName, String aQName) {
        if (TAG_EVENT.equals(aQName)) {
            addEvent();
            resetData();
        } else if (TAG_NDC.equals(aQName)) {
            this.mNDC = this.mBuf.toString();
        } else if (TAG_MESSAGE.equals(aQName)) {
            this.mMessage = this.mBuf.toString();
        } else if (TAG_THROWABLE.equals(aQName)) {
            StringTokenizer st = new StringTokenizer(this.mBuf.toString(), "\n\t");
            this.mThrowableStrRep = new String[st.countTokens()];
            if (this.mThrowableStrRep.length > 0) {
                this.mThrowableStrRep[0] = st.nextToken();
                for (int i = 1; i < this.mThrowableStrRep.length; i++) {
                    this.mThrowableStrRep[i] = new StringBuffer().append("\t").append(st.nextToken()).toString();
                }
            }
        }
    }

    public void startElement(String aNamespaceURI, String aLocalName, String aQName, Attributes aAtts) {
        this.mBuf.setLength(0);
        if (TAG_EVENT.equals(aQName)) {
            this.mThreadName = aAtts.getValue("thread");
            this.mTimeStamp = Long.parseLong(aAtts.getValue("timestamp"));
            this.mCategoryName = aAtts.getValue("logger");
            this.mLevel = Level.toLevel(aAtts.getValue("level"));
        } else if (TAG_LOCATION_INFO.equals(aQName)) {
            this.mLocationDetails = new StringBuffer().append(aAtts.getValue("class")).append(".").append(aAtts.getValue("method")).append("(").append(aAtts.getValue("file")).append(":").append(aAtts.getValue("line")).append(")").toString();
        }
    }

    /* access modifiers changed from: 0000 */
    public int getNumEvents() {
        return this.mNumEvents;
    }

    private void addEvent() {
        this.mModel.addEvent(new EventDetails(this.mTimeStamp, this.mLevel, this.mCategoryName, this.mNDC, this.mThreadName, this.mMessage, this.mThrowableStrRep, this.mLocationDetails));
        this.mNumEvents++;
    }

    private void resetData() {
        this.mTimeStamp = 0;
        this.mLevel = null;
        this.mCategoryName = null;
        this.mNDC = null;
        this.mThreadName = null;
        this.mMessage = null;
        this.mThrowableStrRep = null;
        this.mLocationDetails = null;
    }
}
