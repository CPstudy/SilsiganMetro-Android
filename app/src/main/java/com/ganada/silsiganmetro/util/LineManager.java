package com.ganada.silsiganmetro.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ganada.silsiganmetro.activity.BundangActivity;
import com.ganada.silsiganmetro.activity.GonghangActivity;
import com.ganada.silsiganmetro.activity.KChoonActivity;
import com.ganada.silsiganmetro.activity.KJoongActivity;
import com.ganada.silsiganmetro.activity.Line1Activity;
import com.ganada.silsiganmetro.activity.Line2Activity;
import com.ganada.silsiganmetro.activity.Line3Activity;
import com.ganada.silsiganmetro.activity.Line4Activity;
import com.ganada.silsiganmetro.activity.Line5Activity;
import com.ganada.silsiganmetro.activity.Line6Activity;
import com.ganada.silsiganmetro.activity.Line7Activity;
import com.ganada.silsiganmetro.activity.Line8Activity;
import com.ganada.silsiganmetro.activity.Line9Activity;
import com.ganada.silsiganmetro.activity.ShinBundangActivity;
import com.ganada.silsiganmetro.activity.SuinActivity;
import com.ganada.silsiganmetro.listitem.LineStation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 2017. 8. 2..
 */

public class LineManager {
    public static final int PRIMARY = 0;
    public static final int LINE_1 = 1001;
    public static final int LINE_2 = 1002;
    public static final int LINE_3 = 1003;
    public static final int LINE_4 = 1004;
    public static final int LINE_5 = 1005;
    public static final int LINE_6 = 1006;
    public static final int LINE_7 = 1007;
    public static final int LINE_8 = 1008;
    public static final int LINE_9 = 1009;
    public static final int LINE_KEUIJOONG = 1063;
    public static final int LINE_GONGHANG = 1065;
    public static final int LINE_KYEONGCHOON = 1067;
    public static final int LINE_SUIN = 1071;
    public static final int LINE_BUNDANG = 1075;
    public static final int LINE_SHINBUNDANG = 1077;

    private Context context;

    private SharedPreferences mPref;
    private SharedPreferences.Editor mPrefEdit;

    private HashMap<String, String> stations = new HashMap<>();
    private HashMap<Integer, String> lines = new HashMap<>();

    private String str_line;
    private String str_station;
    private String str_pos;

    public LineManager(Context context) {
        this.context = context;
        mPref = context.getSharedPreferences("Pref1", 0);
        mPrefEdit = mPref.edit();

        str_line = mPref.getString("strFabLine", "");
        str_station = mPref.getString("strFabSta", "");
        str_pos = mPref.getString("strFabPos", "");
    }

    private void setStations() {
        stations.put("상용", "상용(나사렛대)");
        stations.put("총신대입구", "총신대입구(이수)");
        stations.put("이수", "총신대입구(이수)");
        stations.put("신정", "신정(은행정");
        stations.put("오목교", "오목교(목동운동장앞)");
        stations.put("군자", "군자(능동)");
        stations.put("아차산", "아차산(어린이대공원후문)");
        stations.put("광나루", "광나루(장신대)");
        stations.put("천호", "천호(풍납토성)");
        stations.put("굽은다리", "굽은다리(강동구민회관앞)");
        stations.put("올림픽공원", "올림픽공원(한국체대)");
        stations.put("응암순환(상선)", "응암순환");
        stations.put("새절", "새절(신사)");
        stations.put("증산", "증산(명지대앞)");
        stations.put("월드컵경기장", "월드컵경기장(성산)");
        stations.put("대흥", "대흥(서강대앞)");
        stations.put("안암", "안암(고대병원앞)");
        stations.put("월곡", "월곡(동덕여대)");
        stations.put("상월곡", "상월곡(한국과학기술연구원)");
        stations.put("화랑대", "화랑대(서울여대입구)");
        stations.put("공릉", "공릉(서울산업대입구)");
        stations.put("숭실대입구", "숭실대입구(살피재)");
        stations.put("상도", "상도(중앙대앞)");
        stations.put("몽촌토성", "몽촌토성(평화의문)");
        stations.put("남한산성입구", "남한산성입구(성남법원, 검찰청)");
        stations.put("신촌", "신촌(경의.중앙선)");
        stations.put("양재시민의숲(매헌)", "양재시민의숲");
        stations.put("광교중앙(아주대)", "광교중앙");
        stations.put("광교(경기대)", "광교");
        stations.put("서울역", "서울");
    }

