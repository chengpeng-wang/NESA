package org.apache.log4j.lf5.viewer;

import android.support.v4.media.TransportMediator;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.lf5.LogLevel;
import org.apache.log4j.lf5.LogRecord;
import org.apache.log4j.lf5.LogRecordFilter;
import org.apache.log4j.lf5.util.DateFormatManager;
import org.apache.log4j.lf5.util.LogFileParser;
import org.apache.log4j.lf5.viewer.categoryexplorer.CategoryExplorerTree;
import org.apache.log4j.lf5.viewer.categoryexplorer.CategoryPath;
import org.apache.log4j.lf5.viewer.configure.ConfigurationManager;
import org.apache.log4j.lf5.viewer.configure.MRUFileManager;

public class LogBrokerMonitor {
    public static final String DETAILED_VIEW = "Detailed";
    protected String _NDCTextFilter = "";
    protected boolean _callSystemExitOnClose = false;
    protected CategoryExplorerTree _categoryExplorerTree;
    protected List _columns = null;
    protected ConfigurationManager _configurationManager = null;
    protected String _currentView = DETAILED_VIEW;
    protected List _displayedLogBrokerProperties = new Vector();
    protected File _fileLocation = null;
    protected String _fontName = "Dialog";
    protected int _fontSize = 10;
    protected JComboBox _fontSizeCombo;
    protected boolean _isDisposed = false;
    protected Dimension _lastTableViewportSize;
    protected LogLevel _leastSevereDisplayedLogLevel = LogLevel.DEBUG;
    protected List _levels = null;
    protected boolean _loadSystemFonts = false;
    protected Object _lock = new Object();
    protected Map _logLevelMenuItems = new HashMap();
    protected JFrame _logMonitorFrame;
    protected int _logMonitorFrameHeight = HttpStatus.SC_INTERNAL_SERVER_ERROR;
    protected int _logMonitorFrameWidth = 550;
    protected Map _logTableColumnMenuItems = new HashMap();
    protected JScrollPane _logTableScrollPane;
    protected MRUFileManager _mruFileManager = null;
    protected String _searchText;
    protected JLabel _statusLabel;
    protected LogTable _table;
    protected boolean _trackTableScrollPane = true;

    /* renamed from: org.apache.log4j.lf5.viewer.LogBrokerMonitor$11 */
    class AnonymousClass11 implements ActionListener {
        private final LogBrokerMonitor this$0;
        private final LogLevel val$logLevel;
        private final JMenuItem val$result;

        AnonymousClass11(LogBrokerMonitor logBrokerMonitor, JMenuItem jMenuItem, LogLevel logLevel) {
            this.this$0 = logBrokerMonitor;
            this.val$result = jMenuItem;
            this.val$logLevel = logLevel;
        }

        public void actionPerformed(ActionEvent e) {
            this.this$0.showLogLevelColorChangeDialog(this.val$result, this.val$logLevel);
        }
    }

    /* renamed from: org.apache.log4j.lf5.viewer.LogBrokerMonitor$1 */
    class AnonymousClass1 implements Runnable {
        private final LogBrokerMonitor this$0;
        private final int val$delay;

        AnonymousClass1(LogBrokerMonitor logBrokerMonitor, int i) {
            this.this$0 = logBrokerMonitor;
            this.val$delay = i;
        }

        public void run() {
            Thread.yield();
            this.this$0.pause(this.val$delay);
            this.this$0._logMonitorFrame.setVisible(true);
        }
    }

    /* renamed from: org.apache.log4j.lf5.viewer.LogBrokerMonitor$2 */
    class AnonymousClass2 implements Runnable {
        private final LogBrokerMonitor this$0;
        private final LogRecord val$lr;

        AnonymousClass2(LogBrokerMonitor logBrokerMonitor, LogRecord logRecord) {
            this.this$0 = logBrokerMonitor;
            this.val$lr = logRecord;
        }

        public void run() {
            this.this$0._categoryExplorerTree.getExplorerModel().addLogRecord(this.val$lr);
            this.this$0._table.getFilteredLogTableModel().addLogRecord(this.val$lr);
            this.this$0.updateStatusLabel();
        }
    }

    /* renamed from: org.apache.log4j.lf5.viewer.LogBrokerMonitor$6 */
    class AnonymousClass6 {
        private final LogBrokerMonitor this$0;
        private final FilteredLogTableModel val$model;

        AnonymousClass6(LogBrokerMonitor logBrokerMonitor, FilteredLogTableModel filteredLogTableModel) {
            this.this$0 = logBrokerMonitor;
            this.val$model = filteredLogTableModel;
        }

        public String toString() {
            return new StringBuffer().append("Maximum number of displayed LogRecords: ").append(this.val$model._maxNumberOfLogRecords).toString();
        }
    }

    class LogBrokerMonitorWindowAdaptor extends WindowAdapter {
        protected LogBrokerMonitor _monitor;
        private final LogBrokerMonitor this$0;

        public LogBrokerMonitorWindowAdaptor(LogBrokerMonitor logBrokerMonitor, LogBrokerMonitor monitor) {
            this.this$0 = logBrokerMonitor;
            this._monitor = monitor;
        }

        public void windowClosing(WindowEvent ev) {
            this._monitor.requestClose();
        }
    }

    public LogBrokerMonitor(List logLevels) {
        this._levels = logLevels;
        this._columns = LogTableColumn.getLogTableColumns();
        String callSystemExitOnClose = System.getProperty("monitor.exit");
        if (callSystemExitOnClose == null) {
            callSystemExitOnClose = "false";
        }
        if (callSystemExitOnClose.trim().toLowerCase().equals("true")) {
            this._callSystemExitOnClose = true;
        }
        initComponents();
        this._logMonitorFrame.addWindowListener(new LogBrokerMonitorWindowAdaptor(this, this));
    }

