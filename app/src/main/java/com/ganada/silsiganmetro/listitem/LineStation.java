package com.ganada.silsiganmetro.listitem;

public class LineStation {

    public String station;
    public String subText;
    public String number;
    public String prev;
    public String next;
    public int info;
    public int express;
    public int posY;

    public LineStation(String station, String subText, String number, String prev, String next, int info, int express, int posY) {
        this.station = station;
        this.subText = subText;
        this.number = number;
        this.prev = prev;
        this.next = next;
        this.info = info;
        this.express = express;
        this.posY = posY;
    }

}
