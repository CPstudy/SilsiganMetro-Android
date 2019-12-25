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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ganada.silsiganmetro.util.CalcDate;
import com.ganada.silsiganmetro.util.LineManager;
import com.ganada.silsiganmetro.util.DBManager;
import com.ganada.silsiganmetro.listitem.ListExpress;
import com.ganada.silsiganmetro.R;
import com.ganada.silsiganmetro.real.Realtime;
import com.ganada.silsiganmetro.MetroApplication;
import com.ganada.silsiganmetro.view.CustomTitlebar;
import com.ganada.silsiganmetro.view.RefreshButton;
import com.ganada.silsiganmetro.view.StrokeTextView;
import com.ganada.silsiganmetro.util.ThemeManager;


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


public class GonghangActivity extends Activity {

	private SharedPreferences mPref;
	private SharedPreferences.Editor mPrefEdit;

	private MyAsyncTask myAsyncTask;

	LineManager lineManager;
	ThemeManager tm;
	MetroApplication sv;
	ArrayList<ListExpress> arCustomList;
	ListExpressAdapter adapter;
	Animation animSpin;
	CustomTitlebar layTitle;
	RelativeLayout layout_title;
	RefreshButton btnRefresh;
	TextView btnInfo;
	ImageButton btnBack;

	ListView list;
	View footer;
	boolean boolInfo = false;

