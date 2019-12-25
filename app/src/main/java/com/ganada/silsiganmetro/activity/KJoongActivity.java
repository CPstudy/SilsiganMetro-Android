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
import com.ganada.silsiganmetro.listitem.ListDouble;
import com.ganada.silsiganmetro.R;
import com.ganada.silsiganmetro.real.Realtime;
import com.ganada.silsiganmetro.MetroApplication;
import com.ganada.silsiganmetro.util.ThemeManager;
import com.ganada.silsiganmetro.view.CustomTitlebar;
import com.ganada.silsiganmetro.view.RefreshButton;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class KJoongActivity extends Activity {

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
	String POS_LINE = "pos_kjoong";
	String BOOL_LINE = "bool_kjoong";
	String xml;
	int MAX_TRAIN = 65;
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
		ListDouble listdouble;
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
			window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), tm.getTitleBarColor(iTheme, LineManager.LINE_KEUIJOONG)));
		}
		layTitle.setText("경의·중앙선");
		layTitle.setBackgroundColorById(tm.getTitleBarColor(iTheme, LineManager.LINE_KEUIJOONG));

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
		서울역: 4

		급행 미정차: 0
		급행 정차: 1

		 */
		listdouble = new ListDouble("문산 - 가좌", 1063000000, 9, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("문산", 1063075335, 2, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("파주", 1063075334, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("월롱", 1063075333, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("금촌", 1063075331, 0, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("금릉", 1063075330, 0, 1, "일부 급행 미정차");
		arCustomList.add(listdouble);
		listdouble = new ListDouble("운정", 1063075329, 0, 1, "일부 급행 미정차");
		arCustomList.add(listdouble);
		listdouble = new ListDouble("야당", 1063075328, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("탄현", 1063075327, 0, 1, "일부 급행 미정차");
		arCustomList.add(listdouble);
		listdouble = new ListDouble("일산", 1063075326, 0, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("풍산", 1063075325, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("백마", 1063075324, 0, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("곡산", 1063075323, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("대곡", 1063075322, 1, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("능곡", 1063075321, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("행신", 1063075320, 0, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("강매", 1063075319, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("화전", 1063075318, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("수색", 1063075317, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("디지털미디어시티", 1063075316, 1, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("가좌", 1063075315, 1, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("신촌 - 서울역", 1000000000, 9, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("신촌", 1063080312, 0, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("서울역", 1063080313, 4, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("가좌 - 용문", 1000000000, 9, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("가좌", 1063075315, 1, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("홍대입구", 1063075314, 1, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("서강대", 1063075313, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("공덕", 1063075312, 1, 1, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("효창공원앞", 1063075826, 1, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("용산", 1063075110, 1, 3, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("이촌", 1063075111, 1, 2, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("서빙고", 1063075112, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("한남", 1063075113, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("옥수", 1063075114, 1, 2, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("응봉", 1063075115, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("왕십리", 1063075116, 1, 2, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("청량리", 1063075117, 1, 2, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("회기", 1063075118, 1, 2, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("중랑", 1063075119, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("상봉", 1063075120, 1, 2, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("망우", 1063075121, 1, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("양원", 1063075122, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("구리", 1063075123, 0, 2, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("도농", 1063075124, 0, 2, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("양정", 1063075125, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("덕소", 1063075126, 0, 2, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("도심", 1063075127, 0, 2, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("팔당", 1063075128, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("운길산", 1063075129, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("양수", 1063075130, 0, 2, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("신원", 1063075131, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("국수", 1063075132, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("아신", 1063075133, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("오빈", 1063075134, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("양평", 1063075135, 0, 2, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("원덕", 1063075136, 0, 0, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("용문", 1063075137, 0, 2, null);
		arCustomList.add(listdouble);
		listdouble = new ListDouble("지평", 1063075138, 3, 0, null);
		arCustomList.add(listdouble);

		adapter = new ListDoubleAdapter(this, tm.getListTheme(iTheme, LineManager.LINE_KEUIJOONG, mPref.getInt("iLineStatus", 0)), arCustomList);

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

	public void parse() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			// xml을 InputStrem 형태로 변환
			InputStream is = new ByteArrayInputStream(xml.getBytes());
			// document와 element 는 w3c dom에 있는것을 임포트 한다.
			Document doc = documentBuilder.parse(is);
			Element element = doc.getDocumentElement();
			// 읽어올 태그명 정하기
			NodeList items = element.getElementsByTagName("statnId"); // 역 번호
			NodeList items1 = element.getElementsByTagName("updnLineSe"); // 상행 = 0, 하행 = 1
			NodeList items2 = element.getElementsByTagName("trainNo"); // 열차 번호
			NodeList items3 = element.getElementsByTagName("statnTnm"); // 행선지
			NodeList items4 = element.getElementsByTagName("trainSttus"); // 열차 상태
			NodeList items5 = element.getElementsByTagName("directAt"); // 급행 상태
			// 읽어온 자료의 수
			int n = items.getLength();
			// 자료를 누적시킬 stringBuffer 객체
			StringBuffer sBuffer = new StringBuffer();
			// 반복문을 돌면서 모든 데이터 읽어오기
			for (int i = 0; i < n; i++) {
				// 읽어온 자료에서 알고 싶은 문자열의 인덱스 번호를 전달한다.
				Node item = items.item(i);
				Node item1 = items1.item(i);
				Node item2 = items2.item(i);
				Node item3 = items3.item(i);
				Node item4 = items4.item(i);
				Node item5 = items5.item(i);
				Node text = item.getFirstChild();
				Node text1 = item1.getFirstChild();
				Node text2 = item2.getFirstChild();
				Node text3 = item3.getFirstChild();
				Node text4 = item4.getFirstChild();
				Node text5 = item5.getFirstChild();
				// 해당 노드에서 문자열 읽어오기
				station[i] = Integer.parseInt(text.getNodeValue());
				status[i] = Integer.parseInt(text1.getNodeValue());
				trainNo[i] = text2.getNodeValue();
				trainHead[i] = text3.getNodeValue();
				trainStatus[i] = text4.getNodeValue();
				trainExpress[i] = Integer.parseInt(text5.getNodeValue());

			}
			// 읽어온 문자열 출력해보기

		} catch (Exception e) {
			// TODO: handle exception
			//Log.e("파싱 중 에러 발생", e.getMessage());
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
			viewHolder.view_line1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_kj));
			viewHolder.view_line2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_kj));
			viewHolder.view_line3.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_kj));
			viewHolder.view_line4.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.line_kj));

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
				viewHolder.img_icon_express1.setVisibility(View.GONE);
				viewHolder.txt_station_sub.setVisibility(View.GONE);
			} else {
				viewHolder.btn_train_up.setVisibility(View.INVISIBLE);
				viewHolder.btn_train_down.setVisibility(View.INVISIBLE);
				viewHolder.img_train_down_norm.setVisibility(View.INVISIBLE);
				viewHolder.img_train_up_norm.setVisibility(View.INVISIBLE);
				viewHolder.view_margin.setVisibility(View.VISIBLE);
				viewHolder.layout_train.setVisibility(View.VISIBLE);
				viewHolder.view_divider.setVisibility(tm.getDividerVisibility(iTheme));

				if (arC.get(position).strSub == null) {
					viewHolder.txt_station_sub.setVisibility(View.GONE);
				} else {
					viewHolder.txt_station_sub.setVisibility(View.VISIBLE);
					viewHolder.txt_station_sub.setText(arC.get(position).strSub);
				}

				if (arC.get(position).iExpress == 0) {
					viewHolder.img_icon_express.setVisibility(View.GONE);
					viewHolder.img_icon_express1.setVisibility(View.GONE);
				} else if (arC.get(position).iExpress == 1) {
					viewHolder.img_icon_express.setVisibility(View.VISIBLE);
					viewHolder.img_icon_express1.setVisibility(View.GONE);
					viewHolder.img_icon_express.setImageResource(R.drawable.img_express);
				} else if (arC.get(position).iExpress == 2) {
					viewHolder.img_icon_express.setVisibility(View.VISIBLE);
					viewHolder.img_icon_express1.setVisibility(View.GONE);
					viewHolder.img_icon_express.setImageResource(R.drawable.img_express2);
				} else if (arC.get(position).iExpress == 3) {
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
					viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1063_norm);
					viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1063_norm);
				} else if (arC.get(position).iStainfo == 1) {
					// 환승
					viewHolder.view_line1.setVisibility(View.VISIBLE);
					viewHolder.view_line2.setVisibility(View.VISIBLE);
					viewHolder.view_line3.setVisibility(View.VISIBLE);
					viewHolder.view_line4.setVisibility(View.VISIBLE);
					viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1063_tran);
					viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1063_tran);
				} else if (arC.get(position).iStainfo == 2) {
					// 맨 위
					viewHolder.view_line1.setVisibility(View.INVISIBLE);
					viewHolder.view_line2.setVisibility(View.VISIBLE);
					viewHolder.view_line3.setVisibility(View.INVISIBLE);
					viewHolder.view_line4.setVisibility(View.VISIBLE);
					viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1063_tran);
					viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1063_tran);
				} else if (arC.get(position).iStainfo == 3) {
					// 맨 아래
					viewHolder.view_line1.setVisibility(View.VISIBLE);
					viewHolder.view_line2.setVisibility(View.INVISIBLE);
					viewHolder.view_line3.setVisibility(View.VISIBLE);
					viewHolder.view_line4.setVisibility(View.INVISIBLE);
					viewHolder.icon_station_down_norm.setImageResource(R.drawable.icon_1063_tran);
					viewHolder.icon_station_up_norm.setImageResource(R.drawable.icon_1063_tran);
				}

				if (boolInfo) {
					for (int i = 0; i < MAX_TRAIN; i++) {
						if (station[i] == arC.get(position).iNum) {
							if (status[i] == 0) {
								viewHolder.btn_train_up.setVisibility(View.VISIBLE);
								viewHolder.img_train_up_norm.setVisibility(View.VISIBLE);
								if (trainExpress[i] == 0) {
									viewHolder.img_icon_up.setImageResource(tm.getTrainIcon(iTheme, 0, false));
								} else if (trainExpress[i] == 1) {
									viewHolder.img_icon_up.setImageResource(tm.getTrainIcon(iTheme, 0, true));
								}

								if(mPref.getInt("iTrainStatus", 0) == 0) {
									viewHolder.btn_train_up.setText(trainHead[i] + "\n" + trainNo[i]);
								} else {
									viewHolder.btn_train_up.setText(trainHead[i] + "\n" + trainNo[i] + "\n" + trainStatus[i]);
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
								viewHolder.btn_train_down.setVisibility(View.VISIBLE);
								viewHolder.img_train_down_norm.setVisibility(View.VISIBLE);
								if (trainExpress[i] == 0) {
									viewHolder.img_icon_down.setImageResource(tm.getTrainIcon(iTheme, 1, false));
								} else if (trainExpress[i] == 1) {
									viewHolder.img_icon_down.setImageResource(tm.getTrainIcon(iTheme, 1, true));
								}

								if(mPref.getInt("iTrainStatus", 0) == 0) {
									viewHolder.btn_train_down.setText(trainHead[i] + "\n" + trainNo[i]);
								} else {
									viewHolder.btn_train_down.setText(trainHead[i] + "\n" + trainNo[i] + "\n" + trainStatus[i]);
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
			}

			viewHolder.btn_train_up.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					CalcDate cd = new CalcDate();
					StringBuffer sb = new StringBuffer();
					sb.append("https://rail.blue/railroad/logis/Default.aspx?date=");
					sb.append(cd.getDate());
					sb.append("&train=");
					sb.append("K");
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
					sb.append("K");
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

		AlertDialog.Builder alert = new AlertDialog.Builder(KJoongActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
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
						lineManager.setFavorite(str_station, LineManager.LINE_KEUIJOONG, index);
						Toast.makeText(getApplicationContext(), arCustomList.get(index).strStation + "역을 모아보기에 추가하였습니다.", Toast.LENGTH_SHORT).show();
						break;

					case 2:
						DBManager dm = new DBManager(getApplicationContext());
						dm.setStation("경의 · 중앙선", str_station + ":" + index);
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
				trainExpress[i] = Integer.parseInt(parseredData[i][5]);
			}

			return parseredData;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e2) {
			e2.printStackTrace();
			return null;
		}
	}

	public void AppStart() {
		myAsyncTask = new MyAsyncTask();
		myAsyncTask.execute("경의중앙선");
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

