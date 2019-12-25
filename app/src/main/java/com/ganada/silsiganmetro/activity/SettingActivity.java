package com.ganada.silsiganmetro.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ganada.silsiganmetro.MetroOAuthHandler;
import com.ganada.silsiganmetro.OAuthDelegator;
import com.ganada.silsiganmetro.R;
import com.ganada.silsiganmetro.laboratory.LabActivity;
import com.ganada.silsiganmetro.laboratory.LineActivity;
import com.ganada.silsiganmetro.laboratory.MetroConstant;
import com.ganada.silsiganmetro.util.LineManager;
import com.ganada.silsiganmetro.util.ThemeManager;
import com.ganada.silsiganmetro.view.SettingItem;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class SettingActivity extends Activity implements OAuthDelegator {

    private SharedPreferences mPref;
    private SharedPreferences.Editor mPrefEdit;

    ThemeManager tm;

    String str_version = "";
    final String strarr[] = {"노선 목록", "1호선", "2호선", "3호선", "4호선", "5호선", "6호선", "7호선", "8호선", "9호선", "경의·중앙선", "분당선", "수인선", "신분당선", "경춘선", "공항철도", "모아보기"};
    final String strarr3[] = {"사용 안 함", "5초", "10초", "15초", "20초", "25초", "30초"};
    final String strarr4[] = {"경부선 먼저", "경인선 먼저"};

    LinearLayout layoutLogin;
    LinearLayout layoutSuccess;
    TextView txtID;
    Button btnMail;
    Button btnNeed;
    SettingItem btnFavorite;
    SettingItem btnLine1;
    SettingItem btnRefresh;
    SettingItem btnTheme;
    SettingItem btnSort;
    SettingItem btnFavSort;
    SettingItem btnLab;
    SettingItem btnVersion;
    SettingItem btnOpen;
    SettingItem settingTest;
    OAuthLoginButton mOAuthLoginButton;

    int iTheme;
    int iFavor;
    int iSec;
    int iLine1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        layoutLogin = findViewById(R.id.layoutLogin);
        layoutSuccess = findViewById(R.id.layoutSuccess);
        txtID = findViewById(R.id.txtID);
        btnMail = findViewById(R.id.btnMail);
        btnNeed = findViewById(R.id.btnNeed);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnLine1 = findViewById(R.id.btnLine1);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnTheme = findViewById(R.id.btnTheme);
        btnSort = findViewById(R.id.btnSort);
        btnFavSort = findViewById(R.id.btnFavSort);
        btnLab = findViewById(R.id.btnLab);
        btnVersion = findViewById(R.id.btnVersion);
        btnOpen = findViewById(R.id.btnOpen);
        settingTest = findViewById(R.id.settingTest);

        tm = new ThemeManager(getBaseContext());
        mPref = getSharedPreferences("Pref1", 0);
        mPrefEdit = mPref.edit();

        str_version = getString(R.string.str_version);

        iTheme = mPref.getInt("iTheme", 0);
        iFavor = mPref.getInt("linefavor", 0);
        iSec = mPref.getInt("timerefresh", 0) * 5;
        btnRefresh.setSubText(strarr3[iSec / 5]);
        btnFavorite.setSubText(strarr[iFavor]);

        Window window = getWindow();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), tm.getTitleBarColor(iTheme, 0)));
        }

        if (mPref.getInt("boolLine1", 1) == 0) {
            iLine1 = 1;
        } else if (mPref.getInt("boolLine1", 1) == 1) {
            iLine1 = 0;
        }
        btnLine1.setSubText(strarr4[iLine1]);

        btnMail.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String str;

                str = "안드로이드 문의:\n\n\n" +
                        "기종: \n" +
                        "통신사: \n" +
                        "OS버전: \n" +
                        "앱 버전: " + str_version;
                Intent i = new Intent("android.intent.action.SENDTO", Uri.parse("mailto:guide94@naver.com"));
                //i.setType("message/rfc822");
                //i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"guide94@naver.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "[실시간 지하철] 문의 메일");
                i.putExtra(Intent.EXTRA_TEXT, str);
                try {
                    startActivity(Intent.createChooser(i, "문의 메일 보내기"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(SettingActivity.this, "메일 어플리케이션이 없습니다.", Toast.LENGTH_LONG).show();
                }
            }

        });

        btnNeed.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://guk2zzada.tistory.com/guestbook"));
                startActivity(intent);
            }

        });

        btnFavorite.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //다이얼로그에 보여질 내용
                final CharSequence[] arr = strarr;
                AlertDialog.Builder alert = new AlertDialog.Builder(SettingActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                alert.setTitle("첫 화면 설정");
                alert.setSingleChoiceItems(arr, iFavor,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                iFavor = item;
                                mPrefEdit.putInt("linefavor", iFavor);
                                mPrefEdit.commit();
                                btnFavorite.setSubText(arr[item].toString());
                                dialog.cancel();
                            }
                        });
                AlertDialog alert1 = alert.create();
                alert1.show();
            }
        });

        btnLine1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //다이얼로그에 보여질 내용
                final CharSequence[] arr = strarr4;
                AlertDialog.Builder alert = new AlertDialog.Builder(SettingActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                alert.setTitle("1호선 역 목록 순서");
                alert.setSingleChoiceItems(arr, iLine1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                if (item == 0) {
                                    iLine1 = 1;
                                } else if (item == 1) {
                                    iLine1 = 0;
                                }
                                mPrefEdit.putInt("boolLine1", iLine1);
                                mPrefEdit.commit();
                                btnLine1.setSubText(arr[item].toString());
                                iLine1 = item;
                                dialog.cancel();
                            }
                        });
                AlertDialog alert1 = alert.create();
                alert1.show();
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //다이얼로그에 보여질 내용
                final CharSequence[] arr = strarr3;
                AlertDialog.Builder alert = new AlertDialog.Builder(SettingActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                alert.setTitle("새로고침 시간");
                alert.setSingleChoiceItems(arr, iSec / 5,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                iSec = item * 5;
                                Log.e("iSec >>>>>>> ", "iSec = " + iSec);
                                mPrefEdit.putInt("timerefresh", item);
                                mPrefEdit.commit();
                                btnRefresh.setSubText(arr[item].toString());
                                dialog.cancel();
                            }
                        });
                AlertDialog alert1 = alert.create();
                alert1.show();
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SortActivity.class);
                startActivity(intent);
            }
        });

        btnFavSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FabsortActivity.class);
                startActivity(intent);
            }
        });

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OpenSourceActivity.class);
                startActivity(intent);
            }
        });

        btnTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ThemeActivity.class);
                startActivity(intent);
            }
        });

        btnLab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LabActivity.class);
                //intent.putExtra(MetroConstant.KEY_LINE_NUMBER, LineManager.LINE_7);
                startActivity(intent);
            }
        });

        settingTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LabActivity.class);
                intent.putExtra(MetroConstant.KEY_LINE_NUMBER, LineManager.LINE_7);
                startActivity(intent);
            }
        });

        btnVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(intent);
            }
        });

        /*
        MetroOAuthHandler mOAuthLoginHandler = new MetroOAuthHandler(this, this);

        mOAuthLoginButton = findViewById(R.id.buttonOAuthLoginImg);
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
        OAuthLogin.getInstance().startOauthLoginActivity(this, mOAuthLoginHandler);
        */

    }

    @Override
    public void onLoginSuccess() {
        Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTokenGet(String token) {
        Toast.makeText(this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
        layoutLogin.setVisibility(View.GONE);
        layoutSuccess.setVisibility(View.VISIBLE);
        txtID.setText(token);
        new LoginAsync().execute(token);
    }

    @Override
    public void onLoginFail() {
        Toast.makeText(this, "Login Fail", Toast.LENGTH_SHORT).show();
    }

    class LoginAsync extends AsyncTask<String, String, String> {

        String resultString = "No Profile";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            if(strings.length == 0) {
                return null;
            }

            String token = strings[0];// 네이버 로그인 접근 토큰;
            String header = token; // Bearer 다음에 공백 추가
            try {
                //String apiURL = "https://openapi.naver.com/v1/nid/me";
                String apiURL = "https://guide94.cafe24.com/silsiganmetro/insert-train-no.php";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("token", header);
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if(responseCode == 200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                System.out.println(response.toString());
                resultString = response.toString();
            } catch (Exception e) {
                System.out.println(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            txtID.setText(resultString);
            super.onPostExecute(s);
        }
    }
}










