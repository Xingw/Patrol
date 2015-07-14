package dian.org.monitor.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import dian.org.monitor.gps.OneLocationRecord;
import dian.org.monitor.touritem.TourItem;

/**
 * Created by admin on 2015/7/14.
 */
public class PhotoLocationDB extends SQLiteOpenHelper {
    private static final String LOG_TAG = "PhotoLocationDB";

    //数据库名---表名---数据名---
    private static final String DATABASE_NAME = "locationtracker.db";
    private static final String TABLE_LOCATION = "photo";
    private static final String ADDRESS = "address";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String PATROL_ID = "patrol_id";
    private static final String PROJECT = "project_name";
    //新建一个表
    String sql = "create table if not exists " + TABLE_LOCATION +
            " (_id integer primary key autoincrement, " + ADDRESS + " varchar(100), " +
            LONGITUDE + " double, " + LATITUDE + " double, " + PATROL_ID + " int, " + PROJECT + " varchar(50))";

    /**
     * 单例
     */
    private static PhotoLocationDB instance = null;
    private static SQLiteDatabase db;

    /**
     * 私有的构造方法
     *
     * @param context
     */
    private PhotoLocationDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    /**
     * 获取唯一的单例
     *
     * @param context
     * @return
     */
    public static PhotoLocationDB getInstance(Context context) {
        if (instance == null) {
            instance = new PhotoLocationDB(context);
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

    ////////////////////////////////////////////////////////////////
    ////////对外接口
    ////////////////////////////////////////////////////////////////

    /**
     * 记录照片数据
     * @param longitude
     * @param latitude
     * @param address
     * @param Patrol_name
     * @param project_name
     */
    public void recordNewPhoto(final double longitude, final double latitude, final String address, final int Patrol_name, final String project_name) {
        creatHistoryLocationTable();
        ContentValues cv = new ContentValues();
        cv.put(ADDRESS, address);
        cv.put(LONGITUDE, longitude);
        cv.put(LATITUDE, latitude);
        cv.put(PATROL_ID, Patrol_name);
        cv.put(PROJECT, project_name);
        db.insert(TABLE_LOCATION, null, cv);
    }

    /**
     * 查看照片
     * @param longitude
     * @param latitude
     * @param patrol_id
     * @param project_name
     * @return
     */
    public ArrayList<String> getPhoto(double longitude, double latitude, int patrol_id, String project_name) {
        creatHistoryLocationTable();
        ArrayList<String> strl = new ArrayList<String>();
        Cursor cursor = db.query(TABLE_LOCATION, null, LONGITUDE + "=" + "'" + longitude + "' AND " + LATITUDE + "=" + "'" + latitude + "' AND " + PATROL_ID + "=" + "'" + patrol_id + "' AND " + PROJECT + "=" + "'" + project_name + "'", null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String str=cursor.getString(cursor.getColumnIndex(ADDRESS));
                strl.add(str);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return strl;
    }

    /**
     * 删除这个ID的照片
     * @param patrol_id
     * @param project_name
     */
    public void Delete_this_id(int patrol_id, String project_name) {
        creatHistoryLocationTable();
        db.delete(TABLE_LOCATION, PATROL_ID + "=" + "'" + patrol_id + "' AND " + PROJECT + "=" + "'" + project_name + "'", null);
    }

    /**
     * 删除指定ID的照片
     */
    public void Delete_this_id(TourItem tourItem) {
        Delete_this_id(tourItem.getTourNumber(), tourItem.getPrjName());
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
}
