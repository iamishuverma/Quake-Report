package com.example.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<Earthquake>>
{
    String query;
    public EarthquakeLoader (Context context, String query)
    {
        super(context);
        this.query = query;
    }
    @Override
    public ArrayList<Earthquake> loadInBackground() {

        ArrayList<Earthquake> arrayList = new ArrayList<>();
        try {
            URL url = new URL(query);                                                               //Creating an URL object holding our URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");                                                     //Setting Request Method (GET, POST, etc.)
            connection.setReadTimeout(15000);                                                       //Setting Read Time Out
            connection.setConnectTimeout(15000);                                                    //Setting Connection Time Out
            connection.connect();                                                                   //Connect to our url

            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String inputLine;

            while ((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }

            reader.close();
            streamReader.close();

            String result = stringBuilder.toString();

            //     result = result.substring(result.indexOf('{'),result);

            arrayList = getEarthquakeArrayList(result);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Ishu", "Verma");
            arrayList = null;
        }
        return arrayList;
    }

    private static ArrayList<Earthquake> getEarthquakeArrayList(String result)
    {
        ArrayList<Earthquake> list = new ArrayList<>();
        try
        {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("features");
            for(int i=0; i<jsonArray.length(); ++i)
            {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                JSONObject jsonObject2 = jsonObject1.getJSONObject("properties");
                double mag = jsonObject2.getDouble("mag");
                String place = jsonObject2.getString("place");
                long time = jsonObject2.getLong("time");
                list.add(new Earthquake(mag, place, time));
            }
        }
        catch (JSONException e)
        {
            Log.e(result,"blabla");
        }
        return list;
    }
}
