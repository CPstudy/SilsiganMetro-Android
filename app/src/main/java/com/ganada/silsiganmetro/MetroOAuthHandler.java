package com.ganada.silsiganmetro;

import android.content.Context;
import android.util.Log;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class MetroOAuthHandler extends OAuthLoginHandler {

    private OAuthDelegator delegator;
    private Context context;

    public MetroOAuthHandler(OAuthDelegator delegator, Context context){
        this.delegator = delegator;
        this.context = context;
    }

    @Override
    public void run(boolean success) {
        if (success) {
            delegator.onLoginSuccess();
            delegator.onTokenGet(OAuthLogin.getInstance().getAccessToken(context));
        } else {
            delegator.onLoginFail();
        }
    }
}
