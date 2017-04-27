package com.example.chen.barcodescanner;

/**
 * Created by chen on 2/7/2017.
 */


import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.helper.DatabaseHandler;
import com.example.helper.FromActivityEnum;
import com.example.webservice.Model;
import com.example.webservice.ServiceType;

import java.util.List;

public class RetrieveItemsTask extends AsyncTask<Void, Void, List<Model>> {

    private ListView listView;
    private Context context;
    private DatabaseHandler mydb;
    private ServiceType type;
    private ItemListAdapter listAdapter;

    private ImageView imageView;

    public RetrieveItemsTask(Context context, ListView listView, ServiceType type) {
        this.listView = listView;
        this.context = context;
        mydb = new DatabaseHandler(context);
        this.type = type;
    }


    @Override
    protected List<Model> doInBackground(Void... params) {
        List<Model> items = null;
        items = mydb.getAllProducts(type);
        return items;
    }

    @Override
    protected void onPostExecute(List<Model> models) {
        super.onPostExecute(models);

        ItemListAdapter adapter = new ItemListAdapter(context, R.layout.row, models);
        if (type == ServiceType.WALMART)
            adapter.setFromView(FromActivityEnum.WALMART_BAG);
        else
            adapter.setFromView(FromActivityEnum.AMAZON_BAG);
        listView.setAdapter(adapter);

    }

    /*public void Back(View view, Context context) {
        Intent intent = new Intent(context, AmazonBag.class);
    // startActivityForResult(Home.this, 0);
    }*/
}
