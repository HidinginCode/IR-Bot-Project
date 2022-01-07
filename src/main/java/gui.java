import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class gui extends JFrame {
    // Anfang Attribute
    private JTextField jTextField1 = new JTextField();
    private JButton bSend = new JButton();
    private JTextArea jTextArea1 = new JTextArea("");
    private JScrollPane jTextArea1ScrollPane = new JScrollPane(jTextArea1);
    // Ende Attribute

    public gui() {
        // Frame-Initialisierung
        super();
        jTextArea1.setEditable(false);  //TextArea1 can not be edited by the user directly
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        int frameWidth = 799;
        int frameHeight = 543;
        setSize(frameWidth, frameHeight);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (d.width - getSize().width) / 2;
        int y = (d.height - getSize().height) / 2;
        setLocation(x, y);
        setTitle("test");
        setResizable(false);
        Container cp = getContentPane();
        cp.setLayout(null);
        // Anfang Komponenten

        jTextField1.setBounds(48, 432, 481, 33);
        jTextField1.setToolTipText("Put your message here");
        cp.add(jTextField1);
        bSend.setBounds(552, 432, 113, 33);
        bSend.setText("Send");
        bSend.setMargin(new Insets(2, 2, 2, 2));
        bSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jTextField1.setText(""); //Should reset text in JTextField
            }
        });
        cp.add(bSend);
        jTextArea1ScrollPane.setBounds(48, 32, 481, 345);
        cp.add(jTextArea1ScrollPane);
        // Ende Komponenten

        setVisible(true);
    }
}