package MikoBot.GUI;

import MikoBot.MediaManager;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Console extends JFrame{
    private JFrame frame;

    private volatile JPanel panel;
    private volatile JTextArea textArea1;

    /**
     * Create the GUI
     * and add a safety closing mechanism
     */
    public Console (String title) {
        frame = new JFrame(title);
        frame.setContentPane(this.panel);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(480, 240);
        textArea1.setEditable(false);
        WindowAdapter adapter = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MediaManager.disconnectAll();
                System.exit(0);
            }
        };
        frame.addWindowListener(adapter);
        frame.addWindowFocusListener(adapter);
    }

    /**
     * Update the GUI to show stuff
     * @param newMessage Message to show
     */
    public void print(final String newMessage) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> print(newMessage));
            return;
        }

        if (newMessage != null) {
            textArea1.append(newMessage + "\n");
            int w = frame.getWidth();
            int h = frame.getHeight();
            frame.setContentPane(this.panel);
            frame.pack();
            frame.setVisible(true);
            frame.setSize(w, h);
            textArea1.setCaretPosition(textArea1.getDocument().getLength() - 1);

        } else {
            frame.setVisible(true);
        }
    }
}
