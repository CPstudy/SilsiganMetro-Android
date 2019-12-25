package com.ganada.silsiganmetro.listitem;

/**
 * Created by user on 2017. 8. 19..
 */

public class ListTrain {
    public String str_this_station;
    public String str_line;
    public String str_no;
    public String str_dir;
    public String str_dst;
    public String str_station;
    public String str_status;
    public String str_sort;
    public String str_express;
    public int i_time;
    public int arvlCd;

    public ListTrain(String str_this_station,
              String str_line,
              String str_no,
              String str_dir,
              String str_dst,
              String str_station,
              String str_status,
              String str_sort,
              String str_express,
              int i_time,
              int arvlCd) {
        this.str_this_station = str_this_station;
        this.str_line = str_line;
        this.str_no = str_no;
        this.str_dir = str_dir;
        this.str_dst = str_dst;
        this.str_station = str_station;
        this.str_status = str_status;
        this.str_sort = str_sort;
        this.str_express = str_express;
        this.i_time = i_time;
        this.arvlCd = arvlCd;
    }

    public String getLine() {
        return this.str_line + str_sort;
    }
}
