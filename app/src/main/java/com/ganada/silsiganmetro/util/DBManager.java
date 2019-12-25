package com.ganada.silsiganmetro.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;

import com.ganada.silsiganmetro.listitem.DataList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user on 2018-01-09.
 */

public class DBManager {

    DataList datalist;
    CalcDate cd;
    ArrayList<DataList> arrList;
    ArrayList<DataList> arrResult;
    SQLiteDatabase sqlDB;

    private SharedPreferences mPref;
    private SharedPreferences.Editor mPrefEdit;

    public DBManager(Context context) {
        arrList = new ArrayList<>();
        arrResult = new ArrayList<>();

        mPref = context.getSharedPreferences("Pref1", 0);
        mPrefEdit = mPref.edit();

        initArray();
    }

    public void initArray() {
        Log.e("success", "suc");
        datalist = new DataList("1호선", "소요산 - 인천/신창", 1, 1);
        arrList.add(datalist);
        datalist = new DataList("2호선", "시청 - 시청", 2, 1);
        arrList.add(datalist);
        datalist = new DataList("3호선", "대화 - 오금", 3, 1);
        arrList.add(datalist);
        datalist = new DataList("4호선", "당고개 - 오이도", 4, 1);
        arrList.add(datalist);
        datalist = new DataList("5호선", "방화 - 상일동/마천", 5, 1);
        arrList.add(datalist);
        datalist = new DataList("6호선", "응암 - 봉화산", 6, 1);
        arrList.add(datalist);
        datalist = new DataList("7호선", "장암 - 부평구청", 7, 1);
        arrList.add(datalist);
        datalist = new DataList("8호선", "암사 - 모란", 8, 1);
        arrList.add(datalist);
        datalist = new DataList("9호선", "개화 - 종합운동장", 9, 1);
        arrList.add(datalist);
        datalist = new DataList("경의 · 중앙선", "문산 - 서울역/지평", 10, 1);
        arrList.add(datalist);
        datalist = new DataList("분당선", "왕십리 - 수원", 11, 1);
        arrList.add(datalist);
        datalist = new DataList("수인선", "오이도 - 인천", 12, 1);
        arrList.add(datalist);
        datalist = new DataList("신분당선", "강남 - 광교", 13, 1);
        arrList.add(datalist);
        datalist = new DataList("경춘선", "청량리 - 춘천", 14, 1);
        arrList.add(datalist);
        datalist = new DataList("공항철도", "인천국제공항 - 서울역", 15, 1);
        arrList.add(datalist);
    }

    public ArrayList<DataList> resetData() {
        arrResult.clear();
        arrResult.addAll(arrList);
        setDB(arrResult);

        return arrResult;
    }

    public ArrayList<DataList> getList() {
        return arrList;
    }

    public ArrayList<DataList> getDBList() {
        String str = mPref.getString("sortString", "{\"result\":[{\"line\":\"1호선\",\"text\":\"소요산 - 인천\\/신창\",\"pos\":1,\"none\":1},{\"line\":\"2호선\",\"text\":\"시청 - 시청\",\"pos\":2,\"none\":1},{\"line\":\"3호선\",\"text\":\"대화 - 오금\",\"pos\":3,\"none\":1},{\"line\":\"4호선\",\"text\":\"당고개 - 오이도\",\"pos\":4,\"none\":1},{\"line\":\"5호선\",\"text\":\"방화 - 상일동\\/마천\",\"pos\":5,\"none\":1},{\"line\":\"6호선\",\"text\":\"응암 - 봉화산\",\"pos\":6,\"none\":1},{\"line\":\"7호선\",\"text\":\"장암 - 부평구청\",\"pos\":7,\"none\":1},{\"line\":\"8호선\",\"text\":\"암사 - 모란\",\"pos\":8,\"none\":1},{\"line\":\"9호선\",\"text\":\"개화 - 종합운동장\",\"pos\":9,\"none\":1},{\"line\":\"경의 · 중앙선\",\"text\":\"문산 - 서울역\\/지평\",\"pos\":10,\"none\":1},{\"line\":\"분당선\",\"text\":\"왕십리 - 수원\",\"pos\":11,\"none\":1},{\"line\":\"수인선\",\"text\":\"오이도 - 인천\",\"pos\":12,\"none\":1},{\"line\":\"신분당선\",\"text\":\"강남 - 광교\",\"pos\":13,\"none\":1},{\"line\":\"경춘선\",\"text\":\"청량리 - 춘천\",\"pos\":14,\"none\":1},{\"line\":\"공항철도\",\"text\":\"인천국제공항 - 서울역\",\"pos\":15,\"none\":1}]}");
        arrResult.clear();

        try {
            JSONObject json = new JSONObject(str);
            JSONArray jArr = json.getJSONArray("result");

            String[] jsonName = {"line", "text", "pos", "none"};
            String[][] parseredData = new String[jArr.length()][jsonName.length];
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);

                for(int j = 0; j < jsonName.length; j++) {
                    parseredData[i][j] = json.getString(jsonName[j]);
                }
            }

            for(int i=0; i<parseredData.length; i++){
                arrResult.add(new DataList(parseredData[i][0], parseredData[i][1], Integer.parseInt(parseredData[i][2]), Integer.parseInt(parseredData[i][3])));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("string", str);

        return arrResult;
    }

    public int getListDir() {
        return mPref.getInt("iLineStatus", 0);
    }

    public void setStation(String line, String station) {
        getDBList();

        for(int i = 0; i < arrResult.size(); i++) {
            arrResult.get(i).pos = arrResult.get(i).pos + 1;
        }

        arrResult.add(0, new DataList(line, station, 1, 0));
        for(int i = 0; i < arrResult.size(); i++) {
            Log.e("array", arrResult.get(i).text + " " + arrResult.get(i).pos);
        }
        setDB(arrResult);
    }

    public void setDB(ArrayList<DataList> array) {
        JSONObject obj = new JSONObject();
        try{
            JSONArray jArr = new JSONArray();
            for(int i = 0; i < array.size(); i++) {
                JSONObject sObject = new JSONObject();
                sObject.put("line", array.get(i).line);
                sObject.put("text", array.get(i).text);
                sObject.put("pos", array.get(i).pos);
                sObject.put("none", array.get(i).none);
                jArr.put(sObject);
            }
            obj.put("result", jArr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mPrefEdit.putString("sortString", obj.toString());
        mPrefEdit.apply();
        System.out.println(obj.toString());
    }
}

