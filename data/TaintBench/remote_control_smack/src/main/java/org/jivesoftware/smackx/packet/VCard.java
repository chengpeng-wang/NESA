package org.jivesoftware.smackx.packet;

import com.xmpp.client.util.AppPreferences;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smack.util.StringUtils;

public class VCard extends IQ {
    private String avatar;
    /* access modifiers changed from: private */
    public String emailHome;
    /* access modifiers changed from: private */
    public String emailWork;
    /* access modifiers changed from: private */
    public String firstName;
    /* access modifiers changed from: private */
    public Map<String, String> homeAddr = new HashMap();
    /* access modifiers changed from: private */
    public Map<String, String> homePhones = new HashMap();
    /* access modifiers changed from: private */
    public String lastName;
    /* access modifiers changed from: private */
    public String middleName;
    /* access modifiers changed from: private */
    public String organization;
    /* access modifiers changed from: private */
    public String organizationUnit;
    /* access modifiers changed from: private */
    public Map<String, String> otherSimpleFields = new HashMap();
    /* access modifiers changed from: private */
    public Map<String, String> otherUnescapableFields = new HashMap();
    /* access modifiers changed from: private */
    public Map<String, String> workAddr = new HashMap();
    /* access modifiers changed from: private */
    public Map<String, String> workPhones = new HashMap();

    private interface ContentBuilder {
        void addTagContent();
    }

    private class VCardWriter {
        /* access modifiers changed from: private|final */
        public final StringBuilder sb;

        VCardWriter(StringBuilder sb) {
            this.sb = sb;
        }

        public void write() {
            appendTag("vCard", "xmlns", "vcard-temp", VCard.this.hasContent(), new ContentBuilder() {
                public void addTagContent() {
                    VCardWriter.this.buildActualContent();
                }
            });
        }

        /* access modifiers changed from: private */
        public void buildActualContent() {
            if (VCard.this.hasNameField()) {
                appendN();
            }
            appendOrganization();
            appendGenericFields();
            appendEmail(VCard.this.emailWork, "WORK");
            appendEmail(VCard.this.emailHome, "HOME");
            appendPhones(VCard.this.workPhones, "WORK");
            appendPhones(VCard.this.homePhones, "HOME");
            appendAddress(VCard.this.workAddr, "WORK");
            appendAddress(VCard.this.homeAddr, "HOME");
        }

        private void appendEmail(final String email, final String type) {
            if (email != null) {
                appendTag("EMAIL", true, new ContentBuilder() {
                    public void addTagContent() {
                        VCardWriter.this.appendEmptyTag(type);
                        VCardWriter.this.appendEmptyTag("INTERNET");
                        VCardWriter.this.appendEmptyTag("PREF");
                        VCardWriter.this.appendTag(AppPreferences.KEY_PREFS_USERID, StringUtils.escapeForXML(email));
                    }
                });
            }
        }

        private void appendPhones(Map<String, String> phones, final String code) {
            for (final Entry entry : phones.entrySet()) {
                appendTag("TEL", true, new ContentBuilder() {
                    public void addTagContent() {
                        VCardWriter.this.appendEmptyTag(entry.getKey());
                        VCardWriter.this.appendEmptyTag(code);
                        VCardWriter.this.appendTag("NUMBER", StringUtils.escapeForXML((String) entry.getValue()));
                    }
                });
            }
        }

        private void appendAddress(final Map<String, String> addr, final String code) {
            if (addr.size() > 0) {
                appendTag("ADR", true, new ContentBuilder() {
                    public void addTagContent() {
                        VCardWriter.this.appendEmptyTag(code);
                        for (Entry entry : addr.entrySet()) {
                            VCardWriter.this.appendTag((String) entry.getKey(), StringUtils.escapeForXML((String) entry.getValue()));
                        }
                    }
                });
            }
        }

        /* access modifiers changed from: private */
        public void appendEmptyTag(Object tag) {
            this.sb.append('<').append(tag).append("/>");
        }

        private void appendGenericFields() {
            for (Entry entry : VCard.this.otherSimpleFields.entrySet()) {
                appendTag(entry.getKey().toString(), StringUtils.escapeForXML((String) entry.getValue()));
            }
            for (Entry entry2 : VCard.this.otherUnescapableFields.entrySet()) {
                appendTag(entry2.getKey().toString(), (String) entry2.getValue());
            }
        }

