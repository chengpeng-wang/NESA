package org.apache.xmpp;

import java.io.IOException;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.util.StringUtils;

public class SendTest {
    private static String password = "ritwik2003";
    private static String username = "davanumsrinivas";

    public static class MessageParrot implements PacketListener {
        private XMPPConnection xmppConnection;

        public MessageParrot(XMPPConnection conn) {
            this.xmppConnection = conn;
        }

        public void processPacket(Packet packet) {
            Message message = (Message) packet;
            if (message.getBody() != null) {
                String fromName = StringUtils.parseBareAddress(message.getFrom());
                System.out.println("Message from " + fromName + "\n" + message.getBody() + "\n");
                Message reply = new Message();
                reply.setTo(fromName);
                reply.setBody("I am a Java bot. You said: " + message.getBody());
                this.xmppConnection.sendPacket(reply);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting IM client");
        XMPPConnection connection = new XMPPConnection(new ConnectionConfiguration("talk.google.com", 5222, "gmail.com"));
        try {
            connection.connect();
            System.out.println("Connected to " + connection.getHost());
        } catch (XMPPException e) {
            System.out.println("Failed to connect to " + connection.getHost());
            System.exit(1);
        }
        try {
            connection.login(username, password);
            System.out.println("Logged in as " + connection.getUser());
            connection.sendPacket(new Presence(Type.available));
        } catch (XMPPException e2) {
            System.out.println("Failed to log in as " + username);
            System.exit(1);
        }
        connection.addPacketListener(new MessageParrot(connection), new MessageTypeFilter(Message.Type.chat));
        if (args.length > 0) {
            Message msg = new Message(args[0], Message.Type.chat);
            msg.setBody("Test");
            connection.sendPacket(msg);
        }
        System.out.println("Press enter to disconnect\n");
        try {
            System.in.read();
        } catch (IOException e3) {
        }
        connection.disconnect();
    }
}
