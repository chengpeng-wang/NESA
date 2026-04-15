package javax.mail;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;

public abstract class Transport extends Service {
    private Vector transportListeners = null;

    public abstract void sendMessage(Message message, Address[] addressArr) throws MessagingException;

    public Transport(Session session, URLName urlname) {
        super(session, urlname);
    }

    public static void send(Message msg) throws MessagingException {
        msg.saveChanges();
        send0(msg, msg.getAllRecipients());
    }

    public static void send(Message msg, Address[] addresses) throws MessagingException {
        msg.saveChanges();
        send0(msg, addresses);
    }

    private static void send0(Message msg, Address[] addresses) throws MessagingException {
        Address[] a;
        Address[] c;
        if (addresses == null || addresses.length == 0) {
            throw new SendFailedException("No recipient addresses");
        }
        Hashtable protocols = new Hashtable();
        Vector invalid = new Vector();
        Vector validSent = new Vector();
        Vector validUnsent = new Vector();
        for (int i = 0; i < addresses.length; i++) {
            if (protocols.containsKey(addresses[i].getType())) {
                ((Vector) protocols.get(addresses[i].getType())).addElement(addresses[i]);
            } else {
                Vector w = new Vector();
                w.addElement(addresses[i]);
                protocols.put(addresses[i].getType(), w);
            }
        }
        int dsize = protocols.size();
        if (dsize == 0) {
            throw new SendFailedException("No recipient addresses");
        }
        Session s;
        if (msg.session != null) {
            s = msg.session;
        } else {
            s = Session.getDefaultInstance(System.getProperties(), null);
        }
        Transport transport;
        if (dsize == 1) {
            transport = s.getTransport(addresses[0]);
            try {
                transport.connect();
                transport.sendMessage(msg, addresses);
                transport.close();
                return;
            } catch (Throwable th) {
                transport.close();
                throw th;
            }
        }
        MessagingException chainedEx = null;
        boolean sendFailed = false;
        Enumeration e = protocols.elements();
        while (e.hasMoreElements()) {
            Vector v = (Vector) e.nextElement();
            Address[] protaddresses = new Address[v.size()];
            v.copyInto(protaddresses);
            transport = s.getTransport(protaddresses[0]);
            if (transport == null) {
                for (Object addElement : protaddresses) {
                    invalid.addElement(addElement);
                }
            } else {
                try {
                    transport.connect();
                    transport.sendMessage(msg, protaddresses);
                    transport.close();
                } catch (SendFailedException sex) {
                    sendFailed = true;
                    if (chainedEx == null) {
                        chainedEx = sex;
                    } else {
                        chainedEx.setNextException(sex);
                    }
                    a = sex.getInvalidAddresses();
                    if (a != null) {
                        for (Object addElement2 : a) {
                            invalid.addElement(addElement2);
                        }
                    }
                    a = sex.getValidSentAddresses();
                    if (a != null) {
                        for (Object addElement22 : a) {
                            validSent.addElement(addElement22);
                        }
                    }
                    c = sex.getValidUnsentAddresses();
                    if (c != null) {
                        for (Object addElement222 : c) {
                            validUnsent.addElement(addElement222);
                        }
                    }
                    transport.close();
                } catch (MessagingException mex) {
                    sendFailed = true;
                    if (chainedEx == null) {
                        chainedEx = mex;
                    } else {
                        chainedEx.setNextException(mex);
                    }
                    transport.close();
                } catch (Throwable th2) {
                    transport.close();
                    throw th2;
                }
            }
        }
        if (sendFailed || invalid.size() != 0 || validUnsent.size() != 0) {
            a = null;
            Address[] b = null;
            c = null;
            if (validSent.size() > 0) {
                a = new Address[validSent.size()];
                validSent.copyInto(a);
            }
            if (validUnsent.size() > 0) {
                b = new Address[validUnsent.size()];
                validUnsent.copyInto(b);
            }
            if (invalid.size() > 0) {
                c = new Address[invalid.size()];
                invalid.copyInto(c);
            }
            throw new SendFailedException("Sending failed", chainedEx, a, b, c);
        }
    }

    public synchronized void addTransportListener(TransportListener l) {
        if (this.transportListeners == null) {
            this.transportListeners = new Vector();
        }
        this.transportListeners.addElement(l);
    }

    public synchronized void removeTransportListener(TransportListener l) {
        if (this.transportListeners != null) {
            this.transportListeners.removeElement(l);
        }
    }

    /* access modifiers changed from: protected */
    public void notifyTransportListeners(int type, Address[] validSent, Address[] validUnsent, Address[] invalid, Message msg) {
        if (this.transportListeners != null) {
            queueEvent(new TransportEvent(this, type, validSent, validUnsent, invalid, msg), this.transportListeners);
        }
    }
}
