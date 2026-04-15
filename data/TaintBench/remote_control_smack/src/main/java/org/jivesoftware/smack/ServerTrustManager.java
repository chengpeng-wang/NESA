package org.jivesoftware.smack;

import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Principal;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.X509TrustManager;

class ServerTrustManager implements X509TrustManager {
    private static Pattern cnPattern = Pattern.compile("(?i)(cn=)([^,]*)");
    private ConnectionConfiguration configuration;
    private String server;
    private KeyStore trustStore;

    /* JADX WARNING: Removed duplicated region for block: B:31:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x003c A:{SYNTHETIC, Splitter:B:15:0x003c} */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0045 A:{SYNTHETIC, Splitter:B:20:0x0045} */
    public ServerTrustManager(java.lang.String r6, org.jivesoftware.smack.ConnectionConfiguration r7) {
        /*
        r5 = this;
        r5.<init>();
        r5.configuration = r7;
        r5.server = r6;
        r1 = 0;
        r3 = r7.getTruststoreType();	 Catch:{ Exception -> 0x0032 }
        r3 = java.security.KeyStore.getInstance(r3);	 Catch:{ Exception -> 0x0032 }
        r5.trustStore = r3;	 Catch:{ Exception -> 0x0032 }
        r2 = new java.io.FileInputStream;	 Catch:{ Exception -> 0x0032 }
        r3 = r7.getTruststorePath();	 Catch:{ Exception -> 0x0032 }
        r2.<init>(r3);	 Catch:{ Exception -> 0x0032 }
        r3 = r5.trustStore;	 Catch:{ Exception -> 0x004e, all -> 0x004b }
        r4 = r7.getTruststorePassword();	 Catch:{ Exception -> 0x004e, all -> 0x004b }
        r4 = r4.toCharArray();	 Catch:{ Exception -> 0x004e, all -> 0x004b }
        r3.load(r2, r4);	 Catch:{ Exception -> 0x004e, all -> 0x004b }
        if (r2 == 0) goto L_0x0051;
    L_0x002a:
        r2.close();	 Catch:{ IOException -> 0x002f }
        r1 = r2;
    L_0x002e:
        return;
    L_0x002f:
        r3 = move-exception;
        r1 = r2;
        goto L_0x002e;
    L_0x0032:
        r0 = move-exception;
    L_0x0033:
        r0.printStackTrace();	 Catch:{ all -> 0x0042 }
        r3 = 0;
        r7.setVerifyRootCAEnabled(r3);	 Catch:{ all -> 0x0042 }
        if (r1 == 0) goto L_0x002e;
    L_0x003c:
        r1.close();	 Catch:{ IOException -> 0x0040 }
        goto L_0x002e;
    L_0x0040:
        r3 = move-exception;
        goto L_0x002e;
    L_0x0042:
        r3 = move-exception;
    L_0x0043:
        if (r1 == 0) goto L_0x0048;
    L_0x0045:
        r1.close();	 Catch:{ IOException -> 0x0049 }
    L_0x0048:
        throw r3;
    L_0x0049:
        r4 = move-exception;
        goto L_0x0048;
    L_0x004b:
        r3 = move-exception;
        r1 = r2;
        goto L_0x0043;
    L_0x004e:
        r0 = move-exception;
        r1 = r2;
        goto L_0x0033;
    L_0x0051:
        r1 = r2;
        goto L_0x002e;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smack.ServerTrustManager.m1409init(java.lang.String, org.jivesoftware.smack.ConnectionConfiguration):void");
    }

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] x509Certificates, String arg1) throws CertificateException {
        int i;
        int nSize = x509Certificates.length;
        List<String> peerIdentities = getPeerIdentity(x509Certificates[0]);
        if (this.configuration.isVerifyChainEnabled()) {
            Principal principalLast = null;
            for (i = nSize - 1; i >= 0; i--) {
                X509Certificate x509certificate = x509Certificates[i];
                Principal principalIssuer = x509certificate.getIssuerDN();
                Principal principalSubject = x509certificate.getSubjectDN();
                if (principalLast != null) {
                    if (principalIssuer.equals(principalLast)) {
                        try {
                            x509Certificates[i].verify(x509Certificates[i + 1].getPublicKey());
                        } catch (GeneralSecurityException e) {
                            throw new CertificateException("signature verification failed of " + peerIdentities);
                        }
                    }
                    throw new CertificateException("subject/issuer verification failed of " + peerIdentities);
                }
                principalLast = principalSubject;
            }
        }
        if (this.configuration.isVerifyRootCAEnabled()) {
            boolean trusted = false;
            try {
                trusted = this.trustStore.getCertificateAlias(x509Certificates[nSize + -1]) != null;
                if (!trusted && nSize == 1 && this.configuration.isSelfSignedCertificateEnabled()) {
                    System.out.println("Accepting self-signed certificate of remote server: " + peerIdentities);
                    trusted = true;
                }
            } catch (KeyStoreException e2) {
                e2.printStackTrace();
            }
            if (!trusted) {
                throw new CertificateException("root certificate not trusted of " + peerIdentities);
            }
        }
        if (this.configuration.isNotMatchingDomainCheckEnabled()) {
            if (peerIdentities.size() == 1 && ((String) peerIdentities.get(0)).startsWith("*.")) {
                if (!this.server.endsWith(((String) peerIdentities.get(0)).replace("*.", ""))) {
                    throw new CertificateException("target verification failed of " + peerIdentities);
                }
            } else if (!peerIdentities.contains(this.server)) {
                throw new CertificateException("target verification failed of " + peerIdentities);
            }
        }
        if (this.configuration.isExpiredCertificatesCheckEnabled()) {
            Date date = new Date();
            i = 0;
            while (i < nSize) {
                try {
                    x509Certificates[i].checkValidity(date);
                    i++;
                } catch (GeneralSecurityException e3) {
                    throw new CertificateException("invalid date of " + this.server);
                }
            }
        }
    }

    public static List<String> getPeerIdentity(X509Certificate x509Certificate) {
        List<String> names = getSubjectAlternativeNames(x509Certificate);
        if (!names.isEmpty()) {
            return names;
        }
        String name = x509Certificate.getSubjectDN().getName();
        Matcher matcher = cnPattern.matcher(name);
        if (matcher.find()) {
            name = matcher.group(2);
        }
        names = new ArrayList();
        names.add(name);
        return names;
    }

    private static List<String> getSubjectAlternativeNames(X509Certificate certificate) {
        List<String> identities = new ArrayList();
        try {
            if (certificate.getSubjectAlternativeNames() == null) {
                return Collections.emptyList();
            }
            return identities;
        } catch (CertificateParsingException e) {
            e.printStackTrace();
            return identities;
        }
    }
}
