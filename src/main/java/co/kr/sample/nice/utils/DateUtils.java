package co.kr.sample.nice.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    /* * 시간 형식(yyyyMMdd) */
    public static final String DATE_PATTERN = "yyyyMMdd";
    /* * 시간 형식(yyyy-MM-dd HH:mm:ss) */
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String HAPPYMONEY_PATTERN = "yyyyMMddHHmmss";
    public static final String SSGPAY_PATTERN = "yyyyMMddHHmmss";
    public static final String SOIL_PATTERN = "yyyyMMddHHmmss";
    public static final String BOOKANDLIFE_PATTERN = "yyyyMMddHHmmssSSS";
    public static final String FOR_ORDER_ID_PATTERN = "yyyyMMddHHmmssSSS";

    public static final String ID_DATETIME_PATTERN = "yyyyMMddHHmmss";

    public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    // 성수기(1,2,7,8,9,12) 확인
    public static Boolean isPeakSeason() {
        Integer month = (new Date()).getMonth() + 1;

        switch (month) {
            case 1:
            case 2:
            case 7:
            case 8:
            case 9:
            case 12:
                return true;
            case 3:
            case 4:
            case 5:
            case 6:
            case 10:
            case 11:
            default:
                return false;
        }
    }
}
