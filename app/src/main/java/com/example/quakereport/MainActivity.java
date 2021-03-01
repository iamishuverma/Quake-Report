package com.example.quakereport;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;

import android.content.Context;
import android.content.Loader;
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

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Earthquake>>{

    public final String query = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5&limit=10";  //this query will generate JSON response.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLoaderManager().initLoader(1, null, this).forceLoad();
    }
    @Override
    public Loader<ArrayList<Earthquake>> onCreateLoader(int id, Bundle args)
    {
        return new EarthquakeLoader(MainActivity.this, query);
    }
    @Override
    public void onLoadFinished(Loader<ArrayList<Earthquake>> loader, ArrayList<Earthquake> arrayList)
    {
        updateUI(arrayList);
    }
    @Override
    public void onLoaderReset(Loader<ArrayList<Earthquake>> loader)
    {
        updateUI(new ArrayList<Earthquake>());
    }

    private void updateUI(ArrayList<Earthquake> arrayList)
    {
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        MyAdapter adapter = new MyAdapter(this, arrayList);
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.emptyElement));
    }

//    private class EarthquakeAsyncTask extends AsyncTask<String, Void, ArrayList<Earthquake>>     //Async is made by dev. team to make work easy on the background thread.
//    {
//        @Override
//        protected ArrayList<Earthquake> doInBackground(String... tquery) {                               //This method runs in background thread.
//            ArrayList<Earthquake> arrayList = new ArrayList<>();
//
//            String query = tquery[0];
//            try
//            {
//                URL url = new URL(query);                                                               //Creating an URL object holding our URL
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("GET");                                                     //Setting Request Method (GET, POST, etc.)
//                connection.setReadTimeout(15000);                                                       //Setting Read Time Out
//                connection.setConnectTimeout(15000);                                                    //Setting Connection Time Out
//                connection.connect();                                                                   //Connect to our url
//
//                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8"));
//                BufferedReader reader = new BufferedReader(streamReader);
//                StringBuilder stringBuilder = new StringBuilder();
//                String inputLine;
//
//                while((inputLine = reader.readLine()) != null)
//                    {
//                        stringBuilder.append(inputLine);
//                    }
//
//                reader.close();
//                streamReader.close();
//
//                String result = stringBuilder.toString();
//
//           //     result = result.substring(result.indexOf('{'),result);
//
//                arrayList = getEarthquakeArrayList(result);
//                }
//            catch (IOException e)
//            {
//            e.printStackTrace();
//            Log.e("Ishu","Verma");
//            arrayList = null;
//            }
//            return arrayList;
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<Earthquake> arrayList) {                             //This method runs in UI thread.
//            super.onPostExecute(arrayList);
//
//            updateUI(arrayList);
//        }
//    }

}
