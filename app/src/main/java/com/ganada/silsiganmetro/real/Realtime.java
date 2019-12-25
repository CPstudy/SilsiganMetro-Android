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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

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

        //String URL_M = Important.URL_LINE + str;
        //String URL_M = "https://guide94.cafe24.com/test1003.json";

        String URL_M = Important.URL_CAFE_LINE + str;

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
        HashMap<String, TrainMap> map = new HashMap<>();

        try {
            JSONObject json = new JSONObject(result);
            JSONArray jArr = json.getJSONArray("realtimePositionList");

            String[] jsonName = {"updnLine", "directAt", "statnId", "trainNo", "statnTnm", "trainSttus", "recptnDt"};

            int updnLine;
            int directAt;
            String statnId;
            String trainNo;
            String statnTnm;
            String trainSttus;
            String recptnDt;

            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);

                trainNo = json.getString("trainNo");
                recptnDt = json.getString("recptnDt");

                if(!map.containsKey(trainNo) || compareDate(map.get(trainNo).recptDate, recptnDt)) {
                    map.put(trainNo, new TrainMap(trainNo, recptnDt, i));
                }
            }

            for(int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                trainNo = json.getString("trainNo");

                if(map.get(trainNo).index == i) {
                    updnLine = json.getInt("updnLine");
                    directAt = json.getInt("directAt");
                    statnId = json.getString("statnId");
                    statnTnm = tm.changeWord(json.getString("statnTnm"));
                    trainSttus = json.getString("trainSttus");
                    recptnDt = json.getString("recptnDt");

                    array.add(new TrainList(
                            updnLine,
                            directAt,
                            statnId,
                            trainNo,
                            statnTnm,
                            trainSttus,
                            ""
                    ));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return array;
    }

    public HashMap<String, String> getPrimaryList(String line) {
        HashMap<String, String> map = new HashMap<>();

        final StringBuffer sb = new StringBuffer();
        String str = "";

        if(line == null)
            line = "";

        try {
            str = URLEncoder.encode(line, "UTF-8");
        } catch (Exception e) {

        }

        //String URL_M = Important.URL_LINE + str;
        //String URL_M = "https://guide94.cafe24.com/test1003.json";

        String URL_M = "https://guk2zzada.com/silsiganmetro/traininfo/todaylist?line_no=1002";

        try {
            URL url = new URL(URL_M);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();// 접속
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
                        String s = br.readLine();
                        if (s == null) break;
                        sb.append(s);
                        sb.append("\n");
                    }
                    br.close(); // 스트림 해제
                }
                conn.disconnect(); // 연결 끊기

            }
            Log.e("async", sb.toString());

            try {
                JSONObject json = new JSONObject(sb.toString());
                JSONArray jArr = json.getJSONArray("result");

                for(int i = 0; i < jArr.length(); i++) {
                    json = jArr.getJSONObject(i);

                    Log.e("primary list", json.getString("train_no") + " ::: " + json.getString("primary_no"));

                    map.put(json.getString("train_no"), json.getString("primary_no"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    public boolean compareDate(String first, String second) {
        Calendar calFirst = Calendar.getInstance();
        Calendar calSecond = Calendar.getInstance();

        String[] strFirst = first.split(" ");
        String[] strSecond = second.split(" ");

        String[] dateFirst = strFirst[0].split("-");
        String[] timeFirst = strFirst[1].split(":");
        String[] dateSecond = strSecond[0].split("-");
        String[] timeSecond = strSecond[1].split(":");

        int fYear = Integer.parseInt(dateFirst[0]);
        int fMonth = Integer.parseInt(dateFirst[1]) - 1;
        int fDay = Integer.parseInt(dateFirst[2]);
        int fHour = Integer.parseInt(timeFirst[0]) - 1;
        int fMin = Integer.parseInt(timeFirst[1]);
        int fSec = Integer.parseInt(timeFirst[2]);

        int sYear = Integer.parseInt(dateSecond[0]);
        int sMonth = Integer.parseInt(dateSecond[1]) - 1;
        int sDay = Integer.parseInt(dateSecond[2]);
        int sHour = Integer.parseInt(timeSecond[0]) - 1;
        int sMin = Integer.parseInt(timeSecond[1]);
        int sSec = Integer.parseInt(timeSecond[2]);

        calFirst.set(fYear, fMonth, fDay, fHour, fMin, fSec);
        calSecond.set(sYear, sMonth, fDay, fHour, fMin, fSec);

        if(calSecond.getTimeInMillis() - calFirst.getTimeInMillis() > 0) {
            return true;
        } else {
            return false;
        }
    }

    class TrainMap {
        String trainNo;
        String recptDate;
        int index;

        TrainMap(String trainNo, String recptDate, int index) {
            this.trainNo = trainNo;
            this.recptDate = recptDate;
            this.index = index;
        }
    }
}
