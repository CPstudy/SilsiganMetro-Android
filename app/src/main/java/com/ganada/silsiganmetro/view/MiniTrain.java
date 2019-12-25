package com.ganada.silsiganmetro.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ganada.silsiganmetro.R;
import com.ganada.silsiganmetro.activity.ThemeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MiniTrain extends FrameLayout {

    final static int UP = 0;
    final static int DOWN = 1;

    private Context context;

    private ConstraintLayout layoutBack;
    private TextView txtInfo;
    private ImageView imgUp;
    private ImageView imgDown;

    private int updown = 0;
    private String info = "";
    private String dst;
    private String trainNo;
    private String trainStatus;

    public MiniTrain(Context context) {
        super(context);
        this.context = context;
        init(context, null);
    }

    public MiniTrain(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.layout_minitrain, null);
        if(attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MiniTrain);
            updown = a.getInt(R.styleable.MiniTrain_updown, 0);
        }

        layoutBack = v.findViewById(R.id.layoutBack);
        txtInfo = v.findViewById(R.id.txtInfo);
        imgUp = v.findViewById(R.id.imgUp);
        imgDown = v.findViewById(R.id.imgDown);

        setHead(updown);

        addView(v);
    }

    public void setHead(int updown) {
        this.updown = updown;

        if(updown == UP) {
            imgUp.setVisibility(VISIBLE);
            imgDown.setVisibility(INVISIBLE);
        } else {
            imgUp.setVisibility(INVISIBLE);
            imgDown.setVisibility(VISIBLE);
        }
    }

    private void makeString() {
        info = dst + "\n" + trainNo;
        txtInfo.setText(info);
    }

    public void setDestination(String dst) {
        this.dst = dst;
        makeString();
    }

    public void setTrainNumber(String no) {
        this.trainNo = no;
        makeString();
    }

    public void setTrainStatus(String status) {
        this.trainStatus = status;
        makeString();
    }

}
