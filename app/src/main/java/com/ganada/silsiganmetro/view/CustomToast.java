package com.ganada.silsiganmetro.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ganada.silsiganmetro.R;

/**
 * Created by user on 2017. 8. 3..
 */

public class CustomToast extends Toast {
    Context context;

    public CustomToast(Context context) {
        super(context);
        this.context = context;
    }

    @SuppressLint("InflateParams")
    @SuppressWarnings("unused")
    public void showToast(String body, int duration) {
        LayoutInflater inflater;
        View v;

        if(false) {
            Activity activity = (Activity)context;
            inflater = activity.getLayoutInflater();
            v = inflater.inflate(R.layout.toast_layout, null);
        } else {
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.toast_layout, null);
        }

        TextView text = (TextView) v.findViewById(R.id.txt_toast);
        text.setText(body);

        show(this, v, duration);
    }

    private void show(Toast toast, View v, int duration) {
        toast.setDuration(duration);
        toast.setView(v);
        toast.show();
    }
}
