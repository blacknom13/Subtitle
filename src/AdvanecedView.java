import com.eeeeeric.mpc.hc.api.MediaPlayerClassicHomeCinema;
import com.eeeeeric.mpc.hc.api.WMCommand;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class AdvanecedView extends JFrame implements ActionListener, WindowListener, DocumentListener {

    JButton captureStartTime, captureEndTime, nextSubtitle, prevSubtitle, saveChanges,
            stadd10ms, stadd100ms, stadd1s, stsub10ms, stsub100ms, stsub1s,
            endadd10ms, endadd100ms, endadd1s, endsub10ms, endsub100ms, endsub1s;
    JTextField startTime, endTime;
    JTextArea subtitle;
    JPanel timeCtrl;
    final Pattern timeFormat = Pattern.compile("(([01][0-9])|([2][0-3]))(:[0-5][0-9]){2},[0-9]{3}");

    int currInd;
    int end;
    ArrayList<String> subtitles;
    ArrayList<String> timing;

    AdvanecedView(ArrayList<String> subtitles, ArrayList<String> timing) {
        super();
        this.subtitles = subtitles;
        this.timing = timing;
        currInd = findFirstEmptyTiming();

        this.setTitle("Simple Subtitle Manager");
        this.setSize(600, 300);
        this.setLayout(new GridBagLayout());
        GridBagConstraints con = new GridBagConstraints();
        con.fill = GridBagConstraints.HORIZONTAL;
        con.gridx = 0;
        con.gridy = 0;
        this.add(new JLabel("Subtitle text"), con);

        subtitle = new JTextArea();
        subtitle.setLineWrap(true);
        subtitle.getDocument().addDocumentListener(this);
        con.fill = GridBagConstraints.HORIZONTAL;
        con.gridwidth = 7;
        con.ipady = 40;
        con.gridx = 0;
        con.gridy = 1;
        this.add(subtitle, con);

        con.fill = GridBagConstraints.HORIZONTAL;
        con.ipady = 0;
        con.gridwidth = 3;
        con.weightx = 0.4;
        con.gridx = 0;
        con.gridy = 2;
        this.add(new JLabel("Start time"), con);

        con.fill = GridBagConstraints.HORIZONTAL;
        con.weightx = 0.4;
        con.gridx = 4;
        con.gridy = 2;
        this.add(new JLabel("End time"), con);

        startTime = new JTextField();
        con.fill = GridBagConstraints.HORIZONTAL;
        con.weightx = 0.4;
        con.gridx = 0;
        con.gridy = 3;
        this.add(startTime, con);

        endTime = new JTextField();
        con.fill = GridBagConstraints.HORIZONTAL;
        con.weightx = 0.4;
        con.gridx = 4;
        con.gridy = 3;
        this.add(endTime, con);

        captureStartTime = new JButton("Capture");
        captureStartTime.addActionListener(this);
        captureStartTime.setActionCommand("start");
        con.fill = GridBagConstraints.HORIZONTAL;
        con.weightx = 0.4;
        con.gridx = 0;
        con.gridy = 4;
        this.add(captureStartTime, con);

        captureEndTime = new JButton("Capture");
        captureEndTime.addActionListener(this);
        captureEndTime.setActionCommand("end");
        con.fill = GridBagConstraints.HORIZONTAL;
        con.weightx = 0.4;
        con.gridx = 4;
        con.gridy = 4;
        this.add(captureEndTime, con);

        //**********************************************************************************//
        stsub10ms = new JButton("-10ms");
        stsub10ms.addActionListener(this);
        stsub10ms.setActionCommand("s-10");
        con.fill = GridBagConstraints.HORIZONTAL;
        con.weightx = 0.035;
        con.gridwidth = 1;
        con.gridx = 0;
        con.gridy = 5;
        this.add(stsub10ms, con);

        stsub100ms = new JButton("-100ms");
        stsub100ms.addActionListener(this);
        stsub100ms.setActionCommand("s-100");
        con.gridx = 1;
        con.gridy = 5;
        this.add(stsub100ms, con);

        stsub1s = new JButton("-1s");
        stsub1s.addActionListener(this);
        stsub1s.setActionCommand("s-1000");
        con.gridx = 2;
        con.gridy = 5;
        this.add(stsub1s, con);

        stadd10ms = new JButton("+10ms");
        stadd10ms.addActionListener(this);
        stadd10ms.setActionCommand("s+10");
        con.gridx = 0;
        con.gridy = 6;
        this.add(stadd10ms, con);

        stadd100ms = new JButton("+100ms");
        stadd100ms.addActionListener(this);
        stadd100ms.setActionCommand("s+100");
        con.gridx = 1;
        con.gridy = 6;
        this.add(stadd100ms, con);

        stadd1s = new JButton("+1s");
        stadd1s.addActionListener(this);
        stadd1s.setActionCommand("s+1000");
        con.gridx = 2;
        con.gridy = 6;
        this.add(stadd1s, con);

        //**********************************************************************************//
        endsub10ms = new JButton("-10ms");
        endsub10ms.addActionListener(this);
        endsub10ms.setActionCommand("e-10");
        con.fill = GridBagConstraints.HORIZONTAL;
        con.weightx = 0.035;
        con.gridx = 4;
        con.gridy = 5;
        this.add(endsub10ms, con);

        endsub100ms = new JButton("-100ms");
        endsub100ms.addActionListener(this);
        endsub100ms.setActionCommand("e-100");
        con.gridx = 5;
        con.gridy = 5;
        this.add(endsub100ms, con);

        endsub1s = new JButton("-1s");
        endsub1s.addActionListener(this);
        endsub1s.setActionCommand("e-1000");
        con.gridx = 6;
        con.gridy = 5;
        this.add(endsub1s, con);

        endadd10ms = new JButton("+10ms");
        endadd10ms.addActionListener(this);
        endadd10ms.setActionCommand("e+10");
        con.gridx = 4;
        con.gridy = 6;
        this.add(endadd10ms, con);

        endadd100ms = new JButton("+100ms");
        endadd100ms.addActionListener(this);
        endadd100ms.setActionCommand("e+100");
        con.gridx = 5;
        con.gridy = 6;
        this.add(endadd100ms, con);

        endadd1s = new JButton("+1s");
        endadd1s.addActionListener(this);
        endadd1s.setActionCommand("e+1000");
        con.gridx = 6;
        con.gridy = 6;
        this.add(endadd1s, con);
        //***********************************************************************//

        prevSubtitle = new JButton("Previous subtitle");
        prevSubtitle.addActionListener(this);
        prevSubtitle.setActionCommand("prev");
        con.fill = GridBagConstraints.HORIZONTAL;
        con.anchor = GridBagConstraints.PAGE_END;
        con.gridwidth = 3;
        con.weighty = 1;
        con.insets = new Insets(10, 0, 0, 0);
        con.weightx = 0.4;
        con.gridx = 0;
        con.gridy = 7;
        this.add(prevSubtitle, con);

        nextSubtitle = new JButton("Next subtitle");
        nextSubtitle.addActionListener(this);
        nextSubtitle.setActionCommand("next");
        con.fill = GridBagConstraints.HORIZONTAL;
        con.gridx = 4;
        con.gridy = 7;
        this.add(nextSubtitle, con);

        saveChanges = new JButton("Save Changes");
        saveChanges.addActionListener(this);
        saveChanges.setActionCommand("save");
        con.fill = GridBagConstraints.HORIZONTAL;
        con.gridwidth = 1;
        con.insets.set(0, 0, 0, 0);
        con.weightx = 0.2;
        con.weighty = 0;
        con.gridx = 3;
        con.gridy = 8;
        this.add(saveChanges, con);

        if (currInd != -1) {
            subtitle.setText(subtitles.get(currInd));
        }

        this.addWindowListener(this);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private int findFirstEmptyTiming() {
        for (int i = 0; i < timing.size(); i++) {
            if (timing.get(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    private void loadSubtitle() {
        subtitle.setText(subtitles.get(currInd));
        String[] time;
        time = timing.get(currInd).split(" ");
        startTime.setText(time.length < 3 ? "" : time[0]);
        endTime.setText(time.length < 3 ? "" : time[2]);
        if(!(time.length<3)){
            try {
                ClassicPlayerUse.mpc.execute(WMCommand.SEEK,new MediaPlayerClassicHomeCinema.KeyValuePair("position",time[0]));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createSubtitle(){
        subtitles.add("");
        timing.add("");
        nextSubtitle.setEnabled(false);
        saveChanges.setEnabled(false);
    }

    private void saveSubtitle() {
        String text = subtitle.getText();
        String time;
        if (timeFormat.matcher(startTime.getText()).matches() && timeFormat.matcher(endTime.getText()).matches())
            time = startTime.getText() + " --> " + endTime.getText();
        else
            time = "";
        subtitles.set(currInd, text);
        timing.set(currInd, time);
    }

    private void removeEmptySubtitles(){
        for (int i=0;i<subtitles.size();i++){
            if (subtitles.get(i).isEmpty()){
                subtitles.remove(i);
                timing.remove(i);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String a= actionEvent.getActionCommand();
        long millis=0;
        String time="";

        switch (a) {
            case "save":
                saveSubtitle();
                nextSubtitle.doClick();
                break;
            case "next":
                currInd++;
                if (currInd >= subtitles.size()) {
                    end = JOptionPane.showConfirmDialog(null, "The end of the list reached! Add new subtitle?"
                            , "", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    createSubtitle();
                }
                break;
            case "prev":
                currInd--;
                if (currInd < 0) {
                    JOptionPane.showConfirmDialog(null, "No more previous subtitles to load!"
                            , "", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    currInd = 0;
                }
                break;
            case "start":
            case "end":
                try {
                    time = ClassicPlayerUse.mpc.getVariables().get("position");
                    time = ClassicPlayerUse.convertString(time);
                }catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "s+10":
            case "s+100":
            case "s+1000":
            case "s-10":
            case "s-100":
            case "s-1000":
                millis=ClassicPlayerUse.allToMillis(startTime.getText());
                break;
            case "e+10":
            case "e+100":
            case "e+1000":
            case "e-10":
            case "e-100":
            case "e-1000":
                millis=ClassicPlayerUse.allToMillis(endTime.getText());
                break;
            default:
                break;
        }
        switch (a){
            case "next":
            case "prev":
                loadSubtitle();
                break;
            case "start":
                startTime.setText(time);
                break;
            case "end":
                endTime.setText(time);
                break;
            case "s+10":
            case "s+100":
            case "s+1000":
            case "s-10":
            case "s-100":
            case "s-1000":
                millis+=Integer.parseInt(a.substring(1));
                startTime.setText(ClassicPlayerUse.convertString(millis));
                break;
            case "e+10":
            case "e+100":
            case "e+1000":
            case "e-10":
            case "e-100":
            case "e-1000":
                millis+=Integer.parseInt(a.substring(1));
                endTime.setText(ClassicPlayerUse.convertString(millis));
                break;
            default:
                break;
        }
    }

    @Override
    public void windowOpened(WindowEvent windowEvent) {

    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        removeEmptySubtitles();
        Main.setClosed();
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

    @Override
    public void insertUpdate(DocumentEvent documentEvent) {
        nextSubtitle.setEnabled(true);
        saveChanges.setEnabled(true);
    }

    @Override
    public void removeUpdate(DocumentEvent documentEvent) {
        if (documentEvent.getDocument().getLength()==0) {
            nextSubtitle.setEnabled(false);
            saveChanges.setEnabled(false);
        }
    }

    @Override
    public void changedUpdate(DocumentEvent documentEvent) {
    }
}
