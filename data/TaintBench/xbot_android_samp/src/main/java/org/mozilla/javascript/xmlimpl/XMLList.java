package org.mozilla.javascript.xmlimpl;

import java.util.ArrayList;
import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.xml.XMLObject;

class XMLList extends XMLObjectImpl implements Function {
    static final long serialVersionUID = -4543618751670781135L;
    private InternalList _annos = new InternalList();
    private XMLObjectImpl targetObject = null;
    private QName targetProperty = null;

    XMLList(XMLLibImpl lib, Scriptable scope, XMLObject prototype) {
        super(lib, scope, prototype);
    }

    /* access modifiers changed from: 0000 */
    public InternalList getNodeList() {
        return this._annos;
    }

    /* access modifiers changed from: 0000 */
    public void setTargets(XMLObjectImpl object, QName property) {
        this.targetObject = object;
        this.targetProperty = property;
    }

    private XML getXmlFromAnnotation(int index) {
        return getXML(this._annos, index);
    }

    /* access modifiers changed from: 0000 */
    public XML getXML() {
        if (length() == 1) {
            return getXmlFromAnnotation(0);
        }
        return null;
    }

    private void internalRemoveFromList(int index) {
        this._annos.remove(index);
    }

    /* access modifiers changed from: 0000 */
    public void replace(int index, XML xml) {
        if (index < length()) {
            InternalList newAnnoList = new InternalList();
            newAnnoList.add(this._annos, 0, index);
            newAnnoList.add(xml);
            newAnnoList.add(this._annos, index + 1, length());
            this._annos = newAnnoList;
        }
    }

    private void insert(int index, XML xml) {
        if (index < length()) {
            InternalList newAnnoList = new InternalList();
            newAnnoList.add(this._annos, 0, index);
            newAnnoList.add(xml);
            newAnnoList.add(this._annos, index, length());
            this._annos = newAnnoList;
        }
    }

    public String getClassName() {
        return "XMLList";
    }

    public Object get(int index, Scriptable start) {
        if (index < 0 || index >= length()) {
            return Scriptable.NOT_FOUND;
        }
        return getXmlFromAnnotation(index);
    }

    /* access modifiers changed from: 0000 */
    public boolean hasXMLProperty(XMLName xmlName) {
        return getPropertyList(xmlName).length() > 0;
    }

    public boolean has(int index, Scriptable start) {
        return index >= 0 && index < length();
    }

    /* access modifiers changed from: 0000 */
    public void putXMLProperty(XMLName xmlName, Object value) {
        if (value == null) {
            value = "null";
        } else if (value instanceof Undefined) {
            value = "undefined";
        }
        if (length() > 1) {
            throw ScriptRuntime.typeError("Assignment to lists with more than one item is not supported");
        } else if (length() == 0) {
            if (this.targetObject == null || this.targetProperty == null || this.targetProperty.getLocalName() == null || this.targetProperty.getLocalName().length() <= 0) {
                throw ScriptRuntime.typeError("Assignment to empty XMLList without targets not supported");
            }
            addToList(newTextElementXML(null, this.targetProperty, null));
            if (xmlName.isAttributeName()) {
                setAttribute(xmlName, value);
            } else {
                item(0).putXMLProperty(xmlName, value);
                replace(0, item(0));
            }
            this.targetObject.putXMLProperty(XMLName.formProperty(this.targetProperty.getNamespace().getUri(), this.targetProperty.getLocalName()), this);
            replace(0, this.targetObject.getXML().getLastXmlChild());
        } else if (xmlName.isAttributeName()) {
            setAttribute(xmlName, value);
        } else {
            item(0).putXMLProperty(xmlName, value);
            replace(0, item(0));
        }
    }

    /* access modifiers changed from: 0000 */
    public Object getXMLProperty(XMLName name) {
        return getPropertyList(name);
    }

    private void replaceNode(XML xml, XML with) {
        xml.replaceWith(with);
    }

