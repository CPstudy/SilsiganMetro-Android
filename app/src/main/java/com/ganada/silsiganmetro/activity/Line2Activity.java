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
import com.ganada.silsiganmetro.util.DialogManager;
import com.ganada.silsiganmetro.util.LineManager;
import com.ganada.silsiganmetro.util.DBManager;
import com.ganada.silsiganmetro.real.GetType;
import com.ganada.silsiganmetro.listitem.ListNorm;
import com.ganada.silsiganmetro.R;
import com.ganada.silsiganmetro.real.Realtime;
import com.ganada.silsiganmetro.MetroApplication;
import com.ganada.silsiganmetro.util.ThemeManager;
import com.ganada.silsiganmetro.view.CustomTitlebar;
import com.ganada.silsiganmetro.view.RefreshButton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class Line2Activity extends Activity {

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
	RelativeLayout layout_title;
	RefreshButton btnRefresh;
	TextView btnInfo;
	ImageButton btnBack;
	Button btnPrimary;
	View footer;

	HashMap<String, String> mapPrimary = new HashMap<>();

	boolean boolInfo = false;

	int iTime = 5;
	int iSec;
	int iPosition;
	int iTheme = 0;
	int i_favorite;
	boolean bool_pos;
	boolean bool_loaded;
	boolean isPrimary = false;
	String receivedTime = "--";
	String POS_LINE = "pos_line2";
	String BOOL_LINE = "bool_line2";
	String xml;
	int MAX_TRAIN = 120;
	int MAX_XML = 35;
	int[] station = new int[MAX_TRAIN];
	int[] status = new int[MAX_TRAIN];
	String[] trainNo = new String[MAX_TRAIN];
	String[] trainHead = new String[MAX_TRAIN];
	String[] trainStatus = new String[MAX_TRAIN];
	String[] trainPrimary = new String[MAX_TRAIN];
	String[] xmlNumber = new String[MAX_XML];
	String[] xmlDst = new String[MAX_XML];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_line8);

		arCustomList = new ArrayList<>();
		ListNorm listnorm;
		layTitle = findViewById(R.id.layTitle);
		btnRefresh = findViewById(R.id.btnRefresh);
		btnPrimary = findViewById(R.id.btnPrimary);
		footer = getLayoutInflater().inflate(R.layout.item_footer, null, false);

		mPref = getSharedPreferences("Pref1", 0);
		mPrefEdit = mPref.edit();
		tm = new ThemeManager(getBaseContext());
		iTheme = mPref.getInt("iTheme", 0);
		Window window = getWindow();
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), tm.getTitleBarColor(iTheme, LineManager.LINE_2)));
		}
		layTitle.setText("2호선");
		layTitle.setBackgroundColorById(tm.getTitleBarColor(iTheme, LineManager.LINE_2));

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
		listnorm = new ListNorm("성수", 1002000211, 2);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("건대입구", 1002000212, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("구의", 1002000213, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("강변", 1002000214, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("잠실나루", 1002000215, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("잠실", 1002000216, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("잠실새내", 1002000217, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("종합운동장", 1002000218, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("삼성", 1002000219, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("선릉", 1002000220, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("역삼", 1002000221, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("강남", 1002000222, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("교대", 1002000223, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("서초", 1002000224, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("방배", 1002000225, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("사당", 1002000226, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("낙성대", 1002000227, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("서울대입구", 1002000228, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("봉천", 1002000229, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("신림", 1002000230, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("신대방", 1002000231, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("구로디지털단지", 1002000232, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("대림", 1002000233, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("신도림", 1002000234, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("문래", 1002000235, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("영등포구청", 1002000236, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("당산", 1002000237, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("합정", 1002000238, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("홍대입구", 1002000239, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("신촌", 1002000240, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("이대", 1002000241, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("아현", 1002000242, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("충정로", 1002000243, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("시청", 1002000201, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("을지로입구", 1002000202, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("을지로3가", 1002000203, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("을지로4가", 1002000204, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("동대문역사문화공원", 1002000205, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("신당", 1002000206, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("상왕십리", 1002000207, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("왕십리", 1002000208, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("한양대", 1002000209, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("뚝섬", 1002000210, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("성수", 1002000211, 3);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("성수지선", 1000000000, 9);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("성수", 1002009211, 2);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("용답", 1002002111, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("신답", 1002002112, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("용두", 1002002113, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("신설동", 1002002114, 3);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("신정지선", 1000000000, 9);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("신도림", 1002009234, 2);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("도림천", 1002002341, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("양천구청", 1002002342, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("신정네거리", 1002002343, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("까치산", 1002002344, 3);
		arCustomList.add(listnorm);

		/*if(mPref.getInt("iLineStatus", 0) == 0) {
			// 왼쪽
			adapter = new ListNormAdapter(this, R.layout.item_single_left, datas);
		} else {
			// 오른쪽
			adapter = new ListNormAdapter(this, R.layout.item_single_right, datas);
		}*/
		adapter = new ListNormAdapter(this, tm.getListTheme(iTheme, LineManager.LINE_2, mPref.getInt("iLineStatus", 0)), arCustomList);

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

		btnPrimary.setVisibility(View.VISIBLE);
		btnPrimary.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isPrimary) {
					isPrimary = false;
					btnPrimary.setText("열번");
				} else {
					isPrimary = true;
					btnPrimary.setText("편성");
				}

				adapter.notifyDataSetChanged();
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
			long startTime, endTime;
			startTime = System.nanoTime();

			final PersonViewHolder viewHolder;

			if (convertView == null) {
				convertView = inflacter.inflate(layout, parent, false);

				viewHolder = new PersonViewHolder();
				viewHolder.layout_back = convertView.findViewById(R.id.layout_back);
				viewHolder.layout_line = convertView.findViewById(R.id.layout_line);
				viewHolder.layout_station = convertView.findViewById(R.id.layout_station);
				viewHolder.layout_train = convertView.findViewById(R.id.layout_train);
				viewHolder.btn_train_up = convertView.findViewById(R.id.train_up_01);
				viewHolder.btn_train_down = convertView.findViewById(R.id.train_down_01);
				viewHolder.img_icon_express = convertView.findViewById(R.id.img_icon_express);
				viewHolder.img_train_down_norm = convertView.findViewById(R.id.img_train_down_norm);
				viewHolder.img_train_up_norm = convertView.findViewById(R.id.img_train_up_norm);
				viewHolder.view_line1 = convertView.findViewById(R.id.view_line1);
				viewHolder.view_line2 = convertView.findViewById(R.id.view_line2);
				viewHolder.view_line3 = convertView.findViewById(R.id.view_line3);
				viewHolder.view_line4 = convertView.findViewById(R.id.view_line4);
				viewHolder.view_margin = convertView.findViewById(R.id.view_margin);
				viewHolder.view_divider = convertView.findViewById(R.id.view_divider);
				viewHolder.txt_station = convertView.findViewById(R.id.txt_station);
				viewHolder.txt_station_sub = convertView.findViewById(R.id.txt_station_sub);
				viewHolder.icon_station_down_norm = convertView.findViewById(R.id.icon_station_down_norm);
				viewHolder.icon_station_up_norm = convertView.findViewById(R.id.icon_station_up_norm);
                viewHolder.icon_train_up = convertView.findViewById(R.id.imgUp);
                viewHolder.icon_train_down = convertView.findViewById(R.id.img_icon_down);

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
			viewHolder.view_line1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_2));
			viewHolder.view_line2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_2));
			viewHolder.view_line3.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_2));
			viewHolder.view_line4.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_2));

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
					viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1002_norm);
					viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1002_norm);
				} else if (arC.get(position).iStainfo == 1) {
					// 환승
					viewHolder.view_line1.setVisibility(View.VISIBLE);
					viewHolder.view_line2.setVisibility(View.VISIBLE);
					viewHolder.view_line3.setVisibility(View.VISIBLE);
					viewHolder.view_line4.setVisibility(View.VISIBLE);
					viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1002_tran);
					viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1002_tran);
				} else if (arC.get(position).iStainfo == 2) {
					// 맨 위
					viewHolder.view_line1.setVisibility(View.INVISIBLE);
					viewHolder.view_line2.setVisibility(View.VISIBLE);
					viewHolder.view_line3.setVisibility(View.INVISIBLE);
					viewHolder.view_line4.setVisibility(View.VISIBLE);
					viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1002_tran);
					viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1002_tran);
				} else if (arC.get(position).iStainfo == 3) {
					// 맨 아래
					viewHolder.view_line1.setVisibility(View.VISIBLE);
					viewHolder.view_line2.setVisibility(View.INVISIBLE);
					viewHolder.view_line3.setVisibility(View.VISIBLE);
					viewHolder.view_line4.setVisibility(View.INVISIBLE);
					viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1002_tran);
					viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1002_tran);
				}

				if (boolInfo) {
					for (int i = 0; i < MAX_TRAIN; i++) {
						final int index = i;
						StringBuffer sb = new StringBuffer();
						sb.append(trainHead[i]);
						if(!isPrimary) {
							sb.append("\n");
							sb.append(trainNo[i]);
						} else {
							sb.append("\n");
							if(trainPrimary[i] != null) {
								sb.append(trainPrimary[i]);
							} else {
								sb.append("--");
							}
						}
						if (mPref.getInt("iTrainStatus", 0) == 1) {
							sb.append("\n");
							sb.append(trainStatus[i]);
						}
						if (station[i] >= 1002002111 && station[i] <= 1002002114) {
							if (arCustomList.get(position).iNum == station[i]) {
								if (status[i] == 1) {
									pm = (RelativeLayout.LayoutParams)viewHolder.img_train_down_norm.getLayoutParams();
									pm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
									pm.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
									pm.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

									viewHolder.btn_train_down.setVisibility(View.VISIBLE);
									viewHolder.img_train_down_norm.setVisibility(View.VISIBLE);
									viewHolder.btn_train_down.setText(sb.toString());
									viewHolder.icon_train_down.setImageResource(tm.getTrainIcon(iTheme, 1, false));

									viewHolder.btn_train_down.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											startLink(trainNo[index]);
										}
									});

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
								} else if (status[i] == 0) {
									pm = (RelativeLayout.LayoutParams)viewHolder.img_train_up_norm.getLayoutParams();
									pm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
									pm.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
									pm.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

									viewHolder.btn_train_up.setVisibility(View.VISIBLE);
									viewHolder.img_train_up_norm.setVisibility(View.VISIBLE);
									viewHolder.btn_train_up.setText(sb.toString());
									viewHolder.icon_train_up.setImageResource(tm.getTrainIcon(iTheme, 0, false));

									viewHolder.btn_train_up.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											startLink(trainNo[index]);
										}
									});

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
								}
							}
						} else {
							if (arCustomList.get(position).iNum == station[i]) {
								if (status[i] == 1) {
									pm = (RelativeLayout.LayoutParams)viewHolder.img_train_up_norm.getLayoutParams();
									pm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
									pm.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
									pm.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

									viewHolder.btn_train_up.setVisibility(View.VISIBLE);
									viewHolder.img_train_up_norm.setVisibility(View.VISIBLE);
									viewHolder.btn_train_up.setText(sb.toString());
									viewHolder.icon_train_up.setImageResource(tm.getTrainIcon(iTheme, 0, false));

									viewHolder.btn_train_up.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											startLink(trainNo[index]);
										}
									});

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
								} else if (status[i] == 0) {
									pm = (RelativeLayout.LayoutParams)viewHolder.img_train_down_norm.getLayoutParams();
									pm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
									pm.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
									pm.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

									viewHolder.btn_train_down.setVisibility(View.VISIBLE);
									viewHolder.img_train_down_norm.setVisibility(View.VISIBLE);
									viewHolder.btn_train_down.setText(sb.toString());
									viewHolder.icon_train_down.setImageResource(tm.getTrainIcon(iTheme, 1, false));

									viewHolder.btn_train_down.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											startLink(trainNo[index]);
										}
									});

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
				} else {
					for (int i = 0; i < MAX_TRAIN; i++) {
						if (station[i] >= 1002002111 && station[i] <= 1002002114) {
							if (arCustomList.get(position).iNum == station[i]) {
								if (status[i] == 1) {
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
								} else if (status[i] == 0) {
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
								}
							}
						} else {
							if (arCustomList.get(position).iNum == station[i]) {
								if (status[i] == 1) {
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
								} else if (status[i] == 0) {
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
			}



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

			endTime = System.nanoTime();
			Log.e("System Time", "end: " + (endTime - startTime));

			return convertView;
		}
	}

	public void startLink(String number) {
		CalcDate cd = new CalcDate();
		StringBuffer sb = new StringBuffer();
		sb.append("https://rail.blue/railroad/logis/Default.aspx?date=");
		sb.append(cd.getDate());
		sb.append("&train=");
		sb.append("S");
		sb.append(number);

		DialogManager dialog = new DialogManager(Line2Activity.this);
		dialog.alertTrainDialog(sb.toString(), number);
	}

	public void SetFavor(int position, String station) {
		String[] arr = {"리스트 시작", "즐겨찾기 설정/해제", "모아보기 추가", "메인 화면에 추가"};
		final int index = position;
		final String str_station = station;

		AlertDialog.Builder alert = new AlertDialog.Builder(Line2Activity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
		alert.setTitle(String.format(Locale.KOREA, "%s역 선택", station));
		alert.setItems(arr, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// The 'which' argument contains the index position
				// of the selected item
				switch(which) {
					case 1:
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

					case 2:
						LineManager lineManager = new LineManager(getApplicationContext());
						lineManager.setFavorite(str_station, LineManager.LINE_2, index);
						Toast.makeText(getApplicationContext(), arCustomList.get(index).strStation + "역을 모아보기에 추가하였습니다.", Toast.LENGTH_SHORT).show();
						break;

					case 3:
						DBManager dm = new DBManager(getApplicationContext());
						dm.setStation("2호선", str_station + ":" + index);
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
			btnRefresh.startAnimation();
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
				CalcDate cd = new CalcDate();
				GetType gt = new GetType();
				String result_type = gt.getLine2(cd.getDayWeek());
				System.out.println(result_type);
				jsonTypeList(result_type);
			}

			Realtime rt = new Realtime();
			mapPrimary = rt.getPrimaryList("1002");
			String result = rt.getLine(sum);
			jsonParserList(result);


			return sum;
		}

		// 종료
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			btnRefresh.stopAnimation();
			btnRefresh.setTime(receivedTime);
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

	private String[][] jsonTypeList(String pRecvServerPage) {

		try {
			JSONObject json = new JSONObject(pRecvServerPage);
			JSONArray jArr = json.getJSONArray("train");

			String[] jsonName = {"trainNo", "trainDst"};
			String[][] parseredData = new String[jArr.length()][jsonName.length];
			for (int i = 0; i < jArr.length(); i++) {
				json = jArr.getJSONObject(i);

				for(int j = 0; j < jsonName.length; j++) {
					parseredData[i][j] = json.getString(jsonName[j]);
				}
			}

			for(int i = 0; i < parseredData.length; i++){
				Log.i("분석 데이터: " + i + " : ", parseredData[i][0]);
				xmlNumber[i] = parseredData[i][0];
				xmlDst[i] = parseredData[i][1];
			}
			bool_loaded = true;

			return parseredData;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String[][] jsonParserList(String pRecvServerPage) {

		try {
			JSONObject json = new JSONObject(pRecvServerPage);
			receivedTime = json.getString("time");
			json = json.getJSONObject("result");
			JSONArray jArr = json.getJSONArray("realtimePositionList");

			String[] jsonName = {"statnId", "updnLine", "trainNo", "statnTnm", "trainSttus", "directAt"};
			String[][] parseredData = new String[jArr.length()][jsonName.length];
			for (int i = 0; i < jArr.length(); i++) {
				json = jArr.getJSONObject(i);

				for(int j = 0; j < jsonName.length; j++) {
					parseredData[i][j] = json.getString(jsonName[j]);
				}
			}

			Log.e("parseredData.length", "" + parseredData.length);

			ArrayReset();
			for(int i = 0; i < parseredData.length; i++){
				String str_num;
				int i_num = 0;
				Log.i("분석 데이터: " + i + " : ", parseredData[i][0]);
				station[i] = Integer.parseInt(parseredData[i][0]);
				status[i] = Integer.parseInt(parseredData[i][1]);
				str_num = parseredData[i][2];
				i_num = Integer.parseInt(str_num);
				try {
					if ((i_num / 1000) != 2 && (i_num / 1000) != 1 && (i_num / 1000) != 5) {
						trainNo[i] = String.format(Locale.KOREA, "2%03d", i_num % 1000);
					} else {
						trainNo[i] = str_num;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				if(status[i] == 0 && parseredData[i][3].equals("성수종착") || parseredData[i][3].equals("성수")) {
					trainHead[i] = tm.changeWord("내선순환");
				} else if(status[i] == 1 && parseredData[i][3].equals("성수종착") || parseredData[i][3].equals("성수")) {
					trainHead[i] = tm.changeWord("외선순환");
				} else {
					trainHead[i] = tm.changeWord(parseredData[i][3]);
				}

				for(int j = 0; j < MAX_XML; j++) {
					try {
						if (trainNo[i].equals(xmlNumber[j])) {
							trainHead[i] = "성수";
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				if((i_num / 1000) == 1 && station[i] == 1002000211) {
					station[i] = 1002009211;
				} else if ((i_num / 1000) == 5 && station[i] == 1002000234) {
					station[i] = 1002009234;
				}

				trainStatus[i] = parseredData[i][4];

				if(trainStatus[i].equals("0")) {
					trainStatus[i] = "진입";
				} else if(trainStatus[i].equals("1")) {
					trainStatus[i] = "도착";
				} else {
					trainStatus[i] = "출발";
				}

				trainPrimary[i] = mapPrimary.get(trainNo[i]);

				Log.e("primary", trainNo[i] + " ::: " + mapPrimary.get(trainNo[i]));
			}

			return parseredData;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void AppStart() {
		myAsyncTask = new MyAsyncTask();
		myAsyncTask.execute("2호선");
		/*String sMessage = "1071";
		String result = SendByHttp(sMessage);
		String[][] parsedData = jsonParserList(result);*/
	}

	public void ArrayReset() {
		for(int i = 0; i < MAX_TRAIN; i++) {
			station[i] = 0;
			status[i] = 0;
			trainNo[i] = null;
			trainHead[i] = null;
			trainStatus[i] = null;
			trainPrimary[i] = null;
		}
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {

			if(iTime > 0) {
				iTime--;
			} else {
				AppStart();
				btnRefresh.startAnimation();
				iTime = iSec;
			}

			if(iTime == 0 && iSec == 0) {
				AppStart();
				btnRefresh.startAnimation();
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
		btnRefresh.stopAnimation();
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
