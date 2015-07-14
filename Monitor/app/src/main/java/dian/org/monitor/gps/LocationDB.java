package dian.org.monitor.gps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import java.util.ArrayList;

import dian.org.monitor.touritem.TourItem;


public class LocationDB extends SQLiteOpenHelper {
    private static final String LOG_TAG = "LocationDB";

    //数据库名---表名---数据名---
    private static final String DATABASE_NAME = "locationtracker.db";
    private static final String TABLE_LOCATION = "location";
    private static final String TIME = "time";
    private static final String DATE = "datetime";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String IS_PHOTO_THERE = "is_photo_there";
    private static final String PATROL_ID = "patrol_id";
    private static final String PROJECT = "project_name";

    //新建一个表
    String sql = "create table if not exists " + TABLE_LOCATION +
            " (_id integer primary key autoincrement, " + LONGITUDE + " double, " + LATITUDE + " double, " + TIME + " long, " +
            DATE + " varchar(50), " + IS_PHOTO_THERE + " integer, " + PATROL_ID + " int, " + PROJECT + " varchar(50))";
    /**
     * 两条线之间的最小时间间隔，单位：毫秒
     */
    private static final long MAX_TIME = 10000;
    /**
     * 判断两个不同点之间的最长间距，单位：米
     */
    private static final int MIN_DISTANCE = 5;
    private static double LastDistance = 20;
    /**
     * 单例
     */
    private static LocationDB instance = null;


    private static SQLiteDatabase db;

    /**
     * 私有的构造方法
     *
     * @param context
     */
    private LocationDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    /**
     * 获取唯一的单例
     *
     * @param context
     * @return
     */
    public static LocationDB getInstance(Context context) {
        if (instance == null) {
            instance = new LocationDB(context);
        }
        return instance;
    }

    private void initDataBase() {
        if (db == null) {
            db = getWritableDatabase();
        }
    }