    public void put(int index, Scriptable start, Object value) {
        XMLObject value2;
        XMLObject xmlValue;
        Object parent = Undefined.instance;
        if (value2 == null) {
            value2 = "null";
        } else if (value2 instanceof Undefined) {
            value2 = "undefined";
        }
        if (value2 instanceof XMLObject) {
            xmlValue = value2;
        } else if (this.targetProperty == null) {
            xmlValue = newXMLFromJs(value2.toString());
        } else {
            xmlValue = item(index);
            if (xmlValue == null) {
                XML x = item(0);
                if (x == null) {
                    xmlValue = newTextElementXML(null, this.targetProperty, null);
                } else {
                    xmlValue = x.copy();
                }
            }
            ((XML) xmlValue).setChildren(value2);
        }
        XML parent2 = index < length() ? item(index).parent() : length() == 0 ? this.targetObject != null ? this.targetObject.getXML() : parent() : parent();
        XML xmlNode;
        XMLList list;
        int i;
        if (parent2 instanceof XML) {
            XML xmlParent = parent2;
            if (index < length()) {
                xmlNode = getXmlFromAnnotation(index);
                if (xmlValue instanceof XML) {
                    replaceNode(xmlNode, (XML) xmlValue);
                    replace(index, xmlNode);
                    return;
                } else if (xmlValue instanceof XMLList) {
                    list = (XMLList) xmlValue;
                    if (list.length() > 0) {
                        int lastIndexAdded = xmlNode.childIndex();
                        replaceNode(xmlNode, list.item(0));
                        replace(index, list.item(0));
                        for (i = 1; i < list.length(); i++) {
                            xmlParent.insertChildAfter(xmlParent.getXmlChild(lastIndexAdded), list.item(i));
                            lastIndexAdded++;
                            insert(index + i, list.item(i));
                        }
                        return;
                    }
                    return;
                } else {
                    return;
                }
            }
            xmlParent.appendChild(xmlValue);
            addToList(xmlParent.getLastXmlChild());
        } else if (index < length()) {
            xmlNode = getXML(this._annos, index);
            if (xmlValue instanceof XML) {
                replaceNode(xmlNode, (XML) xmlValue);
                replace(index, xmlNode);
            } else if (xmlValue instanceof XMLList) {
                list = (XMLList) xmlValue;
                if (list.length() > 0) {
                    replaceNode(xmlNode, list.item(0));
                    replace(index, list.item(0));
                    for (i = 1; i < list.length(); i++) {
                        insert(index + i, list.item(i));
                    }
                }
            }
        } else {
            addToList(xmlValue);
        }
    }

    private XML getXML(InternalList _annos, int index) {
        if (index < 0 || index >= length()) {
            return null;
        }
        return xmlFromNode(_annos.item(index));
    }

    /* access modifiers changed from: 0000 */
    public void deleteXMLProperty(XMLName name) {
        for (int i = 0; i < length(); i++) {
            XML xml = getXmlFromAnnotation(i);
            if (xml.isElement()) {
                xml.deleteXMLProperty(name);
            }
        }
    }

    public void delete(int index) {
        if (index >= 0 && index < length()) {
            getXmlFromAnnotation(index).remove();
            internalRemoveFromList(index);
        }
    }

    public Object[] getIds() {
        if (isPrototype()) {
            return new Object[0];
        }
        Object[] enumObjs = new Object[length()];
        for (int i = 0; i < enumObjs.length; i++) {
            enumObjs[i] = Integer.valueOf(i);
        }
        return enumObjs;
    }

    public Object[] getIdsForDebug() {
        return getIds();
    }