    public void show(int delay) {
        if (!this._logMonitorFrame.isVisible()) {
            SwingUtilities.invokeLater(new AnonymousClass1(this, delay));
        }
    }

    public void show() {
        show(0);
    }

    public void dispose() {
        this._logMonitorFrame.dispose();
        this._isDisposed = true;
        if (this._callSystemExitOnClose) {
            System.exit(0);
        }
    }

    public void hide() {
        this._logMonitorFrame.setVisible(false);
    }

    public DateFormatManager getDateFormatManager() {
        return this._table.getDateFormatManager();
    }

    public void setDateFormatManager(DateFormatManager dfm) {
        this._table.setDateFormatManager(dfm);
    }

    public boolean getCallSystemExitOnClose() {
        return this._callSystemExitOnClose;
    }

    public void setCallSystemExitOnClose(boolean callSystemExitOnClose) {
        this._callSystemExitOnClose = callSystemExitOnClose;
    }

    public void addMessage(LogRecord lr) {
        if (!this._isDisposed) {
            SwingUtilities.invokeLater(new AnonymousClass2(this, lr));
        }
    }

    public void setMaxNumberOfLogRecords(int maxNumberOfLogRecords) {
        this._table.getFilteredLogTableModel().setMaxNumberOfLogRecords(maxNumberOfLogRecords);
    }

    public JFrame getBaseFrame() {
        return this._logMonitorFrame;
    }

    public void setTitle(String title) {
        this._logMonitorFrame.setTitle(new StringBuffer().append(title).append(" - LogFactor5").toString());
    }

