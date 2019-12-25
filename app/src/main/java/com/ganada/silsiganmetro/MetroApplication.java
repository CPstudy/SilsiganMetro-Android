package com.ganada.silsiganmetro;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.nhn.android.naverlogin.OAuthLogin;

/**
 * Created by user on 2018-01-10.
 */

public class MetroApplication extends MultiDexApplication {

    private static Context applicationContext;

    private OAuthLogin mOAuthLoginModule;

    private int position = -1;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(
                getApplicationContext(),
                "2PGV2ihzA9qMRUPhR7kg",
                "UjI144QLvh",
                "실시간 지하철"
                //,OAUTH_CALLBACK_INTENT
                // SDK 4.1.4 버전부터는 OAUTH_CALLBACK_INTENT변수를 사용하지 않습니다.
        );
    }
}
