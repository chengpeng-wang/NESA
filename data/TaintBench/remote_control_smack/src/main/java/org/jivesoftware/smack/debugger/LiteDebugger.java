package org.jivesoftware.smack.debugger;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Reader;
import java.io.Writer;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.ObservableReader;
import org.jivesoftware.smack.util.ObservableWriter;
import org.jivesoftware.smack.util.ReaderListener;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smack.util.WriterListener;

public class LiteDebugger implements SmackDebugger {
    private static final String NEWLINE = "\n";
    private XMPPConnection connection = null;
    private JFrame frame = null;
    private PacketListener listener = null;
    private Reader reader;
    private ReaderListener readerListener;
    private Writer writer;
    private WriterListener writerListener;

    private class PopupListener extends MouseAdapter {
        JPopupMenu popup;

        PopupListener(JPopupMenu popupMenu) {
            this.popup = popupMenu;
        }

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                this.popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    public LiteDebugger(XMPPConnection connection, Writer writer, Reader reader) {
        this.connection = connection;
        this.writer = writer;
        this.reader = reader;
        createDebug();
    }

    private void createDebug() {
        this.frame = new JFrame("Smack Debug Window -- " + this.connection.getServiceName() + ":" + this.connection.getPort());
        this.frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                LiteDebugger.this.rootWindowClosing(evt);
            }
        });
        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel allPane = new JPanel();
        allPane.setLayout(new GridLayout(3, 1));
        tabbedPane.add("All", allPane);
        final JTextArea sentText1 = new JTextArea();
        final JTextArea sentText2 = new JTextArea();
        sentText1.setEditable(false);
        sentText2.setEditable(false);
        sentText1.setForeground(new Color(112, 3, 3));
        sentText2.setForeground(new Color(112, 3, 3));
        allPane.add(new JScrollPane(sentText1));
        tabbedPane.add("Sent", new JScrollPane(sentText2));
        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuItem1 = new JMenuItem("Copy");
        menuItem1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(sentText1.getText()), null);
            }
        });
        JMenuItem menuItem2 = new JMenuItem("Clear");
        menuItem2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sentText1.setText("");
                sentText2.setText("");
            }
        });
        MouseListener popupListener = new PopupListener(menu);
        sentText1.addMouseListener(popupListener);
        sentText2.addMouseListener(popupListener);
        menu.add(menuItem1);
        menu.add(menuItem2);
        final JTextArea receivedText1 = new JTextArea();
        final JTextArea receivedText2 = new JTextArea();
        receivedText1.setEditable(false);
        receivedText2.setEditable(false);
        receivedText1.setForeground(new Color(6, 76, 133));
        receivedText2.setForeground(new Color(6, 76, 133));
        allPane.add(new JScrollPane(receivedText1));
        tabbedPane.add("Received", new JScrollPane(receivedText2));
        menu = new JPopupMenu();
        menuItem1 = new JMenuItem("Copy");
        menuItem1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(receivedText1.getText()), null);
            }
        });
        menuItem2 = new JMenuItem("Clear");
        menuItem2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                receivedText1.setText("");
                receivedText2.setText("");
            }
        });
        popupListener = new PopupListener(menu);
        receivedText1.addMouseListener(popupListener);
        receivedText2.addMouseListener(popupListener);
        menu.add(menuItem1);
        menu.add(menuItem2);
        final JTextArea interpretedText1 = new JTextArea();
        final JTextArea interpretedText2 = new JTextArea();
        interpretedText1.setEditable(false);
        interpretedText2.setEditable(false);
        interpretedText1.setForeground(new Color(1, 94, 35));
        interpretedText2.setForeground(new Color(1, 94, 35));
        allPane.add(new JScrollPane(interpretedText1));
        tabbedPane.add("Interpreted", new JScrollPane(interpretedText2));
        menu = new JPopupMenu();
        menuItem1 = new JMenuItem("Copy");
        menuItem1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(interpretedText1.getText()), null);
            }
        });
        menuItem2 = new JMenuItem("Clear");
        menuItem2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                interpretedText1.setText("");
                interpretedText2.setText("");
            }
        });
        popupListener = new PopupListener(menu);
        interpretedText1.addMouseListener(popupListener);
        interpretedText2.addMouseListener(popupListener);
        menu.add(menuItem1);
        menu.add(menuItem2);
        this.frame.getContentPane().add(tabbedPane);
        this.frame.setSize(550, 400);
        this.frame.setVisible(true);
        ObservableReader debugReader = new ObservableReader(this.reader);
        this.readerListener = new ReaderListener() {
            public void read(String str) {
                int index = str.lastIndexOf(">");
                if (index != -1) {
                    receivedText1.append(str.substring(0, index + 1));
                    receivedText2.append(str.substring(0, index + 1));
                    receivedText1.append(LiteDebugger.NEWLINE);
                    receivedText2.append(LiteDebugger.NEWLINE);
                    if (str.length() > index) {
                        receivedText1.append(str.substring(index + 1));
                        receivedText2.append(str.substring(index + 1));
                        return;
                    }
                    return;
                }
                receivedText1.append(str);
                receivedText2.append(str);
            }
        };
        debugReader.addReaderListener(this.readerListener);
        ObservableWriter debugWriter = new ObservableWriter(this.writer);
        this.writerListener = new WriterListener() {
            public void write(String str) {
                sentText1.append(str);
                sentText2.append(str);
                if (str.endsWith(">")) {
                    sentText1.append(LiteDebugger.NEWLINE);
                    sentText2.append(LiteDebugger.NEWLINE);
                }
            }
        };
        debugWriter.addWriterListener(this.writerListener);
        this.reader = debugReader;
        this.writer = debugWriter;
        this.listener = new PacketListener() {
            public void processPacket(Packet packet) {
                interpretedText1.append(packet.toXML());
                interpretedText2.append(packet.toXML());
                interpretedText1.append(LiteDebugger.NEWLINE);
                interpretedText2.append(LiteDebugger.NEWLINE);
            }
        };
    }

    public void rootWindowClosing(WindowEvent evt) {
        this.connection.removePacketListener(this.listener);
        ((ObservableReader) this.reader).removeReaderListener(this.readerListener);
        ((ObservableWriter) this.writer).removeWriterListener(this.writerListener);
    }

    public Reader newConnectionReader(Reader newReader) {
        ((ObservableReader) this.reader).removeReaderListener(this.readerListener);
        ObservableReader debugReader = new ObservableReader(newReader);
        debugReader.addReaderListener(this.readerListener);
        this.reader = debugReader;
        return this.reader;
    }

    public Writer newConnectionWriter(Writer newWriter) {
        ((ObservableWriter) this.writer).removeWriterListener(this.writerListener);
        ObservableWriter debugWriter = new ObservableWriter(newWriter);
        debugWriter.addWriterListener(this.writerListener);
        this.writer = debugWriter;
        return this.writer;
    }

    public void userHasLogged(String user) {
        this.frame.setTitle(("Smack Debug Window -- " + ("".equals(StringUtils.parseName(user)) ? "" : StringUtils.parseBareAddress(user)) + "@" + this.connection.getServiceName() + ":" + this.connection.getPort()) + "/" + StringUtils.parseResource(user));
    }

    public Reader getReader() {
        return this.reader;
    }

    public Writer getWriter() {
        return this.writer;
    }

    public PacketListener getReaderListener() {
        return this.listener;
    }

    public PacketListener getWriterListener() {
        return null;
    }
}
