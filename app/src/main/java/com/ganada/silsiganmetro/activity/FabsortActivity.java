package com.ganada.silsiganmetro.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ganada.silsiganmetro.common.ItemMoveListener;
import com.ganada.silsiganmetro.util.LineManager;
import com.ganada.silsiganmetro.common.OnStartDragListener;
import com.ganada.silsiganmetro.R;
import com.ganada.silsiganmetro.common.SimpleItemTouchHelperCallback;
import com.ganada.silsiganmetro.util.ThemeManager;

import java.util.ArrayList;
import java.util.Collections;

public class FabsortActivity extends Activity implements OnStartDragListener {

    private SharedPreferences mPref;
    private SharedPreferences.Editor mPrefEdit;

    LineManager lineManager;
    ThemeManager tm;

    ImageButton btn_reset;
    ImageButton btnBack;

    RecyclerView list;
    RecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ItemTouchHelper touchHelper;

    FabSortList fabsortlist;
    ArrayList<FabSortList> arrSortList;

    String[] arr_line;
    String[] arr_station;
    String[] arr_position;

    boolean bool_remove = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabsort);

        mPref = getSharedPreferences("Pref1", 0);
        mPrefEdit = mPref.edit();

        lineManager = new LineManager(getApplicationContext());
        tm = new ThemeManager(getBaseContext());
        arrSortList = new ArrayList<FabSortList>();

        btn_reset = (ImageButton) findViewById(R.id.btn_reset);
        btnBack = (ImageButton) findViewById(R.id.btnBack);

        LinearLayout layout_status = (LinearLayout) findViewById(R.id.layout_status);

        if(Build.VERSION.SDK_INT < 19) {
            layout_status.setVisibility(View.GONE);
        }
        layout_status.setBackgroundColor(Color.parseColor("#91be2e"));

        setRemoveMode();

        try {
            arr_line = lineManager.getLine().split(";");
            arr_station = lineManager.getStation().split(";");
            arr_position = lineManager.getPosition().split(";");

            for (int i = 0; i < arr_line.length; i++) {
                fabsortlist = new FabSortList(arr_line[i], arr_station[i], arr_position[i]);
                arrSortList.add(fabsortlist);
            }

            adapter = new RecyclerAdapter(arrSortList, this);
            list = (RecyclerView) findViewById(R.id.list);
            layoutManager = new LinearLayoutManager(this);
            list.setLayoutManager(layoutManager);
            list.setAdapter(adapter);

            ItemTouchHelper.Callback callback =  new SimpleItemTouchHelperCallback(adapter);
            touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(list);

            btn_reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bool_remove = !bool_remove;
                    setRemoveMode();
                    adapter.notifyDataSetChanged();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }

    private void setRemoveMode() {
        if(arrSortList.size() > 0) {
            if (!bool_remove) {
                btn_reset.setImageResource(R.drawable.ic_delete_white_24dp);
            } else {
                btn_reset.setImageResource(R.drawable.ic_done_white_24dp);
            }
        }
    }

    private void checkItem() {
        if(arrSortList.size() <= 0) {
            bool_remove = false;
            btn_reset.setImageResource(R.drawable.ic_delete_white_24dp);
        }
    }

    private void makeString() {
        String str_line = "";
        String str_station = "";
        String str_position = "";
        for(int i = 0; i < arrSortList.size(); i++) {
            str_line += arrSortList.get(i).str_line + ";";
            str_station += arrSortList.get(i).str_station + ";";
            str_position += arrSortList.get(i).str_position + ";";
        }

        mPrefEdit.putString("strFabLine", str_line);
        mPrefEdit.putString("strFabSta", str_station);
        mPrefEdit.putString("strFabPos", str_position);
        mPrefEdit.commit();
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> implements ItemMoveListener {
        ArrayList<FabSortList> arC;
        private final OnStartDragListener mDragStartListener;

        RecyclerAdapter(ArrayList<FabSortList> items, OnStartDragListener dragStartListener){
            arC = items;
            mDragStartListener = dragStartListener;
        }


        // 새로운 뷰 홀더 생성
        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fabsort,parent,false);
            return new ItemViewHolder(view);
        }


        // View 의 내용을 해당 포지션의 데이터로 바꿉니다.
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public void onBindViewHolder(final ItemViewHolder holder, final int position) {
            final int pos = position;
            holder.txt1.setText(tm.getCircleLineText(Integer.parseInt(arC.get(position).str_line)));
            holder.txt1.setBackgroundResource(tm.getCircleLineColor(Integer.parseInt(arC.get(position).str_line)));
            holder.txt2.setText(arC.get(position).str_station);
            holder.img.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.icon_sort));

            if(bool_remove) {
                holder.img.setVisibility(View.GONE);
                holder.btn_remove.setVisibility(View.VISIBLE);
            } else {
                holder.img.setVisibility(View.VISIBLE);
                holder.btn_remove.setVisibility(View.GONE);
            }

            holder.img.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) ==
                            MotionEvent.ACTION_DOWN) {
                        mDragStartListener.onStartDrag(holder);
                    }
                    return false;
                }
            });

            holder.btn_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemDismiss(position);
                    makeString();
                }
            });
        }

        // 데이터 셋의 크기를 리턴해줍니다.
        @Override
        public int getItemCount() {
            return arC.size();
        }

        @Override
        public void onItemDismiss(int position) {
            arrSortList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, arrSortList.size());
        }

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(arrSortList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(arrSortList, i, i - 1);
                }
            }
            makeString();
            notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        // 커스텀 뷰홀더
        // item layout 에 존재하는 위젯들을 바인딩합니다.
        class ItemViewHolder extends RecyclerView.ViewHolder{
            private TextView txt1;
            private TextView txt2;
            private ImageView img;
            private ImageButton btn_remove;
            public ItemViewHolder(View itemView) {
                super(itemView);
                txt1 = (TextView) itemView.findViewById(R.id.txt_line_name1);
                txt2 = (TextView) itemView.findViewById(R.id.txt_line_name2);
                img = (ImageView) itemView.findViewById(R.id.drag_handle);
                btn_remove = (ImageButton) itemView.findViewById(R.id.btn_remove);
            }
        }
    }
}

class FabSortList {
    String str_line;
    String str_station;
    String str_position;

    FabSortList(String str_line, String str_station, String str_position) {
        this.str_line = str_line;
        this.str_station = str_station;
        this.str_position = str_position;
    }
}
