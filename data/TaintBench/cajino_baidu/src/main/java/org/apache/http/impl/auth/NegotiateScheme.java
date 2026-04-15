package org.apache.http.impl.auth;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.util.CharArrayBuffer;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.Oid;

public class NegotiateScheme extends AuthSchemeBase {
    private static final String KERBEROS_OID = "1.2.840.113554.1.2.2";
    private static final String SPNEGO_OID = "1.3.6.1.5.5.2";
    private GSSContext gssContext;
    private final Log log;
    private Oid negotiationOid;
    private final SpnegoTokenGenerator spengoGenerator;
    private State state;
    private final boolean stripPort;
    private byte[] token;

    enum State {
        UNINITIATED,
        CHALLENGE_RECEIVED,
        TOKEN_GENERATED,
        FAILED
    }

    public NegotiateScheme(SpnegoTokenGenerator spengoGenerator, boolean stripPort) {
        this.log = LogFactory.getLog(getClass());
        this.gssContext = null;
        this.negotiationOid = null;
        this.state = State.UNINITIATED;
        this.spengoGenerator = spengoGenerator;
        this.stripPort = stripPort;
    }

    public NegotiateScheme(SpnegoTokenGenerator spengoGenerator) {
        this(spengoGenerator, false);
    }

    public NegotiateScheme() {
        this(null, false);
    }

    public boolean isComplete() {
        return this.state == State.TOKEN_GENERATED || this.state == State.FAILED;
    }

    public String getSchemeName() {
        return "Negotiate";
    }

    @Deprecated
    public Header authenticate(Credentials credentials, HttpRequest request) throws AuthenticationException {
        return authenticate(credentials, request, null);
    }

    /* access modifiers changed from: protected */
    public GSSManager getManager() {
        return GSSManager.getInstance();
    }

