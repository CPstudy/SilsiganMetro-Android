package com.ganada.silsiganmetro.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

import com.ganada.silsiganmetro.R;
import com.ganada.silsiganmetro.laboratory.LineType;
import com.ganada.silsiganmetro.laboratory.MetroConstant;
import com.ganada.silsiganmetro.util.LineManager;
import com.ganada.silsiganmetro.util.ThemeManager;
import com.ganada.silsiganmetro.view.CustomTitlebar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrainInfoActivity extends AppCompatActivity {


    @BindView(R.id.titleBar)
    CustomTitlebar titleBar;
    @BindView(R.id.webView)
    WebView webView;

    LineType lineType;
    LineManager lineManager;
    ThemeManager themeManager;

    int lineNumber;
    String trainNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_info);
        ButterKnife.bind(this);

        lineNumber = getIntent().getIntExtra(MetroConstant.KEY_LINE_NUMBER, LineManager.LINE_2);
        lineType = LineType.getLine(lineNumber);

        titleBar.setStatusBar(this, ContextCompat.getColor(getBaseContext(), lineType.getColorId()));
        titleBar.setText(lineType.getLineName());
        titleBar.setBackgroundColorById(lineType.getColorId());

        Intent intent = getIntent();

    }
}
