package com.ganada.silsiganmetro.real;

import android.content.Context;
import android.util.Log;

import com.ganada.silsiganmetro.common.Important;
import com.ganada.silsiganmetro.listitem.TrainList;
import com.ganada.silsiganmetro.util.ThemeManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by user on 16. 10. 8..
 */
public class Realtime {

    public String getLine(String msg) {
        final StringBuffer sb = new StringBuffer();
        String str = "";

        if(msg == null)
            msg = "";

        try {
            str = URLEncoder.encode(msg, "UTF-8");
        } catch (Exception e) {

        }

        String URL_M = Important.URL_LINE + str;

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
            Log.e("async", sb.toString());
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getTestLine(String msg) {
        final StringBuffer sb = new StringBuffer();
        String str = "";

        if(msg == null)
            msg = "";

        try {
            str = URLEncoder.encode(msg, "UTF-8");
        } catch (Exception e) {

        }

        //String URL_M = Important.URL_LINE + str;
        String URL_M = Important.URL_TEST;

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
            Log.e("async", sb.toString());
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public ArrayList<TrainList> getTrainList(Context context, String result) {
        ThemeManager tm = new ThemeManager(context);
        ArrayList<TrainList> array = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(result);
            JSONArray jArr = json.getJSONArray("realtimePositionList");

            String[] jsonName = {"updnLine", "directAt", "statnId", "trainNo", "statnTnm", "trainSttus"};
            String[][] parseredData = new String[jArr.length()][jsonName.length];
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);

                for(int j = 0; j < jsonName.length; j++) {
                    parseredData[i][j] = json.getString(jsonName[j]);
                }
            }

            for(int i = 0; i < parseredData.length; i++){
                Log.i("분석 데이터: " + i + " : ", parseredData[i][0]);
                array.add(new TrainList(
                        Integer.parseInt(parseredData[i][0]),
                        Integer.parseInt(parseredData[i][1]),
                        parseredData[i][2],
                        parseredData[i][3],
                        tm.changeWord(parseredData[i][4]),
                        parseredData[i][5],
                        ""
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return array;
    }
}
