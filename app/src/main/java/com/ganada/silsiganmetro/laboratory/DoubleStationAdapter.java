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
import com.ganada.silsiganmetro.listitem.TrainList;
import com.ganada.silsiganmetro.util.ThemeManager;
import com.ganada.silsiganmetro.view.LineRail;
import com.ganada.silsiganmetro.view.TrainLocation;

import java.util.ArrayList;
import java.util.List;

public class DoubleStationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int SEPERATOR = 0;
    private static final int LINE_LEFT = 1;
    private static final int LINE_RIGHT = 2;

    private List<LineStation> stations;
    private List<TrainList> trains = new ArrayList<>();
    private ThemeManager tm;
    private int lineNumber;
    private int listHeight;
    private int sepHeight;
    private int trainHeight;

    DoubleStationAdapter(List<LineStation> items, ThemeManager tm, int lineNumber, int listHeight, int sepHeight){
        stations = items;
        this.tm = tm;
        this.lineNumber = lineNumber;
        this.listHeight = listHeight;
        this.sepHeight = sepHeight;
    }

    public void setTrains(List<TrainList> trains, int trainHeight) {
        this.trains = trains;
        this.trainHeight = trainHeight;
    }

    @Override
    public int getItemViewType(int position) {
        if(stations.get(position).number.equals("1000000000")) {
            return SEPERATOR;
        } else {
            if(tm.getListDirection() == 0) {
                return LINE_LEFT;
            } else {
                return LINE_RIGHT;
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch(viewType) {
            case SEPERATOR:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seperator, parent, false);
                return new ViewHolderSeperator(view);

            case LINE_LEFT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_line_left_double, parent, false);
                return new ViewHolderSingle(view);

            case LINE_RIGHT:
            default:
                //view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_line_right_single, parent, false);
                //return new ViewHolderSingle(view);
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return stations.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        switch(getItemViewType(position)) {
            case SEPERATOR:
                ViewHolderSeperator vs = (ViewHolderSeperator) holder;
                vs.bindView(position);
                break;

            case LINE_LEFT:
            case LINE_RIGHT:
                ViewHolderSingle v = (ViewHolderSingle) holder;
                v.bindView(position);
                break;
        }
    }

    class ViewHolderSingle extends RecyclerView.ViewHolder {

        LineStation station;

        TrainLocation trainLocation;
        RelativeLayout layoutBack;
        TextView txtStation;
        TextView txtSubtext;
        LineRail lineUp;
        LineRail lineDown;
        LineRail lineEUp;
        LineRail lineEDown;
        ImageView imgExpress1;
        ImageView imgExpress2;

        ViewHolderSingle(View itemView) {
            super(itemView);
            trainLocation = itemView.findViewById(R.id.trainLocation);
            layoutBack = itemView.findViewById(R.id.layoutBack);
            txtStation = itemView.findViewById(R.id.txtStation);
            txtSubtext = itemView.findViewById(R.id.txtSubtext);
            lineUp = itemView.findViewById(R.id.lineUp);
            lineDown = itemView.findViewById(R.id.lineDown);
            lineEUp = itemView.findViewById(R.id.lineEUp);
            lineEDown = itemView.findViewById(R.id.lineEDown);
            imgExpress1 = itemView.findViewById(R.id.imgExpress1);
            imgExpress2 = itemView.findViewById(R.id.imgExpress2);
        }

        void bindView(int position) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) layoutBack.getLayoutParams();
            param.height = listHeight;
            layoutBack.setLayoutParams(param);

            station = stations.get(position);

            lineUp.setStyle(lineNumber, station.info);
            lineDown.setStyle(lineNumber, station.info);
            lineEUp.setStyle(lineNumber, station.info);
            lineEDown.setStyle(lineNumber, station.info);
            trainLocation.clearView();

            for(TrainList train : trains) {
                if(station.number.equals(train.stationNo)) {
                    trainLocation.addTrain(
                            listHeight,
                            trainHeight,
                            train.trainDst,
                            train.trainNo,
                            train.trainSttus,
                            train.updown,
                            0,
                            train.express,
                            lineNumber
                    );
                }
            }

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
            par.height = sepHeight;
            layoutBack.setLayoutParams(par);
            txtText.setText(stations.get(position).station);
        }
    }
}
