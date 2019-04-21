package com.ganada.silsiganmetro;

public interface OAuthDelegator {
    void onLoginSuccess();
    void onLoginFail();
    void onTokenGet(String token);
}
