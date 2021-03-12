import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.ArrayList;

public class Main {

    static boolean windowClosed = false;
    static File prefs=new File("prefs.txt");

    public static void main(String[] args) {

        int connectionPort;
        String intputPort="";

        String path = "";
        ArrayList <String> settings=new ArrayList<>();
        String temp;

        try(BufferedReader reader=new BufferedReader(new FileReader(prefs))){
            while ((temp=reader.readLine())!=null){
                settings.add(temp);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        if (settings.size()>0) {
            intputPort = settings.get(0);
            path = settings.get(1);
        }

        while (true) {
            intputPort=JOptionPane.showInputDialog(null, "Enter the number port on which the player listens",intputPort);
            try{
                connectionPort=Integer.parseInt(intputPort);
                break;
            }catch (NumberFormatException e){
                JOptionPane.showConfirmDialog(null,"The number format isn't correct, please retry");
                intputPort="";
            }
        }

        ClassicPlayerUse.initConnection(connectionPort);
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

        returnVal = JOptionPane.showConfirmDialog(null, "Save changes made to file?");
        if (returnVal == JOptionPane.OK_OPTION) {
            ch = new JFileChooser(path);
            ch.setFileFilter(filter);
            ch.setDialogTitle("Save to...");
            returnVal = ch.showSaveDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                path = ch.getCurrentDirectory().getAbsolutePath();
                File outfull = new File(path + "\\full " + ch.getSelectedFile().getName()+".srt");
                File outWorking = new File(path + "\\work " + ch.getSelectedFile().getName()+".srt");
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
        }

        try(BufferedWriter writer=new BufferedWriter(new FileWriter(prefs))){
            writer.write(""+connectionPort+"\n");
            writer.write(path);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void setClosed() {
        windowClosed = true;
    }

}
