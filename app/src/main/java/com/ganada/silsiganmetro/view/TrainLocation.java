package com.ganada.silsiganmetro.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ganada.silsiganmetro.R;
import com.ganada.silsiganmetro.ResourceUtil;
import com.ganada.silsiganmetro.util.LineManager;

import java.util.ArrayList;
import java.util.Locale;

public class TrainLocation extends RelativeLayout {

    private static final String APPROACHING = "0";
    private static final String ARRIVED = "1";

    private static final int UP = 0;
    private static final int DOWN = 0;

    private static final int NORM = 0;
    private static final int EXPRESS = 1;

    private OnClickListener listener;
    private Context context;

    private ArrayList<MiniTrain> list = new ArrayList<>();

    public TrainLocation(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.layout_trainlocation, null);

        addView(v);
    }

    public TrainLocation addTrain(
            int listHeight,
            int trainHeight,
            String dst,
            String num,
            String stat,
            int updown,
            int posY,
            int express,
            int lineNumber
    ) {
        int stationPosY = posY;
        int listHeightPixel = ResourceUtil.toPixel(getContext(), listHeight);
        int axisY;
        int axisX;
        int calcStat = trainHeight / 2;

        Log.v("TestTrain", "pos = " + posY);

        MiniTrain mt = new MiniTrain(getContext());
        mt.setHead(updown);
        mt.setDestination(dst);

        if(stat.equals(APPROACHING)) {
            mt.setTrainNumber("진입");
        } else if(stat.equals(ARRIVED)) {
            mt.setTrainNumber("도착");
        } else {
            mt.setTrainNumber("출발");
        }
        mt.setTrainNumber(num);

        addView(mt);
        mt.setTranslationX(getAxisX(lineNumber, updown, express));

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mt.getLayoutParams();

        if(updown == UP) {
            if(stat.equals(APPROACHING)) {
                axisY = listHeight - trainHeight;
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            } else if(stat.equals(ARRIVED)) {
                axisY = (listHeight / 2) - calcStat;
                params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            } else {
                axisY = 0;
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            }
            //axisY = stationPosY;
        } else {
            if(stat.equals(APPROACHING)) {
                axisY = 0;
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            } else if(stat.equals(ARRIVED)) {
                axisY = (listHeight / 2) - calcStat;
                params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            } else {
                axisY = listHeight - trainHeight;
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            }

            //axisY = stationPosY;
        }

        mt.setLayoutParams(params);

        Log.e("params", String.format(Locale.KOREA, "listHeight = %d, listHeightPixel = %d, trainHeight = %d, axisX = %d, axisY = %d", listHeight, listHeightPixel, trainHeight, getAxisX(lineNumber, updown, express), axisY));

        //mt.setTranslationY(axisY);
        mt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("MiniTrain", "Clicked");
            }
        });

//        LayoutParams params = (RelativeLayout.LayoutParams) mt.getLayoutParams();
//
//        params.setMargins(axisX, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, axisY, getResources().getDisplayMetrics()), 0, 0);
//        mt.setLayoutParams(params);

        return this;
    }

    public void setClick() {
        Log.e("MiniTrain", "list size = " + list.size());
        for(MiniTrain miniTrain : list) {
            miniTrain.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("MiniTrain", "Clicked");
                }
            });
        }
        invalidate();
    }

    public void clearList() {
        list.clear();
    }

    public void clearView() {
        clearList();
        removeAllViews();
    }

    public int getAxisX(int lineNumber, int updown, int express) {

        int axisX = 0;

        switch(lineNumber) {
            case LineManager.LINE_1:
            case LineManager.LINE_9:
            case LineManager.LINE_GONGHANG:
                if(updown == UP) {
                    if(express == NORM) {
                        axisX = 146;
                    } else {
                        axisX = 211;
                    }
                } else {
                    if(express == NORM) {
                        axisX = 55;
                    } else {
                        axisX = -10;
                    }
                }
                break;

            default:
                if(updown == UP) {
                    axisX = 79;
                } else {
                    axisX = -10;
                }
                break;

        }

        return ResourceUtil.toPixel(getContext(), axisX);
    }
}