	int iTheme = 0;
	int iTime = 5;
	int iSec;
	int iPosition;
	int i_favorite;
	boolean bool_pos;
	String receivedTime = "--";
	String POS_LINE = "pos_gonghang";
	String BOOL_LINE = "bool_gonghang";
	String xml;
	int MAX_TRAIN = 40;
	int[] station = new int[MAX_TRAIN];
	int[] status = new int[MAX_TRAIN];
	int[] trainExpress = new int[MAX_TRAIN];
	String[] trainNo = new String[MAX_TRAIN];
	String[] trainHead = new String[MAX_TRAIN];
	String[] trainStatus = new String[MAX_TRAIN];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_line8);

		arCustomList = new ArrayList<>();
		ListExpress listexpress;
		layTitle = findViewById(R.id.layTitle);
		btnInfo = findViewById(R.id.btnInfo);
		btnRefresh = findViewById(R.id.btnRefresh);
		footer = getLayoutInflater().inflate(R.layout.item_footer, null, false);
		mPref = getSharedPreferences("Pref1", 0);
		mPrefEdit = mPref.edit();
		tm = new ThemeManager(getBaseContext());
		lineManager = new LineManager(getBaseContext());
		iTheme = mPref.getInt("iTheme", 0);

		Window window = getWindow();
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), tm.getTitleBarColor(iTheme, LineManager.LINE_GONGHANG)));
		}
		layTitle.setText("공항철도");
		layTitle.setBackgroundColorById(tm.getTitleBarColor(iTheme, LineManager.LINE_GONGHANG));


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

		급행 미정차: 0
		급행 정차: 1

		 */
		listexpress = new ListExpress("인천공항2터미널", 1065006511, 2, 0);
		arCustomList.add(listexpress);
		listexpress = new ListExpress("인천공항1터미널", 1065006510, 1, 1);
		arCustomList.add(listexpress);
		listexpress = new ListExpress("공항화물청사", 1065006509, 0, 0);
		arCustomList.add(listexpress);
		listexpress = new ListExpress("운서", 1065006508, 0, 0);
		arCustomList.add(listexpress);
		listexpress = new ListExpress("영종", 1065065072, 0, 0);
		arCustomList.add(listexpress);
		listexpress = new ListExpress("청라국제도시", 1065065071, 0, 0);
		arCustomList.add(listexpress);
		listexpress = new ListExpress("검암", 1065006507, 1, 0);
		arCustomList.add(listexpress);
		listexpress = new ListExpress("계양", 1065006506, 1, 0);
		arCustomList.add(listexpress);
		listexpress = new ListExpress("김포공항", 1065006505, 1, 0);
		arCustomList.add(listexpress);
		listexpress = new ListExpress("디지털미디어시티", 1065006504, 1, 0);
		arCustomList.add(listexpress);
		listexpress = new ListExpress("홍대입구", 1065006503, 1, 0);
		arCustomList.add(listexpress);
		listexpress = new ListExpress("공덕", 1065006502, 1, 0);
		arCustomList.add(listexpress);
		listexpress = new ListExpress("서울", 1065006501, 3, 0);
		arCustomList.add(listexpress);

		adapter = new ListExpressAdapter(this, tm.getListTheme(iTheme, LineManager.LINE_GONGHANG, mPref.getInt("iLineStatus", 0)), arCustomList);

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
		public RelativeLayout layout_line;
		public RelativeLayout layout_station;
		public RelativeLayout layout_train;
		public TextView train_up_norm;
		public TextView train_up_express;
		public TextView train_down_norm;
		public TextView train_down_express;
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

	class ListExpressAdapter extends BaseAdapter {
		Context con;
		LayoutInflater inflacter;
		ArrayList<ListExpress> arC;
		int layout;
		RelativeLayout.LayoutParams pm;

		public ListExpressAdapter(Context context, int alayout, ArrayList<ListExpress> aarC) {
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

			viewHolder.txt_station_sub.setVisibility(View.GONE);
			viewHolder.txt_station.setText(arC.get(position).strStation);
			viewHolder.txt_station.setTextColor(ContextCompat.getColor(getApplicationContext(), tm.getStationColor(iTheme)));
			viewHolder.txt_station.setTextSize(TypedValue.COMPLEX_UNIT_SP, tm.getStationFontSize());
			viewHolder.layout_back.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), tm.getListBackgroundColor(iTheme)));
			viewHolder.view_line1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_gh));
			viewHolder.view_line2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_gh));
			viewHolder.view_line3.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_gh));
			viewHolder.view_line4.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_gh));
			viewHolder.view_line5.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_gh));
			viewHolder.view_line6.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_gh));
			viewHolder.view_line7.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_gh));
			viewHolder.view_line8.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_gh));

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

			viewHolder.img_train_up_norm.setVisibility(View.INVISIBLE);
			viewHolder.img_train_up_express.setVisibility(View.INVISIBLE);
			viewHolder.img_train_down_norm.setVisibility(View.INVISIBLE);
			viewHolder.img_train_down_express.setVisibility(View.INVISIBLE);
			viewHolder.layout_train.setVisibility(View.VISIBLE);
			if(iTheme == 0) {
				viewHolder.view_divider.setVisibility(View.VISIBLE);
			} else {
				viewHolder.view_divider.setVisibility(View.GONE);
			}

			if(arC.get(position).iExpress == 0) {
				viewHolder.img_icon_express.setVisibility(View.GONE);
				viewHolder.img_icon_express1.setVisibility(View.GONE);
			} else if(arC.get(position).iExpress == 1) {
				viewHolder.img_icon_express.setVisibility(View.GONE);
				viewHolder.img_icon_express1.setVisibility(View.GONE);
				viewHolder.img_icon_express.setImageResource(R.drawable.img_express);
			} else if(arC.get(position).iExpress == 2) {
				viewHolder.img_icon_express.setVisibility(View.GONE);
				viewHolder.img_icon_express1.setVisibility(View.GONE);
				viewHolder.img_icon_express.setImageResource(R.drawable.img_express2);
			} else if(arC.get(position).iExpress == 3) {
				viewHolder.img_icon_express.setVisibility(View.GONE);
				viewHolder.img_icon_express1.setVisibility(View.GONE);
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
				viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1065_norm);
				viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1065_norm);
				if(arC.get(position).iExpress == 0) {
					viewHolder.icon_station_down_express.setVisibility(View.INVISIBLE);
					viewHolder.icon_station_up_express.setVisibility(View.INVISIBLE);
				} else {
					viewHolder.icon_station_down_express.setVisibility(View.VISIBLE);
					viewHolder.icon_station_up_express.setVisibility(View.VISIBLE);
					viewHolder.icon_station_down_express.setImageResource(R.drawable.icon_1065_norm);
					viewHolder.icon_station_up_express.setImageResource(R.drawable.icon_1065_norm);
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
				viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1065_tran);
				viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1065_tran);
				if(arC.get(position).iExpress == 0) {
					viewHolder.icon_station_down_express.setVisibility(View.INVISIBLE);
					viewHolder.icon_station_up_express.setVisibility(View.INVISIBLE);
				} else {
					viewHolder.icon_station_down_express.setVisibility(View.VISIBLE);
					viewHolder.icon_station_up_express.setVisibility(View.VISIBLE);
					viewHolder.icon_station_down_express.setImageResource(R.drawable.icon_1065_tran);
					viewHolder.icon_station_up_express.setImageResource(R.drawable.icon_1065_tran);
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
				viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1065_tran);
				viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1065_tran);
				viewHolder.icon_station_down_express.setVisibility(View.VISIBLE);
				viewHolder.icon_station_up_express.setVisibility(View.VISIBLE);
				viewHolder.icon_station_down_express.setImageResource(R.drawable.icon_1065_tran);
				viewHolder.icon_station_up_express.setImageResource(R.drawable.icon_1065_tran);
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
				viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1065_tran);
				viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1065_tran);
				viewHolder.icon_station_down_express.setVisibility(View.VISIBLE);
				viewHolder.icon_station_up_express.setVisibility(View.VISIBLE);
				viewHolder.icon_station_down_express.setImageResource(R.drawable.icon_1065_tran);
				viewHolder.icon_station_up_express.setImageResource(R.drawable.icon_1065_tran);
			}

			if(boolInfo) {
				for (int i = 0; i < MAX_TRAIN; i++) {
					if (station[i] == arC.get(position).iNum) {
						if (status[i] == 0) {
							if (trainExpress[i] == 0) {
								viewHolder.train_up_norm.setVisibility(View.VISIBLE);
								viewHolder.img_train_up_norm.setVisibility(View.VISIBLE);
								viewHolder.icon_train_up_norm.setImageResource(tm.getTrainIcon(iTheme, 0, false));

								if(mPref.getInt("iTrainStatus", 0) == 0) {
									viewHolder.train_up_norm.setText(trainHead[i] + "\n" + trainNo[i]);
								} else {
									viewHolder.train_up_norm.setText(trainHead[i] + "\n" + trainNo[i] + "\n" + trainStatus[i]);
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

								if(mPref.getInt("iTrainStatus", 0) == 0) {
									viewHolder.train_up_express.setText(trainHead[i] + "\n" + trainNo[i]);
								} else {
									viewHolder.train_up_express.setText(trainHead[i] + "\n" + trainNo[i] + "\n" + trainStatus[i]);
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

								if(mPref.getInt("iTrainStatus", 0) == 0) {
									viewHolder.train_down_norm.setText(trainHead[i] + "\n" + trainNo[i]);
								} else {
									viewHolder.train_down_norm.setText(trainHead[i] + "\n" + trainNo[i] + "\n" + trainStatus[i]);
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

								if(mPref.getInt("iTrainStatus", 0) == 0) {
									viewHolder.train_down_express.setText(trainHead[i] + "\n" + trainNo[i]);
								} else {
									viewHolder.train_down_express.setText(trainHead[i] + "\n" + trainNo[i] + "\n" + trainStatus[i]);
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

			viewHolder.train_up_norm.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					CalcDate cd = new CalcDate();
					StringBuffer sb = new StringBuffer();
					sb.append("https://rail.blue/railroad/logis/Default.aspx?date=");
					sb.append(cd.getDate());
					sb.append("&train=");
					sb.append("A");
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
					sb.append("A");
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
					sb.append("A");
					sb.append(viewHolder.train_up_express.getText().toString().split("\n")[1].replace("[직] ", ""));

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
					sb.append("A");
					sb.append(viewHolder.train_down_express.getText().toString().split("\n")[1].replace("[직] ", ""));

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
			Realtime rt = new Realtime();
			String result = rt.getLine(sum);
			String[][] parsedData = jsonParserList(result);
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

	public void SetFavor(int position, String station) {
		String[] arr = {"즐겨찾기 설정/해제", "모아보기 추가", "메인 화면에 추가"};
		final int index = position;
		final String str_station = station;

		AlertDialog.Builder alert = new AlertDialog.Builder(GonghangActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
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
						lineManager.setFavorite(str_station, LineManager.LINE_GONGHANG, index);
						Toast.makeText(getApplicationContext(), arCustomList.get(index).strStation + "역을 모아보기에 추가하였습니다.", Toast.LENGTH_SHORT).show();
						break;

					case 2:
						DBManager dm = new DBManager(getApplicationContext());
						dm.setStation("공항철도", str_station + ":" + index);
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

			ArrayReset();

			for(int i=0; i<parseredData.length; i++){
				Log.i("분석 데이터: " + i + " : ", parseredData[i][0]);
				station[i] = Integer.parseInt(parseredData[i][0]);
				status[i] = Integer.parseInt(parseredData[i][1]);
				trainNo[i] = parseredData[i][2].replace("A", "");
				trainHead[i] = tm.changeWord(lineManager.getTrainDst(parseredData[i][3]));
				trainStatus[i] = parseredData[i][4];
				if(trainStatus[i].equals("0")) {
					trainStatus[i] = "진입";
				} else if(trainStatus[i].equals("1")) {
					trainStatus[i] = "도착";
				} else {
					trainStatus[i] = "출발";
				}
				trainExpress[i] = Integer.parseInt(parseredData[i][5]);
			}

			return parseredData;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void AppStart() {
		myAsyncTask = new MyAsyncTask();
		myAsyncTask.execute("공항철도");
	}

	public void ArrayReset() {
		for(int i = 0; i < MAX_TRAIN; i++) {
			station[i] = 0;
			status[i] = 0;
			trainNo[i] = null;
			trainHead[i] = null;
			trainStatus[i] = null;
			trainExpress[i] = 0;
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
