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
import android.widget.LinearLayout;
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
import com.ganada.silsiganmetro.util.ThemeManager;
import com.ganada.silsiganmetro.view.CustomTitlebar;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;


public class Line4Activity extends Activity {

	private SharedPreferences mPref;
	private SharedPreferences.Editor mPrefEdit;

	private MyAsyncTask myAsyncTask;

	ThemeManager tm;
	MetroApplication sv;
	ArrayList<ListDouble> arCustomList;
	ListDoubleAdapter adapter;
	CustomTitlebar layTitle;
	ListView list;
	RelativeLayout layout_title;
	Animation animSpin;
	ImageView imgTime;
	Button btnRefresh;
	TextView btnInfo;
	ImageButton btnBack;
	View footer;
	boolean boolInfo = false;
	boolean bool_type = true;
	boolean bool_loaded = false;

	int MAX_XML = 500;
	int MAX_TRAIN = 120;

	int iTheme = 0;
	int iTime = 5;
	int iSec;
	int iPosition;
	int i_favorite;
	boolean bool_pos;
	String POS_LINE = "pos_line4";
	String BOOL_LINE = "bool_line4";
	String xml;
	String today;
	String sDayWeek;
	boolean bool_weekend;
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
	String[] xmlStart = new String[MAX_XML];

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
			window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), tm.getTitleBarColor(iTheme, LineManager.LINE_4)));
		}
		layTitle.setText("4호선");
		layTitle.setBackgroundColorById(tm.getTitleBarColor(iTheme, LineManager.LINE_4));

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

		급행 미정차: 0
		급행 정차: 1

		 */
		listdouble = new ListDouble("당고개", 1004000409, 2, 0, "당고개 - 금정 각 역 정차");
		arCustomList.add(listdouble);
		listdouble = new ListDouble("상계", 1004000410, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("노원", 1004000411, 1, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("창동", 1004000412, 1, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("쌍문", 1004000413, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("수유", 1004000414, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("미아", 1004000415, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("미아사거리", 1004000416, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("길음", 1004000417, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("성신여대입구", 1004000418, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("한성대입구", 1004000419, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("혜화", 1004000420, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("동대문", 1004000421, 1, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("동대문역사문화공원", 1004000422, 1, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("충무로", 1004000423, 1, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("명동", 1004000424, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("회현", 1004000425, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("서울", 1004000426, 1, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("숙대입구", 1004000427, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("삼각지", 1004000428, 1, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("신용산", 1004000429, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("이촌", 1004000430, 1, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("동작", 1004000431, 1, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("총신대입구(이수)", 1004000432, 1, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("사당", 1004000433, 1, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("남태령", 1004000434, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("선바위", 1004000435, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("경마공원", 1004000436, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("대공원", 1004000437, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("과천", 1004000438, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("정부과천청사", 1004000439, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("인덕원", 1004000440, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("평촌", 1004000441, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("범계", 1004000442, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("금정", 1004000443, 1, 1, "당고개 - 금정 각 역 정차");
		arCustomList.add(listdouble);
		listdouble = new ListDouble("산본", 1004000444, 0, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("수리산", 1004000445, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("대야미", 1004000446, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("반월", 1004000447, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("상록수", 1004000448, 0, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("한대앞", 1004000449, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("중앙", 1004000450, 0, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("고잔", 1004000451, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("초지", 1004000452, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("안산", 1004000453, 0, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("신길온천", 1004000454, 0, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("정왕", 1004000455, 0, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("오이도", 1004000456, 3, 1, null);
		arCustomList.add(listdouble);

		adapter = new ListDoubleAdapter(this, tm.getListTheme(iTheme, LineManager.LINE_4, mPref.getInt("iLineStatus", 0)), arCustomList);

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
		public LinearLayout layout_line;
		public RelativeLayout layout_station;
		public RelativeLayout layout_train;
		public TextView btn_train_up;
		public TextView btn_train_down;
		public ImageView img_icon_express;
		public ImageView img_icon_express1;
		public ImageView img_icon_up;
		public ImageView img_icon_down;
		public RelativeLayout img_train_down_norm;
		public RelativeLayout img_train_up_norm;
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
				viewHolder.layout_line = (LinearLayout) convertView.findViewById(R.id.layout_line);
				viewHolder.layout_station = (RelativeLayout) convertView.findViewById(R.id.layout_station);
				viewHolder.layout_train = (RelativeLayout) convertView.findViewById(R.id.layout_train);
				viewHolder.btn_train_up = (TextView) convertView.findViewById(R.id.train_up_01);
				viewHolder.btn_train_down = (TextView) convertView.findViewById(R.id.train_down_01);
				viewHolder.img_icon_express = (ImageView) convertView.findViewById(R.id.img_icon_express);
				viewHolder.img_icon_express1 = (ImageView) convertView.findViewById(R.id.img_icon_express1);
				viewHolder.img_icon_up = (ImageView) convertView.findViewById(R.id.imgUp);
				viewHolder.img_icon_down = (ImageView) convertView.findViewById(R.id.img_icon_down);
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

			viewHolder.txt_station.setText(arC.get(position).strStation);
			viewHolder.txt_station.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getStationColor(iTheme)));
			viewHolder.txt_station_sub.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getSubtextColor(iTheme)));
			viewHolder.txt_station.setTextSize(TypedValue.COMPLEX_UNIT_SP, tm.getStationFontSize());
			viewHolder.layout_back.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), tm.getListBackgroundColor(iTheme)));
			viewHolder.view_line1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_4));
			viewHolder.view_line2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_4));
			viewHolder.view_line3.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_4));
			viewHolder.view_line4.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_4));

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
			viewHolder.view_margin.setVisibility(View.VISIBLE);
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
				viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1004_norm);
				viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1004_norm);
			} else if (arC.get(position).iStainfo == 1) {
				// 환승
				viewHolder.view_line1.setVisibility(View.VISIBLE);
				viewHolder.view_line2.setVisibility(View.VISIBLE);
				viewHolder.view_line3.setVisibility(View.VISIBLE);
				viewHolder.view_line4.setVisibility(View.VISIBLE);
				viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1004_tran);
				viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1004_tran);
			} else if (arC.get(position).iStainfo == 2) {
				// 맨 위
				viewHolder.view_line1.setVisibility(View.INVISIBLE);
				viewHolder.view_line2.setVisibility(View.VISIBLE);
				viewHolder.view_line3.setVisibility(View.INVISIBLE);
				viewHolder.view_line4.setVisibility(View.VISIBLE);
				viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1004_norm);
				viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1004_norm);
			} else if (arC.get(position).iStainfo == 3) {
				// 맨 아래
				viewHolder.view_line1.setVisibility(View.VISIBLE);
				viewHolder.view_line2.setVisibility(View.INVISIBLE);
				viewHolder.view_line3.setVisibility(View.VISIBLE);
				viewHolder.view_line4.setVisibility(View.INVISIBLE);
				viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1004_tran);
				viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1004_tran);
			}

			if(boolInfo) {
				for (int i = 0; i < MAX_TRAIN; i++) {
					if (station[i] == arC.get(position).iNum) {
						if (status[i] == 0) {
							viewHolder.btn_train_up.setVisibility(View.VISIBLE);
							viewHolder.img_train_up_norm.setVisibility(View.VISIBLE);
							if (trainExpress[i] == 0) {
								viewHolder.img_icon_up.setImageResource(tm.getTrainIcon(iTheme, 0, false));
								if(!bool_type) {
									if(mPref.getInt("iTrainStatus", 0) == 0) {
										viewHolder.btn_train_up.setText(trainHead[i] + "\n" + trainNo[i]);
									} else {
										viewHolder.btn_train_up.setText(trainHead[i] + "\n" + trainType[i] + trainNo[i] + "\n" + trainStatus[i]);
									}
								} else {
									if(mPref.getInt("iTrainStatus", 0) == 0) {
										viewHolder.btn_train_up.setText(trainHead[i] + "\n" + trainType[i] + trainNo[i]);
									} else {
										viewHolder.btn_train_up.setText(trainHead[i] + "\n" + trainType[i] + trainNo[i] + "\n" + trainStatus[i]);
									}
								}
							} else if(trainExpress[i] == 1) {
								viewHolder.img_icon_up.setImageResource(tm.getTrainIcon(iTheme, 0, true));
								if(!bool_type) {
									if(mPref.getInt("iTrainStatus", 0) == 0) {
										viewHolder.btn_train_up.setText(trainHead[i] + "\n" + trainNo[i]);
									} else {
										viewHolder.btn_train_up.setText(trainHead[i] + "\n" + trainType[i] + trainNo[i] + "\n" + trainStatus[i]);
									}
								} else {
									if(mPref.getInt("iTrainStatus", 0) == 0) {
										viewHolder.btn_train_up.setText(trainHead[i] + "\nK" + trainNo[i]);
									} else {
										viewHolder.btn_train_up.setText(trainHead[i] + "\nK" + trainNo[i] + "\n" + trainStatus[i]);
									}
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
							if(trainExpress[i] == 0) {
								viewHolder.img_icon_down.setImageResource(tm.getTrainIcon(iTheme, 1, false));
								if(!bool_type) {
									if(mPref.getInt("iTrainStatus", 0) == 0) {
										viewHolder.btn_train_down.setText(trainHead[i] + "\n" + trainNo[i]);
									} else {
										viewHolder.btn_train_down.setText(trainHead[i] + "\n" + trainType[i] + trainNo[i] + "\n" + trainStatus[i]);
									}
								} else {
									if(mPref.getInt("iTrainStatus", 0) == 0) {
										viewHolder.btn_train_down.setText(trainHead[i] + "\n" + trainType[i] + trainNo[i]);
									} else {
										viewHolder.btn_train_down.setText(trainHead[i] + "\n" + trainType[i] + trainNo[i] + "\n" + trainStatus[i]);
									}
								}
							} else if(trainExpress[i] == 1) {
								viewHolder.img_icon_down.setImageResource(tm.getTrainIcon(iTheme, 1, true));
								if(!bool_type) {
									if(mPref.getInt("iTrainStatus", 0) == 0) {
										viewHolder.btn_train_down.setText(trainHead[i] + "\n" + trainNo[i]);
									} else {
										viewHolder.btn_train_down.setText(trainHead[i] + "\n" + trainType[i] + trainNo[i] + "\n" + trainStatus[i]);
									}
								} else {
									if(mPref.getInt("iTrainStatus", 0) == 0) {
										viewHolder.btn_train_down.setText(trainHead[i] + "\nK" + trainNo[i]);
									} else {
										viewHolder.btn_train_down.setText(trainHead[i] + "\nK" + trainNo[i] + "\n" + trainStatus[i]);
									}
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
							if (trainExpress[i] == 0) {
								viewHolder.img_icon_up.setImageResource(tm.getTrainIcon(iTheme, 0, false));
							} else if (trainExpress[i] == 1) {
								viewHolder.img_icon_up.setImageResource(tm.getTrainIcon(iTheme, 0, true));
							}

							pm = (RelativeLayout.LayoutParams) viewHolder.img_train_up_norm.getLayoutParams();
							pm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
							pm.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
							pm.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

							if (trainStatus[i].equals("출발")) {
								//viewHolder.btn_train_up.setLayoutParams(new RelativeLayout.LayoutParams(param_pos));
								pm.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
							} else if (trainStatus[i].equals("진입")) {
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
							if (trainExpress[i] == 0) {
								viewHolder.img_icon_down.setImageResource(tm.getTrainIcon(iTheme, 1, false));
							} else if (trainExpress[i] == 1) {
								viewHolder.img_icon_down.setImageResource(tm.getTrainIcon(iTheme, 1, true));
							}

							pm = (RelativeLayout.LayoutParams) viewHolder.img_train_down_norm.getLayoutParams();
							pm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
							pm.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
							pm.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

							if (trainStatus[i].equals("출발")) {
								//viewHolder.btn_train_up.setLayoutParams(new RelativeLayout.LayoutParams(param_pos));
								pm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
							} else if (trainStatus[i].equals("진입")) {
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

			/*viewHolder.btn_train_up01.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String[] str = new String[4];
					for(int i = 0; i < MAX_TRAIN; i++) {
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

			viewHolder.btn_train_down01.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String[] str = new String[4];
					for(int i = 0; i < MAX_TRAIN; i++) {
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

		AlertDialog.Builder alert = new AlertDialog.Builder(Line4Activity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
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
						lineManager.setFavorite(str_station, LineManager.LINE_4, index);
						Toast.makeText(getApplicationContext(), arCustomList.get(index).strStation + "역을 모아보기에 추가하였습니다.", Toast.LENGTH_SHORT).show();
						break;

					case 2:
						DBManager dm = new DBManager(getApplicationContext());
						dm.setStation("4호선", str_station + ":" + index);
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
				str_type = gt.getJsonFile(4);
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
				Log.i("분석 데이터: " + i + " : ", parseredData[i][0]);
				station[i] = Integer.parseInt(parseredData[i][0]);
				status[i] = Integer.parseInt(parseredData[i][1]);
				trainNo[i] = parseredData[i][2];
				trainHead[i] = tm.changeWord(parseredData[i][3]);
				try {
					if(Integer.parseInt(trainNo[i]) % 2 == 1) {
						if(trainHead[i].equals("오이도") || trainHead[i].equals("안산") || trainHead[i].equals("산본")) {
							status[i] = 1;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
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
				for(int j = 0; j < MAX_XML; j++) {
					if(parseredData[i][2].equals(xmlNumber[j])) {
						trainType[i] = xmlType[j];
						trainStart[i] = xmlStart[j];
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

	public void AppStart() {
		myAsyncTask = new MyAsyncTask();
		myAsyncTask.execute("4호선");
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
		mHandler.removeMessages(0);
		myAsyncTask.cancel(true);
		imgTime.clearAnimation();
	}

	@Override
	public void onStop() {
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
