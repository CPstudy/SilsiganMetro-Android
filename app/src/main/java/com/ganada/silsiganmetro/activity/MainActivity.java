package com.ganada.silsiganmetro.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ganada.silsiganmetro.R;
import com.ganada.silsiganmetro.MetroApplication;
import com.ganada.silsiganmetro.common.Important;
import com.ganada.silsiganmetro.listitem.DataList;
import com.ganada.silsiganmetro.util.CalcDate;
import com.ganada.silsiganmetro.util.DBManager;
import com.ganada.silsiganmetro.util.LineManager;
import com.ganada.silsiganmetro.util.ThemeManager;
import com.ganada.silsiganmetro.util.Units;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private SharedPreferences mPref;
    private SharedPreferences.Editor mPrefEdit;

    MetroApplication sv;
    DBManager dm;
    ThemeManager tm;
    CalcDate cd;
    MainListAdapter adapter;
    ArrayList<DataList> datas;
    ArrayList<DataList> lines;
    TextView txt_main;
    ImageButton btn_favorite;
    ImageButton btnSetting;
    View marginView;
    LinearLayout layoutBack;

    int iTheme;
    String today;
    String sDayWeek;
    boolean bool_weekend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPref = getSharedPreferences("Pref1", 0);
        mPrefEdit = mPref.edit();
        iTheme = mPref.getInt("iTheme", 0);
        dm = new DBManager(this);
        tm = new ThemeManager(getBaseContext());
        cd = new CalcDate();
        sv = (MetroApplication) getApplication();

        Window window = getWindow();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), tm.getTitleBarColor(iTheme, 0)));
        }

        //Typeface face = Typeface.createFromAsset(getAssets(),"fonts/NanumSquareR.otf");
        txt_main = findViewById(R.id.txtMain);
        btn_favorite = findViewById(R.id.btn_favorite);
        btnSetting = findViewById(R.id.btnSetting);
        layoutBack = findViewById(R.id.layoutBack);
        marginView = getLayoutInflater().inflate(R.layout.footer_main, null, false);

        //txt_main.setTypeface(face);

        MobileAds.initialize(getApplicationContext(), Important.ADMOB_KEY);
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        lines = new ArrayList<>();
        datas = new ArrayList<>();
        datas = dm.getDBList();
        adapter = new MainListAdapter(this, R.layout.item_main, lines);

        layoutBack.setBackgroundColor(tm.getBackgroundColor(iTheme));

        ListView list;
        list = findViewById(R.id.list);
        list.setAdapter(adapter);
        list.addFooterView(marginView);
        list.addHeaderView(marginView);
        list.setDivider(new ColorDrawable(tm.getBackgroundColor(iTheme)));
        list.setDividerHeight(Units.dp(5));

        getWeekend();
        setTime();
        CheckNetwork();

        if(mPref.getInt("helper", 0) != 1) {
            Intent intent = new Intent(this, HelperActivity.class);
            startActivity(intent);
        }

        btn_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FavoriteActivity.class);
                startActivity(intent);
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        datas = dm.getDBList();
        splitString();
        adapter.notifyDataSetChanged();
    }

    public void splitString() {
        lines.clear();
        lines.addAll(datas);
    }

    private void getWeekend() {
        if(cd.getDayWeek() == 0) {
            bool_weekend = false;
        } else {
            bool_weekend = true;
        }
    }

    public void setTime() {
        String sWeek[] = {"일", "월", "화", "수", "목", "금", "토"};
        Calendar calendar;
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        sDayWeek = sWeek[week - 1];

        String sYear = "" + year;
        String sMonth;
        String sDay;

        if (month < 10) {
            sMonth = "0" + month;
        } else {
            sMonth = "" + month;
        }

        if (day < 10) {
            sDay = "0" + day;
        } else {
            sDay = "" + day;
        }

        today = sYear + sMonth + sDay;
    }

    public void StartFavor() {
        if(mPref.getInt("linefavor", 0) == 1) {
            Intent intent = new Intent(getApplicationContext(), Line1Activity.class);
            intent.putExtra("bool_weekend", bool_weekend);
            startActivity(intent);
        } else if(mPref.getInt("linefavor", 0) == 2) {
            Intent intent = new Intent(getApplicationContext(), Line2Activity.class);
            startActivity(intent);
        } else if(mPref.getInt("linefavor", 0) == 3) {
            Intent intent = new Intent(getApplicationContext(), Line3Activity.class);
            intent.putExtra("bool_weekend", bool_weekend);
            startActivity(intent);
        } else if(mPref.getInt("linefavor", 0) == 4) {
            Intent intent = new Intent(getApplicationContext(), Line4Activity.class);
            intent.putExtra("bool_weekend", bool_weekend);
            startActivity(intent);
        } else if(mPref.getInt("linefavor", 0) == 5) {
            Intent intent = new Intent(getApplicationContext(), Line5Activity.class);
            startActivity(intent);
        } else if(mPref.getInt("linefavor", 0) == 6) {
            Intent intent = new Intent(getApplicationContext(), Line6Activity.class);
            startActivity(intent);
        } else if(mPref.getInt("linefavor", 0) == 7) {
            Intent intent = new Intent(getApplicationContext(), Line7Activity.class);
            startActivity(intent);
        } else if(mPref.getInt("linefavor", 0) == 8) {
            Intent intent = new Intent(getApplicationContext(), Line8Activity.class);
            startActivity(intent);
        } else if(mPref.getInt("linefavor", 0) == 9) {
            Intent intent = new Intent(getApplicationContext(), Line9Activity.class);
            startActivity(intent);
        } else if(mPref.getInt("linefavor", 0) == 10) {
            Intent intent = new Intent(getApplicationContext(), KJoongActivity.class);
            startActivity(intent);
        } else if(mPref.getInt("linefavor", 0) == 11) {
            Intent intent = new Intent(getApplicationContext(), BundangActivity.class);
            startActivity(intent);
        } else if(mPref.getInt("linefavor", 0) == 12) {
            Intent intent = new Intent(getApplicationContext(), SuinActivity.class);
            startActivity(intent);
        } else if(mPref.getInt("linefavor", 0) == 13) {
            Intent intent = new Intent(getApplicationContext(), ShinBundangActivity.class);
            startActivity(intent);
        } else if(mPref.getInt("linefavor", 0) == 14) {
            Intent intent = new Intent(getApplicationContext(), KChoonActivity.class);
            startActivity(intent);
        } else if(mPref.getInt("linefavor", 0) == 15) {
            Intent intent = new Intent(getApplicationContext(), GonghangActivity.class);
            startActivity(intent);
        } else if(mPref.getInt("linefavor", 0) == 16) {
            Intent intent = new Intent(getApplicationContext(), FavoriteActivity.class);
            startActivity(intent);
        } else if(mPref.getInt("linefavor", 0) == 0) {
        }
    }

    public boolean CheckNetwork() {
        ConnectivityManager manager =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo lte_4g = manager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);
        boolean blte_4g = false;
        if(lte_4g != null) {
            blte_4g = lte_4g.isConnected();
        }

        if (mobile.isConnected() || blte_4g) {
            StartFavor();
            return true;
        } else if(wifi.isConnected()) {
            StartFavor();
            return true;
        } else {
            AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
            dlg.setTitle("네트워크 오류");
            dlg.setMessage("네트워크 연결 상태를 확인하십시오.");
            dlg.setNegativeButton("종료", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    finish();

                }
            });
            dlg.setCancelable(false);
            dlg.show();
            return false;
        }
    }

    public class ViewHolder {
        TextView txt1;
        TextView txt2;
        TextView txtLineNumber;
        TextView txtLineStation;
        View img1;
        FrameLayout layoutRoot;
        LinearLayout layoutLine;
        LinearLayout layoutStation;
    }

    class MainListAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflater;
        ArrayList<DataList> lines;
        int layout;

        public MainListAdapter(Context context, int layout, ArrayList<DataList> lines) {
            this.context = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.lines = lines;
            this.layout = layout;
        }

        // 어댑터의 항목 수 조사
        @Override
        public int getCount() {
            return lines.size();
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
            ViewHolder viewHolder;
            final String strLine = lines.get(position).line;

            if (convertView == null) {
                convertView = inflater.inflate(layout, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.txt1 = convertView.findViewById(R.id.txt_line_name1);
                viewHolder.txt2 = convertView.findViewById(R.id.txt_line_way1);
                viewHolder.txtLineNumber =  convertView.findViewById(R.id.txtLineNumber);
                viewHolder.txtLineStation = convertView.findViewById(R.id.txtLineStation);
                viewHolder.img1 = convertView.findViewById(R.id.img_info1);
                viewHolder.layoutRoot = convertView.findViewById(R.id.layoutRoot);
                viewHolder.layoutLine = convertView.findViewById(R.id.layoutLine);
                viewHolder.layoutStation = convertView.findViewById(R.id.layoutStation);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if(lines.get(position).none == 0) {
                viewHolder.layoutLine.setVisibility(View.GONE);
                viewHolder.layoutStation.setVisibility(View.VISIBLE);
                viewHolder.txtLineNumber.setText(lines.get(position).line.replace("호선", "").replace("선", ""));
                viewHolder.txtLineStation.setText(lines.get(position).text.split(":")[0]);
                viewHolder.txtLineNumber.setBackgroundResource(new ThemeManager(getBaseContext()).getCircleLineColor(new LineManager(getApplicationContext()).getLineNum(strLine)));
            } else {
                viewHolder.layoutLine.setVisibility(View.VISIBLE);
                viewHolder.layoutStation.setVisibility(View.GONE);
                viewHolder.txt1.setText(lines.get(position).line);
                viewHolder.txt2.setText(lines.get(position).text);
            }

            viewHolder.layoutRoot.setBackgroundResource(tm.getListBackground(iTheme));
            viewHolder.txtLineNumber.setTextColor(Color.parseColor("#ffffff"));
            viewHolder.txtLineStation.setTextColor(ContextCompat.getColor(getBaseContext(), tm.getStationColor(iTheme)));
            viewHolder.txt1.setTextColor(ContextCompat.getColor(getBaseContext(), tm.getStationColor(iTheme)));
            viewHolder.txt2.setTextColor(ContextCompat.getColor(getBaseContext(), tm.getSubtextColor(iTheme)));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 토스트 메세지로 strList 출력
                    if(lines.get(position).none == 0) {
                        sv.setPosition(Integer.parseInt(lines.get(position).text.split(":")[1]));
                    } else {
                        sv.setPosition(-1);
                    }
                    if(strLine.equals("1호선")) {
                        Intent intent = new Intent(getApplicationContext(), Line1Activity.class);
                        intent.putExtra("bool_weekend", bool_weekend);
                        startActivity(intent);
                    } else if(strLine.equals("2호선")) {
                        Intent intent = new Intent(getApplicationContext(), Line2Activity.class);
                        startActivity(intent);
                    } else if(strLine.equals("3호선")) {
                        Intent intent = new Intent(getApplicationContext(), Line3Activity.class);
                        intent.putExtra("bool_weekend", bool_weekend);
                        startActivity(intent);
                    } else if(strLine.equals("4호선")) {
                        Intent intent = new Intent(getApplicationContext(), Line4Activity.class);
                        intent.putExtra("bool_weekend", bool_weekend);
                        startActivity(intent);
                    } else if(strLine.equals("5호선")) {
                        Intent intent = new Intent(getApplicationContext(), Line5Activity.class);
                        startActivity(intent);
                    } else if(strLine.equals("6호선")) {
                        Intent intent = new Intent(getApplicationContext(), Line6Activity.class);
                        startActivity(intent);
                    } else if(strLine.equals("7호선")) {
                        Intent intent = new Intent(getApplicationContext(), Line7Activity.class);
                        startActivity(intent);
                    } else if(strLine.equals("8호선")) {
                        Intent intent = new Intent(getApplicationContext(), Line8Activity.class);
                        startActivity(intent);
                    } else if(strLine.equals("9호선")) {
                        Intent intent = new Intent(getApplicationContext(), Line9Activity.class);
                        startActivity(intent);
                    } else if(strLine.equals("경의 · 중앙선")) {
                        Intent intent = new Intent(getApplicationContext(), KJoongActivity.class);
                        startActivity(intent);
                    } else if(strLine.equals("분당선")) {
                        Intent intent = new Intent(getApplicationContext(), BundangActivity.class);
                        startActivity(intent);
                    } else if(strLine.equals("수인선")) {
                        Intent intent = new Intent(getApplicationContext(), SuinActivity.class);
                        startActivity(intent);
                    } else if(strLine.equals("신분당선")) {
                        Intent intent = new Intent(getApplicationContext(), ShinBundangActivity.class);
                        startActivity(intent);
                    } else if(strLine.equals("경춘선")) {
                        Intent intent = new Intent(getApplicationContext(), KChoonActivity.class);
                        startActivity(intent);
                    } else if(strLine.equals("공항철도")) {
                        Intent intent = new Intent(getApplicationContext(), GonghangActivity.class);
                        startActivity(intent);
                    }
                }
            });

            switch(lines.get(position).line) {
                case "1호선":
                    viewHolder.img1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_1));
                    break;

                case "2호선":
                    viewHolder.img1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_2));
                    break;

                case "3호선":
                    viewHolder.img1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_3));
                    break;

                case "4호선":
                    viewHolder.img1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_4));
                    break;

                case "5호선":
                    viewHolder.img1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_5));
                    break;

                case "6호선":
                    viewHolder.img1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_6));
                    break;

                case "7호선":
                    viewHolder.img1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_7));
                    break;

                case "8호선":
                    viewHolder.img1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_8));
                    break;

                case "9호선":
                    viewHolder.img1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_9));
                    break;

                case "경의 · 중앙선":
                    viewHolder.img1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_kj));
                    break;

                case "분당선":
                    viewHolder.img1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_bs));
                    break;

                case "수인선":
                    viewHolder.img1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_bs));
                    break;

                case "신분당선":
                    viewHolder.img1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_sb));
                    break;

                case "경춘선":
                    viewHolder.img1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_kc));
                    break;

                case "공항철도":
                    viewHolder.img1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_gh));
                    break;

            }

            return convertView;
        }
    }

}
