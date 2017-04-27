package com.example.chen.barcodescanner;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.example.webservice.ServiceType;

/**
 * Created by Adi on 4/14/2017.
 */

public class WalmartBag extends Activity {
    ListView savedWalmartItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walmart_bag);
        savedWalmartItems = (ListView) findViewById(R.id.savedWalmartItems);
        RetrieveItemsTask retrieveItemsTask = new RetrieveItemsTask(getApplicationContext(), savedWalmartItems, ServiceType.WALMART);
        retrieveItemsTask.execute();
    }
}
