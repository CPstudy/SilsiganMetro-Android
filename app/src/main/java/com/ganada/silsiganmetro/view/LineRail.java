package com.ganada.silsiganmetro.view;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ganada.silsiganmetro.R;
import com.ganada.silsiganmetro.laboratory.LineType;
import com.ganada.silsiganmetro.util.ThemeManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LineRail extends RelativeLayout {

    ThemeManager tm;
    Context context;

    @BindView(R.id.lineTop)
    View lineTop;
    @BindView(R.id.lineBottom)
    View lineBottom;
    @BindView(R.id.iconStation)
    ImageView iconStation;

    public LineRail(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public LineRail(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.layout_linerail, null);
        ButterKnife.bind(this, v);

        tm = new ThemeManager(context);

        addView(v);
    }

    public void setStyle(int line, int info) {
        setLineColor(line);
        switch(info) {
            case 0:
                setNorm();
                setIconNorm(line);
                break;

            case 1:
                setNorm();
                setIconTrans(line);
                break;

            case 2:
                setTop();
                setIconNorm(line);
                break;

            case 3:
                setBottom();
                setIconNorm(line);
                break;
        }
    }

    public void setNorm() {
        lineTop.setVisibility(VISIBLE);
        lineBottom.setVisibility(VISIBLE);
    }

    public void setLineColor(int line) {
        LineType lineType = LineType.getLine(line);
        lineTop.setBackgroundColor(ContextCompat.getColor(context, lineType.getColorId()));
        lineBottom.setBackgroundColor(ContextCompat.getColor(context, lineType.getColorId()));
    }

    public void setTop() {
        lineTop.setVisibility(INVISIBLE);
    }

    public void setBottom() {
        lineBottom.setVisibility(INVISIBLE);
    }

    public void setIconNorm(int line) {
        iconStation.setImageResource(tm.getStationIcon(line, false));
    }

    public void setIconTrans(int line) {
        iconStation.setImageResource(tm.getStationIcon(line, true));
    }
}
