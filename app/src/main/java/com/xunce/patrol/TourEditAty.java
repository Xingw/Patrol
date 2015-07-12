package com.xunce.patrol;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.xunce.patrol.touritem.TourItem;

import java.io.File;
import java.util.Calendar;


/**
 * Created by ssthouse on 2015/6/10.
 * 开启该Activity需要---传入一个TourItem
 */
public class TourEditAty extends Activity {
    private static final String TAG = "TourEditAty*******";

    //requestcode**************************
    private static final int REQUEST_CODE_WEATHER_STATE = 1001;

    private static final int REQUEST_CODE_SUPPORT_STRUCT = 1002;

    private static final int REQUEST_CODE_CONSTRUCT_STATE = 1003;

    private static final int REQUEST_CODE_SURROUND_ENV = 1004;

    private static final int REQUEST_CODE_MONITOR_FACILITY = 1005;
    //*********************************************

    /**
     * 修改的数据
     */
    private com.xunce.patrol.touritem.TourItem tourItem;

    private TextView tvNumber;

    private TextView tvObserver;

    private TextView tvDate;

    private TextView tvWeatherState;

    private TextView tvSupportStruct;

    private TextView tvConstructState;

    private TextView tvSurroundEnv;

    private TextView tvMonitorFacility;

    private TextView tvMonitorPatroltrack;