    public void setLines() {
        lines.put(LINE_1, "1호선");
        lines.put(LINE_2, "2호선");
        lines.put(LINE_3, "3호선");
        lines.put(LINE_4, "4호선");
        lines.put(LINE_5, "5호선");
        lines.put(LINE_6, "6호선");
        lines.put(LINE_7, "7호선");
        lines.put(LINE_8, "8호선");
        lines.put(LINE_9, "9호선");
        lines.put(LINE_KEUIJOONG, "경의 · 중앙선");
        lines.put(LINE_BUNDANG, "분당선");
        lines.put(LINE_SUIN, "수인선");
        lines.put(LINE_SHINBUNDANG, "신분당선");
        lines.put(LINE_KYEONGCHOON, "경춘선");
        lines.put(LINE_GONGHANG, "공항철도");
    }

    public void setFavorite(String station, int line, int position) {
        str_line += line + ";";
        str_station += station + ";";
        str_pos += position + ";";

        mPrefEdit.putString("strFabLine", str_line);
        mPrefEdit.putString("strFabSta", str_station);
        mPrefEdit.putString("strFabPos", str_pos);
        mPrefEdit.commit();

        Log.e("mPrefEdit", str_line);
        Log.e("mPrefEdit", str_station);
        Log.e("mPrefEdit", str_pos);
    }

    public String getLine() {
        str_line = mPref.getString("strFabLine", "");
        return str_line.substring(0, str_line.length() - 1);
    }

    public String getStation() {
        str_station = mPref.getString("strFabSta", "");
        return str_station.substring(0, str_station.length() - 1);
    }

    public String getPosition() {
        str_pos = mPref.getString("strFabPos", "");
        return str_pos.substring(0, str_pos.length() - 1);
    }

    public int getLineNum(String str) {
        switch(str) {
            case "1호선":
                return LINE_1;

            case "2호선":
                return LINE_2;

            case "3호선":
                return LINE_3;

            case "4호선":
                return LINE_4;

            case "5호선":
                return LINE_5;

            case "6호선":
                return LINE_6;

            case "7호선":
                return LINE_7;

            case "8호선":
                return LINE_8;

            case "9호선":
                return LINE_9;

            case "경의 · 중앙선":
                return LINE_KEUIJOONG;

            case "분당선":
                return LINE_BUNDANG;

            case "수인선":
                return LINE_SUIN;

            case "신분당선":
                return LINE_SHINBUNDANG;

            case "경춘선":
                return LINE_KYEONGCHOON;

            case "공항철도":
                return LINE_GONGHANG;

            default:
                return 1002;
        }
    }

    // TODO: 2018-07-14 여기 바꿔
    public String getStation(String station) {
        switch(station) {
            case "쌍용":
                return "쌍용(나사렛대)";

            case "총신대입구":
                return "총신대입구(이수)";

            case "이수":
                return "총신대입구(이수)";

            case "신정":
                return "신정(은행정)";

            case "오목교":
                return "오목교(목동운동장앞)";

            case "군자":
                return "군자(능동)";

            case "아차산":
                return "아차산(어린이대공원후문)";

            case "광나루":
                return "광나루(장신대)";

            case "천호":
                return "천호(풍납토성)";

            case "굽은다리":
                return "굽은다리(강동구민회관앞)";

            case "올림픽공원":
                return "올림픽공원(한국체대)";

            case "응암순환(상선)":
                return "응암순환";

            case "새절":
                return "새절(신사)";

            case "증산":
                return "증산(명지대앞)";

            case "월드컵경기장":
                return "월드컵경기장(성산)";

            case "대흥":
                return "대흥(서강대앞)";

            case "안암":
                return "안암(고대병원앞)";

            case "월곡":
                return "월곡(동덕여대)";

            case "상월곡":
                return "상월곡(한국과학기술연구원)";

            case "화랑대":
                return "화랑대(서울여대입구)";

            case "공릉":
                return "공릉(서울산업대입구)";

            case "숭실대입구":
                return "숭실대입구(살피재)";

            case "상도":
                return "상도(중앙대앞)";

            case "몽촌토성":
                return "몽촌토성(평화의문)";

            case "남한산성입구":
                return "남한산성입구(성남법원, 검찰청)";

            case "신촌":
                return "신촌(경의.중앙선)";

            case "양재시민의숲(매헌)":
                return "양재시민의숲";

            case "광교중앙(아주대)":
                return "광교중앙";

            case "광교(경기대)":
                return "광교";

            case "서울역":
                return "서울";

            default:
                return station;
        }
    }

