package com.ganada.silsiganmetro.laboratory;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.ganada.silsiganmetro.R;
import com.ganada.silsiganmetro.listitem.LineStation;
import com.ganada.silsiganmetro.listitem.TrainList;
import com.ganada.silsiganmetro.real.Realtime;
import com.ganada.silsiganmetro.util.DBManager;
import com.ganada.silsiganmetro.util.LineManager;
import com.ganada.silsiganmetro.util.ThemeManager;
import com.ganada.silsiganmetro.view.CustomTitlebar;
import com.ganada.silsiganmetro.view.RefreshButton;
import com.ganada.silsiganmetro.view.TrainLocation;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LineActivity extends Activity {

    LineManager lineManager;
    ThemeManager tm;
    DBManager dm;
    LineType lineType;
    StationAdapter adapter;

    ArrayList<LineStation> arrStation = new ArrayList<>();
    ArrayList<TrainList> arrTrain = new ArrayList<>();
    HashMap<String, LineStation> maps = new HashMap<>();

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.testTrain)
    TrainLocation testTrain;
    @BindView(R.id.btnRefresh)
    RefreshButton btnRefresh;
    @BindView(R.id.titleBar)
    CustomTitlebar titleBar;

    int lineNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        ButterKnife.bind(this);

        lineManager = new LineManager(getApplicationContext());
        tm = new ThemeManager(getApplicationContext());
        dm = new DBManager(getApplicationContext());


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

        adapter = new StationAdapter(arrStation, tm, lineNumber);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new TrainScrollListener());

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


    private class TrainScrollListener extends RecyclerView.OnScrollListener {
        int scroll = 0;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            scroll += dy;
            testTrain.setTranslationY(scroll * -1);
        }
    }

    private static class TrainAsync extends AsyncTask<String, String, String> {

        private final WeakReference<LineActivity> ref;
        Realtime rt;

        TrainAsync(LineActivity context) {
            ref = new WeakReference<>(context);
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

            activity.arrTrain = rt.getTrainList(activity.getApplicationContext(), values[0]);

            activity.testTrain.clearView();

            for(int i = 0; i < activity.arrTrain.size(); i++) {
                if(activity.maps.get(activity.arrTrain.get(i).stationNo) != null) {
                    activity.testTrain.addTrain(
                            activity.arrTrain.get(i).trainDst,
                            activity.arrTrain.get(i).trainNo,
                            activity.arrTrain.get(i).trainSttus,
                            activity.arrTrain.get(i).updown,
                            activity.maps.get(activity.arrTrain.get(i).stationNo).posY
                    );
                }
            }
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
            publishProgress(result);


            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            LineActivity activity = ref.get();
            if(activity == null || activity.isFinishing()) return;

            activity.btnRefresh.stopAnimation();
            Log.e("arrTrain", activity.arrTrain.size() + "");
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
