package com.xunce.patrol;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.xunce.patrol.R;


/**
 * Created by ssthouse on 2015/6/9.
 */
public class SettingAty extends Activity {

    /**
     * 返回ImageView
     */
    private ImageView ivBack;
    /**
     * 服务器Server
     */
    private Spinner spServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_aty);
        //透明顶栏
        com.xunce.patrol.style.TransparentStyle.setAppToTransparentStyle(this, getResources().getColor(R.color.blue_level0));
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        ivBack = (ImageView) findViewById(R.id.id_iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        spServer = (Spinner) findViewById(R.id.id_sp_server);
        spServer.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_server_item,
                R.id.id_tv_server_content,
                getResources().getStringArray(R.array.spinner_server_array)));
        //设置初始时Spinner的选项
        if (com.xunce.patrol.util.PreferenceManager.getServerNumber(this) != -1) {
            spServer.setSelection(com.xunce.patrol.util.PreferenceManager.getServerNumber(this));
        }
        spServer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //更改服务器的preference
                TextView tv = (TextView) view.findViewById(R.id.id_tv_server_content);
                com.xunce.patrol.util.PreferenceManager.changeServerAddr(SettingAty.this, tv.getText().toString());
                com.xunce.patrol.util.PreferenceManager.changeServerNumber(SettingAty.this, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
