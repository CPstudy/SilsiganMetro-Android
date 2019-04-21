package com.ganada.silsiganmetro.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ganada.silsiganmetro.util.CalcDate;
import com.ganada.silsiganmetro.real.GetType;
import com.ganada.silsiganmetro.util.LineManager;
import com.ganada.silsiganmetro.R;
import com.ganada.silsiganmetro.real.Realtable;
import com.ganada.silsiganmetro.view.CustomTitlebar;
import com.ganada.silsiganmetro.view.RefreshButton;
import com.ganada.silsiganmetro.util.ThemeManager;
import com.ganada.silsiganmetro.listitem.TimeList;
import com.ganada.silsiganmetro.view.TrainIcon;
import com.ganada.silsiganmetro.view.TrainRail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class TimetableActivity extends Activity {

    private SharedPreferences mPref;
    private SharedPreferences.Editor mPrefEdit;

    private MyAsyncTask myAsyncTask;

    LineManager lineManager;
    ThemeManager tm;
    CustomListAdapter adapter;
    TimeList timelist;
    ArrayList<TimeList> arTimeList;

    View footer, viewUpLine, viewDownLine, viewRail;
    CustomTitlebar layTitle;
    TrainRail rail_up, rail_down;
    LinearLayout layout_sta_back;
    LinearLayout layout_btn;
    Button btn_norm;
    Button btn_week;
    Button btn_sunday;
    ImageButton btnBack;
    RefreshButton btnRefresh;
    TextView txtUpDir, txtDownDir;
    TextView txt_loading;
    TextView txt_station;
    TextView txt_circle;
    ListView list;
    ArrayList<TrainIcon> arrTrain = new ArrayList<>();

    int MAX_XML = 0;
    int iTheme = 0;
    int iWeek = 1;
    int i_line = 0;
    String str_station_name;
    String str_station_num;
    String str_station_code = "201";
    String str_line_num;

    String[] arrTime = {"05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};
    String[] arrUp = new String[20];
    String[] arrDown = new String[20];
    String[] xmlNumber;
    String[] xmlDst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        mPref = getSharedPreferences("Pref1", 0);
        mPrefEdit = mPref.edit();
        tm = new ThemeManager(getBaseContext());
        lineManager = new LineManager(getApplicationContext());
        iTheme = mPref.getInt("iTheme", 0);

        Intent intent = getIntent();
        str_station_name = intent.getStringExtra("str_station");
        str_station_num = intent.getStringExtra("str_num");
        str_line_num = str_station_num.substring(0, 4);

        btn_norm = (Button) findViewById(R.id.btn_norm);
        btn_week = (Button) findViewById(R.id.btn_week);
        btn_sunday = (Button) findViewById(R.id.btn_sunday);
        txt_loading = findViewById(R.id.txt_loading);
        txt_station = (TextView) findViewById(R.id.txt_station);
        txt_circle = (TextView) findViewById(R.id.txt_circle);
        layTitle = findViewById(R.id.layTitle);
        layout_sta_back = (LinearLayout) findViewById(R.id.layout_sta_back);
        layout_btn = (LinearLayout) findViewById(R.id.layout_btn);
        footer = getLayoutInflater().inflate(R.layout.item_footer, null, false);

        Window window = getWindow();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), tm.getTitleBarColor(iTheme, Integer.parseInt(str_line_num))));
        }
        layTitle.setBackgroundColorById(tm.getTitleBarColor(iTheme, Integer.parseInt(str_line_num)));

        //Log.e("str_station", str_station_name);
        txt_station.setText(str_station_name);

        arTimeList = new ArrayList<TimeList>();
        for(int i = 0; i < arrUp.length; i++) {
            arrUp[i] = "";
            arrDown[i] = "";
        }

        setToday();
        startApp();

        adapter = new CustomListAdapter(this, R.layout.item_timetable, arTimeList);

        list = (ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.addFooterView(footer);
        footer.setBackgroundResource(tm.getFooterBackgroundColor(iTheme));

        btn_norm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iWeek = 1;
                setWeekButton();
                startApp();
            }
        });

        btn_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iWeek = 2;
                setWeekButton();
                startApp();
            }
        });

        btn_sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iWeek = 3;
                setWeekButton();
                startApp();
            }
        });

    }

    private void startApp() {
        arTimeList.clear();
        for(int i = 0; i < arrUp.length; i++) {
            arrUp[i] = "";
            arrDown[i] = "";
        }
        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute(str_station_code);
    }

    private void setToday() {
        try {
            CalcDate cd = new CalcDate();
            iWeek = cd.getDayWeek();
            setWeekButton();

            i_line = Integer.parseInt(str_line_num);
            str_station_code = checkCode(Integer.parseInt(str_station_num));

            //Log.e("checkCode", str_station_code);

            txt_circle.setText(tm.getCircleLineText(i_line));
            txt_circle.setBackgroundResource(tm.getCircleLineColor(i_line));
            layout_sta_back.setBackgroundResource(tm.getStationBackImage(i_line));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String checkCode(int code) {
        String str;
        switch(code) {
            case 1002002111:
                str = "211-1";
                break;

            case 1002002112:
                str = "211-2";
                break;

            case 1002002113:
                str = "211-3";
                break;

            case 1002002114:
                str = "211-4";
                break;

            case 1002002341:
                str = "234-1";
                break;

            case 1002002342:
                str = "234-2";
                break;

            case 1002002343:
                str = "234-3";
                break;

            case 1002002344:
                str = "234-4";
                break;

            case 1077000687:
                str = "D7";
                break;

            case 1077000688:
                str = "D8";
                break;

            case 1077000689:
                str = "D9";
                break;

            case 1077006810:
                str = "D10";
                break;

            case 1077006811:
                str = "D11";
                break;

            case 1077006812:
                str = "D12";
                break;

            case 1077006814:
                str = "D14";
                break;

            case 1077006815:
                str = "D15";
                break;

            case 1077006816:
                str = "D16";
                break;

            case 1077006817:
                str = "D17";
                break;

            case 1077006818:
                str = "D18";
                break;

            case 1077006819:
                str = "D19";
                break;

            case 1065006511:
                str = "A11";
                break;

            case 1065006510:
                str = "A10";
                break;

            case 1065006509:
                str = "A09";
                break;

            case 1065006508:
                str = "A08";
                break;

            case 1065065072:
                str = "A072";
                break;

            case 1065065071:
                str = "A071";
                break;

            case 1065006507:
                str = "A07";
                break;

            case 1065006506:
                str = "A06";
                break;

            case 1065006505:
                str = "A05";
                break;

            case 1065006504:
                str = "A04";
                break;

            case 1065006503:
                str = "A03";
                break;

            case 1065006502:
                str = "A02";
                break;

            case 1065006501:
                str = "A01";
                break;

            case 1063080313:
                // 경의중앙선 서울역
                str = "P313";
                break;

            case 1063080312:
                // 경의중앙선 신촌
                str = "P312";
                break;

            default:
                if(code >= 1001080142 && code <= 1001080175) {
                    str = "P" + String.valueOf(code).substring(7, 10);
                } else if(i_line == LineManager.LINE_BUNDANG || i_line == LineManager.LINE_SUIN || i_line == LineManager.LINE_KEUIJOONG) {
                    str = "K" + String.valueOf(code).substring(7, 10);
                } else if(i_line == LineManager.LINE_KYEONGCHOON) {
                    str = "P" + String.valueOf(code).substring(7, 10);
                } else {
                    str = String.valueOf(code).substring(7, 10);
                }
                break;
        }

        return str;
    }

    private void setWeekButton() {
        switch(iWeek) {
            case 1:
                btn_norm.setBackgroundResource(R.drawable.item_blue);
                btn_week.setBackgroundResource(R.drawable.item_white);
                btn_sunday.setBackgroundResource(R.drawable.item_white);
                btn_norm.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_seltext));
                btn_week.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                btn_sunday.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                break;

            case 2:
                btn_norm.setBackgroundResource(R.drawable.item_white);
                btn_week.setBackgroundResource(R.drawable.item_blue);
                btn_sunday.setBackgroundResource(R.drawable.item_white);
                btn_norm.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                btn_week.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_seltext));
                btn_sunday.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                break;

            case 3:
                btn_norm.setBackgroundResource(R.drawable.item_white);
                btn_week.setBackgroundResource(R.drawable.item_white);
                btn_sunday.setBackgroundResource(R.drawable.item_blue);
                btn_norm.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                btn_week.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                btn_sunday.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_seltext));
                break;

            default:
                btn_norm.setBackgroundResource(R.drawable.item_blue);
                btn_week.setBackgroundResource(R.drawable.item_white);
                btn_sunday.setBackgroundResource(R.drawable.item_white);
                btn_norm.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_seltext));
                btn_week.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                btn_sunday.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                break;

        }
    }

    public class MyAsyncTask extends AsyncTask<String, Void, String> {

        String str_type;

        // 수행 전
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            txt_loading.setVisibility(View.VISIBLE);
        }

        // 수행 >> 끝나면 종료
        @Override
        protected String doInBackground(String... params) {

            String sum = "";

            if(params != null){
                for(String s : params){
                    sum += s;
                }
            }
            if(str_line_num.equals("1002")) {
                CalcDate cd = new CalcDate();
                GetType gt = new GetType();
                String result_type = gt.getLine2(cd.getDayWeek());
                System.out.println(result_type);
                jsonTypeList(result_type);
            } else if(str_line_num.equals("1009")) {
                GetType gt = new GetType();
                str_type = gt.getJson9File(9, iWeek);
                String result_type = gt.getType(str_type);
                System.out.println(result_type);
                jsonTypeList(result_type);
            }

            Realtable rt = new Realtable();
            String result = rt.getTable(sum, iWeek, 1);
            jsonParserList(result, 1);
            result = rt.getTable(sum, iWeek, 2);
            jsonParserList(result, 2);
            return sum;
        }

        // 종료
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            txt_loading.setVisibility(View.GONE);
            try {
                final CalcDate cd = new CalcDate();
                for (int i = 0; i < arrTime.length; i++) {
                    String str_up;
                    String str_down;
                    try {
                        str_up = arrUp[i].substring(0, arrUp[i].length() - 8);
                    } catch (Exception e) {
                        str_up = arrUp[i];
                    }
                    try {
                        str_down = arrDown[i].substring(0, arrDown[i].length() - 8);
                    } catch (Exception e) {
                        str_down = arrDown[i];
                    }
                    timelist = new TimeList(str_up, str_down, arrTime[i]);
                    arTimeList.add(timelist);
                }
                adapter.notifyDataSetChanged();
                list.clearFocus();
                list.post(new Runnable() {
                    @Override
                    public void run() {
                        list.setSelection(cd.getTimeList());
                    }
                });
                Log.e("calendar", "" + cd.getTimeList());

            } catch (Exception e) {
                e.printStackTrace();
            }
            if(result != null){
                //Log.d("ASYNC", "result = " + result);
            }

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }

    private String[][] jsonTypeList(String pRecvServerPage) {

        try {
            if(str_line_num.equals("1002")) {
                JSONObject json = new JSONObject(pRecvServerPage);
                JSONArray jArr = json.getJSONArray("train");

                String[] jsonName = {"trainNo", "trainDst"};
                String[][] parseredData = new String[jArr.length()][jsonName.length];
                for (int i = 0; i < jArr.length(); i++) {
                    json = jArr.getJSONObject(i);

                    for (int j = 0; j < jsonName.length; j++) {
                        parseredData[i][j] = json.getString(jsonName[j]);
                    }
                }

                MAX_XML = parseredData.length;
                xmlNumber = new String[MAX_XML];
                xmlDst = new String[MAX_XML];

                for (int i = 0; i < parseredData.length; i++) {
                    //Log.i("분석 데이터: " + i + " : ", parseredData[i][0]);
                    xmlNumber[i] = parseredData[i][0];
                    xmlDst[i] = parseredData[i][1];
                }

                return parseredData;
            } else {
                JSONObject json = new JSONObject(pRecvServerPage);
                JSONArray jArr = json.getJSONArray("train");

                String[] jsonName = {"trainNo"};
                String[][] parseredData = new String[jArr.length()][jsonName.length];
                for (int i = 0; i < jArr.length(); i++) {
                    json = jArr.getJSONObject(i);

                    for(int j = 0; j < jsonName.length; j++) {
                        parseredData[i][j] = json.getString(jsonName[j]);
                        System.out.println(parseredData[i][j]);
                    }
                }

                MAX_XML = parseredData.length;
                xmlNumber = new String[MAX_XML];

                for(int i = 0; i < parseredData.length; i++){
                    //Log.i("분석 데이터: " + i + " : ", parseredData[i][0]);
                    xmlNumber[i] = parseredData[i][0];
                }

                return parseredData;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String[][] jsonParserList(String pRecvServerPage, int updown) {

        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            json = json.getJSONObject("SearchSTNTimeTableByFRCodeService");
            JSONArray jArr = json.getJSONArray("row");

            String[] jsonName = {"ARRIVETIME", "LEFTTIME", "SUBWAYSNAME", "SUBWAYENAME", "TRAIN_NO", "EXPRESS_YN", "STATION_CD", "ORIGINSTATION"};
            String[][] parseredData = new String[jArr.length()][jsonName.length];
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);

                for(int j = 0; j < jsonName.length; j++) {
                    try {
                        parseredData[i][j] = json.getString(jsonName[j]);
                        if(j == 4 && (str_line_num.equals("1001") || str_line_num.equals("1003") || str_line_num.equals("1004"))) {
                            if(parseredData[i][j].contains("K")) {
                                parseredData[i][j] = "K" + parseredData[i][j].replace("K", "");
                            } else {
                                parseredData[i][j] = "S" + parseredData[i][j];
                            }
                        }
                    } catch (Exception e) {
                        parseredData[i][j] = "undefined";
                    }
                }
            }

            //Log.e("parseredData.length", "" + parseredData.length);

            for(int i = 0; i < parseredData.length; i++){
                int h = Integer.parseInt(parseredData[i][1].split(":")[0]);
                int m = Integer.parseInt(parseredData[i][1].split(":")[1]);

                if(!str_line_num.equals("1002")) {
                    if(parseredData[i][6].equals(parseredData[i][7])) {
                        parseredData[i][3] = parseredData[i][3] + "★";
                    }
                }

                if(str_line_num.equals("1001")) {
                    try {
                        parseredData[i][4] = String.format(Locale.KOREA, "%c%04d", parseredData[i][4].charAt(0), Integer.parseInt(parseredData[i][4].substring(1, parseredData[i][4].length())));

                        if (parseredData[i][4].substring(1, parseredData[i][4].length()).charAt(1) == '3') {
                            parseredData[i][5] = "T";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if(str_line_num.equals("1002")) {
                    for (int j = 0; j < MAX_XML; j++) {
                        try {
                            if ((Integer.parseInt(parseredData[i][4]) / 1000) == 2) {
                                if (parseredData[i][4].equals(xmlNumber[j])) {
                                    parseredData[i][3] = "성수종착";
                                } else {
                                    if (updown == 1 && parseredData[i][3].equals("성수")) {
                                        parseredData[i][3] = "내선순환";
                                    } else if (updown == 2 && parseredData[i][3].equals("성수")) {
                                        parseredData[i][3] = "외선순환";
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //Log.e("line2-f", parseredData[i][3]);
                    }
                } else if(str_line_num.equals("1009")) {
                    for (int ii = 0; ii < MAX_XML; ii++) {
                        if (iWeek == 1 && xmlNumber[ii].equals(parseredData[i][4].replace("E", "").replace("C", ""))) {
                            parseredData[i][3] = parseredData[i][3] + " 6량";
                        }
                    }
                    switch(parseredData[i][4]) {
                        case "E9533":
                        case "E9537":
                        case "E9541":
                        case "E9545":
                            if(str_station_num.equals("1009000925")) {
                                parseredData[i][3] = "가양★";
                            } else {
                                parseredData[i][3] = "가양";
                            }
                            break;
                        default:
                            break;
                    }
                }

                parseredData[i][3] = parseredData[i][3].replace("성수종착", "성수");

                String str;
                if(updown == 1) {
                    if(parseredData[i][5].equals("D")) {
                        str = String.format(Locale.KOREA, "<b>%02d:%02d</b><br><font color='#7b7b7b'>%s %s </font><font color='#ff4949'>급행</font><br><br>", h, m, parseredData[i][4], parseredData[i][3]);
                    } else if(parseredData[i][5].equals("T")) {
                        str = String.format(Locale.KOREA, "<b>%02d:%02d</b><br><font color='#7b7b7b'>%s %s </font><font color='#2079ff'>특급</font><br><br>", h, m, parseredData[i][4], parseredData[i][3]);
                    } else {
                        str = String.format(Locale.KOREA, "<b>%02d:%02d</b><br><font color='#7b7b7b'>%s %s</font><br><br>", h, m, parseredData[i][4], parseredData[i][3]);
                    }
                } else {
                    if(parseredData[i][5].equals("D")) {
                        str = String.format(Locale.KOREA, "<b>%02d:%02d</b><br><font color='#7b7b7b'>%s </font><font color='#ff4949'>급행</font><font color='#7b7b7b'> %s</font><br><br>", h, m, parseredData[i][3], parseredData[i][4]);
                    } else if(parseredData[i][5].equals("T")) {
                        str = String.format(Locale.KOREA, "<b>%02d:%02d</b><br><font color='#7b7b7b'>%s </font><font color='#2079ff'>특급</font><font color='#7b7b7b'> %s</font><br><br>", h, m, parseredData[i][3], parseredData[i][4]);
                    } else {
                        str = String.format(Locale.KOREA, "<b>%02d:%02d</b><br><font color='#7b7b7b'>%s %s</font><br><br>", h, m, parseredData[i][3], parseredData[i][4]);
                    }
                }
                if(h == 4) {
                    if (updown == 1) {
                        arrUp[h - 4] += str;
                    } else {
                        arrDown[h - 4] += str;
                    }
                } else if(h >= 5 && h <= 24) {
                    if (updown == 1) {
                        arrUp[h - 5] += str;
                    } else {
                        arrDown[h - 5] += str;
                    }
                } else if(h == 25 || h == 26) {
                    if (updown == 1) {
                        arrUp[19] += str;
                    } else {
                        arrDown[19] += str;
                    }
                }
            }

            return parseredData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public class PersonViewHolder {
        public TextView txt_up;
        public TextView txt_down;
        public TextView txt_time;
        public LinearLayout layout_back;
        public View view_divider;
    }

    class CustomListAdapter extends BaseAdapter {
        Context con;
        LayoutInflater inflacter;
        ArrayList<TimeList> arC;
        int layout;

        public CustomListAdapter(Context context, int alayout, ArrayList<TimeList> aarC) {
            con = context;
            inflacter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            arC = aarC;
            layout = alayout;
        }

        // 어댑터의 항목 수 조사
        @Override
        public int getCount() {
            return arC.size();
        }

        // position 위치 항목 Name 반환
        @Override
        public Object getItem(int position) {
            return null;
        }

        // position 위치 항목 ID 반환
        @Override
        public long getItemId(int position) {
            return 0;
        }

        // 각각의 항목 뷰 생성
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            PersonViewHolder viewHolder;

            if (convertView == null) {
                convertView = inflacter.inflate(layout, parent, false);

                viewHolder = new PersonViewHolder();
                viewHolder.txt_up = (TextView) convertView.findViewById(R.id.txt_table_up);
                viewHolder.txt_down = (TextView) convertView.findViewById(R.id.txt_table_down);
                viewHolder.txt_time = (TextView) convertView.findViewById(R.id.txt_hour);
                viewHolder.layout_back = (LinearLayout) convertView.findViewById(R.id.layout_back);
                viewHolder.view_divider = (View) convertView.findViewById(R.id.view_divider);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (PersonViewHolder) convertView.getTag();
            }

            viewHolder.layout_back.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), tm.getListBackgroundColor(iTheme)));
            viewHolder.view_divider.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), tm.getTableDividerColor(iTheme)));
            viewHolder.txt_time.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), tm.getTableDividerColor(iTheme)));

            viewHolder.txt_up.setText(Html.fromHtml(arC.get(position).str_up));
            viewHolder.txt_down.setText(Html.fromHtml(arC.get(position).str_down));
            viewHolder.txt_time.setText(arC.get(position).str_time);

            return convertView;
        }
    }
}
