package com.ganada.silsiganmetro.common;

public class Important {
    public static final String ADMOB_KEY = "ca-app-pub-9749702189208691~3867615479";

    public static final String API_KEY_LINE = "44624958526b736a393451466a7849";
    public static final String API_KEY_STATION = "744e4442656b736a39314877564a41";
    public static final String API_KEY_TABLE = "46634179736b736a313035596f587452";

    public static final String URL_SUBWAY = "http://m.bus.go.kr/mBus/subway/getArvlByInfo.bms?subwayId=%s&statnId=%s";
    public static final String URL_INFO = "https://guk2zzada.com/silsiganmetro/";
    public static final String URL_CUSTOM_LINE = "https://guide94.cafe24.com/silsiganmetro/whereistrain.php";
    public static final String URL_LINE = "http://swopenAPI.seoul.go.kr/api/subway/" + API_KEY_LINE + "/json/realtimePosition/0/200/";
    public static final String URL_STATION = "http://swopenAPI.seoul.go.kr/api/subway/" + API_KEY_STATION + "/json/realtimeStationArrival/0/20/";
    public static final String URL_TABLE = "http://openAPI.seoul.go.kr:8088/" + API_KEY_TABLE + "/json/SearchSTNTimeTableByFRCodeService/1/500/";

    public static final String URL_DOMAIN = "https://guk2zzada.com/silsiganmetro/";
    public static final String URL_CAFE_LINE = URL_DOMAIN + "silsigan/real.php?line=";

    public static final String URL_TEST = "http://guide94.cafe24.com/test1002.json";
}
