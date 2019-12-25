package com.ganada.silsiganmetro.laboratory;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.ganada.silsiganmetro.R;
import com.ganada.silsiganmetro.ResourceUtil;
import com.ganada.silsiganmetro.listitem.LineStation;
import com.ganada.silsiganmetro.listitem.TrainList;
import com.ganada.silsiganmetro.real.Realtime;
import com.ganada.silsiganmetro.util.DBManager;
import com.ganada.silsiganmetro.util.LineManager;
import com.ganada.silsiganmetro.util.ThemeManager;
import com.ganada.silsiganmetro.view.CustomTitlebar;
import com.ganada.silsiganmetro.view.MiniTrain;
import com.ganada.silsiganmetro.view.RefreshButton;
import com.ganada.silsiganmetro.view.TrainLocation;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LineActivity extends Activity {

    private SharedPreferences mPref;
    private SharedPreferences.Editor mPrefEdit;

    LineManager lineManager;
    ThemeManager themeManager;
    DBManager dbManager;
    LineType lineType;
    StationAdapter adapterSingle;
    DoubleStationAdapter adapterDouble;

    List<LineStation> arrStation = new ArrayList<>();
    List<TrainList> arrTrain = new ArrayList<>();
    HashMap<String, LineStation> maps = new HashMap<>();

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btnRefresh)
    RefreshButton btnRefresh;
    @BindView(R.id.titleBar)
    CustomTitlebar titleBar;

    int lineNumber;
    int trainHeight;
    int railWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        ButterKnife.bind(this);

        lineManager = new LineManager(getApplicationContext());
        themeManager = new ThemeManager(getApplicationContext());
        dbManager = new DBManager(getApplicationContext());

        mPref = getSharedPreferences("Pref1", 0);
        mPrefEdit = mPref.edit();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int screenWidth = size.x;

        lineNumber = getIntent().getIntExtra(MetroConstant.KEY_LINE_NUMBER, LineManager.LINE_2);
        arrStation = lineManager.getStationArray(lineNumber);
        lineType = LineType.getLine(lineNumber);

        Window window = getWindow();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), lineType.getColorId()));
        }
        titleBar.setText(lineType.getLineName());
        titleBar.setBackgroundColorById(lineType.getColorId());

        adapterSingle = new StationAdapter(arrStation, themeManager, lineNumber, ResourceUtil.toPixel(getApplicationContext(), themeManager.getListHeight()), ResourceUtil.toPixel(getApplicationContext(), themeManager.getListSepHeight()));
        adapterDouble = new DoubleStationAdapter(arrStation, themeManager, lineNumber, ResourceUtil.toPixel(getApplicationContext(), themeManager.getListHeight()), ResourceUtil.toPixel(getApplicationContext(), themeManager.getListSepHeight()));

        switch(lineNumber) {
            case LineManager.LINE_1:
            case LineManager.LINE_9:
            case LineManager.LINE_GONGHANG:
                recyclerView.setAdapter(adapterDouble);
                break;

            default:
                recyclerView.setAdapter(adapterSingle);
        }

        for(LineStation station : arrStation) {
            maps.put(station.number, station);
        }

        refreshTrain();

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshTrain();
            }
        });

    }

    public void refreshTrain() {
        new TrainAsync(this).execute(lineType.getLineName());
    }

    private static class TrainAsync extends AsyncTask<String, String, String> {

        HashMap<String, TrainList> map = new HashMap<>();

        int locHeight = 0;

        private final WeakReference<LineActivity> ref;
        Realtime rt;
        ThemeManager tm;
        LineManager lineManager;

        TrainAsync(LineActivity context) {
            ref = new WeakReference<>(context);
            tm = new ThemeManager(context);
            lineManager = new LineManager(context);
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LineActivity activity = ref.get();
            if(activity == null || activity.isFinishing()) return;

            activity.btnRefresh.startAnimation();
            rt = new Realtime();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);


            LineActivity activity = ref.get();
            if(activity == null || activity.isFinishing()) return;

            activity.arrTrain.clear();

            activity.arrTrain = rt.getTrainList(activity.getApplicationContext(), values[0]);

            switch(activity.lineNumber) {
                case LineManager.LINE_1:
                case LineManager.LINE_9:
                case LineManager.LINE_GONGHANG:
                    activity.adapterDouble.setTrains(activity.arrTrain, activity.trainHeight);
                    break;

                default:
                    activity.adapterSingle.setTrains(activity.arrTrain, activity.trainHeight);
            }
            //activity.testTrain.clearView();

            /*
            for(int i = 0; i < activity.arrStation.size(); i++) {

                locHeight += activity.arrStation.get(i).posY;

                for(int j = 0; j < activity.arrTrain.size(); j++) {
                    if(activity.arrStation.get(i).number.equals(activity.arrTrain.get(j).stationNo)) {
                        activity.testTrain.addTrain(
                                tm.getListHeight(),
                                activity.trainHeight,
                                lineManager.getTrainDst(activity.arrTrain.get(j).trainDst),
                                activity.arrTrain.get(j).trainNo,
                                activity.arrTrain.get(j).trainSttus,
                                activity.arrTrain.get(j).updown,
                                activity.arrStation.get(i).posY,
                                activity.arrTrain.get(j).express,
                                activity.lineNumber
                        );
                    }
                }
            }
            */

//            for(int i = 0; i < activity.arrTrain.size(); i++) {
//                if(activity.maps.get(activity.arrTrain.get(i).stationNo) != null) {
//                    activity.testTrain.addTrain(
//                            tm.getListHeight(),
//                            activity.trainHeight,
//                            (RelativeLayout.LayoutParams) activity.sampleUp.getLayoutParams(),
//                            (RelativeLayout.LayoutParams) activity.sampleDown.getLayoutParams(),
//                            activity.arrTrain.get(i).trainDst,
//                            activity.arrTrain.get(i).trainNo,
//                            activity.arrTrain.get(i).trainSttus,
//                            activity.arrTrain.get(i).updown,
//                            activity.maps.get(activity.arrTrain.get(i).stationNo).posY,
//                            activity.arrTrain.get(i).stationNo
//                    );
//                }
//            }
        }

        @Override
        protected String doInBackground(String... params) {

            StringBuilder sb = new StringBuilder();

            if(params != null){
                for(String s : params){
                    sb.append(s);
                }
            }

            String result = rt.getLine(sb.toString());
            if(!result.equals("")) {
                publishProgress(result);
            }


            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            final LineActivity activity = ref.get();
            if(activity == null || activity.isFinishing()) return;

            activity.btnRefresh.stopAnimation();
            Log.e("arrTrain", activity.arrTrain.size() + "");
            Log.e("locHeight", locHeight + "");

            switch(activity.lineNumber) {
                case LineManager.LINE_1:
                case LineManager.LINE_9:
                case LineManager.LINE_GONGHANG:
                    activity.adapterDouble.notifyDataSetChanged();
                    break;

                default:
                    activity.adapterSingle.notifyDataSetChanged();
            }

            /*
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) activity.testTrain.getLayoutParams();
            params.height = locHeight;
            activity.testTrain.setLayoutParams(params);
            */
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }


    }


}