    private TextView tvPrjName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.xunce.patrol.R.layout.tour_edit_aty);
        //透明顶栏
        com.xunce.patrol.style.TransparentStyle.setAppToTransparentStyle(this, getResources().getColor(com.xunce.patrol.R.color.blue_level0));
        //获取TourItem数据
        Intent intent = getIntent();
        tourItem = (com.xunce.patrol.touritem.TourItem) intent.getSerializableExtra(Constant.INTENT_KEY_DATA_TOUR_ITEM);
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        //返回按钮
        ImageView ivBack = (ImageView) findViewById(com.xunce.patrol.R.id.id_iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提示对话框
                showExitDialog();
            }
        });

        //ActionBar工程名
        TextView tvAbPrjName = (TextView) findViewById(com.xunce.patrol.R.id.id_tv_ab_prjName);
        tvAbPrjName.setText(tourItem.getTourInfo().getPrjName());

        //保存TextView
        TextView tvSave = (TextView) findViewById(com.xunce.patrol.R.id.id_tv_save);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e(TAG, "我点击了保存");
                //打开对应数据库
                SQLiteDatabase db = com.xunce.patrol.db.DbFileManager.getDb(tourItem);
                com.xunce.patrol.db.TourDbHelper.setTourItem(db, tourItem);
                //保存数据
                finish();
            }
        });

        //工程名
        tvPrjName = (TextView) findViewById(com.xunce.patrol.R.id.id_tv_prjName);
        tvPrjName.setText(tourItem.getTourInfo().getPrjName());

        //观测次数
        tvNumber = (TextView) findViewById(com.xunce.patrol.R.id.id_tv_number);
        tvNumber.setText("第 " + tourItem.getTourInfo().getTourNumber() + " 次");
        LinearLayout llNumber = (LinearLayout) findViewById(com.xunce.patrol.R.id.id_ll_number);
        llNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberEditDialog();
            }
        });

        //观测者
        tvObserver = (TextView) findViewById(com.xunce.patrol.R.id.id_tv_observer);
        tvObserver.setText(tourItem.getTourInfo().getObserver());
        LinearLayout llObserver = (LinearLayout) findViewById(com.xunce.patrol.R.id.id_ll_observer);
        llObserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showObserverEditDialog();
            }
        });

        //观测日期
        tvDate = (TextView) findViewById(com.xunce.patrol.R.id.id_tv_date);
        tvDate.setText(com.xunce.patrol.util.StringUtil.getFormatDate(com.xunce.patrol.util.StringUtil.getCalendarFromTimeInMiles(
                tourItem.getTourInfo().getTimeInMilesStr())));
        LinearLayout llDate = (LinearLayout) findViewById(com.xunce.patrol.R.id.id_ll_date);
        llDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        //自然条件
        tvWeatherState = (TextView) findViewById(com.xunce.patrol.R.id.id_tv_weather_state);
        LinearLayout llWeatherState = (LinearLayout) findViewById(com.xunce.patrol.R.id.id_ll_weather_state);
        llWeatherState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TourEditAty.this, com.xunce.patrol.touritem.WeatherStateAty.class);
                intent.putExtra("data", tourItem.getWeatherState());
                if (tourItem.getWeatherState() == null) {
                    Log.e(TAG, "天啊...我传递给WeatherStateAty的数据竟然是空的!!!");
                }
                startActivityForResult(intent, REQUEST_CODE_WEATHER_STATE);
            }
        });

        //支护结构
        tvSupportStruct = (TextView) findViewById(com.xunce.patrol.R.id.id_tv_support_struct);
        LinearLayout llSupportStruct = (LinearLayout) findViewById(com.xunce.patrol.R.id.id_ll_support_struct);
        llSupportStruct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TourEditAty.this, com.xunce.patrol.touritem.SupportStructAty.class);
                intent.putExtra(Constant.INTENT_KEY_DATA_TOUR_ITEM, tourItem);
                startActivityForResult(intent, REQUEST_CODE_SUPPORT_STRUCT);
            }
        });

        //施工工况
        tvConstructState = (TextView) findViewById(com.xunce.patrol.R.id.id_tv_construct_state);
        LinearLayout llConstructState = (LinearLayout) findViewById(com.xunce.patrol.R.id.id_ll_construct_state);
        llConstructState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TourEditAty.this, com.xunce.patrol.touritem.ConstructStateAty.class);
                intent.putExtra(Constant.INTENT_KEY_DATA_TOUR_ITEM, tourItem);
                startActivityForResult(intent, REQUEST_CODE_CONSTRUCT_STATE);
            }
        });

        //周边情况
        tvSurroundEnv = (TextView) findViewById(com.xunce.patrol.R.id.id_tv_surround_env);
        LinearLayout llSurroundEnv = (LinearLayout) findViewById(com.xunce.patrol.R.id.id_ll_surround_env);
        llSurroundEnv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TourEditAty.this, com.xunce.patrol.touritem.SurroundEnvAty.class);
                intent.putExtra(Constant.INTENT_KEY_DATA_TOUR_ITEM, tourItem);
                startActivityForResult(intent, REQUEST_CODE_SURROUND_ENV);
            }
        });

        //监测设施
        tvMonitorFacility = (TextView) findViewById(com.xunce.patrol.R.id.id_tv_monitor_facility);
        LinearLayout llMonitorFacility = (LinearLayout) findViewById(com.xunce.patrol.R.id.id_ll_monitor_facility);
        llMonitorFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TourEditAty.this, com.xunce.patrol.touritem.MonitorFacilityAty.class);
                intent.putExtra(Constant.INTENT_KEY_DATA_TOUR_ITEM, tourItem);
                startActivityForResult(intent, REQUEST_CODE_MONITOR_FACILITY);
            }
        });

        //巡视轨迹
        tvMonitorPatroltrack = (TextView) findViewById(com.xunce.patrol.R.id.id_tv_monitor_patroltrack);
        LinearLayout llMonitorPatroltrack = (LinearLayout) findViewById(com.xunce.patrol.R.id.id_ll_monitor_patroltrack);
        llMonitorPatroltrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TourEditAty.this, com.xunce.patrol.gps.GpsTestAty.class);
                intent.putExtra(Constant.INTENT_KEY_DATA_TOUR_ITEM, tourItem);
                startActivityForResult(intent, REQUEST_CODE_MONITOR_FACILITY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.e(TAG, "我回调了----tourEditAty");
        switch (requestCode) {
            case REQUEST_CODE_WEATHER_STATE:
                Log.e(TAG, "我回调了---TourEditActivity----weatherState");
                if (resultCode == Constant.RESULT_CODE_SAVE) {
                    //更新weatherState数据
                    com.xunce.patrol.touritem.WeatherState weatherState = (com.xunce.patrol.touritem.WeatherState) data.getSerializableExtra("data");
                    tourItem.setWeatherState(weatherState);
                    Log.e(TAG, "我改变了weatherState");
                } else if (resultCode == Constant.RESULT_CODE_CANCEL) {
                    //do nothing
                }
                break;
            case REQUEST_CODE_SUPPORT_STRUCT:
                if (resultCode == Constant.RESULT_CODE_SAVE) {
                    //更新数据---如果不为空的话
                    com.xunce.patrol.touritem.TourItem tourItem = (TourItem) data.getSerializableExtra(
                            Constant.INTENT_KEY_DATA_TOUR_ITEM);
                    if (tourItem != null) {
                        this.tourItem = tourItem;
                    }
                    Log.e(TAG, "我改变了SupportStruct");
                }
                break;
            case REQUEST_CODE_CONSTRUCT_STATE:
                if (resultCode == Constant.RESULT_CODE_SAVE) {
                    //更新数据---如果不为空的话
                    com.xunce.patrol.touritem.TourItem tourItem = (TourItem) data.getSerializableExtra(
                            Constant.INTENT_KEY_DATA_TOUR_ITEM);
                    if (tourItem != null) {
                        this.tourItem = tourItem;
                    }
                    Log.e(TAG, "我改变了construct_state");
                }
                break;
            case REQUEST_CODE_SURROUND_ENV:
                if (resultCode == Constant.RESULT_CODE_SAVE) {
                    //更新数据---如果不为空的话
                    com.xunce.patrol.touritem.TourItem tourItem = (TourItem) data.getSerializableExtra(
                            Constant.INTENT_KEY_DATA_TOUR_ITEM);
                    if (tourItem != null) {
                        this.tourItem = tourItem;
                    }
                    Log.e(TAG, "我改变了surround_env");
                }
                break;
            case REQUEST_CODE_MONITOR_FACILITY:
                if (resultCode == Constant.RESULT_CODE_SAVE) {
                    //更新数据---如果不为空的话
                    com.xunce.patrol.touritem.TourItem tourItem = (TourItem) data.getSerializableExtra(
                            Constant.INTENT_KEY_DATA_TOUR_ITEM);
                    if (tourItem != null) {
                        this.tourItem = tourItem;
                    }
                    Log.e(TAG, "我改变了monitor_facility");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    //Dialog****************************************************************************************

    /**
     * 显示确认退出的Dialog
     */
    private void showExitDialog() {
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
        dialogBuilder.withTitle("确认退出?")             //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")
                .withDividerColor("#11000000")
                .withMessage("退出将不会保存当前编辑的数据!")//.withMessage(null)  no Msg
                .withMessageColor("#FFFFFFFF")
                .withDialogColor(getResources().getColor(com.xunce.patrol.R.color.dialog_color))
                .withEffect(Effectstype.Slidetop)       //def Effectstype.Slidetop
                .withButton1Text("确认")                 //def gone
                .withButton2Text("取消")                 //def gone
                .isCancelableOnTouchOutside(false)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        finish();
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                })
                .withDuration(400)
                .show();
    }

    /**
     * 显示巡视次数选择Dialog
     */
    private void showNumberEditDialog() {
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
        dialogBuilder.withTitle("观测次数")             //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")
                .withDividerColor("#11000000")
                .withMessage(null)                      //.withMessage(null)  no Msg
                .withDialogColor(getResources().getColor(com.xunce.patrol.R.color.dialog_color))
                .withEffect(Effectstype.Slidetop)       //def Effectstype.Slidetop
                .withButton1Text("确认")                 //def gone
                .withButton2Text("取消")                 //def gone
                .isCancelableOnTouchOutside(false)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //修改数据
                        EditText etNumber = (EditText) dialogBuilder.
                                findViewById(com.xunce.patrol.R.id.id_et_number_dialog);
                        //判断是否为空
                        if (!etNumber.getText().toString().equals("")) {
                            int number = Integer.parseInt(etNumber.getText().toString());
                            if (number > 0) {
                                //判断当前数字的数据库是否存在
                                File tourDbFIle = new File(com.xunce.patrol.db.DbFileManager.DATABASE_PATH
                                        + tourItem.getTourInfo().getPrjName() + "/" + number);
                                if (tourDbFIle.exists()) {
                                    com.xunce.patrol.util.ToastUtil.showToast(TourEditAty.this, "该观测次数意存在,不可重复!");
                                } else {
                                    //TODO 更改数据库名称---如果当前名称的数据库存在---不可完成更改
                                    //更改文件名
                                    com.xunce.patrol.db.DbFileManager.changeDbFileName(com.xunce.patrol.db.DbFileManager.DATABASE_PATH
                                                    + tourItem.getTourInfo().getPrjName() + "/",
                                            tourItem.getTourInfo().getTourNumber() + "", number + "");
                                    //更新界面
                                    tvNumber.setText("第 " + number + " 次");
                                    //更新数据
                                    tourItem.getTourInfo().setTourNumber(number);
                                    dialogBuilder.dismiss();
                                }
                            } else {
                                com.xunce.patrol.util.ToastUtil.showToast(TourEditAty.this, "观测次数不可为0!");
                            }
                        } else {
                            com.xunce.patrol.util.ToastUtil.showToast(TourEditAty.this, "观测次数不可为空!");
                        }
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                })
                .withDuration(400)
                .setCustomView(com.xunce.patrol.R.layout.tour_number_edit_dialog, this)
                .show();
    }

    /**
     * 显示观测者修改Dialog
     */
    private void showObserverEditDialog() {
        //新建dialog
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
        //确定监听器
        View.OnClickListener sureListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //修改数据
                EditText etObserver = (EditText) dialogBuilder.
                        findViewById(com.xunce.patrol.R.id.id_et_observer_dialog);
                String strObserver = etObserver.getText().toString();
                //更新界面
                if (!strObserver.equals("")) {
                    if (strObserver.contains(" ")) {
                        Toast.makeText(TourEditAty.this, "观测人不可有空格!", Toast.LENGTH_SHORT).
                                show();
                    } else {
                        //更新数据
                        tourItem.getTourInfo().setObserver(strObserver);
                        //更新界面
                        tvObserver.setText(strObserver);
                        dialogBuilder.dismiss();
                    }
                }
            }
        };
        View.OnClickListener cancleListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        };
        dialogBuilder.withTitle("观测者")             //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")
                .withDividerColor("#11000000")
                .withMessage(null)                      //.withMessage(null)  no Msg
                .withDialogColor(getResources().getColor(com.xunce.patrol.R.color.dialog_color))
                .withEffect(Effectstype.Slidetop)       //def Effectstype.Slidetop
                .withButton1Text("确认")                 //def gone
                .withButton2Text("取消")                 //def gone
                .isCancelableOnTouchOutside(false)
                .setButton1Click(sureListener)
                .setButton2Click(cancleListener)
                .withDuration(400)
                .setCustomView(com.xunce.patrol.R.layout.tour_observer_edit_dialog, this)
                .show();
    }

    /**
     * 显示Date选择Dialog
     */
    private void showDatePickerDialog() {
        //新建dialog
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
        //确定监听器
        View.OnClickListener sureListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取时间
                CalendarView calendarView = (CalendarView) dialogBuilder.
                        findViewById(com.xunce.patrol.R.id.id_cv_date_picker);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(calendarView.getDate());
                //更新界面
                String strDate = com.xunce.patrol.util.StringUtil.getFormatDate(calendar);
                tvDate.setText(strDate);
                //更新数据
                tourItem.getTourInfo().setTimeInMilesStr(calendar.getTimeInMillis() + "");
                dialogBuilder.dismiss();
            }
        };
        View.OnClickListener cancleListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        };
        dialogBuilder.withTitle("观测日期")             //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")
                .withDividerColor("#11000000")
                .withMessage(null)                      //.withMessage(null)  no Msg
                .withDialogColor(getResources().getColor(com.xunce.patrol.R.color.dialog_color))
                .withEffect(Effectstype.Slidetop)       //def Effectstype.Slidetop
                .withButton1Text("确认")                 //def gone
                .withButton2Text("取消")                 //def gone
                .isCancelableOnTouchOutside(false)
                .setButton1Click(sureListener)
                .setButton2Click(cancleListener)
                .withDuration(400)
                .setCustomView(com.xunce.patrol.R.layout.tour_date_picker_dialog, this)
                .show();
        //初始化dialogBuilder界面
        CalendarView calendarView = (CalendarView) dialogBuilder.findViewById(com.xunce.patrol.R.id.id_cv_date_picker);
        calendarView.setDate(Long.parseLong(tourItem.getTourInfo().getTimeInMilesStr()));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        com.xunce.patrol.gps.LocationTracker.stopWorking();
    }
}
