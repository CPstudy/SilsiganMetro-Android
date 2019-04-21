package com.ganada.silsiganmetro.real;

import android.util.Log;

import com.ganada.silsiganmetro.util.CalcDate;
import com.ganada.silsiganmetro.common.Important;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by user on 16. 12. 21..
 */
public class GetType {
    public String getType(String msg) {
        final StringBuffer sb = new StringBuffer();
        String str = "";

        if(msg == null)
            msg = "";

        try {
            str = URLEncoder.encode(msg, "UTF-8");
        } catch (Exception e) {

        }

        String URL_M = Important.URL_INFO + "json/" + str;

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
            //Log.e(">>>>>>>>>>>>>>>>", sb.toString());
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getLine2(int week) {
        final StringBuffer sb = new StringBuffer();
        String str = "";
        String URL_M;

        switch(week) {
            case 1:
                URL_M = Important.URL_INFO + "json/line2_norm.json";
                break;

            case 2:
                URL_M = Important.URL_INFO + "json/line2_saturday.json";
                break;

            case 3:
                URL_M = Important.URL_INFO + "json/line2_sunday.json";
                break;

            default:
                URL_M = Important.URL_INFO + "json/line2_norm.json";
                break;

        }

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
                        sb.append(line);
                        sb.append("\n");
                    }
                    br.close(); // 스트림 해제
                }
                conn.disconnect(); // 연결 끊기

            }
            Log.e("line2", sb.toString());
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getJsonFile(int line) {
        CalcDate cd = new CalcDate();
        String str;
        switch(line) {
            case 1:
                if(cd.getDayWeek() == 2) {
                    str = "line1_week.json";
                } else if(cd.getDayWeek() == 3) {
                    str = "line1_week.json";
                } else {
                    str = "line1_norm.json";
                }
                break;

            case 3:
                if(cd.getDayWeek() == 2) {
                    str = "line3_week.json";
                } else if(cd.getDayWeek() == 3) {
                    str = "line3_week.json";
                } else {
                    str = "line3_norm.json";
                }
                break;

            case 4:
                if(cd.getDayWeek() == 2) {
                    str = "line4_week.json";
                } else if(cd.getDayWeek() == 3) {
                    str = "line4_week.json";
                } else {
                    str = "line4_norm.json";
                }
                break;

            case 9:
                if(cd.getDayWeek() == 2) {
                    str = "";
                } else if(cd.getDayWeek() == 3) {
                    str = "";
                } else {
                    str = "line9_norm.json";
                }
                break;

            default:
                str = "undefined";
                break;
        }
        Log.e("return_json", str);
        return str;
    }

    public String getJson9File(int line, int week) {
        CalcDate cd = new CalcDate();
        String str;
        if(week == 2) {
            str = "";
        } else if(week == 3) {
            str = "";
        } else {
            str = "line9_norm.json";
        }
        return str;
    }
}
