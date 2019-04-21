package com.ganada.silsiganmetro.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.ganada.silsiganmetro.R;
import com.ganada.silsiganmetro.util.Units;

public class TrainLocation extends RelativeLayout {

    private Context context;

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

    public TrainLocation addTrain(String dst, String num, String stat, int updown, float posY) {
        float axisY = posY;

        MiniTrain mt = new MiniTrain(context);
        mt.setHead(updown);
        mt.setDestination(dst);
        mt.setTrainNumber(String.valueOf(updown));

        if(updown == 0) {
            mt.setX(Units.dp(78.5f));

            if(stat.equals("0")) {
                axisY = posY + Units.dp(10);
            } else if(stat.equals("2")) {
                axisY = posY + Units.dp(-10);
            }
        } else {
            mt.setX(Units.dp(-9));
            if(stat.equals("0")) {
                axisY = posY + Units.dp(-10);
            } else if(stat.equals("2")) {
                axisY = posY + Units.dp(10);
            }
        }

        mt.setY(axisY);

        addView(mt);

        return this;
    }

    public void clearView() {
        removeAllViews();
    }
}
