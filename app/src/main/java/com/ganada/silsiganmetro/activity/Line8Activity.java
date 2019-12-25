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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class Line8Activity extends Activity {

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
	View footer;
	boolean boolInfo = false;

	int iTheme = 0;
	int iTime = 5;
	int iSec;
	int iPosition;
	int i_favorite;
	boolean bool_pos;
	String receivedTime = "--";
	String POS_LINE = "pos_line8";
	String BOOL_LINE = "bool_line8";
	String xml;
	String today;
	String sDayWeek;
	boolean bool_weekend;
	int MAX_XML = 310;
	int MAX_TRAIN = 50;
	int[] station = new int[MAX_TRAIN];
	int[] status = new int[MAX_TRAIN];
	String[] trainNo = new String[MAX_TRAIN];
	String[] trainHead = new String[MAX_TRAIN];
	String[] trainStatus = new String[MAX_TRAIN];
	String[] trainStart = new String[MAX_TRAIN];
	String[] trainNext = new String[MAX_TRAIN];
	String[] xmlNumber = new String[MAX_XML];
	String[] xmlStart = new String[MAX_XML];
	String[] xmlNext = new String[MAX_XML];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_line8);

		arCustomList = new ArrayList<>();
		ListNorm listnorm;
		layTitle = findViewById(R.id.layTitle);
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
			window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), tm.getTitleBarColor(iTheme, LineManager.LINE_8)));
		}
		layTitle.setText("8호선");
		layTitle.setBackgroundColorById(tm.getTitleBarColor(iTheme, LineManager.LINE_8));

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
		listnorm = new ListNorm("암사", 1008000810, 2);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("천호", 1008000811, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("강동구청", 1008000812, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("몽촌토성", 1008000813, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("잠실", 1008000814, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("석촌", 1008000815, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("송파", 1008000816, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("가락시장", 1008000817, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("문정", 1008000818, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("장지", 1008000819, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("복정", 1008000820, 1);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("산성", 1008000821, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("남한산성입구", 1008000822, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("단대오거리", 1008000823, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("신흥", 1008000824, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("수진", 1008000825, 0);
		arCustomList.add(listnorm);
		listnorm = new ListNorm("모란", 1008000826, 3);
		arCustomList.add(listnorm);

		adapter = new ListNormAdapter(this, tm.getListTheme(iTheme, LineManager.LINE_8, mPref.getInt("iLineStatus", 0)), arCustomList);


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

	public void alertDialog(final String strStation, final String strStart, final String strEnd, final String strNext) {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
		dialog.setContentView(R.layout.dialog_traininfo2);
		dialog.setCanceledOnTouchOutside(true);

		// set the custom dialog components - text, image and button
		TextView dialog_txt_trainNo = (TextView) dialog.findViewById(R.id.dialog_txt_trainNo);
		TextView dialog_txt_sta_start = (TextView) dialog.findViewById(R.id.dialog_txt_sta_start);
		TextView dialog_txt_sta_end = (TextView) dialog.findViewById(R.id.dialog_txt_sta_end);
		TextView dialog_txt_next = (TextView) dialog.findViewById(R.id.dialog_txt_next);

		dialog_txt_trainNo.setText(strStation);
		dialog_txt_sta_start.setText(strStart);
		dialog_txt_sta_end.setText(strEnd);
		dialog_txt_next.setText(strNext);

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
			viewHolder.view_line1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_8));
			viewHolder.view_line2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_8));
			viewHolder.view_line3.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_8));
			viewHolder.view_line4.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_8));

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
				viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1008_norm);
				viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1008_norm);
			} else if (arC.get(position).iStainfo == 1) {
				// 환승
				viewHolder.view_line1.setVisibility(View.VISIBLE);
				viewHolder.view_line2.setVisibility(View.VISIBLE);
				viewHolder.view_line3.setVisibility(View.VISIBLE);
				viewHolder.view_line4.setVisibility(View.VISIBLE);
				viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1008_tran);
				viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1008_tran);
			} else if (arC.get(position).iStainfo == 2) {
				// 맨 위
				viewHolder.view_line1.setVisibility(View.INVISIBLE);
				viewHolder.view_line2.setVisibility(View.VISIBLE);
				viewHolder.view_line3.setVisibility(View.INVISIBLE);
				viewHolder.view_line4.setVisibility(View.VISIBLE);
				viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1008_norm);
				viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1008_norm);
			} else if (arC.get(position).iStainfo == 3) {
				// 맨 아래
				viewHolder.view_line1.setVisibility(View.VISIBLE);
				viewHolder.view_line2.setVisibility(View.INVISIBLE);
				viewHolder.view_line3.setVisibility(View.VISIBLE);
				viewHolder.view_line4.setVisibility(View.INVISIBLE);
				viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1008_tran);
				viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1008_tran);
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
							pm = (RelativeLayout.LayoutParams) viewHolder.img_train_up_norm.getLayoutParams();
							pm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
							pm.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
							pm.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

							viewHolder.btn_train_up.setVisibility(View.INVISIBLE);
							viewHolder.img_train_up_norm.setVisibility(View.VISIBLE);
							viewHolder.icon_train_up.setImageResource(tm.getTrainIcon(iTheme, 0, false));

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
							pm = (RelativeLayout.LayoutParams) viewHolder.img_train_down_norm.getLayoutParams();
							pm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
							pm.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
							pm.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

							viewHolder.btn_train_down.setVisibility(View.INVISIBLE);
							viewHolder.img_train_down_norm.setVisibility(View.VISIBLE);
							viewHolder.icon_train_down.setImageResource(tm.getTrainIcon(iTheme, 1, false));

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

			/*viewHolder.btn_train_up.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String[] str = new String[4];
					for(int i = 0; i < MAX_TRAIN; i++) {
						if (station[i] == favorites.get(position).iNum && status[i] == 0) {
							str[0] = trainNo[i];
							str[1] = trainStart[i];
							str[2] = trainHead[i];
							str[3] = trainNext[i];
						}
					}
					alertDialog(str[0], str[1], str[2], str[3]);
				}
			});

			viewHolder.btn_train_down.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String[] str = new String[4];
					for(int i = 0; i < MAX_TRAIN; i++) {
						if (station[i] == favorites.get(position).iNum && status[i] == 1) {
							str[0] = trainNo[i];
							str[1] = trainStart[i];
							str[2] = trainHead[i];
							str[3] = trainNext[i];
						}
					}
					alertDialog(str[0], str[1], str[2], str[3]);
				}
			});*/

			return convertView;
		}
	}

	public void SetFavor(int position, String station) {
		String[] arr = {"즐겨찾기 설정/해제", "모아보기 추가", "메인 화면에 추가"};
		final int index = position;
		final String str_station = station;

		AlertDialog.Builder alert = new AlertDialog.Builder(Line8Activity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
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
						lineManager.setFavorite(str_station, LineManager.LINE_8, index);
						Toast.makeText(getApplicationContext(), arCustomList.get(index).strStation + "역을 모아보기에 추가하였습니다.", Toast.LENGTH_SHORT).show();
						break;

					case 2:
						DBManager dm = new DBManager(getApplicationContext());
						dm.setStation("8호선", str_station + ":" + index);
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

	public void setTime() {
		String sWeek[] = {"일", "월", "화", "수", "목", "금", "토"};
		Calendar calendar;
		calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int week = calendar.get(Calendar.DAY_OF_WEEK);
		sDayWeek = sWeek[week - 1];

		if(sDayWeek.equals("토") || sDayWeek.equals("일")) {
			bool_weekend = true;
		} else {
			bool_weekend = false;
		}

		String sYear = "" + year;
		String sMonth;
		String sDay;

		if (month < 10) {
			sMonth = "0" + month;
		} else {
			sMonth = "" + month;
		}

		if (day < 10) {
			sDay = "0" + day;
		} else {
			sDay = "" + day;
		}

		today = sYear + sMonth + sDay;
	}

	public void parse() {
		String str;
		if(bool_weekend == false) {
			str = "line8_norm.xml";
		} else {
			str = "line8_weekend.xml";
		}
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			// xml을 InputStrem 형태로 변환
			// document와 element 는 w3c dom에 있는것을 임포트 한다.

			XmlPullParser xpp = XmlPullParserFactory.newInstance().newPullParser();
			InputStream is = getAssets().open(str);
			xpp.setInput(new InputStreamReader(is));

			Document doc = documentBuilder.parse(is);
			Element element = doc.getDocumentElement();
			// 읽어올 태그명 정하기
			NodeList items1 = element.getElementsByTagName("trainNumber");
			NodeList items2 = element.getElementsByTagName("trainStart");
			NodeList items3 = element.getElementsByTagName("trainNext");
			// 읽어온 자료의 수
			int n = items1.getLength();
			// 자료를 누적시킬 stringBuffer 객체
			StringBuffer sBuffer = new StringBuffer();
			// 반복문을 돌면서 모든 데이터 읽어오기
			for (int i = 0; i < n; i++) {
				// 읽어온 자료에서 알고 싶은 문자열의 인덱스 번호를 전달한다.
				Node item1 = items1.item(i);
				Node item2 = items2.item(i);
				Node item3 = items3.item(i);
				Node text1 = item1.getFirstChild();
				Node text2 = item2.getFirstChild();
				Node text3 = item3.getFirstChild();
				// 해당 노드에서 문자열 읽어오기
				xmlNumber[i] = text1.getNodeValue();
				xmlStart[i] = text2.getNodeValue();
				xmlNext[i] = text3.getNodeValue();
			}
			// 읽어온 문자열 출력해보기
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("파싱 중 에러 발생", e.getMessage());
			Toast.makeText(getApplicationContext(), "파싱 오류", Toast.LENGTH_SHORT).show();
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
						trainStart[i] = xmlStart[j];
						trainNext[i] = xmlNext[j];
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

	public void AppStart() {
		myAsyncTask = new MyAsyncTask();
		myAsyncTask.execute("8호선");
	}

	public void ArrayReset() {
		for(int i = 0; i < MAX_TRAIN; i++) {
			station[i] = 0;
			status[i] = 0;
			trainNo[i] = null;
			trainHead[i] = null;
			trainStatus[i] = null;
			trainStart[i] = "";
			trainNext[i] = "";
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
