package org.apache.log4j.lf5.viewer;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.JFrame;
import org.apache.http.HttpStatus;

public abstract class LogFactor5Dialog extends JDialog {
    protected static final Font DISPLAY_FONT = new Font("Arial", 1, 12);

    protected LogFactor5Dialog(JFrame jframe, String message, boolean modal) {
        super(jframe, message, modal);
    }

    public void show() {
        pack();
        minimumSizeDialog(this, HttpStatus.SC_OK, 100);
        centerWindow(this);
        LogFactor5Dialog.super.show();
    }

    /* access modifiers changed from: protected */
    public void centerWindow(Window win) {
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        if (screenDim.width < win.getSize().width) {
            win.setSize(screenDim.width, win.getSize().height);
        }
        if (screenDim.height < win.getSize().height) {
            win.setSize(win.getSize().width, screenDim.height);
        }
        win.setLocation((screenDim.width - win.getSize().width) / 2, (screenDim.height - win.getSize().height) / 2);
    }

    /* access modifiers changed from: protected */
    public void wrapStringOnPanel(String message, Container container) {
        GridBagConstraints c = getDefaultConstraints();
        c.gridwidth = 0;
        c.insets = new Insets(0, 0, 0, 0);
        GridBagLayout gbLayout = (GridBagLayout) container.getLayout();
        while (message.length() > 0) {
            String line;
            int newLineIndex = message.indexOf(10);
            if (newLineIndex >= 0) {
                line = message.substring(0, newLineIndex);
                message = message.substring(newLineIndex + 1);
            } else {
                line = message;
                message = "";
            }
            Label label = new Label(line);
            label.setFont(DISPLAY_FONT);
            gbLayout.setConstraints(label, c);
            container.add(label);
        }
    }

    /* access modifiers changed from: protected */
    public GridBagConstraints getDefaultConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1.0d;
        constraints.weighty = 1.0d;
        constraints.gridheight = 1;
        constraints.insets = new Insets(4, 4, 4, 4);
        constraints.fill = 0;
        constraints.anchor = 17;
        return constraints;
    }

    /* access modifiers changed from: protected */
    public void minimumSizeDialog(Component component, int minWidth, int minHeight) {
        if (component.getSize().width < minWidth) {
            component.setSize(minWidth, component.getSize().height);
        }
        if (component.getSize().height < minHeight) {
            component.setSize(component.getSize().width, minHeight);
        }
    }
}
