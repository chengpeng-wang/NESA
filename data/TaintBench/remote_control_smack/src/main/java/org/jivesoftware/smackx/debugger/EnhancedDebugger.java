package org.jivesoftware.smackx.debugger;

import com.baidu.mapapi.map.MKEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.debugger.SmackDebugger;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.ObservableReader;
import org.jivesoftware.smack.util.ObservableWriter;
import org.jivesoftware.smack.util.ReaderListener;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smack.util.WriterListener;

public class EnhancedDebugger implements SmackDebugger {
    private static final String NEWLINE = "\n";
    /* access modifiers changed from: private|static */
    public static ImageIcon iqPacketIcon;
    /* access modifiers changed from: private|static */
    public static ImageIcon messagePacketIcon;
    /* access modifiers changed from: private|static */
    public static ImageIcon packetReceivedIcon;
    /* access modifiers changed from: private|static */
    public static ImageIcon packetSentIcon;
    /* access modifiers changed from: private|static */
    public static ImageIcon presencePacketIcon;
    /* access modifiers changed from: private|static */
    public static ImageIcon unknownPacketTypeIcon;
    /* access modifiers changed from: private */
    public ConnectionListener connListener;
    /* access modifiers changed from: private */
    public XMPPConnection connection;
    private Date creationTime;
    /* access modifiers changed from: private */
    public JTextArea messageTextArea;
    /* access modifiers changed from: private */
    public DefaultTableModel messagesTable;
    private PacketListener packetReaderListener;
    private PacketListener packetWriterListener;
    private Reader reader;
    private ReaderListener readerListener;
    /* access modifiers changed from: private */
    public int receivedIQPackets;
    /* access modifiers changed from: private */
    public int receivedMessagePackets;
    /* access modifiers changed from: private */
    public int receivedOtherPackets;
    /* access modifiers changed from: private */
    public int receivedPackets;
    /* access modifiers changed from: private */
    public int receivedPresencePackets;
    /* access modifiers changed from: private */
    public int sentIQPackets;
    /* access modifiers changed from: private */
    public int sentMessagePackets;
    /* access modifiers changed from: private */
    public int sentOtherPackets;
    /* access modifiers changed from: private */
    public int sentPackets;
    /* access modifiers changed from: private */
    public int sentPresencePackets;
    private DefaultTableModel statisticsTable;
    /* access modifiers changed from: private */
    public JFormattedTextField statusField;
    JTabbedPane tabbedPane;
    /* access modifiers changed from: private */
    public JFormattedTextField userField;
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

    private class SelectionListener implements ListSelectionListener {
        JTable table;

        SelectionListener(JTable table) {
            this.table = table;
        }

        public void valueChanged(ListSelectionEvent e) {
            if (this.table.getSelectedRow() == -1) {
                EnhancedDebugger.this.messageTextArea.setText(null);
                return;
            }
            EnhancedDebugger.this.messageTextArea.setText((String) this.table.getModel().getValueAt(this.table.getSelectedRow(), 0));
            EnhancedDebugger.this.messageTextArea.setCaretPosition(0);
        }
    }

    private class AdHocPacket extends Packet {
        private String text;

        public AdHocPacket(String text) {
            this.text = text;
        }

        public String toXML() {
            return this.text;
        }
    }

