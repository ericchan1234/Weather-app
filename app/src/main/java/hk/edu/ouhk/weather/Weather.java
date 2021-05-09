package hk.edu.ouhk.weather;

import android.util.Log;

import java.sql.Timestamp;
import java.util.Date;

public class Weather {
    private static String temp;
    private static String temp_min;
    private static String temp_max;
    private static String humidity;
    private static String pressure;
    private static String feels_like;
    private static String wind_speed;
    private static String wind_degree;
    private static String location;
    private static String dt;
    private static String timezone;
    private static Date currDate;

    public static void WeatherStats(String temp2, String feels_like2, String temp_min2, String temp_max2, String pressure2, String humidity2, String wind_speed2, String wind_degree2, String location2, String dt2, String timezone2){
        temp = temp2;
        dt = dt2;
        feels_like = feels_like2;
        humidity = humidity2;
        location = location2;
        pressure = pressure2;
        temp_max = temp_max2;
        temp_min = temp_min2;
        wind_speed = wind_speed2;
        wind_degree = wind_degree2;
        timezone = timezone2;
        currDate = new Date(System.currentTimeMillis());
    }
    public static String getLocation(){
        return location;
    }
    public static String getTemp(){
        return doubleTo1FP(temp);
    }
    public static String getTemp_min() {
        return doubleTo1FP(temp_min);
    }
    public static String getTemp_max(){
        return doubleTo1FP(temp_max);
    }
    public static String getHumidity(){
        return humidity;
    }
    public static String getPressure(){
        return pressure;
    }
    public static String getFeels_like(){
        return doubleTo1FP(feels_like);
    }
    public static String getWind_speed(){
        return wind_speed;
    }
    public static String getWind_degree(){
        return wind_degree;
    }
    public static String getTimeZone(){
        return timezone;
    }

    public static String doubleTo1FP(String num){
        return String.valueOf((Math.floor(Double.parseDouble(num)))/ 10);
    }
    public static int getCurrHour(){
        return currDate.getHours();
    }
    public static String getCurrTime(){
        int hour = currDate.getHours();
        String time;
        if (currDate.getMinutes() < 10) {
            time = String.valueOf(hour) + ":0" + String.valueOf(currDate.getMinutes());
        } else {
            time = String.valueOf(hour) + ":" + String.valueOf(currDate.getMinutes());
        }
        return time;
    }

    public static String getTime(){
       if (dt == null)
           return null;
       Date date = new Date(Long.parseLong(dt) * 1000);
       int hour = date.getHours();
        String time;
       if (date.getMinutes() < 10) {
           time = String.valueOf(hour) + ":0" + String.valueOf(date.getMinutes());
       } else {
           time = String.valueOf(hour) + ":" + String.valueOf(date.getMinutes());
       }
       return time;
    }
    public static String getDate(){
        if(dt == null)
            return null;
        Date date = new Date(Long.parseLong(dt) * 1000);
        String Date = String.valueOf((date.getYear()+ 1900)) + "/" + String.valueOf(date.getMonth()+1) + "/" + String.valueOf(date.getDate());;
        return Date;
    }
}
