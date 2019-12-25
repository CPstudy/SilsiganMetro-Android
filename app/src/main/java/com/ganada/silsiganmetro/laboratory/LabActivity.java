package com.ganada.silsiganmetro.laboratory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ganada.silsiganmetro.R;
import com.ganada.silsiganmetro.activity.BundangActivity;
import com.ganada.silsiganmetro.activity.FavoriteActivity;
import com.ganada.silsiganmetro.activity.GonghangActivity;
import com.ganada.silsiganmetro.activity.HelperActivity;
import com.ganada.silsiganmetro.activity.KChoonActivity;
import com.ganada.silsiganmetro.activity.KJoongActivity;
import com.ganada.silsiganmetro.activity.Line1Activity;
import com.ganada.silsiganmetro.activity.Line2Activity;
import com.ganada.silsiganmetro.activity.Line3Activity;
import com.ganada.silsiganmetro.activity.Line4Activity;
import com.ganada.silsiganmetro.activity.Line5Activity;
import com.ganada.silsiganmetro.activity.Line6Activity;
import com.ganada.silsiganmetro.activity.Line7Activity;
import com.ganada.silsiganmetro.activity.Line8Activity;
import com.ganada.silsiganmetro.activity.Line9Activity;
import com.ganada.silsiganmetro.activity.SettingActivity;
import com.ganada.silsiganmetro.activity.ShinBundangActivity;
import com.ganada.silsiganmetro.activity.SuinActivity;
import com.ganada.silsiganmetro.MetroApplication;
import com.ganada.silsiganmetro.listitem.DataList;
import com.ganada.silsiganmetro.util.CalcDate;
import com.ganada.silsiganmetro.util.DBManager;
import com.ganada.silsiganmetro.util.ThemeManager;
import com.ganada.silsiganmetro.view.CustomTitlebar;

import java.util.ArrayList;
import java.util.Calendar;

public class LabActivity extends Activity {

    private SharedPreferences mPref;
    private SharedPreferences.Editor mPrefEdit;

    MetroApplication sv;
    DBManager dm;
    CalcDate cd;
    CustomListAdapter adapter;
    ArrayList<DataList> arCustomList;
    ArrayList<DataList> arSortList;
    CustomTitlebar titleBar;
    TextView txt_main;
    ImageButton btnFavorite;
    ImageButton btnSetting;
    View marginView;

    String today;
    String sDayWeek;
    boolean bool_weekend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPref = getSharedPreferences("Pref1", 0);
        mPrefEdit = mPref.edit();
        dm = new DBManager(this);
        cd = new CalcDate();
        sv = (MetroApplication) getApplication();

        //Typeface face = Typeface.createFromAsset(getAssets(),"fonts/NanumSquareR.otf");
        txt_main = findViewById(R.id.txtMain);
        btnFavorite = findViewById(R.id.btn_favorite);
        btnSetting = findViewById(R.id.btnSetting);
        titleBar = findViewById(R.id.layTitle);
        marginView = getLayoutInflater().inflate(R.layout.footer_main, null, false);

        Window window = getWindow();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        arSortList = new ArrayList<>();
        arCustomList = new ArrayList<>();
        arCustomList = dm.getDBList();

        adapter = new CustomListAdapter(this, R.layout.item_main, arSortList);

        ListView list;
        list = findViewById(R.id.list);
        list.setAdapter(adapter);
        list.addFooterView(marginView);
        list.addHeaderView(marginView);

        getWeekend();
        setTime();
        checkNetwork();

        if(mPref.getInt("helper", 0) != 1) {
            Intent intent = new Intent(this, HelperActivity.class);
            startActivity(intent);
        }

        btnFavorite.setOnClickListener(new View.OnClickListener() {
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
        splitString();
        adapter.notifyDataSetChanged();
    }

    public void splitString() {
        arSortList.clear();
        arSortList.addAll(arCustomList);
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
        // TODO: 2018-07-14 여기 고치세요.
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

    public boolean checkNetwork() {
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
            AlertDialog.Builder dlg = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
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
        LinearLayout layoutLine;
        LinearLayout layoutStation;
    }

    class CustomListAdapter extends BaseAdapter {
        Context con;
        LayoutInflater inflacter;
        ArrayList<DataList> lines;
        int layout;

        public CustomListAdapter(Context context, int layout, ArrayList<DataList> lines) {
            con = context;
            inflacter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

            final LineType lineType = LineType.getLine(lines.get(position).line);

            if (convertView == null) {
                convertView = inflacter.inflate(layout, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.txt1 = convertView.findViewById(R.id.txt_line_name1);
                viewHolder.txt2 = convertView.findViewById(R.id.txt_line_way1);
                viewHolder.txtLineNumber = convertView.findViewById(R.id.txtLineNumber);
                viewHolder.txtLineStation = convertView.findViewById(R.id.txtLineStation);
                viewHolder.img1 = convertView.findViewById(R.id.img_info1);
                viewHolder.layoutLine = convertView.findViewById(R.id.layoutLine);
                viewHolder.layoutStation = convertView.findViewById(R.id.layoutStation);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if(lines.get(position).none == 0) {
                viewHolder.layoutLine.setVisibility(View.GONE);
                viewHolder.layoutStation.setVisibility(View.VISIBLE);
                viewHolder.txtLineNumber.setText(
                        lineType.getLineName()
                                .replace("호선", "")
                                .replace("선", "")
                );
                viewHolder.txtLineStation.setText(lineType.getDescription().split(":")[0]);
                viewHolder.txtLineNumber.setBackgroundResource(
                        new ThemeManager(getBaseContext()).getCircleLineColor(lineType.getColorId())
                );
            } else {
                viewHolder.layoutLine.setVisibility(View.VISIBLE);
                viewHolder.layoutStation.setVisibility(View.GONE);
                viewHolder.txt1.setText(lineType.getLineName());
                viewHolder.txt2.setText(lineType.getDescription());
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 토스트 메세지로 strList 출력
                    if(lines.get(position).none == 0) {
                        sv.setPosition(Integer.parseInt(lineType.getDescription().split(":")[1]));
                    } else {
                        sv.setPosition(-1);
                    }

                    Intent intent = new Intent(LabActivity.this, LineActivity.class);
                    intent.putExtra(MetroConstant.KEY_LINE_NUMBER, lineType.getLineNumber());
                    startActivity(intent);
                }
            });

            viewHolder.img1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), lineType.getColorId()));

            return convertView;
        }
    }

}