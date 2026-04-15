package org.jivesoftware.smackx.debugger;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.provider.ProviderManager;

public class EnhancedDebuggerWindow {
    public static int MAX_TABLE_ROWS = 150;
    public static boolean PERSISTED_DEBUGGER = false;
    private static ImageIcon connectionActiveIcon;
    private static ImageIcon connectionClosedIcon;
    private static ImageIcon connectionClosedOnErrorIcon;
    private static ImageIcon connectionCreatedIcon;
    private static EnhancedDebuggerWindow instance;
    /* access modifiers changed from: private */
    public List<EnhancedDebugger> debuggers;
    /* access modifiers changed from: private */
    public JFrame frame;
    /* access modifiers changed from: private */
    public JTabbedPane tabbedPane;

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

    private EnhancedDebuggerWindow() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("images/trafficlight_off.png");
        if (url != null) {
            connectionCreatedIcon = new ImageIcon(url);
        }
        url = Thread.currentThread().getContextClassLoader().getResource("images/trafficlight_green.png");
        if (url != null) {
            connectionActiveIcon = new ImageIcon(url);
        }
        url = Thread.currentThread().getContextClassLoader().getResource("images/trafficlight_red.png");
        if (url != null) {
            connectionClosedIcon = new ImageIcon(url);
        }
        url = Thread.currentThread().getContextClassLoader().getResource("images/warning.png");
        if (url != null) {
            connectionClosedOnErrorIcon = new ImageIcon(url);
        }
        this.frame = null;
        this.tabbedPane = null;
        this.debuggers = new ArrayList();
    }

    public static EnhancedDebuggerWindow getInstance() {
        if (instance == null) {
            instance = new EnhancedDebuggerWindow();
        }
        return instance;
    }

    static synchronized void addDebugger(EnhancedDebugger debugger) {
        synchronized (EnhancedDebuggerWindow.class) {
            getInstance().showNewDebugger(debugger);
        }
    }

    private void showNewDebugger(EnhancedDebugger debugger) {
        if (this.frame == null) {
            createDebug();
        }
        debugger.tabbedPane.setName("Connection_" + this.tabbedPane.getComponentCount());
        this.tabbedPane.add(debugger.tabbedPane, this.tabbedPane.getComponentCount() - 1);
        this.tabbedPane.setIconAt(this.tabbedPane.indexOfComponent(debugger.tabbedPane), connectionCreatedIcon);
        this.frame.setTitle("Smack Debug Window -- Total connections: " + (this.tabbedPane.getComponentCount() - 1));
        this.debuggers.add(debugger);
    }

    static synchronized void userHasLogged(EnhancedDebugger debugger, String user) {
        synchronized (EnhancedDebuggerWindow.class) {
            int index = getInstance().tabbedPane.indexOfComponent(debugger.tabbedPane);
            getInstance().tabbedPane.setTitleAt(index, user);
            getInstance().tabbedPane.setIconAt(index, connectionActiveIcon);
        }
    }

    static synchronized void connectionClosed(EnhancedDebugger debugger) {
        synchronized (EnhancedDebuggerWindow.class) {
            getInstance().tabbedPane.setIconAt(getInstance().tabbedPane.indexOfComponent(debugger.tabbedPane), connectionClosedIcon);
        }
    }

    static synchronized void connectionClosedOnError(EnhancedDebugger debugger, Exception e) {
        synchronized (EnhancedDebuggerWindow.class) {
            int index = getInstance().tabbedPane.indexOfComponent(debugger.tabbedPane);
            getInstance().tabbedPane.setToolTipTextAt(index, "Connection closed due to the exception: " + e.getMessage());
            getInstance().tabbedPane.setIconAt(index, connectionClosedOnErrorIcon);
        }
    }

    static synchronized void connectionEstablished(EnhancedDebugger debugger) {
        synchronized (EnhancedDebuggerWindow.class) {
            getInstance().tabbedPane.setIconAt(getInstance().tabbedPane.indexOfComponent(debugger.tabbedPane), connectionActiveIcon);
        }
    }

    private void createDebug() {
        this.frame = new JFrame("Smack Debug Window");
        if (!PERSISTED_DEBUGGER) {
            this.frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent evt) {
                    EnhancedDebuggerWindow.this.rootWindowClosing(evt);
                }
            });
        }
        this.tabbedPane = new JTabbedPane();
        JPanel informationPanel = new JPanel();
        informationPanel.setLayout(new BoxLayout(informationPanel, 1));
        JPanel versionPanel = new JPanel();
        versionPanel.setLayout(new BoxLayout(versionPanel, 0));
        versionPanel.setMaximumSize(new Dimension(2000, 31));
        versionPanel.add(new JLabel(" Smack version: "));
        JFormattedTextField field = new JFormattedTextField(SmackConfiguration.getVersion());
        field.setEditable(false);
        field.setBorder(null);
        versionPanel.add(field);
        informationPanel.add(versionPanel);
        JPanel iqProvidersPanel = new JPanel();
        iqProvidersPanel.setLayout(new GridLayout(1, 1));
        iqProvidersPanel.setBorder(BorderFactory.createTitledBorder("Installed IQ Providers"));
        Vector<String> providers = new Vector();
        for (Object provider : ProviderManager.getInstance().getIQProviders()) {
            if (provider.getClass() == Class.class) {
                providers.add(((Class) provider).getName());
            } else {
                providers.add(provider.getClass().getName());
            }
        }
        Collections.sort(providers);
        iqProvidersPanel.add(new JScrollPane(new JList(providers)));
        informationPanel.add(iqProvidersPanel);
        JPanel extensionProvidersPanel = new JPanel();
        extensionProvidersPanel.setLayout(new GridLayout(1, 1));
        extensionProvidersPanel.setBorder(BorderFactory.createTitledBorder("Installed Extension Providers"));
        providers = new Vector();
        for (Object provider2 : ProviderManager.getInstance().getExtensionProviders()) {
            if (provider2.getClass() == Class.class) {
                providers.add(((Class) provider2).getName());
            } else {
                providers.add(provider2.getClass().getName());
            }
        }
        Collections.sort(providers);
        extensionProvidersPanel.add(new JScrollPane(new JList(providers)));
        informationPanel.add(extensionProvidersPanel);
        this.tabbedPane.add("Smack Info", informationPanel);
        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Close");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (EnhancedDebuggerWindow.this.tabbedPane.getSelectedIndex() < EnhancedDebuggerWindow.this.tabbedPane.getComponentCount() - 1) {
                    EnhancedDebugger debugger = (EnhancedDebugger) EnhancedDebuggerWindow.this.debuggers.get(EnhancedDebuggerWindow.this.tabbedPane.getSelectedIndex());
                    debugger.cancel();
                    EnhancedDebuggerWindow.this.tabbedPane.remove(debugger.tabbedPane);
                    EnhancedDebuggerWindow.this.debuggers.remove(debugger);
                    EnhancedDebuggerWindow.this.frame.setTitle("Smack Debug Window -- Total connections: " + (EnhancedDebuggerWindow.this.tabbedPane.getComponentCount() - 1));
                }
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Close All Not Active");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EnhancedDebugger debugger;
                ArrayList<EnhancedDebugger> debuggersToRemove = new ArrayList();
                for (int index = 0; index < EnhancedDebuggerWindow.this.tabbedPane.getComponentCount() - 1; index++) {
                    debugger = (EnhancedDebugger) EnhancedDebuggerWindow.this.debuggers.get(index);
                    if (!debugger.isConnectionActive()) {
                        debugger.cancel();
                        debuggersToRemove.add(debugger);
                    }
                }
                Iterator i$ = debuggersToRemove.iterator();
                while (i$.hasNext()) {
                    debugger = (EnhancedDebugger) i$.next();
                    EnhancedDebuggerWindow.this.tabbedPane.remove(debugger.tabbedPane);
                    EnhancedDebuggerWindow.this.debuggers.remove(debugger);
                }
                EnhancedDebuggerWindow.this.frame.setTitle("Smack Debug Window -- Total connections: " + (EnhancedDebuggerWindow.this.tabbedPane.getComponentCount() - 1));
            }
        });
        menu.add(menuItem);
        this.tabbedPane.addMouseListener(new PopupListener(menu));
        this.frame.getContentPane().add(this.tabbedPane);
        this.frame.setSize(650, 400);
        if (!PERSISTED_DEBUGGER) {
            this.frame.setVisible(true);
        }
    }

    public void rootWindowClosing(WindowEvent evt) {
        for (EnhancedDebugger debugger : this.debuggers) {
            debugger.cancel();
        }
        this.debuggers.removeAll(this.debuggers);
        instance = null;
    }

    public void setVisible(boolean visible) {
        if (this.frame != null) {
            this.frame.setVisible(visible);
        }
    }

    public boolean isVisible() {
        return this.frame != null && this.frame.isVisible();
    }
}