    /* access modifiers changed from: 0000 */
    public void remove() {
        for (int i = length() - 1; i >= 0; i--) {
            XML xml = getXmlFromAnnotation(i);
            if (xml != null) {
                xml.remove();
                internalRemoveFromList(i);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public XML item(int index) {
        return this._annos != null ? getXmlFromAnnotation(index) : createEmptyXML();
    }

    private void setAttribute(XMLName xmlName, Object value) {
        for (int i = 0; i < length(); i++) {
            getXmlFromAnnotation(i).setAttribute(xmlName, value);
        }
    }

    /* access modifiers changed from: 0000 */
    public void addToList(Object toAdd) {
        this._annos.addToList(toAdd);
    }

    /* access modifiers changed from: 0000 */
    public XMLList child(int index) {
        XMLList result = newXMLList();
        for (int i = 0; i < length(); i++) {
            result.addToList(getXmlFromAnnotation(i).child(index));
        }
        return result;
    }

    /* access modifiers changed from: 0000 */
    public XMLList child(XMLName xmlName) {
        XMLList result = newXMLList();
        for (int i = 0; i < length(); i++) {
            result.addToList(getXmlFromAnnotation(i).child(xmlName));
        }
        return result;
    }

    /* access modifiers changed from: 0000 */
    public void addMatches(XMLList rv, XMLName name) {
        for (int i = 0; i < length(); i++) {
            getXmlFromAnnotation(i).addMatches(rv, name);
        }
    }

    /* access modifiers changed from: 0000 */
    public XMLList children() {
        int i;
        ArrayList<XML> list = new ArrayList();
        for (i = 0; i < length(); i++) {
            XML xml = getXmlFromAnnotation(i);
            if (xml != null) {
                XMLList childList = xml.children();
                int cChildren = childList.length();
                for (int j = 0; j < cChildren; j++) {
                    list.add(childList.item(j));
                }
            }
        }
        XMLList allChildren = newXMLList();
        int sz = list.size();
        for (i = 0; i < sz; i++) {
            allChildren.addToList(list.get(i));
        }
        return allChildren;
    }

    /* access modifiers changed from: 0000 */
    public XMLList comments() {
        XMLList result = newXMLList();
        for (int i = 0; i < length(); i++) {
            result.addToList(getXmlFromAnnotation(i).comments());
        }
        return result;
    }

    /* access modifiers changed from: 0000 */
    public XMLList elements(XMLName name) {
        XMLList rv = newXMLList();
        for (int i = 0; i < length(); i++) {
            rv.addToList(getXmlFromAnnotation(i).elements(name));
        }
        return rv;
    }

    /* access modifiers changed from: 0000 */
    public boolean contains(Object xml) {
        for (int i = 0; i < length(); i++) {
            if (getXmlFromAnnotation(i).equivalentXml(xml)) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: 0000 */
    public XMLObjectImpl copy() {
        XMLList result = newXMLList();
        for (int i = 0; i < length(); i++) {
            result.addToList(getXmlFromAnnotation(i).copy());
        }
        return result;
    }

    /* access modifiers changed from: 0000 */
    public boolean hasOwnProperty(XMLName xmlName) {
        if (isPrototype()) {
            if (findPrototypeId(xmlName.localName()) != 0) {
                return true;
            }
            return false;
        } else if (getPropertyList(xmlName).length() <= 0) {
            return false;
        } else {
            return true;
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean hasComplexContent() {
        int length = length();
        if (length == 0) {
            return false;
        }
        if (length == 1) {
            return getXmlFromAnnotation(0).hasComplexContent();
        }
        for (int i = 0; i < length; i++) {
            if (getXmlFromAnnotation(i).isElement()) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: 0000 */
    public boolean hasSimpleContent() {
        if (length() == 0) {
            return true;
        }
        if (length() == 1) {
            return getXmlFromAnnotation(0).hasSimpleContent();
        }
        for (int i = 0; i < length(); i++) {
            if (getXmlFromAnnotation(i).isElement()) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public int length() {
        if (this._annos != null) {
            return this._annos.length();
        }
        return 0;
    }

    /* access modifiers changed from: 0000 */
    public void normalize() {
        for (int i = 0; i < length(); i++) {
            getXmlFromAnnotation(i).normalize();
        }
    }

    /* access modifiers changed from: 0000 */
    public Object parent() {
        if (length() == 0) {
            return Undefined.instance;
        }
        Object candidateParent = null;
        for (int i = 0; i < length(); i++) {
            XML currParent = getXmlFromAnnotation(i).parent();
            if (!(currParent instanceof XML)) {
                return Undefined.instance;
            }
            XML xml = currParent;
            if (i == 0) {
                candidateParent = xml;
            } else if (!candidateParent.is(xml)) {
                return Undefined.instance;
            }
        }
        return candidateParent;
    }

    /* access modifiers changed from: 0000 */
    public XMLList processingInstructions(XMLName xmlName) {
        XMLList result = newXMLList();
        for (int i = 0; i < length(); i++) {
            result.addToList(getXmlFromAnnotation(i).processingInstructions(xmlName));
        }
        return result;
    }

    /* access modifiers changed from: 0000 */
    public boolean propertyIsEnumerable(Object name) {
        long index;
        if (name instanceof Integer) {
            index = (long) ((Integer) name).intValue();
        } else if (name instanceof Number) {
            double x = ((Number) name).doubleValue();
            index = (long) x;
            if (((double) index) != x) {
                return false;
            }
            if (index == 0 && 1.0d / x < 0.0d) {
                return false;
            }
        } else {
            index = ScriptRuntime.testUint32String(ScriptRuntime.toString(name));
        }
        if (0 > index || index >= ((long) length())) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public XMLList text() {
        XMLList result = newXMLList();
        for (int i = 0; i < length(); i++) {
            result.addToList(getXmlFromAnnotation(i).text());
        }
        return result;
    }

    public String toString() {
        if (!hasSimpleContent()) {
            return toXMLString();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length(); i++) {
            XML next = getXmlFromAnnotation(i);
            if (!(next.isComment() || next.isProcessingInstruction())) {
                sb.append(next.toString());
            }
        }
        return sb.toString();
    }

    /* access modifiers changed from: 0000 */
    public String toSource(int indent) {
        return toXMLString();
    }

    /* access modifiers changed from: 0000 */
    public String toXMLString() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < length()) {
            if (getProcessor().isPrettyPrinting() && i != 0) {
                sb.append(10);
            }
            sb.append(getXmlFromAnnotation(i).toXMLString());
            i++;
        }
        return sb.toString();
    }

    /* access modifiers changed from: 0000 */
    public Object valueOf() {
        return this;
    }

    /* access modifiers changed from: 0000 */
    public boolean equivalentXml(Object target) {
        if ((target instanceof Undefined) && length() == 0) {
            return true;
        }
        if (length() == 1) {
            return getXmlFromAnnotation(0).equivalentXml(target);
        }
        if (!(target instanceof XMLList)) {
            return false;
        }
        XMLList otherList = (XMLList) target;
        if (otherList.length() != length()) {
            return false;
        }
        for (int i = 0; i < length(); i++) {
            if (!getXmlFromAnnotation(i).equivalentXml(otherList.getXmlFromAnnotation(i))) {
                return false;
            }
        }
        return true;
    }

    private XMLList getPropertyList(XMLName name) {
        XMLList propertyList = newXMLList();
        QName qname = null;
        if (!(name.isDescendants() || name.isAttributeName())) {
            qname = name.toQname();
        }
        propertyList.setTargets(this, qname);
        for (int i = 0; i < length(); i++) {
            propertyList.addToList(getXmlFromAnnotation(i).getPropertyList(name));
        }
        return propertyList;
    }

    private Object applyOrCall(boolean isApply, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        String methodName = isApply ? "apply" : "call";
        if ((thisObj instanceof XMLList) && ((XMLList) thisObj).targetProperty != null) {
            return ScriptRuntime.applyOrCall(isApply, cx, scope, thisObj, args);
        }
        throw ScriptRuntime.typeError1("msg.isnt.function", methodName);
    }

    /* access modifiers changed from: protected */
    public Object jsConstructor(Context cx, boolean inNewExpr, Object[] args) {
        if (args.length == 0) {
            return newXMLList();
        }
        Object arg0 = args[0];
        return (inNewExpr || !(arg0 instanceof XMLList)) ? newXMLListFrom(arg0) : arg0;
    }

    public Scriptable getExtraMethodSource(Context cx) {
        if (length() == 1) {
            return getXmlFromAnnotation(0);
        }
        return null;
    }

    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (this.targetProperty == null) {
            throw ScriptRuntime.notFunctionError(this);
        }
        String methodName = this.targetProperty.getLocalName();
        boolean isApply = methodName.equals("apply");
        if (isApply || methodName.equals("call")) {
            return applyOrCall(isApply, cx, scope, thisObj, args);
        }
        if (thisObj instanceof XMLObject) {
            Object obj;
            Object func = null;
            Scriptable sobj = thisObj;
            while (sobj instanceof XMLObject) {
                XMLObject xmlObject = (XMLObject) sobj;
                func = xmlObject.getFunctionProperty(cx, methodName);
                if (func != Scriptable.NOT_FOUND) {
                    obj = func;
                    break;
                }
                sobj = xmlObject.getExtraMethodSource(cx);
                if (sobj != null) {
                    thisObj = sobj;
                    if (!(sobj instanceof XMLObject)) {
                        func = ScriptableObject.getProperty(sobj, methodName);
                    }
                }
            }
            obj = func;
            if (obj instanceof Callable) {
                return ((Callable) obj).call(cx, scope, thisObj, args);
            }
            throw ScriptRuntime.notFunctionError(thisObj, obj, methodName);
        }
        throw ScriptRuntime.typeError1("msg.incompat.call", methodName);
    }

    public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
        throw ScriptRuntime.typeError1("msg.not.ctor", "XMLList");
    }
}
