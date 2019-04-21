package com.ganada.silsiganmetro.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ganada.silsiganmetro.util.LineManager;
import com.ganada.silsiganmetro.view.CustomToast;
import com.ganada.silsiganmetro.R;
import com.ganada.silsiganmetro.view.StrokeTextView;
import com.ganada.silsiganmetro.util.ThemeManager;
import com.guk2zzada.sandwich.Sandwich;


import java.util.Locale;

public class ThemeActivity extends Activity {

    private SharedPreferences mPref;
    private SharedPreferences.Editor mPrefEdit;

    LinearLayout layout_1;
    LinearLayout layout_2;
    LinearLayout layout_3;
    View layout_single_left;
    View layout_single_right;
    View layout_double_left;
    View layout_double_right;
    View layout_old_single_right;
    View layout_old_double_right;
    View layout_single_left_divider;
    View layout_single_right_divider;
    View layout_double_left_divider;
    View layout_double_right_divider;

    SeekBar skb_font;
    SeekBar skb_station;
    SeekBar skb_list;

    Button btn_theme_new;
    Button btn_theme_old;
    Button btn_theme_black;
    Button btn_theme_4;
    Button btn_left;
    Button btn_right;
    Button btn_info_yes;
    Button btn_info_no;
    Button btn_status_yes;
    Button btn_status_no;
    Button btn_type_yes;
    Button btn_type_no;
    Button btn_list_small;
    Button btn_list_large;
    Button btn_font_small;
    Button btn_font_medium;
    Button btn_font_large;

    TextView txt_font_size;
    TextView txt_station_size;
    TextView txt_list_size;
    TextView txt_single_left_up;
    TextView txt_single_left_down;
    TextView txt_single_right_up;
    TextView txt_single_right_down;
    TextView txt_double_left_up_express;
    TextView txt_double_left_up_norm;
    TextView txt_double_left_down_express;
    TextView txt_double_left_down_norm;
    TextView txt_double_right_up_express;
    TextView txt_double_right_up_norm;
    TextView txt_double_right_down_express;
    TextView txt_double_right_down_norm;
    TextView txt_single_left_station;
    TextView txt_single_right_station;
    StrokeTextView txt_double_left_station;
    StrokeTextView txt_double_right_station;
    TextView txt_old_single_right_up;
    TextView txt_old_single_right_down;
    TextView txt_old_double_right_up_norm;
    TextView txt_old_double_right_up_express;
    TextView txt_old_double_right_down_norm;
    TextView txt_old_double_right_down_express;
    TextView txt_old_single_station;
    StrokeTextView txt_old_double_station;
    
    ImageView img_single_left_up;
    ImageView img_single_left_down;
    ImageView img_single_right_up;
    ImageView img_single_right_down;
    ImageView img_double_left_up_norm;
    ImageView img_double_left_up_express;
    ImageView img_double_left_down_norm;
    ImageView img_double_left_down_express;
    ImageView img_double_right_up_norm;
    ImageView img_double_right_up_express;
    ImageView img_double_right_down_norm;
    ImageView img_double_right_down_express;

    ThemeManager tm;

    String str_des = "열차행선지";
    String str_yes = "S1234";
    String str_no = "1234";
    String str_status = "상태";

    int i_theme = 0;
    int i_train_icon = 0;
    int i_train_info = 1;
    int i_train_type = 0;
    int i_train_status = 0;
    int i_list_size = 66;
    int i_station_font_size = 1;
    int i_train_font_size = 1;
    int i_font_cut = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        tm = new ThemeManager(getBaseContext());
        mPref = getSharedPreferences("Pref1", 0);
        mPrefEdit = mPref.edit();

        layout_1 = findViewById(R.id.layout_1);
        layout_2 = findViewById(R.id.layout_2);
        layout_3 = findViewById(R.id.layout_3);
        layout_single_left = findViewById(R.id.layout_single_left);
        layout_single_right = findViewById(R.id.layout_single_right);
        layout_double_left = findViewById(R.id.layout_double_left);
        layout_double_right = findViewById(R.id.layout_double_right);
        layout_old_single_right = findViewById(R.id.layout_old_single_right);
        layout_old_double_right = findViewById(R.id.layout_old_double_right);
        layout_single_left_divider = layout_single_left.findViewById(R.id.view_divider);
        layout_single_right_divider = layout_single_right.findViewById(R.id.view_divider);
        layout_double_left_divider = layout_double_left.findViewById(R.id.view_divider);
        layout_double_right_divider = layout_double_right.findViewById(R.id.view_divider);

        skb_font = findViewById(R.id.skb_font);
        skb_station = findViewById(R.id.skb_station);
        skb_list = findViewById(R.id.skb_list);

