package com.example.lifecircle.Utils;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LisaiZhang on 2016/5/4.
 */
public class UrlUtils {

    /**
     * insert '\' into string where there is '"'
     * @param string:String
     * @return query string
     */
    public static String StringToQuery(String string){
        char charset[] = string.toCharArray();
        List<Character> result = new ArrayList<Character>();
        int length = string.length();
        for(int i = 0;i < length;i++){
            if(charset[i] == '\"'){
                result.add('\\');
            }
            result.add(charset[i]);
        }
        return result.toString();
    }
//"time":
// {"nanos":0,
// "time":1462620012000,
// "minutes":20,
// "seconds":12,
// "hours":19,
// "month":4,
// "year":116,
// "timezoneOffset":-480,
// "day":6,
// "date":7}
//int theYear, int theMonth, int theDate, int theHour, int theMinute, int theSecond, int theNano
    public static Timestamp JsonToTimestamp(JSONObject jsonObject) throws Exception{
        Timestamp time =  new Timestamp(jsonObject.getInt("year"),jsonObject.getInt("month"),jsonObject.getInt("date"),
                jsonObject.getInt("hours"),jsonObject.getInt("minutes"),jsonObject.getInt("seconds"),jsonObject.getInt("nanos"));
        return time;
    }

    public static String NumToDay(int num){
        String day = new String();
        switch (num){
            case 0:
                day = "星期日";
                break;
            case 1:
                day = "星期一";
                break;
            case 2:
                day = "星期二";
                break;
            case 3:
                day = "星期三";
                break;
            case 4:
                day = "星期四";
                break;
            case 5:
                day = "星期五";
                break;
            case 6:
                day = "星期六";
                break;
        }
        return day;
    }
}
