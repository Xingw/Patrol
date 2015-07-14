package dian.org.monitor.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import dian.org.monitor.R;
import dian.org.monitor.util.PictureManager;

/**
 * Created by admin on 2015/7/14.
 */
public class ImageAdapter extends BaseAdapter {
    private static final String TAG = "TourLvAdapter*****";

    /**
     * 保存所有数据的List
     */
    private List<String> dataList;

    private Context mContext;

    private static final String strTvNumber = "巡检标号 : ";

    /**
     * 构造方法
     */
    public ImageAdapter(Context mContext, List<String> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }


    @Override
    public int getCount() {
//        Log.e(TAG, "我有"+dataList.size()+"个数据");
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.photo_lv_item, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.id_Ph_image);
            Bitmap bm = PictureManager.getSmallBitmap(dataList.get(position), 300, 300);
            //填充数据
            viewHolder.imageView.setImageBitmap(bm);
            //添加标签
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            //viewHolder.imageView = (ImageView) convertView.findViewById(R.id.id_Ph_image);
            Bitmap bm = PictureManager.getSmallBitmap(dataList.get(position), 300, 300);

            //填充数据
            viewHolder.imageView.setImageBitmap(bm);
        }
        return convertView;
    }

    class ViewHolder {
        public ImageView imageView;
    }
}
