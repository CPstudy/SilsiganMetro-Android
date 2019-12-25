package com.ganada.silsiganmetro.common;

/**
 * Created by user on 2018-01-04.
 */

public interface ItemMoveListener {

    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}