package com.ganada.silsiganmetro.common;

/**
 * Created by user on 2017. 8. 22..
 */

public class StringRefactor {
    public String removeLastChar(String str) {
        String temp = "";
        if(str.length() > 0) {
            return str.substring(0, str.length() - 1);
        } else {
            return str;
        }
    }

    public String changeStatus(String str) {
        return str.replace("초 후", "초").replace("번째 전역", "번째 전").replace("전역 ", "전 역 ");
    }
}
