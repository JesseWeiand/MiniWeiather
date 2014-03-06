package com.xiantos.miniWeather;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Created by Zos on 2/28/14.
 */

public class JSONParser {

    static InputStream is = null;
    static JSONObject jsonObject = null;
    static String json = "";

    public JSONParser(){

    }
    public JSONObject getJSONFromUrl(String url){
        try{
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        } catch(IOException e){
            e.printStackTrace();
        }
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader( is, "iso-8859-1"),8);
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null){
                stringBuilder.append(line + "\n");
            }
            is.close();
            json = stringBuilder.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            jsonObject = new JSONObject(json);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return jsonObject;
    }


}
