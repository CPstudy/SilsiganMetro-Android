package com.ganada.silsiganmetro.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.ganada.silsiganmetro.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

/**
 * Created by user on 2017. 7. 30..
 */

public class ThemeManager {

    @IntDef({View.VISIBLE, View.INVISIBLE, View.GONE})
    @Retention(RetentionPolicy.SOURCE)
    private @interface Visibility {}

    private SharedPreferences mPref;
    private SharedPreferences.Editor mPrefEdit;
    private Resources res;
    private Context context;

    public static final int VERSION_NEW = 0;
    public static final int VERSION_OLD = 1;
    public static final int VERSION_BLACK = 2;
    public static final int VERSION_BLUEGREEN = 3;

    private int i_station_font_size = 1;
    private int i_train_font_size = 1;
    private int i_font_cut = 0;

    public ThemeManager(Context context) {
        this.context = context;
        res = context.getResources();
        mPref = context.getSharedPreferences("Pref1", 0);
        mPrefEdit = mPref.edit();

        i_station_font_size = mPref.getInt("iStationFontSize", 16);
        i_train_font_size = mPref.getInt("iTrainFontSize", 11);
        i_font_cut = mPref.getInt("iFontCut", 0);
    }

    public int getTheme() {
        return mPref.getInt("iTheme", 0);
    }

    public void setStationFontSize(int size) {
        i_station_font_size = size;
        mPrefEdit.putInt("iStationFontSize", i_station_font_size);
        mPrefEdit.apply();
        mPrefEdit.commit();
    }

    public void setTrainFontSize(int size) {
        i_train_font_size = size;
        mPrefEdit.putInt("iTrainFontSize", i_train_font_size);
        mPrefEdit.apply();
        mPrefEdit.commit();
    }

    public String changeWord(String str) {
        i_font_cut = mPref.getInt("iFontCut", 0);
        String str_return = "";

        try {
            switch (i_font_cut) {
                case VERSION_NEW:
                    str_return = str;
                    //str_return = "기본";
                    break;

                case VERSION_OLD:
                    if (str.length() >= 4) {
                        str_return = String.format(Locale.KOREA, "%c%c%c", str.charAt(0), str.charAt(1), str.charAt(2));
                    } else {
                        str_return = str;
                    }
                    //str_return = "3자";
                    break;

                case VERSION_BLACK:
                    if (str.length() >= 4) {
                        str_return = String.format(Locale.KOREA, "%c%c%c%c", str.charAt(0), str.charAt(1), str.charAt(2), str.charAt(3));
                    } else {
                        str_return = str;
                    }
                    //str_return = "4자";
                    break;

                default:
                    break;
            }
        } catch (Exception e) {
            str_return = "null";
        }

        return str_return;
    }

    public int getBackgroundColor(int version) {
        switch(version) {
            case VERSION_NEW:
                return ContextCompat.getColor(context, R.color.lab_sep);

            case VERSION_OLD:
                return ContextCompat.getColor(context, R.color.old_footer_back);

            case VERSION_BLACK:
                return ContextCompat.getColor(context, R.color.black_background);

            case VERSION_BLUEGREEN:
                return ContextCompat.getColor(context, R.color.gt_sep);

            default:
                return ContextCompat.getColor(context, R.color.lab_sep);
        }
    }

    public int getListBackground(int version) {
        switch(version) {
            case VERSION_NEW:
                return R.drawable.item_main_bg;

            case VERSION_OLD:
                return R.drawable.item_main_bg_black;

            case VERSION_BLACK:
                return R.drawable.item_main_bg_black;

            case VERSION_BLUEGREEN:
                return R.drawable.item_main_bg_black;

            default:
                return R.drawable.item_main_bg;
        }
    }

    public int getTitleHeight() {
        return 80;
    }

