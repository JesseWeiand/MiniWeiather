package com.xiantos.miniWeather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Zos on 2/26/14.
 */

// http://api.wunderground.com/api/c9b43e353b33088e/conditions/q/ND/Fargo.json
// http://api.wunderground.com/api/c9b43e353b33088e/forecast/q/ND/Fargo.json
// http://api.wunderground.com/api/c9b43e353b33088e/geolookup/q/37.776289,-122.395234.json
public class WundergroundAPI{

    private static final String TAG_CUR_OBV = "current_observation";
    private static final String TAG_ESTIMATED = "estimated";
    private static final String TAG_TEMP_F = "temp_f";
    private static final String TAG_WIND_DIR = "wind_dir";
    private static final String TAG_WIND_SPEED = "wind_mph";
    private static final String TAG_WINDCHILL = "windchill_f";

    //forecast fields
    private static final String TAG_FORECAST = "forecast";
    private static final String TAG_SIMPLEFORECAST = "simpleforecast";
    private static final String TAG_FORECASTDAY = "forecastday";
    private static final String TAG_FAHRENHEIT = "fahrenheit";
    private static final String TAG_FORE_HIGH = "high";
    private static final String TAG_FORE_LOW = "low";

    private static final String base_url = "http://api.wunderground.com/api/c9b43e353b33088e/";
    private static final String TAG_DATE = "date";
    private static final String TAG_HIGH = "high";
    private static final String TAG_LOW = "low";
    private static final String TAG_AVEWIND = "avewind";
    private static final String TAG_DAY = "day";
    private static final String TAG_MONTH = "month";

    //Variables used to store weather information
    //GPS coordinates
    double gpsLatitude;
    double gpsLongitude;
    boolean useGps;

    //Current conditions
    String cityName;
    String stateName;
    String windDir;
    String windChill;
    int curTemp;
    int windSpeed;
    int loTemp;
    int highTemp;

    //forecasted conditions
    int[] day = new int[3];
    int[] month = new int[3];
    int[] forecastHighTemp = new int[3];
    int[] forecastLowTemp = new int[3];
    int[] forecastPrecip = new int[3];
    int[] forecastWind = new int[3];
    String[] forecastConditions = new String[3];

    JSONArray curObv = null;

