import com.eeeeeric.mpc.hc.api.TimeCode;
import com.eeeeeric.mpc.hc.api.TimeCodeException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.*;

public class Main {

    static boolean windowClosed = false;
    static File prefs = new File("prefs.txt");

    public static void main(String[] args) {

        int connectionPort = -1;
        String intputPort = "";

        String path = "";
        String MPCPath="";
        int returnVal;
        ArrayList<String> settings = new ArrayList<>();
        List<Object> values;
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
            if (settings.size()>1)
                MPCPath=settings.get(1);
            if (settings.size()>2)
                path = settings.get(2);
        }

        while (true) {
            intputPort = JOptionPane.showInputDialog(null, "Enter the number port on which the player listens", intputPort);
            try {
                if (intputPort == null)
                    break;
                connectionPort = Integer.parseInt(intputPort);
                values=activateFileChooser(MPCPath,"Locate the Media Player Classic program","exe");
                MPCPath=(String)values.get(1);
                Runtime.getRuntime().exec(MPCPath, null, new File(MPCPath.substring(0,MPCPath.lastIndexOf("\\"))));
                ClassicPlayerUse.initConnection(connectionPort);
                ClassicPlayerUse.mpc.seek(new TimeCode("00:00:00"));
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showConfirmDialog(null, "The number format isn't correct, please retry",
                        "Erroneous number", JOptionPane.DEFAULT_OPTION);
                intputPort = "";
            } catch (IOException | TimeCodeException e) {
                JOptionPane.showConfirmDialog(null,
                        "Either the Media Player Classic is offline or the port isn't correct.", "Error", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        if (intputPort == null) {
            JOptionPane.showConfirmDialog(null, "The Program is terminated", "Exit", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }

        ArrayList<String> subtitles = new ArrayList<>();
        ArrayList<String> timing = new ArrayList<>();

        values = activateFileChooser(path, "Choose subtitle file to edit", "srt", "txt");
        returnVal=(int)values.get(0);
        path=(String) values.get(1);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            //path = ch.getSelectedFile().getPath();
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
            String full = "", working = "";
            if (checkBox.isSelected()) {
                if (path.substring(path.lastIndexOf('\\')).contains("work")) {
                    working = path;
                    full = "";
                } else if (path.substring(path.lastIndexOf('\\')).contains("full")) {
                    full = path;
                    working = path.substring(0, path.lastIndexOf('\\') + 1) +
                            path.substring(path.lastIndexOf('\\')).replaceAll("full", "work");
                } else {
                    full = path.substring(0, path.lastIndexOf('\\') + 1) +
                            "full " + path.substring(path.lastIndexOf('\\') + 1);
                    working = path.substring(0, path.lastIndexOf('\\') + 1) +
                            "work " + path.substring(path.lastIndexOf('\\') + 1);
                    ;
                }
            }
            if (!checkBox.isSelected()) {
                values=activateFileChooser(path, "Save to...");
                returnVal=(int)values.get(0);
                path=(String)values.get(2);
                String middleName=(String) values.get(3);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    full = path + "\\full " + middleName + ".srt";
                    working = path + "\\work " + middleName + ".srt";
                }
            }
            outfull = new File(full);
            outWorking = new File(working);
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
            writer.write(MPCPath+"\n");
            writer.write(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setClosed() {
        windowClosed = true;
    }

    static List<Object> activateFileChooser(String path, String title, String... fileExtentions) {
        JFileChooser ch=new JFileChooser(path);
        ch.setDialogTitle(title);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Subtitle files", fileExtentions);
        ch.setFileFilter(filter);
        return Arrays.asList(ch.showOpenDialog(null), ch.getSelectedFile().getPath(),
                ch.getSelectedFile().getName());
    }
}