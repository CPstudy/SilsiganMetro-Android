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
import com.ganada.silsiganmetro.listitem.ListNorm;
import com.ganada.silsiganmetro.R;
import com.ganada.silsiganmetro.real.Realtime;
import com.ganada.silsiganmetro.MetroApplication;
import com.ganada.silsiganmetro.util.ThemeManager;
import com.ganada.silsiganmetro.view.CustomTitlebar;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

public class Line5Activity extends Activity {

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

	int iTheme = 0;
	int iTime = 5;
	int iSec;
	int iPosition;
	int i_favorite;
	boolean bool_pos;
	String POS_LINE = "pos_line5";
	String BOOL_LINE = "bool_line5";
	String xml;
	int MAX_TRAIN = 120;
	int[] station = new int[MAX_TRAIN];
	int[] status = new int[MAX_TRAIN];
	String[] trainNo = new String[MAX_TRAIN];
	String[] trainHead = new String[MAX_TRAIN];
	String[] trainStatus = new String[MAX_TRAIN];

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
			window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), tm.getTitleBarColor(iTheme, LineManager.LINE_5)));
		}
		layTitle.setText("5호선");
		layTitle.setBackgroundColorById(tm.getTitleBarColor(iTheme, LineManager.LINE_5));

		iPosition = mPref.getInt(POS_LINE, 0);
		bool_pos = mPref.getBoolean(BOOL_LINE, false);
		iSec = mPref.getInt("timerefresh", 0) * 5;
		iTime = iSec;

		if(mPref.getInt("boolInfo", 1) == 0) {
			boolInfo = false;
		} else if(mPref.getInt("boolInfo", 1) == 1) {
			boolInfo = true;
		}

		/*
		단위역: 0
		환승역: 1
		기점: 2
		종점: 3

		 */
		listnorm = new ListNorm("본선", 1000000000, 9);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("방화", 1005000510, 2);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("개화산", 1005000511, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("김포공항", 1005000512, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("송정", 1005000513, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("마곡", 1005000514, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("발산", 1005000515, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("우장산", 1005000516, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("화곡", 1005000517, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("까치산", 1005000518, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("신정", 1005000519, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("목동", 1005000520, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("오목교", 1005000521, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("양평", 1005000522, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("영등포구청", 1005000523, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("영등포시장", 1005000524, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("신길", 1005000525, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("여의도", 1005000526, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("여의나루", 1005000527, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("마포", 1005000528, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("공덕", 1005000529, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("애오개", 1005000530, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("충정로", 1005000531, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("서대문", 1005000532, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("광화문", 1005000533, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("종로3가", 1005000534, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("을지로4가", 1005000535, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("동대문역사문화공원", 1005000536, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("청구", 1005000537, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("신금호", 1005000538, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("행당", 1005000539, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("왕십리", 1005000540, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("마장", 1005000541, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("답십리", 1005000542, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("장한평", 1005000543, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("군자", 1005000544, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("아차산", 1005000545, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("광나루", 1005000546, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("천호", 1005000547, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("강동", 1005000548, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("길동", 1005000549, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("굽은다리", 1005000550, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("명일", 1005000551, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("고덕", 1005000552, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("상일동", 1005000553, 3);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("마천지선", 1000000000, 9);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("강동", 1005000548, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("둔촌동", 1005080549, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("올림픽공원", 1005080550, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("방이", 1005080551, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("오금", 1005080552, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("개롱", 1005080553, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("거여", 1005080554, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("마천", 1005080555, 3);
		arCustomList.add(listnorm);

		adapter = new ListNormAdapter(this, tm.getListTheme(iTheme, LineManager.LINE_5, mPref.getInt("iLineStatus", 0)), arCustomList);

		animSpin = AnimationUtils.loadAnimation(this, R.anim.spin_anim);

		AppStart();

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

		btnRefresh.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				AppStart();
			}

		});
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
			viewHolder.view_line1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_5));
			viewHolder.view_line2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_5));
			viewHolder.view_line3.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_5));
			viewHolder.view_line4.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_5));

			if(iPosition == position && bool_pos && sv.getPosition() == -1) {
				viewHolder.layout_back.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.list_train_fa));
				viewHolder.txt_station.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_fa));
			} else if(sv.getPosition() == position) {
				viewHolder.layout_back.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), tm.getHighlightColor(iTheme)));
			}

			if(arC.get(position).iStainfo == 9) {
				viewHolder.txt_station.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.lab_txt_sep));
				viewHolder.layout_back.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), tm.getFooterBackgroundColor(iTheme)));
				viewHolder.txt_station.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getSubtextColor(iTheme)));
				viewHolder.layout_train.setVisibility(View.GONE);
				viewHolder.view_margin.setVisibility(View.GONE);
				viewHolder.view_divider.setVisibility(View.GONE);
				viewHolder.img_icon_express.setVisibility(View.GONE);
			} else {
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
					viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1005_norm);
					viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1005_norm);
				} else if (arC.get(position).iStainfo == 1) {
					// 환승
					viewHolder.view_line1.setVisibility(View.VISIBLE);
					viewHolder.view_line2.setVisibility(View.VISIBLE);
					viewHolder.view_line3.setVisibility(View.VISIBLE);
					viewHolder.view_line4.setVisibility(View.VISIBLE);
					viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1005_tran);
					viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1005_tran);
				} else if (arC.get(position).iStainfo == 2) {
					// 맨 위
					viewHolder.view_line1.setVisibility(View.INVISIBLE);
					viewHolder.view_line2.setVisibility(View.VISIBLE);
					viewHolder.view_line3.setVisibility(View.INVISIBLE);
					viewHolder.view_line4.setVisibility(View.VISIBLE);
					viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1005_norm);
					viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1005_norm);
				} else if (arC.get(position).iStainfo == 3) {
					// 맨 아래
					viewHolder.view_line1.setVisibility(View.VISIBLE);
					viewHolder.view_line2.setVisibility(View.INVISIBLE);
					viewHolder.view_line3.setVisibility(View.VISIBLE);
					viewHolder.view_line4.setVisibility(View.INVISIBLE);
					viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1005_norm);
					viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1005_norm);
				}

				if (boolInfo) {
					for (int i = 0; i < MAX_TRAIN; i++) {
						StringBuffer sb = new StringBuffer();
						sb.append(trainHead[i]);
						sb.append("\n");
						sb.append(trainNo[i]);
						if(mPref.getInt("iTrainStatus", 0) == 1) {
							sb.append("\n");
							sb.append(trainStatus[i]);
						}
						if (station[i] == arC.get(position).iNum) {
							if (status[i] == 0) {
								pm = (RelativeLayout.LayoutParams)viewHolder.img_train_up_norm.getLayoutParams();
								pm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
								pm.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
								pm.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

								viewHolder.btn_train_up.setVisibility(View.VISIBLE);
								viewHolder.img_train_up_norm.setVisibility(View.VISIBLE);
								viewHolder.btn_train_up.setText(sb.toString());
								viewHolder.icon_train_up.setImageResource(tm.getTrainIcon(iTheme, 0, false));

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
								pm = (RelativeLayout.LayoutParams)viewHolder.img_train_down_norm.getLayoutParams();
								pm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
								pm.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
								pm.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

								viewHolder.btn_train_down.setVisibility(View.VISIBLE);
								viewHolder.img_train_down_norm.setVisibility(View.VISIBLE);
								viewHolder.btn_train_down.setText(sb.toString());
								viewHolder.icon_train_down.setImageResource(tm.getTrainIcon(iTheme, 1, false));

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
								pm = (RelativeLayout.LayoutParams)viewHolder.img_train_up_norm.getLayoutParams();
								pm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
								pm.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
								pm.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

								viewHolder.btn_train_up.setVisibility(View.INVISIBLE);
								viewHolder.img_train_up_norm.setVisibility(View.VISIBLE);
								viewHolder.icon_train_up.setImageResource(tm.getTrainIcon(iTheme, 0, false));

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
								pm = (RelativeLayout.LayoutParams)viewHolder.img_train_down_norm.getLayoutParams();
								pm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
								pm.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
								pm.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

								viewHolder.btn_train_down.setVisibility(View.INVISIBLE);
								viewHolder.img_train_down_norm.setVisibility(View.VISIBLE);
								viewHolder.icon_train_down.setImageResource(tm.getTrainIcon(iTheme, 1, false));

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
			}

			viewHolder.btn_train_up.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					CalcDate cd = new CalcDate();
					StringBuffer sb = new StringBuffer();
					sb.append("https://rail.blue/railroad/logis/Default.aspx?date=");
					sb.append(cd.getDate());
					sb.append("&train=");
					sb.append("SMRT");
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
					sb.append("SMRT");
					sb.append(viewHolder.btn_train_down.getText().toString().split("\n")[1]);

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

			// 리스트 아이템 길게 터치
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

			return convertView;
		}
	}

	public void SetFavor(int position, String station) {
		String[] arr = {"즐겨찾기 설정/해제", "모아보기 추가", "메인 화면에 추가"};
		final int index = position;
		final String str_station = station;

		AlertDialog.Builder alert = new AlertDialog.Builder(Line5Activity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
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
						lineManager.setFavorite(str_station, LineManager.LINE_5, index);
						Toast.makeText(getApplicationContext(), arCustomList.get(index).strStation + "역을 모아보기에 추가하였습니다.", Toast.LENGTH_SHORT).show();
						break;

					case 2:
						DBManager dm = new DBManager(getApplicationContext());
						dm.setStation("5호선", str_station + ":" + index);
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

	public class MyAsyncTask extends AsyncTask<String, Void, String> {

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

	private String SendByHttp(String msg) {
		if(msg == null)
			msg = "";

		String URL = "http://m.bus.go.kr/mBus/subway/getLcByRoute.bms";

		DefaultHttpClient client = new DefaultHttpClient();
		try {
			HttpPost post = new HttpPost(URL+"?subwayId="+msg);

			HttpParams params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpResponse response = client.execute(post);
			BufferedReader bufreader = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent(),
							"euc-kr"));

			String line = null;
			String result = "";

			while ((line = bufreader.readLine()) != null) {
				result += line;
			}

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			client.getConnectionManager().shutdown();
			return "";
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
			}

			return parseredData;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void AppStart() {
		myAsyncTask = new MyAsyncTask();
		myAsyncTask.execute("5호선");
	}

	public void ArrayReset() {
		for(int i = 0; i < MAX_TRAIN; i++) {
			station[i] = 0;
			status[i] = 0;
			trainNo[i] = null;
			trainHead[i] = null;
			trainStatus[i] = null;
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