    public int getTitleBarColor(int version, int line) {
        if(version == 0 || version == 1) {
            switch (line) {
                case LineManager.LINE_1:
                    return R.color.line_1;

                case LineManager.LINE_2:
                    return R.color.line_2;

                case LineManager.LINE_3:
                    return R.color.line_3;

                case LineManager.LINE_4:
                    return R.color.line_4;

                case LineManager.LINE_5:
                    return R.color.line_5;

                case LineManager.LINE_6:
                    return R.color.line_6;

                case LineManager.LINE_7:
                    return R.color.line_7;

                case LineManager.LINE_8:
                    return R.color.line_8;

                case LineManager.LINE_9:
                    return R.color.line_9;

                case LineManager.LINE_BUNDANG:
                case LineManager.LINE_SUIN:
                    return R.color.line_bs;

                case LineManager.LINE_SHINBUNDANG:
                    return R.color.line_sb;

                case LineManager.LINE_GONGHANG:
                    return R.color.line_gh;

                case LineManager.LINE_KYEONGCHOON:
                    return R.color.line_kc;

                case LineManager.LINE_KEUIJOONG:
                    return R.color.line_kj;

                default:
                    return R.color.colorPrimary;
            }
        } else if(version == 3) {
            return R.color.gt_titlebar;
        } else {
            return R.color.black_titlebar;
        }
    }

    public int getListHeight() {
        return mPref.getInt("iListSize", 66);
    }

    public int getListSepHeight() {
        return 25;
    }

    public int getStationFontSize() {
        return i_station_font_size;
    }

    public int getTrainFontSize() {
        return i_train_font_size;
    }

    public int getStationColor(int version) {
        switch(version) {
            case VERSION_NEW:
                return R.color.lab_station;

            case VERSION_OLD:
                return R.color.old_txt_station;

            case VERSION_BLACK:
                return R.color.black_station;

            case VERSION_BLUEGREEN:
                return R.color.gt_station;

            default:
                return R.color.lab_station;
        }
    }

    public int getStrokeColor(int version) {
        switch(version) {
            case VERSION_NEW:
                return R.color.lab_background;

            case VERSION_BLACK:
                return R.color.black_background;

            case VERSION_BLUEGREEN:
                return R.color.gt_background;

            default:
                return R.color.lab_background;
        }
    }

    public int getHighlightStrokeColor(int version) {
        switch(version) {
            case VERSION_NEW:
                return R.color.lab_highlight;

            case VERSION_BLACK:
                return R.color.black_highlight;

            case VERSION_BLUEGREEN:
                return R.color.gt_background;

            default:
                return R.color.lab_highlight;
        }
    }

    public int getListBackgroundColor(int version) {
        switch(version) {
            case VERSION_NEW:
                // 신 버전
                return R.color.lab_background;

            case VERSION_OLD:
                // 구 버전
                return R.color.old_list_back;

            case VERSION_BLACK:
                // 블랙
                return R.color.black_background;

            case VERSION_BLUEGREEN:
                return R.color.gt_background;

            default:
                return R.color.lab_background;
        }
    }

    public int getSubtextColor(int version) {
        switch(version) {
            case VERSION_NEW:
                // 신 버전
                return R.color.lab_station;

            case VERSION_OLD:
                // 구 버전
                return R.color.old_txt_subtext;

            case VERSION_BLACK:
                // 블랙
                return R.color.black_subtext;

            case VERSION_BLUEGREEN:
                return R.color.gt_subtext;

            default:
                return R.color.lab_station;
        }
    }

    public int getPrevStationColor(int version) {
        switch(version) {
            case VERSION_NEW:
                // 신 버전
                return R.color.old_txt_subtext;

            case VERSION_OLD:
                // 구 버전
                return R.color.old_txt_subtext;

            case VERSION_BLACK:
                // 블랙
                return R.color.black_subtext;

            case VERSION_BLUEGREEN:
                return R.color.gt_subtext;

            default:
                return R.color.lab_station;
        }
    }

    public int getFooterBackgroundColor(int version) {
        switch(version) {
            case VERSION_NEW:
                // 신 버전
                return R.color.lab_sep;

            case VERSION_OLD:
                // 구 버전
                return R.color.old_footer_back;

            case VERSION_BLACK:
                // 블랙
                return R.color.black_sep;

            case VERSION_BLUEGREEN:
                return R.color.gt_sep;

            default:
                return R.color.lab_sep;
        }
    }

    public int getDividerColor(int version) {
        switch(version) {
            case VERSION_NEW:
                return R.color.item_divider;

            case VERSION_BLACK:
                return R.color.black_divider;

            case VERSION_BLUEGREEN:
                return R.color.gt_divider;

            default:
                return R.color.item_divider;
        }
    }

    public int getTableDividerColor(int version) {
        switch(version) {
            case VERSION_NEW:
                return R.color.lab_back;

            case VERSION_BLACK:
                return R.color.black_sep;

            case VERSION_OLD:
                return R.color.old_footer_back;

            case VERSION_BLUEGREEN:
                return R.color.gt_sep;

            default:
                return R.color.lab_back;
        }
    }

