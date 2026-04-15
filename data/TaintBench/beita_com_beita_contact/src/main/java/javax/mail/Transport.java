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
        Enumeration e;
        Transport transport;
        Object e2;
        if (addresses == null || addresses.length == 0) {
            throw new SendFailedException("No recipient addresses");
        }
        int dsize;
        Hashtable protocols = new Hashtable();
        Vector invalid = new Vector();
        Vector validSent = new Vector();
        Vector validUnsent = new Vector();
        for (dsize = 0; dsize < addresses.length; dsize++) {
            if (protocols.containsKey(addresses[dsize].getType())) {
                ((Vector) protocols.get(addresses[dsize].getType())).addElement(addresses[dsize]);
            } else {
                Vector w = new Vector();
                w.addElement(addresses[dsize]);
                protocols.put(addresses[dsize].getType(), w);
            }
        }
        dsize = protocols.size();
        if (dsize == 0) {
            throw new SendFailedException("No recipient addresses");
        }
        Session s;
        if (msg.session != null) {
            s = msg.session;
        } else {
            s = Session.getDefaultInstance(System.getProperties(), null);
        }
        if (dsize == 1) {
            dsize = s.getTransport(addresses[0]);
            try {
                dsize.connect();
                dsize.sendMessage(msg, addresses);
                return;
            } finally {
                dsize.close();
            }
        } else {
            Address[] protaddresses;
            MessagingException chainedEx = null;
            e = protocols.elements();
            boolean sendFailed = false;
            while (e.hasMoreElements()) {
                Vector v = (Vector) e.nextElement();
                protaddresses = new Address[v.size()];
                v.copyInto(protaddresses);
                transport = s.getTransport(protaddresses[null]);
                if (transport == null) {
                    for (Object addElement : protaddresses) {
                        invalid.addElement(addElement);
                    }
                } else {
                    try {
                        transport.connect();
                        transport.sendMessage(msg, protaddresses);
                        transport.close();
                    } catch (SendFailedException e3) {
                        sendFailed = e3;
                        if (chainedEx == null) {
                            chainedEx = sendFailed;
                        } else {
                            chainedEx.setNextException(sendFailed);
                        }
                        protaddresses = sendFailed.getInvalidAddresses();
                        if (protaddresses != null) {
                            for (Object addElement2 : protaddresses) {
                                invalid.addElement(addElement2);
                            }
                        }
                        protaddresses = sendFailed.getValidSentAddresses();
                        if (protaddresses != null) {
                            for (Object addElement22 : protaddresses) {
                                validSent.addElement(addElement22);
                            }
                        }
                        sendFailed = sendFailed.getValidUnsentAddresses();
                        if (sendFailed) {
                            for (Address[] protaddresses2 : sendFailed) {
                                validUnsent.addElement(protaddresses2);
                            }
                        }
                        transport.close();
                        sendFailed = true;
                    } catch (MessagingException mex) {
                        if (chainedEx == null) {
                            chainedEx = mex;
                        } else {
                            chainedEx.setNextException(mex);
                        }
                        transport.close();
                        sendFailed = true;
                    } catch (Throwable msg2) {
                        e2 = msg2;
                        boolean sendFailed2 = true;
                    }
                }
            }
            if (sendFailed || invalid.size() != null || validUnsent.size() != 0) {
                protaddresses2 = null;
                s = null;
                sendFailed = null;
                if (validSent.size() > 0) {
                    protaddresses2 = new Address[validSent.size()];
                    validSent.copyInto(protaddresses2);
                }
                if (validUnsent.size() > 0) {
                    s = new Address[validUnsent.size()];
                    validUnsent.copyInto(s);
                }
                if (invalid.size() > 0) {
                    sendFailed = new Address[invalid.size()];
                    invalid.copyInto(sendFailed);
                }
                throw new SendFailedException("Sending failed", chainedEx, protaddresses2, s, sendFailed);
            }
            return;
        }
        transport.close();
        throw e;
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
