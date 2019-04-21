package com.ganada.silsiganmetro.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ganada.silsiganmetro.R;

public class TrainLabel extends FrameLayout {

    private Context context;
    private TextView txtDst;
    private TextView txtNum;
    private TextView txtStatus;

    public TrainLabel(Context context) {
        super(context);

        this.context = context;
        initView();
    }

    public TrainLabel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.layout_trainlabel, null);

        txtDst = v.findViewById(R.id.txtDst);
        txtNum = v.findViewById(R.id.txtNum);
        txtStatus = v.findViewById(R.id.txtStatus);

        addView(v);
    }

    public void setDestination(String dst) {
        txtDst.setText(dst);
    }

    public void setNumber(String number) {
        txtNum.setText(number);
    }

    public void setStatus(String status) {
        txtStatus.setText(status);
    }

    public void clearView() {
        clearView();
    }
}
