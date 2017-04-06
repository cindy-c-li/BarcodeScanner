package com.example.webservice;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.example.helper.SignedRequestsHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anu on 12-04-2017.
 */

public class AmazonPurchaseUtility extends AsyncTask<Void, Void, Model> {

    private Model itemToBuy;
    private View view;

    public void setEnvironement(Model itemToBuy, View view) {
        this.itemToBuy = itemToBuy;
        this.view = view;
    }

    @Override
    protected Model doInBackground(Void... v) {
        Model response = null;
        Map<String, String> params = new HashMap<>();
        params.put("AssociateTag", "thrifts0c-20");
        params.put("Operation", "CartCreate");
        params.put("Item.1.ASIN", itemToBuy.getItemId());
        params.put("Item.1.Quantity", "1");
        JSONObject object = SignedRequestsHelper.sendRequest(params);
        if (object != null) {
            Log.d("AmazonService", object.toString());
            try {
                JSONObject cartCreate = object.getJSONObject("CartCreateResponse");
                JSONObject cart = cartCreate.getJSONObject("Cart");
                String purchaseURL = cart.getString("PurchaseURL");
                itemToBuy.setItemPurchaseURL(purchaseURL);
                response = itemToBuy;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    protected void onPostExecute(Model model) {
        String url = model.getItemPurchaseURL();
        Log.d("place order", url);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setData(Uri.parse(url));
        view.getContext().startActivity(i);
    }
}