        private void appendOrganization() {
            if (VCard.this.hasOrganizationFields()) {
                appendTag("ORG", true, new ContentBuilder() {
                    public void addTagContent() {
                        VCardWriter.this.appendTag("ORGNAME", StringUtils.escapeForXML(VCard.this.organization));
                        VCardWriter.this.appendTag("ORGUNIT", StringUtils.escapeForXML(VCard.this.organizationUnit));
                    }
                });
            }
        }

        private void appendN() {
            appendTag("N", true, new ContentBuilder() {
                public void addTagContent() {
                    VCardWriter.this.appendTag("FAMILY", StringUtils.escapeForXML(VCard.this.lastName));
                    VCardWriter.this.appendTag("GIVEN", StringUtils.escapeForXML(VCard.this.firstName));
                    VCardWriter.this.appendTag("MIDDLE", StringUtils.escapeForXML(VCard.this.middleName));
                }
            });
        }

        private void appendTag(String tag, String attr, String attrValue, boolean hasContent, ContentBuilder builder) {
            this.sb.append('<').append(tag);
            if (attr != null) {
                this.sb.append(' ').append(attr).append('=').append('\'').append(attrValue).append('\'');
            }
            if (hasContent) {
                this.sb.append('>');
                builder.addTagContent();
                this.sb.append("</").append(tag).append(">\n");
                return;
            }
            this.sb.append("/>\n");
        }

        private void appendTag(String tag, boolean hasContent, ContentBuilder builder) {
            appendTag(tag, null, null, hasContent, builder);
        }

        /* access modifiers changed from: private */
        public void appendTag(String tag, final String tagText) {
            if (tagText != null) {
                appendTag(tag, true, new ContentBuilder() {
                    public void addTagContent() {
                        VCardWriter.this.sb.append(tagText.trim());
                    }
                });
            }
        }
    }

    public String getField(String field) {
        return (String) this.otherSimpleFields.get(field);
    }

    public void setField(String field, String value) {
        setField(field, value, false);
    }

    public void setField(String field, String value, boolean isUnescapable) {
        if (isUnescapable) {
            this.otherUnescapableFields.put(field, value);
        } else {
            this.otherSimpleFields.put(field, value);
        }
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        updateFN();
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        updateFN();
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
        updateFN();
    }

    public String getNickName() {
        return (String) this.otherSimpleFields.get("NICKNAME");
    }

    public void setNickName(String nickName) {
        this.otherSimpleFields.put("NICKNAME", nickName);
    }

    public String getEmailHome() {
        return this.emailHome;
    }

    public void setEmailHome(String email) {
        this.emailHome = email;
    }

    public String getEmailWork() {
        return this.emailWork;
    }

    public void setEmailWork(String emailWork) {
        this.emailWork = emailWork;
    }

    public String getJabberId() {
        return (String) this.otherSimpleFields.get("JABBERID");
    }

    public void setJabberId(String jabberId) {
        this.otherSimpleFields.put("JABBERID", jabberId);
    }