        txt_font_size = findViewById(R.id.txt_font_size);
        txt_station_size = findViewById(R.id.txt_station_size);
        txt_list_size = findViewById(R.id.txt_list_size);
        txt_single_left_up = layout_single_left.findViewById(R.id.train_up_01);
        txt_single_left_down = layout_single_left.findViewById(R.id.train_down_01);
        txt_single_right_up = layout_single_right.findViewById(R.id.train_up_01);
        txt_single_right_down = layout_single_right.findViewById(R.id.train_down_01);
        txt_double_left_up_express = layout_double_left.findViewById(R.id.train_up_express);
        txt_double_left_up_norm = layout_double_left.findViewById(R.id.train_up_norm);
        txt_double_left_down_express = layout_double_left.findViewById(R.id.train_down_express);
        txt_double_left_down_norm = layout_double_left.findViewById(R.id.train_down_norm);
        txt_double_right_up_express = layout_double_right.findViewById(R.id.train_up_express);
        txt_double_right_up_norm = layout_double_right.findViewById(R.id.train_up_norm);
        txt_double_right_down_express = layout_double_right.findViewById(R.id.train_down_express);
        txt_double_right_down_norm = layout_double_right.findViewById(R.id.train_down_norm);
        txt_single_left_station = layout_single_left.findViewById(R.id.txt_station);
        txt_single_right_station = layout_single_right.findViewById(R.id.txt_station);
        txt_double_left_station = layout_double_left.findViewById(R.id.txt_station);
        txt_double_right_station = layout_double_right.findViewById(R.id.txt_station);
        txt_old_single_right_up = layout_old_single_right.findViewById(R.id.train_up_01);
        txt_old_single_right_down = layout_old_single_right.findViewById(R.id.train_down_01);
        txt_old_double_right_up_norm = layout_old_double_right.findViewById(R.id.train_up_norm);
        txt_old_double_right_up_express = layout_old_double_right.findViewById(R.id.train_up_express);
        txt_old_double_right_down_norm = layout_old_double_right.findViewById(R.id.train_down_norm);
        txt_old_double_right_down_express = layout_old_double_right.findViewById(R.id.train_down_express);
        txt_old_single_station = layout_old_single_right.findViewById(R.id.txt_station);
        txt_old_double_station = layout_old_double_right.findViewById(R.id.txt_station);

        btn_theme_new = findViewById(R.id.btn_theme_new);
        btn_theme_old = findViewById(R.id.btn_theme_old);
        btn_theme_black = findViewById(R.id.btn_theme_black);
        btn_theme_4 = findViewById(R.id.btn_theme_4);
        btn_left = findViewById(R.id.btn_left);
        btn_right = findViewById(R.id.btn_right);
        btn_info_yes = findViewById(R.id.btn_info_yes);
        btn_info_no = findViewById(R.id.btn_info_no);
        btn_status_yes = findViewById(R.id.btn_status_yes);
        btn_status_no = findViewById(R.id.btn_status_no);
        btn_type_yes = findViewById(R.id.btn_type_yes);
        btn_type_no = findViewById(R.id.btn_type_no);
        btn_list_small = findViewById(R.id.btn_list_small);
        btn_list_large = findViewById(R.id.btn_list_large);
        btn_font_small = findViewById(R.id.btn_font_small);
        btn_font_medium = findViewById(R.id.btn_font_medium);
        btn_font_large = findViewById(R.id.btn_font_large);

        img_single_left_up = layout_single_left.findViewById(R.id.imgUp);
        img_single_left_down = layout_single_left.findViewById(R.id.img_icon_down);
        img_single_right_up = layout_single_right.findViewById(R.id.imgUp);
        img_single_right_down = layout_single_right.findViewById(R.id.img_icon_down);
        img_double_left_up_norm = layout_double_left.findViewById(R.id.img_icon_up_norm);
        img_double_left_up_express = layout_double_left.findViewById(R.id.img_icon_up_express);
        img_double_left_down_norm = layout_double_left.findViewById(R.id.img_icon_down_norm);
        img_double_left_down_express = layout_double_left.findViewById(R.id.img_icon_down_express);
        img_double_right_up_norm = layout_double_right.findViewById(R.id.img_icon_up_norm);
        img_double_right_up_express = layout_double_right.findViewById(R.id.img_icon_up_express);
        img_double_right_down_norm = layout_double_right.findViewById(R.id.img_icon_down_norm);
        img_double_right_down_express = layout_double_right.findViewById(R.id.img_icon_down_express);

