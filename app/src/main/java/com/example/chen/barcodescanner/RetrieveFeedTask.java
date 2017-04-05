package com.example.chen.barcodescanner;

/**
 * Created by chen on 2/7/2017.
 */


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.chen.barcodescanner.model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RetrieveFeedTask extends AsyncTask<Void, Void, List<Model>> {

    private String query;
    private ListView listView;
    private Context context;

    private ItemListAdapter listAdapter;

    private ImageView imageView;

    public RetrieveFeedTask(Context context, String query, ListView listView) {
        this.query = query;
        this.listView = listView;
        this.context = context;
    }


    @Override
    protected List<Model> doInBackground(Void... params) {
        ArrayList<Model> items = new ArrayList<>();
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

            String x = stringBuilder.toString();
            JSONObject object = new JSONObject(x);//.nextValue();

            JSONArray itemsJSON = object.getJSONArray("items");
            JSONObject item = itemsJSON.getJSONObject(0);

            Model m = new Model();

            m.setItemPurchaseURL(item.getString("addToCartUrl"));
            m.setItemId(item.getInt("itemId"));
            m.setName(item.getString("name"));
            m.setSalePrice(item.getString("salePrice"));
            m.setThumbnailImage(item.getString("thumbnailImage"));

            items.add(m);
        } catch (JSONException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();

        }
        return items;
    }

    @Override
    protected void onPostExecute(List<Model> models) {
        super.onPostExecute(models);
        ItemListAdapter adapter = new ItemListAdapter(context, R.layout.row, models);
        listView.setAdapter(adapter);

    }
}
