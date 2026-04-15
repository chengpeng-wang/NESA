package org.apache.log4j.lf5.viewer;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LogFactor5InputDialog extends LogFactor5Dialog {
    public static final int SIZE = 30;
    private JTextField _textField;

    static JTextField access$000(LogFactor5InputDialog x0) {
        return x0._textField;
    }

    public LogFactor5InputDialog(JFrame jframe, String title, String label) {
        this(jframe, title, label, 30);
    }

    public LogFactor5InputDialog(JFrame jframe, String title, String label, int size) {
        super(jframe, title, true);
        JPanel bottom = new JPanel();
        bottom.setLayout(new FlowLayout());
        JPanel main = new JPanel();
        main.setLayout(new FlowLayout());
        main.add(new JLabel(label));
        this._textField = new JTextField(size);
        main.add(this._textField);
        addKeyListener(new KeyAdapter(this) {
            private final LogFactor5InputDialog this$0;

            {
                this.this$0 = r1;
            }

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    this.this$0.hide();
                }
            }
        });
        JButton ok = new JButton("Ok");
        ok.addActionListener(new ActionListener(this) {
            private final LogFactor5InputDialog this$0;

            {
                this.this$0 = r1;
            }

            public void actionPerformed(ActionEvent e) {
                this.this$0.hide();
            }
        });
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener(this) {
            private final LogFactor5InputDialog this$0;

            {
                this.this$0 = r1;
            }

            public void actionPerformed(ActionEvent e) {
                this.this$0.hide();
                LogFactor5InputDialog.access$000(this.this$0).setText("");
            }
        });
        bottom.add(ok);
        bottom.add(cancel);
        getContentPane().add(main, "Center");
        getContentPane().add(bottom, "South");
        pack();
        centerWindow(this);
        show();
    }

    public String getText() {
        String s = this._textField.getText();
        if (s == null || s.trim().length() != 0) {
            return s;
        }
        return null;
    }
}