    public EnhancedDebugger(XMPPConnection connection, Writer writer, Reader reader) {
        URL url = Thread.currentThread().getContextClassLoader().getResource("images/nav_left_blue.png");
        if (url != null) {
            packetReceivedIcon = new ImageIcon(url);
        }
        url = Thread.currentThread().getContextClassLoader().getResource("images/nav_right_red.png");
        if (url != null) {
            packetSentIcon = new ImageIcon(url);
        }
        url = Thread.currentThread().getContextClassLoader().getResource("images/photo_portrait.png");
        if (url != null) {
            presencePacketIcon = new ImageIcon(url);
        }
        url = Thread.currentThread().getContextClassLoader().getResource("images/question_and_answer.png");
        if (url != null) {
            iqPacketIcon = new ImageIcon(url);
        }
        url = Thread.currentThread().getContextClassLoader().getResource("images/message.png");
        if (url != null) {
            messagePacketIcon = new ImageIcon(url);
        }
        url = Thread.currentThread().getContextClassLoader().getResource("images/unknown.png");
        if (url != null) {
            unknownPacketTypeIcon = new ImageIcon(url);
        }
        this.messagesTable = null;
        this.messageTextArea = null;
        this.userField = null;
        this.statusField = null;
        this.connection = null;
        this.packetReaderListener = null;
        this.packetWriterListener = null;
        this.connListener = null;
        this.creationTime = new Date();
        this.statisticsTable = null;
        this.sentPackets = 0;
        this.receivedPackets = 0;
        this.sentIQPackets = 0;
        this.receivedIQPackets = 0;
        this.sentMessagePackets = 0;
        this.receivedMessagePackets = 0;
        this.sentPresencePackets = 0;
        this.receivedPresencePackets = 0;
        this.sentOtherPackets = 0;
        this.receivedOtherPackets = 0;
        this.connection = connection;
        this.writer = writer;
        this.reader = reader;
        createDebug();
        EnhancedDebuggerWindow.addDebugger(this);
    }