    public int getHighlightColor(int version) {
        switch(version) {
            case VERSION_NEW:
                return R.color.lab_highlight;

            case VERSION_BLACK:
                return R.color.black_highlight;

            case VERSION_OLD:
                return R.color.old_highlight;

            case VERSION_BLUEGREEN:
                return R.color.gt_sep;

            default:
                return R.color.lab_highlight;
        }
    }

    public @Visibility int getDividerVisibility(int version) {
        switch(version) {
            case VERSION_NEW:
            case VERSION_BLACK:
            case VERSION_BLUEGREEN:
                return View.VISIBLE;

            case VERSION_OLD:
                return View.GONE;

            default:
                return View.VISIBLE;
        }
    }

    public int getListTheme(int version, int line, int direction) {
        if(version == VERSION_NEW || version == VERSION_BLACK || version == VERSION_BLUEGREEN) {
            switch(line) {
                case LineManager.LINE_2:
                case LineManager.LINE_3:
                case LineManager.LINE_4:
                case LineManager.LINE_5:
                case LineManager.LINE_6:
                case LineManager.LINE_7:
                case LineManager.LINE_8:
                case LineManager.LINE_BUNDANG:
                case LineManager.LINE_SHINBUNDANG:
                case LineManager.LINE_SUIN:
                case LineManager.LINE_KEUIJOONG:
                case LineManager.LINE_KYEONGCHOON:
                    if(direction == 0) {
                        return R.layout.item_single_left;
                    } else {
                        return R.layout.item_single_right;
                    }
                default:
                    if(direction == 0) {
                        return R.layout.item_double_left;
                    } else {
                        return R.layout.item_double_right;
                    }

            }
        } else {
            switch(line) {
                case LineManager.LINE_2:
                case LineManager.LINE_3:
                case LineManager.LINE_4:
                case LineManager.LINE_5:
                case LineManager.LINE_6:
                case LineManager.LINE_7:
                case LineManager.LINE_8:
                case LineManager.LINE_BUNDANG:
                case LineManager.LINE_SHINBUNDANG:
                case LineManager.LINE_SUIN:
                case LineManager.LINE_KEUIJOONG:
                case LineManager.LINE_KYEONGCHOON:
                    return R.layout.item_old_single_right;
                default:
                    return R.layout.item_old_double_right;

            }
        }
    }

    public int getTrainInfo(int version) {
        switch(version) {
            case VERSION_NEW:
                return R.drawable.bg_train_info;

            case VERSION_BLACK:
                return R.drawable.bg_train_info_black;

            case VERSION_BLUEGREEN:
                return R.drawable.bg_train_info;

            case VERSION_OLD:
                return 0;

            default:
                return R.drawable.bg_train_info;
        }
    }

    public int getTrainColor(int version) {
        switch(version) {
            case VERSION_NEW:
                return R.color.item_train;

            case VERSION_BLACK:
                return R.color.black_train;

            case VERSION_OLD:
                return R.color.old_txt_train;

            case VERSION_BLUEGREEN:
                return R.color.gt_train;

            default:
                return R.color.item_train;
        }
    }

    public int getTrainIcon(int version, int updown, boolean express) {
        if(!express) {
            // 일반
            if(updown == 0) {
                // 상행
                switch(version) {
                    case VERSION_NEW:
                        // 신 버전
                        return R.drawable.train_up_only;

                    case VERSION_OLD:
                        // 구 버전
                        return R.drawable.train_up;

                    case VERSION_BLACK:
                        // 블랙
                        return R.drawable.train_up_dark;

                    case VERSION_BLUEGREEN:
                        return R.drawable.train_up_only;

                    default:
                        return R.drawable.train_up_only;
                }
            } else {
                // 하행
                switch(version) {
                    case VERSION_NEW:
                        // 신 버전
                        return R.drawable.train_down_only;

                    case VERSION_OLD:
                        // 구 버전
                        return R.drawable.train_down;

                    case VERSION_BLACK:
                        // 블랙
                        return R.drawable.train_down_dark;

                    case VERSION_BLUEGREEN:
                        return R.drawable.train_down_only;

                    default:
                        return R.drawable.train_down_only;
                }
            }
        } else {
            // 급행
            if(updown == 0) {
                // 상행
                switch(version) {
                    case VERSION_NEW:
                        // 신 버전
                        return R.drawable.train_up_only_express;

                    case VERSION_OLD:
                        // 구 버전
                        return R.drawable.train_up_ex;

                    case VERSION_BLACK:
                        // 블랙
                        return R.drawable.train_up_dark_express;

                    case VERSION_BLUEGREEN:
                        return R.drawable.train_up_only_express;

                    default:
                        return R.drawable.train_up_only_express;
                }
            } else {
                // 하행
                switch(version) {
                    case VERSION_NEW:
                        // 신 버전
                        return R.drawable.train_down_only_express;

                    case VERSION_OLD:
                        // 구 버전
                        return R.drawable.train_down_ex;

                    case VERSION_BLACK:
                        // 블랙
                        return R.drawable.train_down_dark_express;

                    case VERSION_BLUEGREEN:
                        return R.drawable.train_down_only_express;

                    default:
                        return R.drawable.train_down_only_express;
                }
            }
        }
    }

