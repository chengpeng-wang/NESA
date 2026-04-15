package org.jivesoftware.smackx.workgroup.packet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.workgroup.packet.Transcripts.AgentDetail;
import org.jivesoftware.smackx.workgroup.packet.Transcripts.TranscriptSummary;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class TranscriptsProvider implements IQProvider {
    private static final SimpleDateFormat UTC_FORMAT = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");

    static {
        UTC_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT+0"));
    }

    public IQ parseIQ(XmlPullParser parser) throws Exception {
        String userID = parser.getAttributeValue("", "userID");
        List<TranscriptSummary> summaries = new ArrayList();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("transcript")) {
                    summaries.add(parseSummary(parser));
                }
            } else if (eventType == 3 && parser.getName().equals("transcripts")) {
                done = true;
            }
        }
        return new Transcripts(userID, summaries);
    }

    private TranscriptSummary parseSummary(XmlPullParser parser) throws IOException, XmlPullParserException {
        String sessionID = parser.getAttributeValue("", "sessionID");
        Date joinTime = null;
        Date leftTime = null;
        List<AgentDetail> agents = new ArrayList();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("joinTime")) {
                    try {
                        joinTime = UTC_FORMAT.parse(parser.nextText());
                    } catch (ParseException e) {
                    }
                } else if (parser.getName().equals("leftTime")) {
                    try {
                        leftTime = UTC_FORMAT.parse(parser.nextText());
                    } catch (ParseException e2) {
                    }
                } else if (parser.getName().equals("agents")) {
                    agents = parseAgents(parser);
                }
            } else if (eventType == 3 && parser.getName().equals("transcript")) {
                done = true;
            }
        }
        return new TranscriptSummary(sessionID, joinTime, leftTime, agents);
    }

    private List<AgentDetail> parseAgents(XmlPullParser parser) throws IOException, XmlPullParserException {
        List<AgentDetail> agents = new ArrayList();
        String agentJID = null;
        Date joinTime = null;
        Date leftTime = null;
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("agentJID")) {
                    agentJID = parser.nextText();
                } else if (parser.getName().equals("joinTime")) {
                    try {
                        joinTime = UTC_FORMAT.parse(parser.nextText());
                    } catch (ParseException e) {
                    }
                } else if (parser.getName().equals("leftTime")) {
                    try {
                        leftTime = UTC_FORMAT.parse(parser.nextText());
                    } catch (ParseException e2) {
                    }
                } else if (parser.getName().equals("agent")) {
                    agentJID = null;
                    joinTime = null;
                    leftTime = null;
                }
            } else if (eventType == 3) {
                if (parser.getName().equals("agents")) {
                    done = true;
                } else if (parser.getName().equals("agent")) {
                    agents.add(new AgentDetail(agentJID, joinTime, leftTime));
                }
            }
        }
        return agents;
    }
}