    public void readConditions(JSONObject json){
        try{
            JSONObject jobj = json.getJSONObject(TAG_CUR_OBV);
            int curTemp = jobj.getInt(TAG_TEMP_F);
            int windSpeed = jobj.getInt(TAG_WIND_SPEED);
            String windDir = jobj.getString(TAG_WIND_DIR);
            String windChill = jobj.getString(TAG_WINDCHILL);


            setCurTemp(curTemp);
            setWindSpeed(windSpeed);
            setWindDir(windDir);
            setWindChill(windChill);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void readForecast(JSONObject json){
        try{
            //curObv = json.getJSONArray(TAG_SIMPLEFORECAST);

            //What does this line do? change index perhaps if needed?
            //JSONObject jobj = curObv.getJSONObject(1);

            //Begin storing strings into variables
            //weather.setCurTemp(jsonObject.getDouble(TAG_TEMP_F));
            //weather.setCurTemp(json.getDouble(TAG_TEMP_F));

            JSONObject jobj;
            JSONObject childObj;
            JSONArray jArr;
            jobj = json.getJSONObject(TAG_FORECAST);
            jobj = jobj.getJSONObject(TAG_SIMPLEFORECAST);
            jArr = jobj.getJSONArray("forecastday");

            //for (int i=0; i < jArr.length(); i++){
            for (int i=0; i < 3; i++){
                childObj = jArr.getJSONObject(i);
                readForecastDay(childObj,i);
            }


            //jArr = json.getJSONArray(TAG_FORECASTDAY);

/*
            int curTemp = jobj.getInt(TAG_TEMP_F);
            int windSpeed = jobj.getInt(TAG_WIND_SPEED);
            String windDir = jobj.getString(TAG_WIND_DIR);
            String windChill = jobj.getString(TAG_WINDCHILL);

            setCurTemp(curTemp);
            setWindSpeed(windSpeed);
            setWindDir(windDir);
            setWindChill(windChill);*/

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void readForecastDay(JSONObject childObj, int position){
        JSONObject dateObj;
        JSONObject highObj;
        JSONObject lowObj;
        JSONObject windObj;
        int day;
        int month;
        int high;
        int low;
        int aveWind;
        String conditions;

        try {
            dateObj = childObj.getJSONObject(TAG_DATE);
            highObj = childObj.getJSONObject(TAG_HIGH);
            lowObj = childObj.getJSONObject(TAG_LOW);
            windObj = childObj.getJSONObject(TAG_AVEWIND);

            day = dateObj.getInt(TAG_DAY);
            month = dateObj.getInt(TAG_MONTH);
            high = Integer.parseInt(highObj.getString("fahrenheit"));
            low = Integer.parseInt(lowObj.getString("fahrenheit"));
            conditions = childObj.getString("conditions");
            aveWind = windObj.getInt("mph");

        }
        catch(JSONException e){
            e.printStackTrace();
            day = -1;
            month = -1;
            high = -1;
            low = -1;
            conditions = "--";
            aveWind = -1;
        }

        setDay(position, day);
        setMonth(position, month);
        setForecastHighTemp(position, high);
        setForecastLowTemp(position, low);
        setForecastConditions(position, conditions);
        setForecastWind(position, aveWind);
    }





    public String getCurrentUrl(int type) {
        //Type 1 will be the current conditions, while 2 will be forecast
        if (type == 1){
            if (isUseGps()){
                return base_url + "geolookup/q/" + gpsLatitude + "," + gpsLongitude;
            }
            else{
                return base_url + "conditions/q/" + stateName + "/" + cityName + ".json";
            }
        }else{
            return base_url + "forecast/q/" + stateName + "/" + cityName + ".json";
        }
    }

    public void lookUpLocation(){

    }

    public void getCurrentWind(){

    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public int getCurTemp() {
        return curTemp;
    }

    public void setCurTemp(Integer curTemp) {
        this.curTemp = curTemp;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getLoTemp() {
        return loTemp;
    }

    public void setLoTemp(int loTemp) {
        this.loTemp = loTemp;
    }

    public int getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(int highTemp) {
        this.highTemp = highTemp;
    }

    public String getWindDir() {
        return windDir;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }

    public String getWindChill() {
        return windChill;
    }

    public void setWindChill(String windChill) {
        this.windChill = windChill;
    }



    public int[] getDay() {
        return day;
    }

    public void setDay(int[] day) {
        this.day = day;
    }

    public void setDay(int pos, int value) {
        this.day[pos] = value;
    }

    public int[] getMonth() {
        return month;
    }

    public void setMonth(int[] month) {
        this.month = month;
    }

    public void setMonth(int pos, int value) {
        this.month[pos] = value;
    }

    public int[] getForecastHighTemp() {
        return forecastHighTemp;
    }

    public void setForecastHighTemp(int[] forecastHighTemp) {
        this.forecastHighTemp = forecastHighTemp;
    }

    public void setForecastHighTemp(int pos, int value) {
        this.forecastHighTemp[pos] = value;
    }

    public int[] getForecastPrecip() {
        return forecastPrecip;
    }

    public void setForecastPrecip(int[] forecastPrecip) {
        this.forecastPrecip = forecastPrecip;
    }

    public String[] getForecastConditions() {
        return forecastConditions;
    }

    public void setForecastConditions(String[] forecastConditions) {
        this.forecastConditions = forecastConditions;
    }

    public void setForecastConditions(int pos, String value) {
        this.forecastConditions[pos] = value;
    }

    public int[] getForecastLowTemp() {
        return forecastLowTemp;
    }

    public void setForecastLowTemp(int[] forecastLowTemp) {
        this.forecastLowTemp = forecastLowTemp;
    }

    public void setForecastLowTemp(int pos, int value) {
        this.forecastLowTemp[pos] = value;
    }

    public int[] getForecastWind() {
        return forecastWind;
    }

    public void setForecastWind(int[] forecastWind) {
        this.forecastWind = forecastWind;
    }

    public void setForecastWind(int pos, int value) {
        this.forecastWind[pos] = value;
    }

    public double getGpsLongitude() {
        return gpsLongitude;
    }

    public void setGpsLongitude(double gpsLongitude) {
        this.gpsLongitude = gpsLongitude;
    }

    public double getGpsLatitude() {
        return gpsLatitude;
    }

    public void setGpsLatitude(double gpsLatitude) {
        this.gpsLatitude = gpsLatitude;
    }

    public boolean isUseGps() {
        return useGps;
    }

    public void setUseGps(boolean useGps) {
        this.useGps = useGps;
    }


}


