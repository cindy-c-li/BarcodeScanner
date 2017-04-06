package com.example.webservice;

import android.util.Log;

import com.example.helper.SignedRequestsHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Annad Rawat on 06-04-2017.
 */

public class AmazonService extends Service {

    @Override
    public Model dispatchRequest() {
        Model response = null;
        Map<String, String> params = new HashMap<>();
        params.put("AssociateTag", "thrifts0c-20");
        params.put("Operation", "ItemLookup");
        params.put("IdType", "UPC");
        params.put("ItemId", getUpc());
        params.put("SearchIndex", "All");
        params.put("ResponseGroup", "Medium");
        params.put("Condition", "New");
        params.put("Availability", "Available");
        params.put("IncludeReviewsSummary", "False");
        params.put("RelatedItemPage", "0");
        params.put("VariationPage", "1");
        params.put("Sort", "reviewrank");

        JSONObject object = SignedRequestsHelper.sendRequest(params);
        if (object != null) {
            Log.d("AmazonService", object.toString());
            JSONObject itemsObject = null;
            try {
                itemsObject = object.getJSONObject("ItemLookupResponse").getJSONObject("Items");
                JSONArray itemsArray = itemsObject.getJSONArray("Item");
                JSONObject minPriceItem = (JSONObject) itemsArray.get(0);
                int minPrice = minPriceItem.getJSONObject("OfferSummary").getJSONObject("LowestNewPrice").getInt("Amount");
                for (int i = 1; i < itemsArray.length(); i++) {
                    JSONObject arrayItem = (JSONObject) itemsArray.get(i);
                    JSONObject offerSummary = arrayItem.getJSONObject("OfferSummary");
                    int price = Integer.MAX_VALUE;
                    try {
                        price = offerSummary.getJSONObject("LowestNewPrice").getInt("Amount");
                    } catch (JSONException e) {
                        Log.d("AmazonService", arrayItem.toString());
                    }
                    if (price < minPrice) {
                        minPriceItem = arrayItem;
                        minPrice = price;
                    }
                }

                JSONObject offerSummary = minPriceItem.getJSONObject("OfferSummary");
                String title = minPriceItem.getJSONObject("ItemAttributes").getString("Title");
                String price = offerSummary.getJSONObject("LowestNewPrice").getString("FormattedPrice");
                String thumbnailURL = minPriceItem.getJSONObject("SmallImage").getString("URL");
                String itemId = minPriceItem.getString("ASIN");
                Log.d("SignedRequestsHelper", minPriceItem.toString());
                response = new Model();
                response.setThumbnailImage(thumbnailURL);
                response.setItemUPC(getUpc());
                response.setSalePrice(price);
                response.setName(title);
                response.setItemId(itemId);
                response.setType(ServiceType.AMAZON);
                Log.d("SignedRequestsHelper", response.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return response;
    }

}
