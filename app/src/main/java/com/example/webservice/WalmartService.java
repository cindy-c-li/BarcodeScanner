package com.example.webservice;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Anand Rawat on 02-04-2017.
 */

public class WalmartService extends Service {
    @Override
    public Model dispatchRequest() {
        Model response = null;
        final String query = String.format("%s%s&upc=%s", ServiceConstant.WALMART_ENDPOINT, ServiceConstant.WALMART_API_KEY, getUpc());

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

            Log.d("WalmartService", stringBuilder.toString());
            JSONObject object = (JSONObject) new JSONTokener(stringBuilder.toString()).nextValue();
            JSONArray items = object.getJSONArray("items");
            JSONObject item = items.getJSONObject(0);
            response = new Model();
            response.setName(item.getString("name"));
            response.setSalePrice("$" + item.getString("salePrice"));
            response.setType(ServiceType.WALMART);
            response.setItemPurchaseURL(item.getString("addToCartUrl"));
            response.setItemUPC(item.getString("itemId"));
            response.setThumbnailImage(item.getString("thumbnailImage"));

        } catch (JSONException e) {
            e.printStackTrace();

        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            Log.d("RetrieveFeedTask", e.toString());
            //return e.toString();
        }

        return response;
    }
}
