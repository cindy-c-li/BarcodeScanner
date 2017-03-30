package com.example.chen.barcodescanner;

/**
 * Created by chen on 2/7/2017.
 */


import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

    private String query;
    private ListView listView;
    private ArrayAdapter<String> listAdapter;
    private Button walmartBuy;

    private JSONObject item;

    private List<String> items;

    public JSONObject getItem() {
        return item;
    }


    public RetrieveFeedTask(String query, ListView listView, ArrayAdapter arrayAdapter, Button walmartBuy) {
        this.query = query;
        this.listView = listView;
        this.listAdapter = arrayAdapter;
        this.listView.setAdapter(listAdapter);
        this.items = new ArrayList<String>();
        this.walmartBuy = walmartBuy;
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

    }

    @Override
    protected void onPostExecute(String s) {
        if (s != null) {

            try {
                JSONObject object = (JSONObject) new JSONTokener(s).nextValue();
                JSONArray items = object.getJSONArray("items");
                this.item = items.getJSONObject(0);


                int requestID = item.getInt("itemId");
                String name = item.getString("name");
                String price = item.getString("salePrice");
                String addToCartUrl = item.getString("addToCartUrl");
                listView.setTag(addToCartUrl);
                StringBuilder item = new StringBuilder(requestID+"");
                item.append(" ").append(name).append(" ").append(price);

                //make buy now button visiable
                walmartBuy.setVisibility(View.VISIBLE);

                this.items.add(item.toString());

            } catch (JSONException e) {
                e.printStackTrace();

            } catch (Exception e) {
                this.items.add("No item with this UPC found in Walmart");
            }

            listAdapter.clear();
            listAdapter.addAll(this.items);

        }


    }
}
