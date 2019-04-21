package com.ganada.silsiganmetro.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ganada.silsiganmetro.R;

import java.util.ArrayList;

public class TrainRail extends FrameLayout {

    public final static int LEFT = 0;
    public final static int CENTER = 1;
    public final static int RIGHT = 2;
    public final static int STATE_APPROACH = 0;
    public final static int STATE_ARRIVAL = 1;
    public final static int STATE_DEPARTURE = 2;

    Context con;
    RelativeLayout layout;
    LinearLayout layout_text;
    LinearLayout sta_oxx, sta_xox, sta_xxo;
    View view_line;

    TrainIcon trainIcon;
    ArrayList<TrainIcon> list = new ArrayList<>();

    int size = 0;
    int half = 0;
    int width = 0;
    int center = 0;
    int left = 0;
    int right = 0;
    int state = 0;
    boolean flip = false;

    public TrainRail(Context context) {
        super(context);

        this.con = context;
        initView();
    }

    public TrainRail(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.con = context;
        initView();
    }

    public void initView() {
        removeAllViews();

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.layout_train_rail, null);

        layout = v.findViewById(R.id.layout);
        layout_text = v.findViewById(R.id.layout_text);
        sta_oxx = v.findViewById(R.id.sta_oxx);
        sta_xox = v.findViewById(R.id.sta_xox);
        sta_xxo = v.findViewById(R.id.sta_xxo);
        view_line = v.findViewById(R.id.view_line);

        addView(v);
    }

    /**
     *
     * @param array
     * @param flip
     * @param pos
     * @param state
     */
    public void addTrain(String[] array, boolean flip, int pos, int state) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ABOVE, layout_text.getId());
        params.setMargins(0, 0, 0, (int)dp(5));

        this.state = state;
        this.flip = flip;

        trainIcon = new TrainIcon(con);

        measure(state);

        trainIcon.setText(array);
        trainIcon.setLayoutParams(params);
        //trainIcon.setTranslationX(trans(pos));
        trainIcon.setX(trans(pos));
        trainIcon.setFlip(flip);

        list.add(trainIcon);
    }

    public void clearTrain() {
        for(TrainIcon icon : list) {
            if(icon.getParent() != null)
                ((ViewGroup)icon.getParent()).removeView(icon);
        }

        list.clear();
    }

    public void showTrain() {
        for(TrainIcon icon : list) {
            if(icon.getParent() != null)
                ((ViewGroup)icon.getParent()).removeView(icon);
            layout.addView(icon);
        }
    }

    public void stopAnimation() {
        for(TrainIcon icon : list) {
            icon.stopAnimation();
        }
    }

    public void resumeAnimation() {
        for(TrainIcon icon : list) {
            icon.startAnimation();
        }
    }

    private int trans(int pos) {
        switch(pos) {
            case LEFT:
                return left;

            case CENTER:
                return center;

            case RIGHT:
                return right;

                default:
                    return 0;
        }
    }

    private void measure(int state) {

        WindowManager wm = (WindowManager) con.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        width = point.x - (int)dp(8) - (int)dp(8);

        size = (int)dp(50.0f);
        half = size / 2;

        int pos = width / 6;
        int pos2 = 0;

        if(!flip) {
            // 상행
            switch(state) {
                case STATE_APPROACH:
                    pos2 = -half;
                    break;

                case STATE_ARRIVAL:
                    pos2 = 0;
                    break;

                case STATE_DEPARTURE:
                    pos2 = half;
                    break;

                default:
                    pos2 = 0;
                    break;
            }
        } else {
            // 하행
            switch(state) {
                case STATE_APPROACH:
                    pos2 = half;
                    break;

                case STATE_ARRIVAL:
                    pos2 = 0;
                    break;

                case STATE_DEPARTURE:
                    pos2 = -half;
                    break;

                default:
                    pos2 = 0;
                    break;
            }
        }

        left = pos - half + pos2;
        center = (width / 2) - half + pos2;
        right = (pos * 5) - half + pos2;

        Log.e("Axis", "width = " + size);
        Log.e("Axis", "half = " + (size / 2));
    }

    public static int getTrainState(String str) {
        if(str.contains("진입")) {
            return STATE_APPROACH;
        } else if(str.contains("도착")) {
            return STATE_ARRIVAL;
        } else if(str.contains("출발")) {
            return STATE_DEPARTURE;
        } else {
            return STATE_ARRIVAL;
        }
    }

    private float dp(float dp){
        final DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        int m = metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT;

        return dp * m;
    }
}