    private void createDebug() {
        this.tabbedPane = new JTabbedPane();
        addBasicPanels();
        addAdhocPacketPanel();
        addInformationPanel();
        this.packetReaderListener = new PacketListener() {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm:ss aaa");

            public void processPacket(final Packet packet) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        EnhancedDebugger.this.addReadPacketToTable(AnonymousClass1.this.dateFormatter, packet);
                    }
                });
            }
        };
        this.packetWriterListener = new PacketListener() {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm:ss aaa");

            public void processPacket(final Packet packet) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        EnhancedDebugger.this.addSentPacketToTable(AnonymousClass2.this.dateFormatter, packet);
                    }
                });
            }
        };
        this.connListener = new ConnectionListener() {
            public void connectionClosed() {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        EnhancedDebugger.this.statusField.setValue("Closed");
                        EnhancedDebuggerWindow.connectionClosed(EnhancedDebugger.this);
                    }
                });
            }

            public void connectionClosedOnError(final Exception e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        EnhancedDebugger.this.statusField.setValue("Closed due to an exception");
                        EnhancedDebuggerWindow.connectionClosedOnError(EnhancedDebugger.this, e);
                    }
                });
            }

            public void reconnectingIn(final int seconds) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        EnhancedDebugger.this.statusField.setValue("Attempt to reconnect in " + seconds + " seconds");
                    }
                });
            }

            public void reconnectionSuccessful() {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        EnhancedDebugger.this.statusField.setValue("Reconnection stablished");
                        EnhancedDebuggerWindow.connectionEstablished(EnhancedDebugger.this);
                    }
                });
            }

            public void reconnectionFailed(Exception e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        EnhancedDebugger.this.statusField.setValue("Reconnection failed");
                    }
                });
            }
        };
    }

    private void addBasicPanels() {
        JSplitPane allPane = new JSplitPane(0);
        allPane.setOneTouchExpandable(true);
        this.messagesTable = new DefaultTableModel(new Object[]{"Hide", "Timestamp", "", "", "Message", "Id", "Type", "To", "From"}, 0) {
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
            }

            public Class getColumnClass(int columnIndex) {
                if (columnIndex == 2 || columnIndex == 3) {
                    return Icon.class;
                }
                return EnhancedDebugger.super.getColumnClass(columnIndex);
            }
        };
        JTable table = new JTable(this.messagesTable);
        table.setSelectionMode(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        table.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(1).setMaxWidth(MKEvent.ERROR_PERMISSION_DENIED);
        table.getColumnModel().getColumn(1).setPreferredWidth(70);
        table.getColumnModel().getColumn(2).setMaxWidth(50);
        table.getColumnModel().getColumn(2).setPreferredWidth(30);
        table.getColumnModel().getColumn(3).setMaxWidth(50);
        table.getColumnModel().getColumn(3).setPreferredWidth(30);
        table.getColumnModel().getColumn(5).setMaxWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(55);
        table.getColumnModel().getColumn(6).setMaxWidth(200);
        table.getColumnModel().getColumn(6).setPreferredWidth(50);
        table.getColumnModel().getColumn(7).setMaxWidth(MKEvent.ERROR_PERMISSION_DENIED);
        table.getColumnModel().getColumn(7).setPreferredWidth(90);
        table.getColumnModel().getColumn(8).setMaxWidth(MKEvent.ERROR_PERMISSION_DENIED);
        table.getColumnModel().getColumn(8).setPreferredWidth(90);
        SelectionListener selectionListener = new SelectionListener(table);
        table.getSelectionModel().addListSelectionListener(selectionListener);
        table.getColumnModel().getSelectionModel().addListSelectionListener(selectionListener);
        allPane.setTopComponent(new JScrollPane(table));
        this.messageTextArea = new JTextArea();
        this.messageTextArea.setEditable(false);
        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuItem1 = new JMenuItem("Copy");
        menuItem1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(EnhancedDebugger.this.messageTextArea.getText()), null);
            }
        });
        menu.add(menuItem1);
        this.messageTextArea.addMouseListener(new PopupListener(menu));
        allPane.setBottomComponent(new JScrollPane(this.messageTextArea));
        allPane.setDividerLocation(150);
        this.tabbedPane.add("All Packets", allPane);
        this.tabbedPane.setToolTipTextAt(0, "Sent and received packets processed by Smack");
        final JTextArea sentText = new JTextArea();
        sentText.setWrapStyleWord(true);
        sentText.setLineWrap(true);
        sentText.setEditable(false);
        sentText.setForeground(new Color(112, 3, 3));
        this.tabbedPane.add("Raw Sent Packets", new JScrollPane(sentText));
        this.tabbedPane.setToolTipTextAt(1, "Raw text of the sent packets");
        menu = new JPopupMenu();
        menuItem1 = new JMenuItem("Copy");
        menuItem1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(sentText.getText()), null);
            }
        });
        JMenuItem menuItem2 = new JMenuItem("Clear");
        menuItem2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sentText.setText("");
            }
        });
        sentText.addMouseListener(new PopupListener(menu));
        menu.add(menuItem1);
        menu.add(menuItem2);
        final JTextArea receivedText = new JTextArea();
        receivedText.setWrapStyleWord(true);
        receivedText.setLineWrap(true);
        receivedText.setEditable(false);
        receivedText.setForeground(new Color(6, 76, 133));
        this.tabbedPane.add("Raw Received Packets", new JScrollPane(receivedText));
        this.tabbedPane.setToolTipTextAt(2, "Raw text of the received packets before Smack process them");
        menu = new JPopupMenu();
        menuItem1 = new JMenuItem("Copy");
        menuItem1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(receivedText.getText()), null);
            }
        });
        menuItem2 = new JMenuItem("Clear");
        menuItem2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                receivedText.setText("");
            }
        });
        receivedText.addMouseListener(new PopupListener(menu));
        menu.add(menuItem1);
        menu.add(menuItem2);
        ObservableReader debugReader = new ObservableReader(this.reader);
        this.readerListener = new ReaderListener() {
            public void read(final String str) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if (!EnhancedDebuggerWindow.PERSISTED_DEBUGGER || EnhancedDebuggerWindow.getInstance().isVisible()) {
                            int index = str.lastIndexOf(">");
                            if (index != -1) {
                                if (receivedText.getLineCount() >= EnhancedDebuggerWindow.MAX_TABLE_ROWS) {
                                    try {
                                        receivedText.replaceRange("", 0, receivedText.getLineEndOffset(0));
                                    } catch (BadLocationException e) {
                                        e.printStackTrace();
                                    }
                                }
                                receivedText.append(str.substring(0, index + 1));
                                receivedText.append(EnhancedDebugger.NEWLINE);
                                if (str.length() > index) {
                                    receivedText.append(str.substring(index + 1));
                                    return;
                                }
                                return;
                            }
                            receivedText.append(str);
                        }
                    }
                });
            }
        };
        debugReader.addReaderListener(this.readerListener);
        ObservableWriter debugWriter = new ObservableWriter(this.writer);
        this.writerListener = new WriterListener() {
            public void write(final String str) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if (!EnhancedDebuggerWindow.PERSISTED_DEBUGGER || EnhancedDebuggerWindow.getInstance().isVisible()) {
                            if (sentText.getLineCount() >= EnhancedDebuggerWindow.MAX_TABLE_ROWS) {
                                try {
                                    sentText.replaceRange("", 0, sentText.getLineEndOffset(0));
                                } catch (BadLocationException e) {
                                    e.printStackTrace();
                                }
                            }
                            sentText.append(str);
                            if (str.endsWith(">")) {
                                sentText.append(EnhancedDebugger.NEWLINE);
                            }
                        }
                    }
                });
            }
        };
        debugWriter.addWriterListener(this.writerListener);
        this.reader = debugReader;
        this.writer = debugWriter;
    }

    private void addAdhocPacketPanel() {
        final JTextArea adhocMessages = new JTextArea();
        adhocMessages.setEditable(true);
        adhocMessages.setForeground(new Color(1, 94, 35));
        this.tabbedPane.add("Ad-hoc message", new JScrollPane(adhocMessages));
        this.tabbedPane.setToolTipTextAt(3, "Panel that allows you to send adhoc packets");
        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Message");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adhocMessages.setText("<message to=\"\" id=\"" + StringUtils.randomString(5) + "-X\"><body></body></message>");
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("IQ Get");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adhocMessages.setText("<iq type=\"get\" to=\"\" id=\"" + StringUtils.randomString(5) + "-X\"><query xmlns=\"\"></query></iq>");
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("IQ Set");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adhocMessages.setText("<iq type=\"set\" to=\"\" id=\"" + StringUtils.randomString(5) + "-X\"><query xmlns=\"\"></query></iq>");
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Presence");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adhocMessages.setText("<presence to=\"\" id=\"" + StringUtils.randomString(5) + "-X\"/>");
            }
        });
        menu.add(menuItem);
        menu.addSeparator();
        menuItem = new JMenuItem("Send");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!"".equals(adhocMessages.getText())) {
                    EnhancedDebugger.this.connection.sendPacket(new AdHocPacket(adhocMessages.getText()));
                }
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Clear");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adhocMessages.setText(null);
            }
        });
        menu.add(menuItem);
        adhocMessages.addMouseListener(new PopupListener(menu));
    }

    private void addInformationPanel() {
        JPanel informationPanel = new JPanel();
        informationPanel.setLayout(new BorderLayout());
        JPanel connPanel = new JPanel();
        connPanel.setLayout(new GridBagLayout());
        connPanel.setBorder(BorderFactory.createTitledBorder("Connection information"));
        JLabel jLabel = new JLabel("Host: ");
        jLabel.setMinimumSize(new Dimension(150, 14));
        jLabel.setMaximumSize(new Dimension(150, 14));
        connPanel.add(jLabel, new GridBagConstraints(0, 0, 1, 1, 0.0d, 0.0d, 21, 0, new Insets(0, 0, 0, 0), 0, 0));
        JFormattedTextField jFormattedTextField = new JFormattedTextField(this.connection.getServiceName());
        jFormattedTextField.setMinimumSize(new Dimension(150, 20));
        jFormattedTextField.setMaximumSize(new Dimension(150, 20));
        jFormattedTextField.setEditable(false);
        jFormattedTextField.setBorder(null);
        connPanel.add(jFormattedTextField, new GridBagConstraints(1, 0, 1, 1, 1.0d, 0.0d, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
        jLabel = new JLabel("Port: ");
        jLabel.setMinimumSize(new Dimension(150, 14));
        jLabel.setMaximumSize(new Dimension(150, 14));
        connPanel.add(jLabel, new GridBagConstraints(0, 1, 1, 1, 0.0d, 0.0d, 21, 0, new Insets(0, 0, 0, 0), 0, 0));
        jFormattedTextField = new JFormattedTextField(Integer.valueOf(this.connection.getPort()));
        jFormattedTextField.setMinimumSize(new Dimension(150, 20));
        jFormattedTextField.setMaximumSize(new Dimension(150, 20));
        jFormattedTextField.setEditable(false);
        jFormattedTextField.setBorder(null);
        connPanel.add(jFormattedTextField, new GridBagConstraints(1, 1, 1, 1, 0.0d, 0.0d, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
        jLabel = new JLabel("User: ");
        jLabel.setMinimumSize(new Dimension(150, 14));
        jLabel.setMaximumSize(new Dimension(150, 14));
        connPanel.add(jLabel, new GridBagConstraints(0, 2, 1, 1, 0.0d, 0.0d, 21, 0, new Insets(0, 0, 0, 0), 0, 0));
        this.userField = new JFormattedTextField();
        this.userField.setMinimumSize(new Dimension(150, 20));
        this.userField.setMaximumSize(new Dimension(150, 20));
        this.userField.setEditable(false);
        this.userField.setBorder(null);
        connPanel.add(this.userField, new GridBagConstraints(1, 2, 1, 1, 0.0d, 0.0d, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
        jLabel = new JLabel("Creation time: ");
        jLabel.setMinimumSize(new Dimension(150, 14));
        jLabel.setMaximumSize(new Dimension(150, 14));
        connPanel.add(jLabel, new GridBagConstraints(0, 3, 1, 1, 0.0d, 0.0d, 21, 0, new Insets(0, 0, 0, 0), 0, 0));
        jFormattedTextField = new JFormattedTextField(new SimpleDateFormat("yyyy.MM.dd hh:mm:ss aaa"));
        jFormattedTextField.setMinimumSize(new Dimension(150, 20));
        jFormattedTextField.setMaximumSize(new Dimension(150, 20));
        jFormattedTextField.setValue(this.creationTime);
        jFormattedTextField.setEditable(false);
        jFormattedTextField.setBorder(null);
        connPanel.add(jFormattedTextField, new GridBagConstraints(1, 3, 1, 1, 0.0d, 0.0d, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
        jLabel = new JLabel("Status: ");
        jLabel.setMinimumSize(new Dimension(150, 14));
        jLabel.setMaximumSize(new Dimension(150, 14));
        connPanel.add(jLabel, new GridBagConstraints(0, 4, 1, 1, 0.0d, 0.0d, 21, 0, new Insets(0, 0, 0, 0), 0, 0));
        this.statusField = new JFormattedTextField();
        this.statusField.setMinimumSize(new Dimension(150, 20));
        this.statusField.setMaximumSize(new Dimension(150, 20));
        this.statusField.setValue("Active");
        this.statusField.setEditable(false);
        this.statusField.setBorder(null);
        connPanel.add(this.statusField, new GridBagConstraints(1, 4, 1, 1, 0.0d, 0.0d, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
        informationPanel.add(connPanel, "North");
        JPanel packetsPanel = new JPanel();
        packetsPanel.setLayout(new GridLayout(1, 1));
        packetsPanel.setBorder(BorderFactory.createTitledBorder("Transmitted Packets"));
        r3 = new Object[5][];
        r3[0] = new Object[]{"IQ", Integer.valueOf(0), Integer.valueOf(0)};
        r3[1] = new Object[]{"Message", Integer.valueOf(0), Integer.valueOf(0)};
        r3[2] = new Object[]{"Presence", Integer.valueOf(0), Integer.valueOf(0)};
        r3[3] = new Object[]{"Other", Integer.valueOf(0), Integer.valueOf(0)};
        r3[4] = new Object[]{"Total", Integer.valueOf(0), Integer.valueOf(0)};
        this.statisticsTable = new DefaultTableModel(r3, new Object[]{"Type", "Received", "Sent"}) {
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
            }
        };
        JTable jTable = new JTable(this.statisticsTable);
        jTable.setSelectionMode(0);
        packetsPanel.add(new JScrollPane(jTable));
        informationPanel.add(packetsPanel, "Center");
        this.tabbedPane.add("Information", new JScrollPane(informationPanel));
        this.tabbedPane.setToolTipTextAt(4, "Information and statistics about the debugged connection");
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

    public void userHasLogged(final String user) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                EnhancedDebugger.this.userField.setText(user);
                EnhancedDebuggerWindow.userHasLogged(this, user);
                EnhancedDebugger.this.connection.addConnectionListener(EnhancedDebugger.this.connListener);
            }
        });
    }

    public Reader getReader() {
        return this.reader;
    }

    public Writer getWriter() {
        return this.writer;
    }

    public PacketListener getReaderListener() {
        return this.packetReaderListener;
    }

    public PacketListener getWriterListener() {
        return this.packetWriterListener;
    }

    /* access modifiers changed from: private */
    public void updateStatistics() {
        this.statisticsTable.setValueAt(new Integer(this.receivedIQPackets), 0, 1);
        this.statisticsTable.setValueAt(new Integer(this.sentIQPackets), 0, 2);
        this.statisticsTable.setValueAt(new Integer(this.receivedMessagePackets), 1, 1);
        this.statisticsTable.setValueAt(new Integer(this.sentMessagePackets), 1, 2);
        this.statisticsTable.setValueAt(new Integer(this.receivedPresencePackets), 2, 1);
        this.statisticsTable.setValueAt(new Integer(this.sentPresencePackets), 2, 2);
        this.statisticsTable.setValueAt(new Integer(this.receivedOtherPackets), 3, 1);
        this.statisticsTable.setValueAt(new Integer(this.sentOtherPackets), 3, 2);
        this.statisticsTable.setValueAt(new Integer(this.receivedPackets), 4, 1);
        this.statisticsTable.setValueAt(new Integer(this.sentPackets), 4, 2);
    }

    /* access modifiers changed from: private */
    public void addReadPacketToTable(final SimpleDateFormat dateFormatter, final Packet packet) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Icon packetTypeIcon;
                String messageType;
                String from = packet.getFrom();
                String type = "";
                EnhancedDebugger.this.receivedPackets = EnhancedDebugger.this.receivedPackets + 1;
                if (packet instanceof IQ) {
                    packetTypeIcon = EnhancedDebugger.iqPacketIcon;
                    messageType = "IQ Received (class=" + packet.getClass().getName() + ")";
                    type = ((IQ) packet).getType().toString();
                    EnhancedDebugger.this.receivedIQPackets = EnhancedDebugger.this.receivedIQPackets + 1;
                } else if (packet instanceof Message) {
                    packetTypeIcon = EnhancedDebugger.messagePacketIcon;
                    messageType = "Message Received";
                    type = ((Message) packet).getType().toString();
                    EnhancedDebugger.this.receivedMessagePackets = EnhancedDebugger.this.receivedMessagePackets + 1;
                } else if (packet instanceof Presence) {
                    packetTypeIcon = EnhancedDebugger.presencePacketIcon;
                    messageType = "Presence Received";
                    type = ((Presence) packet).getType().toString();
                    EnhancedDebugger.this.receivedPresencePackets = EnhancedDebugger.this.receivedPresencePackets + 1;
                } else {
                    packetTypeIcon = EnhancedDebugger.unknownPacketTypeIcon;
                    messageType = packet.getClass().getName() + " Received";
                    EnhancedDebugger.this.receivedOtherPackets = EnhancedDebugger.this.receivedOtherPackets + 1;
                }
                if (EnhancedDebuggerWindow.MAX_TABLE_ROWS > 0 && EnhancedDebugger.this.messagesTable.getRowCount() >= EnhancedDebuggerWindow.MAX_TABLE_ROWS) {
                    EnhancedDebugger.this.messagesTable.removeRow(0);
                }
                EnhancedDebugger.this.messagesTable.addRow(new Object[]{EnhancedDebugger.this.formatXML(packet.toXML()), dateFormatter.format(new Date()), EnhancedDebugger.packetReceivedIcon, packetTypeIcon, messageType, packet.getPacketID(), type, "", from});
                EnhancedDebugger.this.updateStatistics();
            }
        });
    }

    /* access modifiers changed from: private */
    public void addSentPacketToTable(final SimpleDateFormat dateFormatter, final Packet packet) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Icon packetTypeIcon;
                String messageType;
                String to = packet.getTo();
                String type = "";
                EnhancedDebugger.this.sentPackets = EnhancedDebugger.this.sentPackets + 1;
                if (packet instanceof IQ) {
                    packetTypeIcon = EnhancedDebugger.iqPacketIcon;
                    messageType = "IQ Sent (class=" + packet.getClass().getName() + ")";
                    type = ((IQ) packet).getType().toString();
                    EnhancedDebugger.this.sentIQPackets = EnhancedDebugger.this.sentIQPackets + 1;
                } else if (packet instanceof Message) {
                    packetTypeIcon = EnhancedDebugger.messagePacketIcon;
                    messageType = "Message Sent";
                    type = ((Message) packet).getType().toString();
                    EnhancedDebugger.this.sentMessagePackets = EnhancedDebugger.this.sentMessagePackets + 1;
                } else if (packet instanceof Presence) {
                    packetTypeIcon = EnhancedDebugger.presencePacketIcon;
                    messageType = "Presence Sent";
                    type = ((Presence) packet).getType().toString();
                    EnhancedDebugger.this.sentPresencePackets = EnhancedDebugger.this.sentPresencePackets + 1;
                } else {
                    packetTypeIcon = EnhancedDebugger.unknownPacketTypeIcon;
                    messageType = packet.getClass().getName() + " Sent";
                    EnhancedDebugger.this.sentOtherPackets = EnhancedDebugger.this.sentOtherPackets + 1;
                }
                if (EnhancedDebuggerWindow.MAX_TABLE_ROWS > 0 && EnhancedDebugger.this.messagesTable.getRowCount() >= EnhancedDebuggerWindow.MAX_TABLE_ROWS) {
                    EnhancedDebugger.this.messagesTable.removeRow(0);
                }
                EnhancedDebugger.this.messagesTable.addRow(new Object[]{EnhancedDebugger.this.formatXML(packet.toXML()), dateFormatter.format(new Date()), EnhancedDebugger.packetSentIcon, packetTypeIcon, messageType, packet.getPacketID(), type, to, ""});
                EnhancedDebugger.this.updateStatistics();
            }
        });
    }

    /* access modifiers changed from: private */
    public String formatXML(String str) {
        Throwable x;
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            try {
                tFactory.setAttribute("indent-number", Integer.valueOf(2));
            } catch (IllegalArgumentException e) {
            }
            Transformer transformer = tFactory.newTransformer();
            transformer.setOutputProperty("omit-xml-declaration", "yes");
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            StreamSource source = new StreamSource(new StringReader(str));
            StringWriter sw = new StringWriter();
            transformer.transform(source, new StreamResult(sw));
            return sw.toString();
        } catch (TransformerConfigurationException tce) {
            System.out.println("\n** Transformer Factory error");
            System.out.println("   " + tce.getMessage());
            x = tce;
            if (tce.getException() != null) {
                x = tce.getException();
            }
            x.printStackTrace();
            return str;
        } catch (TransformerException te) {
            System.out.println("\n** Transformation error");
            System.out.println("   " + te.getMessage());
            x = te;
            if (te.getException() != null) {
                x = te.getException();
            }
            x.printStackTrace();
            return str;
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean isConnectionActive() {
        return this.connection.isConnected();
    }

    /* access modifiers changed from: 0000 */
    public void cancel() {
        this.connection.removeConnectionListener(this.connListener);
        this.connection.removePacketListener(this.packetReaderListener);
        this.connection.removePacketWriterListener(this.packetWriterListener);
        ((ObservableReader) this.reader).removeReaderListener(this.readerListener);
        ((ObservableWriter) this.writer).removeWriterListener(this.writerListener);
        this.messagesTable = null;
    }
}
