package org.jivesoftware.smackx.search;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.packet.DataForm;
import org.xmlpull.v1.XmlPullParser;

public class UserSearch extends IQ {

    public static class Provider implements IQProvider {
        public IQ parseIQ(XmlPullParser parser) throws Exception {
            UserSearch search = null;
            SimpleUserSearch simpleUserSearch = new SimpleUserSearch();
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2 && parser.getName().equals("instructions")) {
                    UserSearch.buildDataForm(simpleUserSearch, parser.nextText(), parser);
                    return simpleUserSearch;
                } else if (eventType == 2 && parser.getName().equals("item")) {
                    simpleUserSearch.parseItems(parser);
                    return simpleUserSearch;
                } else if (eventType == 2 && parser.getNamespace().equals("jabber:x:data")) {
                    search = new UserSearch();
                    search.addExtension(PacketParserUtils.parsePacketExtension(parser.getName(), parser.getNamespace(), parser));
                } else if (eventType == 3 && parser.getName().equals("query")) {
                    done = true;
                }
            }
            if (search != null) {
                return search;
            }
            return simpleUserSearch;
        }
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\"jabber:iq:search\">");
        buf.append(getExtensionsXML());
        buf.append("</query>");
        return buf.toString();
    }

    public Form getSearchForm(XMPPConnection con, String searchService) throws XMPPException {
        UserSearch search = new UserSearch();
        search.setType(Type.GET);
        search.setTo(searchService);
        PacketCollector collector = con.createPacketCollector(new PacketIDFilter(search.getPacketID()));
        con.sendPacket(search);
        IQ response = (IQ) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (response.getError() == null) {
            return Form.getFormFrom(response);
        } else {
            throw new XMPPException(response.getError());
        }
    }

    public ReportedData sendSearchForm(XMPPConnection con, Form searchForm, String searchService) throws XMPPException {
        UserSearch search = new UserSearch();
        search.setType(Type.SET);
        search.setTo(searchService);
        search.addExtension(searchForm.getDataFormToSend());
        PacketCollector collector = con.createPacketCollector(new PacketIDFilter(search.getPacketID()));
        con.sendPacket(search);
        IQ response = (IQ) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (response.getError() != null) {
            return sendSimpleSearchForm(con, searchForm, searchService);
        } else {
            return ReportedData.getReportedDataFrom(response);
        }
    }

    public ReportedData sendSimpleSearchForm(XMPPConnection con, Form searchForm, String searchService) throws XMPPException {
        SimpleUserSearch search = new SimpleUserSearch();
        search.setForm(searchForm);
        search.setType(Type.SET);
        search.setTo(searchService);
        PacketCollector collector = con.createPacketCollector(new PacketIDFilter(search.getPacketID()));
        con.sendPacket(search);
        IQ response = (IQ) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (response.getError() != null) {
            throw new XMPPException(response.getError());
        } else if (response instanceof SimpleUserSearch) {
            return ((SimpleUserSearch) response).getReportedData();
        } else {
            return null;
        }
    }

    /* access modifiers changed from: private|static */
    public static void buildDataForm(SimpleUserSearch search, String instructions, XmlPullParser parser) throws Exception {
        DataForm dataForm = new DataForm(Form.TYPE_FORM);
        boolean done = false;
        dataForm.setTitle("User Search");
        dataForm.addInstruction(instructions);
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2 && !parser.getNamespace().equals("jabber:x:data")) {
                String name = parser.getName();
                FormField field = new FormField(name);
                if (name.equals("first")) {
                    field.setLabel("First Name");
                } else if (name.equals("last")) {
                    field.setLabel("Last Name");
                } else if (name.equals("email")) {
                    field.setLabel("Email Address");
                } else if (name.equals("nick")) {
                    field.setLabel("Nickname");
                }
                field.setType(FormField.TYPE_TEXT_SINGLE);
                dataForm.addField(field);
            } else if (eventType == 3) {
                if (parser.getName().equals("query")) {
                    done = true;
                }
            } else if (eventType == 2 && parser.getNamespace().equals("jabber:x:data")) {
                search.addExtension(PacketParserUtils.parsePacketExtension(parser.getName(), parser.getNamespace(), parser));
                done = true;
            }
        }
        if (search.getExtension(GroupChatInvitation.ELEMENT_NAME, "jabber:x:data") == null) {
            search.addExtension(dataForm);
        }
    }
}