    public int getTrain6Icon(int version, int updown) {
        if(updown == 0) {
            // 상행
            switch(version) {
                case VERSION_NEW:
                    // 신 버전
                    return R.drawable.train_up_only_express_6;

                case VERSION_OLD:
                    // 구 버전
                    return R.drawable.train_up_ex;

                case VERSION_BLACK:
                    // 블랙
                    return R.drawable.train_up_dark_express_6;

                case VERSION_BLUEGREEN:
                    return R.drawable.train_up_only_express_6;

                default:
                    return R.drawable.train_up_only_express_6;
            }
        } else {
            // 하행
            switch(version) {
                case VERSION_NEW:
                    // 신 버전
                    return R.drawable.train_down_only_express_6;

                case VERSION_OLD:
                    // 구 버전
                    return R.drawable.train_down_ex;

                case VERSION_BLACK:
                    // 블랙
                    return R.drawable.train_down_dark_express_6;

                case VERSION_BLUEGREEN:
                    return R.drawable.train_down_only_express_6;

                default:
                    return R.drawable.train_down_only_express_6;
            }
        }
    }

    public int getCircleLineColor(int line) {
        switch (line) {
            case LineManager.LINE_1:
                return R.drawable.icon_1001_cir;

            case LineManager.LINE_2:
                return R.drawable.icon_1002_cir;

            case LineManager.LINE_3:
                return R.drawable.icon_1003_cir;

            case LineManager.LINE_4:
                return R.drawable.icon_1004_cir;

            case LineManager.LINE_5:
                return R.drawable.icon_1005_cir;

            case LineManager.LINE_6:
                return R.drawable.icon_1006_cir;

            case LineManager.LINE_7:
                return R.drawable.icon_1007_cir;

            case LineManager.LINE_8:
                return R.drawable.icon_1008_cir;

            case LineManager.LINE_9:
                return R.drawable.icon_1009_cir;

            case LineManager.LINE_BUNDANG:
            case LineManager.LINE_SUIN:
                return R.drawable.icon_1075_cir;

            case LineManager.LINE_SHINBUNDANG:
                return R.drawable.icon_1077_cir;

            case LineManager.LINE_GONGHANG:
                return R.drawable.icon_1065_cir;

            case LineManager.LINE_KYEONGCHOON:
                return R.drawable.icon_1067_cir;

            case LineManager.LINE_KEUIJOONG:
                return R.drawable.icon_1063_cir;

            default:
                return R.drawable.icon_1002_cir;
        }
    }

    public int getStationBackImage(int line) {
        switch (line) {
            case LineManager.LINE_1:
                return R.drawable.icon_1001_sta;

            case LineManager.LINE_2:
                return R.drawable.icon_1002_sta;

            case LineManager.LINE_3:
                return R.drawable.icon_1003_sta;

            case LineManager.LINE_4:
                return R.drawable.icon_1004_sta;

            case LineManager.LINE_5:
                return R.drawable.icon_1005_sta;

            case LineManager.LINE_6:
                return R.drawable.icon_1006_sta;

            case LineManager.LINE_7:
                return R.drawable.icon_1007_sta;

            case LineManager.LINE_8:
                return R.drawable.icon_1008_sta;

            case LineManager.LINE_9:
                return R.drawable.icon_1009_sta;

            case LineManager.LINE_BUNDANG:
            case LineManager.LINE_SUIN:
                return R.drawable.icon_1075_sta;

            case LineManager.LINE_SHINBUNDANG:
                return R.drawable.icon_1077_sta;

            case LineManager.LINE_GONGHANG:
                return R.drawable.icon_1065_sta;

            case LineManager.LINE_KYEONGCHOON:
                return R.drawable.icon_1067_sta;

            case LineManager.LINE_KEUIJOONG:
                return R.drawable.icon_1063_sta;

            default:
                return R.drawable.icon_1002_sta;
        }
    }

