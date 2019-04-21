package com.ganada.silsiganmetro.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ganada.silsiganmetro.util.CalcDate;
import com.ganada.silsiganmetro.util.LineManager;
import com.ganada.silsiganmetro.util.DBManager;
import com.ganada.silsiganmetro.real.GetType;
import com.ganada.silsiganmetro.listitem.ListDouble;
import com.ganada.silsiganmetro.R;
import com.ganada.silsiganmetro.real.Realtime;
import com.ganada.silsiganmetro.MetroApplication;
import com.ganada.silsiganmetro.view.CustomTitlebar;
import com.ganada.silsiganmetro.view.StrokeTextView;
import com.ganada.silsiganmetro.util.ThemeManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class Line1Activity extends Activity {

	private SharedPreferences mPref;
	private SharedPreferences.Editor mPrefEdit;

	private MyAsyncTask myAsyncTask;

	ThemeManager tm;
	MetroApplication sv;
	ArrayList<ListDouble> arCustomList;
	ListDoubleAdapter adapter;
	CustomTitlebar layTitle;
	Animation animSpin;
	RelativeLayout layout_title;
	ImageView imgTime;
	Button btnRefresh;
	TextView btnInfo;
	ImageButton btnBack;
	ListView list;
	View footer;
	boolean boolInfo = false;
	boolean bool_type = true;
	boolean bool_line1 = false;

	String POS_LINE = "pos_line1";
	String BOOL_LINE = "bool_line1";

	int iTheme = 0;
	int iTime = 5;
	int iSec;
	int iPosition;
	int i_favorite;
	boolean bool_pos;
	String xml;
	String today;
	String sDayWeek;
	boolean bool_weekend;
	boolean bool_loaded;
	int MAX_XML = 880;
	int MAX_TRAIN = 120;
	int[] station = new int[MAX_TRAIN];
	int[] status = new int[MAX_TRAIN];
	int[] trainExpress = new int[MAX_TRAIN];
	String[] trainNo = new String[MAX_TRAIN];
	String[] trainHead = new String[MAX_TRAIN];
	String[] trainStatus = new String[MAX_TRAIN];
	String[] trainType = new String[MAX_TRAIN];
	String[] trainStart = new String[MAX_TRAIN];
	String[] xmlNumber = new String[MAX_XML];
	String[] xmlType = new String[MAX_XML];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_line8);

		arCustomList = new ArrayList<>();
		ListDouble listdouble;
		layTitle = findViewById(R.id.layTitle);
		imgTime = findViewById(R.id.imgTime);
		btnRefresh = findViewById(R.id.btnRefresh);
		footer = getLayoutInflater().inflate(R.layout.item_footer, null, false);

		mPref = getSharedPreferences("Pref1", 0);
		mPrefEdit = mPref.edit();
		tm = new ThemeManager(getBaseContext());
		iTheme = mPref.getInt("iTheme", 0);
		Window window = getWindow();
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), tm.getTitleBarColor(iTheme, LineManager.LINE_1)));
		}
		layTitle.setText("1호선");
		layTitle.setBackgroundColorById(tm.getTitleBarColor(iTheme, LineManager.LINE_1));

		iPosition = mPref.getInt(POS_LINE, 0);
		bool_pos = mPref.getBoolean(BOOL_LINE, false);
		iSec = mPref.getInt("timerefresh", 0) * 5;
		iTime = iSec;

		if(mPref.getInt("boolInfo", 1) == 0) {
			boolInfo = false;
		} else if(mPref.getInt("boolInfo", 1) == 1) {
			boolInfo = true;
		}

		if(mPref.getInt("boolType", 1) == 0) {
			bool_type = false;
		} else if(mPref.getInt("boolType", 1) == 1) {
			bool_type = true;
		}

		if(mPref.getInt("boolLine1", 1) == 0) {
			bool_line1 = false;
		} else if(mPref.getInt("boolLine1", 1) == 1) {
			bool_line1 = true;
		}

		/*
		단위역: 0
		환승역: 1
		기점: 2
		종점: 3

		급행 미정차: 0
		급행 정차: 1

		 */
		listdouble = new ListDouble("소요산 - 구로", 1000000000, 9, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("소요산", 1001000100, 2, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("동두천", 1001000101, 0, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("보산", 1001000102, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("동두천중앙", 1001000103, 0, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("지행", 1001000104, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("덕정", 1001000105, 0, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("덕계", 1001000106, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("양주", 1001000107, 0, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("녹양", 1001000108, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("가능", 1001000109, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("의정부", 1001000110, 0, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("회룡", 1001000111, 1, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("망월사", 1001000112, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("도봉산", 1001000113, 1, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("도봉", 1001000114, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("방학", 1001000115, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("창동", 1001000116, 1, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("녹천", 1001000117, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("월계", 1001000118, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("광운대", 1001000119, 1, 1, "광운대 - 서울역 각 역 정차");
		arCustomList.add(listdouble);
		listdouble = new ListDouble("석계", 1001000120, 1, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("신이문", 1001000121, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("외대앞", 1001000122, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("회기", 1001000123, 1, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("청량리", 1001000124, 1, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("제기동", 1001000125, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("신설동", 1001000126, 1, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("동묘앞", 1001000127, 1, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("동대문", 1001000128, 1, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("종로5가", 1001000129, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("종로3가", 1001000130, 1, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("종각", 1001000131, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("시청", 1001000132, 1, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("서울역", 1001000133, 1, 3, "소요산 - 인천 / 서울역 - 천안 급행");
		arCustomList.add(listdouble);
		listdouble = new ListDouble("남영", 1001000134, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("용산", 1001000135, 1, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("노량진", 1001000136, 1, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("대방", 1001000137, 0, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("신길", 1001000138, 1, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("영등포", 1001000139, 0, 3, "서울역 >> 천안 급행 미정차");
		arCustomList.add(listdouble);
		listdouble = new ListDouble("신도림", 1001000140, 1, 1, "영등포 급행 미정차");
		arCustomList.add(listdouble);
		listdouble = new ListDouble("구로", 1001000141, 1, 1, "영등포 급행 미정차");
		arCustomList.add(listdouble);
		if(bool_line1) {
			listdouble = new ListDouble("경부선(가산디지털단지 - 신창)", 1000000000, 9, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("가산디지털단지", 1001080142, 1, 1, "영등포 급행 미정차");
			arCustomList.add(listdouble);
			listdouble = new ListDouble("독산", 1001080143, 0, 0, "영등포 급행 미정차");
			arCustomList.add(listdouble);
			listdouble = new ListDouble("금천구청", 1001080144, 1, 2, "영등포 급행 미정차");
			arCustomList.add(listdouble);
			listdouble = new ListDouble("석수", 1001080145, 0, 0, "영등포 급행 미정차");
			arCustomList.add(listdouble);
			listdouble = new ListDouble("관악", 1001080146, 0, 0, "영등포 급행 미정차");
			arCustomList.add(listdouble);
			listdouble = new ListDouble("안양", 1001080147, 0, 3, "휴일 급행 미정차");
			arCustomList.add(listdouble);
			listdouble = new ListDouble("명학", 1001080148, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("금정", 1001080149, 1, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("군포", 1001080150, 0, 2, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("당정", 1001080151, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("의왕", 1001080152, 0, 2, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("성균관대", 1001080153, 0, 2, "서울역 >> 천안 급행 아침 한정");
			arCustomList.add(listdouble);
			listdouble = new ListDouble("화서", 1001080154, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("수원", 1001080155, 1, 3, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("세류", 1001080156, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("병점", 1001080157, 1, 3, "휴일 급행 미정차");
			arCustomList.add(listdouble);
			listdouble = new ListDouble("세마", 1001080158, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("오산대", 1001080159, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("오산", 1001080160, 0, 3, "휴일 급행 미정차");
			arCustomList.add(listdouble);
			listdouble = new ListDouble("진위", 1001080161, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("송탄", 1001080162, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("서정리", 1001080163, 0, 3, "휴일 급행 미정차");
			arCustomList.add(listdouble);
			listdouble = new ListDouble("지제", 1001080164, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("평택", 1001080165, 0, 3, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("성환", 1001080166, 0, 3, "휴일 급행 미정차");
			arCustomList.add(listdouble);
			listdouble = new ListDouble("직산", 1001080167, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("두정", 1001080168, 0, 3, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("천안", 1001080169, 0, 3, "신창 급행 천안 - 신창 각 역 정차");
			arCustomList.add(listdouble);
			listdouble = new ListDouble("봉명", 1001080170, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("쌍용", 1001080171, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("아산", 1001080172, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("배방", 1001080173, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("온양온천", 1001080174, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("신창", 1001080175, 3, 1, "신창 급행 천안 - 신창 각 역 정차");
			arCustomList.add(listdouble);
			listdouble = new ListDouble("경인선(구일 - 인천)", 1000000000, 9, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("구일", 1001000142, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("개봉", 1001000143, 0, 1, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("오류동", 1001000144, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("온수", 1001000145, 1, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("역곡", 1001000146, 0, 1, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("소사", 1001000147, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("부천", 1001000148, 0, 1, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("중동", 1001000149, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("송내", 1001000150, 0, 1, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("부개", 1001000151, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("부평", 1001000152, 1, 1, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("백운", 1001000153, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("동암", 1001000154, 0, 1, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("간석", 1001000155, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("주안", 1001000156, 0, 1, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("도화", 1001000157, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("제물포", 1001000158, 0, 1, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("도원", 1001000159, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("동인천", 1001000160, 0, 1, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("인천", 1001000161, 4, 0, null);
			arCustomList.add(listdouble);
		} else {
			listdouble = new ListDouble("경인선(구일 - 인천)", 1000000000, 9, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("구일", 1001000142, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("개봉", 1001000143, 0, 1, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("오류동", 1001000144, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("온수", 1001000145, 1, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("역곡", 1001000146, 0, 1, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("소사", 1001000147, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("부천", 1001000148, 0, 1, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("중동", 1001000149, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("송내", 1001000150, 0, 1, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("부개", 1001000151, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("부평", 1001000152, 1, 1, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("백운", 1001000153, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("동암", 1001000154, 0, 1, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("간석", 1001000155, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("주안", 1001000156, 0, 1, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("도화", 1001000157, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("제물포", 1001000158, 0, 1, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("도원", 1001000159, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("동인천", 1001000160, 0, 1, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("인천", 1001000161, 4, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("경부선(가산디지털단지 - 신창)", 1000000000, 9, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("가산디지털단지", 1001080142, 1, 1, "영등포 급행 미정차");
			arCustomList.add(listdouble);
			listdouble = new ListDouble("독산", 1001080143, 0, 0, "영등포 급행 미정차");
			arCustomList.add(listdouble);
			listdouble = new ListDouble("금천구청", 1001080144, 1, 2, "영등포 급행 미정차");
			arCustomList.add(listdouble);
			listdouble = new ListDouble("석수", 1001080145, 0, 0, "영등포 급행 미정차");
			arCustomList.add(listdouble);
			listdouble = new ListDouble("관악", 1001080146, 0, 0, "영등포 급행 미정차");
			arCustomList.add(listdouble);
			listdouble = new ListDouble("안양", 1001080147, 0, 3, "휴일 급행 미정차");
			arCustomList.add(listdouble);
			listdouble = new ListDouble("명학", 1001080148, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("금정", 1001080149, 1, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("군포", 1001080150, 0, 2, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("당정", 1001080151, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("의왕", 1001080152, 0, 2, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("성균관대", 1001080153, 0, 2, "서울역 >> 천안 급행 아침 한정");
			arCustomList.add(listdouble);
			listdouble = new ListDouble("화서", 1001080154, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("수원", 1001080155, 1, 3, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("세류", 1001080156, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("병점", 1001080157, 1, 3, "휴일 급행 미정차");
			arCustomList.add(listdouble);
			listdouble = new ListDouble("세마", 1001080158, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("오산대", 1001080159, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("오산", 1001080160, 0, 3, "휴일 급행 미정차");
			arCustomList.add(listdouble);
			listdouble = new ListDouble("진위", 1001080161, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("송탄", 1001080162, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("서정리", 1001080163, 0, 3, "휴일 급행 미정차");
			arCustomList.add(listdouble);
			listdouble = new ListDouble("지제", 1001080164, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("평택", 1001080165, 0, 3, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("성환", 1001080166, 0, 3, "휴일 급행 미정차");
			arCustomList.add(listdouble);
			listdouble = new ListDouble("직산", 1001080167, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("두정", 1001080168, 0, 3, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("천안", 1001080169, 0, 3, "신창 급행 천안 - 신창 각 역 정차");
			arCustomList.add(listdouble);
			listdouble = new ListDouble("봉명", 1001080170, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("쌍용", 1001080171, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("아산", 1001080172, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("배방", 1001080173, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("온양온천", 1001080174, 0, 0, null);
			arCustomList.add(listdouble);
			listdouble = new ListDouble("신창", 1001080175, 3, 1, "신창 급행 천안 - 신창 각 역 정차");
			arCustomList.add(listdouble);
		}

		adapter = new ListDoubleAdapter(this, tm.getListTheme(iTheme, LineManager.LINE_1, mPref.getInt("iLineStatus", 0)), arCustomList);

		animSpin = AnimationUtils.loadAnimation(this, R.anim.spin_anim);

		Intent intent = getIntent();
		bool_weekend = intent.getBooleanExtra("bool_weekend", false);
		//parse();
		AppStart();

		list = (ListView) findViewById(R.id.list);
		list.addFooterView(footer);
		list.setAdapter(adapter);
		list.setSelection(iPosition - 4);
		footer.setBackgroundResource(tm.getFooterBackgroundColor(iTheme));

		sv = (MetroApplication) getApplication();

		i_favorite = intent.getIntExtra("i_favorite", 0);

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		int rowHeight = mPref.getInt("iListSize", 66);

		if(i_favorite != 0) {
			list.setSelectionFromTop(i_favorite, (size.y / 2) - (rowHeight * 2) - tm.getTitleHeight());
		}

		if(sv.getPosition() != -1) {
			list.setSelectionFromTop(sv.getPosition(), (size.y / 2) - (rowHeight * 2) - tm.getTitleHeight());
		}

		btnRefresh.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				AppStart();
			}

		});

	}

	public class MyAsyncTask extends AsyncTask<String, Void, String> {

		String str_type;

		// 수행 전
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			imgTime.startAnimation(animSpin);
		}

		// 수행 >> 끝나면 종료
		@Override
		protected String doInBackground(String... params) {

			String sum = "";

			if(params != null){
				for(String s : params){
					sum += s;
				}
			}

			if(!bool_loaded) {
				Log.e("type", "Type Loaded");
				GetType gt = new GetType();
				str_type = gt.getJsonFile(1);
				String result_type = gt.getType(str_type);
				System.out.println(result_type);
				String[][] typeDate = jsonTypeList(result_type);
			}

			Realtime rt = new Realtime();
			String result = rt.getLine(sum);
			String[][] parsedData = jsonParserList(result);

			return sum;
		}

		// 종료
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			imgTime.clearAnimation();
			adapter.notifyDataSetChanged();
			if(result != null){
				Log.d("ASYNC", "result = " + result);
			}

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

	}

	private String[][] jsonParserList(String pRecvServerPage) {

		try {
			JSONObject json = new JSONObject(pRecvServerPage);
			JSONArray jArr = json.getJSONArray("realtimePositionList");

			String[] jsonName = {"statnId", "updnLine", "trainNo", "statnTnm", "trainSttus", "directAt"};
			String[][] parseredData = new String[jArr.length()][jsonName.length];
			for (int i = 0; i < jArr.length(); i++) {
				json = jArr.getJSONObject(i);

				for(int j = 0; j < jsonName.length; j++) {
					parseredData[i][j] = json.getString(jsonName[j]);
				}
			}


			ArrayReset();

			for(int i=0; i<parseredData.length; i++){
				//Log.i("분석 데이터: " + i + " : ", parseredData[i][0]);
				station[i] = Integer.parseInt(parseredData[i][0]);
				status[i] = Integer.parseInt(parseredData[i][1]);
				trainNo[i] = parseredData[i][2];
				trainHead[i] = tm.changeWord(parseredData[i][3]);
				if(trainHead[i].equals("동대문")) {
					trainHead[i] = "구로";
				} else if(trainHead[i].equals("종로3가")) {
					trainHead[i] = "광명";
				}
				trainStatus[i] = parseredData[i][4];
				if(trainStatus[i].equals("0")) {
					trainStatus[i] = "진입";
				} else if(trainStatus[i].equals("1")) {
					trainStatus[i] = "도착";
				} else {
					trainStatus[i] = "출발";
				}
				trainExpress[i] = Integer.parseInt(parseredData[i][5]);

				if(!trainNo[i].equals("1902") || !trainNo[i].equals("1904") || !trainNo[i].equals("1906")) {
					if(trainExpress[i] == 1 && status[i] == 0 && trainHead[i].equals("서울")) {
						trainHead[i] = "용산";
					}
				}

				for(int j = 0; j < MAX_XML; j++) {
					if(parseredData[i][2].equals(xmlNumber[j])) {
						trainType[i] = xmlType[j];
						break;
					}
				}
			}

			return parseredData;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String[][] jsonTypeList(String pRecvServerPage) {

		try {
			JSONObject json = new JSONObject(pRecvServerPage);
			JSONArray jArr = json.getJSONArray("train");

			String[] jsonName = {"trainNumber", "trainType"};
			String[][] parseredData = new String[jArr.length()][jsonName.length];
			for (int i = 0; i < jArr.length(); i++) {
				json = jArr.getJSONObject(i);

				for(int j = 0; j < jsonName.length; j++) {
					parseredData[i][j] = json.getString(jsonName[j]);
				}
			}

			for(int i=0; i<parseredData.length; i++){
				//Log.i("분석 데이터: " + i + " : ", parseredData[i][0]);
				xmlNumber[i] = parseredData[i][0];
				xmlType[i] = parseredData[i][1];
			}

			bool_loaded = true;
			return parseredData;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void alertDialog(final String strStation, final String strStart, final String strEnd) {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
		dialog.setContentView(R.layout.dialog_traininfo);
		dialog.setCanceledOnTouchOutside(true);

		// set the custom dialog components - text, image and button
		TextView dialog_txt_trainNo = (TextView) dialog.findViewById(R.id.dialog_txt_trainNo);
		TextView dialog_txt_sta_start = (TextView) dialog.findViewById(R.id.dialog_txt_sta_start);
		TextView dialog_txt_sta_end = (TextView) dialog.findViewById(R.id.dialog_txt_sta_end);

		dialog_txt_trainNo.setText(strStation);
		dialog_txt_sta_start.setText(strStart);
		dialog_txt_sta_end.setText(strEnd);

		dialog.show();
	}

	public class PersonViewHolder {
		public RelativeLayout layout_back;
		public RelativeLayout layout_line;
		public RelativeLayout layout_station;
		public RelativeLayout layout_train;
		public TextView train_up_norm;
		public TextView train_up_express;
		public TextView train_down_norm;
		public TextView train_down_express;
		public TextView txt_route;
		public ImageView img_icon_express;
		public ImageView img_icon_express1;
		public RelativeLayout img_train_down_norm;
		public RelativeLayout img_train_up_norm;
		public RelativeLayout img_train_down_express;
		public RelativeLayout img_train_up_express;
		public View view_line1;
		public View view_line2;
		public View view_line3;
		public View view_line4;
		public View view_line5;
		public View view_line6;
		public View view_line7;
		public View view_line8;
		public View view_divider;
		public StrokeTextView txt_station;
		public TextView txt_station_sub;
		public ImageView icon_station_down_norm;
		public ImageView icon_station_up_norm;
		public ImageView icon_station_down_express;
		public ImageView icon_station_up_express;
		public ImageView icon_train_up_norm;
		public ImageView icon_train_up_express;
		public ImageView icon_train_down_norm;
		public ImageView icon_train_down_express;
	}

	class ListDoubleAdapter extends BaseAdapter {
		Context con;
		LayoutInflater inflacter;
		ArrayList<ListDouble> arC;
		int layout;
		RelativeLayout.LayoutParams pm;

		public ListDoubleAdapter(Context context, int alayout, ArrayList<ListDouble> aarC) {
			con = context;
			inflacter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			arC = aarC;
			layout = alayout;
		}

		// 어댑터의 항목 수 조사
		@Override
		public int getCount() {
			return arC.size();
		}

		// position 위치 항목 Name 반환
		@Override
		public Object getItem(int position) {
			return null;
		}

		// position 위치 항목 ID 반환
		@Override
		public long getItemId(int position) {
			return 0;
		}

		// 각각의 항목 뷰 생성
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final PersonViewHolder viewHolder;

			if (convertView == null) {
				convertView = inflacter.inflate(layout, parent, false);

				viewHolder = new PersonViewHolder();
				viewHolder.layout_back = (RelativeLayout) convertView.findViewById(R.id.layout_back);
				viewHolder.layout_line = (RelativeLayout) convertView.findViewById(R.id.layout_line);
				viewHolder.layout_station = (RelativeLayout) convertView.findViewById(R.id.layout_station);
				viewHolder.layout_train = (RelativeLayout) convertView.findViewById(R.id.layout_train);
				viewHolder.train_up_norm = (TextView) convertView.findViewById(R.id.train_up_norm);
				viewHolder.train_up_express = (TextView) convertView.findViewById(R.id.train_up_express);
				viewHolder.train_down_norm = (TextView) convertView.findViewById(R.id.train_down_norm);
				viewHolder.train_down_express = (TextView) convertView.findViewById(R.id.train_down_express);
				viewHolder.txt_route = (TextView) convertView.findViewById(R.id.txt_route);
				viewHolder.img_icon_express = (ImageView) convertView.findViewById(R.id.img_icon_express);
				viewHolder.img_icon_express1 = (ImageView) convertView.findViewById(R.id.img_icon_express1);
				viewHolder.img_train_down_norm = (RelativeLayout) convertView.findViewById(R.id.img_train_down_norm);
				viewHolder.img_train_up_norm = (RelativeLayout) convertView.findViewById(R.id.img_train_up_norm);
				viewHolder.img_train_down_express = (RelativeLayout) convertView.findViewById(R.id.img_train_down_express);
				viewHolder.img_train_up_express = (RelativeLayout) convertView.findViewById(R.id.img_train_up_express);
				viewHolder.view_line1 = convertView.findViewById(R.id.view_line1);
				viewHolder.view_line2 = convertView.findViewById(R.id.view_line2);
				viewHolder.view_line3 = convertView.findViewById(R.id.view_line3);
				viewHolder.view_line4 = convertView.findViewById(R.id.view_line4);
				viewHolder.view_line5 = convertView.findViewById(R.id.view_line5);
				viewHolder.view_line6 = convertView.findViewById(R.id.view_line6);
				viewHolder.view_line7 = convertView.findViewById(R.id.view_line7);
				viewHolder.view_line8 = convertView.findViewById(R.id.view_line8);
				viewHolder.view_divider = convertView.findViewById(R.id.view_divider);
				viewHolder.txt_station = (StrokeTextView) convertView.findViewById(R.id.txt_station);
				viewHolder.txt_station_sub = (TextView) convertView.findViewById(R.id.txt_station_sub);
				viewHolder.icon_station_down_norm = (ImageView) convertView.findViewById(R.id.icon_station_down_norm);
				viewHolder.icon_station_up_norm = (ImageView) convertView.findViewById(R.id.icon_station_up_norm);
				viewHolder.icon_station_down_express = (ImageView) convertView.findViewById(R.id.icon_station_down_express);
				viewHolder.icon_station_up_express = (ImageView) convertView.findViewById(R.id.icon_station_up_express);
				viewHolder.icon_train_up_norm = (ImageView) convertView.findViewById(R.id.img_icon_up_norm);
				viewHolder.icon_train_up_express = (ImageView) convertView.findViewById(R.id.img_icon_up_express);
				viewHolder.icon_train_down_norm = (ImageView) convertView.findViewById(R.id.img_icon_down_norm);
				viewHolder.icon_train_down_express = (ImageView) convertView.findViewById(R.id.img_icon_down_express);

				convertView.setTag(viewHolder);

				RelativeLayout.LayoutParams param;
				param = (RelativeLayout.LayoutParams)viewHolder.layout_train.getLayoutParams();
				param.height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mPref.getInt("iListSize", 66), getResources().getDisplayMetrics());
				viewHolder.layout_train.setLayoutParams(param);

				viewHolder.train_up_norm.setTextSize(TypedValue.COMPLEX_UNIT_SP, tm.getTrainFontSize());
				viewHolder.train_up_express.setTextSize(TypedValue.COMPLEX_UNIT_SP, tm.getTrainFontSize());
				viewHolder.train_down_norm.setTextSize(TypedValue.COMPLEX_UNIT_SP, tm.getTrainFontSize());
				viewHolder.train_down_express.setTextSize(TypedValue.COMPLEX_UNIT_SP, tm.getTrainFontSize());
				viewHolder.train_up_norm.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getTrainColor(iTheme)));
				viewHolder.train_up_express.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getTrainColor(iTheme)));
				viewHolder.train_down_norm.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getTrainColor(iTheme)));
				viewHolder.train_down_express.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getTrainColor(iTheme)));
				viewHolder.train_up_norm.setBackgroundResource(tm.getTrainInfo(iTheme));
				viewHolder.train_up_express.setBackgroundResource(tm.getTrainInfo(iTheme));
				viewHolder.train_down_norm.setBackgroundResource(tm.getTrainInfo(iTheme));
				viewHolder.train_down_express.setBackgroundResource(tm.getTrainInfo(iTheme));
				viewHolder.view_divider.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), tm.getDividerColor(iTheme)));
			} else {
				viewHolder = (PersonViewHolder) convertView.getTag();
			}

			viewHolder.txt_station.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getStationColor(iTheme)));
			viewHolder.txt_station_sub.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getSubtextColor(iTheme)));
			viewHolder.txt_station.setTextSize(TypedValue.COMPLEX_UNIT_SP, tm.getStationFontSize());
			viewHolder.layout_back.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), tm.getListBackgroundColor(iTheme)));
			viewHolder.view_line1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_1));
			viewHolder.view_line2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_1));
			viewHolder.view_line3.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_1));
			viewHolder.view_line4.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_1));
			viewHolder.view_line5.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_1));
			viewHolder.view_line6.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_1));
			viewHolder.view_line7.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_1));
			viewHolder.view_line8.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_1));

			if(iTheme == 1) {
				viewHolder.txt_station.setStrokeWidth(0.0f);
			}

			if(iPosition == position && bool_pos && sv.getPosition() == -1) {
				viewHolder.layout_back.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.list_train_fa));
				viewHolder.txt_station.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_fa));
				viewHolder.txt_station.setStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.list_train_fa));
			} else if(sv.getPosition() == position) {
				viewHolder.layout_back.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), tm.getHighlightColor(iTheme)));
				viewHolder.txt_station.setStrokeColor(ContextCompat.getColor(getApplicationContext(), tm.getHighlightStrokeColor(iTheme)));
			} else {
				viewHolder.txt_station.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getStationColor(iTheme)));
				viewHolder.txt_station.setStrokeColor(ContextCompat.getColor(getApplicationContext(), tm.getStrokeColor(iTheme)));
			}

			if(arC.get(position).iStainfo == 9) {
				viewHolder.txt_station.setVisibility(View.GONE);
				viewHolder.txt_route.setVisibility(View.VISIBLE);
				viewHolder.txt_route.setText(arC.get(position).strStation);
				viewHolder.txt_route.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getSubtextColor(iTheme)));
				viewHolder.layout_back.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), tm.getFooterBackgroundColor(iTheme)));
				viewHolder.img_icon_express.setVisibility(View.GONE);
				viewHolder.img_icon_express1.setVisibility(View.GONE);
				viewHolder.txt_station_sub.setVisibility(View.GONE);
				viewHolder.img_train_up_norm.setVisibility(View.GONE);
				viewHolder.img_train_up_express.setVisibility(View.GONE);
				viewHolder.img_train_down_norm.setVisibility(View.GONE);
				viewHolder.img_train_down_express.setVisibility(View.GONE);
				viewHolder.view_divider.setVisibility(View.GONE);
				viewHolder.img_icon_express.setVisibility(View.GONE);
				viewHolder.layout_train.setVisibility(View.GONE);
			} else {
				viewHolder.txt_station.setVisibility(View.VISIBLE);
				viewHolder.txt_route.setVisibility(View.GONE);
				viewHolder.txt_station.setText(arC.get(position).strStation);
				viewHolder.img_train_up_norm.setVisibility(View.INVISIBLE);
				viewHolder.img_train_up_express.setVisibility(View.INVISIBLE);
				viewHolder.img_train_down_norm.setVisibility(View.INVISIBLE);
				viewHolder.img_train_down_express.setVisibility(View.INVISIBLE);
				viewHolder.layout_train.setVisibility(View.VISIBLE);
				viewHolder.view_divider.setVisibility(tm.getDividerVisibility(iTheme));

				if(arC.get(position).strSub == null) {
					viewHolder.txt_station_sub.setVisibility(View.GONE);
				} else {
					viewHolder.txt_station_sub.setVisibility(View.VISIBLE);
					viewHolder.txt_station_sub.setText(arC.get(position).strSub);
				}

				if(arC.get(position).iExpress == 0) {
					viewHolder.img_icon_express.setVisibility(View.GONE);
					viewHolder.img_icon_express1.setVisibility(View.GONE);
				} else if(arC.get(position).iExpress == 1) {
					viewHolder.img_icon_express.setVisibility(View.VISIBLE);
					viewHolder.img_icon_express1.setVisibility(View.GONE);
					viewHolder.img_icon_express.setImageResource(R.drawable.img_express);
				} else if(arC.get(position).iExpress == 2) {
					viewHolder.img_icon_express.setVisibility(View.VISIBLE);
					viewHolder.img_icon_express1.setVisibility(View.GONE);
					viewHolder.img_icon_express.setImageResource(R.drawable.img_express2);
				} else if(arC.get(position).iExpress == 3) {
					viewHolder.img_icon_express.setVisibility(View.VISIBLE);
					viewHolder.img_icon_express1.setVisibility(View.VISIBLE);
					viewHolder.img_icon_express.setImageResource(R.drawable.img_express);
					viewHolder.img_icon_express1.setImageResource(R.drawable.img_express2);
				}

				if (arC.get(position).iStainfo == 0) {
					// 일반
					viewHolder.view_line1.setVisibility(View.VISIBLE);
					viewHolder.view_line2.setVisibility(View.VISIBLE);
					viewHolder.view_line3.setVisibility(View.VISIBLE);
					viewHolder.view_line4.setVisibility(View.VISIBLE);
					viewHolder.view_line5.setVisibility(View.VISIBLE);
					viewHolder.view_line6.setVisibility(View.VISIBLE);
					viewHolder.view_line7.setVisibility(View.VISIBLE);
					viewHolder.view_line8.setVisibility(View.VISIBLE);
					viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1001_norm);
					viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1001_norm);
					if(arC.get(position).iExpress == 0) {
						viewHolder.icon_station_down_express.setVisibility(View.INVISIBLE);
						viewHolder.icon_station_up_express.setVisibility(View.INVISIBLE);
					} else {
						viewHolder.icon_station_down_express.setVisibility(View.VISIBLE);
						viewHolder.icon_station_up_express.setVisibility(View.VISIBLE);
						viewHolder.icon_station_down_express.setImageResource(R.drawable.icon_1001_norm);
						viewHolder.icon_station_up_express.setImageResource(R.drawable.icon_1001_norm);
					}
				} else if (arC.get(position).iStainfo == 1) {
					// 환승
					viewHolder.view_line1.setVisibility(View.VISIBLE);
					viewHolder.view_line2.setVisibility(View.VISIBLE);
					viewHolder.view_line3.setVisibility(View.VISIBLE);
					viewHolder.view_line4.setVisibility(View.VISIBLE);
					viewHolder.view_line5.setVisibility(View.VISIBLE);
					viewHolder.view_line6.setVisibility(View.VISIBLE);
					viewHolder.view_line7.setVisibility(View.VISIBLE);
					viewHolder.view_line8.setVisibility(View.VISIBLE);
					viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1001_tran);
					viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1001_tran);
					if(arC.get(position).iExpress == 0) {
						viewHolder.icon_station_down_express.setVisibility(View.INVISIBLE);
						viewHolder.icon_station_up_express.setVisibility(View.INVISIBLE);
					} else {
						viewHolder.icon_station_down_express.setVisibility(View.VISIBLE);
						viewHolder.icon_station_up_express.setVisibility(View.VISIBLE);
						viewHolder.icon_station_down_express.setImageResource(R.drawable.icon_1001_tran);
						viewHolder.icon_station_up_express.setImageResource(R.drawable.icon_1001_tran);
					}
				} else if (arC.get(position).iStainfo == 2) {
					// 맨 위
					viewHolder.view_line1.setVisibility(View.INVISIBLE);
					viewHolder.view_line2.setVisibility(View.VISIBLE);
					viewHolder.view_line3.setVisibility(View.INVISIBLE);
					viewHolder.view_line4.setVisibility(View.VISIBLE);
					viewHolder.view_line5.setVisibility(View.INVISIBLE);
					viewHolder.view_line6.setVisibility(View.VISIBLE);
					viewHolder.view_line7.setVisibility(View.INVISIBLE);
					viewHolder.view_line8.setVisibility(View.VISIBLE);
					viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1001_norm);
					viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1001_norm);
					if(arC.get(position).iExpress == 0) {
						viewHolder.icon_station_down_express.setVisibility(View.INVISIBLE);
						viewHolder.icon_station_up_express.setVisibility(View.INVISIBLE);
					} else {
						viewHolder.icon_station_down_express.setVisibility(View.VISIBLE);
						viewHolder.icon_station_up_express.setVisibility(View.VISIBLE);
						viewHolder.icon_station_down_express.setImageResource(R.drawable.icon_1001_norm);
						viewHolder.icon_station_up_express.setImageResource(R.drawable.icon_1001_norm);
					}
				} else if (arC.get(position).iStainfo == 3) {
					// 맨 아래
					viewHolder.view_line1.setVisibility(View.VISIBLE);
					viewHolder.view_line2.setVisibility(View.INVISIBLE);
					viewHolder.view_line3.setVisibility(View.VISIBLE);
					viewHolder.view_line4.setVisibility(View.INVISIBLE);
					viewHolder.view_line5.setVisibility(View.VISIBLE);
					viewHolder.view_line6.setVisibility(View.INVISIBLE);
					viewHolder.view_line7.setVisibility(View.VISIBLE);
					viewHolder.view_line8.setVisibility(View.INVISIBLE);
					viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1001_norm);
					viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1001_norm);
					if(arC.get(position).iExpress == 0) {
						viewHolder.icon_station_down_express.setVisibility(View.INVISIBLE);
						viewHolder.icon_station_up_express.setVisibility(View.INVISIBLE);
					} else {
						viewHolder.icon_station_down_express.setVisibility(View.VISIBLE);
						viewHolder.icon_station_up_express.setVisibility(View.VISIBLE);
						viewHolder.icon_station_down_express.setImageResource(R.drawable.icon_1001_norm);
						viewHolder.icon_station_up_express.setImageResource(R.drawable.icon_1001_norm);
					}
				}

				if(arC.get(position).iNum > 1001080169) {
					viewHolder.view_line1.setVisibility(View.VISIBLE);
					viewHolder.view_line2.setVisibility(View.VISIBLE);
					viewHolder.view_line3.setVisibility(View.VISIBLE);
					viewHolder.view_line4.setVisibility(View.VISIBLE);
					viewHolder.view_line5.setVisibility(View.VISIBLE);
					viewHolder.view_line6.setVisibility(View.VISIBLE);
					viewHolder.view_line7.setVisibility(View.VISIBLE);
					viewHolder.view_line8.setVisibility(View.VISIBLE);
					viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1001_norm);
					viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1001_norm);
					viewHolder.icon_station_down_express.setVisibility(View.VISIBLE);
					viewHolder.icon_station_up_express.setVisibility(View.VISIBLE);
					viewHolder.icon_station_down_express.setImageResource(R.drawable.icon_1001_norm);
					viewHolder.icon_station_up_express.setImageResource(R.drawable.icon_1001_norm);
				}

				if(arC.get(position).strStation.equals("동인천")) {
					viewHolder.view_line1.setVisibility(View.VISIBLE);
					viewHolder.view_line2.setVisibility(View.VISIBLE);
					viewHolder.view_line3.setVisibility(View.VISIBLE);
					viewHolder.view_line4.setVisibility(View.VISIBLE);
					viewHolder.view_line5.setVisibility(View.VISIBLE);
					viewHolder.view_line6.setVisibility(View.INVISIBLE);
					viewHolder.view_line7.setVisibility(View.VISIBLE);
					viewHolder.view_line8.setVisibility(View.INVISIBLE);
					viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1001_norm);
					viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1001_norm);
					viewHolder.icon_station_down_express.setVisibility(View.VISIBLE);
					viewHolder.icon_station_up_express.setVisibility(View.VISIBLE);
					viewHolder.icon_station_down_express.setImageResource(R.drawable.icon_1001_norm);
					viewHolder.icon_station_up_express.setImageResource(R.drawable.icon_1001_norm);
				} else if(arC.get(position).strStation.equals("인천")) {
					viewHolder.view_line1.setVisibility(View.VISIBLE);
					viewHolder.view_line2.setVisibility(View.INVISIBLE);
					viewHolder.view_line3.setVisibility(View.VISIBLE);
					viewHolder.view_line4.setVisibility(View.INVISIBLE);
					viewHolder.view_line5.setVisibility(View.INVISIBLE);
					viewHolder.view_line6.setVisibility(View.INVISIBLE);
					viewHolder.view_line7.setVisibility(View.INVISIBLE);
					viewHolder.view_line8.setVisibility(View.INVISIBLE);
					viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1001_tran);
					viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1001_tran);
					viewHolder.icon_station_down_express.setVisibility(View.INVISIBLE);
					viewHolder.icon_station_up_express.setVisibility(View.INVISIBLE);
				} else if(arC.get(position).strStation.equals("신창")) {
					viewHolder.view_line1.setVisibility(View.VISIBLE);
					viewHolder.view_line2.setVisibility(View.INVISIBLE);
					viewHolder.view_line3.setVisibility(View.VISIBLE);
					viewHolder.view_line4.setVisibility(View.INVISIBLE);
					viewHolder.view_line5.setVisibility(View.VISIBLE);
					viewHolder.view_line6.setVisibility(View.INVISIBLE);
					viewHolder.view_line7.setVisibility(View.VISIBLE);
					viewHolder.view_line8.setVisibility(View.INVISIBLE);
					viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1001_norm);
					viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1001_norm);
					viewHolder.icon_station_down_express.setVisibility(View.VISIBLE);
					viewHolder.icon_station_up_express.setVisibility(View.VISIBLE);
					viewHolder.icon_station_down_express.setImageResource(R.drawable.icon_1001_norm);
					viewHolder.icon_station_up_express.setImageResource(R.drawable.icon_1001_norm);
				}

				if(boolInfo) {
					for (int i = 0; i < MAX_TRAIN; i++) {
						if (station[i] == arC.get(position).iNum) {
							if (status[i] == 0) {
								if (trainExpress[i] == 0) {
									viewHolder.train_up_norm.setVisibility(View.VISIBLE);
									viewHolder.img_train_up_norm.setVisibility(View.VISIBLE);
									viewHolder.icon_train_up_norm.setImageResource(tm.getTrainIcon(iTheme, 0, false));
									if(!bool_type) {
										if(mPref.getInt("iTrainStatus", 0) == 0) {
											viewHolder.train_up_norm.setText(trainHead[i] + "\n" + trainNo[i]);
										} else {
											viewHolder.train_up_norm.setText(trainHead[i] + "\n" + trainNo[i] + "\n" + trainStatus[i]);
										}
									} else {
										if(mPref.getInt("iTrainStatus", 0) == 0) {
											viewHolder.train_up_norm.setText(trainHead[i] + "\n" + trainType[i] + trainNo[i]);
										} else {
											viewHolder.train_up_norm.setText(trainHead[i] + "\n" + trainType[i] + trainNo[i] + "\n" + trainStatus[i]);
										}
									}

									pm = (RelativeLayout.LayoutParams)viewHolder.img_train_up_norm.getLayoutParams();
									pm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
									pm.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
									pm.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

									if(trainStatus[i].equals("출발")) {
										//viewHolder.btn_train_up.setLayoutParams(new RelativeLayout.LayoutParams(param_pos));
										pm.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
									} else if(trainStatus[i].equals("진입")) {
										//viewHolder.btn_train_up.setLayoutParams(new RelativeLayout.LayoutParams(param_neg));
										pm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
									} else {
										//viewHolder.btn_train_up.setLayoutParams(new RelativeLayout.LayoutParams(param_def));
										pm.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
									}
									viewHolder.img_train_up_norm.setLayoutParams(pm);
								} else if (trainExpress[i] == 1) {
									viewHolder.train_up_express.setVisibility(View.VISIBLE);
									viewHolder.img_train_up_express.setVisibility(View.VISIBLE);
									viewHolder.icon_train_up_express.setImageResource(tm.getTrainIcon(iTheme, 0, true));
									if(!bool_type) {
										if(mPref.getInt("iTrainStatus", 0) == 0) {
											viewHolder.train_up_express.setText(trainHead[i] + "\n" + trainNo[i]);
										} else {
											viewHolder.train_up_express.setText(trainHead[i] + "\n" + trainNo[i] + "\n" + trainStatus[i]);
										}
									} else {
										if(mPref.getInt("iTrainStatus", 0) == 0) {
											viewHolder.train_up_express.setText(trainHead[i] + "\nK" + trainNo[i]);
										} else {
											viewHolder.train_up_express.setText(trainHead[i] + "\nK" + trainNo[i] + "\n" + trainStatus[i]);
										}
									}

									pm = (RelativeLayout.LayoutParams)viewHolder.img_train_up_express.getLayoutParams();
									pm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
									pm.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
									pm.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

									if(trainStatus[i].equals("출발")) {
										//viewHolder.btn_train_up.setLayoutParams(new RelativeLayout.LayoutParams(param_pos));
										pm.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
									} else if(trainStatus[i].equals("진입")) {
										//viewHolder.btn_train_up.setLayoutParams(new RelativeLayout.LayoutParams(param_neg));
										pm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
									} else {
										//viewHolder.btn_train_up.setLayoutParams(new RelativeLayout.LayoutParams(param_def));
										pm.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
									}
									viewHolder.img_train_up_express.setLayoutParams(pm);
								}

							} else if (status[i] == 1) {
								if (trainExpress[i] == 0) {
									viewHolder.train_down_norm.setVisibility(View.VISIBLE);
									viewHolder.img_train_down_norm.setVisibility(View.VISIBLE);
									viewHolder.icon_train_down_norm.setImageResource(tm.getTrainIcon(iTheme, 1, false));
									if(!bool_type) {
										if(mPref.getInt("iTrainStatus", 0) == 0) {
											viewHolder.train_down_norm.setText(trainHead[i] + "\n" + trainNo[i]);
										} else {
											viewHolder.train_down_norm.setText(trainHead[i] + "\n" + trainNo[i] + "\n" + trainStatus[i]);
										}
									} else {
										if(mPref.getInt("iTrainStatus", 0) == 0) {
											viewHolder.train_down_norm.setText(trainHead[i] + "\n" + trainType[i] + trainNo[i]);
										} else {
											viewHolder.train_down_norm.setText(trainHead[i] + "\n" + trainType[i] + trainNo[i] + "\n" + trainStatus[i]);
										}
									}

									pm = (RelativeLayout.LayoutParams)viewHolder.img_train_down_norm.getLayoutParams();
									pm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
									pm.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
									pm.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

									if(trainStatus[i].equals("출발")) {
										//viewHolder.btn_train_up.setLayoutParams(new RelativeLayout.LayoutParams(param_pos));
										pm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
									} else if(trainStatus[i].equals("진입")) {
										//viewHolder.btn_train_up.setLayoutParams(new RelativeLayout.LayoutParams(param_neg));
										pm.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
									} else {
										//viewHolder.btn_train_up.setLayoutParams(new RelativeLayout.LayoutParams(param_def));
										pm.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
									}
									viewHolder.img_train_down_norm.setLayoutParams(pm);
								} else if(trainExpress[i] == 1) {
									viewHolder.train_down_express.setVisibility(View.VISIBLE);
									viewHolder.img_train_down_express.setVisibility(View.VISIBLE);
									viewHolder.icon_train_down_express.setImageResource(tm.getTrainIcon(iTheme, 1, true));
									if(!bool_type) {
										if(mPref.getInt("iTrainStatus", 0) == 0) {
											viewHolder.train_down_express.setText(trainHead[i] + "\n" + trainNo[i]);
										} else {
											viewHolder.train_down_express.setText(trainHead[i] + "\n" + trainNo[i] + "\n" + trainStatus[i]);
										}
									} else {
										if(mPref.getInt("iTrainStatus", 0) == 0) {
											viewHolder.train_down_express.setText(trainHead[i] + "\nK" + trainNo[i]);
										} else {
											viewHolder.train_down_express.setText(trainHead[i] + "\nK" + trainNo[i] + "\n" + trainStatus[i]);
										}
									}

									pm = (RelativeLayout.LayoutParams)viewHolder.img_train_down_express.getLayoutParams();
									pm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
									pm.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
									pm.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

									if(trainStatus[i].equals("출발")) {
										//viewHolder.btn_train_up.setLayoutParams(new RelativeLayout.LayoutParams(param_pos));
										pm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
									} else if(trainStatus[i].equals("진입")) {
										//viewHolder.btn_train_up.setLayoutParams(new RelativeLayout.LayoutParams(param_neg));
										pm.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
									} else {
										//viewHolder.btn_train_up.setLayoutParams(new RelativeLayout.LayoutParams(param_def));
										pm.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
									}
									viewHolder.img_train_down_express.setLayoutParams(pm);
								}
							}
						}
					}
				} else {
					for (int i = 0; i < MAX_TRAIN; i++) {
						if (station[i] == arC.get(position).iNum) {
							if (status[i] == 0) {
								if (trainExpress[i] == 0) {
									viewHolder.train_up_norm.setVisibility(View.INVISIBLE);
									viewHolder.img_train_up_norm.setVisibility(View.VISIBLE);
									viewHolder.icon_train_up_norm.setImageResource(tm.getTrainIcon(iTheme, 0, false));

									pm = (RelativeLayout.LayoutParams)viewHolder.img_train_up_norm.getLayoutParams();
									pm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
									pm.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
									pm.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

									if(trainStatus[i].equals("출발")) {
										//viewHolder.btn_train_up.setLayoutParams(new RelativeLayout.LayoutParams(param_pos));
										pm.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
									} else if(trainStatus[i].equals("진입")) {
										//viewHolder.btn_train_up.setLayoutParams(new RelativeLayout.LayoutParams(param_neg));
										pm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
									} else {
										//viewHolder.btn_train_up.setLayoutParams(new RelativeLayout.LayoutParams(param_def));
										pm.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
									}
									viewHolder.img_train_up_norm.setLayoutParams(pm);
								} else if (trainExpress[i] == 1) {
									viewHolder.train_up_express.setVisibility(View.INVISIBLE);
									viewHolder.img_train_up_express.setVisibility(View.VISIBLE);
									viewHolder.icon_train_up_express.setImageResource(tm.getTrainIcon(iTheme, 0, true));

									pm = (RelativeLayout.LayoutParams)viewHolder.img_train_up_express.getLayoutParams();
									pm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
									pm.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
									pm.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

									if(trainStatus[i].equals("출발")) {
										//viewHolder.btn_train_up.setLayoutParams(new RelativeLayout.LayoutParams(param_pos));
										pm.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
									} else if(trainStatus[i].equals("진입")) {
										//viewHolder.btn_train_up.setLayoutParams(new RelativeLayout.LayoutParams(param_neg));
										pm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
									} else {
										//viewHolder.btn_train_up.setLayoutParams(new RelativeLayout.LayoutParams(param_def));
										pm.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
									}
									viewHolder.img_train_up_express.setLayoutParams(pm);
								}

							} else if (status[i] == 1) {
								if (trainExpress[i] == 0) {
									viewHolder.train_down_norm.setVisibility(View.INVISIBLE);
									viewHolder.img_train_down_norm.setVisibility(View.VISIBLE);
									viewHolder.icon_train_down_norm.setImageResource(tm.getTrainIcon(iTheme, 1, false));

									pm = (RelativeLayout.LayoutParams)viewHolder.img_train_down_norm.getLayoutParams();
									pm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
									pm.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
									pm.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

									if(trainStatus[i].equals("출발")) {
										//viewHolder.btn_train_up.setLayoutParams(new RelativeLayout.LayoutParams(param_pos));
										pm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
									} else if(trainStatus[i].equals("진입")) {
										//viewHolder.btn_train_up.setLayoutParams(new RelativeLayout.LayoutParams(param_neg));
										pm.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
									} else {
										//viewHolder.btn_train_up.setLayoutParams(new RelativeLayout.LayoutParams(param_def));
										pm.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
									}
									viewHolder.img_train_down_norm.setLayoutParams(pm);
								} else if(trainExpress[i] == 1) {
									viewHolder.train_down_express.setVisibility(View.INVISIBLE);
									viewHolder.img_train_down_express.setVisibility(View.VISIBLE);
									viewHolder.icon_train_down_express.setImageResource(tm.getTrainIcon(iTheme, 1, true));

									pm = (RelativeLayout.LayoutParams)viewHolder.img_train_down_express.getLayoutParams();
									pm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
									pm.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
									pm.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

									if(trainStatus[i].equals("출발")) {
										//viewHolder.btn_train_up.setLayoutParams(new RelativeLayout.LayoutParams(param_pos));
										pm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
									} else if(trainStatus[i].equals("진입")) {
										//viewHolder.btn_train_up.setLayoutParams(new RelativeLayout.LayoutParams(param_neg));
										pm.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
									} else {
										//viewHolder.btn_train_up.setLayoutParams(new RelativeLayout.LayoutParams(param_def));
										pm.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
									}
									viewHolder.img_train_down_express.setLayoutParams(pm);
								}
							}
						}
					}
				}
			}

			viewHolder.train_up_norm.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					CalcDate cd = new CalcDate();
					StringBuffer sb = new StringBuffer();
					sb.append("https://rail.blue/railroad/logis/Default.aspx?date=");
					sb.append(cd.getDate());
					sb.append("&train=");
					sb.append(viewHolder.train_up_norm.getText().toString().split("\n")[1]);

					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sb.toString()));
					startActivity(intent);
				}
			});

			viewHolder.train_down_norm.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					CalcDate cd = new CalcDate();
					StringBuffer sb = new StringBuffer();
					sb.append("https://rail.blue/railroad/logis/Default.aspx?date=");
					sb.append(cd.getDate());
					sb.append("&train=");
					sb.append(viewHolder.train_down_norm.getText().toString().split("\n")[1]);

					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sb.toString()));
					startActivity(intent);
				}
			});

			viewHolder.train_up_express.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					CalcDate cd = new CalcDate();
					StringBuffer sb = new StringBuffer();
					sb.append("https://rail.blue/railroad/logis/Default.aspx?date=");
					sb.append(cd.getDate());
					sb.append("&train=");
					sb.append("K");
					sb.append(viewHolder.train_up_express.getText().toString().split("\n")[1].replace("K", ""));

					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sb.toString()));
					startActivity(intent);
				}
			});

			viewHolder.train_down_express.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					CalcDate cd = new CalcDate();
					StringBuffer sb = new StringBuffer();
					sb.append("https://rail.blue/railroad/logis/Default.aspx?date=");
					sb.append(cd.getDate());
					sb.append("&train=");
					sb.append("K");
					sb.append(viewHolder.train_down_express.getText().toString().split("\n")[1].replace("K", ""));

					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sb.toString()));
					startActivity(intent);
				}
			});

			convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if(arC.get(position).iNum != 1000000000) {
						Intent intent = new Intent(getApplicationContext(), StationActivity.class);
						intent.putExtra("str_station", arC.get(position).strStation);
						intent.putExtra("str_num", String.valueOf(arC.get(position).iNum));
						startActivity(intent);
					}
				}
			});

			convertView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					// 토스트 메세지로 strList 출력
					if(arC.get(position).iNum != 1000000000) {
						SetFavor(position, arC.get(position).strStation);
					}
					return true;
				}
			});

			/*viewHolder.btn_train_up1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String[] str = new String[4];
					for (int i = 0; i < MAX_TRAIN; i++) {
						if (station[i] == favorites.get(position).iNum && status[i] == 0) {
							str[0] = trainType[i];
							str[1] = trainNo[i];
							str[2] = trainStart[i];
							str[3] = trainHead[i];
						}
					}
					alertDialog(str[0] + str[1], str[2], str[3]);
				}
			});

			viewHolder.btn_train_up2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String[] str = new String[4];
					for (int i = 0; i < MAX_TRAIN; i++) {
						if (station[i] == favorites.get(position).iNum && status[i] == 0) {
							str[0] = trainType[i];
							str[1] = trainNo[i];
							str[2] = trainStart[i];
							str[3] = trainHead[i];
						}
					}
					alertDialog(str[0] + str[1], str[2], str[3]);
				}
			});

			viewHolder.btn_train_down1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String[] str = new String[4];
					for (int i = 0; i < MAX_TRAIN; i++) {
						if (station[i] == favorites.get(position).iNum && status[i] == 1) {
							str[0] = trainType[i];
							str[1] = trainNo[i];
							str[2] = trainStart[i];
							str[3] = trainHead[i];
						}
					}
					alertDialog(str[0] + str[1], str[2], str[3]);
				}
			});

			viewHolder.btn_train_down2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String[] str = new String[4];
					for (int i = 0; i < MAX_TRAIN; i++) {
						if (station[i] == favorites.get(position).iNum && status[i] == 1) {
							str[0] = trainType[i];
							str[1] = trainNo[i];
							str[2] = trainStart[i];
							str[3] = trainHead[i];
						}
					}
					alertDialog(str[0] + str[1], str[2], str[3]);
				}
			});*/

			return convertView;
		}
	}

	public void SetFavor(int position, String station) {
		String[] arr = {"즐겨찾기 설정/해제", "모아보기 추가", "메인 화면에 추가"};
		final int index = position;
		final String str_station = station;

		AlertDialog.Builder alert = new AlertDialog.Builder(Line1Activity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
		alert.setTitle(String.format(Locale.KOREA, "%s역 선택", station));
		alert.setItems(arr, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// The 'which' argument contains the index position
				// of the selected item
				switch(which) {
					case 0:
						if(bool_pos && iPosition == index) {
							iPosition = 0;
							bool_pos = false;
							mPrefEdit.putInt(POS_LINE, 0);
							mPrefEdit.putBoolean(BOOL_LINE, false);
							Toast.makeText(getApplicationContext(), "즐겨찾기를 제거하였습니다.", Toast.LENGTH_SHORT).show();
						} else {
							iPosition = index;
							bool_pos = true;
							mPrefEdit.putInt(POS_LINE, index);
							mPrefEdit.putBoolean(BOOL_LINE, true);
							Toast.makeText(getApplicationContext(), arCustomList.get(index).strStation + "역을 즐겨찾기로 설정하였습니다.", Toast.LENGTH_SHORT).show();
						}
						mPrefEdit.commit();
						adapter.notifyDataSetChanged();
						break;

					case 1:
						LineManager lineManager = new LineManager(getApplicationContext());
						lineManager.setFavorite(str_station, LineManager.LINE_1, index);
						Toast.makeText(getApplicationContext(), arCustomList.get(index).strStation + "역을 모아보기에 추가하였습니다.", Toast.LENGTH_SHORT).show();
						break;

					case 2:
						DBManager dm = new DBManager(getApplicationContext());
						dm.setStation("1호선", str_station + ":" + index);
						Toast.makeText(getApplicationContext(), arCustomList.get(index).strStation + "역을 메인 화면에 추가하였습니다.", Toast.LENGTH_SHORT).show();
						break;

					default:
						break;
				}
			}
		});

		AlertDialog alert1 = alert.create();
		alert1.show();

	}

	public void AppStart() {
		myAsyncTask = new MyAsyncTask();
		myAsyncTask.execute("1호선");
	}

	public void ArrayReset() {
		for(int i = 0; i < MAX_TRAIN; i++) {
			station[i] = 0;
			status[i] = 0;
			trainNo[i] = null;
			trainHead[i] = null;
			trainStatus[i] = null;
			trainExpress[i] = 0;
			trainType[i] = "";
			trainStart[i] = "";
		}
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {

			if(iTime > 0) {
				iTime--;
			} else {
				AppStart();
				imgTime.startAnimation(animSpin);
				iTime = iSec;
			}

			if(iTime == 0 && iSec == 0) {
				AppStart();
				imgTime.startAnimation(animSpin);
			} else {
				Log.e("iTime >>>>>> ", "iTime = " + iTime);
				mHandler.sendEmptyMessageDelayed(0, 1000);
			}
		}
	};

	@Override
	public void onPause() {
		super.onPause();
		myAsyncTask.cancel(true);
		imgTime.clearAnimation();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onResume() {
		super.onResume();
		if(iSec != 0) {
			mHandler.sendEmptyMessage(0);
		}
	}
}