    /* JADX WARNING: Removed duplicated region for block: B:47:0x0136 A:{Splitter:B:8:0x0019, ExcHandler: IOException (r2_0 'ex' java.io.IOException)} */
    /* JADX WARNING: Missing block: B:47:0x0136, code skipped:
            r2 = move-exception;
     */
    /* JADX WARNING: Missing block: B:48:0x0137, code skipped:
            r14.state = org.apache.http.impl.auth.NegotiateScheme.State.FAILED;
     */
    /* JADX WARNING: Missing block: B:49:0x0144, code skipped:
            throw new org.apache.http.auth.AuthenticationException(r2.getMessage());
     */
    public org.apache.http.Header authenticate(org.apache.http.auth.Credentials r15, org.apache.http.HttpRequest r16, org.apache.http.protocol.HttpContext r17) throws org.apache.http.auth.AuthenticationException {
        /*
        r14 = this;
        if (r16 != 0) goto L_0x000a;
    L_0x0002:
        r10 = new java.lang.IllegalArgumentException;
        r11 = "HTTP request may not be null";
        r10.<init>(r11);
        throw r10;
    L_0x000a:
        r10 = r14.state;
        r11 = org.apache.http.impl.auth.NegotiateScheme.State.CHALLENGE_RECEIVED;
        if (r10 == r11) goto L_0x0018;
    L_0x0010:
        r10 = new java.lang.IllegalStateException;
        r11 = "Negotiation authentication process has not been initiated";
        r10.<init>(r11);
        throw r10;
    L_0x0018:
        r5 = 0;
        r10 = r14.isProxy();	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        if (r10 == 0) goto L_0x0052;
    L_0x001f:
        r5 = "http.proxy_host";
    L_0x0021:
        r0 = r17;
        r4 = r0.getAttribute(r5);	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r4 = (org.apache.http.HttpHost) r4;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        if (r4 != 0) goto L_0x0055;
    L_0x002b:
        r10 = new org.apache.http.auth.AuthenticationException;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r11 = "Authentication host is not set in the execution context";
        r10.m1977init(r11);	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        throw r10;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
    L_0x0033:
        r3 = move-exception;
        r10 = org.apache.http.impl.auth.NegotiateScheme.State.FAILED;
        r14.state = r10;
        r10 = r3.getMajor();
        r11 = 9;
        if (r10 == r11) goto L_0x0048;
    L_0x0040:
        r10 = r3.getMajor();
        r11 = 8;
        if (r10 != r11) goto L_0x01cb;
    L_0x0048:
        r10 = new org.apache.http.auth.InvalidCredentialsException;
        r11 = r3.getMessage();
        r10.m2177init(r11, r3);
        throw r10;
    L_0x0052:
        r5 = "http.target_host";
        goto L_0x0021;
    L_0x0055:
        r10 = r14.stripPort;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        if (r10 != 0) goto L_0x0145;
    L_0x0059:
        r10 = r4.getPort();	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        if (r10 <= 0) goto L_0x0145;
    L_0x005f:
        r1 = r4.toHostString();	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
    L_0x0063:
        r10 = r14.log;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r10 = r10.isDebugEnabled();	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        if (r10 == 0) goto L_0x0083;
    L_0x006b:
        r10 = r14.log;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r11 = new java.lang.StringBuilder;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r11.<init>();	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r12 = "init ";
        r11 = r11.append(r12);	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r11 = r11.append(r1);	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r11 = r11.toString();	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r10.debug(r11);	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
    L_0x0083:
        r10 = new org.ietf.jgss.Oid;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r11 = "1.3.6.1.5.5.2";
        r10.<init>(r11);	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r14.negotiationOid = r10;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r9 = 0;
        r6 = r14.getManager();	 Catch:{ GSSException -> 0x014b, IOException -> 0x0136 }
        r10 = new java.lang.StringBuilder;	 Catch:{ GSSException -> 0x014b, IOException -> 0x0136 }
        r10.<init>();	 Catch:{ GSSException -> 0x014b, IOException -> 0x0136 }
        r11 = "HTTP/";
        r10 = r10.append(r11);	 Catch:{ GSSException -> 0x014b, IOException -> 0x0136 }
        r10 = r10.append(r1);	 Catch:{ GSSException -> 0x014b, IOException -> 0x0136 }
        r10 = r10.toString();	 Catch:{ GSSException -> 0x014b, IOException -> 0x0136 }
        r11 = 0;
        r7 = r6.createName(r10, r11);	 Catch:{ GSSException -> 0x014b, IOException -> 0x0136 }
        r10 = r14.negotiationOid;	 Catch:{ GSSException -> 0x014b, IOException -> 0x0136 }
        r10 = r7.canonicalize(r10);	 Catch:{ GSSException -> 0x014b, IOException -> 0x0136 }
        r11 = r14.negotiationOid;	 Catch:{ GSSException -> 0x014b, IOException -> 0x0136 }
        r12 = 0;
        r13 = 0;
        r10 = r6.createContext(r10, r11, r12, r13);	 Catch:{ GSSException -> 0x014b, IOException -> 0x0136 }
        r14.gssContext = r10;	 Catch:{ GSSException -> 0x014b, IOException -> 0x0136 }
        r10 = r14.gssContext;	 Catch:{ GSSException -> 0x014b, IOException -> 0x0136 }
        r11 = 1;
        r10.requestMutualAuth(r11);	 Catch:{ GSSException -> 0x014b, IOException -> 0x0136 }
        r10 = r14.gssContext;	 Catch:{ GSSException -> 0x014b, IOException -> 0x0136 }
        r11 = 1;
        r10.requestCredDeleg(r11);	 Catch:{ GSSException -> 0x014b, IOException -> 0x0136 }
    L_0x00c5:
        if (r9 == 0) goto L_0x010f;
    L_0x00c7:
        r10 = r14.log;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r11 = "Using Kerberos MECH 1.2.840.113554.1.2.2";
        r10.debug(r11);	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r10 = new org.ietf.jgss.Oid;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r11 = "1.2.840.113554.1.2.2";
        r10.<init>(r11);	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r14.negotiationOid = r10;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r6 = r14.getManager();	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r10 = new java.lang.StringBuilder;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r10.<init>();	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r11 = "HTTP/";
        r10 = r10.append(r11);	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r10 = r10.append(r1);	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r10 = r10.toString();	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r11 = 0;
        r7 = r6.createName(r10, r11);	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r10 = r14.negotiationOid;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r10 = r7.canonicalize(r10);	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r11 = r14.negotiationOid;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r12 = 0;
        r13 = 0;
        r10 = r6.createContext(r10, r11, r12, r13);	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r14.gssContext = r10;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r10 = r14.gssContext;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r11 = 1;
        r10.requestMutualAuth(r11);	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r10 = r14.gssContext;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r11 = 1;
        r10.requestCredDeleg(r11);	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
    L_0x010f:
        r10 = r14.token;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        if (r10 != 0) goto L_0x0118;
    L_0x0113:
        r10 = 0;
        r10 = new byte[r10];	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r14.token = r10;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
    L_0x0118:
        r10 = r14.gssContext;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r11 = r14.token;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r12 = 0;
        r13 = r14.token;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r13 = r13.length;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r10 = r10.initSecContext(r11, r12, r13);	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r14.token = r10;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r10 = r14.token;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        if (r10 != 0) goto L_0x015e;
    L_0x012a:
        r10 = org.apache.http.impl.auth.NegotiateScheme.State.FAILED;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r14.state = r10;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r10 = new org.apache.http.auth.AuthenticationException;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r11 = "GSS security context initialization failed";
        r10.m1977init(r11);	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        throw r10;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
    L_0x0136:
        r2 = move-exception;
        r10 = org.apache.http.impl.auth.NegotiateScheme.State.FAILED;
        r14.state = r10;
        r10 = new org.apache.http.auth.AuthenticationException;
        r11 = r2.getMessage();
        r10.m1977init(r11);
        throw r10;
    L_0x0145:
        r1 = r4.getHostName();	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        goto L_0x0063;
    L_0x014b:
        r2 = move-exception;
        r10 = r2.getMajor();	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r11 = 2;
        if (r10 != r11) goto L_0x015d;
    L_0x0153:
        r10 = r14.log;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r11 = "GSSException BAD_MECH, retry with Kerberos MECH";
        r10.debug(r11);	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r9 = 1;
        goto L_0x00c5;
    L_0x015d:
        throw r2;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
    L_0x015e:
        r10 = r14.spengoGenerator;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        if (r10 == 0) goto L_0x017a;
    L_0x0162:
        r10 = r14.negotiationOid;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r10 = r10.toString();	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r11 = "1.2.840.113554.1.2.2";
        r10 = r10.equals(r11);	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        if (r10 == 0) goto L_0x017a;
    L_0x0170:
        r10 = r14.spengoGenerator;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r11 = r14.token;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r10 = r10.generateSpnegoDERObject(r11);	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r14.token = r10;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
    L_0x017a:
        r10 = org.apache.http.impl.auth.NegotiateScheme.State.TOKEN_GENERATED;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r14.state = r10;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r8 = new java.lang.String;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r10 = r14.token;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r11 = 0;
        r10 = org.apache.commons.codec.binary.Base64.encodeBase64(r10, r11);	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r8.<init>(r10);	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r10 = r14.log;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r10 = r10.isDebugEnabled();	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        if (r10 == 0) goto L_0x01b0;
    L_0x0192:
        r10 = r14.log;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r11 = new java.lang.StringBuilder;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r11.<init>();	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r12 = "Sending response '";
        r11 = r11.append(r12);	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r11 = r11.append(r8);	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r12 = "' back to the auth server";
        r11 = r11.append(r12);	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r11 = r11.toString();	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r10.debug(r11);	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
    L_0x01b0:
        r10 = new org.apache.http.message.BasicHeader;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r11 = "Authorization";
        r12 = new java.lang.StringBuilder;	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r12.<init>();	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r13 = "Negotiate ";
        r12 = r12.append(r13);	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r12 = r12.append(r8);	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r12 = r12.toString();	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        r10.m1729init(r11, r12);	 Catch:{ GSSException -> 0x0033, IOException -> 0x0136 }
        return r10;
    L_0x01cb:
        r10 = r3.getMajor();
        r11 = 13;
        if (r10 != r11) goto L_0x01dd;
    L_0x01d3:
        r10 = new org.apache.http.auth.InvalidCredentialsException;
        r11 = r3.getMessage();
        r10.m2177init(r11, r3);
        throw r10;
    L_0x01dd:
        r10 = r3.getMajor();
        r11 = 10;
        if (r10 == r11) goto L_0x01f5;
    L_0x01e5:
        r10 = r3.getMajor();
        r11 = 19;
        if (r10 == r11) goto L_0x01f5;
    L_0x01ed:
        r10 = r3.getMajor();
        r11 = 20;
        if (r10 != r11) goto L_0x01ff;
    L_0x01f5:
        r10 = new org.apache.http.auth.AuthenticationException;
        r11 = r3.getMessage();
        r10.m1978init(r11, r3);
        throw r10;
    L_0x01ff:
        r10 = new org.apache.http.auth.AuthenticationException;
        r11 = r3.getMessage();
        r10.m1977init(r11);
        throw r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.auth.NegotiateScheme.authenticate(org.apache.http.auth.Credentials, org.apache.http.HttpRequest, org.apache.http.protocol.HttpContext):org.apache.http.Header");
    }

    public String getParameter(String name) {
        if (name != null) {
            return null;
        }
        throw new IllegalArgumentException("Parameter name may not be null");
    }

    public String getRealm() {
        return null;
    }

    public boolean isConnectionBased() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void parseChallenge(CharArrayBuffer buffer, int beginIndex, int endIndex) throws MalformedChallengeException {
        String challenge = buffer.substringTrimmed(beginIndex, endIndex);
        if (this.log.isDebugEnabled()) {
            this.log.debug("Received challenge '" + challenge + "' from the auth server");
        }
        if (this.state == State.UNINITIATED) {
            this.token = new Base64().decode(challenge.getBytes());
            this.state = State.CHALLENGE_RECEIVED;
            return;
        }
        this.log.debug("Authentication already attempted");
        this.state = State.FAILED;
    }
}
