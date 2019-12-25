package com.ganada.silsiganmetro.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.ganada.silsiganmetro.R;

public class HelperActivity extends Activity {

    private SharedPreferences mPref;
    private SharedPreferences.Editor mPrefEdit;

    ViewFlipper viewFlipper;
    Animation slide_in_left, slide_in_right, slide_out_left, slide_out_right;
    Button btnPrev, btnNext;
    ImageView imgView1, imgView2, imgView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);

        mPref = getSharedPreferences("Pref1", 0);
        mPrefEdit = mPref.edit();

        viewFlipper = findViewById(R.id.viewFlipper);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        slide_in_left = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        slide_in_right = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        slide_out_left = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        slide_out_right = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);

        imgView1 = findViewById(R.id.imgView1);
        imgView2 = findViewById(R.id.imgView2);
        imgView3 = findViewById(R.id.imgView3);

        Glide.with(this).load(R.drawable.helper1).into(imgView1);
        Glide.with(this).load(R.drawable.helper2).into(imgView2);
        Glide.with(this).load(R.drawable.helper3).into(imgView3);

        setButtonEnabled();

    }

    public void prevPage(View v) {
        viewFlipper.setInAnimation(slide_in_left);
        viewFlipper.setOutAnimation(slide_out_right);
        viewFlipper.showPrevious();
        setButtonEnabled();
    }

    public void nextPage(View v) {
        if(viewFlipper.getDisplayedChild() == viewFlipper.getChildCount() - 1) {
            mPrefEdit.putInt("helper", 1);
            mPrefEdit.apply();
            finish();
        } else {
            viewFlipper.setInAnimation(slide_in_right);
            viewFlipper.setOutAnimation(slide_out_left);
            viewFlipper.showNext();
            setButtonEnabled();
        }
    }

    public void setButtonEnabled() {
        btnPrev.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.VISIBLE);
        btnNext.setText("다음");

        if(viewFlipper.getDisplayedChild() == 0) {
            btnPrev.setVisibility(View.INVISIBLE);
        } else if(viewFlipper.getDisplayedChild() == viewFlipper.getChildCount() - 1) {;
            btnNext.setText("닫기");
        }
    }
}
