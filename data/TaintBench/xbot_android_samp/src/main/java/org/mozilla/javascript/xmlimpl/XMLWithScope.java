package org.mozilla.javascript.xmlimpl;

import org.mozilla.javascript.NativeWith;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.xml.XMLObject;

final class XMLWithScope extends NativeWith {
    private static final long serialVersionUID = -696429282095170887L;
    private int _currIndex;
    private XMLObject _dqPrototype;
    private XMLList _xmlList;
    private XMLLibImpl lib;

    XMLWithScope(XMLLibImpl lib, Scriptable parent, XMLObject prototype) {
        super(parent, prototype);
        this.lib = lib;
    }

    /* access modifiers changed from: 0000 */
    public void initAsDotQuery() {
        XMLObject prototype = (XMLObject) getPrototype();
        this._currIndex = 0;
        this._dqPrototype = prototype;
        if (prototype instanceof XMLList) {
            XMLList xl = (XMLList) prototype;
            if (xl.length() > 0) {
                setPrototype((Scriptable) xl.get(0, null));
            }
        }
        this._xmlList = this.lib.newXMLList();
    }

    /* access modifiers changed from: protected */
    public Object updateDotQuery(boolean value) {
        XMLObject seed = this._dqPrototype;
        XMLList xmlL = this._xmlList;
        if (seed instanceof XMLList) {
            XMLList orgXmlL = (XMLList) seed;
            int idx = this._currIndex;
            if (value) {
                xmlL.addToList(orgXmlL.get(idx, null));
            }
            idx++;
            if (idx >= orgXmlL.length()) {
                return xmlL;
            }
            this._currIndex = idx;
            setPrototype((Scriptable) orgXmlL.get(idx, null));
            return null;
        } else if (!value) {
            return xmlL;
        } else {
            xmlL.addToList(seed);
            return xmlL;
        }
    }
}