    /**
     * 如果不存在创建历史Location的table
     */
    private void creatHistoryLocationTable() {
        //初始化db
        initDataBase();
        // sqlite 没有boolean类型的数据，IS_PHOTO_THERE 这个字段 1表示true，0表示false
        db.execSQL(sql);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    ////////////////////////////////////////////////////////////////////////////////////////////
    /// 对外接口
    /////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 记录一个位置信息
     *
     * @param time      位置的生成时间
     * @param longitude 经度
     * @param latitude  纬度
     */
    public void recordLocation(long time, double longitude, double latitude, int patrol_id, String data, String project_name) {
        //如果不存在就创建table
        creatHistoryLocationTable();
        Cursor cursor = db.query(TABLE_LOCATION, null, PATROL_ID + "=" + "'" + patrol_id + "' AND " + PROJECT + "=" + "'" + project_name + "'", null, null, null, TIME + " desc");
        // 数据库里面已经记录了数据
        if (cursor != null && cursor.moveToFirst()) {
            //long lastTime = cursor.getLong(cursor.getColumnIndex(TIME));
            // 每隔一段时间记录一个点
            //if (time - lastTime> MAX_TIME) {
            //超过最小距离，记录一个点
            Double NowDistance = Distance(longitude, latitude,
                    cursor.getDouble(cursor.getColumnIndex(LONGITUDE)),
                    cursor.getDouble(cursor.getColumnIndex(LATITUDE)));
            if (NowDistance > MIN_DISTANCE) {
                Log.i(LOG_TAG, "记录了一条数据，记录信息:" + longitude + "   " + latitude + "   " + patrol_id + "   " + data);
                recordNewLocation(longitude, latitude, false, patrol_id, data, time, project_name);
            }
            //}
            cursor.close();
        } else {
            //如果数据库为空---创建新的Location数据
            recordNewLocation(longitude, latitude, false, patrol_id, data, time, project_name);
            Log.i(LOG_TAG, "我创建了新的数据库，记录信息:" + longitude + "   " + latitude + "   " + patrol_id + "   " + data);
        }
    }

    /**
     * 记录一条新的时间线上的第一个点
     *
     * @param date      位置的生成时间
     * @param longitude 经度
     * @param latitude  纬度
     * @param isFirst   是否为一条线的第一个点
     */
    private void recordNewLocation(final double longitude, final double latitude, final Boolean isFirst,
                                   final int Patrol_name, final String date, final long time, final String project_name) {
        ContentValues cv = new ContentValues();
        int iIsFirst = isFirst ? 1 : 0;
        cv.put(DATE, date);
        cv.put(LONGITUDE, longitude);
        cv.put(LATITUDE, latitude);
        cv.put(IS_PHOTO_THERE, iIsFirst);
        cv.put(PATROL_ID, Patrol_name);
        cv.put(TIME, time);
        cv.put(PROJECT, project_name);
        db.insert(TABLE_LOCATION, null, cv);
    }

    /**
     * @param oneLocationRecord
     */
    public void recordLocation(OneLocationRecord oneLocationRecord) {
        recordLocation(oneLocationRecord.getTime(), oneLocationRecord.getLongitude(),
                oneLocationRecord.getLatitude(), oneLocationRecord.getPatrol_name(), oneLocationRecord.getDate(), oneLocationRecord.getProject_name());
    }

    /**
     * 查询某一巡查工程的路径
     *
     * @param patrol_id 同一工程的名字
     * @return
     */
    public ArrayList<OneLocationRecord> getLocationsInaProject(int patrol_id, String project_name) {
        creatHistoryLocationTable();
        ArrayList<OneLocationRecord> records = new ArrayList<OneLocationRecord>();
        Log.i(LOG_TAG, "这是工程：" + project_name + " 第" + patrol_id + "次巡查");
        Cursor cursor = db.query(TABLE_LOCATION, null, PATROL_ID + "=" + "'" + patrol_id + "' AND " + PROJECT + "=" + "'" + project_name + "'", null, null, null, TIME);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                OneLocationRecord record = new OneLocationRecord();
                record.setLongitude(cursor.getDouble(cursor.getColumnIndex(LONGITUDE)));
                record.setLatitude(cursor.getDouble(cursor.getColumnIndex(LATITUDE)));
                record.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                record.setPatrol_name(cursor.getInt(cursor.getColumnIndex(PATROL_ID)));
                record.set_photo_there(cursor.getInt(cursor.getColumnIndex(IS_PHOTO_THERE)) == 1);
                records.add(record);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return records;
    }

    /**
     * 调用百度API计算两点间距离
     *
     * @param longitude
     * @param latitude
     * @param nextlongitude
     * @param nextlatitude
     * @return
     */
    private double Distance(double longitude, double latitude, double nextlongitude, double nextlatitude) {
        double distance = DistanceUtil.getDistance(new LatLng(latitude, longitude), new LatLng(nextlatitude, nextlongitude));
        return distance;
    }

    /**
     * 删除指定ID记录的点
     *
     * @param patrol_id
     * @param project_name
     */
    public void Delete_this_id(int patrol_id, String project_name) {
        creatHistoryLocationTable();
        db.delete(TABLE_LOCATION, PATROL_ID + "=" + "'" + patrol_id + "' AND " + PROJECT + "=" + "'" + project_name + "'", null);
    }

    /**
     * 删除指定ID记录的点
     */
    public void Delete_this_id(TourItem tourItem) {
        Delete_this_id(tourItem.getTourNumber(), tourItem.getPrjName());
    }

    /**
     * 删除指定工程的数据
     *
     * @param project_name
     */
    public void Delete_this_Project(String project_name) {
        creatHistoryLocationTable();
        db.delete(TABLE_LOCATION, PROJECT + "=" + "'" + project_name + "'", null);
    }

    /**
     * 用于更新修改的巡检次数
     */
    public void Update_this_id(TourItem tourItem, int id) {
        creatHistoryLocationTable();
        ContentValues newValues = new ContentValues();
        newValues.put(PATROL_ID, id);
        db.update(TABLE_LOCATION, newValues, PATROL_ID + "=" + "'" + tourItem.getTourNumber() + "' AND " + PROJECT + "=" + "'" + tourItem.getPrjName() + "'", null);
    }

    /**
     * 用于照片获取当前位置信息
     *
     * @param patrol_id
     * @param project_name
     * @return
     */
    public LatLng getLocations(int patrol_id, String project_name) {
        creatHistoryLocationTable();
        double v1 = 0;
        double v2 = 0;
        Cursor cursor = db.query(TABLE_LOCATION, null, PATROL_ID + "=" + "'" + patrol_id + "' AND " + PROJECT + "=" + "'" + project_name + "'", null, null, null, TIME + " desc");
        if (cursor != null && cursor.moveToFirst()) {
            v1 = cursor.getDouble(cursor.getColumnIndex(LONGITUDE));
            v2 = cursor.getDouble(cursor.getColumnIndex(LATITUDE));
            while (cursor.moveToNext()) {
                if (cursor.getInt(cursor.getColumnIndex(IS_PHOTO_THERE)) == 1)
                    if (Distance(v1, v2, cursor.getDouble(cursor.getColumnIndex(LONGITUDE)), cursor.getDouble(cursor.getColumnIndex(LATITUDE))) < 20) {
                        v1 = cursor.getDouble(cursor.getColumnIndex(LONGITUDE));
                        v2 = cursor.getDouble(cursor.getColumnIndex(LATITUDE));
                    }
            }
            cursor.close();
        }
        ContentValues newValues = new ContentValues();
        newValues.put(IS_PHOTO_THERE, 1);
        db.update(TABLE_LOCATION, newValues, LONGITUDE + "=" + "'" + v1 + "' AND " + LATITUDE + "=" + "'" + v2 + "'", null);
        LatLng latlng = new LatLng(v2, v1);
        return latlng;
    }
}