    public String getOrganization() {
        return this.organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOrganizationUnit() {
        return this.organizationUnit;
    }

    public void setOrganizationUnit(String organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public String getAddressFieldHome(String addrField) {
        return (String) this.homeAddr.get(addrField);
    }

    public void setAddressFieldHome(String addrField, String value) {
        this.homeAddr.put(addrField, value);
    }

    public String getAddressFieldWork(String addrField) {
        return (String) this.workAddr.get(addrField);
    }

    public void setAddressFieldWork(String addrField, String value) {
        this.workAddr.put(addrField, value);
    }

    public void setPhoneHome(String phoneType, String phoneNum) {
        this.homePhones.put(phoneType, phoneNum);
    }

    public String getPhoneHome(String phoneType) {
        return (String) this.homePhones.get(phoneType);
    }

    public void setPhoneWork(String phoneType, String phoneNum) {
        this.workPhones.put(phoneType, phoneNum);
    }

    public String getPhoneWork(String phoneType) {
        return (String) this.workPhones.get(phoneType);
    }

    public void setAvatar(URL avatarURL) {
        byte[] bytes = new byte[0];
        try {
            bytes = getBytes(avatarURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setAvatar(bytes);
    }

    public void setAvatar(byte[] bytes) {
        if (bytes == null) {
            this.otherUnescapableFields.remove("PHOTO");
            return;
        }
        String encodedImage = StringUtils.encodeBase64(bytes);
        this.avatar = encodedImage;
        setField("PHOTO", "<TYPE>image/jpeg</TYPE><BINVAL>" + encodedImage + "</BINVAL>", true);
    }

    public void setEncodedImage(String encodedAvatar) {
        this.avatar = encodedAvatar;
    }

    public byte[] getAvatar() {
        if (this.avatar == null) {
            return null;
        }
        return StringUtils.decodeBase64(this.avatar);
    }

    public static byte[] getBytes(URL url) throws IOException {
        File file = new File(url.getPath());
        if (file.exists()) {
            return getFileBytes(file);
        }
        return null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0025  */
    private static byte[] getFileBytes(java.io.File r7) throws java.io.IOException {
        /*
        r0 = 0;
        r1 = new java.io.BufferedInputStream;	 Catch:{ all -> 0x002f }
        r5 = new java.io.FileInputStream;	 Catch:{ all -> 0x002f }
        r5.<init>(r7);	 Catch:{ all -> 0x002f }
        r1.<init>(r5);	 Catch:{ all -> 0x002f }
        r5 = r7.length();	 Catch:{ all -> 0x0021 }
        r3 = (int) r5;	 Catch:{ all -> 0x0021 }
        r2 = new byte[r3];	 Catch:{ all -> 0x0021 }
        r4 = r1.read(r2);	 Catch:{ all -> 0x0021 }
        r5 = r2.length;	 Catch:{ all -> 0x0021 }
        if (r4 == r5) goto L_0x0029;
    L_0x0019:
        r5 = new java.io.IOException;	 Catch:{ all -> 0x0021 }
        r6 = "Entire file not read";
        r5.<init>(r6);	 Catch:{ all -> 0x0021 }
        throw r5;	 Catch:{ all -> 0x0021 }
    L_0x0021:
        r5 = move-exception;
        r0 = r1;
    L_0x0023:
        if (r0 == 0) goto L_0x0028;
    L_0x0025:
        r0.close();
    L_0x0028:
        throw r5;
    L_0x0029:
        if (r1 == 0) goto L_0x002e;
    L_0x002b:
        r1.close();
    L_0x002e:
        return r2;
    L_0x002f:
        r5 = move-exception;
        goto L_0x0023;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smackx.packet.VCard.getFileBytes(java.io.File):byte[]");
    }

    public String getAvatarHash() {
        byte[] bytes = getAvatar();
        if (bytes == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(bytes);
            return StringUtils.encodeHex(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updateFN() {
        StringBuilder sb = new StringBuilder();
        if (this.firstName != null) {
            sb.append(StringUtils.escapeForXML(this.firstName)).append(' ');
        }
        if (this.middleName != null) {
            sb.append(StringUtils.escapeForXML(this.middleName)).append(' ');
        }
        if (this.lastName != null) {
            sb.append(StringUtils.escapeForXML(this.lastName));
        }
        setField("FN", sb.toString());
    }

    public void save(XMPPConnection connection) throws XMPPException {
        checkAuthenticated(connection, true);
        setType(Type.SET);
        setFrom(connection.getUser());
        PacketCollector collector = connection.createPacketCollector(new PacketIDFilter(getPacketID()));
        connection.sendPacket(this);
        Packet response = collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (response.getError() != null) {
            throw new XMPPException(response.getError());
        }
    }

    public void load(XMPPConnection connection) throws XMPPException {
        checkAuthenticated(connection, true);
        setFrom(connection.getUser());
        doLoad(connection, connection.getUser());
    }

    public void load(XMPPConnection connection, String user) throws XMPPException {
        checkAuthenticated(connection, false);
        setTo(user);
        doLoad(connection, user);
    }

    private void doLoad(XMPPConnection connection, String user) throws XMPPException {
        setType(Type.GET);
        PacketCollector collector = connection.createPacketCollector(new PacketIDFilter(getPacketID()));
        connection.sendPacket(this);
        VCard result = null;
        try {
            result = (VCard) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
            if (result == null) {
                String errorMessage = "Timeout getting VCard information";
                throw new XMPPException(errorMessage, new XMPPError(Condition.request_timeout, errorMessage));
            }
            if (result.getError() != null) {
                throw new XMPPException(result.getError());
            }
            copyFieldsFrom(result);
        } catch (ClassCastException e) {
            System.out.println("No VCard for " + user);
        }
    }

    public String getChildElementXML() {
        StringBuilder sb = new StringBuilder();
        new VCardWriter(sb).write();
        return sb.toString();
    }

    private void copyFieldsFrom(VCard result) {
        if (result == null) {
            result = new VCard();
        }
        for (Field field : VCard.class.getDeclaredFields()) {
            if (field.getDeclaringClass() == VCard.class && !Modifier.isFinal(field.getModifiers())) {
                try {
                    field.setAccessible(true);
                    field.set(this, field.get(result));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("This cannot happen:" + field, e);
                }
            }
        }
    }

    private void checkAuthenticated(XMPPConnection connection, boolean checkForAnonymous) {
        if (connection == null) {
            throw new IllegalArgumentException("No connection was provided");
        } else if (!connection.isAuthenticated()) {
            throw new IllegalArgumentException("Connection is not authenticated");
        } else if (checkForAnonymous && connection.isAnonymous()) {
            throw new IllegalArgumentException("Connection cannot be anonymous");
        }
    }

    /* access modifiers changed from: private */
    public boolean hasContent() {
        return hasNameField() || hasOrganizationFields() || this.emailHome != null || this.emailWork != null || this.otherSimpleFields.size() > 0 || this.otherUnescapableFields.size() > 0 || this.homeAddr.size() > 0 || this.homePhones.size() > 0 || this.workAddr.size() > 0 || this.workPhones.size() > 0;
    }

    /* access modifiers changed from: private */
    public boolean hasNameField() {
        return (this.firstName == null && this.lastName == null && this.middleName == null) ? false : true;
    }

    /* access modifiers changed from: private */
    public boolean hasOrganizationFields() {
        return (this.organization == null && this.organizationUnit == null) ? false : true;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VCard vCard = (VCard) o;
        if (this.emailHome != null) {
            if (!this.emailHome.equals(vCard.emailHome)) {
                return false;
            }
        } else if (vCard.emailHome != null) {
            return false;
        }
        if (this.emailWork != null) {
            if (!this.emailWork.equals(vCard.emailWork)) {
                return false;
            }
        } else if (vCard.emailWork != null) {
            return false;
        }
        if (this.firstName != null) {
            if (!this.firstName.equals(vCard.firstName)) {
                return false;
            }
        } else if (vCard.firstName != null) {
            return false;
        }
        if (!this.homeAddr.equals(vCard.homeAddr) || !this.homePhones.equals(vCard.homePhones)) {
            return false;
        }
        if (this.lastName != null) {
            if (!this.lastName.equals(vCard.lastName)) {
                return false;
            }
        } else if (vCard.lastName != null) {
            return false;
        }
        if (this.middleName != null) {
            if (!this.middleName.equals(vCard.middleName)) {
                return false;
            }
        } else if (vCard.middleName != null) {
            return false;
        }
        if (this.organization != null) {
            if (!this.organization.equals(vCard.organization)) {
                return false;
            }
        } else if (vCard.organization != null) {
            return false;
        }
        if (this.organizationUnit != null) {
            if (!this.organizationUnit.equals(vCard.organizationUnit)) {
                return false;
            }
        } else if (vCard.organizationUnit != null) {
            return false;
        }
        if (this.otherSimpleFields.equals(vCard.otherSimpleFields) && this.workAddr.equals(vCard.workAddr)) {
            return this.workPhones.equals(vCard.workPhones);
        }
        return false;
    }

    public int hashCode() {
        int hashCode;
        int i = 0;
        int hashCode2 = ((((((((this.homePhones.hashCode() * 29) + this.workPhones.hashCode()) * 29) + this.homeAddr.hashCode()) * 29) + this.workAddr.hashCode()) * 29) + (this.firstName != null ? this.firstName.hashCode() : 0)) * 29;
        if (this.lastName != null) {
            hashCode = this.lastName.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode2 = (hashCode2 + hashCode) * 29;
        if (this.middleName != null) {
            hashCode = this.middleName.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode2 = (hashCode2 + hashCode) * 29;
        if (this.emailHome != null) {
            hashCode = this.emailHome.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode2 = (hashCode2 + hashCode) * 29;
        if (this.emailWork != null) {
            hashCode = this.emailWork.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode2 = (hashCode2 + hashCode) * 29;
        if (this.organization != null) {
            hashCode = this.organization.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode = (hashCode2 + hashCode) * 29;
        if (this.organizationUnit != null) {
            i = this.organizationUnit.hashCode();
        }
        return ((hashCode + i) * 29) + this.otherSimpleFields.hashCode();
    }

    public String toString() {
        return getChildElementXML();
    }
}
