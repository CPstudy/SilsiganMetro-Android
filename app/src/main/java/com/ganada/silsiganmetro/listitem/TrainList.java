package com.ganada.silsiganmetro.listitem;

public class TrainList {
    public int updown;
    public int express;
    public String stationNo;
    public String trainNo;
    public String trainDst;
    public String trainSttus;
    public String trainType;

    public TrainList(int updown, int express, String stationNo, String trainNo, String trainDst, String trainSttus, String trainType) {
        this.updown = updown;
        this.express = express;
        this.stationNo = stationNo;
        this.trainNo = trainNo;
        this.trainDst = trainDst;
        this.trainSttus = trainSttus;
        this.trainType = trainType;
    }
}
