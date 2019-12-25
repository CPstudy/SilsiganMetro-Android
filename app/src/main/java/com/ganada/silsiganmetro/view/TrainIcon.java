package com.ganada.silsiganmetro.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ganada.silsiganmetro.R;

import java.lang.ref.WeakReference;

public class TrainIcon extends FrameLayout {

    int count = 0;
    int textSize = 13;
    String strDst = "";
    String[] array;
    boolean boolFlip = false;

    TextView txtDst;
    ImageView imgTrain;

    AnimHandler animHandler = new AnimHandler(this);

    public TrainIcon(Context context) {
        super(context);
        initView(context, null);
    }

    public TrainIcon(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public TrainIcon(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }



    private void initView(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TrainIcon);
        strDst = a.getString(R.styleable.TrainIcon_text);
        boolFlip = a.getBoolean(R.styleable.TrainIcon_flip, false);

        array = new String[1];
        array[0] = strDst;

        a.recycle();

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.train_side, null);

        txtDst = v.findViewById(R.id.txtDst);
        imgTrain = v.findViewById(R.id.imgTrain);


        txtDst.setText(strDst);
        txtDst.setTextSize(textSize);

        if(boolFlip) {
            imgTrain.setScaleX(-1.0f);
        } else {
            imgTrain.setScaleX(1.0f);
        }
        addView(v);
    }

    public void setFlip(boolean b) {
        boolFlip = b;

        if(boolFlip) {
            imgTrain.setScaleX(-1.0f);
        } else {
            imgTrain.setScaleX(1.0f);
        }
    }

    public void setText(String[] array) {
        this.array = array;
    }

    public void startAnimation() {
        invalidate();
        for(int i = 0; i < array.length; i++) {
            Log.e("trainArray", "" + array[i]);
        }

        animHandler.sendEmptyMessage(0);
    }

    public void stopAnimation() {
        animHandler.removeMessages(0);
    }

    private void handleMessage(Message msg) {

        if(count >= array.length) {
            count = 0;
        }

        Log.e("trainArray", array[count]);
        txtDst.setText(array[count]);
        count++;
    }

    private static class AnimHandler extends Handler {

        private final WeakReference<TrainIcon> mActivity;
        private AnimHandler(TrainIcon trainIcon) {
            mActivity = new WeakReference<TrainIcon>(trainIcon);
        }

        @Override
        public void handleMessage(Message msg) {
            TrainIcon trainIcon = mActivity.get();

            if(trainIcon != null) {
                trainIcon.handleMessage(msg);
                this.sendEmptyMessageDelayed(0, 1000);
            }
        }
    }


}
