package com.ganada.silsiganmetro.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ganada.silsiganmetro.R;
import com.ganada.silsiganmetro.common.Important;
import com.ganada.silsiganmetro.util.ThemeManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class WebActivity extends AppCompatActivity {

    WebView webView;
    WebSettings webSettings;

    ThemeManager tm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        tm = new ThemeManager(getApplicationContext());

        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new MetroWebClient());
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                final JsResult jsResult = result;
                final String msg = message;

                new AlertDialog.Builder(WebActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog)
                        .setTitle("오류 메시지")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                if(!msg.contains("편성")) {
                                    finish();
                                }
                                jsResult.confirm();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();


                return true;
            }
        });

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        Window window = getWindow();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), tm.getTitleBarColor(0, 0)));
        }

        MobileAds.initialize(getApplicationContext(), Important.ADMOB_KEY);
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Intent intent = getIntent();
        String line = "1002";
        String trainNo = intent.getStringExtra("trainNo");

        trainNo = trainNo.replace("S", "");

        Log.e("url", trainNo);

        //webView.loadUrl("https://m.naver.com");
        //webView.loadUrl("https://guide94.cafe24.com/silsiganmetro/search-train.php");
        webView.loadUrl(Important.URL_DOMAIN + "traininfo/number?h=f&line=" + line + "&train=" + trainNo);

    }

    class MetroWebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            Log.e("ssl error", error.toString());
        }
    }
}
