package com.ganada.silsiganmetro.real;

import android.util.Log;

import com.ganada.silsiganmetro.common.Important;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;

/**
 * Created by user on 2017. 9. 22..
 */

public class Realtable {
    public String getTable(String code, int updown, int weekend) {
        final StringBuffer sb = new StringBuffer();
        String str = "";

        if(code == null)
            code = "";

        try {
            str = URLEncoder.encode(code, "UTF-8");
        } catch (Exception e) {

        }

        String URL_M = String.format(
                Locale.KOREA,
                Important.URL_TABLE + "%s/%d/%d/", code, updown, weekend);

        try {
            URL url = new URL(URL_M);
            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();// 접속
            if (conn != null) {
                conn.setConnectTimeout(10000);
                conn.setUseCaches(false);
                if (conn.getResponseCode()
                        == HttpURLConnection.HTTP_OK) {
                    //    데이터 읽기
                    BufferedReader br
                            = new BufferedReader(new InputStreamReader
                            (conn.getInputStream(), "utf-8"));//"utf-8"
                    while (true) {
                        String line = br.readLine();
                        if (line == null) break;
                        sb.append(line + "\n");
                    }
                    br.close(); // 스트림 해제
                }
                conn.disconnect(); // 연결 끊기

            }
            Log.e(">>>>>>>>>>>>>>>>", sb.toString());
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
