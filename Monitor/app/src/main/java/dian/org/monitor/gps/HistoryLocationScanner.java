package dian.org.monitor.gps;

import android.content.Context;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 这个类可以用来浏览地理位置数据，也就是{@link LocationDB}里面的数据。
 */
public class HistoryLocationScanner {
    private static final String LOG_TAG = "HistoryLocationScanner";

    /**
     * 一个数据库
     */
    private LocationDB db;
    /**
     *
     */
    private long date;

    private long ONE_DAY_TIME = 24 * 3600 * 1000;

    private boolean isDateSet = false;

    public HistoryLocationScanner(Context context) {
        db = LocationDB.getInstance(context);
    }

    /**
     * 返回当前日期的数据
     *
     * @return
     */
    public ArrayList<OneLocationRecord> getLocationRecordData(int patrol_id, String project_name) {
        return db.getLocationsInaProject(patrol_id, project_name);
    }

}