        Window window = getWindow();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), tm.getTitleBarColor(ThemeManager.VERSION_NEW, 0)));
        }

        i_theme = mPref.getInt("iTheme", 0);
        i_train_icon = mPref.getInt("iLineStatus", 0);
        i_train_info = mPref.getInt("boolInfo", 1);
        i_train_type = mPref.getInt("boolType", 1);
        i_train_status = mPref.getInt("iTrainStatus", 0);
        i_list_size = mPref.getInt("iListSize", 66);
        i_font_cut = mPref.getInt("iFontCut", 0);
        i_station_font_size = tm.getStationFontSize();
        i_train_font_size = tm.getTrainFontSize();

        setButtonColor();
        setListSize();
        setFontSize();
        txt_old_double_station.setStroke(false);
        skb_font.setProgress(i_train_font_size - 6);
        txt_font_size.setText(String.format(Locale.KOREA, "열차 정보 크기: %d", i_train_font_size));
        skb_station.setProgress(i_station_font_size - 11);
        txt_station_size.setText(String.format(Locale.KOREA, "역 명 크기: %d", i_station_font_size));
        skb_list.setProgress(i_list_size - 50);
        txt_list_size.setText(String.format(Locale.KOREA, "높이: %d", i_list_size));

        skb_font.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i_train_font_size = i + 6;

                txt_font_size.setText(String.format(Locale.KOREA, "열차 정보 크기: %d", i_train_font_size));

                tm.setTrainFontSize(i_train_font_size);
                setFontSize();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        skb_station.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i_station_font_size = i + 11;

                txt_station_size.setText(String.format(Locale.KOREA, "역 명 크기: %d", i_station_font_size));

                tm.setStationFontSize(i_station_font_size);
                setFontSize();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        skb_list.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i_list_size = i + 50;

                txt_list_size.setText(String.format(Locale.KOREA, "높이: %d", i_list_size));

                mPrefEdit.putInt("iListSize", i_list_size);
                mPrefEdit.apply();
                mPrefEdit.commit();
                setListSize();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btn_theme_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i_theme = 0;
                setButtonColor();

                mPrefEdit.putInt("iTheme", i_theme);
                mPrefEdit.apply();
                mPrefEdit.commit();
            }
        });

        btn_theme_old.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i_theme = 1;
                setButtonColor();

                mPrefEdit.putInt("iTheme", i_theme);
                mPrefEdit.apply();
                mPrefEdit.commit();

                Sandwich.makeText(getApplicationContext(), "구 버전은 열차 아이콘이 움직이지 않습니다.", Sandwich.LENGTH_SHORT).show();
            }
        });

        btn_theme_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i_theme = 2;
                setButtonColor();

                mPrefEdit.putInt("iTheme", i_theme);
                mPrefEdit.apply();
                mPrefEdit.commit();
            }
        });

        btn_theme_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i_theme = 3;
                setButtonColor();

                mPrefEdit.putInt("iTheme", i_theme);
                mPrefEdit.apply();
                mPrefEdit.commit();
            }
        });

        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i_train_icon = 0;
                layout_2.setVisibility(View.GONE);
                setButtonColor();

                mPrefEdit.putInt("iLineStatus", i_train_icon);
                mPrefEdit.apply();
                mPrefEdit.commit();
            }
        });

        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i_train_icon = 1;
                layout_2.setVisibility(View.VISIBLE);
                setButtonColor();

                mPrefEdit.putInt("iLineStatus", i_train_icon);
                mPrefEdit.apply();
                mPrefEdit.commit();
            }
        });

        btn_info_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i_train_info = 1;
                setButtonColor();

                mPrefEdit.putInt("boolInfo", i_train_info);
                mPrefEdit.apply();
                mPrefEdit.commit();
            }
        });

        btn_info_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i_train_info = 0;
                setButtonColor();

                mPrefEdit.putInt("boolInfo", i_train_info);
                mPrefEdit.apply();
                mPrefEdit.commit();
            }
        });

        btn_status_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i_train_status = 1;

                mPrefEdit.putInt("iTrainStatus", i_train_status);
                mPrefEdit.apply();
                mPrefEdit.commit();

                setButtonColor();
            }
        });

        btn_status_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i_train_status = 0;

                mPrefEdit.putInt("iTrainStatus", i_train_status);
                mPrefEdit.apply();
                mPrefEdit.commit();

                setButtonColor();
            }
        });

        btn_type_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i_train_type = 1;

                mPrefEdit.putInt("boolType", i_train_type);
                mPrefEdit.apply();
                mPrefEdit.commit();

                setButtonColor();
            }
        });

        btn_type_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i_train_type = 0;

                mPrefEdit.putInt("boolType", i_train_type);
                mPrefEdit.apply();
                mPrefEdit.commit();

                setButtonColor();
            }
        });

        btn_list_small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i_list_size = 55;

                mPrefEdit.putInt("iListSize", i_list_size);
                mPrefEdit.apply();
                mPrefEdit.commit();

                setListSize();
            }
        });

        btn_list_large.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i_list_size = 66;

                mPrefEdit.putInt("iListSize", i_list_size);
                mPrefEdit.apply();
                mPrefEdit.commit();

                setListSize();
            }
        });

        btn_font_small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i_font_cut = 0;

                mPrefEdit.putInt("iFontCut", i_font_cut);
                mPrefEdit.apply();
                mPrefEdit.commit();

                setButtonColor();
            }
        });

        btn_font_medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i_font_cut = 1;

                mPrefEdit.putInt("iFontCut", i_font_cut);
                mPrefEdit.apply();
                mPrefEdit.commit();

                setButtonColor();
            }
        });

        btn_font_large.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i_font_cut = 2;

                mPrefEdit.putInt("iFontCut", i_font_cut);
                mPrefEdit.apply();
                mPrefEdit.commit();

                setButtonColor();
            }
        });
    }

    private void setButtonColor() {

        layout_single_left.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), tm.getListBackgroundColor(i_theme)));
        layout_double_left.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), tm.getListBackgroundColor(i_theme)));
        layout_single_right.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), tm.getListBackgroundColor(i_theme)));
        layout_double_right.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), tm.getListBackgroundColor(i_theme)));
        txt_single_left_station.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getStationColor(i_theme)));
        txt_double_left_station.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getStationColor(i_theme)));
        txt_single_right_station.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getStationColor(i_theme)));
        txt_double_right_station.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getStationColor(i_theme)));

        layout_single_left_divider.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), tm.getDividerColor(i_theme)));
        layout_single_right_divider.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), tm.getDividerColor(i_theme)));
        layout_double_left_divider.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), tm.getDividerColor(i_theme)));
        layout_double_right_divider.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), tm.getDividerColor(i_theme)));

        txt_double_left_station.setStrokeColor(ContextCompat.getColor(getApplicationContext(), tm.getStrokeColor(i_theme)));
        txt_double_right_station.setStrokeColor(ContextCompat.getColor(getApplicationContext(), tm.getStrokeColor(i_theme)));

        txt_single_left_up.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getTrainColor(i_theme)));
        txt_single_left_down.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getTrainColor(i_theme)));
        txt_single_right_up.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getTrainColor(i_theme)));
        txt_single_right_down.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getTrainColor(i_theme)));
        txt_double_left_up_express.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getTrainColor(i_theme)));
        txt_double_left_up_norm.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getTrainColor(i_theme)));
        txt_double_left_down_express.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getTrainColor(i_theme)));
        txt_double_left_down_norm.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getTrainColor(i_theme)));
        txt_double_right_up_express.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getTrainColor(i_theme)));
        txt_double_right_up_norm.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getTrainColor(i_theme)));
        txt_double_right_down_express.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getTrainColor(i_theme)));
        txt_double_right_down_norm.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getTrainColor(i_theme)));
        txt_old_single_right_up.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getTrainColor(i_theme)));
        txt_old_single_right_down.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getTrainColor(i_theme)));
        txt_old_double_right_up_norm.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getTrainColor(i_theme)));
        txt_old_double_right_up_express.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getTrainColor(i_theme)));
        txt_old_double_right_down_norm.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getTrainColor(i_theme)));
        txt_old_double_right_down_express.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getTrainColor(i_theme)));

        txt_single_left_up.setBackgroundResource(tm.getTrainInfo(i_theme));
        txt_single_left_down.setBackgroundResource(tm.getTrainInfo(i_theme));
        txt_single_right_up.setBackgroundResource(tm.getTrainInfo(i_theme));
        txt_single_right_down.setBackgroundResource(tm.getTrainInfo(i_theme));
        txt_double_left_up_express.setBackgroundResource(tm.getTrainInfo(i_theme));
        txt_double_left_up_norm.setBackgroundResource(tm.getTrainInfo(i_theme));
        txt_double_left_down_express.setBackgroundResource(tm.getTrainInfo(i_theme));
        txt_double_left_down_norm.setBackgroundResource(tm.getTrainInfo(i_theme));
        txt_double_right_up_express.setBackgroundResource(tm.getTrainInfo(i_theme));
        txt_double_right_up_norm.setBackgroundResource(tm.getTrainInfo(i_theme));
        txt_double_right_down_express.setBackgroundResource(tm.getTrainInfo(i_theme));
        txt_double_right_down_norm.setBackgroundResource(tm.getTrainInfo(i_theme));
        txt_old_single_right_up.setBackgroundResource(tm.getTrainInfo(i_theme));
        txt_old_single_right_down.setBackgroundResource(tm.getTrainInfo(i_theme));
        txt_old_double_right_up_norm.setBackgroundResource(tm.getTrainInfo(i_theme));
        txt_old_double_right_up_express.setBackgroundResource(tm.getTrainInfo(i_theme));
        txt_old_double_right_down_norm.setBackgroundResource(tm.getTrainInfo(i_theme));
        txt_old_double_right_down_express.setBackgroundResource(tm.getTrainInfo(i_theme));

        img_single_left_up.setImageResource(tm.getTrainIcon(i_theme, 0, false));
        img_single_left_down.setImageResource(tm.getTrainIcon(i_theme, 1, false));
        img_single_right_up.setImageResource(tm.getTrainIcon(i_theme, 0, false));
        img_single_right_down.setImageResource(tm.getTrainIcon(i_theme, 1, false));
        img_double_left_up_norm.setImageResource(tm.getTrainIcon(i_theme, 0, false));
        img_double_left_up_express.setImageResource(tm.getTrainIcon(i_theme, 0, true));
        img_double_left_down_norm.setImageResource(tm.getTrainIcon(i_theme, 1, false));
        img_double_left_down_express.setImageResource(tm.getTrainIcon(i_theme, 1, true));
        img_double_right_up_norm.setImageResource(tm.getTrainIcon(i_theme, 0, false));
        img_double_right_up_express.setImageResource(tm.getTrainIcon(i_theme, 0, true));
        img_double_right_down_norm.setImageResource(tm.getTrainIcon(i_theme, 1, false));
        img_double_right_down_express.setImageResource(tm.getTrainIcon(i_theme, 1, true));

        switch(i_train_icon) {
            // 열차 아이콘 위치
            case 0:
                // 왼쪽
                layout_2.setVisibility(View.GONE);
                btn_left.setBackgroundResource(R.drawable.item_blue);
                btn_left.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_seltext));
                btn_right.setBackgroundResource(R.drawable.item_white);
                btn_right.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                break;

            case 1:
                // 오른쪽
                layout_2.setVisibility(View.VISIBLE);
                btn_left.setBackgroundResource(R.drawable.item_white);
                btn_left.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                btn_right.setBackgroundResource(R.drawable.item_blue);
                btn_right.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_seltext));
                break;

            default:
                break;
        }

        switch(i_theme) {
            // 테마
            case 0:
                // 신 버전
                switch(i_train_icon) {
                    case 0:
                        layout_1.setVisibility(View.VISIBLE);
                        layout_2.setVisibility(View.GONE);
                        break;

                    case 1:
                        layout_1.setVisibility(View.GONE);
                        layout_2.setVisibility(View.VISIBLE);
                        break;

                    default:
                        break;
                }
                layout_3.setVisibility(View.GONE);
                btn_left.setEnabled(true);
                btn_right.setEnabled(true);
                switch(i_train_icon) {
                    case 0:
                        btn_left.setBackgroundResource(R.drawable.item_blue);
                        btn_right.setBackgroundResource(R.drawable.item_white);
                        break;

                    case 1:
                        btn_left.setBackgroundResource(R.drawable.item_white);
                        btn_right.setBackgroundResource(R.drawable.item_blue);
                        break;

                    default:
                        break;
                }
                btn_theme_new.setBackgroundResource(R.drawable.item_blue);
                btn_theme_new.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_seltext));
                btn_theme_old.setBackgroundResource(R.drawable.item_white);
                btn_theme_old.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                btn_theme_black.setBackgroundResource(R.drawable.item_white);
                btn_theme_black.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                btn_theme_4.setBackgroundResource(R.drawable.item_white);
                btn_theme_4.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                break;

            case 1:
                // 구 버전
                layout_1.setVisibility(View.GONE);
                layout_2.setVisibility(View.GONE);
                layout_3.setVisibility(View.VISIBLE);
                btn_left.setEnabled(false);
                btn_right.setEnabled(false);
                switch(i_train_icon) {
                    case 0:
                        btn_left.setBackgroundResource(R.drawable.item_gray);
                        btn_right.setBackgroundResource(R.drawable.item_white);
                        break;

                    case 1:
                        btn_left.setBackgroundResource(R.drawable.item_white);
                        btn_right.setBackgroundResource(R.drawable.item_gray);
                        break;

                    default:
                        break;
                }
                btn_theme_new.setBackgroundResource(R.drawable.item_white);
                btn_theme_new.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                btn_theme_old.setBackgroundResource(R.drawable.item_blue);
                btn_theme_old.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_seltext));
                btn_theme_black.setBackgroundResource(R.drawable.item_white);
                btn_theme_black.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                btn_theme_4.setBackgroundResource(R.drawable.item_white);
                btn_theme_4.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                break;

            case 2:
                // 블랙
                switch(i_train_icon) {
                    case 0:
                        layout_1.setVisibility(View.VISIBLE);
                        layout_2.setVisibility(View.GONE);
                        break;

                    case 1:
                        layout_1.setVisibility(View.GONE);
                        layout_2.setVisibility(View.VISIBLE);
                        break;

                    default:
                        break;
                }
                layout_3.setVisibility(View.GONE);
                btn_left.setEnabled(true);
                btn_right.setEnabled(true);
                switch(i_train_icon) {
                    case 0:
                        btn_left.setBackgroundResource(R.drawable.item_blue);
                        btn_right.setBackgroundResource(R.drawable.item_white);
                        break;

                    case 1:
                        btn_left.setBackgroundResource(R.drawable.item_white);
                        btn_right.setBackgroundResource(R.drawable.item_blue);
                        break;

                    default:
                        break;
                }
                btn_theme_new.setBackgroundResource(R.drawable.item_white);
                btn_theme_new.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                btn_theme_old.setBackgroundResource(R.drawable.item_white);
                btn_theme_old.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                btn_theme_black.setBackgroundResource(R.drawable.item_blue);
                btn_theme_black.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_seltext));
                btn_theme_4.setBackgroundResource(R.drawable.item_white);
                btn_theme_4.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                break;

            case 3:
                switch(i_train_icon) {
                    case 0:
                        layout_1.setVisibility(View.VISIBLE);
                        layout_2.setVisibility(View.GONE);
                        break;

                    case 1:
                        layout_1.setVisibility(View.GONE);
                        layout_2.setVisibility(View.VISIBLE);
                        break;

                    default:
                        break;
                }
                layout_3.setVisibility(View.GONE);
                btn_left.setEnabled(true);
                btn_right.setEnabled(true);
                switch(i_train_icon) {
                    case 0:
                        btn_left.setBackgroundResource(R.drawable.item_blue);
                        btn_right.setBackgroundResource(R.drawable.item_white);
                        break;

                    case 1:
                        btn_left.setBackgroundResource(R.drawable.item_white);
                        btn_right.setBackgroundResource(R.drawable.item_blue);
                        break;

                    default:
                        break;
                }
                btn_theme_new.setBackgroundResource(R.drawable.item_white);
                btn_theme_new.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                btn_theme_old.setBackgroundResource(R.drawable.item_white);
                btn_theme_old.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                btn_theme_black.setBackgroundResource(R.drawable.item_white);
                btn_theme_black.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                btn_theme_4.setBackgroundResource(R.drawable.item_blue);
                btn_theme_4.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_seltext));
                break;

            default:
                break;
        }

        switch(i_train_info) {
            // 열차 정보 표시
            case 0:
                // 표시 안 함
                txt_single_left_up.setVisibility(View.INVISIBLE);
                txt_single_left_down.setVisibility(View.INVISIBLE);
                txt_single_right_up.setVisibility(View.INVISIBLE);
                txt_single_right_down.setVisibility(View.INVISIBLE);
                txt_double_left_up_express.setVisibility(View.INVISIBLE);
                txt_double_left_up_norm.setVisibility(View.INVISIBLE);
                txt_double_left_down_express.setVisibility(View.INVISIBLE);
                txt_double_left_down_norm.setVisibility(View.INVISIBLE);
                txt_double_right_up_express.setVisibility(View.INVISIBLE);
                txt_double_right_up_norm.setVisibility(View.INVISIBLE);
                txt_double_right_down_express.setVisibility(View.INVISIBLE);
                txt_double_right_down_norm.setVisibility(View.INVISIBLE);
                txt_old_single_right_up.setVisibility(View.INVISIBLE);
                txt_old_single_right_down.setVisibility(View.INVISIBLE);
                txt_old_double_right_up_norm.setVisibility(View.INVISIBLE);
                txt_old_double_right_up_express.setVisibility(View.INVISIBLE);
                txt_old_double_right_down_norm.setVisibility(View.INVISIBLE);
                txt_old_double_right_down_express.setVisibility(View.INVISIBLE);
                btn_info_yes.setBackgroundResource(R.drawable.item_white);
                btn_info_yes.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                btn_info_no.setBackgroundResource(R.drawable.item_blue);
                btn_info_no.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_seltext));
                break;

            case 1:
                // 표시
                txt_single_left_up.setVisibility(View.VISIBLE);
                txt_single_left_down.setVisibility(View.VISIBLE);
                txt_single_right_up.setVisibility(View.VISIBLE);
                txt_single_right_down.setVisibility(View.VISIBLE);
                txt_double_left_up_express.setVisibility(View.VISIBLE);
                txt_double_left_up_norm.setVisibility(View.VISIBLE);
                txt_double_left_down_express.setVisibility(View.VISIBLE);
                txt_double_left_down_norm.setVisibility(View.VISIBLE);
                txt_double_right_up_express.setVisibility(View.VISIBLE);
                txt_double_right_up_norm.setVisibility(View.VISIBLE);
                txt_double_right_down_express.setVisibility(View.VISIBLE);
                txt_double_right_down_norm.setVisibility(View.VISIBLE);
                txt_old_single_right_up.setVisibility(View.VISIBLE);
                txt_old_single_right_down.setVisibility(View.VISIBLE);
                txt_old_double_right_up_norm.setVisibility(View.VISIBLE);
                txt_old_double_right_up_express.setVisibility(View.VISIBLE);
                txt_old_double_right_down_norm.setVisibility(View.VISIBLE);
                txt_old_double_right_down_express.setVisibility(View.VISIBLE);

                btn_info_yes.setBackgroundResource(R.drawable.item_blue);
                btn_info_yes.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_seltext));
                btn_info_no.setBackgroundResource(R.drawable.item_white);
                btn_info_no.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                break;

            default:
                break;
        }

        switch(i_train_status) {
            // 열차 정보 표시
            case 0:
                // 표시 안 함
                btn_status_yes.setBackgroundResource(R.drawable.item_white);
                btn_status_yes.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                btn_status_no.setBackgroundResource(R.drawable.item_blue);
                btn_status_no.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_seltext));
                break;

            case 1:
                // 표시
                btn_status_yes.setBackgroundResource(R.drawable.item_blue);
                btn_status_yes.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_seltext));
                btn_status_no.setBackgroundResource(R.drawable.item_white);
                btn_status_no.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                break;

            default:
                break;
        }

        String str_res = tm.changeWord(str_des);
        String str_temp;
        switch(i_train_type) {
            // 차종 표시
            case 0:
                // 표시 안 함
                if(i_train_status == 0) {
                    str_temp = str_res + "\n" + str_no;
                } else {
                    str_temp = str_res + "\n" + str_no + "\n" + str_status;
                }
                txt_single_left_up.setText(str_temp);
                txt_single_left_down.setText(str_temp);
                txt_single_right_up.setText(str_temp);
                txt_single_right_down.setText(str_temp);
                txt_double_left_up_express.setText(str_temp);
                txt_double_left_up_norm.setText(str_temp);
                txt_double_left_down_express.setText(str_temp);
                txt_double_left_down_norm.setText(str_temp);
                txt_double_right_up_express.setText(str_temp);
                txt_double_right_up_norm.setText(str_temp);
                txt_double_right_down_express.setText(str_temp);
                txt_double_right_down_norm.setText(str_temp);
                txt_old_single_right_up.setText(str_temp);
                txt_old_single_right_down.setText(str_temp);
                txt_old_double_right_up_norm.setText(str_temp);
                txt_old_double_right_up_express.setText(str_temp);
                txt_old_double_right_down_norm.setText(str_temp);
                txt_old_double_right_down_express.setText(str_temp);
                btn_type_yes.setBackgroundResource(R.drawable.item_white);
                btn_type_yes.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                btn_type_no.setBackgroundResource(R.drawable.item_blue);
                btn_type_no.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_seltext));
                break;

            case 1:
                // 표시
                if(i_train_status == 0) {
                    str_temp = str_res + "\n" + str_yes;
                } else {
                    str_temp = str_res + "\n" + str_yes + "\n" + str_status;
                }
                txt_single_left_up.setText(str_temp);
                txt_single_left_down.setText(str_temp);
                txt_single_right_up.setText(str_temp);
                txt_single_right_down.setText(str_temp);
                txt_double_left_up_express.setText(str_temp);
                txt_double_left_up_norm.setText(str_temp);
                txt_double_left_down_express.setText(str_temp);
                txt_double_left_down_norm.setText(str_temp);
                txt_double_right_up_express.setText(str_temp);
                txt_double_right_up_norm.setText(str_temp);
                txt_double_right_down_express.setText(str_temp);
                txt_double_right_down_norm.setText(str_temp);
                txt_old_single_right_up.setText(str_temp);
                txt_old_single_right_down.setText(str_temp);
                txt_old_double_right_up_norm.setText(str_temp);
                txt_old_double_right_up_express.setText(str_temp);
                txt_old_double_right_down_norm.setText(str_temp);
                txt_old_double_right_down_express.setText(str_temp);
                btn_type_yes.setBackgroundResource(R.drawable.item_blue);
                btn_type_yes.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_seltext));
                btn_type_no.setBackgroundResource(R.drawable.item_white);
                btn_type_no.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                break;

            default:
                break;
        }

        switch(i_font_cut) {
            case 0:
                btn_font_small.setBackgroundResource(R.drawable.item_blue);
                btn_font_small.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_seltext));
                btn_font_medium.setBackgroundResource(R.drawable.item_white);
                btn_font_medium.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                btn_font_large.setBackgroundResource(R.drawable.item_white);
                btn_font_large.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                break;

            case 1:
                btn_font_small.setBackgroundResource(R.drawable.item_white);
                btn_font_small.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                btn_font_medium.setBackgroundResource(R.drawable.item_blue);
                btn_font_medium.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_seltext));
                btn_font_large.setBackgroundResource(R.drawable.item_white);
                btn_font_large.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                break;

            case 2:
                btn_font_small.setBackgroundResource(R.drawable.item_white);
                btn_font_small.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                btn_font_medium.setBackgroundResource(R.drawable.item_white);
                btn_font_medium.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                btn_font_large.setBackgroundResource(R.drawable.item_blue);
                btn_font_large.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_seltext));
                break;

            default:
                break;
        }
    }

    private void setListSize() {
        FrameLayout.LayoutParams param;
        param = (FrameLayout.LayoutParams)layout_1.getLayoutParams();

        switch(i_list_size) {
            case 55:
                btn_list_small.setBackgroundResource(R.drawable.item_blue);
                btn_list_small.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_seltext));
                btn_list_large.setBackgroundResource(R.drawable.item_white);
                btn_list_large.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                break;

            case 66:
                btn_list_small.setBackgroundResource(R.drawable.item_white);
                btn_list_small.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_subtext));
                btn_list_large.setBackgroundResource(R.drawable.item_blue);
                btn_list_large.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.item_seltext));
                break;

            default:
                break;
        }

        param.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i_list_size * 2, getResources().getDisplayMetrics());
        layout_1.setLayoutParams(param);
        layout_2.setLayoutParams(param);
        layout_3.setLayoutParams(param);
    }

    private void setFontSize() {
        txt_single_left_up.setTextSize(TypedValue.COMPLEX_UNIT_SP, i_train_font_size);
        txt_single_left_down.setTextSize(TypedValue.COMPLEX_UNIT_SP, i_train_font_size);
        txt_single_right_up.setTextSize(TypedValue.COMPLEX_UNIT_SP, i_train_font_size);
        txt_single_right_down.setTextSize(TypedValue.COMPLEX_UNIT_SP, i_train_font_size);
        txt_double_left_up_express.setTextSize(TypedValue.COMPLEX_UNIT_SP, i_train_font_size);
        txt_double_left_up_norm.setTextSize(TypedValue.COMPLEX_UNIT_SP, i_train_font_size);
        txt_double_left_down_express.setTextSize(TypedValue.COMPLEX_UNIT_SP, i_train_font_size);
        txt_double_left_down_norm.setTextSize(TypedValue.COMPLEX_UNIT_SP, i_train_font_size);
        txt_double_right_up_express.setTextSize(TypedValue.COMPLEX_UNIT_SP, i_train_font_size);
        txt_double_right_up_norm.setTextSize(TypedValue.COMPLEX_UNIT_SP, i_train_font_size);
        txt_double_right_down_express.setTextSize(TypedValue.COMPLEX_UNIT_SP, i_train_font_size);
        txt_double_right_down_norm.setTextSize(TypedValue.COMPLEX_UNIT_SP, i_train_font_size);
        txt_single_left_station.setTextSize(TypedValue.COMPLEX_UNIT_SP, i_station_font_size);
        txt_single_right_station.setTextSize(TypedValue.COMPLEX_UNIT_SP, i_station_font_size);
        txt_double_left_station.setTextSize(TypedValue.COMPLEX_UNIT_SP, i_station_font_size);
        txt_double_right_station.setTextSize(TypedValue.COMPLEX_UNIT_SP, i_station_font_size);
        txt_old_single_right_up.setTextSize(TypedValue.COMPLEX_UNIT_SP, i_train_font_size);
        txt_old_single_right_down.setTextSize(TypedValue.COMPLEX_UNIT_SP, i_train_font_size);
        txt_old_double_right_up_norm.setTextSize(TypedValue.COMPLEX_UNIT_SP, i_train_font_size);
        txt_old_double_right_up_express.setTextSize(TypedValue.COMPLEX_UNIT_SP, i_train_font_size);
        txt_old_double_right_down_norm.setTextSize(TypedValue.COMPLEX_UNIT_SP, i_train_font_size);
        txt_old_double_right_down_express.setTextSize(TypedValue.COMPLEX_UNIT_SP, i_train_font_size);
        txt_old_single_station.setTextSize(TypedValue.COMPLEX_UNIT_SP, i_station_font_size);
        txt_old_double_station.setTextSize(TypedValue.COMPLEX_UNIT_SP, i_station_font_size);

    }
}
