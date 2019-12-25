package com.ganada.silsiganmetro.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.ganada.silsiganmetro.R;

/**
 * Created by user on 2017. 7. 19..
 */

public class StrokeTextView extends TextView {

    private boolean stroke = false;
    private float strokeWidth = 0.0f;
    private int strokeColor;

    public StrokeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initView(context, attrs);
    }

    public StrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView(context, attrs);
    }

    public StrokeTextView(Context context) {
        super(context);
    }

    private void initView(Context context, AttributeSet attrs) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StrokeTextView);
        stroke = a.getBoolean(R.styleable.StrokeTextView_textStroke, false);
        strokeWidth = a.getFloat(R.styleable.StrokeTextView_textStrokeWidth, 0.0f);
        strokeWidth = Math.round(strokeWidth * dm.density);
        strokeColor = a.getColor(R.styleable.StrokeTextView_textStrokeColor, 0xffffffff);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (stroke) {
            ColorStateList states = getTextColors();
            getPaint().setStyle(Paint.Style.STROKE);
            getPaint().setStrokeWidth(strokeWidth);
            setTextColor(strokeColor);
            super.onDraw(canvas);

            getPaint().setStyle(Paint.Style.FILL);
            setTextColor(states);
        }

        super.onDraw(canvas);
    }

    public void setStroke(boolean b) {
        if(b) {
            stroke = true;
        } else {
            stroke = false;
        }
        invalidate();
    }

    public void setStrokeColor(int color) {
        strokeColor = color;
        invalidate();
    }

    public void setStrokeWidth(float width) {
        strokeWidth = width;
        invalidate();
    }
}
