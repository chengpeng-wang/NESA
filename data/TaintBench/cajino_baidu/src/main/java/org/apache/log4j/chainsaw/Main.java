package org.apache.log4j.chainsaw;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Main extends JFrame {
    private static final int DEFAULT_PORT = 4445;
    private static final Logger LOG;
    public static final String PORT_PROP_NAME = "chainsaw.port";
    static Class class$org$apache$log4j$chainsaw$Main;

    static {
        Class class$;
        if (class$org$apache$log4j$chainsaw$Main == null) {
            class$ = class$("org.apache.log4j.chainsaw.Main");
            class$org$apache$log4j$chainsaw$Main = class$;
        } else {
            class$ = class$org$apache$log4j$chainsaw$Main;
        }
        LOG = Logger.getLogger(class$);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    private Main() {
        super("CHAINSAW - Log4J Log Viewer");
        MyTableModel model = new MyTableModel();
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu menu = new JMenu("File");
        menuBar.add(menu);
        try {
            LoadXMLAction lxa = new LoadXMLAction(this, model);
            JMenuItem loadMenuItem = new JMenuItem("Load file...");
            menu.add(loadMenuItem);
            loadMenuItem.addActionListener(lxa);
        } catch (NoClassDefFoundError e) {
            LOG.info("Missing classes for XML parser", e);
            JOptionPane.showMessageDialog(this, "XML parser not in classpath - unable to load XML events.", "CHAINSAW", 0);
        } catch (Exception e2) {
            LOG.info("Unable to create the action to load XML files", e2);
            JOptionPane.showMessageDialog(this, "Unable to create a XML parser - unable to load XML events.", "CHAINSAW", 0);
        }
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        menu.add(exitMenuItem);
        exitMenuItem.addActionListener(ExitAction.INSTANCE);
        getContentPane().add(new ControlPanel(model), "North");
        JTable table = new JTable(model);
        table.setSelectionMode(0);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Events: "));
        scrollPane.setPreferredSize(new Dimension(900, HttpStatus.SC_MULTIPLE_CHOICES));
        JPanel details = new DetailPanel(table, model);
        details.setPreferredSize(new Dimension(900, HttpStatus.SC_MULTIPLE_CHOICES));
        getContentPane().add(new JSplitPane(0, scrollPane, details), "Center");
        addWindowListener(new WindowAdapter(this) {
            private final Main this$0;

            {
                this.this$0 = r1;
            }

            public void windowClosing(WindowEvent aEvent) {
                ExitAction.INSTANCE.actionPerformed(null);
            }
        });
        pack();
        setVisible(true);
        setupReceiver(model);
    }

    private void setupReceiver(MyTableModel aModel) {
        int port = DEFAULT_PORT;
        String strRep = System.getProperty(PORT_PROP_NAME);
        if (strRep != null) {
            try {
                port = Integer.parseInt(strRep);
            } catch (NumberFormatException e) {
                LOG.fatal(new StringBuffer().append("Unable to parse chainsaw.port property with value ").append(strRep).append(".").toString());
                JOptionPane.showMessageDialog(this, new StringBuffer().append("Unable to parse port number from '").append(strRep).append("', quitting.").toString(), "CHAINSAW", 0);
                System.exit(1);
            }
        }
        try {
            new LoggingReceiver(aModel, port).start();
        } catch (IOException e2) {
            LOG.fatal("Unable to connect to socket server, quiting", e2);
            JOptionPane.showMessageDialog(this, new StringBuffer().append("Unable to create socket on port ").append(port).append(", quitting.").toString(), "CHAINSAW", 0);
            System.exit(1);
        }
    }

    private static void initLog4J() {
        Properties props = new Properties();
        props.setProperty("log4j.rootLogger", "DEBUG, A1");
        props.setProperty("log4j.appender.A1", "org.apache.log4j.ConsoleAppender");
        props.setProperty("log4j.appender.A1.layout", "org.apache.log4j.TTCCLayout");
        PropertyConfigurator.configure(props);
    }

    public static void main(String[] aArgs) {
        initLog4J();
        Main main = new Main();
    }
}
