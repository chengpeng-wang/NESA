package javax.mail;

import java.util.Vector;
import javax.mail.event.MailEvent;

class EventQueue implements Runnable {
    private QueueElement head = null;
    private Thread qThread = new Thread(this, "JavaMail-EventQueue");
    private QueueElement tail = null;

    static class QueueElement {
        MailEvent event = null;
        QueueElement next = null;
        QueueElement prev = null;
        Vector vector = null;

        QueueElement(MailEvent event, Vector vector) {
            this.event = event;
            this.vector = vector;
        }
    }

    public EventQueue() {
        this.qThread.setDaemon(true);
        this.qThread.start();
    }

    public synchronized void enqueue(MailEvent event, Vector vector) {
        QueueElement newElt = new QueueElement(event, vector);
        if (this.head == null) {
            this.head = newElt;
            this.tail = newElt;
        } else {
            newElt.next = this.head;
            this.head.prev = newElt;
            this.head = newElt;
        }
        notifyAll();
    }

    private synchronized QueueElement dequeue() throws InterruptedException {
        QueueElement elt;
        while (this.tail == null) {
            wait();
        }
        elt = this.tail;
        this.tail = elt.prev;
        if (this.tail == null) {
            this.head = null;
        } else {
            this.tail.next = null;
        }
        elt.next = null;
        elt.prev = null;
        return elt;
    }

    public void run() {
        while (true) {
            try {
                QueueElement qe = dequeue();
                if (qe != null) {
                    MailEvent e = qe.event;
                    Vector v = qe.vector;
                    for (int i = 0; i < v.size(); i++) {
                        try {
                            e.dispatch(v.elementAt(i));
                        } catch (Throwable t) {
                            if (t instanceof InterruptedException) {
                                return;
                            }
                        }
                    }
                } else {
                    return;
                }
            } catch (InterruptedException e2) {
                return;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void stop() {
        if (this.qThread != null) {
            this.qThread.interrupt();
            this.qThread = null;
        }
    }
}
