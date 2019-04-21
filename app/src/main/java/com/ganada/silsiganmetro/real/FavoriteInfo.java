package com.ganada.silsiganmetro.real;

import android.util.Log;

import com.ganada.silsiganmetro.common.Important;
import com.ganada.silsiganmetro.listitem.ListTrain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by user on 2017. 8. 19..
 */

public class FavoriteInfo {
    public String getLine(String msg) {
        final StringBuffer sb = new StringBuffer();
        String str = "";

        if(msg == null)
            msg = "";

        try {
            str = URLEncoder.encode(msg, "UTF-8");
        } catch (Exception e) {

        }

        String URL_M = Important.URL_STATION + str;

        try {
            URL url = new URL(URL_M);
            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();// 접속
            if (conn != null) {
                conn.setConnectTimeout(3000);
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
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getLine(String line, String station) {
        final StringBuffer sb = new StringBuffer();
        String strLine = line;
        String strCode = station;

        if(line == null)
            line = "";

        try {
            strLine = URLEncoder.encode(line, "UTF-8");
            strCode = URLEncoder.encode(station, "UTF-8");
        } catch (Exception e) {

        }

        String URL_M = String.format(Locale.KOREA, Important.URL_SUBWAY, strLine, strCode);
        Log.e("URL", URL_M);

        try {
            URL url = new URL(URL_M);
            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();// 접속
            if (conn != null) {
                conn.setConnectTimeout(3000);
                conn.setUseCaches(false);
                if (conn.getResponseCode()
                        == HttpURLConnection.HTTP_OK) {
                    //    데이터 읽기
                    BufferedReader br
                            = new BufferedReader(new InputStreamReader
                            (conn.getInputStream(), "EUC-KR"));
                    while (true) {
                        String buffer = br.readLine();
                        if (buffer == null) break;
                        sb.append(buffer);
                        sb.append("\n");
                    }
                    br.close(); // 스트림 해제
                }
                conn.disconnect(); // 연결 끊기

            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public ArrayList<ListTrain> jsonParserList(String pRecvServerPage, String line, String station) {

        try {
            ArrayList<ListTrain> arTrainList = new ArrayList<>();
            Log.e("line", pRecvServerPage);
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("realtimeArrivalList");

            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                if (json.getString("subwayId").equals(line)) {
                    String str_line = json.getString("subwayId");
                    String str_no = json.getString("btrainNo");
                    String str_dir = json.getString("updnLine");
                    String str_dst = json.getString("bstatnNm");
                    String str_station = json.getString("arvlMsg3");
                    String str_status = json.getString("arvlMsg2").split("\\(")[0].trim().replace("[", "").replace("]", "");
                    String str = json.getString("ordkey");
                    int i_time = json.getInt("barvlDt");
                    int arvlCd = json.getInt("arvlCd");
                    char[] chr = new char[5];
                    str.getChars(0, 5, chr, 0);
                    String str_sort = String.valueOf(chr);
                    //String str_express = json.getString("");

                    arTrainList.add(new ListTrain(station, str_line, str_no, str_dir, str_dst, str_station, str_status, str_sort, "", i_time, arvlCd));
                }
            }

            return arTrainList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<ListTrain> jsonParseList2(String pRecvServerPage, String line, String station) {
        try {
            ArrayList<ListTrain> arTrainList = new ArrayList<>();
            Log.e("line", pRecvServerPage);
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("resultList");

            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                if (json.getString("subwayId").equals(line)) {
                    String str_line = json.getString("subwayId");
                    String str_no = json.getString("bTrainNo");
                    String str_dir = json.getString("updnLine");
                    String str_dst = json.getString("bStatnNm");
                    String str_station = json.getString("arvlMsg3");
                    String str_status = json.getString("arvlMsg2");
                    String str = json.getString("bArvlDt");
                    int i_time = json.getInt("bArvlDt");
                    int arvlCd = json.getInt("arvlCd");
                    char[] chr = new char[5];
                    //str.getChars(0, 5, chr, 0);
                    String str_sort = "";
                    //String str_express = json.getString("");

                    arTrainList.add(new ListTrain(station, str_line, str_no, str_dir, str_dst, str_station, str_status, str_sort, "", i_time, arvlCd));
                }
            }

            return arTrainList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
