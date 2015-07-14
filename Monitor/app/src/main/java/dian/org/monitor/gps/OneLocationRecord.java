package dian.org.monitor.gps;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 一个单独的location的数据
 */
public class OneLocationRecord {

    /**
     * 表示生成位置时的时间点。
     */
    private String date;

    private long time;

    /**
     * 经度
     */
    private double longitude;

    /**
     * 纬度
     */
    private double latitude;

    /**
     * 位置对应的地址名称
     */
    //private String addrName;

    /**
     * 是否有照片在此地拍摄
     */
    private boolean is_photo_there;

    /**
     * 巡视ID
     */
    private int patrol_name;

    /**
     * 工程名
     */
    private String project_name;

    //getter---and---setter----------------------------------------------------
    public int getPatrol_name() {
        return patrol_name;
    }

    public void setPatrol_name(int patrol_name) {
        this.patrol_name = patrol_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(Date time) {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.date = format.format(time);
    }
    public void setDate(String date) {
        this.date = date;
    }
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean is_photo_there() {
        return is_photo_there;
    }

    public void set_photo_there(boolean is_first_of_line) {
        this.is_photo_there = is_first_of_line;
    }

    public void setTime(long time){this.time=time;}

    public long getTime(){return time;}

    public String getProject_name(){return project_name;}

    public void setProject_name(String project_name){this.project_name=project_name;}
}
