package com.ganada.silsiganmetro.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ganada.silsiganmetro.laboratory.LineType;
import com.ganada.silsiganmetro.util.DBManager;
import com.ganada.silsiganmetro.listitem.DataList;
import com.ganada.silsiganmetro.common.ItemMoveListener;
import com.ganada.silsiganmetro.common.OnStartDragListener;
import com.ganada.silsiganmetro.R;
import com.ganada.silsiganmetro.common.SimpleItemTouchHelperCallback;
import com.ganada.silsiganmetro.util.ThemeManager;

import java.util.ArrayList;
import java.util.Collections;

public class SortActivity extends Activity implements OnStartDragListener {

    private SharedPreferences mPref;
    private SharedPreferences.Editor mPrefEdit;
    DBManager dm;

    ImageButton btn_reset;
    ImageButton btnBack;
    ImageButton btnRemove;
    RecyclerView list;

    RecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ItemTouchHelper touchHelper;
    ItemTouchHelper.Callback callback;
    ArrayList<DataList> arCustomList;
    ArrayList<DataList> arSortList;

    int screen_w;
    int screen_h;
    int height;
    boolean bool_remove = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);

        dm = new DBManager(this);
        mPref = getSharedPreferences("Pref1", 0);
        mPrefEdit = mPref.edit();

        btn_reset = (ImageButton) findViewById(R.id.btn_reset);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnRemove = (ImageButton) findViewById(R.id.btn_remove);

        LinearLayout layout_status = (LinearLayout) findViewById(R.id.layout_status);

        if(Build.VERSION.SDK_INT < 19) {
            layout_status.setVisibility(View.GONE);
        }
        layout_status.setBackgroundColor(Color.parseColor("#91be2e"));

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screen_w = size.x;
        screen_h = size.y;
        //Log.e("Screen Width - ", String.valueOf(screen_w));
        //Log.e("Screen Height - ", String.valueOf(screen_h));

        arSortList = new ArrayList<DataList>();
        arCustomList = dm.getDBList();
        arSortList.clear();
        arSortList.addAll(arCustomList);

        //adapter = new MainListAdapter(this, R.layout.item_sort, lines);

        adapter = new RecyclerAdapter(arSortList, this);
        list = (RecyclerView) findViewById(R.id.list);
        layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);
        ((SimpleItemAnimator) list.getItemAnimator()).setSupportsChangeAnimations(true);
        list.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_UP) {
                    View reV = rv.findChildViewUnder(e.getX(), e.getY());
                    Log.e("action", reV + "");
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_UP) {
                    Log.e("action", "up");
                }
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        callback =  new SimpleItemTouchHelperCallback(adapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(list);

        btn_reset.setColorFilter(Color.parseColor("#FFFFFF"));
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(SortActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                dlg.setTitle("목록 초기화");
                dlg.setMessage("목록이 모두 초기화됩니다.\n정말로 초기화하시겠습니까?");
                dlg.setPositiveButton("초기화", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        arSortList.clear();
                        bool_remove = false;
                        btnRemove.setImageResource(R.drawable.ic_delete_white_24dp);
                        arSortList.addAll(dm.resetData());
                        adapter.notifyDataSetChanged();
                    }
                });

                dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                dlg.setCancelable(true);
                dlg.show();
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bool_remove = !bool_remove;
                setRemoveMode();
                adapter.notifyDataSetChanged();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    protected void onPause() {
        super.onPause();
        saveData();
    }

    public void saveData() {
        for(int i = 0; i < arSortList.size(); i++) {
            Log.e("array[" + i + "]", arSortList.get(i).text);
        }
        dm.setDB(arSortList);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        /*View img = (ImageView)this.findViewById(R.id.img_drag);
        width = img.getWidth();
        height = img.getHeight();
        Log.e("Layout Width - ", String.valueOf(img.getWidth()));
        Log.e("Layout Height - ", String.valueOf(img.getHeight()));*/
        //listView.setScreenSize(screen_w, screen_h, width, height);
    }

    private void setRemoveMode() {
        if(arSortList.size() > 0) {
            if (!bool_remove) {
                btnRemove.setImageResource(R.drawable.ic_delete_white_24dp);
            } else {
                btnRemove.setImageResource(R.drawable.ic_done_white_24dp);
            }
        }
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> implements ItemMoveListener {
        ArrayList<DataList> arC;
        private final OnStartDragListener mDragStartListener;

        public RecyclerAdapter(ArrayList<DataList> items, OnStartDragListener dragStartListener){
            arC = items;
            mDragStartListener = dragStartListener;
        }


        // 새로운 뷰 홀더 생성
        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sort, parent,false);
            return new ItemViewHolder(view);
        }


        // View 의 내용을 해당 포지션의 데이터로 바꿉니다.
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public void onBindViewHolder(final ItemViewHolder holder, final int position) {
            holder.handleView.setClickable(true);
            LineType lineType = LineType.getLine(arSortList.get(position).line);

            if (arSortList.get(position).none == 1) {
                holder.txtCircle.setVisibility(View.GONE);

                holder.txt_line_name1.setText(lineType.getLineName());
            } else {
                holder.txtCircle.setVisibility(View.VISIBLE);
                holder.txtCircle.setText(
                        lineType.getLineName()
                                .replace("호선", "")
                                .replace("선", "")
                );
                holder.txtCircle.setBackgroundResource(
                        new ThemeManager(getBaseContext()).getCircleLineColor(lineType.getLineNumber())
                );
                holder.txt_line_name1.setText(arSortList.get(position).text.split(":")[0]);
            }

            if(bool_remove) {
                holder.handleView.setVisibility(View.GONE);
                if(arSortList.get(position).none == 0) {
                    holder.btn_remove.setVisibility(View.VISIBLE);
                } else {
                    holder.btn_remove.setVisibility(View.INVISIBLE);
                }
            } else {
                holder.handleView.setVisibility(View.VISIBLE);
                holder.btn_remove.setVisibility(View.GONE);
            }

            holder.handleView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();

                    if (action == MotionEvent.ACTION_DOWN) {
                        mDragStartListener.onStartDrag(holder);
                        Log.e("action", "down");
                    } else if(action == MotionEvent.ACTION_UP) {
                        Log.e("action", "up");
                    }
                    return true;
                }
            });

            holder.btn_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemDismiss(position);
                    dm.setDB(arSortList);
                }
            });
        }

        // 데이터 셋의 크기를 리턴해줍니다.
        @Override
        public int getItemCount() {
            return arSortList.size();
        }

        @Override
        public void onItemDismiss(int position) {
            arSortList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, arSortList.size());
        }

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(arSortList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(arSortList, i, i - 1);
                }
            }
            dm.setDB(arSortList);
            notifyItemMoved(fromPosition, toPosition);
            return true;
        }



        // 커스텀 뷰홀더
        // item layout 에 존재하는 위젯들을 바인딩합니다.
        class ItemViewHolder extends RecyclerView.ViewHolder{
            private TextView txt_line_name1;
            private TextView txtCircle;
            private ImageView handleView;
            private ImageButton btn_remove;
            public ItemViewHolder(View itemView) {
                super(itemView);
                txtCircle = (TextView) itemView.findViewById(R.id.txtCircle);
                txt_line_name1 = (TextView) itemView.findViewById(R.id.txt_line_name1);
                handleView = (ImageView) itemView.findViewById(R.id.drag_handle);
                btn_remove = (ImageButton) itemView.findViewById(R.id.btn_remove);
            }
        }
    }
}