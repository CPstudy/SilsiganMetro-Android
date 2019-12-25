package com.ganada.silsiganmetro.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ganada.silsiganmetro.R;

public class RefreshButton extends RelativeLayout {

    private OnClickListener listener;
    private ImageView imgArrow;
    private TextView txtTime;
    private Animation animSpin;

    public RefreshButton(Context context) {
        super(context);
        initView(context, null);
    }

    public RefreshButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context ,attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.layout_refreshbutton, null);
        imgArrow = v.findViewById(R.id.imgTime);
        txtTime = v.findViewById(R.id.txtTime);
        animSpin = AnimationUtils.loadAnimation(context, R.anim.spin_anim);

        addView(v);
    }

    public void setTime(String text) {
        txtTime.setText(text);
    }

    public void startAnimation() {
        imgArrow.startAnimation(animSpin);
        setEnabled(false);
    }

    public void stopAnimation() {
        imgArrow.clearAnimation();
        setEnabled(true);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {
            if(listener != null) listener.onClick(this);
        }
        return super.dispatchTouchEvent(event);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_UP && (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER || event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            if(listener != null) listener.onClick(this);
        }
        return super.dispatchKeyEvent(event);
    }
}
