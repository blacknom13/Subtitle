import com.eeeeeric.mpc.hc.api.TimeCode;
import com.eeeeeric.mpc.hc.api.TimeCodeException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.ArrayList;

public class Main {

    static boolean windowClosed = false;
    static File prefs = new File("prefs.txt");

    public static void main(String[] args) {

        int connectionPort=-1;
        String intputPort = "";

        String path = "";
        ArrayList<String> settings = new ArrayList<>();
        String temp;

        try (BufferedReader reader = new BufferedReader(new FileReader(prefs))) {
            while ((temp = reader.readLine()) != null) {
                settings.add(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (settings.size() > 0) {
            intputPort = settings.get(0);
            path = settings.get(1);
        }

        while (true) {
            intputPort = JOptionPane.showInputDialog(null, "Enter the number port on which the player listens", intputPort);
            try {
                if (intputPort==null)
                    break;
                connectionPort = Integer.parseInt(intputPort);
                ClassicPlayerUse.initConnection(connectionPort);
                ClassicPlayerUse.mpc.seek(new TimeCode("00:00:00"));
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showConfirmDialog(null, "The number format isn't correct, please retry",
                        "Erroneous number",JOptionPane.DEFAULT_OPTION);
                intputPort = "";
            }catch (IOException|TimeCodeException e) {
                JOptionPane.showConfirmDialog(null,
                        "Either the Media Player Classic is offline or the port isn't correct.","Error",JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        if (intputPort==null) {
            JOptionPane.showConfirmDialog(null, "The Program is terminated","Exit",JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }

        ArrayList<String> subtitles = new ArrayList<>();
        ArrayList<String> timing = new ArrayList<>();


        JFileChooser ch = new JFileChooser(path);
        ch.setDialogTitle("Choose subtitle file to edit");
        int returnVal;
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Subtitle files", "srt", "txt");
        ch.setFileFilter(filter);

        returnVal = ch.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            path = ch.getSelectedFile().getPath();
            String tempSub = "", tempTime = "";
            File f = new File(path);
            boolean over = false;
            try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
                while (!over) {
                    temp = reader.readLine();
                    if (temp == null) {
                        over = true;
                        temp = "";
                    }
                    temp = temp.strip();
                    if (temp.equals("")) {
                        subtitles.add(tempSub);
                        timing.add(tempTime);
                        tempTime = "";
                        tempSub = "";
                    } else
                        try {
                            Integer.parseInt(temp);
                        } catch (NumberFormatException e) {
                            if (temp.contains("-->")) {
                                tempTime = temp;
                            } else {
                                tempSub += (tempSub.isEmpty() ? "" : "\n") + temp;
                            }
                        }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        AdvanecedView v = new AdvanecedView(subtitles, timing);
        v.setVisible(true);

        while (!windowClosed) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        JCheckBox checkBox = new JCheckBox("Save to the same file");
        checkBox.setSelected(true);
        Object[] msg = new Object[]{checkBox, "Save changes made to the file?"};
        returnVal = JOptionPane.showConfirmDialog(null, msg, "Save changes", JOptionPane.YES_NO_OPTION);
        if (returnVal == JOptionPane.OK_OPTION) {
            File outfull;
            File outWorking;
            String full="",working="";
            if (checkBox.isSelected()) {
                if (path.substring(path.lastIndexOf('\\')).contains("work")) {
                    working = path;
                    full="";
                }else if (path.substring(path.lastIndexOf('\\')).contains("full")){
                    full = path;
                    working =path.substring(0,path.lastIndexOf('\\')+1)+
                            path.substring(path.lastIndexOf('\\')).replaceAll("full","work");
                }else {
                    full = path.substring(0,path.lastIndexOf('\\')+1)+
                            "full "+path.substring(path.lastIndexOf('\\')+1);
                    working = path.substring(0,path.lastIndexOf('\\')+1)+
                            "work "+path.substring(path.lastIndexOf('\\')+1);;
                }
            }
            if (!checkBox.isSelected()){
                ch = new JFileChooser(path);
                ch.setFileFilter(filter);
                ch.setDialogTitle("Save to...");
                returnVal = ch.showSaveDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    path = ch.getCurrentDirectory().getAbsolutePath();
                    full = path + "\\full " + ch.getSelectedFile().getName() + ".srt";
                    working =path + "\\work " + ch.getSelectedFile().getName() + ".srt";
                }
            }
            outfull=new File(full);
            outWorking=new File(working);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outWorking))) {
                for (int i = 0; i < subtitles.size(); i++) {
                    if (!timing.get(i).isEmpty()) {
                        writer.write("" + (i + 1) + "\n");
                        writer.write(timing.get(i) + "\n");
                        writer.write(subtitles.get(i) + "\n\n");
                    } else
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outfull))) {
                for (int i = 0; i < subtitles.size(); i++) {
                    writer.write("" + (i + 1) + "\n");
                    if (!timing.get(i).isEmpty())
                        writer.write(timing.get(i) + "\n");
                    writer.write(subtitles.get(i) + "\n\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(prefs))) {
            writer.write("" + connectionPort + "\n");
            writer.write(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setClosed() {
        windowClosed = true;
    }

}
