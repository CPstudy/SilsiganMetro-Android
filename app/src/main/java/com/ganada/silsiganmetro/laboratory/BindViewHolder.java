package com.ganada.silsiganmetro.laboratory;

import android.content.ClipData;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class BindViewHolder extends RecyclerView.ViewHolder {
    public BindViewHolder(View itemView) {
        super(itemView);
    }

    abstract void bind(ClipData.Item item);
}
