import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class DivideSubtitleFrame extends JFrame implements ActionListener, WindowListener {

    JTextArea subtitlePart1, subtitlePart2;

    JTextField startTime, endTime, midTime[];
    JButton captureMidTime, done, cancel;
    private AdvanecedView view;

    DivideSubtitleFrame(int width, int height, String subtitle, String strtTime, String finTime, AdvanecedView view) {
        super();
        this.setSize(width, height);
        this.setLayout(new GridBagLayout());

        GridBagConstraints con = new GridBagConstraints();

        con.fill = GridBagConstraints.HORIZONTAL;
        con.gridx = 0;
        con.gridy = 0;
        this.add(new JLabel("First subtitle part"), con);

        subtitlePart1 = new JTextArea(subtitle);
        con.gridx = 0;
        con.gridy = 1;
        con.ipady = 40;
        con.gridwidth = 3;
        this.add(subtitlePart1, con);

        con.gridx = 0;
        con.gridy = 2;
        con.weightx = 0.4;
        con.ipady = 0;
        con.gridwidth = 1;
        this.add(new JLabel("Start time"), con);

        con.gridx = 2;
        con.gridy = 2;
        this.add(new JLabel("End time"), con);

        startTime = new JTextField(strtTime);
        startTime.setFocusable(false);
        con.gridx = 0;
        con.gridy = 3;
        this.add(startTime, con);

        midTime = new JTextField[2];
        midTime[0] = new JTextField();
        con.gridx = 2;
        con.gridy = 3;
        this.add(midTime[0], con);

        subtitlePart2 = new JTextArea(subtitle);
        con.gridx = 0;
        con.gridy = 4;
        con.ipady = 40;
        con.gridwidth = 3;
        this.add(subtitlePart2, con);

        con.gridx = 0;
        con.gridy = 5;
        con.weightx = 0.4;
        con.ipady = 0;
        con.gridwidth = 1;
        this.add(new JLabel("Start time"), con);

        con.gridx = 2;
        con.gridy = 5;
        this.add(new JLabel("End time"), con);

        midTime[1] = new JTextField();
        con.gridx = 0;
        con.gridy = 6;
        this.add(midTime[1], con);

        endTime = new JTextField(finTime);
        endTime.setFocusable(false);
        con.gridx = 2;
        con.gridy = 6;
        this.add(endTime, con);

        captureMidTime = new JButton("Capture middle time");
        captureMidTime.addActionListener(this);
        captureMidTime.setActionCommand("mid");
        con.anchor = GridBagConstraints.PAGE_END;
        con.weighty = 1;
        con.gridx = 0;
        con.gridy = 7;
        this.add(captureMidTime, con);

        done = new JButton("Done editing");
        done.addActionListener(this);
        done.setActionCommand("done");
        con.gridx = 2;
        con.gridy = 7;
        this.add(done, con);

        cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        cancel.setActionCommand("cancel");
        con.gridx = 1;
        con.gridy = 7;
        this.add(cancel, con);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.addWindowListener(this);
        this.view = view;
        this.view.setFocusable(false);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        switch (actionEvent.getActionCommand()) {
            case "mid":
                String time = ClassicPlayerUse.captureTime();
                time = ClassicPlayerUse.formatTime(time);
                midTime[0].setText(time);
                midTime[1].setText(time);
                break;
            case "done":
                view.saveSubtitle(subtitlePart1.getText(), startTime.getText(), midTime[0].getText(), view.getCurrInd());
                view.insertSubtitle(subtitlePart2.getText(), midTime[1].getText(), endTime.getText(), view.getCurrInd() + 1);
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                break;
            case "cancel":
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                break;
            default:
                break;
        }
    }

    @Override
    public void windowOpened(WindowEvent windowEvent) {
        view.setEnabled(false);
    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        view.setEnabled(true);
    }

    @Override
    public void windowClosed(WindowEvent windowEvent) {

    }

    @Override
    public void windowIconified(WindowEvent windowEvent) {

    }

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {

    }

    @Override
    public void windowActivated(WindowEvent windowEvent) {

    }

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {

    }
}
