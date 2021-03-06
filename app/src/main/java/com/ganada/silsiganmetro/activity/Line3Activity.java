package com.ganada.silsiganmetro.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ganada.silsiganmetro.util.CalcDate;
import com.ganada.silsiganmetro.util.LineManager;
import com.ganada.silsiganmetro.util.DBManager;
import com.ganada.silsiganmetro.real.GetType;
import com.ganada.silsiganmetro.listitem.ListNorm;
import com.ganada.silsiganmetro.R;
import com.ganada.silsiganmetro.real.Realtime;
import com.ganada.silsiganmetro.MetroApplication;
import com.ganada.silsiganmetro.util.ThemeManager;
import com.ganada.silsiganmetro.view.CustomTitlebar;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class Line3Activity extends Activity {

	private SharedPreferences mPref;
	private SharedPreferences.Editor mPrefEdit;

	private MyAsyncTask myAsyncTask;

	ThemeManager tm;
	MetroApplication sv;
	ArrayList<ListNorm> arCustomList;
	ListNormAdapter adapter;
	CustomTitlebar layTitle;
	ListView list;
	Animation animSpin;
	ImageView imgTime;
	Button btnRefresh;
	RelativeLayout layout_title;
	TextView btnInfo;
	ImageButton btnBack;
	View footer;
	boolean boolInfo = false;
	boolean bool_type = true;
	boolean bool_loaded = false;

	int iTheme = 0;
	int iTime = 5;
	int iSec;
	int iPosition;
	int i_favorite;
	boolean bool_pos;
	String POS_LINE = "pos_line3";
	String BOOL_LINE = "bool_line3";

	int MAX_XML = 450;
	int MAX_TRAIN = 120;

	String xml;
	String today;
	String sDayWeek;
	boolean bool_weekend;
	int[] station = new int[MAX_TRAIN];
	int[] status = new int[MAX_TRAIN];
	String[] trainNo = new String[MAX_TRAIN];
	String[] trainHead = new String[MAX_TRAIN];
	String[] trainStatus = new String[MAX_TRAIN];
	String[] xmlNumber = new String[MAX_XML];
	String[] xmlType = new String[MAX_XML];
	String[] trainType = new String[MAX_TRAIN];

	String PACKAGE_DIR = "com.ganada.silsiganmetro";
	String DATABASE = "test_data.db";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_line8);

		arCustomList = new ArrayList<>();
		ListNorm listnorm;
		imgTime = findViewById(R.id.imgTime);
		btnRefresh = findViewById(R.id.btnRefresh);
		layTitle = findViewById(R.id.layTitle);
		footer = getLayoutInflater().inflate(R.layout.item_footer, null, false);

		mPref = getSharedPreferences("Pref1", 0);
		mPrefEdit = mPref.edit();
		tm = new ThemeManager(getBaseContext());
		iTheme = mPref.getInt("iTheme", 0);
		Window window = getWindow();
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), tm.getTitleBarColor(iTheme, LineManager.LINE_3)));
		}
		layTitle.setText("3호선");
		layTitle.setBackgroundColorById(tm.getTitleBarColor(iTheme, LineManager.LINE_3));

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

		/*
		단위역: 0
		환승역: 1
		기점: 2
		종점: 3

		 */
		listnorm = new ListNorm("대화", 1003000310, 2);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("주엽", 1003000311, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("정발산", 1003000312, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("마두", 1003000313, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("백석", 1003000314, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("대곡", 1003000315, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("화정", 1003000316, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("원당", 1003000317, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("원흥", 1003000309, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("삼송", 1003000318, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("지축", 1003000319, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("구파발", 1003000320, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("연신내", 1003000321, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("불광", 1003000322, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("녹번", 1003000323, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("홍제", 1003000324, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("무악재", 1003000325, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("독립문", 1003000326, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("경복궁", 1003000327, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("안국", 1003000328, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("종로3가", 1003000329, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("을지로3가", 1003000330, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("충무로", 1003000331, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("동대입구", 1003000332, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("약수", 1003000333, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("금호", 1003000334, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("옥수", 1003000335, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("압구정", 1003000336, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("신사", 1003000337, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("잠원", 1003000338, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("고속터미널", 1003000339, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("교대", 1003000340, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("남부터미널", 1003000341, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("양재", 1003000342, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("매봉", 1003000343, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("도곡", 1003000344, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("대치", 1003000345, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("학여울", 1003000346, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("대청", 1003000347, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("일원", 1003000348, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("수서", 1003000349, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("가락시장", 1003000350, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("경찰병원", 1003000351, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("오금", 1003000352, 3);
		arCustomList.add(listnorm);

		adapter = new ListNormAdapter(this, tm.getListTheme(iTheme, LineManager.LINE_3, mPref.getInt("iLineStatus", 0)), arCustomList);

		animSpin = AnimationUtils.loadAnimation(this, R.anim.spin_anim);

		list = (ListView) findViewById(R.id.list);
		list.addFooterView(footer);
		list.setAdapter(adapter);
		list.setSelection(iPosition - 4);
		footer.setBackgroundResource(tm.getFooterBackgroundColor(iTheme));

		sv = (MetroApplication) getApplication();

		Intent intent = getIntent();
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

		bool_weekend = intent.getBooleanExtra("bool_weekend", false);
		//parse();
		AppStart();

		//Toast.makeText(getApplicationContext(), "" + bool_weekend, Toast.LENGTH_SHORT).show();

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
				str_type = gt.getJsonFile(3);
				String result_type = gt.getType(str_type);
				System.out.println(result_type);
				String[][] typeDate = jsonTypeList(result_type);
			}

			Realtime rt = new Realtime();
			String result = rt.getLine(sum);
			jsonParserList(result);
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
				Log.i("분석 데이터: " + i + " : ", parseredData[i][0]);
				station[i] = Integer.parseInt(parseredData[i][0]);
				status[i] = Integer.parseInt(parseredData[i][1]);
				trainNo[i] = parseredData[i][2];
				trainHead[i] = tm.changeWord(parseredData[i][3]);
				trainStatus[i] = parseredData[i][4];
				if(trainStatus[i].equals("0")) {
					trainStatus[i] = "진입";
				} else if(trainStatus[i].equals("1")) {
					trainStatus[i] = "도착";
				} else {
					trainStatus[i] = "출발";
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
				Log.i("분석 데이터: " + i + " : ", parseredData[i][0]);
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

	public class PersonViewHolder {
		public RelativeLayout layout_back;
		public LinearLayout layout_line;
		public RelativeLayout layout_station;
		public RelativeLayout layout_train;
		public TextView btn_train_up;
		public TextView btn_train_down;
		public ImageView img_icon_express;
		public RelativeLayout img_train_down_norm;
		public RelativeLayout img_train_up_norm;
		//public ImageView img_line;
		public View view_line1;
		public View view_line2;
		public View view_line3;
		public View view_line4;
		public View view_margin;
		public View view_divider;
		public TextView txt_station;
		public TextView txt_station_sub;
		public ImageView icon_station_down_norm;
		public ImageView icon_station_up_norm;
		public ImageView icon_train_up;
		public ImageView icon_train_down;
	}

	class ListNormAdapter extends BaseAdapter {
		Context con;
		LayoutInflater inflacter;
		ArrayList<ListNorm> arC;
		int layout;
		RelativeLayout.LayoutParams pm;

		public ListNormAdapter(Context context, int alayout, ArrayList<ListNorm> aarC) {
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
				viewHolder.layout_line = (LinearLayout) convertView.findViewById(R.id.layout_line);
				viewHolder.layout_station = (RelativeLayout) convertView.findViewById(R.id.layout_station);
				viewHolder.layout_train = (RelativeLayout) convertView.findViewById(R.id.layout_train);
				viewHolder.btn_train_up = (TextView) convertView.findViewById(R.id.train_up_01);
				viewHolder.btn_train_down = (TextView) convertView.findViewById(R.id.train_down_01);
				viewHolder.img_icon_express = (ImageView) convertView.findViewById(R.id.img_icon_express);
				viewHolder.img_train_down_norm = (RelativeLayout) convertView.findViewById(R.id.img_train_down_norm);
				viewHolder.img_train_up_norm = (RelativeLayout) convertView.findViewById(R.id.img_train_up_norm);
				viewHolder.view_line1 = convertView.findViewById(R.id.view_line1);
				viewHolder.view_line2 = convertView.findViewById(R.id.view_line2);
				viewHolder.view_line3 = convertView.findViewById(R.id.view_line3);
				viewHolder.view_line4 = convertView.findViewById(R.id.view_line4);
				viewHolder.view_margin = convertView.findViewById(R.id.view_margin);
				viewHolder.view_divider = convertView.findViewById(R.id.view_divider);
				viewHolder.txt_station = (TextView) convertView.findViewById(R.id.txt_station);
				viewHolder.txt_station_sub = (TextView) convertView.findViewById(R.id.txt_station_sub);
				viewHolder.icon_station_down_norm = (ImageView) convertView.findViewById(R.id.icon_station_down_norm);
				viewHolder.icon_station_up_norm = (ImageView) convertView.findViewById(R.id.icon_station_up_norm);
				viewHolder.icon_train_up = (ImageView) convertView.findViewById(R.id.imgUp);
				viewHolder.icon_train_down = (ImageView) convertView.findViewById(R.id.img_icon_down);

				convertView.setTag(viewHolder);

				RelativeLayout.LayoutParams param;
				param = (RelativeLayout.LayoutParams)viewHolder.layout_train.getLayoutParams();
				param.height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mPref.getInt("iListSize", 66), getResources().getDisplayMetrics());
				viewHolder.layout_train.setLayoutParams(param);

				viewHolder.btn_train_up.setTextSize(TypedValue.COMPLEX_UNIT_SP, tm.getTrainFontSize());
				viewHolder.btn_train_down.setTextSize(TypedValue.COMPLEX_UNIT_SP, tm.getTrainFontSize());
				viewHolder.btn_train_up.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getTrainColor(iTheme)));
				viewHolder.btn_train_down.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getTrainColor(iTheme)));
				viewHolder.btn_train_up.setBackgroundResource(tm.getTrainInfo(iTheme));
				viewHolder.btn_train_down.setBackgroundResource(tm.getTrainInfo(iTheme));
				viewHolder.view_divider.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), tm.getDividerColor(iTheme)));
			} else {
				viewHolder = (PersonViewHolder) convertView.getTag();
			}

			viewHolder.txt_station_sub.setVisibility(View.GONE);
			viewHolder.txt_station.setText(arC.get(position).strStation);
			viewHolder.txt_station.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getStationColor(iTheme)));
			viewHolder.txt_station.setTextSize(TypedValue.COMPLEX_UNIT_SP, tm.getStationFontSize());
			viewHolder.layout_back.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), tm.getListBackgroundColor(iTheme)));
			viewHolder.view_line1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_3));
			viewHolder.view_line2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_3));
			viewHolder.view_line3.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_3));
			viewHolder.view_line4.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_3));

			if(iPosition == position && bool_pos && sv.getPosition() == -1) {
				viewHolder.layout_back.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.list_train_fa));
				viewHolder.txt_station.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_fa));
			} else if(sv.getPosition() == position) {
				viewHolder.layout_back.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), tm.getHighlightColor(iTheme)));
			}

			viewHolder.btn_train_up.setVisibility(View.INVISIBLE);
			viewHolder.btn_train_down.setVisibility(View.INVISIBLE);
			viewHolder.img_train_down_norm.setVisibility(View.INVISIBLE);
			viewHolder.img_train_up_norm.setVisibility(View.INVISIBLE);
			viewHolder.img_icon_express.setVisibility(View.INVISIBLE);
			viewHolder.view_margin.setVisibility(View.VISIBLE);
			viewHolder.layout_train.setVisibility(View.VISIBLE);
			viewHolder.view_divider.setVisibility(tm.getDividerVisibility(iTheme));

			if (arC.get(position).iStainfo == 0) {
				// 일반
				viewHolder.view_line1.setVisibility(View.VISIBLE);
				viewHolder.view_line2.setVisibility(View.VISIBLE);
				viewHolder.view_line3.setVisibility(View.VISIBLE);
				viewHolder.view_line4.setVisibility(View.VISIBLE);
				viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1003_norm);
				viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1003_norm);
			} else if (arC.get(position).iStainfo == 1) {
				// 환승
				viewHolder.view_line1.setVisibility(View.VISIBLE);
				viewHolder.view_line2.setVisibility(View.VISIBLE);
				viewHolder.view_line3.setVisibility(View.VISIBLE);
				viewHolder.view_line4.setVisibility(View.VISIBLE);
				viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1003_tran);
				viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1003_tran);
			} else if (arC.get(position).iStainfo == 2) {
				// 맨 위
				viewHolder.view_line1.setVisibility(View.INVISIBLE);
				viewHolder.view_line2.setVisibility(View.VISIBLE);
				viewHolder.view_line3.setVisibility(View.INVISIBLE);
				viewHolder.view_line4.setVisibility(View.VISIBLE);
				viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1003_norm);
				viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1003_norm);
			} else if (arC.get(position).iStainfo == 3) {
				// 맨 아래
				viewHolder.view_line1.setVisibility(View.VISIBLE);
				viewHolder.view_line2.setVisibility(View.INVISIBLE);
				viewHolder.view_line3.setVisibility(View.VISIBLE);
				viewHolder.view_line4.setVisibility(View.INVISIBLE);
				viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1003_tran);
				viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1003_tran);
			}

			if(boolInfo) {
				for (int i = 0; i < MAX_TRAIN; i++) {
					if (station[i] == arC.get(position).iNum) {
						if (status[i] == 0) {
							viewHolder.btn_train_up.setVisibility(View.VISIBLE);
							viewHolder.img_train_up_norm.setVisibility(View.VISIBLE);
							viewHolder.icon_train_up.setImageResource(tm.getTrainIcon(iTheme, 0, false));

							if(!bool_type) {
								if(mPref.getInt("iTrainStatus", 0) == 0) {
									viewHolder.btn_train_up.setText(trainHead[i] + "\n" + trainNo[i]);
								} else {
									viewHolder.btn_train_up.setText(trainHead[i] + "\n" + trainNo[i] + "\n" + trainStatus[i]);
								}
							} else {
								if(mPref.getInt("iTrainStatus", 0) == 0) {
									viewHolder.btn_train_up.setText(trainHead[i] + "\n" + trainType[i] + trainNo[i]);
								} else {
									viewHolder.btn_train_up.setText(trainHead[i] + "\n" + trainType[i] + trainNo[i] + "\n" + trainStatus[i]);
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
						} else if (status[i] == 1) {
							viewHolder.btn_train_down.setVisibility(View.VISIBLE);
							viewHolder.img_train_down_norm.setVisibility(View.VISIBLE);
							viewHolder.icon_train_down.setImageResource(tm.getTrainIcon(iTheme, 1, false));

							if(!bool_type) {
								if(mPref.getInt("iTrainStatus", 0) == 0) {
									viewHolder.btn_train_down.setText(trainHead[i] + "\n" + trainNo[i]);
								} else {
									viewHolder.btn_train_down.setText(trainHead[i] + "\n" + trainNo[i] + "\n" + trainStatus[i]);
								}
							} else {
								if(mPref.getInt("iTrainStatus", 0) == 0) {
									viewHolder.btn_train_down.setText(trainHead[i] + "\n" + trainType[i] + trainNo[i]);
								} else {
									viewHolder.btn_train_down.setText(trainHead[i] + "\n" + trainType[i] + trainNo[i] + "\n" + trainStatus[i]);
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
						}
					}
				}
			} else {
				for (int i = 0; i < MAX_TRAIN; i++) {
					if (station[i] == arC.get(position).iNum) {
						if (status[i] == 0) {
							viewHolder.btn_train_up.setVisibility(View.INVISIBLE);
							viewHolder.img_train_up_norm.setVisibility(View.VISIBLE);
							viewHolder.icon_train_up.setImageResource(tm.getTrainIcon(iTheme, 0, false));

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
						} else if (status[i] == 1) {
							viewHolder.btn_train_down.setVisibility(View.INVISIBLE);
							viewHolder.img_train_down_norm.setVisibility(View.VISIBLE);
							viewHolder.icon_train_down.setImageResource(tm.getTrainIcon(iTheme, 1, false));

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
						}
					}
				}
			}

			viewHolder.btn_train_up.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					CalcDate cd = new CalcDate();
					StringBuffer sb = new StringBuffer();
					sb.append("https://rail.blue/railroad/logis/Default.aspx?date=");
					sb.append(cd.getDate());
					sb.append("&train=");
					sb.append(viewHolder.btn_train_up.getText().toString().split("\n")[1]);

					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sb.toString()));
					startActivity(intent);
				}
			});

			viewHolder.btn_train_down.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					CalcDate cd = new CalcDate();
					StringBuffer sb = new StringBuffer();
					sb.append("https://rail.blue/railroad/logis/Default.aspx?date=");
					sb.append(cd.getDate());
					sb.append("&train=");
					sb.append(viewHolder.btn_train_down.getText().toString().split("\n")[1]);

					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sb.toString()));
					startActivity(intent);
				}
			});

			convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(getApplicationContext(), StationActivity.class);
					intent.putExtra("str_station", arC.get(position).strStation);
					intent.putExtra("str_num", String.valueOf(arC.get(position).iNum));
					startActivity(intent);
				}
			});

			// 리스트 아이템 길게 터치
			convertView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					// 토스트 메세지로 strList 출력
					SetFavor(position, arC.get(position).strStation);
					return true;
				}
			});

			return convertView;
		}
	}

	public void SetFavor(int position, String station) {
		String[] arr = {"즐겨찾기 설정/해제", "모아보기 추가", "메인 화면에 추가"};
		final int index = position;
		final String str_station = station;

		AlertDialog.Builder alert = new AlertDialog.Builder(Line3Activity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
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
						lineManager.setFavorite(str_station, LineManager.LINE_3, index);
						Toast.makeText(getApplicationContext(), arCustomList.get(index).strStation + "역을 모아보기에 추가하였습니다.", Toast.LENGTH_SHORT).show();
						break;

					case 2:
						DBManager dm = new DBManager(getApplicationContext());
						dm.setStation("3호선", str_station + ":" + index);
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
		myAsyncTask.execute("3호선");
	}

	public void ArrayReset() {
		for(int i = 0; i < MAX_TRAIN; i++) {
			station[i] = 0;
			status[i] = 0;
			trainNo[i] = null;
			trainHead[i] = null;
			trainStatus[i] = null;
			trainType[i] = "";
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
		mHandler.removeMessages(0);
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