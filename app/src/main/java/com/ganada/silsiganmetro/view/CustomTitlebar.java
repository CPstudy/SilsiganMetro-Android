package com.ganada.silsiganmetro.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ganada.silsiganmetro.R;
import com.ganada.silsiganmetro.util.LineManager;
import com.ganada.silsiganmetro.util.ThemeManager;
import com.ganada.silsiganmetro.util.Units;

public class CustomTitlebar extends RelativeLayout {

    private ThemeManager tm;

    private Context context;
    private ImageButton btnBack;
    private TextView txtInfo;
    private RelativeLayout layTitle;
    private RelativeLayout layBack;

    private String strTitle;

    private boolean backButtonVisible = false;

    public CustomTitlebar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        tm = new ThemeManager(context);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.layout_customtitlebar, null);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTitlebar);
        strTitle = a.getString(R.styleable.CustomTitlebar_text);
        backButtonVisible = a.getBoolean(R.styleable.CustomTitlebar_backButton, true);

        btnBack = v.findViewById(R.id.btnBack);
        txtInfo = v.findViewById(R.id.txtInfo);
        layTitle = v.findViewById(R.id.layTitle);
        layBack = v.findViewById(R.id.layBack);

        setBackgroundColor(ContextCompat.getColor(context, tm.getTitleBarColor(tm.getTheme(), 0)));
        changeText();
        setBackButtonVisible(backButtonVisible);

        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Button", "Title Bar Button Clicked");
                ((Activity)context).finish();
            }
        });

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setPadding(0, Units.dp(25), 0, 0);
        } else {
            setPadding(0, 0, 0, 0);
        }


        addView(v);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        int id = child.getId();

        if(id != R.id.layTitle && id != R.id.layBack) {
            layTitle.addView(child, index, params);
        } else {
            super.addView(child, index, params);
        }
    }

    private void changeText() {
        txtInfo.setText(strTitle);
    }

    public void setText(String text) {
        strTitle = text;
        changeText();
    }

    public void setBackButtonVisible(boolean b) {
        backButtonVisible = b;
        if(b) {
            btnBack.setVisibility(View.VISIBLE);
        } else {
            btnBack.setVisibility(View.GONE);
        }
    }

    public void setBackgroundColorById(int color) {
        setBackgroundColor(ContextCompat.getColor(context, color));
    }

    public void setStatusBar(Activity activity, int color) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }
}
