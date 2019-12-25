package com.ganada.silsiganmetro.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ganada.silsiganmetro.laboratory.LabActivity;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("state", "launch");
        startActivity(intent);
        finish();
    }
}
