package com.ganada.silsiganmetro.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ganada.silsiganmetro.util.CalcDate;
import com.ganada.silsiganmetro.real.FavoriteInfo;
import com.ganada.silsiganmetro.real.GetType;
import com.ganada.silsiganmetro.util.LineManager;
import com.ganada.silsiganmetro.listitem.LineStation;
import com.ganada.silsiganmetro.listitem.ListTrain;
import com.ganada.silsiganmetro.R;
import com.ganada.silsiganmetro.view.RefreshButton;
import com.ganada.silsiganmetro.util.ThemeManager;
import com.ganada.silsiganmetro.view.TrainLabel;
import com.ganada.silsiganmetro.view.TrainRail;
import com.ganada.silsiganmetro.util.Units;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StationActivity extends Activity {

//    private SharedPreferences mPref;
//    private SharedPreferences.Editor mPrefEdit;
//
//    private MyAsyncTask myAsyncTask;
//    private TrainAsyncTask trainAsync;
//
//    LineManager lineManager;
//    ThemeManager tm;
//    ArrayList<ListTrain> arrTrainList;
//
//    View footer, viewUpLine, viewDownLine, viewRail;
//    LinearLayout layoutUp, layoutDown;
//    RelativeLayout layout_title;
//    TrainRail rail_up, rail_down;
//    LinearLayout layout_sta_back;
//    ImageButton btnBack, btnTimetable;
//    ImageView imgUpThis, imgUpPrev1, imgUpPrev2;
//    ImageView imgDownThis, imgDownPrev1, imgDownPrev2;
//    RefreshButton btnRefresh;
//    TextView txtUpDir, txtDownDir;
//    TextView txt_station;
//    TextView txt_circle;
//    TextView txtUpThis, txtUpPrev1, txtUpPrev2;
//    TextView txtDownThis, txtDownPrev1, txtDownPrev2;
//    TextView txtPrev, txtNext;
//
//    int MAX_XML = 0;
//    int time = 0;
//    int count = 0;
//    int iTheme = 0;
//    int iWeek = 1;
//    int i_line = 0;
//    String str_station_name;
//    String str_station_num;
//    String str_station_code = "201";
//    String str_line_num;
//
//    String[] xmlNumber;
//    String[] xmlDst;
//    String[] arrUpSta = new String[3];
//    String[] arrDownSta = new String[3];
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_station);
//
//        mPref = getSharedPreferences("Pref1", 0);
//        mPrefEdit = mPref.edit();
//        tm = new ThemeManager(getBaseContext());
//        lineManager = new LineManager(getApplicationContext());
//        iTheme = mPref.getInt("iTheme", 0);
//        time = mPref.getInt("timerefresh", 0) * 5;
//
//        Intent intent = getIntent();
//        str_station_name = intent.getStringExtra("str_station");
//        str_station_num = intent.getStringExtra("str_num");
//
//        /*intent = new Intent(StationActivity.this, TimetableActivity.class);
//        intent.putExtra("str_station", str_station_name);
//        intent.putExtra("str_num", str_station_num);
//        startActivity(intent);
//        finish();*/
//
//        btnRefresh = findViewById(R.id.btnRefresh);
//        btnBack = findViewById(R.id.btnBack);
//        btnTimetable = findViewById(R.id.btnTimetable);
//        txt_station = findViewById(R.id.txt_station);
//        txt_circle = findViewById(R.id.txt_circle);
//        txtPrev = findViewById(R.id.txtPrev);
//        txtNext = findViewById(R.id.txtNext);
//        layoutUp = findViewById(R.id.layoutUp);
//        layoutDown = findViewById(R.id.layoutDown);
//        layout_title = findViewById(R.id.layTitle);
//        layout_sta_back = findViewById(R.id.layout_sta_back);
//        footer = getLayoutInflater().inflate(R.layout.item_footer, null, false);
//        LinearLayout layout_status = findViewById(R.id.layout_status);
//        RelativeLayout layout_back = findViewById(R.id.layout_back);
//        layout_back.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), tm.getFooterBackgroundColor(iTheme)));
//
//        viewRail = findViewById(R.id.view_rail);
//        txtUpDir = viewRail.findViewById(R.id.txtUpDir);
//        txtDownDir = viewRail.findViewById(R.id.txtDownDir);
//        rail_up = findViewById(R.id.rail_up);
//        rail_down = findViewById(R.id.rail_down);
//        txtUpThis = rail_up.findViewById(R.id.txt_station_right);
//        txtUpPrev1 = rail_up.findViewById(R.id.txt_station_center);
//        txtUpPrev2 = rail_up.findViewById(R.id.txt_station_left);
//        txtDownThis = rail_down.findViewById(R.id.txt_station_left);
//        txtDownPrev1 = rail_down.findViewById(R.id.txt_station_center);
//        txtDownPrev2 = rail_down.findViewById(R.id.txt_station_right);
//        txtUpThis.setTextSize(16);
//        txtUpPrev1.setTextSize(13);
//        txtUpPrev2.setTextSize(13);
//        txtDownThis.setTextSize(16);
//        txtDownPrev1.setTextSize(13);
//        txtDownPrev2.setTextSize(13);
//        txtUpThis.setTextColor(ContextCompat.getColor(getBaseContext(), tm.getStationColor(iTheme)));
//        txtUpPrev1.setTextColor(ContextCompat.getColor(getBaseContext(), tm.getPrevStationColor(iTheme)));
//        txtUpPrev2.setTextColor(ContextCompat.getColor(getBaseContext(), tm.getPrevStationColor(iTheme)));
//        txtDownThis.setTextColor(ContextCompat.getColor(getBaseContext(), tm.getStationColor(iTheme)));
//        txtDownPrev1.setTextColor(ContextCompat.getColor(getBaseContext(), tm.getPrevStationColor(iTheme)));
//        txtDownPrev2.setTextColor(ContextCompat.getColor(getBaseContext(), tm.getPrevStationColor(iTheme)));
//        imgUpThis = rail_up.findViewById(R.id.icon_station_right);
//        imgUpPrev1 = rail_up.findViewById(R.id.icon_station_center);
//        imgUpPrev2 = rail_up.findViewById(R.id.icon_station_left);
//        imgDownThis = rail_down.findViewById(R.id.icon_station_left);
//        imgDownPrev1 = rail_down.findViewById(R.id.icon_station_center);
//        imgDownPrev2 = rail_down.findViewById(R.id.icon_station_right);
//
//        viewUpLine = rail_up.findViewById(R.id.view_line);
//        viewDownLine = rail_down.findViewById(R.id.view_line);
//
//        str_line_num = str_station_num.substring(0, 4);
//
//        LinearLayout.LayoutParams circleThis = new LinearLayout.LayoutParams(Units.dp(12), Units.dp(12));
//        LinearLayout.LayoutParams circlePrev = new LinearLayout.LayoutParams(Units.dp(10), Units.dp(10));
//
//        viewUpLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), tm.getTitleBarColor(iTheme, Integer.parseInt(str_line_num))));
//        viewDownLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), tm.getTitleBarColor(iTheme, Integer.parseInt(str_line_num))));
//        imgUpThis.setImageResource(tm.getStationIcon(Integer.parseInt(str_line_num), true));
//        imgUpPrev1.setImageResource(tm.getStationIcon(Integer.parseInt(str_line_num), false));
//        imgUpPrev2.setImageResource(tm.getStationIcon(Integer.parseInt(str_line_num), false));
//        imgDownThis.setImageResource(tm.getStationIcon(Integer.parseInt(str_line_num), true));
//        imgDownPrev1.setImageResource(tm.getStationIcon(Integer.parseInt(str_line_num), false));
//        imgDownPrev2.setImageResource(tm.getStationIcon(Integer.parseInt(str_line_num), false));
//        imgUpThis.setLayoutParams(circleThis);
//        imgUpPrev1.setLayoutParams(circlePrev);
//        imgUpPrev2.setLayoutParams(circlePrev);
//        imgDownThis.setLayoutParams(circleThis);
//        imgDownPrev1.setLayoutParams(circlePrev);
//        imgDownPrev2.setLayoutParams(circlePrev);
//        txtDownThis.setTypeface(null, Typeface.BOLD);
//        txtDownPrev2.setTypeface(null, Typeface.NORMAL);
//
//        if(Build.VERSION.SDK_INT < 19) {
//            layout_status.setVisibility(View.GONE);
//        }
//        layout_status.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), tm.getTitleBarColor(iTheme, Integer.parseInt(str_station_num.substring(0, 4)))));
//        layout_title.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), tm.getTitleBarColor(iTheme, Integer.parseInt(str_station_num.substring(0, 4)))));
//
//        //Log.e("str_station", str_station_name);
//        txt_station.setText(str_station_name);
//
//        setToday();
//        createTrain();
//        startApp();
//
//        footer.setBackgroundResource(tm.getFooterBackgroundColor(iTheme));
//
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//
//        btnTimetable.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(StationActivity.this, TimetableActivity.class);
//                intent.putExtra("str_station", str_station_name);
//                intent.putExtra("str_num", str_station_num);
//                startActivity(intent);
//            }
//        });
//
//        btnRefresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("Refresh", "Click");
//                createTrain();
//            }
//        });
//    }
//
//    private void startApp() {
//        LineStation lsPrev1 = lineManager.getPrevStation(lineManager.getStationArray(str_line_num), str_station_name, 1);
//        LineStation lsPrev2 = lineManager.getPrevStation(lineManager.getStationArray(str_line_num), str_station_name, 2);
//        LineStation lsNext1 = lineManager.getNextStation(lineManager.getStationArray(str_line_num), str_station_name, 1);
//        LineStation lsNext2 = lineManager.getNextStation(lineManager.getStationArray(str_line_num), str_station_name, 2);
//
//        arrUpSta[2] = lineManager.getStation(str_station_name);
//        arrDownSta[0] = arrUpSta[2];
//
//        txtUpThis.setText(str_station_name);
//        imgUpThis.setVisibility(View.VISIBLE);
//        imgUpPrev1.setVisibility(View.VISIBLE);
//        imgUpPrev2.setVisibility(View.VISIBLE);
//        imgDownThis.setVisibility(View.VISIBLE);
//        imgDownPrev1.setVisibility(View.VISIBLE);
//        imgDownPrev2.setVisibility(View.VISIBLE);
//
//        if(lsPrev1 != null) {
//            txtPrev.setText(lsPrev1.station + " 방면");
//            txtUpPrev1.setText(lsPrev1.station);
//            txtDownDir.setText("<< " + lsPrev1.station + " 방면");
//            arrUpSta[1] = lsPrev1.station;
//        } else {
//            txtPrev.setText("종착역");
//            txtUpPrev1.setText("");
//            txtDownDir.setText("");
//            imgUpPrev1.setVisibility(View.INVISIBLE);
//            arrUpSta[1] = "";
//        }
//
//        if(lsPrev2 != null) {
//            txtUpPrev2.setText(lsPrev2.station);
//            arrUpSta[0] = lsPrev2.station;
//        } else {
//            txtUpPrev2.setText("");
//            imgUpPrev2.setVisibility(View.INVISIBLE);
//            arrUpSta[0] = "";
//        }
//
//        txtDownThis.setText(str_station_name);
//
//        if(lsNext1 != null) {
//            txtNext.setText(lsNext1.station + " 방면");
//            txtDownPrev1.setText(lsNext1.station);
//            txtUpDir.setText(lsNext1.station + " 방면 >>");
//            arrDownSta[1] = lsNext1.station;
//        } else {
//            txtNext.setText("종착역");
//            txtDownPrev1.setText("");
//            txtUpDir.setText("");
//            imgDownPrev1.setVisibility(View.INVISIBLE);
//            arrDownSta[1] = "";
//        }
//
//        if(lsNext2 != null) {
//            txtDownPrev2.setText(lsNext2.station);
//            arrDownSta[2] = lsNext2.station;
//        } else {
//            txtDownPrev2.setText("");
//            imgDownPrev2.setVisibility(View.INVISIBLE);
//            arrDownSta[2] = "";
//        }
//
//        myAsyncTask = new MyAsyncTask();
//        myAsyncTask.execute(str_station_code);
//    }
//
//    private void createTrain() {
//        trainAsync = new TrainAsyncTask();
//        trainAsync.execute();
//    }
//
//    private void setToday() {
//        try {
//            CalcDate cd = new CalcDate();
//            iWeek = cd.getDayWeek();
//
//            i_line = Integer.parseInt(str_line_num);
//            str_station_code = checkCode(Integer.parseInt(str_station_num));
//
//            //Log.e("checkCode", str_station_code);
//
//            txt_circle.setText(tm.getCircleLineText(i_line));
//            txt_circle.setBackgroundResource(tm.getCircleLineColor(i_line));
//            layout_sta_back.setBackgroundResource(tm.getStationBackImage(i_line));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private String checkCode(int code) {
//        String str;
//        switch(code) {
//            case 1002002111:
//                str = "211-1";
//                break;
//
//            case 1002002112:
//                str = "211-2";
//                break;
//
//            case 1002002113:
//                str = "211-3";
//                break;
//
//            case 1002002114:
//                str = "211-4";
//                break;
//
//            case 1002002341:
//                str = "234-1";
//                break;
//
//            case 1002002342:
//                str = "234-2";
//                break;
//
//            case 1002002343:
//                str = "234-3";
//                break;
//
//            case 1002002344:
//                str = "234-4";
//                break;
//
//            case 1077000687:
//                str = "D7";
//                break;
//
//            case 1077000688:
//                str = "D8";
//                break;
//
//            case 1077000689:
//                str = "D9";
//                break;
//
//            case 1077006810:
//                str = "D10";
//                break;
//
//            case 1077006811:
//                str = "D11";
//                break;
//
//            case 1077006812:
//                str = "D12";
//                break;
//
//            case 1077006814:
//                str = "D14";
//                break;
//
//            case 1077006815:
//                str = "D15";
//                break;
//
//            case 1077006816:
//                str = "D16";
//                break;
//
//            case 1077006817:
//                str = "D17";
//                break;
//
//            case 1077006818:
//                str = "D18";
//                break;
//
//            case 1077006819:
//                str = "D19";
//                break;
//
//            case 1065006511:
//                str = "A11";
//                break;
//
//            case 1065006510:
//                str = "A10";
//                break;
//
//            case 1065006509:
//                str = "A09";
//                break;
//
//            case 1065006508:
//                str = "A08";
//                break;
//
//            case 1065065072:
//                str = "A072";
//                break;
//
//            case 1065065071:
//                str = "A071";
//                break;
//
//            case 1065006507:
//                str = "A07";
//                break;
//
//            case 1065006506:
//                str = "A06";
//                break;
//
//            case 1065006505:
//                str = "A05";
//                break;
//
//            case 1065006504:
//                str = "A04";
//                break;
//
//            case 1065006503:
//                str = "A03";
//                break;
//
//            case 1065006502:
//                str = "A02";
//                break;
//
//            case 1065006501:
//                str = "A01";
//                break;
//
//            case 1063080313:
//                // 경의중앙선 서울역
//                str = "P313";
//                break;
//
//            case 1063080312:
//                // 경의중앙선 신촌
//                str = "P312";
//                break;
//
//            default:
//                if(code >= 1001080142 && code <= 1001080175) {
//                    str = "P" + String.valueOf(code).substring(7, 10);
//                } else if(i_line == LineManager.LINE_BUNDANG || i_line == LineManager.LINE_SUIN || i_line == LineManager.LINE_KEUIJOONG) {
//                    str = "K" + String.valueOf(code).substring(7, 10);
//                } else if(i_line == LineManager.LINE_KYEONGCHOON) {
//                    str = "P" + String.valueOf(code).substring(7, 10);
//                } else {
//                    str = String.valueOf(code).substring(7, 10);
//                }
//                break;
//        }
//
//        return str;
//    }
//
//    public class TrainAsyncTask extends AsyncTask<String, Void, String> {
//
//        // 수행 전
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            btnRefresh.startAnimation();
//        }
//
//        // 수행 >> 끝나면 종료
//        @Override
//        protected String doInBackground(String... params) {
//
//            String result = "";
//
//            String station = lineManager.getStation(str_station_name);
//            FavoriteInfo fi = new FavoriteInfo();
//            //arrTrainList = fi.jsonParseList2(fi.getLine(str_line_num, str_station_num), str_line_num, station);
//            arrTrainList = fi.jsonParserList(fi.getLine(station), str_line_num, station);
//
//            return result;
//        }
//
//        // 종료
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            btnRefresh.stopAnimation();
//
//            if(result != null){
//                Log.d("TrainAsync", "result = " + result);
//            }
//
//            if(arrTrainList != null) {
//
//                rail_up.clearTrain();
//                rail_down.clearTrain();
//                layoutDown.removeAllViews();
//                layoutUp.removeAllViews();
//
//                for (int i = 0; i < arrTrainList.size(); i++) {
//
//                    int arvlCd = arrTrainList.get(i).arvlCd;
//                    String station = lineManager.getStation(arrTrainList.get(i).str_station);
//                    String state = arrTrainList.get(i).str_status;
//                    String[] array = new String[2];
//                    array[0] = arrTrainList.get(i).str_dst;
//                    array[1] = arrTrainList.get(i).str_no;
//
//                    TrainLabel tl = new TrainLabel(getBaseContext());
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                    params.setMargins(0, 0, 0, Units.dp(8));
//                    tl.setDestination(array[0]);
//                    tl.setNumber(array[1]);
//                    tl.setStatus(state);
//                    tl.setLayoutParams(params);
//
//                    if(arrTrainList.get(i).str_dir.equals("상행") || arrTrainList.get(i).str_dir.equals("내선")) {
//                        if(arvlCd != 0) {
//                            if (station.equals(arrUpSta[0])) {
//                                rail_up.addTrain(array, false, TrainRail.LEFT, TrainRail.getTrainState(state));
//                            } else if (station.equals(arrUpSta[1])) {
//                                rail_up.addTrain(array, false, TrainRail.CENTER, TrainRail.getTrainState(state));
//                            } else if (station.equals(arrUpSta[2])) {
//                                rail_up.addTrain(array, false, TrainRail.RIGHT, TrainRail.getTrainState(state));
//                            }
//                        } else {
//                            rail_up.addTrain(array, false, TrainRail.RIGHT, TrainRail.getTrainState(state));
//                        }
//                        layoutUp.addView(tl);
//                    } else {
//                        if(arvlCd != 0) {
//                            if (station.equals(arrDownSta[0])) {
//                                rail_down.addTrain(array, true, TrainRail.LEFT, TrainRail.getTrainState(state));
//                            } else if (station.equals(arrDownSta[1])) {
//                                rail_down.addTrain(array, true, TrainRail.CENTER, TrainRail.getTrainState(state));
//                            } else if (station.equals(arrDownSta[2])) {
//                                rail_down.addTrain(array, true, TrainRail.RIGHT, TrainRail.getTrainState(state));
//                            }
//                        } else {
//                            rail_down.addTrain(array, true, TrainRail.LEFT, TrainRail.getTrainState(state));
//                        }
//                        layoutDown.addView(tl);
//                    }
//                }
//                rail_up.showTrain();
//                rail_down.showTrain();
//                rail_up.resumeAnimation();
//                rail_down.resumeAnimation();
//
//            } else {
//                Log.e("arrList", "Empty Array");
//            }
//
//        }
//
//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//        }
//
//    }
//
//    public class MyAsyncTask extends AsyncTask<String, Void, String> {
//
//        String str_type;
//
//        // 수행 전
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        // 수행 >> 끝나면 종료
//        @Override
//        protected String doInBackground(String... params) {
//
//            String sum = "";
//
//            if(params != null){
//                for(String s : params){
//                    sum += s;
//                }
//            }
//            if(str_line_num.equals("1002")) {
//                CalcDate cd = new CalcDate();
//                GetType gt = new GetType();
//                String result_type = gt.getLine2(cd.getDayWeek());
//                System.out.println(result_type);
//                jsonTypeList(result_type);
//            } else if(str_line_num.equals("1009")) {
//                GetType gt = new GetType();
//                str_type = gt.getJson9File(9, iWeek);
//                String result_type = gt.getType(str_type);
//                System.out.println(result_type);
//                jsonTypeList(result_type);
//            }
//            return sum;
//        }
//
//        // 종료
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//        }
//
//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//        }
//
//    }
//
//    private String[][] jsonTypeList(String pRecvServerPage) {
//
//        try {
//            if(str_line_num.equals("1002")) {
//                JSONObject json = new JSONObject(pRecvServerPage);
//                JSONArray jArr = json.getJSONArray("train");
//
//                String[] jsonName = {"trainNo", "trainDst"};
//                String[][] parseredData = new String[jArr.length()][jsonName.length];
//                for (int i = 0; i < jArr.length(); i++) {
//                    json = jArr.getJSONObject(i);
//
//                    for (int j = 0; j < jsonName.length; j++) {
//                        parseredData[i][j] = json.getString(jsonName[j]);
//                    }
//                }
//
//                MAX_XML = parseredData.length;
//                xmlNumber = new String[MAX_XML];
//                xmlDst = new String[MAX_XML];
//
//                for (int i = 0; i < parseredData.length; i++) {
//                    //Log.i("분석 데이터: " + i + " : ", parseredData[i][0]);
//                    xmlNumber[i] = parseredData[i][0];
//                    xmlDst[i] = parseredData[i][1];
//                }
//
//                return parseredData;
//            } else {
//                JSONObject json = new JSONObject(pRecvServerPage);
//                JSONArray jArr = json.getJSONArray("train");
//
//                String[] jsonName = {"trainNo"};
//                String[][] parseredData = new String[jArr.length()][jsonName.length];
//                for (int i = 0; i < jArr.length(); i++) {
//                    json = jArr.getJSONObject(i);
//
//                    for(int j = 0; j < jsonName.length; j++) {
//                        parseredData[i][j] = json.getString(jsonName[j]);
//                        System.out.println(parseredData[i][j]);
//                    }
//                }
//
//                MAX_XML = parseredData.length;
//                xmlNumber = new String[MAX_XML];
//
//                for(int i = 0; i < parseredData.length; i++){
//                    //Log.i("분석 데이터: " + i + " : ", parseredData[i][0]);
//                    xmlNumber[i] = parseredData[i][0];
//                }
//
//                return parseredData;
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        rail_up.stopAnimation();
//        rail_down.stopAnimation();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        rail_up.resumeAnimation();
//        rail_down.resumeAnimation();
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String str_station_name;
        String str_station_num;

        setContentView(R.layout.activity_station);
        Intent intent = getIntent();

        str_station_name = intent.getStringExtra("str_station");
        str_station_num = intent.getStringExtra("str_num");

        intent = new Intent(StationActivity.this, TimetableActivity.class);
        intent.putExtra("str_station", str_station_name);
        intent.putExtra("str_num", str_station_num);
        startActivity(intent);
        finish();
    }

}
