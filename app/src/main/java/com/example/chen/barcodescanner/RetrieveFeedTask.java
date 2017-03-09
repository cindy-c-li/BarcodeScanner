package com.example.chen.barcodescanner;

/**
 * Created by chen on 2/7/2017.
 */


import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

    private String query;
    private TextView textView;

    private JSONObject item;

    public JSONObject getItem() {
        return item;
    }


    public RetrieveFeedTask(String query, TextView textView) {
        this.query = query;
        this.textView = textView;
    }


    @Override
    protected String doInBackground(Void... params) {
        try {
            URL url = new URL(query);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            Log.d("RetrieveFeedTask", "checkpoint1");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();

            Log.d("RetrieveFeedTask", stringBuilder.toString());
            return stringBuilder.toString();

        } catch (Exception e) {
            Log.d("RetrieveFeedTask", e.toString());
            return e.toString();
        }

        //return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if (s != null) {
            //textView.setText(s);

            try {
                JSONObject object = (JSONObject) new JSONTokener(s).nextValue();
                JSONArray items = object.getJSONArray("items");
                this.item = items.getJSONObject(0);


                int requestID = item.getInt("itemId");
                String likelihood = item.getString("name");
                String price = item.getString("salePrice");

                textView.setText(requestID + " " + likelihood + " " + price);


            } catch (JSONException e) {
                e.printStackTrace();

            } catch (Exception e) {
                textView.setText("No item with this UPC found in Walmart");
            }
        }


    }
}
