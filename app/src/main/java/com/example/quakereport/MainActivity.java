package com.example.quakereport;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.view.View;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.nio.charset.Charset;

import android.util.Log;

public class MainActivity extends AppCompatActivity {

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

    private void updateUI(ArrayList<Earthquake> arrayList)
    {
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        MyAdapter adapter = new MyAdapter(this,arrayList);
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String query = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5&limit=10";  //this query will generate JSON response.

        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(query);
    }
    private class EarthquakeAsyncTask extends AsyncTask<String, Void, ArrayList<Earthquake>>     //Async is made by dev. team to make work easy on the background thread.
    {
        @Override
        protected ArrayList<Earthquake> doInBackground(String... tquery) {                               //This method runs in background thread.
            ArrayList<Earthquake> arrayList = new ArrayList<>();

            String query = tquery[0];
            try
            {
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

                while((inputLine = reader.readLine()) != null)
                    {
                        stringBuilder.append(inputLine);
                    }

                reader.close();
                streamReader.close();

                String result = stringBuilder.toString();

           //     result = result.substring(result.indexOf('{'),result);

                arrayList = getEarthquakeArrayList(result);
                }
            catch (IOException e)
            {
            e.printStackTrace();
            Log.e("Ishu","Verma");
            arrayList = null;
            }
            return arrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<Earthquake> arrayList) {                             //This method runs in UI thread.
            super.onPostExecute(arrayList);

            updateUI(arrayList);
        }
    }
}