    public String getDst(String dst, String line) {
        switch(dst) {
            case "응암순환(상선)":
                return "응암순환";

            case "안암(고대병원앞)":
                return "안암";

            case "동인천 (급행)":
                return "동인천 급행";

            case "천안 (급행)":
                return "천안 급행";

            case "구로 (급행)":
                return "구로 급행";

            case "용산 (급행)":
                return "용산 급행";

            case "서울 (급행)":
                return "서울 급행";

            case "왕십리 (급행)":
                return "왕십리 급행";

            case "수원 (급행)":
                return "수원 급행";

            case "당고개 (급행)":
                return "당고개 급행";

            case "안산 (급행)":
                return "안산 급행";

            case "문산 (급행)":
                return "문산 급행";

            case "용문 (급행)":
                return "용문 급행";

            case "종합운동장 (급행)":
                return "종합운동장 급행";

            case "김포공항 (급행)":
                return "김포공항 급행";

            default:
                return dst + "행";
        }
    }

    public Class getClass(int line) {
        switch (line) {
            case LINE_1:
                return Line1Activity.class;

            case LINE_2:
                return Line2Activity.class;

            case LINE_3:
                return Line3Activity.class;

            case LINE_4:
                return Line4Activity.class;

            case LINE_5:
                return Line5Activity.class;

            case LINE_6:
                return Line6Activity.class;

            case LINE_7:
                return Line7Activity.class;

            case LINE_8:
                return Line8Activity.class;

            case LINE_9:
                return Line9Activity.class;

            case LINE_BUNDANG:
                return BundangActivity.class;

            case LINE_SUIN:
                return SuinActivity.class;

            case LINE_SHINBUNDANG:
                return ShinBundangActivity.class;

            case LINE_GONGHANG:
                return GonghangActivity.class;

            case LINE_KYEONGCHOON:
                return KChoonActivity.class;

            case LINE_KEUIJOONG:
                return KJoongActivity.class;

            default:
                return Line2Activity.class;
        }
    }

    public LineStation getStationByCode(ArrayList<LineStation> array, String code) {
        for(int i = 0; i < array.size(); i++) {
            if(array.get(i).number.equals(code) && !code.equals("1000000000")) {
                return array.get(i);
            }
        }
        return null;
    }

    public LineStation getPrevStation(ArrayList<LineStation> array, String station, int count) {
        try {
            String str = getStation(station);

            for(int i = 0; i < array.size(); i++) {
                if(array.get(i).station.equals(str)) {
                    LineStation ls = array.get(i);
                    int j = 0;

                    while(j < count) {
                        ls = getStationByCode(array, ls.prev);
                        j++;
                    }

                    return ls;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public LineStation getNextStation(ArrayList<LineStation> array, String station, int count) {
        try {
            String str = getStation(station);

            for(int i = 0; i < array.size(); i++) {
                if(array.get(i).station.equals(str)) {
                    LineStation ls = array.get(i);
                    int j = 0;

                    while(j < count) {
                        ls = getStationByCode(array, ls.next);
                        j++;
                    }

                    return ls;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<LineStation> getStationArray(int line) {
        ArrayList<LineStation> array = new ArrayList<>();
        String pRecvServerPage = openFile(context, line);
        ThemeManager tm = new ThemeManager(context);
        int posY = 0;

        try {
            JSONArray jArr = new JSONArray(pRecvServerPage);

            for (int i = 0; i < jArr.length(); i++) {
                JSONObject json = jArr.getJSONObject(i);

                if(json.getString("staNum").equals("1000000000")) {
                    posY += Units.dp(tm.getListSepHeight());
                } else {
                    posY += Units.dp(tm.getListHeight());
                }

                array.add(new LineStation(
                        json.getString("staName"),
                        json.getString("staSubText"),
                        json.getString("staNum"),
                        json.getString("staPrev"),
                        json.getString("staNext"),
                        json.getInt("staInfo"),
                        json.getInt("staExpress"),
                        posY
                ));
            }

            return array;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<LineStation> getStationArray(String line) {
        return getStationArray(Integer.parseInt(line));
    }

    private String openFile(Context context, int line) {
        String json;

        try {
            InputStream is = context.getAssets().open(line + ".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }
}