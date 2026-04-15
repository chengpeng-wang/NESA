package org.jivesoftware.smack.packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMPPError {
    private List<PacketExtension> applicationExtensions = null;
    private int code;
    private String condition;
    private String message;
    private Type type;

    public static class Condition {
        public static final Condition bad_request = new Condition("bad-request");
        public static final Condition conflict = new Condition("conflict");
        public static final Condition feature_not_implemented = new Condition("feature-not-implemented");
        public static final Condition forbidden = new Condition("forbidden");
        public static final Condition gone = new Condition("gone");
        public static final Condition interna_server_error = new Condition("internal-server-error");
        public static final Condition item_not_found = new Condition("item-not-found");
        public static final Condition jid_malformed = new Condition("jid-malformed");
        public static final Condition no_acceptable = new Condition("not-acceptable");
        public static final Condition not_allowed = new Condition("not-allowed");
        public static final Condition not_authorized = new Condition("not-authorized");
        public static final Condition payment_required = new Condition("payment-required");
        public static final Condition recipient_unavailable = new Condition("recipient-unavailable");
        public static final Condition redirect = new Condition("redirect");
        public static final Condition registration_required = new Condition("registration-required");
        public static final Condition remote_server_error = new Condition("remote-server-error");
        public static final Condition remote_server_not_found = new Condition("remote-server-not-found");
        public static final Condition remote_server_timeout = new Condition("remote-server-timeout");
        public static final Condition request_timeout = new Condition("request-timeout");
        public static final Condition resource_constraint = new Condition("resource-constraint");
        public static final Condition service_unavailable = new Condition("service-unavailable");
        public static final Condition subscription_required = new Condition("subscription-required");
        public static final Condition undefined_condition = new Condition("undefined-condition");
        public static final Condition unexpected_condition = new Condition("unexpected-condition");
        /* access modifiers changed from: private */
        public String value;

        public Condition(String value) {
            this.value = value;
        }

        public String toString() {
            return this.value;
        }
    }

    private static class ErrorSpecification {
        private static Map<Condition, ErrorSpecification> instances = errorSpecifications();
        private int code;
        private Condition condition;
        private Type type;

        private ErrorSpecification(Condition condition, Type type, int code) {
            this.code = code;
            this.type = type;
            this.condition = condition;
        }

        private static Map<Condition, ErrorSpecification> errorSpecifications() {
            Map<Condition, ErrorSpecification> instances = new HashMap(22);
            instances.put(Condition.interna_server_error, new ErrorSpecification(Condition.interna_server_error, Type.WAIT, 500));
            instances.put(Condition.forbidden, new ErrorSpecification(Condition.forbidden, Type.AUTH, 403));
            instances.put(Condition.bad_request, new ErrorSpecification(Condition.bad_request, Type.MODIFY, 400));
            instances.put(Condition.item_not_found, new ErrorSpecification(Condition.item_not_found, Type.CANCEL, 404));
            instances.put(Condition.conflict, new ErrorSpecification(Condition.conflict, Type.CANCEL, 409));
            instances.put(Condition.feature_not_implemented, new ErrorSpecification(Condition.feature_not_implemented, Type.CANCEL, 501));
            instances.put(Condition.gone, new ErrorSpecification(Condition.gone, Type.MODIFY, 302));
            instances.put(Condition.jid_malformed, new ErrorSpecification(Condition.jid_malformed, Type.MODIFY, 400));
            instances.put(Condition.no_acceptable, new ErrorSpecification(Condition.no_acceptable, Type.MODIFY, 406));
            instances.put(Condition.not_allowed, new ErrorSpecification(Condition.not_allowed, Type.CANCEL, 405));
            instances.put(Condition.not_authorized, new ErrorSpecification(Condition.not_authorized, Type.AUTH, 401));
            instances.put(Condition.payment_required, new ErrorSpecification(Condition.payment_required, Type.AUTH, 402));
            instances.put(Condition.recipient_unavailable, new ErrorSpecification(Condition.recipient_unavailable, Type.WAIT, 404));
            instances.put(Condition.redirect, new ErrorSpecification(Condition.redirect, Type.MODIFY, 302));
            instances.put(Condition.registration_required, new ErrorSpecification(Condition.registration_required, Type.AUTH, 407));
            instances.put(Condition.remote_server_not_found, new ErrorSpecification(Condition.remote_server_not_found, Type.CANCEL, 404));
            instances.put(Condition.remote_server_timeout, new ErrorSpecification(Condition.remote_server_timeout, Type.WAIT, 504));
            instances.put(Condition.remote_server_error, new ErrorSpecification(Condition.remote_server_error, Type.CANCEL, 502));
            instances.put(Condition.resource_constraint, new ErrorSpecification(Condition.resource_constraint, Type.WAIT, 500));
            instances.put(Condition.service_unavailable, new ErrorSpecification(Condition.service_unavailable, Type.CANCEL, 503));
            instances.put(Condition.subscription_required, new ErrorSpecification(Condition.subscription_required, Type.AUTH, 407));
            instances.put(Condition.undefined_condition, new ErrorSpecification(Condition.undefined_condition, Type.WAIT, 500));
            instances.put(Condition.unexpected_condition, new ErrorSpecification(Condition.unexpected_condition, Type.WAIT, 400));
            instances.put(Condition.request_timeout, new ErrorSpecification(Condition.request_timeout, Type.CANCEL, 408));
            return instances;
        }

        protected static ErrorSpecification specFor(Condition condition) {
            return (ErrorSpecification) instances.get(condition);
        }

        /* access modifiers changed from: protected */
        public Condition getCondition() {
            return this.condition;
        }

        /* access modifiers changed from: protected */
        public Type getType() {
            return this.type;
        }

        /* access modifiers changed from: protected */
        public int getCode() {
            return this.code;
        }
    }

    public enum Type {
        WAIT,
        CANCEL,
        MODIFY,
        AUTH,
        CONTINUE
    }

    public XMPPError(Condition condition) {
        init(condition);
        this.message = null;
    }

    public XMPPError(Condition condition, String messageText) {
        init(condition);
        this.message = messageText;
    }

    public XMPPError(int code) {
        this.code = code;
        this.message = null;
    }

    public XMPPError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public XMPPError(int code, Type type, String condition, String message, List<PacketExtension> extension) {
        this.code = code;
        this.type = type;
        this.condition = condition;
        this.message = message;
        this.applicationExtensions = extension;
    }

    private void init(Condition condition) {
        ErrorSpecification defaultErrorSpecification = ErrorSpecification.specFor(condition);
        this.condition = condition.value;
        if (defaultErrorSpecification != null) {
            this.type = defaultErrorSpecification.getType();
            this.code = defaultErrorSpecification.getCode();
        }
    }

    public String getCondition() {
        return this.condition;
    }

    public Type getType() {
        return this.type;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String toXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<error code=\"").append(this.code).append("\"");
        if (this.type != null) {
            buf.append(" type=\"");
            buf.append(this.type.name());
            buf.append("\"");
        }
        buf.append(">");
        if (this.condition != null) {
            buf.append("<").append(this.condition);
            buf.append(" xmlns=\"urn:ietf:params:xml:ns:xmpp-stanzas\"/>");
        }
        if (this.message != null) {
            buf.append("<text xml:lang=\"en\" xmlns=\"urn:ietf:params:xml:ns:xmpp-stanzas\">");
            buf.append(this.message);
            buf.append("</text>");
        }
        for (PacketExtension element : getExtensions()) {
            buf.append(element.toXML());
        }
        buf.append("</error>");
        return buf.toString();
    }

    public String toString() {
        StringBuffer txt = new StringBuffer();
        if (this.condition != null) {
            txt.append(this.condition);
        }
        txt.append("(").append(this.code).append(")");
        if (this.message != null) {
            txt.append(" ").append(this.message);
        }
        return txt.toString();
    }

    public synchronized List<PacketExtension> getExtensions() {
        List<PacketExtension> emptyList;
        if (this.applicationExtensions == null) {
            emptyList = Collections.emptyList();
        } else {
            emptyList = Collections.unmodifiableList(this.applicationExtensions);
        }
        return emptyList;
    }

    public synchronized PacketExtension getExtension(String elementName, String namespace) {
        PacketExtension ext;
        if (this.applicationExtensions == null || elementName == null || namespace == null) {
            ext = null;
        } else {
            for (PacketExtension ext2 : this.applicationExtensions) {
                if (elementName.equals(ext2.getElementName()) && namespace.equals(ext2.getNamespace())) {
                    break;
                }
            }
            ext2 = null;
        }
        return ext2;
    }

    public synchronized void addExtension(PacketExtension extension) {
        if (this.applicationExtensions == null) {
            this.applicationExtensions = new ArrayList();
        }
        this.applicationExtensions.add(extension);
    }

    public synchronized void setExtension(List<PacketExtension> extension) {
        this.applicationExtensions = extension;
    }
}