    public int getStationIcon(int line, boolean transfer) {
        if(!transfer) {
            switch (line) {
                case LineManager.LINE_1:
                    return R.drawable.icon_1001_norm;

                case LineManager.LINE_2:
                    return R.drawable.icon_1002_norm;

                case LineManager.LINE_3:
                    return R.drawable.icon_1003_norm;

                case LineManager.LINE_4:
                    return R.drawable.icon_1004_norm;

                case LineManager.LINE_5:
                    return R.drawable.icon_1005_norm;

                case LineManager.LINE_6:
                    return R.drawable.icon_1006_norm;

                case LineManager.LINE_7:
                    return R.drawable.icon_1007_norm;

                case LineManager.LINE_8:
                    return R.drawable.icon_1008_norm;

                case LineManager.LINE_9:
                    return R.drawable.icon_1009_norm;

                case LineManager.LINE_BUNDANG:
                case LineManager.LINE_SUIN:
                    return R.drawable.icon_1075_norm;

                case LineManager.LINE_SHINBUNDANG:
                    return R.drawable.icon_1077_norm;

                case LineManager.LINE_GONGHANG:
                    return R.drawable.icon_1065_norm;

                case LineManager.LINE_KYEONGCHOON:
                    return R.drawable.icon_1067_norm;

                case LineManager.LINE_KEUIJOONG:
                    return R.drawable.icon_1063_norm;

                default:
                    return R.drawable.icon_1002_norm;
            }
        } else {
            switch (line) {
                case LineManager.LINE_1:
                    return R.drawable.icon_1001_tran;

                case LineManager.LINE_2:
                    return R.drawable.icon_1002_tran;

                case LineManager.LINE_3:
                    return R.drawable.icon_1003_tran;

                case LineManager.LINE_4:
                    return R.drawable.icon_1004_tran;

                case LineManager.LINE_5:
                    return R.drawable.icon_1005_tran;

                case LineManager.LINE_6:
                    return R.drawable.icon_1006_tran;

                case LineManager.LINE_7:
                    return R.drawable.icon_1007_tran;

                case LineManager.LINE_8:
                    return R.drawable.icon_1008_tran;

                case LineManager.LINE_9:
                    return R.drawable.icon_1009_tran;

                case LineManager.LINE_BUNDANG:
                case LineManager.LINE_SUIN:
                    return R.drawable.icon_1075_tran;

                case LineManager.LINE_SHINBUNDANG:
                    return R.drawable.icon_1077_tran;

                case LineManager.LINE_GONGHANG:
                    return R.drawable.icon_1065_tran;

                case LineManager.LINE_KYEONGCHOON:
                    return R.drawable.icon_1067_tran;

                case LineManager.LINE_KEUIJOONG:
                    return R.drawable.icon_1063_tran;

                default:
                    return R.drawable.icon_1002_tran;
            }
        }
    }

    public String getCircleLineText(int line) {
        switch (line) {
            case LineManager.LINE_1:
                return "1";

            case LineManager.LINE_2:
                return "2";

            case LineManager.LINE_3:
                return "3";

            case LineManager.LINE_4:
                return "4";

            case LineManager.LINE_5:
                return "5";

            case LineManager.LINE_6:
                return "6";

            case LineManager.LINE_7:
                return "7";

            case LineManager.LINE_8:
                return "8";

            case LineManager.LINE_9:
                return "9";

            case LineManager.LINE_BUNDANG:
                return "분당";

            case LineManager.LINE_SUIN:
                return "수인";

            case LineManager.LINE_SHINBUNDANG:
                return "신분당";

            case LineManager.LINE_GONGHANG:
                return "공항";

            case LineManager.LINE_KYEONGCHOON:
                return "경춘";

            case LineManager.LINE_KEUIJOONG:
                return "경의 · 중앙";

            default:
                return "2";
        }
    }
}
