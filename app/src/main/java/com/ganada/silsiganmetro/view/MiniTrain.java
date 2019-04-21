package com.ganada.silsiganmetro.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ganada.silsiganmetro.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MiniTrain extends ConstraintLayout {

    final static int UP = 0;
    final static int DOWN = 1;

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
        init(context, null);
    }

    public MiniTrain(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.layout_minitrain, null);
        if(attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MiniTrain);
            updown = a.getInt(R.styleable.MiniTrain_updown, 0);
        }

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
