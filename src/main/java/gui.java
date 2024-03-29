import org.apache.lucene.queryparser.classic.ParseException;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


public class gui extends JFrame {
    private JTextField jTextField1 = new JTextField();
    private JButton bSend = new JButton();
    private JTextPane chatWindow = new JTextPane();
    private JScrollPane jTextArea1ScrollPane = new JScrollPane(chatWindow);
    public communicationManager cm = new communicationManager();


    public gui() throws IOException, InterruptedException {
        //Create Frame
        super();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        chatWindow.setEditable(false);
        int frameWidth = 799;
        int frameHeight = 543;
        setSize(frameWidth, frameHeight);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (d.width - getSize().width) / 2;
        int y = (d.height - getSize().height) / 2;
        setLocation(x, y);
        setTitle("Botti - The best Chatbot in the World");
        setResizable(false);
        Container cp = getContentPane();
        cp.setLayout(null);

        //Create Components
        jTextField1.setBounds(48, 432, 481, 33);
        jTextField1.setToolTipText("Put your message here");
        cp.add(jTextField1);
        bSend.setBounds(552, 432, 113, 33);
        bSend.setText("Send");
        bSend.setMargin(new Insets(2, 2, 2, 2));
        bSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    button_pressed(evt);
                } catch (IOException | InterruptedException | ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        cp.add(bSend);
        jTextArea1ScrollPane.setBounds(48, 32, 481, 345);
        cp.add(jTextArea1ScrollPane);
        setVisible(true);
        //Everything is initialized --> now put Welcome Message
        sleeps();
        coloredText(chatWindow, "Botti: Hey, my name is Botti.\n", Color.RED);
        sleeps();
        coloredText(chatWindow, "Botti: How can I help you?\n", Color.RED);
    }

    //Button Actions
    public void button_pressed(ActionEvent evt) throws IOException, InterruptedException, ParseException {
        String message = jTextField1.getText();
        jTextField1.setText(""); //Should reset text in JTextField
        coloredText(chatWindow, "You: "+message+"\n", Color.GREEN);
        sleeps();
        botAnswer(message); //had to split because sleep pauses whole method
    } // end of bSend_ActionPerformed

    public void botAnswer(String message) throws IOException, InterruptedException, ParseException {
        String answer = cm.getMessageText(message);
        coloredText(chatWindow, "Bottie: "+answer+"\n", Color.RED);
    }


    //Make text Colorful
    public void coloredText(JTextPane pane, String message, Color color) throws InterruptedException {
        StyledDocument doc = pane.getStyledDocument();
        Style style = pane.addStyle("Color Style", null);
        StyleConstants.setForeground(style, color);
        try{
            doc.insertString(doc.getLength(), message, style);
        }
        catch(BadLocationException e){
            e.printStackTrace();
        }
    }

    public void sleeps() throws InterruptedException {
        Thread.sleep((int)(Math.random()*1000));
    }

}