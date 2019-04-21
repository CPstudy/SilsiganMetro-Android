package com.ganada.silsiganmetro.laboratory;

import com.ganada.silsiganmetro.R;

public enum LineType {
    LINE1("1호선", "1호선", "소요산 - 인천/신창", 1001, R.color.line_1),
    LINE2("2호선", "2호선", "시청 - 시청", 1002, R.color.line_2),
    LINE3("3호선", "3호선", "대화 - 오금", 1003, R.color.line_3),
    LINE4("4호선", "4호선", "당고개 - 오이도", 1004, R.color.line_4),
    LINE5("5호선", "5호선", "방화 - 상일동/마천", 1005, R.color.line_5),
    LINE6("6호선", "6호선", "응암 - 봉화산", 1006, R.color.line_6),
    LINE7("7호선", "7호선", "장암 - 부평구청", 1007, R.color.line_7),
    LINE8("8호선", "8호선", "암사 - 모란", 1008, R.color.line_8),
    LINE9("9호선", "9호선", "개화 - 종합운동장", 1009, R.color.line_9),
    KEUIJOONGANG("경의 · 중앙선", "경의중앙선", "문산 - 서울역/지평", 1063, R.color.line_kj),
    BUNDANG("분당선", "분당선", "왕십리 - 수원", 1075, R.color.line_bs),
    SUIN("수인선", "수인선", "오이도 - 인천", 1071, R.color.line_bs),
    SHINBUNDANG("신분당선", "신분당선", "강남 - 광교", 1077, R.color.line_sb),
    GONGHANG("공항철도", "공항철도", "청량리 - 춘천", 1065, R.color.line_gh),
    KYEONGCHOON("경춘선", "경춘선", "인천국제공항 - 서울역", 1067, R.color.line_kc);

    private String lineName;
    private String apiName;
    private String description;
    private int lineNumber;
    private int colorId;

    LineType(String lineName, String apiName, String description, int lineNumber, int colorId) {
        this.lineName = lineName;
        this.apiName = apiName;
        this.description = description;
        this.lineNumber = lineNumber;
        this.colorId = colorId;
    }



    public static LineType getLine(String lineName) {
        for(LineType v : values()) {
            if (v.getLineName().equals(lineName)) {
                return v;
            }
        }
        return LINE1;
    }

    public static LineType getLine(int lineNumber) {
        for(LineType v : values()) {
            if (v.getLineNumber() == lineNumber) {
                return v;
            }
        }
        return LINE1;
    }

    public String getLineName() {
        return lineName;
    }

    public String getApiName() {
        return apiName;
    }

    public String getDescription() {
        return description;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColorId() {
        return colorId;
    }

}
