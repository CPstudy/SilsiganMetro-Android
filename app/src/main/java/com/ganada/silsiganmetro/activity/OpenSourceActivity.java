package com.ganada.silsiganmetro.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.ganada.silsiganmetro.R;

public class OpenSourceActivity extends Activity {

    WebView web;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_source);

        LinearLayout layout_status = (LinearLayout) findViewById(R.id.layout_status);
        btnBack = (ImageButton) findViewById(R.id.btnBack);

        if(Build.VERSION.SDK_INT < 19) {
            layout_status.setVisibility(View.GONE);
        }
        layout_status.setBackgroundColor(Color.parseColor("#91be2e"));

        web = (WebView) findViewById(R.id.web);
        web.loadUrl("file:///android_asset/opensource.html");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
