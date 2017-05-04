package com.example.chen.barcodescanner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.webservice.ServiceType;

/**
 * Created by Adi on 4/14/2017.
 */

public class AmazonBag extends AppCompatActivity {
    ListView savedAmazonItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amazon_bag);
        savedAmazonItems = (ListView) findViewById(R.id.savedAmazonItems);
        RetrieveItemsTask retrieveFeedTask3 = new RetrieveItemsTask(getApplicationContext(), savedAmazonItems, ServiceType.AMAZON);
        retrieveFeedTask3.execute();
    }

}
