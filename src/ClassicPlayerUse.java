
import com.eeeeeric.mpc.hc.api.MediaPlayerClassicHomeCinema;
import com.eeeeeric.mpc.hc.api.WMCommand;

import java.io.IOException;

public class ClassicPlayerUse{

    static MediaPlayerClassicHomeCinema mpc;

    public static void initConnection(int port){
        mpc=new MediaPlayerClassicHomeCinema("127.0.0.1",port);
    }

    public static String formatTime(String time){
        long total=Long.parseLong(time);
        String output="";
        String temp;
        temp= String.format(",%03d", total%1000);
        total/=1000;
        output+=temp;
        temp=String.format(":%02d",total%60);
        total/=60;
        output=temp+output;
        temp=String.format("%02d:%02d",total/60,total%60);
        output=temp+output;
        return output;
    }

    public static String formatTime(long time){
        long total=time;
        String output="";
        String temp;
        temp= String.format(",%03d", total%1000);
        total/=1000;
        output+=temp;
        temp=String.format(":%02d",total%60);
        total/=60;
        output=temp+output;
        temp=String.format("%02d:%02d",total/60,total%60);
        output=temp+output;
        return output;
    }

    public static long allToMillis(String time){
        String[] nums;
        nums = time.split("[:,]");
        long total=Long.parseLong(nums[0]);
        total=total*60+Long.parseLong(nums[1]);
        total=total*60+Long.parseLong(nums[2]);
        total=total*1000+Long.parseLong(nums[3]);
        return total;
    }

    public static String captureTime(){
        try {
            return (mpc.getVariables().get("position"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void seekToTime(String time){
        try {
            mpc.execute(WMCommand.SEEK,new MediaPlayerClassicHomeCinema.KeyValuePair("position",time));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
