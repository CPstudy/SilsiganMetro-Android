package com.ganada.silsiganmetro.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ganada.silsiganmetro.MetroApplication;
import com.ganada.silsiganmetro.laboratory.MetroConstant;
import com.ganada.silsiganmetro.real.FavoriteInfo;
import com.ganada.silsiganmetro.util.LineManager;
import com.ganada.silsiganmetro.listitem.ListFavorite;
import com.ganada.silsiganmetro.listitem.ListTrain;
import com.ganada.silsiganmetro.R;
import com.ganada.silsiganmetro.common.StringRefactor;
import com.ganada.silsiganmetro.util.ThemeManager;
import com.ganada.silsiganmetro.view.CustomTitlebar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FavoriteActivity extends Activity {

    private SharedPreferences mPref;
    private SharedPreferences.Editor mPrefEdit;

    LineManager lineManager;
    ThemeManager tm;
    private MyAsyncTask myAsyncTask;

    MetroApplication sv;
    ListFavorite listfavorite;
    ListTrain listtrain;
    ArrayList<ListFavorite> arCustomList;
    ArrayList<ListTrain> arTrainList;
    ListFavoriteAdapter adapter;
    ListView list;
    Animation animSpin;
    CustomTitlebar layout_title;
    ImageView imgTime;
    Button btnRefresh;
    View footer;

    int iTime = 5;
    int iSec;
    boolean bool_done = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        sv = (MetroApplication) getApplication();
        list = findViewById(R.id.list);
        layout_title = findViewById(R.id.layTitle);
        imgTime = findViewById(R.id.imgTime);
        btnRefresh = findViewById(R.id.btnRefresh);
        footer = getLayoutInflater().inflate(R.layout.item_footer, null, false);
        LinearLayout layout_status = findViewById(R.id.layout_status);

        arCustomList = new ArrayList<ListFavorite>();
        arTrainList = new ArrayList<ListTrain>();

        mPref = getSharedPreferences("Pref1", 0);
        mPrefEdit = mPref.edit();
        tm = new ThemeManager(getBaseContext());
        lineManager = new LineManager(getApplicationContext());

        iSec = mPref.getInt("timerefresh", 0) * 5;
        iTime = iSec;

        Window window = getWindow();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), tm.getTitleBarColor(0, 0)));
        }
        layout_title.setBackgroundColorById(tm.getTitleBarColor(0, 0));

        animSpin = AnimationUtils.loadAnimation(this, R.anim.spin_anim);

        appStart();

        adapter = new ListFavoriteAdapter(this, R.layout.item_favorite, arCustomList);
        list.addFooterView(footer);
        list.setAdapter(adapter);

        getRealFavorite();

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnRefresh.setEnabled(false);
                getRealFavorite();
                iTime = 5;
            }
        });

    }

    private void appStart() {
        try {
            String str_line = lineManager.getLine();
            String str_station = lineManager.getStation();
            String str_pos = lineManager.getPosition();

            String[] arr_line = str_line.split(";");
            String[] arr_station = str_station.split(";");
            String[] arr_pos = str_pos.split(";");

            arCustomList.clear();
            for(int i = 0; i < arr_line.length; i++) {
                listfavorite = new ListFavorite(arr_station[i], Integer.parseInt(arr_line[i]), Integer.parseInt(arr_pos[i]));
                arCustomList.add(listfavorite);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void getRealFavorite() {
        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute("");
    }

    public class ViewHolder {
        RelativeLayout layout_topline;
        LinearLayout layout_station_back;
        TextView txt_station;
        TextView txt_circle;
        TextView txt_up_dst;
        TextView txt_up_station;
        TextView txt_up_status;
        TextView txt_up_next_status;
        TextView txt_up_next;
        TextView txt_down_dst;
        TextView txt_down_station;
        TextView txt_down_status;
        TextView txt_down_next;
        TextView txt_down_next_status;
    }

    private class ListFavoriteAdapter extends BaseAdapter {
        Context con;
        LayoutInflater inflater;
        ArrayList<ListFavorite> favorites;
        int layout;
        RelativeLayout.LayoutParams pm;
        StringRefactor sr = new StringRefactor();

        ListFavoriteAdapter(Context context, int layout, ArrayList<ListFavorite> favorites) {
            con = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.favorites = favorites;
            this.layout = layout;
        }

        // 어댑터의 항목 수 조사
        @Override
        public int getCount() {
            return favorites.size();
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
            final ViewHolder viewHolder;

            if (convertView == null) {
                convertView = inflater.inflate(layout, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.layout_topline = convertView.findViewById(R.id.layout_topline);
                viewHolder.layout_station_back = convertView.findViewById(R.id.layout_sta_back);
                viewHolder.txt_station = convertView.findViewById(R.id.txt_station);
                viewHolder.txt_circle = convertView.findViewById(R.id.txt_circle);
                viewHolder.txt_up_dst = convertView.findViewById(R.id.txt_up_dst);
                viewHolder.txt_up_station = convertView.findViewById(R.id.txt_up_station);
                viewHolder.txt_up_status = convertView.findViewById(R.id.txt_up_status);
                viewHolder.txt_up_next = convertView.findViewById(R.id.txt_up_next);
                viewHolder.txt_up_next_status = convertView.findViewById(R.id.txt_up_next_status);
                viewHolder.txt_down_dst = convertView.findViewById(R.id.txt_down_dst);
                viewHolder.txt_down_station = convertView.findViewById(R.id.txt_down_station);
                viewHolder.txt_down_status = convertView.findViewById(R.id.txt_down_status);
                viewHolder.txt_down_next = convertView.findViewById(R.id.txt_down_next);
                viewHolder.txt_down_next_status = convertView.findViewById(R.id.txt_down_next_status);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            boolean bool_first_up = false;
            boolean bool_first_down = false;
            String str_up_dst = "";
            String str_up_station = "";
            String str_up_status = "";
            String str_up_next = "";
            String str_up_next_status = "";
            String str_down_dst = "";
            String str_down_station = "";
            String str_down_status = "";
            String str_down_next = "";
            String str_down_next_status = "";

            viewHolder.txt_station.setText(favorites.get(position).str_station);
            viewHolder.layout_station_back.setBackgroundResource(tm.getStationBackImage(favorites.get(position).i_line));
            viewHolder.txt_circle.setBackgroundResource(tm.getCircleLineColor(favorites.get(position).i_line));
            viewHolder.txt_circle.setText(tm.getCircleLineText(favorites.get(position).i_line));
            //viewHolder.layout_topline.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), tm.getTitleBarColor(0, favorites.get(position).i_line)));

            try {
                for (int i = 0; i < arTrainList.size(); i++) {
                    if ((Integer.parseInt(arTrainList.get(i).str_line) == favorites.get(position).i_line) && arTrainList.get(i).str_this_station.equals(favorites.get(position).str_station)) {
                        if (!bool_first_up && (arTrainList.get(i).str_dir.equals("상행") || arTrainList.get(i).str_dir.equals("내선"))) {
                            if(!(arTrainList.get(i).str_dst.contains("인천국제공항 (급행)") && arTrainList.get(i).str_line.equals("1065"))) {
                                str_up_dst = lineManager.getDst(arTrainList.get(i).str_dst, arTrainList.get(i).str_line);
                                str_up_station = arTrainList.get(i).str_station;
                                str_up_status = sr.changeStatus(arTrainList.get(i).str_status);
                                bool_first_up = true;
                            }
                        } else if (bool_first_up && (arTrainList.get(i).str_dir.equals("상행") || arTrainList.get(i).str_dir.equals("내선"))) {
                            if(!(arTrainList.get(i).str_dst.contains("인천국제공항 (급행)") && arTrainList.get(i).str_line.equals("1065"))) {
                                str_up_next += lineManager.getDst(arTrainList.get(i).str_dst, arTrainList.get(i).str_line) + "\n";
                                str_up_next_status += sr.changeStatus(arTrainList.get(i).str_status) + "\n";
                            }
                        }

                        if (!bool_first_down && (arTrainList.get(i).str_dir.equals("하행") || arTrainList.get(i).str_dir.equals("외선"))) {
                            if (!(arTrainList.get(i).str_dst.contains("서울 (급행)") && arTrainList.get(i).str_line.equals("1065"))) {
                                str_down_dst = lineManager.getDst(arTrainList.get(i).str_dst, arTrainList.get(i).str_line);
                                str_down_station = arTrainList.get(i).str_station;
                                str_down_status = sr.changeStatus(arTrainList.get(i).str_status);
                                bool_first_down = true;
                            }
                        } else if (bool_first_down && (arTrainList.get(i).str_dir.equals("하행") || arTrainList.get(i).str_dir.equals("외선"))) {
                            if (!(arTrainList.get(i).str_dst.contains("서울 (급행)") && arTrainList.get(i).str_line.equals("1065"))) {
                                str_down_next += lineManager.getDst(arTrainList.get(i).str_dst, arTrainList.get(i).str_line) + "\n";
                                str_down_next_status += sr.changeStatus(arTrainList.get(i).str_status) + "\n";

                            }
                        }
                    } else {
                    }
                }
            } catch (Exception e) {
                Log.e("fail", "fail");
                e.printStackTrace();
            }

            viewHolder.txt_up_dst.setText(str_up_dst);
            viewHolder.txt_up_station.setText(str_up_station);
            viewHolder.txt_up_status.setText(str_up_status);
            viewHolder.txt_up_next.setText(sr.removeLastChar(str_up_next));
            viewHolder.txt_up_next_status.setText(sr.removeLastChar(str_up_next_status));
            viewHolder.txt_down_dst.setText(str_down_dst);
            viewHolder.txt_down_station.setText(str_down_station);
            viewHolder.txt_down_status.setText(str_down_status);
            viewHolder.txt_down_next.setText(sr.removeLastChar(str_down_next));
            viewHolder.txt_down_next_status.setText(sr.removeLastChar(str_down_next_status));


            /*if(!bool_first_up) {
                viewHolder.txt_up_dst.setText("");
                viewHolder.txt_up_station.setText("정보 없음");
                viewHolder.txt_up_status.setText("");
            } else {
                viewHolder.txt_up_dst.setText(str_up_dst);
                viewHolder.txt_up_station.setText(str_up_station);
                viewHolder.txt_up_status.setText(str_up_status);
            }

            if(!bool_first_down) {
                viewHolder.txt_down_dst.setText("");
                viewHolder.txt_down_station.setText("정보 없음");
                viewHolder.txt_down_status.setText("");
            } else {
                viewHolder.txt_down_dst.setText(str_down_dst);
                viewHolder.txt_down_station.setText(str_down_station);
                viewHolder.txt_down_status.setText(str_down_status);
            }*/

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), lineManager.getClass(favorites.get(position).i_line));
                    //Intent intent = new Intent(FavoriteActivity.this, LineActivity.class);
                    //intent.putExtra(MetroConstant.KEY_LINE_POSITION, favorites.get(position).i_pos);
                    sv.setPosition(favorites.get(position).i_pos);
                    intent.putExtra(MetroConstant.KEY_LINE_NUMBER, favorites.get(position).i_line);
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }

    public class MyAsyncTask extends AsyncTask<String, Void, String> {

        // 수행 전
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imgTime.startAnimation(animSpin);
            arTrainList.clear();
            bool_done = false;
        }

        // 수행 >> 끝나면 종료
        @Override
        protected String doInBackground(String... params) {

            String sum = "";
            String result = "";

            if(params != null){
                for(String s : params){
                    sum += s;
                }
            }

            for(int i = 0; i < arCustomList.size(); i++) {
                FavoriteInfo fi = new FavoriteInfo();
                result = fi.getLine(lineManager.getStation(arCustomList.get(i).str_station));
                jsonParserList(result, String.valueOf(arCustomList.get(i).i_line), arCustomList.get(i).str_station);
            }
            Collections.sort(arTrainList, new CompareLine());

            return result;
        }

        // 종료
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            imgTime.clearAnimation();
            if(bool_done) {
                adapter.notifyDataSetChanged();
            }
            btnRefresh.setEnabled(true);
            if(result != null){
                Log.d("ASYNC", "result = " + result);
            }

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }

    private String[][] jsonParserList(String pRecvServerPage, String line, String station) {

        try {
            Log.e("lineManager", pRecvServerPage);
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("realtimeArrivalList");

            String[] jsonName = {"updnLine", "bstatnNm", "arvlMsg3", "btrainNo", "ordkey", "subwayId"};
            String[][] parseredData = new String[jArr.length()][jsonName.length];
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                if(json.getString("subwayId").equals(line)) {
                    String str_line = json.getString("subwayId");
                    String str_no = json.getString("btrainNo");
                    String str_dir = json.getString("updnLine");
                    String str_dst = json.getString("bstatnNm");
                    String str_station = json.getString("arvlMsg3");
                    String str_status = json.getString("arvlMsg2").split("\\(")[0].trim().replace("[", "").replace("]", "");
                    String str = json.getString("ordkey");
                    int i_time = json.getInt("barvlDt");
                    int arvlCd = json.getInt("arvlCd");
                    char[] chr = new char[5];
                    str.getChars(0, 5, chr, 0);
                    String str_sort = String.valueOf(chr);
                    //String str_express = json.getString("");

                    listtrain = new ListTrain(station, str_line, str_no, str_dir, str_dst, str_station, str_status, str_sort, "", i_time, arvlCd);
                    arTrainList.add(listtrain);
                }
                /*for(int j = 0; j < jsonName.length; j++) {
                    if(json.getString("subwayId").equals(lineManager)) {
                        if (jsonName[j].equals("ordkey")) {
                            String str = json.getString(jsonName[j]);
                            char[] chr = new char[5];
                            str.getChars(0, 5, chr, 0);
                            parseredData[i][j] = String.valueOf(chr);

                        } else {
                            parseredData[i][j] = json.getString(jsonName[j]);
                        }
                    }
                }*/
            }

            /*for(int i=0; i<parseredData.length; i++) {
                Log.i(String.format(Locale.KOREA, "%2d : ", i + 1), String.format(Locale.KOREA, "%4s\t%5s\t%5s\t%10s", parseredData[i][0], parseredData[i][1], parseredData[i][2], parseredData[i][4]));
            }*/

            bool_done = true;

            return parseredData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    static class CompareLine implements Comparator<ListTrain> {

        @Override
        public int compare(ListTrain o1, ListTrain o2) {
            // TODO Auto-generated method stub
            return o1.getLine().compareTo(o2.getLine());
        }
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            if(iTime > 0) {
                if(bool_done) {
                    iTime--;
                }
            } else {
                getRealFavorite();
                imgTime.startAnimation(animSpin);
                iTime = iSec;
            }

            if(iTime == 0 && iSec == 0) {
                getRealFavorite();
                imgTime.startAnimation(animSpin);
            } else {
                Log.e("iTime >>>>>> ", "iTime = " + iTime);
                mHandler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeMessages(0);
        myAsyncTask.cancel(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        appStart();
        adapter.notifyDataSetChanged();
        iTime = 0;
        if(iSec != 0) {
            mHandler.sendEmptyMessage(0);
        }
    }
}
