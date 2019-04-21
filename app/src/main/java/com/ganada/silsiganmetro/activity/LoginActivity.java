package com.ganada.silsiganmetro.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.ganada.silsiganmetro.MetroOAuthHandler;
import com.ganada.silsiganmetro.OAuthDelegator;
import com.ganada.silsiganmetro.R;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

public class LoginActivity extends Activity implements OAuthDelegator {

    OAuthLoginButton mOAuthLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*
         * OAuthLoginHandler를 startOAuthLoginActivity() 메서드 호출 시 파라미터로 전달하거나 OAuthLoginButton
         객체에 등록하면 인증이 종료되는 것을 확인할 수 있습니다.
         */

        MetroOAuthHandler mOAuthLoginHandler = new MetroOAuthHandler(this, this);

        mOAuthLoginButton = findViewById(R.id.buttonOAuthLoginImg);
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
        OAuthLogin.getInstance().startOauthLoginActivity(this, mOAuthLoginHandler);
    }

    @Override
    public void onLoginSuccess() {
        Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTokenGet(String token) {
        Toast.makeText(this, token, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginFail() {
        Toast.makeText(this, "Login Fail", Toast.LENGTH_SHORT).show();
    }
}