package com.ganada.silsiganmetro.laboratory;

import android.content.ClipData;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ganada.silsiganmetro.R;
import com.ganada.silsiganmetro.listitem.LineStation;
import com.ganada.silsiganmetro.util.DBManager;
import com.ganada.silsiganmetro.util.LineManager;
import com.ganada.silsiganmetro.util.ThemeManager;
import com.ganada.silsiganmetro.util.Units;
import com.ganada.silsiganmetro.view.LineRail;

import java.util.ArrayList;

public class StationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<LineStation> stations;
    private ThemeManager tm;
    private int lineNumber;

    StationAdapter(ArrayList<LineStation> items, ThemeManager tm, int lineNumber){
        stations = items;
        this.tm = tm;
        this.lineNumber = lineNumber;
    }

    @Override
    public int getItemViewType(int position) {
        if(stations.get(position).number.equals("1000000000")) {
            return 1;
        } else {
            if(lineNumber != LineManager.LINE_1 && lineNumber != LineManager.LINE_9 && lineNumber != LineManager.LINE_GONGHANG) {
                return 2;
            } else {
                return 3;
            }
        }
    }

    public int getItemLayoutId(int type) {
        switch(type) {
            case 1:
                return R.layout.item_seperator;
            case 2:
            case 3:
            default:
                return R.layout.item_test_single;

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getItemLayoutId(viewType), parent, false);
        switch(viewType) {
            case 1:
                return new ViewHolderSeperator(view);

            case 2:
                return new ViewHolderSingle(view);
            case 3:
            default:
                return new ViewHolderDouble(view);
        }
    }

    @Override
    public int getItemCount() {
        return stations.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        switch(getItemViewType(position)) {
            case 1:
                ViewHolderSeperator vs = (ViewHolderSeperator) holder;
                vs.bindView(position);
                break;

            case 2:
                ViewHolderSingle v = (ViewHolderSingle) holder;
                v.bindView(position);
                break;

            case 3:
                break;
        }
    }

    class ViewHolderSingle extends RecyclerView.ViewHolder {

        LineStation station;

        RelativeLayout layoutBack;
        TextView txtStation;
        TextView txtSubtext;
        LineRail lineUp;
        LineRail lineDown;
        ImageView imgExpress1;
        ImageView imgExpress2;

        ViewHolderSingle(View itemView) {
            super(itemView);
            layoutBack = itemView.findViewById(R.id.layoutBack);
            txtStation = itemView.findViewById(R.id.txtStation);
            txtSubtext = itemView.findViewById(R.id.txtSubtext);
            lineUp = itemView.findViewById(R.id.lineUp);
            lineDown = itemView.findViewById(R.id.lineDown);
            imgExpress1 = itemView.findViewById(R.id.imgExpress1);
            imgExpress2 = itemView.findViewById(R.id.imgExpress2);
        }

        void bindView(int position) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) layoutBack.getLayoutParams();
            param.height = Units.dp(tm.getListHeight());
            layoutBack.setLayoutParams(param);

            station = stations.get(position);

            lineUp.setStyle(lineNumber, station.info);
            lineDown.setStyle(lineNumber, station.info);

            if(station.subText.equals("")) {
                txtSubtext.setVisibility(View.GONE);
            } else {
                txtSubtext.setVisibility(View.VISIBLE);
            }

            switch(station.express) {
                case 0:
                    imgExpress1.setVisibility(View.GONE);
                    imgExpress2.setVisibility(View.GONE);
                    break;

                case 1:
                    imgExpress1.setVisibility(View.VISIBLE);
                    imgExpress2.setVisibility(View.GONE);
                    break;

                case 2:
                    imgExpress1.setVisibility(View.GONE);
                    imgExpress2.setVisibility(View.VISIBLE);
                    break;

                case 3:
                    imgExpress1.setVisibility(View.VISIBLE);
                    imgExpress2.setVisibility(View.VISIBLE);
                    break;
            }

            txtStation.setText(station.station);
            txtStation.setTextSize(TypedValue.COMPLEX_UNIT_SP, tm.getStationFontSize());
        }
    }

    class ViewHolderDouble extends RecyclerView.ViewHolder {

        ViewHolderDouble(View itemView) {
            super(itemView);
        }
    }

    class ViewHolderSeperator extends BindViewHolder {

        RelativeLayout layoutBack;
        TextView txtText;

        ViewHolderSeperator(View itemView) {
            super(itemView);
            layoutBack = itemView.findViewById(R.id.layoutBack);
            txtText = itemView.findViewById(R.id.txtText);
        }

        @Override
        void bind(ClipData.Item item) {

        }

        void bindView(int position) {
            RecyclerView.LayoutParams par = (RecyclerView.LayoutParams) layoutBack.getLayoutParams();
            par.height = Units.dp(25);
            layoutBack.setLayoutParams(par);
            txtText.setText(stations.get(position).station);
        }
    }
}
