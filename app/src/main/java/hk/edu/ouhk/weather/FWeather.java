package hk.edu.ouhk.weather;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class FWeather {
    public static String date = "Date";
    public static String temp = "Temp";
    public static String icon = "Icon";

    public static ArrayList<HashMap<String, String>> foreweather = new ArrayList<>();

    public static String dtToDate(String dt){
        if(dt == null)
            return null;
        Date date = new Date(Long.parseLong(dt) * 1000);
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM (EEE)", Locale.ENGLISH);
        String Date = formatter.format(date);
        return Date;
    }

    public static String doubleTo1FP(String num){
        return String.valueOf((Math.floor(Double.parseDouble(num)))/ 10);
    }

    public static boolean clearData(){
        foreweather.clear();
        return true;
    }
    public static void addWeatherData(String dt, String tempmin, String tempmax, String i){
        HashMap<String, String> w = new HashMap<>();
        String temp_range = "";
        if(weatherforecast.isCelsius) {
            temp_range = doubleTo1FP(tempmin) + "°C - " + doubleTo1FP(tempmax) + "°C";
        } else{
            temp_range = ConvertCTOF(doubleTo1FP(tempmin)) + "°F - " + ConvertCTOF(doubleTo1FP(tempmax)) + "°F";
        }
        w.put(date, dtToDate(dt));
        w.put(temp, temp_range);
        w.put(icon, i);

        foreweather.add(w);
    }
    public static String ConvertCTOF(String num){
        double f = Double.valueOf(num) * 1.8 +32;
        return String.valueOf((Math.floor(f*10))/ 10);
    }
}