    public void setFrameSize(int width, int height) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        if (width > 0 && width < screen.width) {
            this._logMonitorFrameWidth = width;
        }
        if (height > 0 && height < screen.height) {
            this._logMonitorFrameHeight = height;
        }
        updateFrameSize();
    }

    public void setFontSize(int fontSize) {
        changeFontSizeCombo(this._fontSizeCombo, fontSize);
    }

    public void addDisplayedProperty(Object messageLine) {
        this._displayedLogBrokerProperties.add(messageLine);
    }

    public Map getLogLevelMenuItems() {
        return this._logLevelMenuItems;
    }

    public Map getLogTableColumnMenuItems() {
        return this._logTableColumnMenuItems;
    }

    public JCheckBoxMenuItem getTableColumnMenuItem(LogTableColumn column) {
        return getLogTableColumnMenuItem(column);
    }

    public CategoryExplorerTree getCategoryExplorerTree() {
        return this._categoryExplorerTree;
    }

    public String getNDCTextFilter() {
        return this._NDCTextFilter;
    }

    public void setNDCLogRecordFilter(String textFilter) {
        this._table.getFilteredLogTableModel().setLogRecordFilter(createNDCLogRecordFilter(textFilter));
    }

    /* access modifiers changed from: protected */
    public void setSearchText(String text) {
        this._searchText = text;
    }

    /* access modifiers changed from: protected */
    public void setNDCTextFilter(String text) {
        if (text == null) {
            this._NDCTextFilter = "";
        } else {
            this._NDCTextFilter = text;
        }
    }

    /* access modifiers changed from: protected */
    public void sortByNDC() {
        String text = this._NDCTextFilter;
        if (text != null && text.length() != 0) {
            this._table.getFilteredLogTableModel().setLogRecordFilter(createNDCLogRecordFilter(text));
        }
    }

    /* access modifiers changed from: protected */
    public void findSearchText() {
        String text = this._searchText;
        if (text != null && text.length() != 0) {
            selectRow(findRecord(getFirstSelectedRow(), text, this._table.getFilteredLogTableModel().getFilteredRecords()));
        }
    }

    /* access modifiers changed from: protected */
    public int getFirstSelectedRow() {
        return this._table.getSelectionModel().getMinSelectionIndex();
    }

    /* access modifiers changed from: protected */
    public void selectRow(int foundRow) {
        if (foundRow == -1) {
            JOptionPane.showMessageDialog(this._logMonitorFrame, new StringBuffer().append(this._searchText).append(" not found.").toString(), "Text not found", 1);
            return;
        }
        LF5SwingUtils.selectRow(foundRow, this._table, this._logTableScrollPane);
    }

    /* access modifiers changed from: protected */
    public int findRecord(int startRow, String searchText, List records) {
        int i;
        if (startRow < 0) {
            startRow = 0;
        } else {
            startRow++;
        }
        int len = records.size();
        for (i = startRow; i < len; i++) {
            if (matches((LogRecord) records.get(i), searchText)) {
                return i;
            }
        }
        len = startRow;
        for (i = 0; i < len; i++) {
            if (matches((LogRecord) records.get(i), searchText)) {
                return i;
            }
        }
        return -1;
    }

    /* access modifiers changed from: protected */
    public boolean matches(LogRecord record, String text) {
        String message = record.getMessage();
        String NDC = record.getNDC();
        if ((message == null && NDC == null) || text == null) {
            return false;
        }
        if (message.toLowerCase().indexOf(text.toLowerCase()) == -1 && NDC.toLowerCase().indexOf(text.toLowerCase()) == -1) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void refresh(JTextArea textArea) {
        String text = textArea.getText();
        textArea.setText("");
        textArea.setText(text);
    }

    /* access modifiers changed from: protected */
    public void refreshDetailTextArea() {
        refresh(this._table._detailTextArea);
    }

    /* access modifiers changed from: protected */
    public void clearDetailTextArea() {
        this._table._detailTextArea.setText("");
    }

    /* access modifiers changed from: protected */
    public int changeFontSizeCombo(JComboBox box, int requestedSize) {
        int len = box.getItemCount();
        Object selectedObject = box.getItemAt(0);
        int selectedValue = Integer.parseInt(String.valueOf(selectedObject));
        for (int i = 0; i < len; i++) {
            Object currentObject = box.getItemAt(i);
            int currentValue = Integer.parseInt(String.valueOf(currentObject));
            if (selectedValue < currentValue && currentValue <= requestedSize) {
                selectedValue = currentValue;
                selectedObject = currentObject;
            }
        }
        box.setSelectedItem(selectedObject);
        return selectedValue;
    }

    /* access modifiers changed from: protected */
    public void setFontSizeSilently(int fontSize) {
        this._fontSize = fontSize;
        setFontSize(this._table._detailTextArea, fontSize);
        selectRow(0);
        setFontSize(this._table, fontSize);
    }

    /* access modifiers changed from: protected */
    public void setFontSize(Component component, int fontSize) {
        Font oldFont = component.getFont();
        component.setFont(new Font(oldFont.getFontName(), oldFont.getStyle(), fontSize));
    }

    /* access modifiers changed from: protected */
    public void updateFrameSize() {
        this._logMonitorFrame.setSize(this._logMonitorFrameWidth, this._logMonitorFrameHeight);
        centerFrame(this._logMonitorFrame);
    }

    /* access modifiers changed from: protected */
    public void pause(int millis) {
        try {
            Thread.sleep((long) millis);
        } catch (InterruptedException e) {
        }
    }

    /* access modifiers changed from: protected */
    public void initComponents() {
        this._logMonitorFrame = new JFrame("LogFactor5");
        this._logMonitorFrame.setDefaultCloseOperation(0);
        URL lf5IconURL = getClass().getResource("/org/apache/log4j/lf5/viewer/images/lf5_small_icon.gif");
        if (lf5IconURL != null) {
            this._logMonitorFrame.setIconImage(new ImageIcon(lf5IconURL).getImage());
        }
        updateFrameSize();
        JTextArea detailTA = createDetailTextArea();
        JScrollPane detailTAScrollPane = new JScrollPane(detailTA);
        this._table = new LogTable(detailTA);
        setView(this._currentView, this._table);
        this._table.setFont(new Font(this._fontName, 0, this._fontSize));
        this._logTableScrollPane = new JScrollPane(this._table);
        if (this._trackTableScrollPane) {
            this._logTableScrollPane.getVerticalScrollBar().addAdjustmentListener(new TrackingAdjustmentListener());
        }
        JSplitPane tableViewerSplitPane = new JSplitPane();
        tableViewerSplitPane.setOneTouchExpandable(true);
        tableViewerSplitPane.setOrientation(0);
        tableViewerSplitPane.setLeftComponent(this._logTableScrollPane);
        tableViewerSplitPane.setRightComponent(detailTAScrollPane);
        tableViewerSplitPane.setDividerLocation(350);
        this._categoryExplorerTree = new CategoryExplorerTree();
        this._table.getFilteredLogTableModel().setLogRecordFilter(createLogRecordFilter());
        JScrollPane categoryExplorerTreeScrollPane = new JScrollPane(this._categoryExplorerTree);
        categoryExplorerTreeScrollPane.setPreferredSize(new Dimension(TransportMediator.KEYCODE_MEDIA_RECORD, HttpStatus.SC_BAD_REQUEST));
        this._mruFileManager = new MRUFileManager();
        JSplitPane splitPane = new JSplitPane();
        splitPane.setOneTouchExpandable(true);
        splitPane.setRightComponent(tableViewerSplitPane);
        splitPane.setLeftComponent(categoryExplorerTreeScrollPane);
        splitPane.setDividerLocation(TransportMediator.KEYCODE_MEDIA_RECORD);
        this._logMonitorFrame.getRootPane().setJMenuBar(createMenuBar());
        this._logMonitorFrame.getContentPane().add(splitPane, "Center");
        this._logMonitorFrame.getContentPane().add(createToolBar(), "North");
        this._logMonitorFrame.getContentPane().add(createStatusArea(), "South");
        makeLogTableListenToCategoryExplorer();
        addTableModelProperties();
        this._configurationManager = new ConfigurationManager(this, this._table);
    }

    /* access modifiers changed from: protected */
    public LogRecordFilter createLogRecordFilter() {
        return new LogRecordFilter(this) {
            private final LogBrokerMonitor this$0;

            {
                this.this$0 = r1;
            }

            public boolean passes(LogRecord record) {
                return this.this$0.getMenuItem(record.getLevel()).isSelected() && this.this$0._categoryExplorerTree.getExplorerModel().isCategoryPathActive(new CategoryPath(record.getCategory()));
            }
        };
    }

    /* access modifiers changed from: protected */
    public LogRecordFilter createNDCLogRecordFilter(String text) {
        this._NDCTextFilter = text;
        return new LogRecordFilter(this) {
            private final LogBrokerMonitor this$0;

            {
                this.this$0 = r1;
            }

            public boolean passes(LogRecord record) {
                String NDC = record.getNDC();
                CategoryPath path = new CategoryPath(record.getCategory());
                if (NDC == null || this.this$0._NDCTextFilter == null || NDC.toLowerCase().indexOf(this.this$0._NDCTextFilter.toLowerCase()) == -1 || !this.this$0.getMenuItem(record.getLevel()).isSelected() || !this.this$0._categoryExplorerTree.getExplorerModel().isCategoryPathActive(path)) {
                    return false;
                }
                return true;
            }
        };
    }

    /* access modifiers changed from: protected */
    public void updateStatusLabel() {
        this._statusLabel.setText(getRecordsDisplayedMessage());
    }

    /* access modifiers changed from: protected */
    public String getRecordsDisplayedMessage() {
        FilteredLogTableModel model = this._table.getFilteredLogTableModel();
        return getStatusText(model.getRowCount(), model.getTotalRowCount());
    }

    /* access modifiers changed from: protected */
    public void addTableModelProperties() {
        FilteredLogTableModel model = this._table.getFilteredLogTableModel();
        addDisplayedProperty(new Object(this) {
            private final LogBrokerMonitor this$0;

            {
                this.this$0 = r1;
            }

            public String toString() {
                return this.this$0.getRecordsDisplayedMessage();
            }
        });
        addDisplayedProperty(new AnonymousClass6(this, model));
    }

    /* access modifiers changed from: protected */
    public String getStatusText(int displayedRows, int totalRows) {
        StringBuffer result = new StringBuffer();
        result.append("Displaying: ");
        result.append(displayedRows);
        result.append(" records out of a total of: ");
        result.append(totalRows);
        result.append(" records.");
        return result.toString();
    }

    /* access modifiers changed from: protected */
    public void makeLogTableListenToCategoryExplorer() {
        this._categoryExplorerTree.getExplorerModel().addActionListener(new ActionListener(this) {
            private final LogBrokerMonitor this$0;

            {
                this.this$0 = r1;
            }

            public void actionPerformed(ActionEvent e) {
                this.this$0._table.getFilteredLogTableModel().refresh();
                this.this$0.updateStatusLabel();
            }
        });
    }

    /* access modifiers changed from: protected */
    public JPanel createStatusArea() {
        JPanel statusArea = new JPanel();
        JLabel status = new JLabel("No log records to display.");
        this._statusLabel = status;
        status.setHorizontalAlignment(2);
        statusArea.setBorder(BorderFactory.createEtchedBorder());
        statusArea.setLayout(new FlowLayout(0, 0, 0));
        statusArea.add(status);
        return statusArea;
    }

    /* access modifiers changed from: protected */
    public JTextArea createDetailTextArea() {
        JTextArea detailTA = new JTextArea();
        detailTA.setFont(new Font("Monospaced", 0, 14));
        detailTA.setTabSize(3);
        detailTA.setLineWrap(true);
        detailTA.setWrapStyleWord(false);
        return detailTA;
    }

    /* access modifiers changed from: protected */
    public JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createEditMenu());
        menuBar.add(createLogLevelMenu());
        menuBar.add(createViewMenu());
        menuBar.add(createConfigureMenu());
        menuBar.add(createHelpMenu());
        return menuBar;
    }

    /* access modifiers changed from: protected */
    public JMenu createLogLevelMenu() {
        JMenu result = new JMenu("Log Level");
        result.setMnemonic('l');
        Iterator levels = getLogLevels();
        while (levels.hasNext()) {
            result.add(getMenuItem((LogLevel) levels.next()));
        }
        result.addSeparator();
        result.add(createAllLogLevelsMenuItem());
        result.add(createNoLogLevelsMenuItem());
        result.addSeparator();
        result.add(createLogLevelColorMenu());
        result.add(createResetLogLevelColorMenuItem());
        return result;
    }

    /* access modifiers changed from: protected */
    public JMenuItem createAllLogLevelsMenuItem() {
        JMenuItem result = new JMenuItem("Show all LogLevels");
        result.setMnemonic('s');
        result.addActionListener(new ActionListener(this) {
            private final LogBrokerMonitor this$0;

            {
                this.this$0 = r1;
            }

            public void actionPerformed(ActionEvent e) {
                this.this$0.selectAllLogLevels(true);
                this.this$0._table.getFilteredLogTableModel().refresh();
                this.this$0.updateStatusLabel();
            }
        });
        return result;
    }

    /* access modifiers changed from: protected */
    public JMenuItem createNoLogLevelsMenuItem() {
        JMenuItem result = new JMenuItem("Hide all LogLevels");
        result.setMnemonic('h');
        result.addActionListener(new ActionListener(this) {
            private final LogBrokerMonitor this$0;

            {
                this.this$0 = r1;
            }

            public void actionPerformed(ActionEvent e) {
                this.this$0.selectAllLogLevels(false);
                this.this$0._table.getFilteredLogTableModel().refresh();
                this.this$0.updateStatusLabel();
            }
        });
        return result;
    }

    /* access modifiers changed from: protected */
    public JMenu createLogLevelColorMenu() {
        JMenu colorMenu = new JMenu("Configure LogLevel Colors");
        colorMenu.setMnemonic('c');
        Iterator levels = getLogLevels();
        while (levels.hasNext()) {
            colorMenu.add(createSubMenuItem((LogLevel) levels.next()));
        }
        return colorMenu;
    }

    /* access modifiers changed from: protected */
    public JMenuItem createResetLogLevelColorMenuItem() {
        JMenuItem result = new JMenuItem("Reset LogLevel Colors");
        result.setMnemonic('r');
        result.addActionListener(new ActionListener(this) {
            private final LogBrokerMonitor this$0;

            {
                this.this$0 = r1;
            }

            public void actionPerformed(ActionEvent e) {
                LogLevel.resetLogLevelColorMap();
                this.this$0._table.getFilteredLogTableModel().refresh();
            }
        });
        return result;
    }

    /* access modifiers changed from: protected */
    public void selectAllLogLevels(boolean selected) {
        Iterator levels = getLogLevels();
        while (levels.hasNext()) {
            getMenuItem((LogLevel) levels.next()).setSelected(selected);
        }
    }

    /* access modifiers changed from: protected */
    public JCheckBoxMenuItem getMenuItem(LogLevel level) {
        JCheckBoxMenuItem result = (JCheckBoxMenuItem) this._logLevelMenuItems.get(level);
        if (result != null) {
            return result;
        }
        result = createMenuItem(level);
        this._logLevelMenuItems.put(level, result);
        return result;
    }

    /* access modifiers changed from: protected */
    public JMenuItem createSubMenuItem(LogLevel level) {
        JMenuItem result = new JMenuItem(level.toString());
        LogLevel logLevel = level;
        result.setMnemonic(level.toString().charAt(0));
        result.addActionListener(new AnonymousClass11(this, result, logLevel));
        return result;
    }

    /* access modifiers changed from: protected */
    public void showLogLevelColorChangeDialog(JMenuItem result, LogLevel level) {
        JMenuItem menuItem = result;
        Color newColor = JColorChooser.showDialog(this._logMonitorFrame, "Choose LogLevel Color", result.getForeground());
        if (newColor != null) {
            level.setLogLevelColorMap(level, newColor);
            this._table.getFilteredLogTableModel().refresh();
        }
    }

    /* access modifiers changed from: protected */
    public JCheckBoxMenuItem createMenuItem(LogLevel level) {
        JCheckBoxMenuItem result = new JCheckBoxMenuItem(level.toString());
        result.setSelected(true);
        result.setMnemonic(level.toString().charAt(0));
        result.addActionListener(new ActionListener(this) {
            private final LogBrokerMonitor this$0;

            {
                this.this$0 = r1;
            }

            public void actionPerformed(ActionEvent e) {
                this.this$0._table.getFilteredLogTableModel().refresh();
                this.this$0.updateStatusLabel();
            }
        });
        return result;
    }

    /* access modifiers changed from: protected */
    public JMenu createViewMenu() {
        JMenu result = new JMenu("View");
        result.setMnemonic('v');
        Iterator columns = getLogTableColumns();
        while (columns.hasNext()) {
            result.add(getLogTableColumnMenuItem((LogTableColumn) columns.next()));
        }
        result.addSeparator();
        result.add(createAllLogTableColumnsMenuItem());
        result.add(createNoLogTableColumnsMenuItem());
        return result;
    }

    /* access modifiers changed from: protected */
    public JCheckBoxMenuItem getLogTableColumnMenuItem(LogTableColumn column) {
        JCheckBoxMenuItem result = (JCheckBoxMenuItem) this._logTableColumnMenuItems.get(column);
        if (result != null) {
            return result;
        }
        result = createLogTableColumnMenuItem(column);
        this._logTableColumnMenuItems.put(column, result);
        return result;
    }

    /* access modifiers changed from: protected */
    public JCheckBoxMenuItem createLogTableColumnMenuItem(LogTableColumn column) {
        JCheckBoxMenuItem result = new JCheckBoxMenuItem(column.toString());
        result.setSelected(true);
        result.setMnemonic(column.toString().charAt(0));
        result.addActionListener(new ActionListener(this) {
            private final LogBrokerMonitor this$0;

            {
                this.this$0 = r1;
            }

            public void actionPerformed(ActionEvent e) {
                this.this$0._table.setView(this.this$0.updateView());
            }
        });
        return result;
    }

    /* access modifiers changed from: protected */
    public List updateView() {
        ArrayList updatedList = new ArrayList();
        for (LogTableColumn column : this._columns) {
            if (getLogTableColumnMenuItem(column).isSelected()) {
                updatedList.add(column);
            }
        }
        return updatedList;
    }

    /* access modifiers changed from: protected */
    public JMenuItem createAllLogTableColumnsMenuItem() {
        JMenuItem result = new JMenuItem("Show all Columns");
        result.setMnemonic('s');
        result.addActionListener(new ActionListener(this) {
            private final LogBrokerMonitor this$0;

            {
                this.this$0 = r1;
            }

            public void actionPerformed(ActionEvent e) {
                this.this$0.selectAllLogTableColumns(true);
                this.this$0._table.setView(this.this$0.updateView());
            }
        });
        return result;
    }

    /* access modifiers changed from: protected */
    public JMenuItem createNoLogTableColumnsMenuItem() {
        JMenuItem result = new JMenuItem("Hide all Columns");
        result.setMnemonic('h');
        result.addActionListener(new ActionListener(this) {
            private final LogBrokerMonitor this$0;

            {
                this.this$0 = r1;
            }

            public void actionPerformed(ActionEvent e) {
                this.this$0.selectAllLogTableColumns(false);
                this.this$0._table.setView(this.this$0.updateView());
            }
        });
        return result;
    }

    /* access modifiers changed from: protected */
    public void selectAllLogTableColumns(boolean selected) {
        Iterator columns = getLogTableColumns();
        while (columns.hasNext()) {
            getLogTableColumnMenuItem((LogTableColumn) columns.next()).setSelected(selected);
        }
    }

    /* access modifiers changed from: protected */
    public JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('f');
        fileMenu.add(createOpenMI());
        fileMenu.add(createOpenURLMI());
        fileMenu.addSeparator();
        fileMenu.add(createCloseMI());
        createMRUFileListMI(fileMenu);
        fileMenu.addSeparator();
        fileMenu.add(createExitMI());
        return fileMenu;
    }

    /* access modifiers changed from: protected */
    public JMenuItem createOpenMI() {
        JMenuItem result = new JMenuItem("Open...");
        result.setMnemonic('o');
        result.addActionListener(new ActionListener(this) {
            private final LogBrokerMonitor this$0;

            {
                this.this$0 = r1;
            }

            public void actionPerformed(ActionEvent e) {
                this.this$0.requestOpen();
            }
        });
        return result;
    }

    /* access modifiers changed from: protected */
    public JMenuItem createOpenURLMI() {
        JMenuItem result = new JMenuItem("Open URL...");
        result.setMnemonic('u');
        result.addActionListener(new ActionListener(this) {
            private final LogBrokerMonitor this$0;

            {
                this.this$0 = r1;
            }

            public void actionPerformed(ActionEvent e) {
                this.this$0.requestOpenURL();
            }
        });
        return result;
    }

    /* access modifiers changed from: protected */
    public JMenuItem createCloseMI() {
        JMenuItem result = new JMenuItem(HTTP.CONN_CLOSE);
        result.setMnemonic('c');
        result.setAccelerator(KeyStroke.getKeyStroke("control Q"));
        result.addActionListener(new ActionListener(this) {
            private final LogBrokerMonitor this$0;

            {
                this.this$0 = r1;
            }

            public void actionPerformed(ActionEvent e) {
                this.this$0.requestClose();
            }
        });
        return result;
    }

    /* access modifiers changed from: protected */
    public void createMRUFileListMI(JMenu menu) {
        String[] files = this._mruFileManager.getMRUFileList();
        if (files != null) {
            menu.addSeparator();
            for (int i = 0; i < files.length; i++) {
                JMenuItem result = new JMenuItem(new StringBuffer().append(i + 1).append(" ").append(files[i]).toString());
                result.setMnemonic(i + 1);
                result.addActionListener(new ActionListener(this) {
                    private final LogBrokerMonitor this$0;

                    {
                        this.this$0 = r1;
                    }

                    public void actionPerformed(ActionEvent e) {
                        this.this$0.requestOpenMRU(e);
                    }
                });
                menu.add(result);
            }
        }
    }

    /* access modifiers changed from: protected */
    public JMenuItem createExitMI() {
        JMenuItem result = new JMenuItem("Exit");
        result.setMnemonic('x');
        result.addActionListener(new ActionListener(this) {
            private final LogBrokerMonitor this$0;

            {
                this.this$0 = r1;
            }

            public void actionPerformed(ActionEvent e) {
                this.this$0.requestExit();
            }
        });
        return result;
    }

    /* access modifiers changed from: protected */
    public JMenu createConfigureMenu() {
        JMenu configureMenu = new JMenu("Configure");
        configureMenu.setMnemonic('c');
        configureMenu.add(createConfigureSave());
        configureMenu.add(createConfigureReset());
        configureMenu.add(createConfigureMaxRecords());
        return configureMenu;
    }

    /* access modifiers changed from: protected */
    public JMenuItem createConfigureSave() {
        JMenuItem result = new JMenuItem("Save");
        result.setMnemonic('s');
        result.addActionListener(new ActionListener(this) {
            private final LogBrokerMonitor this$0;

            {
                this.this$0 = r1;
            }

            public void actionPerformed(ActionEvent e) {
                this.this$0.saveConfiguration();
            }
        });
        return result;
    }

    /* access modifiers changed from: protected */
    public JMenuItem createConfigureReset() {
        JMenuItem result = new JMenuItem("Reset");
        result.setMnemonic('r');
        result.addActionListener(new ActionListener(this) {
            private final LogBrokerMonitor this$0;

            {
                this.this$0 = r1;
            }

            public void actionPerformed(ActionEvent e) {
                this.this$0.resetConfiguration();
            }
        });
        return result;
    }

    /* access modifiers changed from: protected */
    public JMenuItem createConfigureMaxRecords() {
        JMenuItem result = new JMenuItem("Set Max Number of Records");
        result.setMnemonic('m');
        result.addActionListener(new ActionListener(this) {
            private final LogBrokerMonitor this$0;

            {
                this.this$0 = r1;
            }

            public void actionPerformed(ActionEvent e) {
                this.this$0.setMaxRecordConfiguration();
            }
        });
        return result;
    }

    /* access modifiers changed from: protected */
    public void saveConfiguration() {
        this._configurationManager.save();
    }

    /* access modifiers changed from: protected */
    public void resetConfiguration() {
        this._configurationManager.reset();
    }

    /* access modifiers changed from: protected */
    public void setMaxRecordConfiguration() {
        String temp = new LogFactor5InputDialog(getBaseFrame(), "Set Max Number of Records", "", 10).getText();
        if (temp != null) {
            try {
                setMaxNumberOfLogRecords(Integer.parseInt(temp));
            } catch (NumberFormatException e) {
                LogFactor5ErrorDialog error = new LogFactor5ErrorDialog(getBaseFrame(), new StringBuffer().append("'").append(temp).append("' is an invalid parameter.\nPlease try again.").toString());
                setMaxRecordConfiguration();
            }
        }
    }

    /* access modifiers changed from: protected */
    public JMenu createHelpMenu() {
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('h');
        helpMenu.add(createHelpProperties());
        return helpMenu;
    }

    /* access modifiers changed from: protected */
    public JMenuItem createHelpProperties() {
        String title = "LogFactor5 Properties";
        JMenuItem result = new JMenuItem("LogFactor5 Properties");
        result.setMnemonic('l');
        result.addActionListener(new ActionListener(this) {
            private final LogBrokerMonitor this$0;

            {
                this.this$0 = r1;
            }

            public void actionPerformed(ActionEvent e) {
                this.this$0.showPropertiesDialog("LogFactor5 Properties");
            }
        });
        return result;
    }

    /* access modifiers changed from: protected */
    public void showPropertiesDialog(String title) {
        JOptionPane.showMessageDialog(this._logMonitorFrame, this._displayedLogBrokerProperties.toArray(), title, -1);
    }

    /* access modifiers changed from: protected */
    public JMenu createEditMenu() {
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic('e');
        editMenu.add(createEditFindMI());
        editMenu.add(createEditFindNextMI());
        editMenu.addSeparator();
        editMenu.add(createEditSortNDCMI());
        editMenu.add(createEditRestoreAllNDCMI());
        return editMenu;
    }

    /* access modifiers changed from: protected */
    public JMenuItem createEditFindNextMI() {
        JMenuItem editFindNextMI = new JMenuItem("Find Next");
        editFindNextMI.setMnemonic('n');
        editFindNextMI.setAccelerator(KeyStroke.getKeyStroke("F3"));
        editFindNextMI.addActionListener(new ActionListener(this) {
            private final LogBrokerMonitor this$0;

            {
                this.this$0 = r1;
            }

            public void actionPerformed(ActionEvent e) {
                this.this$0.findSearchText();
            }
        });
        return editFindNextMI;
    }

    /* access modifiers changed from: protected */
    public JMenuItem createEditFindMI() {
        JMenuItem editFindMI = new JMenuItem("Find");
        editFindMI.setMnemonic('f');
        editFindMI.setAccelerator(KeyStroke.getKeyStroke("control F"));
        editFindMI.addActionListener(new ActionListener(this) {
            private final LogBrokerMonitor this$0;

            {
                this.this$0 = r1;
            }

            public void actionPerformed(ActionEvent e) {
                this.this$0.setSearchText(JOptionPane.showInputDialog(this.this$0._logMonitorFrame, "Find text: ", "Search Record Messages", 3));
                this.this$0.findSearchText();
            }
        });
        return editFindMI;
    }

    /* access modifiers changed from: protected */
    public JMenuItem createEditSortNDCMI() {
        JMenuItem editSortNDCMI = new JMenuItem("Sort by NDC");
        editSortNDCMI.setMnemonic('s');
        editSortNDCMI.addActionListener(new ActionListener(this) {
            private final LogBrokerMonitor this$0;

            {
                this.this$0 = r1;
            }

            public void actionPerformed(ActionEvent e) {
                this.this$0.setNDCTextFilter(JOptionPane.showInputDialog(this.this$0._logMonitorFrame, "Sort by this NDC: ", "Sort Log Records by NDC", 3));
                this.this$0.sortByNDC();
                this.this$0._table.getFilteredLogTableModel().refresh();
                this.this$0.updateStatusLabel();
            }
        });
        return editSortNDCMI;
    }

    /* access modifiers changed from: protected */
    public JMenuItem createEditRestoreAllNDCMI() {
        JMenuItem editRestoreAllNDCMI = new JMenuItem("Restore all NDCs");
        editRestoreAllNDCMI.setMnemonic('r');
        editRestoreAllNDCMI.addActionListener(new ActionListener(this) {
            private final LogBrokerMonitor this$0;

            {
                this.this$0 = r1;
            }

            public void actionPerformed(ActionEvent e) {
                this.this$0._table.getFilteredLogTableModel().setLogRecordFilter(this.this$0.createLogRecordFilter());
                this.this$0.setNDCTextFilter("");
                this.this$0._table.getFilteredLogTableModel().refresh();
                this.this$0.updateStatusLabel();
            }
        });
        return editRestoreAllNDCMI;
    }

    /* access modifiers changed from: protected */
    public JToolBar createToolBar() {
        String[] fonts;
        JToolBar tb = new JToolBar();
        tb.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
        JComboBox fontCombo = new JComboBox();
        JComboBox fontSizeCombo = new JComboBox();
        this._fontSizeCombo = fontSizeCombo;
        ClassLoader cl = getClass().getClassLoader();
        if (cl == null) {
            cl = ClassLoader.getSystemClassLoader();
        }
        URL newIconURL = cl.getResource("org/apache/log4j/lf5/viewer/images/channelexplorer_new.gif");
        ImageIcon newIcon = null;
        if (newIconURL != null) {
            newIcon = new ImageIcon(newIconURL);
        }
        JButton newButton = new JButton("Clear Log Table");
        if (newIcon != null) {
            newButton.setIcon(newIcon);
        }
        newButton.setToolTipText("Clear Log Table.");
        newButton.addActionListener(new ActionListener(this) {
            private final LogBrokerMonitor this$0;

            {
                this.this$0 = r1;
            }

            public void actionPerformed(ActionEvent e) {
                this.this$0._table.clearLogRecords();
                this.this$0._categoryExplorerTree.getExplorerModel().resetAllNodeCounts();
                this.this$0.updateStatusLabel();
                this.this$0.clearDetailTextArea();
                LogRecord.resetSequenceNumber();
            }
        });
        Toolkit tk = Toolkit.getDefaultToolkit();
        if (this._loadSystemFonts) {
            fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        } else {
            fonts = tk.getFontList();
        }
        for (Object addItem : fonts) {
            fontCombo.addItem(addItem);
        }
        fontCombo.setSelectedItem(this._fontName);
        fontCombo.addActionListener(new ActionListener(this) {
            private final LogBrokerMonitor this$0;

            {
                this.this$0 = r1;
            }

            public void actionPerformed(ActionEvent e) {
                String font = (String) ((JComboBox) e.getSource()).getSelectedItem();
                this.this$0._table.setFont(new Font(font, 0, this.this$0._fontSize));
                this.this$0._fontName = font;
            }
        });
        fontSizeCombo.addItem("8");
        fontSizeCombo.addItem("9");
        fontSizeCombo.addItem("10");
        fontSizeCombo.addItem("12");
        fontSizeCombo.addItem("14");
        fontSizeCombo.addItem("16");
        fontSizeCombo.addItem("18");
        fontSizeCombo.addItem("24");
        fontSizeCombo.setSelectedItem(String.valueOf(this._fontSize));
        fontSizeCombo.addActionListener(new ActionListener(this) {
            private final LogBrokerMonitor this$0;

            {
                this.this$0 = r1;
            }

            public void actionPerformed(ActionEvent e) {
                int s = Integer.valueOf((String) ((JComboBox) e.getSource()).getSelectedItem()).intValue();
                this.this$0.setFontSizeSilently(s);
                this.this$0.refreshDetailTextArea();
                this.this$0._fontSize = s;
            }
        });
        tb.add(new JLabel(" Font: "));
        tb.add(fontCombo);
        tb.add(fontSizeCombo);
        tb.addSeparator();
        tb.addSeparator();
        tb.add(newButton);
        newButton.setAlignmentY(0.5f);
        newButton.setAlignmentX(0.5f);
        fontCombo.setMaximumSize(fontCombo.getPreferredSize());
        fontSizeCombo.setMaximumSize(fontSizeCombo.getPreferredSize());
        return tb;
    }

    /* access modifiers changed from: protected */
    public void setView(String viewString, LogTable table) {
        if (DETAILED_VIEW.equals(viewString)) {
            table.setDetailedView();
            this._currentView = viewString;
            return;
        }
        throw new IllegalArgumentException(new StringBuffer().append(viewString).append("does not match a supported view.").toString());
    }

    /* access modifiers changed from: protected */
    public JComboBox createLogLevelCombo() {
        JComboBox result = new JComboBox();
        Iterator levels = getLogLevels();
        while (levels.hasNext()) {
            result.addItem(levels.next());
        }
        result.setSelectedItem(this._leastSevereDisplayedLogLevel);
        result.addActionListener(new ActionListener(this) {
            private final LogBrokerMonitor this$0;

            {
                this.this$0 = r1;
            }

            public void actionPerformed(ActionEvent e) {
                this.this$0.setLeastSevereDisplayedLogLevel((LogLevel) ((JComboBox) e.getSource()).getSelectedItem());
            }
        });
        result.setMaximumSize(result.getPreferredSize());
        return result;
    }

    /* access modifiers changed from: protected */
    public void setLeastSevereDisplayedLogLevel(LogLevel level) {
        if (level != null && this._leastSevereDisplayedLogLevel != level) {
            this._leastSevereDisplayedLogLevel = level;
            this._table.getFilteredLogTableModel().refresh();
            updateStatusLabel();
        }
    }

    /* access modifiers changed from: protected */
    public void trackTableScrollPane() {
    }

    /* access modifiers changed from: protected */
    public void centerFrame(JFrame frame) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension comp = frame.getSize();
        frame.setLocation((screen.width - comp.width) / 2, (screen.height - comp.height) / 2);
    }

    /* access modifiers changed from: protected */
    public void requestOpen() {
        JFileChooser chooser;
        if (this._fileLocation == null) {
            chooser = new JFileChooser();
        } else {
            chooser = new JFileChooser(this._fileLocation);
        }
        if (chooser.showOpenDialog(this._logMonitorFrame) == 0) {
            File f = chooser.getSelectedFile();
            if (loadLogFile(f)) {
                this._fileLocation = chooser.getSelectedFile();
                this._mruFileManager.set(f);
                updateMRUList();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void requestOpenURL() {
        String temp = new LogFactor5InputDialog(getBaseFrame(), "Open URL", "URL:").getText();
        if (temp != null) {
            if (temp.indexOf("://") == -1) {
                temp = new StringBuffer().append("http://").append(temp).toString();
            }
            try {
                URL url = new URL(temp);
                if (loadLogFile(url)) {
                    this._mruFileManager.set(url);
                    updateMRUList();
                }
            } catch (MalformedURLException e) {
                LogFactor5ErrorDialog logFactor5ErrorDialog = new LogFactor5ErrorDialog(getBaseFrame(), "Error reading URL.");
            }
        }
    }

    /* access modifiers changed from: protected */
    public void updateMRUList() {
        JMenu menu = this._logMonitorFrame.getJMenuBar().getMenu(0);
        menu.removeAll();
        menu.add(createOpenMI());
        menu.add(createOpenURLMI());
        menu.addSeparator();
        menu.add(createCloseMI());
        createMRUFileListMI(menu);
        menu.addSeparator();
        menu.add(createExitMI());
    }

    /* access modifiers changed from: protected */
    public void requestClose() {
        setCallSystemExitOnClose(false);
        closeAfterConfirm();
    }

    /* access modifiers changed from: protected */
    public void requestOpenMRU(ActionEvent e) {
        StringTokenizer st = new StringTokenizer(e.getActionCommand());
        String num = st.nextToken().trim();
        String file = st.nextToken("\n");
        try {
            int index = Integer.parseInt(num) - 1;
            new LogFileParser(this._mruFileManager.getInputStream(index)).parse(this);
            this._mruFileManager.moveToTop(index);
            updateMRUList();
        } catch (Exception e2) {
            LogFactor5ErrorDialog logFactor5ErrorDialog = new LogFactor5ErrorDialog(getBaseFrame(), new StringBuffer().append("Unable to load file ").append(file).toString());
        }
    }

    /* access modifiers changed from: protected */
    public void requestExit() {
        this._mruFileManager.save();
        setCallSystemExitOnClose(true);
        closeAfterConfirm();
    }

    /* access modifiers changed from: protected */
    public void closeAfterConfirm() {
        StringBuffer message = new StringBuffer();
        if (this._callSystemExitOnClose) {
            message.append("Are you sure you want to exit?\n");
            message.append("This will shut down the Virtual Machine.\n");
        } else {
            message.append("Are you sure you want to close the logging ");
            message.append("console?\n");
            message.append("(Note: This will not shut down the Virtual Machine,\n");
            message.append("or the Swing event thread.)");
        }
        String title = "Are you sure you want to dispose of the Logging Console?";
        if (this._callSystemExitOnClose) {
            title = "Are you sure you want to exit?";
        }
        if (JOptionPane.showConfirmDialog(this._logMonitorFrame, message.toString(), title, 2, 3, null) == 0) {
            dispose();
        }
    }

    /* access modifiers changed from: protected */
    public Iterator getLogLevels() {
        return this._levels.iterator();
    }

    /* access modifiers changed from: protected */
    public Iterator getLogTableColumns() {
        return this._columns.iterator();
    }

    /* access modifiers changed from: protected */
    public boolean loadLogFile(File file) {
        try {
            new LogFileParser(file).parse(this);
            return true;
        } catch (IOException e) {
            LogFactor5ErrorDialog logFactor5ErrorDialog = new LogFactor5ErrorDialog(getBaseFrame(), new StringBuffer().append("Error reading ").append(file.getName()).toString());
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public boolean loadLogFile(URL url) {
        try {
            new LogFileParser(url.openStream()).parse(this);
            return true;
        } catch (IOException e) {
            LogFactor5ErrorDialog logFactor5ErrorDialog = new LogFactor5ErrorDialog(getBaseFrame(), new StringBuffer().append("Error reading URL:").append(url.getFile()).toString());
            return false;
        }
    }
}
