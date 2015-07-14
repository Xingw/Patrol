package dian.org.monitor.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import java.util.List;

import dian.org.monitor.Constant;
import dian.org.monitor.R;
import dian.org.monitor.touritem.TourItem;

/**
 * Created by admin on 2015/7/14.
 */
public class PhotoShow extends Activity {
    private static final String TAG = "PhotoShow";
    private PhotoLocationDB pdb;
    private double longitude;
    private double latitude;
    TourItem tourItem;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_show);
        //返回键
        ivBack = (ImageView) findViewById(R.id.id_iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pdb = PhotoLocationDB.getInstance(this);
        Intent intent = getIntent();
        tourItem = (TourItem) intent.getSerializableExtra(Constant.INTENT_KEY_DATA_TOUR_ITEM);
        longitude=(Double) intent.getSerializableExtra(Constant.LATLNG_LONGITUDE);
        latitude=(Double) intent.getSerializableExtra(Constant.LATLNG_LATITUDE);
        List<String> str = pdb.getPhoto(longitude, latitude, tourItem.getTourNumber(), tourItem.getPrjName());
        GridView gridView = (GridView) findViewById(R.id.picture_show_gridview);
        ListAdapter imageadapter = new ImageAdapter(this,str);
        gridView.setAdapter(imageadapter);
    }
}
