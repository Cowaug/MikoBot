package MikoBot.GUI;

import MikoBot.BotInstance;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Console extends JFrame {
    private JFrame frame;

    private volatile JPanel panel;
    private JButton startBtn;
    private JTabbedPane tabbedPane1;
    private JPanel tabHome;
    private volatile JTextArea messageHistory;

    private BotInstance botInstance = null;

    /**
     * Create the GUI
     * and add a safety closing mechanism
     */
    public Console(String[] args) {
        frame = new JFrame("eBotTeam Bot");
        frame.setContentPane(this.panel);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(480, 240);
        messageHistory.setEditable(false);
        WindowAdapter adapter = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };
        frame.addWindowListener(adapter);
        frame.addWindowFocusListener(adapter);

        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (botInstance == null) {
                    botInstance = new BotInstance(args[0],args[1]);
                    startBtn.setText(args[1]);
                    startBtn.setEnabled(false);
                }
            }
        });
    }

    /**
     * Update the GUI to show stuff
     *
     * @param newMessage Message to show
     */
    public void update(final String newMessage) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> update(newMessage));
            return;
        }

        if (newMessage != null) {
            messageHistory.append(newMessage + "\n");
            int w = frame.getWidth();
            int h = frame.getHeight();
            frame.setContentPane(this.panel);
            frame.pack();
            frame.setVisible(true);
            frame.setSize(w, h);
            messageHistory.setCaretPosition(messageHistory.getDocument().getLength() - 1);

        } else {
            frame.setVisible(true);
        }
    }
}
