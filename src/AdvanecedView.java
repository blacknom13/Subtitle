import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class AdvanecedView extends JFrame implements ActionListener, WindowListener, DocumentListener {

    JButton captureStartTime, captureEndTime, nextSubtitle, divideSubtitle, prevSubtitle, saveChanges,
            stadd10ms, stadd100ms, stadd1s, stsub10ms, stsub100ms, stsub1s,
            endadd10ms, endadd100ms, endadd1s, endsub10ms, endsub100ms, endsub1s;
    JTextField startTime, endTime;
    JTextArea subtitle;
    final Pattern timeFormat = Pattern.compile("(([01][0-9])|([2][0-3]))(:[0-5][0-9]){2},[0-9]{3}");

    int currInd;
    ArrayList<String> subtitles;
    protected ArrayList<String> timing;

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
        subtitle.getDocument().putProperty("parent","sub");
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
        startTime.getDocument().addDocumentListener(this);
        startTime.getDocument().putProperty("parent","start");
        con.fill = GridBagConstraints.HORIZONTAL;
        con.weightx = 0.4;
        con.gridx = 0;
        con.gridy = 3;
        this.add(startTime, con);

        endTime = new JTextField();
        endTime.getDocument().addDocumentListener(this);
        endTime.getDocument().putProperty("parent","end");
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

        divideSubtitle = new JButton("Divide subtitle");
        divideSubtitle.addActionListener(this);
        divideSubtitle.setActionCommand("div");
        con.fill = GridBagConstraints.HORIZONTAL;
        con.gridwidth = 1;
        con.weightx = 0.4;
        con.gridx = 3;
        con.gridy = 7;
        this.add(divideSubtitle, con);

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

    private boolean hasNextSubtitle(){
        return (currInd<subtitles.size()-1);
    }

    private void loadSubtitle() {
        subtitle.setText(subtitles.get(currInd));
        String[] time;
        time = timing.get(currInd).split(" ");
        startTime.setText(time.length < 3 ? "" : time[0]);
        endTime.setText(time.length < 3 ? "" : time[2]);
        if (!(time.length < 3)) {
            ClassicPlayerUse.seekToTime(time[0]);
        }
    }

    private void createSubtitle(int ind) {
        if (ind>=subtitles.size()) {
            subtitles.add("");
            timing.add("");
        }else{
            subtitles.add(ind,"");
            timing.add(ind,"");
        }
        nextSubtitle.setEnabled(false);
        saveChanges.setEnabled(false);
        divideSubtitle.setEnabled(false);
    }

    protected void saveSubtitle(String sub, String stime, String etime, int ind) {
        String text = subtitle.getText();
        String time;
        if (sub.isEmpty()) {
            removeEmptySubtitle(ind);
        }
        if (timeFormat.matcher(stime).matches() && timeFormat.matcher(etime).matches()) {
            String msg="";
            boolean showMsg=false;
            int retVal=JOptionPane.YES_OPTION;
//            if (ind - 1 >= 0) { // FIXME: 16-Mar-21
//                String prevEndTime= timing.get(ind-1);
//                System.out.println(stime+" "+ prevEndTime.substring(prevEndTime.lastIndexOf(' ')+1));
//                if(!prevEndTime.isEmpty() &&
//                        stime.compareTo(prevEndTime.substring(prevEndTime.lastIndexOf(' ')+1))<0){
//                    msg="The starting time of this subtitle overlaps with the previous one. Save anyway?";
//                    showMsg=true;
//                }
//            }
//            if (ind +1<timing.size()){ // FIXME: 16-Mar-21
//                String nextStartTime= timing.get(ind+1);
//                System.out.println(etime+" "+(!nextStartTime.isEmpty()?nextStartTime.substring(0,nextStartTime.indexOf(' ')):" "));
//                if(!nextStartTime.isEmpty() &&
//                        etime.compareTo(nextStartTime.substring(0,nextStartTime.indexOf(' ')))>0){
//                    msg="The ending time of this subtitle overlaps with the next one. Save anyway?";
//                    showMsg=true;
//                }
//            }
            if (showMsg) {
                retVal=JOptionPane.showConfirmDialog(null, msg, "Time overlap",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
            }
            if (retVal==JOptionPane.YES_OPTION)
                time = stime + " --> " + etime;
            else
                return;
        }
        else
            time = "";
        subtitles.set(ind, text);
        timing.set(ind, time);
    }

    protected void insertSubtitle(String sub, String stime, String etime, int ind){
        createSubtitle(ind);
        saveSubtitle(sub,stime,etime,ind);
    }

    private void removeEmptySubtitle(int i) {
        if (subtitles.get(i).isEmpty()) {
            subtitles.remove(i);
            timing.remove(i);
        }
    }

    public int getCurrInd(){
        return currInd;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String a = actionEvent.getActionCommand();
        long millis = 0;
        String time = "";

        switch (a) {
            case "div":
                DivideSubtitleFrame d=new DivideSubtitleFrame(this.getWidth(),this.getHeight(),subtitle.getText(),
                        startTime.getText(),endTime.getText(),this);
                d.setVisible(true);
                break;
            case "save":
                saveSubtitle(subtitle.getText(),startTime.getText(),endTime.getText(),currInd);
                nextSubtitle.doClick();
                break;
            case "next":
                currInd++;
                if (currInd >= subtitles.size()) {
//                    end = JOptionPane.showConfirmDialog(null, "The end of the list reached! Add new subtitle?"
//                            , "", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    createSubtitle(currInd);
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
                time = ClassicPlayerUse.captureTime();
                time = ClassicPlayerUse.formatTime(time);
                break;
            case "s+10":
            case "s+100":
            case "s+1000":
            case "s-10":
            case "s-100":
            case "s-1000":
                millis = ClassicPlayerUse.allToMillis(startTime.getText());
                break;
            case "e+10":
            case "e+100":
            case "e+1000":
            case "e-10":
            case "e-100":
            case "e-1000":
                millis = ClassicPlayerUse.allToMillis(endTime.getText());
                break;
            default:
                break;
        }
        switch (a) {
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
                millis += Integer.parseInt(a.substring(1));
                startTime.setText(ClassicPlayerUse.formatTime(millis));
                break;
            case "e+10":
            case "e+100":
            case "e+1000":
            case "e-10":
            case "e-100":
            case "e-1000":
                millis += Integer.parseInt(a.substring(1));
                endTime.setText(ClassicPlayerUse.formatTime(millis));
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
        manageButtons();
    }

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {

    }

    @Override
    public void insertUpdate(DocumentEvent documentEvent) {
        manageButtons();
//        if (hasNextSubtitle()) {
//            nextSubtitle.setText("Next subtitle");
//        }
//        nextSubtitle.setEnabled(true);
//        if (!subtitle.getText().isEmpty())
//            saveChanges.setEnabled(true);
//            if (timeFormat.matcher(startTime.getText()).matches() && timeFormat.matcher(endTime.getText()).matches()){
//                divideSubtitle.setEnabled(true);
//            }
    }

    @Override
    public void removeUpdate(DocumentEvent documentEvent) {
        manageButtons();
    }

    @Override
    public void changedUpdate(DocumentEvent documentEvent) {
        manageButtons();
//        if (!hasNextSubtitle())
//            nextSubtitle.setText("Create subtitle");
//        if (subtitle.getText().isEmpty() || !timeFormat.matcher(startTime.getText()).matches() ||
//                !timeFormat.matcher(endTime.getText()).matches())
//            divideSubtitle.setEnabled(false);
//        if (subtitle.getText().isEmpty()) {
//            saveChanges.setEnabled(false);
//            if (nextSubtitle.getText().equals("Create subtitle"))
//                nextSubtitle.setEnabled(false);
//        }
    }

    private void manageButtons(){
        if (!hasNextSubtitle())
            nextSubtitle.setText("Create subtitle");
        else
            nextSubtitle.setText("Next Subtitle");
        if (subtitle.getText().isEmpty() || !timeFormat.matcher(startTime.getText()).matches() ||
                !timeFormat.matcher(endTime.getText()).matches())
            divideSubtitle.setEnabled(false);
        else{
            divideSubtitle.setEnabled(true);
        }
        if (subtitle.getText().isEmpty()) {
            saveChanges.setEnabled(false);
            if (nextSubtitle.getText().equals("Create subtitle"))
                nextSubtitle.setEnabled(false);
            else
                nextSubtitle.setEnabled(true);
        }else{
            saveChanges.setEnabled(true);
            nextSubtitle.setEnabled(true);
        }
    }
}
